package com.demo.bussiness.FutureTask.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.demo.bussiness.FutureTask.Callable.CallableTask;
import com.demo.bussiness.ThreadPool.Service.ThreadPool;

/**
 * Future ����callable�ӿ�,��ȡ�߳�ִ�еĽ��,�ж������Ƿ�ɹ�,�ж�����ִ��
 * @author wangdian05
 */
public class FutureService
{
	
	@Test
	public void TestFuture() throws InterruptedException, ExecutionException, TimeoutException
	{
		ArrayList<String> array = new ArrayList<String>();
		array.add("testName1");
		array.add("testName2");
		array.add("testName3");
		array.add("testName4");
		array.add("testName5");
		array.add("testName6");
		array.add("testName7");
		dealTask(array);
		
	}
	
	
	public void dealTask(ArrayList<String> array) throws InterruptedException, ExecutionException, TimeoutException
	{
		ArrayList<FutureTask<String>> tasklist = new ArrayList<FutureTask<String>>();
		Iterator<String> ir = array.iterator();
		while(ir.hasNext())
		{
			String name = ir.next();
			CallableTask task = new CallableTask(name);
			FutureTask<String> futureTask = (FutureTask<String>)ThreadPool.instance().submit(task);
			tasklist.add(futureTask);
		}
		
		//��ȡ������
		for(FutureTask<String> taskitem : tasklist)
		{
			try
			{
				String retName = taskitem.get(2, TimeUnit.SECONDS);
				if(retName != null)
					System.out.println("=" + retName);
				else
					System.out.println("NULL");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		ThreadPool.stopThreads();
	}
	
	
	
}
