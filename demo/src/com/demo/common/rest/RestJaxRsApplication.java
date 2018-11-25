package com.demo.common.rest;

import org.glassfish.jersey.server.ResourceConfig;


public class RestJaxRsApplication extends ResourceConfig
{
	public RestJaxRsApplication()
	{
		packages("com.demo.api.rest");
		
		// 注册异常
				register(HeraExceptionMapper.class);
	}
}
