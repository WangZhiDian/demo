package com.demo.bussiness.loadingcache.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

/**
 * ʹ��guava�������ݵ����棬���ݼ��ص������ڴ��У�ʹ�ó�����
 * 1 ��Ը������һЩ�ڴ�ռ��������ٶȡ�
 * 2 ��Ԥ�ϵ�ĳЩ���ᱻ��ѯһ�����ϡ�
 * 3 �����д�ŵ������������ᳬ���ڴ�������
 * ��Guava Cache�ǵ���Ӧ������ʱ�ı��ػ��档���������ݴ�ŵ��ļ����ⲿ������������ⲻ������������볢��Memcached���๤�ߣ�
 * ע������㲻��ҪCache�е����ԣ�ʹ��ConcurrentHashMap�и��õ��ڴ�Ч�ʡ�����Cache�Ĵ�������Զ����ѻ��ھ��е�ConcurrentMap���ƣ���������������������
 * @author wangdian05
 */
@Service
public class LoadingCacheService
{
	@Test
	public void loadCahce() throws ExecutionException, InterruptedException
	{
		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
				// ���ò�������Ϊ8������������ָ����ͬʱд������߳���
				.concurrencyLevel(8)
				// ����д�����8���ӹ���
				.expireAfterWrite(3, TimeUnit.SECONDS)
				// ���û��������ĳ�ʼ����Ϊ10
				.initialCapacity(10)
				// ���û����������Ϊ100������100֮��ͻᰴ��LRU�������ʹ���㷨���Ƴ�������
				.maximumSize(100)
				// ����Ҫͳ�ƻ����������
				.recordStats()
				// ���û�����Ƴ�֪ͨ
				.removalListener(new RemovalListener<Object, Object>()
				{
					@Override
					public void onRemoval(com.google.common.cache.RemovalNotification<Object, Object> notification)
					{

						System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
					};
				})
				.build(new CacheLoader<String, String>()
				{
					@Override
					public String load(String key) throws Exception {
						
						
						return "aa";
					}

//					@Override
//					public Student load(Integer key) throws Exception
//					{
//						System.out.println("load student " + key);
//						Student student = new Student();
//						student.setId(key);
//						student.setName("name" + key);
//						return student;
//
//					}
					
				});
				
		for (int i = 0; i < 10; i++)
		{
			// �ӻ����еõ����ݣ���������û�����ù����棬������Ҫͨ��CacheLoader���ػ�������
			String t1 = cache.get("b");
			System.out.println("t1:" + t1 + " | " + i);
			
			if(i == 2)
				cache.put("wang", "dian----");
			if(i == 4)
				cache.put("wang", "zhidian======");
			String t3 = cache.get("wang");
			System.out.println("t3:" + t3 + " | " + i);
			
			if(i == 2)
				cache.put("liang", "shuwei");
			
			
			if(i == 3)
			{
				System.out.println("liang:" + cache.get("liang"));
			}
			
			System.out.println();
			// ����һ��
			TimeUnit.SECONDS.sleep(1);
		}
		System.out.println("cache stats:");
		// ����ӡ����������ʵ� ���
		System.out.println(cache.stats().toString());
	}
	
}
