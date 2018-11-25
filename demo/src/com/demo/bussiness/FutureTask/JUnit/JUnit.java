package com.demo.bussiness.FutureTask.JUnit;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.demo.bussiness.FutureTask.Callable.CallableItem;
import com.demo.bussiness.FutureTask.Callable.CallableManual;
import com.demo.bussiness.FutureTask.Callable.CallablePerson;
import com.demo.bussiness.FutureTask.Service.FutureServices;
import com.demo.bussiness.FutureTask.Service.MultiTaskPool;
import com.demo.bussiness.FutureTask.bean.Person;

public class JUnit
{
//	@Test
	public void testfuture() throws InterruptedException, ExecutionException, TimeoutException
	{
		ArrayList<CallableItem<String>> list = new ArrayList<CallableItem<String>>();
		ArrayList<String> array = new ArrayList<String>();
		array.add("testName1");
		array.add("testName2");
		array.add("testName3");
		array.add("testName4");
		array.add("testName5");
		array.add("testName6");
		array.add("testName7");
		
		for(int i = 0; i < array.size(); i++)
		{
			CallableManual manual =  new CallableManual(array.get(i));
			list.add(manual);
		}
		
		FutureServices service = new FutureServices();
		ArrayList<String> retlist = service.dealTaskT(list);
		
		for(int i = 0; i < retlist.size(); i ++)
		{
			System.out.println(retlist.get(i));
		}
		
	}
	
	/**
	 * 将一系列继承了callable方法的对象交给服务类
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws TimeoutException
	 */
	@Test
	public void testfuture2() throws InterruptedException, ExecutionException, TimeoutException
	{
		ArrayList<CallableItem<Person>> list = new ArrayList<CallableItem<Person>>();
		ArrayList<String> array = new ArrayList<String>();
		array.add("testName1");
		array.add("testName2");
		array.add("testName3");
		array.add("testName4");
		array.add("testName5");
		array.add("testName6");
		array.add("testName7");
		
		for(int i = 0; i < array.size(); i++)
		{
			Person p = new Person(array.get(i));
			CallablePerson manual =  new CallablePerson(p);
			list.add(manual);
		}
		
		FutureServices service = new FutureServices();
		ArrayList<Person> retlist = service.dealTaskT(list);
		
		Thread.sleep(4000);
		for(int i = 0; i < retlist.size(); i ++)
		{
			System.out.println(retlist.get(i).getName());
		}
	}
	
	/**
	 * 测试多线程池运行任务
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
//	@Test
	public void testMultiTaskPool() throws InterruptedException, ExecutionException
	{
		MultiTask task = null;
		ArrayList<MultiTask> list = new ArrayList<MultiTask>(); 
		for(int i = 0; i < 5; i++)
		{
			task = new MultiTask(new Person("test" + i));
			list.add(task);
		}
		
		FutureTask future = new FutureTask(null);
		
		Person p = null;
		for(MultiTask item : list)
		{
			p = MultiTaskPool.getInstance().executeJob(item);
			System.out.println(p.getName());
		}
		
	}
}
