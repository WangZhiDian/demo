package com.demo.common.velocity;

import org.apache.velocity.VelocityContext;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JUnitTest
{
	@Autowired
	private VelocityEngineService velocityEngineService;
	
	@Test
	public void Tests()
	{
		String resJson = "input info";
		String tempStr = "template info";
		VelocityContext context = new VelocityContext();
		context.put("json", resJson);
		String orderinfo = velocityEngineService.transferString(context, tempStr);
		System.out.println(orderinfo);
	}
}
