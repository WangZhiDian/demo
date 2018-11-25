package com.demo.bussiness.FutureTask.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.demo.bussiness.FutureTask.Callable.CallableItem;
import com.demo.bussiness.ThreadPool.Service.ThreadPool;

/*
 * 通过多线程处理处理任务,返回任务获得结果
 */
public class FutureServices 
{
	private static final int timeout = 2;
	
	/**
	 * 处理一个列表任务，返回列表
	 * @param array
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public <T> ArrayList<T> dealTaskT(ArrayList<CallableItem<T>> array) throws InterruptedException, ExecutionException, TimeoutException
	{
		//获取数字中task 的列表
		ArrayList<FutureTask<T>> tasklist = new ArrayList<FutureTask<T>>();
		
		//获取task结果的列表
		ArrayList<T> list = new ArrayList<T>();
		
		//获取迭代器
		Iterator<CallableItem<T>> ir = array.iterator();
		CallableItem<T> task;
		while(ir.hasNext())
		{
			CallableItem<T> item = ir.next();
			task = (CallableItem<T>) (item);
			FutureTask<T> futureTask = (FutureTask<T>)ThreadPool.instance().submit(task);
			tasklist.add(futureTask);
		}
		
		//获取处理结果
		for(FutureTask<T> taskitem : tasklist)
		{
			try
			{
				//超过两秒未处理完，返回错误
				T ret = taskitem.get(timeout, TimeUnit.SECONDS);
				list.add(ret);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		ThreadPool.stopThreads();
		
		return list;
	}
	
	/**
	 * 执行单个任务
	 * @return
	 */
	public <T> FutureTask<T> execute_task(CallableItem<T> item)
	{
		FutureTask<T> task = (FutureTask<T>)ThreadPool.instance().submit(item);
		return task;
	}
	
}
