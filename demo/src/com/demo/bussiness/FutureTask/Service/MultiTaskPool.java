package com.demo.bussiness.FutureTask.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiTaskPool
{
	// 待执行任务的对列
	private LinkedBlockingQueue<Runnable> task_queue = new LinkedBlockingQueue<Runnable>();
	// 执行任务的线程池
	private static ThreadPoolExecutor executor = null;
	
	// 使用单实例模式
	private static class LazyHolder {
		private static final MultiTaskPool INSTANCE = new MultiTaskPool();
	}

	// 获取当前实例
	public static final MultiTaskPool getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private MultiTaskPool() {
		// 线程池
		executor = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS, task_queue);
	}
	
	// 获取当前线程池大小
	public int getPoolSize()
	{
		return executor.getCorePoolSize();
	}
	
	// 动态设置当前线程池大小
	public void setPoolSize(int corePoolSize)
	{
		executor.setCorePoolSize(corePoolSize);
		executor.setMaximumPoolSize(corePoolSize);
	}
	
	//关闭线程池
	public static synchronized void stopThreads()
	{
		if(executor != null)
		{
			executor.shutdown();
			executor = null;
		}
	}
	
	/**
	 * 利用多线程执行future任务
	 * @param itask 需要集成接口
	 * @return <T>
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public <T> T executeJob(final ITask<T> itask) throws InterruptedException, ExecutionException
	{
		
		Future<T> future = executor.submit(new Callable<T>(){
			public T call() throws Exception {
				return itask.execute_task();
			}
		});
		
		return future.get();
	}
	
	public <T> void executeJob2(FutureTask<T> T)
	{
		executor.execute(T);
	}
	
}
