package com.demo.bussiness.batch.disruptor;


import com.demo.bussiness.batch.disruptor.bean.EventPerformer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorTwoArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

/**
 * 公共disruptor池的抽象类
 */
public abstract class AbstractPooledDisruptorBatch<T> implements
		EventFactory<T>, EventTranslatorTwoArg<T, Object, EventPerformer<?>>,
		WorkHandler<T>
{
	/**
	 * ringBuffer的长度
	 */
	private final int bufferSize;

	/**
	 * 消费者的数量
	 */
	private final int paralleSize;

	public AbstractPooledDisruptorBatch(int bufferSize, int paralleSize)
	{
		this.bufferSize = bufferSize;
		this.paralleSize = paralleSize;
	}

	private Disruptor<T> disruptor;

	private RingBuffer<T> ringBuffer = null;

	/**
	 * 事件工厂的实现
	 */
	public abstract T newInstance();

	/**
	 * 消费者的任务
	 */
	public abstract void onEvent(T event) throws Exception;

	/**
	 * 生产者提交的数据、提交的方法与消费者之前的转换关系
	 */
	public abstract void translateTo(T event, long sequence, Object value,
			EventPerformer<?> performer);

	/**
	 * 提交数据和对数据操作的方法
	 * 
	 * @param value
	 *            实现业务的数据
	 * @param performer
	 *            具体实现业务的接口实现类
	 * @return 提交成功返回true，因阻塞未提交成功返回false
	 */
	public <V> boolean submit(V value, EventPerformer<V> performer)
	{
		return ringBuffer.tryPublishEvent(this, value, performer);
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
	public void start()
	{
		disruptor = new Disruptor<T>(this, bufferSize,
				DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
				new BlockingWaitStrategy());

		disruptor.setDefaultExceptionHandler(new Slf4jExceptionHandler<T>());

		allocatingTask();

		ringBuffer = disruptor.start();
	}

	/**
	 * 指定任务策略
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void allocatingTask()
	{
		if (paralleSize <= 0)
			throw new RuntimeException("消费者数量不能小于0！");
		WorkHandler<T>[] firstWorks = new WorkHandler[paralleSize];
		for (int i = 0; i < paralleSize; i++)
		{
			firstWorks[i] = this;
		}
		disruptor.handleEventsWithWorkerPool(firstWorks);
	}

	/**
	 * 关闭disruptor，会一直等所有任务执行完成后关闭
	 * 
	 */
	public void stop()
	{
		disruptor.shutdown();
	}

}
