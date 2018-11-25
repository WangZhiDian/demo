package com.demo.api.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
//import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.apache.commons.codec.binary.Base64;
//import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.demo.bussiness.base64.service.Base64Service.BaseService;
import com.demo.bussiness.http.RequestUtil;
import com.demo.bussiness.okhttp.service.OkHttpService;
import com.demo.bussiness.qrcode.QrCodeService;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

@Component
@Path("function")
public class FunctionUtilRest
{
	@Autowired
	OkHttpService okHttpService;
	@Autowired
	private QrCodeService qrCodeService;
	
	/**
	 * test okhttpget
	 * @return
	 * @throws IOException
	 */
	@GET
	@Path("/okhttpget")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchName() throws IOException
	{
		String url = "http://my.taikang.com:8091/demo/rest/nomaltest/constName";
		String constname = okHttpService.getAsString(url);
		return constname;
	}
	
	/**
	 * test http get
	 * @throws Exception
	 */
	@GET
	@Path("/httpget")
	@Produces(MediaType.APPLICATION_JSON)
	public String httpGet() throws Exception
	{
		String url = "http://my.taikang.com:8091/demo/rest/nomaltest/constName?parm=mytest";		
		String ret = RequestUtil.initHttp().doGet(url);
		System.out.println(ret);
		return ret;
	}
	
	/**
	 * test http post
	 * @throws Exception
	 */
	@GET
	@Path("/httppost")
	@Produces(MediaType.APPLICATION_JSON)
	public String httpPost() throws Exception
	{
		String url = "http://my.taikang.com:8091/demo/rest/nomaltest/constPostName?parm=testpost";
		JSONObject json = new JSONObject();
		json.put("name", "test1");
		json.put("age", 21);
		
		String content = json.toString();
		
		String ret = RequestUtil.initHttp().doPost(url, content, "application/json", "UTF-8");
		System.out.println(ret);
		return ret;
	}
	
	/**
	 * test http get
	 * @throws Exception
	 */
	@GET
	@Path("/qrcode")
	public Response qucode(@Context HttpServletRequest request,String value)
	{
//		String suitId = request.getParameter("suitId");
		String url = "http://weit.taikang.com/2016/no_tobacco/page/policy_success1.html";
		String oathUrl = ""; //公众号生成oathUrl链接
		try{
			oathUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxcd7143c00e5bb6f7&redirect_uri=http://wxpt.taikang.com/tkmap/wechat/oauth2/redirect/wxcd7143c00e5bb6f7?other=###&response_type=code&scope=snsapi_base&state=redict#wechat_redirect";
//			url = Base64.encodeBase64URLSafeString(url.getBytes());
			System.out.println(url);
			System.out.println(oathUrl.replace("###", url));
			url = oathUrl.replace("###", url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		byte[] qucode = qrCodeService.writeToByte(url);
		
		return Response.status(Response.Status.OK).entity(qucode).build();
		
	}
	
	@GET
	@Path("/base64")
	@Produces(MediaType.TEXT_PLAIN)
//	public Response base64(@Context HttpServletRequest request,String value)
	public String base64(@Context HttpServletRequest request,String value)
	{
		BaseService bservice = new BaseService();
//		byte[] base = bservice.image2byte();
		
		String strurl = "weixin：//wxpay/s/An4baqw";
		byte[] qucode = qrCodeService.writeToByte(strurl);
		
		String base64str = Base64.encode(qucode);
//		String base64str = bservice.str2base64(qucode);
		base64str = "data:image/jpg;base64," + base64str;
//		return Response.status(Response.Status.OK).entity(qucode).build();
		return base64str;
		
	}
}
