package com.demo.api.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import com.demo.service.wechat.WechatPayBean;
import com.demo.service.wechat.WechatPayServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Path("/wechat")
public class WechatPayRest {

	@Autowired
	WechatPayServiceImpl wechatPayServiceImpl;
	
    @GET
    @Path("/order")
    @Produces(MediaType.TEXT_HTML)
    public Response payOrderRedirect(@Context HttpServletRequest request, String value,@PathParam("version") String version) throws ParserConfigurationException, IOException, SAXException
    {

    	// http://localhost:8091/demo/rest/wechat/order?tradeNo=100-20180116-001&tradeType=PCPAY
    	
        log.debug("开始跳转,跳转传入得参数为.aliPay notify..:{}", value);
        
		String tradeNo = request.getParameter("tradeNo");
		
		//JSAPI NATIVE MWEB
        String tradeType = request.getParameter("tradeType");
		
//		String outTradeNo = "1000-20180114-0001";
        WechatPayBean bean = new WechatPayBean();
		bean.setBody("泰康微信测试支付");
		bean.setOutTradeNo(tradeNo);
		bean.setSubject("泰康微信测试");
		bean.setTotalAmount("1");
		bean.setTradeType(tradeType);
		
        String linkValue = wechatPayServiceImpl.generateDeeplink(bean);

        return Response.status(Response.Status.FOUND).entity(linkValue).build();
    }
}
