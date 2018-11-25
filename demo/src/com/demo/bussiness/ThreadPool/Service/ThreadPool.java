package com.demo.bussiness.ThreadPool.Service;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ���ɿɿ��Ƶ��̳߳ع���
 * @author wangdian05
 */
public class ThreadPool
{
	private static ThreadPoolExecutor executor = null; // �̳߳�

	private static LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>(); // ����
	
	private static volatile boolean stop_flag = true; // ֹͣ��־λ
	
	// ʹ��ȱʧ��������
	public synchronized void startThreads()
	{
		//��ʼ����ȱʡ5���̣߳������չ��10
		this.startThreads(5, 10);
	}
	
	// �����߳�
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
	
	//��ȡ�߳�ʵ��,���û��,����Ĭ���̳߳�,��С5���߳�,���10���߳�
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
	
	//�ر��̳߳�
	public static synchronized void stopThreads()
	{
		if(executor != null)
		{
			executor.shutdown();
			executor = null;
		}
		stop_flag = true;
		DateFormat d5 = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL); // ��ʾ���ڣ��ܣ�ʱ�䣨��ȷ���룩
		System.out.println("Stop the thread pool at: " + d5.format(new Date()));
	}
	
}
