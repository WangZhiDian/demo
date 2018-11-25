package com.demo.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.service.alipay.AliPayBean;
import com.demo.service.alipay.AliPayServiceImpl;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
	
@Component
@Slf4j
@Path("/alipay")
public class AlipayRest {

	@Autowired
	AliPayServiceImpl aliPayServiceImpl;
	
    @GET
    @Path("/order")
    @Produces(MediaType.TEXT_HTML)
    public Response payOrderRedirect(@Context HttpServletRequest request, String value,@PathParam("version") String version) throws ParseException
    {

    	// http://localhost:8091/demo/rest/alipay/order?tradeNo=100-20180116-001&tradeType=PCPAY
    	
        log.debug("开始跳转,跳转传入得参数为.aliPay notify..:{}", value);
        
		String tradeNo = request.getParameter("tradeNo");
		
		//WAPPAY PCWAP QRCODE
        String tradeType = request.getParameter("tradeType");
		
//		String outTradeNo = "1000-20180114-0001";
		AliPayBean bean = new AliPayBean();
		bean.setBody("泰康测试支付");
		bean.setOutTradeNo(tradeNo);
		bean.setSubject("泰康测试");
		bean.setTotalAmount("0.01");
		bean.setTradeType(tradeType);
		
        String linkValue = aliPayServiceImpl.getPayUrl(bean);

        return Response.status(Response.Status.FOUND).entity(linkValue).build();
    }
	
}
