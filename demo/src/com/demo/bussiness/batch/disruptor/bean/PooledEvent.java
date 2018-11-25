package com.demo.bussiness.batch.disruptor.bean;

public class PooledEvent
{
	private EventPerformer<?> performer;
	private Object value;
	public EventPerformer<?> getPerformer()
	{
		return performer;
	}
	public void setPerformer(EventPerformer<?> performer)
	{
		this.performer = performer;
	}
	public Object getValue()
	{
		return value;
	}
	public void setValue(Object value)
	{
		this.value = value;
	}
	
	
}
