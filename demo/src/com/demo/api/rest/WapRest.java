package com.demo.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.bussiness.Exception.exception.HeraRuntimeException;
import com.demo.bussiness.qrcode.QrCodeService;
import com.demo.common.rest.RestResponse;
import com.demo.common.util.Function.StringUtil;
import com.demo.service.wapBind.bean.WapBindInfo;
//import com.demo.service.wapBind.dao.WapBindMapper;
import com.demo.service.wapBind.service.WapBindProcessService;

import org.apache.commons.codec.binary.Base64;

/**
 * wap 支付流程完成后，将客户数据库中fake openid替换为真实openid
 */

@Component
@Path("/wapbind")
public class WapRest
{
	
	private static Logger logger = LoggerFactory.getLogger(WapRest.class);
	
	@Autowired
	WapBindProcessService bindProcessService;
	@Autowired
	private QrCodeService qrCodeService;
//	@Autowired
//	private WapBindMapper wapBindService;
	
	/**
	 * 页面绑定微信openid，需要验证手机验证码
	 * @return
	 * @throws ParseException 
	 */
	@POST
    @Path("/pageBind")
	public Response smokeSign(@Context HttpServletRequest request,String value) throws ParseException{
		
		logger.debug("开始签到,签到传入得参数为...{}",value);
		
		WapBindInfo wapModel = JSON.parseObject(value, WapBindInfo.class);
		
		logger.debug("pageBind needCode:{}",wapModel.getName());
		logger.debug("pageBind needCode:{}",wapModel.getSuitId());
//		wapModel.setNeedCheckCode(false);
		bindProcessService.generateWapBind(wapModel);
		
		JSONObject restResult = new JSONObject();
		restResult.put("status", wapModel.getStatus());
		
		RestResponse restResponse = new RestResponse();
		restResponse.setData(restResult);
		logger.debug("pauUrl:{}",restResult.toJSONString());
		
		return Response.status(Response.Status.OK).entity(restResponse.toJSONString()).build();
	}
	
	/**
	 * 二维码扫描绑定客户信息，不需要验证手机验证码
	 * @return
	 * @throws ParseException 
	 */
	@POST
    @Path("/scanCodeBind")
	@Produces("application/json;charset=utf-8")
	public Response smokeNoSign(@Context HttpServletRequest request,String value) throws ParseException{
		
		logger.debug("开始签到,签到传入得参数为...{}",value);
		
		WapBindInfo wapModel = JSON.parseObject(value, WapBindInfo.class);
		logger.debug("pageBind needCode:{}",wapModel.getName());
		wapModel.setNeedCheckCode(false);
		bindProcessService.generateWapBind(wapModel);
		
		JSONObject restResult = new JSONObject();
		restResult.put("status", wapModel.getStatus());
		
		RestResponse restResponse = new RestResponse();
		restResponse.setData(restResult);
		logger.debug("pauUrl:{}",restResult.toJSONString());
		
		return Response.status(Response.Status.OK).entity(restResponse.toJSONString()).build();
	}
	
	/**
	 * 提供客户扫描二维码图片流
	 * @return
	 * @throws ParseException 
	 */
	@GET
    @Path("/provideBindCodeImage")
	public Response provideBindCodeImage(@Context HttpServletRequest request,String value){
		
		//get info from redis: cid, mobile
		String name = request.getParameter("name");
		String identifyType = request.getParameter("identifyType");
		String identifyNo = request.getParameter("identifyNo");
		String phoneNo = request.getParameter("phoneNo");
		String handler = request.getParameter("handler");
		String suitId = request.getParameter("suitId");
		String url = "XXX";
		String oathUrl = "";
		try{
			name = URLDecoder.decode(name, "utf-8");
			url = url + "?name=" + name + "&identifyType=" + identifyType + "&identifyNo=" + identifyNo + "&phoneNo=" + phoneNo + "&handler=" + handler + "&suitId=" + suitId;
			oathUrl = "https://www.baidu.com";
			url = Base64.encodeBase64URLSafeString(url.getBytes());
			System.out.println(url);
			System.out.println(oathUrl.replace("###", url));
			url = oathUrl.replace("###", url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		byte[] qucode = qrCodeService.writeToByte(url);
		return Response.status(Response.Status.OK).entity(qucode).build();
	}
	
	/**
	 * 通过订单号查询保单号
	 * @return
	 * @throws ParseException 
	 */
	@GET
    @Path("/isUnderwrite")
	public Response isTradeUnderwrite(@Context HttpServletRequest request,String value) throws ParseException{
		
		JSONObject restResult = new JSONObject();
		String tId = request.getParameter("tId");
		if("".equals(StringUtil.killNull(tId)))
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_ILLEGAL_REQUEST);
//		String pId = wapBindService.selectPolicyIdByTradeId(tId);
		String pId = "";
		pId = StringUtil.killNull(pId);
		restResult.put("pId", pId);
		
		RestResponse restResponse = new RestResponse();
		restResponse.setData(restResult);
		
		return Response.status(Response.Status.OK).entity(restResponse.toJSONString()).build();
	}
}
