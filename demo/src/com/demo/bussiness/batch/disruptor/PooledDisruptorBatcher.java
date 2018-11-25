package com.demo.bussiness.batch.disruptor;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import com.demo.bussiness.batch.disruptor.bean.EventPerformer;
import com.demo.bussiness.batch.disruptor.bean.PooledEvent;


/**
 * 所有异步任务会共用一个线程池。统一管理，简化编程。
 * <p>
 * 使用方法：调用submit(V value,EventPerformer<V> performer)方法即可
 * <p>
 */
@Service
public class PooledDisruptorBatcher extends
		AbstractPooledDisruptorBatch<PooledEvent> implements InitializingBean
{

	public PooledDisruptorBatcher()
	{
		super(65536, 12);
	}

	@Override
	public void afterPropertiesSet() throws Exception
	{
		start();
	}

	@Override
	public PooledEvent newInstance()
	{
		return new PooledEvent();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onEvent(PooledEvent event) throws Exception
	{
		EventPerformer performer = event.getPerformer();
		Object value = event.getValue();
		performer.handle(value);
	}

	@Override
	public void translateTo(PooledEvent event, long sequence, Object value,
			EventPerformer<?> performer)
	{
		event.setValue(value);
		event.setPerformer(performer);
	}

}
