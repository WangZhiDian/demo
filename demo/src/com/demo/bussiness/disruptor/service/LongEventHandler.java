package com.demo.bussiness.disruptor.service;

import com.demo.bussiness.disruptor.bean.LongEvent;
import com.lmax.disruptor.EventHandler;

/**
 * 处理任务的具体实现
 * @author wangdian05
 *
 */
public class LongEventHandler implements EventHandler<LongEvent>{

	@Override
	public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("event.name:" + event.getName());
		System.out.println("event:" + event);
	}

}
