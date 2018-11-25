package com.demo.bussiness.Exception.service;

import static com.demo.bussiness.Exception.exception.ExceptionCode.SYSTEM_ILLEGAL_REQUEST;
import static com.demo.bussiness.Exception.exception.ExceptionCode.SUCCESS;

import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.bussiness.Exception.exception.ExceptionCode2;
import com.demo.bussiness.Exception.exception.HeraRuntimeException;
import com.google.common.base.Strings;



public class ExceptionService
{
	public ExceptionCode TestException(String mobile, String captcha)
	{
		if (Strings.isNullOrEmpty(captcha)
				|| mobile == null || !mobile.matches("^1[0-9]{10}$"))
		{
			return SYSTEM_ILLEGAL_REQUEST;
		}
		
		
		return SUCCESS;
	}

	public void testHrea(String mobile)
	{
		if (mobile == null || !mobile.matches("^1[0-9]{10}$"))
		{
			System.out.println("11111");
		}else
		{
			System.out.println("HeraRuntimeException11");
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_SERVICE_UNAVAILABLE);
		}
		System.out.println("222222");
	}

}
