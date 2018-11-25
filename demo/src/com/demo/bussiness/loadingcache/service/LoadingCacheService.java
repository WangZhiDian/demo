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
 * 使用guava加载数据到缓存，数据加载到机器内存中，使用场景：
 * 1 你愿意消耗一些内存空间来提升速度。
 * 2 你预料到某些键会被查询一次以上。
 * 3 缓存中存放的数据总量不会超出内存容量。
 * （Guava Cache是单个应用运行时的本地缓存。它不把数据存放到文件或外部服务器。如果这不符合你的需求，请尝试Memcached这类工具）
 * 注：如果你不需要Cache中的特性，使用ConcurrentHashMap有更好的内存效率——但Cache的大多数特性都很难基于旧有的ConcurrentMap复制，甚至根本不可能做到。
 * @author wangdian05
 */
@Service
public class LoadingCacheService
{
	@Test
	public void loadCahce() throws ExecutionException, InterruptedException
	{
		LoadingCache<String, String> cache = CacheBuilder.newBuilder()
				// 设置并发级别为8，并发级别是指可以同时写缓存的线程数
				.concurrencyLevel(8)
				// 设置写缓存后8秒钟过期
				.expireAfterWrite(3, TimeUnit.SECONDS)
				// 设置缓存容器的初始容量为10
				.initialCapacity(10)
				// 设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
				.maximumSize(100)
				// 设置要统计缓存的命中率
				.recordStats()
				// 设置缓存的移除通知
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
			// 从缓存中得到数据，由于我们没有设置过缓存，所以需要通过CacheLoader加载缓存数据
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
			// 休眠一秒
			TimeUnit.SECONDS.sleep(1);
		}
		System.out.println("cache stats:");
		// 最后打印缓存的命中率等 情况
		System.out.println(cache.stats().toString());
	}
	
}
