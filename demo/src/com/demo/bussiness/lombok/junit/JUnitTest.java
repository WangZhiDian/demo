package com.demo.bussiness.lombok.junit;

import org.junit.Test;

import com.demo.bussiness.lombok.service.LomBokService;

public class JUnitTest
{

	@Test
	public void testLomBok()
	{
		LomBokService service = new LomBokService();
		service.getAddr();
	}
	
}
