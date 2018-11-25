package com.demo.bussiness.FutureTask.Callable;

import java.util.concurrent.Callable;

/**
 * 执行任务的task定义
 * @author wangdian05
 *
 */
public class CallableTask implements Callable<String>
{
	
	private String name;
	
	public CallableTask(String input)
	{
		this.name = input;
	}
	

	@Override
	public String call() throws Exception
	{
		String ret = "";
		if("testName3".equals(name))
		{
			Thread.sleep(3000);
		}

		ret = "My Name is:" + this.name;
		System.out.println("this is in callable:" + name);
		return ret;
	}

}
