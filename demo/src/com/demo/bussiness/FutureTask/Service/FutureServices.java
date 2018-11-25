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
 * ͨ�����̴߳���������,���������ý��
 */
public class FutureServices 
{
	private static final int timeout = 2;
	
	/**
	 * ����һ���б����񣬷����б�
	 * @param array
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	public <T> ArrayList<T> dealTaskT(ArrayList<CallableItem<T>> array) throws InterruptedException, ExecutionException, TimeoutException
	{
		//��ȡ������task ���б�
		ArrayList<FutureTask<T>> tasklist = new ArrayList<FutureTask<T>>();
		
		//��ȡtask������б�
		ArrayList<T> list = new ArrayList<T>();
		
		//��ȡ������
		Iterator<CallableItem<T>> ir = array.iterator();
		CallableItem<T> task;
		while(ir.hasNext())
		{
			CallableItem<T> item = ir.next();
			task = (CallableItem<T>) (item);
			FutureTask<T> futureTask = (FutureTask<T>)ThreadPool.instance().submit(task);
			tasklist.add(futureTask);
		}
		
		//��ȡ������
		for(FutureTask<T> taskitem : tasklist)
		{
			try
			{
				//��������δ�����꣬���ش���
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
	 * ִ�е�������
	 * @return
	 */
	public <T> FutureTask<T> execute_task(CallableItem<T> item)
	{
		FutureTask<T> task = (FutureTask<T>)ThreadPool.instance().submit(item);
		return task;
	}
	
}
