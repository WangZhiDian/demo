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
	// ��ִ������Ķ���
	private LinkedBlockingQueue<Runnable> task_queue = new LinkedBlockingQueue<Runnable>();
	// ִ��������̳߳�
	private static ThreadPoolExecutor executor = null;
	
	// ʹ�õ�ʵ��ģʽ
	private static class LazyHolder {
		private static final MultiTaskPool INSTANCE = new MultiTaskPool();
	}

	// ��ȡ��ǰʵ��
	public static final MultiTaskPool getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	private MultiTaskPool() {
		// �̳߳�
		executor = new ThreadPoolExecutor(2, 2, 10, TimeUnit.SECONDS, task_queue);
	}
	
	// ��ȡ��ǰ�̳߳ش�С
	public int getPoolSize()
	{
		return executor.getCorePoolSize();
	}
	
	// ��̬���õ�ǰ�̳߳ش�С
	public void setPoolSize(int corePoolSize)
	{
		executor.setCorePoolSize(corePoolSize);
		executor.setMaximumPoolSize(corePoolSize);
	}
	
	//�ر��̳߳�
	public static synchronized void stopThreads()
	{
		if(executor != null)
		{
			executor.shutdown();
			executor = null;
		}
	}
	
	/**
	 * ���ö��߳�ִ��future����
	 * @param itask ��Ҫ���ɽӿ�
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
