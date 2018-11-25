package com.demo.service.alipay;

import javax.annotation.Resource;

import org.junit.Test;

public class JUnitTest {

//	@Resource
//	AliPayServiceImpl aliPayServiceImpl;
	
//	@Test
	public void Test()
	{
		String outTradeNo = "1000-20180114-0001";
		AliPayBean bean = new AliPayBean();
		bean.setBody("泰康测试支付");
		bean.setOutTradeNo(outTradeNo);
		bean.setSubject("泰康测试");
		bean.setTotalAmount("0.01");
		bean.setTradeType("WAPPAY");
		
		AliPayServiceImpl aliPayServiceImpl = new AliPayServiceImpl();
		String payUrl = aliPayServiceImpl.getPayUrl(bean);
		
		System.out.println("test alipay pay url:" );
		
	}
	
	
}
