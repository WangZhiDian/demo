package com.demo.bussiness.ThreadPool.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 生成可控制的线程池工具
 * @author wangdian05
 */
public class ThreadPool
{
	private static ThreadPoolExecutor executor = null; // 线程池

	private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(); // 队列
	
	private static volatile boolean stop_flag = true; // 停止标志位
	
	// 使用缺失参数启动
	public synchronized void startThreads()
	{
		//初始化，缺省5个线程，最大扩展到10
		this.startThreads(5, 10);
	}
	
	// 启动线程
	public synchronized void startThreads(int min_threads, int max_threads)
	{
		if (stop_flag)
		{
			stop_flag = false;
			synchronized(ThreadPool.class)
			{
				if(executor == null)
					executor = new ThreadPoolExecutor(min_threads, max_threads, 10, TimeUnit.SECONDS, queue);
			}
			System.out.println("start thread pool succeed");
		}else
		{
			System.out.println("alrdady exist the thread pool");
		}
	}
	
	//获取线程实例,如果没有,创建默认线程池,最小5个线程,最大10个线程
	public static ThreadPoolExecutor instance()
	{
		if(executor == null)
		{
			synchronized(ThreadPool.class)
			{
				if(executor == null)
					executor = new ThreadPoolExecutor(5, 10, 10, TimeUnit.SECONDS, queue);
			}
		}
		return executor;
	}
	
	//关闭线程池
	public static synchronized void stopThreads()
	{
		if(executor != null)
		{
			executor.shutdown();
			executor = null;
		}
		stop_flag = true;
		DateFormat d5 = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL); // 显示日期，周，时间（精确到秒）
		System.out.println("Stop the thread pool at: " + d5.format(new Date()));
	}
	
}
