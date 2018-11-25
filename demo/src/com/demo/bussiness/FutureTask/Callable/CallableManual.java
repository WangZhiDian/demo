package com.demo.bussiness.FutureTask.Callable;

import java.util.concurrent.Callable;

public class CallableManual extends CallableItem<String> implements Callable<String>
{

	public CallableManual(String input)
	{
		super(input);
		
	}

	@Override
	public String call() throws Exception
	{
		String name = super.obj;
		return "name:" + name;
	}
	
}
