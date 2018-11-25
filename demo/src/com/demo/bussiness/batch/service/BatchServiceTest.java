package com.demo.bussiness.batch.service;

import org.springframework.stereotype.Service;

import com.demo.bussiness.batch.disruptor.SimplyDisruptorBatch;
import com.demo.bussiness.batch.service.BonusHera;

@Service
public class BatchServiceTest extends SimplyDisruptorBatch<BonusHera>
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(BonusHera value)
	{
		System.out.println(value.getCname());
		
		System.out.println(value.getAddr());
		
	}
}
