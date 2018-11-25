package com.demo.bussiness.redis.service;

import org.junit.Test;

import com.demo.common.redis.ShareJedisManager;
import com.demo.common.redis.TwemproxyJedisManager;

public class RedisService
{
	//ceshi TwemproxyJedisManager
	private static final TwemproxyJedisManager demoRedisMgr = new TwemproxyJedisManager("demo", "test");
	// ceshi ShareJedisManager
	private static final ShareJedisManager redisManager = new ShareJedisManager("demo","test2");
	
	private static final ShareJedisManager redisManager2 = new ShareJedisManager("demo","test");
	
	public void service()
	{
		String str = demoRedisMgr.getString("test1");
		if(null == str || "".equals(str))
		{
			demoRedisMgr.setString("test1", "testRedis");
			System.out.println("set redis");
		}else{
			System.out.println("from redis str:" + str);
		}
	}
	
	public void serviceJedis()
	{
		String str = redisManager.getString("test2");
		if(null == str || "".equals(str))
		{
			redisManager.setString("test2", "testRedis2", 55);
			System.out.println("set redis2");
		}else{
			System.out.println("from redis jedis str:" + str);
		}
	}
	
	public void testSet()
	{
		//两种获取缓存方式的key组织形式一样.
		String str = redisManager2.getString("test1");
		System.out.println("str:" + str);
	}
	
	@Test
	public void test()
	{
//		service();
//		serviceJedis();
		testSet();
	}
}
