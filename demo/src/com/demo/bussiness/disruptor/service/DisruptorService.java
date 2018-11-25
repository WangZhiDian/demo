package com.demo.bussiness.disruptor.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.demo.bussiness.disruptor.bean.LongEvent;
import com.demo.bussiness.disruptor.beanFactory.LongEventFactory;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 测试disruptor
 * @author wangdian05
 *
 */
@Service
public class DisruptorService
{
	Disruptor<LongEvent> disruptor = null;
	ExecutorService executor = null;
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public String dealDisruptor()
	{
		String ret = "fail";
		try
		{
			EventFactory<LongEvent> eventFactory = new LongEventFactory();
			executor = Executors.newSingleThreadExecutor();
//			int ringBufferSize = 1024 * 1024;
			int ringBufferSize = 4;
	//		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
			this.disruptor = new Disruptor<LongEvent>(eventFactory, ringBufferSize, executor, ProducerType.SINGLE, new YieldingWaitStrategy());
			
			EventHandler<LongEvent> enentHandle = new LongEventHandler();
			disruptor.handleEventsWith(enentHandle);
			
			disruptor.start();
			
			ret = "success";
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public String publishAnEvent(String name)
	{
		String ret = "fail";
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();
		System.out.println(sequence);
		try
		{
			LongEvent event = ringBuffer.get(sequence);
			long data = 123423; // for test
			event.setValue(data);
			event.setName(name);
			ret = "success";
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			ringBuffer.publish(sequence);
		}
		return ret;
	}
	
	//-----------------------------------------------------------------------------
	static class Translator implements EventTranslatorOneArg<LongEvent, String>{
		
		@Override
		public void translateTo(LongEvent event, long sequence, String name) {
			// TODO Auto-generated method stub
			event.setName(name);
		}
	}
	
	public static Translator TRANSLATOR = new Translator();
	
	public void publishEvent2(String name)
	{
	    // 发布事件；
	    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
	    long data = 124325;//获取要通过事件传递的业务数据；
	    ringBuffer.publishEvent(TRANSLATOR, name);
	}
	
}
