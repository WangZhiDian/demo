package com.demo.bussiness.disruptor.beanFactory;

import com.demo.bussiness.disruptor.bean.LongEvent;
import com.lmax.disruptor.EventFactory;

/**
 * 处理时间工厂
 * @author wangdian05
 *
 */
public class LongEventFactory implements EventFactory<LongEvent>{

	@Override
	public LongEvent newInstance() {
		// TODO Auto-generated method stub
		
		return new LongEvent();
	}
	

}
