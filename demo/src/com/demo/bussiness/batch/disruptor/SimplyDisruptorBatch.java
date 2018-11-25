package com.demo.bussiness.batch.disruptor;

import com.demo.bussiness.batch.disruptor.bean.EventPerformer;
import com.demo.bussiness.batch.disruptor.bean.ValueEvent;

/** 
 * @author 王文超
 * @Description 对disruptor的简单进一步封装,没有执行顺序，没有结果处理 ，只暴露要进行具体逻辑的抽象方法handle，方便使用。
 * <pre>
 * 使用方法：1、继承此类，复写handle方法，然后调用add方法。2、实现EventPerformer接口，在构造SimplyDisruptorBatch的时候，作为构造参数传进去。然后调用add方法。
 * </pre>
 * @time 2016年3月23日 下午4:59:53 
 * @param <T>
 */
public class SimplyDisruptorBatch<T> extends AsyncDisruptorBatch<ValueEvent<T>,T>
{
	private EventPerformer<T> eventPerformer;
	public SimplyDisruptorBatch()
	{
		super();
	}
	public SimplyDisruptorBatch(EventPerformer<T> eventPerformer)
	{
		super();
		this.eventPerformer=eventPerformer;
	}
	public SimplyDisruptorBatch(int bufferSize, int firstParalleSize)
	{
		super(bufferSize, firstParalleSize);
	}
	public SimplyDisruptorBatch(int bufferSize, int firstParalleSize,EventPerformer<T> eventPerformer)
	{
		super(bufferSize, firstParalleSize);
		this.eventPerformer=eventPerformer;
	}

	@Override
	public ValueEvent<T> newInstance()
	{
		return new ValueEvent<T>();
	}
	@Override
	public void translateTo(ValueEvent<T> event, long sequence, T value)
	{
		event.setValue(value);
	}
	@Override
	public void onFirstEvent(ValueEvent<T> event) throws Exception
	{
		T value = event.getValue();
		handle(value);
	}
	/**
	 * 具体的业务实现
	 */
	public void handle(T value){
		if (eventPerformer!=null)
		{
			eventPerformer.handle(value);
		}
	}
}
