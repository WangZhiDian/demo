package com.demo.bussiness.cacheable.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 使用Spring3 注解使用服务器存储缓存数据 @Cacheable
 * 针对于方法，对函数返回数据，根据参数为key做缓存：key value condition 三个参数
 * 使用配置applicationContext-cache.xml
 * @author wangdian05
 *
 */
@Component
public class CacheAbleService
{
	@Autowired
	private CacheTest test;
	
	
	public void testCacheable()
	{
//		CacheTest test = new CacheTest();
		String name = "wang";
//		String sex = getSexByName("1", name);
		
		System.out.println("name:" + test.getSexByName("1", name).getName());
		
		name = "liang";
//		sex = getSexByName("1", name);
		System.out.println("name:" + test.getSexByName("1", name).getName());
		
		name = "wang";
//		sex = getSexByName("1", name);
		System.out.println("name:" + test.getSexByName("1", name).getName());
		
		name = "wang";
//		sex = getSexByName("2", name);
		System.out.println("name:" + test.getSexByName("1", name).getName());
		
//		assert(true);
	}
}
