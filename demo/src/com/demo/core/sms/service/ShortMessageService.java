package com.demo.core.sms.service;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.bussiness.Exception.exception.HeraRuntimeException;
import com.demo.common.redis.ShareJedisManager;
import com.demo.common.security.RequestParam;
import com.demo.common.security.WebSecurityUtils;
import com.google.common.base.Strings;

/**
 * 发送短信接口
 * 
 *
 */
@Component
public class ShortMessageService
{
	private static final Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

	@Autowired
	private WebSecurityUtils webSecurityUtils;
	
	private static final ShareJedisManager msgCacheRedisMgr = new ShareJedisManager("wechat", "sms");
	
	
	/**
	 * 发送短信手机验证码
	 * @param bussinessId 活动id、套餐编号
	 * @param mobile 手机号
	 * @param request
	 * @return
	 */
	public ExceptionCode getMobileCaptcha(String bussinessId,String mobile,String clientIp)
	{
		String sExpTime = "60";
		if (Strings.isNullOrEmpty(bussinessId)
				|| Strings.isNullOrEmpty(mobile))
		{
			return ExceptionCode.SYSTEM_ILLEGAL_REQUEST;
		}
		if (mobile == null || !mobile.matches("^1[0-9]{10}$"))
		{
			return ExceptionCode.SYSTEM_ILLEGAL_REQUEST;
		}

		RequestParam requestParam = new RequestParam();
		requestParam.setBussinessId("mobile");
		requestParam.setParamArrays(new String[]{mobile});
		String redismobile = webSecurityUtils.getSignature(requestParam);
		// 校验手机验证码发送次数有无超过限制
		if (validateCaptchaByIp(clientIp) && validateCaptchaByPhone(redismobile))
		{
			// 组织内容
			int expTime = Integer.parseInt(sExpTime); // 验证码有效期;
			String verficationCode = RandomStringUtils.randomNumeric(6);
			msgCacheRedisMgr.setString("exp" + redismobile, verficationCode,expTime);
			
			//组织短信,将短信写到短信表中,另有定时任务批量读取发送短信.
			//to-do
		} 
		else
		{
			return ExceptionCode.MOBILE_EXCEED_MAX_TIMES;
		}
		return ExceptionCode.SUCCESS;
	}
	
	/**
	 * 验证短信验证码
	 * @param eventId 活动编号
	 * @param mark 页面用户输入的6位验证码
	 * @param mobile 手机号
	 * @return
	 */
	public ExceptionCode checkMobileCaptcha(String captcha,String mobile)
	{
		String sExpTime = "60";
		
		if (Strings.isNullOrEmpty(captcha)
				|| mobile == null || !mobile.matches("^1[0-9]{10}$"))
		{
			return ExceptionCode.SYSTEM_ILLEGAL_REQUEST;
		}
		
		RequestParam requestParam = new RequestParam();
		requestParam.setBussinessId("mobile");
		requestParam.setParamArrays(new String[]{mobile});
		String redismobile = webSecurityUtils.getSignature(requestParam);
		
		String rediscaptcha = msgCacheRedisMgr.getString("exp" + redismobile);
		if (rediscaptcha != null)
		{
			if (rediscaptcha.equals(captcha))
			{
				msgCacheRedisMgr.delValue("exp" + redismobile);
				return ExceptionCode.SUCCESS;
			} 
			else
			{
				int expTime = Integer.parseInt(sExpTime); // 验证码有效期;
				if (msgCacheRedisMgr.incr("error" + redismobile, expTime) >= 3)
				{
					msgCacheRedisMgr.delValue("exp" + redismobile);
					return ExceptionCode.MOBILE_CAPTCHA_INPUT_TIMES;
				} 
				else
				{
					return ExceptionCode.MOBILE_CAPTCHA_FAILED;
				}
			}
		} 
		else
		{
			return ExceptionCode.MOBILE_CAPTCHA_EXPIRED;
		}
	}
	
	/**
	 * 验证同一个手机号发送验证码，24小时内是否超过>6次
	 * 
	 * @param object
	 * @param phone_number
	 * @return false代表超过6次，true代表没有超过6次
	 */
	private boolean validateCaptchaByPhone(String redisMobile)
	{
		String time = "60000";
		String times = "6";
		String stimespan = "60";
		int timespan = Integer.parseInt(stimespan); // 时间间隔60秒;
		int phoneExpiredTime = Integer.parseInt(time);// 24小时 24*60*60;
		int phoneTimes = Integer.parseInt(times); // 6次;
		
		long retryTimes = msgCacheRedisMgr.incr(redisMobile, phoneExpiredTime);
		logger.debug("验证同一个手机号发送验证码，24小时内是否超过{}次,phoneNum={},次数={}",
				phoneTimes, redisMobile, retryTimes);
		// 如果超过6次
		if (retryTimes > phoneTimes)
		{
			logger.warn("验证同一个手机号发送验证码，24小时内是否超过{}次,phoneNum={},次数={}",
					phoneTimes, redisMobile, retryTimes);
			return false;
		} 
		else
		{
			if (msgCacheRedisMgr.incr("timespan" + redisMobile, timespan) > 1)
			{
				logger.warn("验证同一个手机号发送验证码，时间间隔小于{}秒", timespan, redisMobile);
				throw new HeraRuntimeException(ExceptionCode.MOBILE_CAPTCHA_STILL_AVAILABLE);
			}
			return true;
		}
	}
	/**
	 * 验证同一个ip发送验证码，10分钟内是否超过60次
	 * 
	 * @param object
	 * @param phone_number
	 * @return false代表超过60次，true代表没有超过60次
	 */
	private boolean validateCaptchaByIp(String redisip)
	{
		String time = "60000";
		String times = "60";
		int ipExpiredTime = Integer.parseInt(time); // 10分钟;
		int ipTimes = Integer.parseInt(times); // 60次;
		
		long retryTimes = msgCacheRedisMgr.incr(redisip, ipExpiredTime);
		logger.debug("验证同一个ip发送验证码，10分钟内是否超过{}次,ipNum={},次数={}", ipTimes,
				redisip, retryTimes);
		// 如果超过60次
		if (retryTimes > ipTimes)
		{
			logger.warn("验证同一个ip发送验证码，10分钟内是否超过{}次,ip={},次数={}", ipTimes,
					redisip, retryTimes);
			return false;
		}
		return true;
	}

	
}
