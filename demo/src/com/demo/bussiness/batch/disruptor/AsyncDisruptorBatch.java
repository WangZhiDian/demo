package com.demo.bussiness.batch.disruptor;

import java.util.Arrays;
import java.util.Collection;

import com.demo.bussiness.batch.TaskService;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 使用disruptor进行异步处理的代码 使用后台线程进行异步处理，队列已满的时候采用blocking策略,发生异常时使用slf4j记录日志
 * 添加了任务链，后一批批消费者会等前一批消费者处理完毕后进行处理
 * 任务链如下：
 * +----+    +-----+    +-----+    +-----+
 * | P  |--->| EP1 |--->| EP2 |--->| EP3 |
 * +----+    +-----+    +-----+    +-----+
 * P  -  生产者
 * EP1 - 第一批消费者
 * EP2 - 第二批消费者
 * EP3 - 第三批消费者
 */
public abstract class AsyncDisruptorBatch<T, T2> implements EventFactory<T>,
		EventTranslatorOneArg<T, T2>,TaskService
{

	/**
	 * ringBuffer的默认长度
	 */
	private int bufferSize = 8192;

	/**
	 * 第一批消费者的数量,消费者数量建议与cpu的数量同步即可
	 */
	private int firstParalleSize = 4;
	/**
	 * 第二批消费者的数量，不用时候设为0，提高性能
	 */
	private int secondParalleSize = 0;
	/**
	 * 第三批消费者的数量，不用时候设为0，提高性能
	 */
	private int thirdParalleSize = 0;

	public AsyncDisruptorBatch()
	{
		start();
	}

	public AsyncDisruptorBatch(int bufferSize)
	{
		this.bufferSize = bufferSize;
		start();
	}

	public AsyncDisruptorBatch(int bufferSize, int firstParalleSize)
	{
		this.bufferSize = bufferSize;
		this.firstParalleSize = firstParalleSize;
		start();
	}

	public AsyncDisruptorBatch(int bufferSize, int firstParalleSize,
			int secondParalleSize, int thirdParalleSize)
	{
		this.bufferSize = bufferSize;
		this.firstParalleSize = firstParalleSize;
		this.secondParalleSize = secondParalleSize;
		this.thirdParalleSize = thirdParalleSize;
		start();
	}

	private Disruptor<T> disruptor ;

	private RingBuffer<T> ringBuffer ;

	public RingBuffer<T> getRingBuffer()
	{
		if (ringBuffer!=null)
		{
			return ringBuffer;
		}
		throw new RuntimeException("disruptor has not started");
	}

	/**
	 * 事件工厂的实现
	 */
	public abstract T newInstance();

	/**
	 * 第一批消费者的任务
	 * 
	 * @throws Exception
	 */
	public abstract void onFirstEvent(T event) throws Exception;

	/**
	 * 第二批消费者的任务
	 * 
	 * @throws Exception
	 */
	public void onSecondEvent(T event) throws Exception
	{
	};

	/**
	 * 第三批消费者的任务
	 * 
	 * @throws Exception
	 */
	public void onThirdEvent(T event) throws Exception
	{
	}

	/**
	 * 生产者提交的数据与消费者的需要的数据之间的关系
	 */
	public abstract void translateTo(T event, long sequence, T2 value);

	/**
	 * @return 提交成功返回true，因阻塞未提交成功返回false
	 */
	public boolean add(T2 value)
	{
		return ringBuffer.tryPublishEvent(this, value);
	}

	/**
	 * 批量提交，values的长度不能超过bufferSize
	 * 
	 * @return 提交成功返回true，因阻塞未提交成功返回false
	 */
	public boolean add(T2[] values)
	{
		boolean b = true;
		if(values.length > bufferSize / 8) {
			T2[] firstHalf = Arrays.copyOfRange(values, 0, values.length / 2);
			b = b && ringBuffer.tryPublishEvents(this,firstHalf);
			T2[] secondHalf = Arrays.copyOfRange(values, values.length / 2, values.length);
			b = b && ringBuffer.tryPublishEvents(this,secondHalf);
			return b;
		}
		else return ringBuffer.tryPublishEvents(this, values);
	}

	/**
	 * 批量提交，list的长度不能超过bufferSize
	 * 
	 * @return 提交成功返回true，因阻塞未提交成功返回false
	 */
	public boolean add(Collection<T2> values)
	{
		@SuppressWarnings("unchecked")
		T2[] val = (T2[]) values.toArray();
		return add(val);
	}

	/**
	 * 启动disruptor
	 * Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer如何等待新事件，这是策略模式的应用。
	 * Disruptor 提供了多个 WaitStrategy的实现，每种策略都具有不同性能和优缺点，
	 * 根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM的配置参数，能够实现不同的性能提升。
	 * 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
	 * BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
	 * SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU
	 * 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景； 
	 * YieldingWaitStrategy的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
	 */
	@Override
	public void start()
	{
		disruptor = new Disruptor<T>(this, bufferSize,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
				new BlockingWaitStrategy());

		disruptor.setDefaultExceptionHandler(new Slf4jExceptionHandler<T>());

		allocatingTask();

		ringBuffer = disruptor.start();
	}

	private class firstWorkTask implements WorkHandler<T>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onEvent(T event) throws Exception
		{
			onFirstEvent(event);
		}

	}

	private class SecondWorkTask implements WorkHandler<T>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onEvent(T event) throws Exception
		{
			onSecondEvent(event);

		}
	}

	private class ThirdWorkTask implements WorkHandler<T>
	{
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void onEvent(T event) throws Exception
		{
			onThirdEvent(event);
		}
	}

	/**
	 * 指定任务策略，默认只有三个任务链，如果有更复杂的任务链请覆盖此方法
	 * 
	 * @time 2016年3月23日 上午10:35:29
	 */
	@SuppressWarnings("unchecked")
	public void allocatingTask()
	{
		if (firstParalleSize <= 0)
			throw new RuntimeException("The firstParalleSize can't less than 0!");
		WorkHandler<T>[] firstWorks = new WorkHandler[firstParalleSize];
		for (int i = 0; i < firstParalleSize; i++)
		{
			firstWorks[i] = new firstWorkTask();
		}
		EventHandlerGroup<T> workerPool = disruptor
				.handleEventsWithWorkerPool(firstWorks);
		if (secondParalleSize > 0)
		{
			WorkHandler<T>[] secondWorks = new WorkHandler[secondParalleSize];
			for (int i = 0; i < secondParalleSize; i++)
			{
				secondWorks[i] = new SecondWorkTask();
			}
			workerPool = workerPool.thenHandleEventsWithWorkerPool(secondWorks);
		}
		if (thirdParalleSize > 0)
		{
			WorkHandler<T>[] thirdWorks = new WorkHandler[thirdParalleSize];
			for (int i = 0; i < thirdParalleSize; i++)
			{
				thirdWorks[i] = new ThirdWorkTask();
			}
			workerPool.thenHandleEventsWithWorkerPool(thirdWorks);
		}
	}
	/**
	 * 上一批数据的完成情况
	 * 
	 * @return true全部完成 false没有全部完成
	 * @time 2016年3月30日 上午11:52:12
	 */
	private boolean handleCompletion()
	{
		long cursor = this.getRingBuffer().getCursor();// 当前数据光标位置
		long minimumGatingSequence = this.getRingBuffer()
				.getMinimumGatingSequence();// 最慢的消费者的位置
		return minimumGatingSequence == cursor;
	}
	/**
	 * 调用此方法，等待线程池中的所有任务执行完毕
	 * @time 2016年5月23日 下午2:22:55 
	 */
	public void await()
	{
		while (!handleCompletion())
		{
			try
			{
				Thread.sleep(100);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	/**
	 * 关闭disruptor，会一直等所有任务执行完成后关闭
	 * 
	 * @time 2016年3月23日 上午11:11:33
	 */
	@Override
	public void stop()
	{
		if (disruptor!=null)
		{
			disruptor.shutdown();
		}
		disruptor=null;
		ringBuffer=null;
	}
	@Override
	public boolean isRunning()
	{
		return ringBuffer!=null;
	}

}