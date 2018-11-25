package com.demo.bussiness.cacheable.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.demo.bussiness.cacheable.bean.User;

@Component
//@Cacheable(value="CacheTest")
public class CacheTest
{
	@Cacheable(value="CacheTest",key="#name")
	public User getSexByName(String id, String name)
	{
		User user = new User();
		if(name.equals("wang"))
			user.setName("male1");
		else if(name.equals("liang"))
			user.setName("male2");
		else if(name.equals("li"))
			user.setName("femail");
		else
			user.setName("unknow");
		System.out.println("in test:" + name);
		return user;
	}
}
