package com.demo.bussiness.batch.disruptor.bean;

public class ValueEvent<T>
{
	private T value;

	public T getValue()
	{
		return value;
	}

	public void setValue(T value)
	{
		this.value = value;
	}

	@Override
	public String toString()
	{
		return "ValueEvent [" + (value != null ? "value=" + value : "") + "]";
	}

}
