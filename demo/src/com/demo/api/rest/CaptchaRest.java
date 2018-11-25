package com.demo.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.common.element.CaptchaAggregator;
import com.demo.common.resource.APIParam;
import com.demo.common.resource.APIResponse;
import com.demo.common.util.Function.HttpUtils;

@Component
@Path("/insure/{"+APIParam.VERSION_PARAM+"}/captcha")
public class CaptchaRest
{
	private static Logger logger = LoggerFactory.getLogger(CaptchaRest.class);
	@Autowired
	private CaptchaAggregator captchaAggregator;
	
	@POST
	@Path("/send")
	@Produces("application/json;charset=utf-8")
	public Response getCaptcha(@Context HttpServletRequest request,
			@PathParam(APIParam.VERSION_PARAM) String version,
			@QueryParam(APIParam.MOBILE_PARAM) String mobile)
	{
		logger.debug("====mobile:{}====",mobile);
		String clientIp = HttpUtils.getIpAddr(request);  //客户端IP
		logger.debug("====clientIp:{}====",clientIp);
		int result = captchaAggregator.getMobileCaptcha(mobile, clientIp);
		APIResponse apiRes = new APIResponse();
		if(result == 0) {
			apiRes.setCode("0");
			apiRes.setMessage("验证码发送成功");
		} else if(result == -1) {
			apiRes.setCode("-1");
			apiRes.setMessage("手机号码格式不正确");
		} else if(result == 1) {
			apiRes.setCode("1");
			apiRes.setMessage("验证码发送失败");
		} else if(result == 2) {
			apiRes.setCode("2");
			apiRes.setMessage("短信发送过于频繁");
		} else if(result == 3) {
			apiRes.setCode("3");
			apiRes.setMessage("未配置验证码短信发送规则");
		}
		logger.debug("====result:{}====",apiRes.toJSONString());
		return apiRes.getResponse();
	}
	
	
}
