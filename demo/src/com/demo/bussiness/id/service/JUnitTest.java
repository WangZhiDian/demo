package com.demo.bussiness.id.service;

import org.junit.Test;

import com.demo.common.id.BasicEntityIdGenerator;
import com.demo.common.util.Function.HashUtil;


public class JUnitTest
{
//	@Test
	public void idTest()
	{
		/**
		 * 根据不同字符串生成唯一码
		 */
		long id = HashUtil.hashCode("name", "type", "id");
		
		System.out.println("id:" + id);
	}
	
	/**
	 * 生成唯一字符串
	 */
	@Test
	public void idTest2()
	{
		BasicEntityIdGenerator APPIDGENERATOR  = new BasicEntityIdGenerator();
		String id = APPIDGENERATOR.generateLongIdString();
		long lid = APPIDGENERATOR.generateLongId();
		
		String timestamp = BasicEntityIdGenerator.getInstance().generateLongIdString();
		System.out.println("id:" + id + "  lid:" + lid + "   timestamp:" + timestamp);
	}
	
	
	
	
}
