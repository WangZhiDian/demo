/** 
 * @author 王文超 
 * @time 2016年3月23日 下午2:57:59  
 * 类说明 
 */
package com.demo.bussiness.batch.disruptor;

import com.demo.bussiness.batch.disruptor.bean.ValueAndResultEvent;

/**
 * @author 王文超
 * @Description 基于discuptor做的批处理框架，提供了对结果的处理
 * @time 2016年3月23日 下午2:57:59
 */
public abstract class ProcessResultDisruptorBatch<T, V> extends
		AsyncDisruptorBatch<ValueAndResultEvent<T, V>, T>
{
	public ProcessResultDisruptorBatch()
	{
		super(8192,4,4,0);
	}

	public ProcessResultDisruptorBatch(int bufferSize, int firstParalleSize,
			int secondParalleSize, int thirdParalleSize)
	{
		super(bufferSize, firstParalleSize, secondParalleSize, thirdParalleSize);
	}

	public ProcessResultDisruptorBatch(int bufferSize, int firstParalleSize)
	{
		super(bufferSize, firstParalleSize);
	}

	public ProcessResultDisruptorBatch(int bufferSize)
	{
		super(bufferSize);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ValueAndResultEvent<T, V> newInstance()
	{
		return new ValueAndResultEvent<T, V>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onFirstEvent(ValueAndResultEvent<T, V> event) throws Exception
	{
		T value = event.getValue();
		V result = handleData(value);
		event.setResult(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSecondEvent(ValueAndResultEvent<T, V> event) throws Exception
	{
		V result = event.getResult();
		handleResult(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void translateTo(ValueAndResultEvent<T, V> event, long sequence,
			T value)
	{
		event.setValue(value);
	}

	public abstract V handleData(T value);

	public abstract void handleResult(V result);
}
