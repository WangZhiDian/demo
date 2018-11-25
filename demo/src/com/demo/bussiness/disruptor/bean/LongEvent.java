package com.demo.bussiness.disruptor.bean;

public class LongEvent
{
	private long value;
	
	private String name;

	public void setValue(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
