package com.demo.bussiness.Exception.service;

import org.junit.Test;

import com.demo.bussiness.Exception.exception.ExceptionCode;

public class JUnit
{
	
//	@Test
	public void testExcep()
	{
		String mobile = "1514455588";
		String captcha = "33";
		ExceptionService service = new ExceptionService();
		ExceptionCode code = service.TestException(mobile, captcha);
		
		System.out.println(code);
		
	}
	
	
	@Test
	public void testHreaRuntimeExction()
	{
		String mobile = "15155522254";
		ExceptionService service = new ExceptionService();
		service.testHrea(mobile);
		
		System.out.println("");
	}
}
