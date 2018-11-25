package com.demo.bussiness.batch.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.demo.bussiness.batch.disruptor.SimplyDisruptorBatch;
import com.demo.bussiness.batch.service.BonusHera;
import com.demo.bussiness.mybatisLocal.dao.SuitMapper;

/**
 * test SimpleDisruptor
 * 自定义处理类,集成handle方法,利用一步工具处理任务逻辑.
 */
@Service
public class SimplyDidruptorBatchServiceTest extends SimplyDisruptorBatch<BonusHera>
{
	@Resource
	SuitMapper suitMapper;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(BonusHera value)
	{
		System.out.println(value.getCname());
		String name = suitMapper.getSuitName();
		System.out.println(value.getAddr() + "  " + name);
		
	}
}
