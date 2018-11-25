package com.demo.common.element;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.common.util.Function.Md5Encrypt;
import com.demo.service.cache.BusinessType;
import com.demo.service.cache.CacheFactory;
import com.demo.service.cache.CacheService;
@Component
public class CaptchaAggregator {
	private static Logger logger = LoggerFactory.getLogger(CaptchaAggregator.class);
	
	private static final CacheService cacheService =CacheFactory.createCache(BusinessType.CAPTCHA_CACHE);
	
//	@Autowired
//	private ConfigSystemService configSystemService;
//	@Autowired
//	private SmsService smsService;
//	@Autowired
//	private SmsRuleService smsRuleService;
//	@Autowired
//	private SmsTemplateService SmsTemplateService;
//	@Autowired
//	private VelocityEngineService velocityEngineService;
	
	/**
	 * 发送短信手机验证码
	 * 
	 * @param mobile
	 * @param clientIp
	 * @return
	 * 		-1:手机号码格式不正确
	 * 		0：验证码发送成功	
	 * 		1:失败
	 * 		2：短信发送过于频繁	
	 */
	public int getMobileCaptcha(String mobile,String clientIp)
	{
		try {
			if (StringUtils.isEmpty(mobile)||!mobile.matches("^1[0-9]{10}$"))
			{
				return -1;
			}

			String redismobile = this.getMobileSignature(mobile);
			// 校验手机验证码发送次数有无超过限制
			if (validateCaptchaByPhone(redismobile) && validateCaptchaByIp(clientIp))
			{
				// 组织内容
				int expTime = Integer.parseInt(expTime2); // 验证码有效期 to-do;
				String verficationCode = RandomStringUtils.randomNumeric(6);
				cacheService.setString("exp" + redismobile, verficationCode,expTime);
				//查询短信配置规则
//				SmsRuleKey ruleKey = new SmsRuleKey();
//				ruleKey.setBusinessId("hera_insure");
//				ruleKey.setSmsType("captcha");
//				SmsRule rule = smsRuleService.findBySmsRuleKey(ruleKey);
//				if(rule != null){
//					long smsChannelId = rule.getSmsChannelId();
//					String templateId = rule.getSmsTemplateId();
//					SmsTemplate template = SmsTemplateService.findByTemplateId(templateId);
//					if(template == null)
//						return 3;//未配置验证码短信模板
//					VelocityContext context = new VelocityContext();
//					context.put("captcha", verficationCode);
//					String smsData = velocityEngineService.transferString(context, "sms_"+templateId, template.getSmsTemplateContent());
					
//					Sms sms = new Sms();
//					sms.setSmsId(Long.valueOf("1"+RandomStringUtils.randomNumeric(15)));
//					sms.setSmsChannelId(smsChannelId);
//					sms.setSmsType("captcha");
//					sms.setClientIp(clientIp);
//					sms.setHandlerServer(HttpUtils.getMachineIpAndPort());
//					sms.setSmsData(smsData);
//					sms.setSmsReceiver(EncryptMobile.getEncryptMobile(mobile));
//					sms.setSmsSentStatus(-1);//-1    :消息记录生成（消息是先落库然后异步发送）others:消息发送状态值（0：消息发送成功）
//					//sms.setSmsRetryTimes(0);
//					//将短信内容保存至sms表中
//					if(smsService.saveSms(sms)){
//						return 0;	//验证码发送成功
//					} else {
//						return 1; 	//验证码发送失败
//					}
//				} else {
//					return 3;//未配置验证码短信发送规则
//				}
				return 0; //to-modify
			} 
			else
			{
				return 2;	//短信发送过于频繁
			}
		} catch (Exception e) {
			logger.error("====getMobileCaptcha Exception====",e);
			return 1; 	//验证码发送失败
		}
	}
	
	/**
	 * 验证短信验证码
	 * @param captcha 验证码
	 * @param mobile 接收验证码的手机号码
	 * @return
	 */
	/**
	 * 
	 * @param captcha
	 * @param mobile
	 * @return
	 * 		-2:手机号码格式不正确
	 * 		-1:验证码码为空
	 * 		0：验证码验证成功
	 * 		1:验证码错误次数过多，已失效，请重新获取
	 * 		2：验证码错误
	 * 		3:验证码已失效	
	 */
	String expTime2 = "30";
	public int checkMobileCaptcha(String captcha,String mobile)
	{
		if (StringUtils.isEmpty(mobile)||!mobile.matches("^1[0-9]{10}$")) {
			return -2;	//手机号码格式不正确
		}
		if (StringUtils.isEmpty(captcha)) {
			return -1;	//验证码码不能为空
		}
		
		String redismobile = this.getMobileSignature(mobile);
		
		String rediscaptcha = cacheService.getString("exp" + redismobile);
		if (rediscaptcha != null)
		{
			if (rediscaptcha.equals(captcha))
			{
				cacheService.delValue("exp" + redismobile);
				return 0;	//验证码验证成功
			}
			else
			{
				int expTime = Integer.parseInt(expTime2); // 验证码有效期;
				if (cacheService.incr("error" + redismobile, expTime) >= 3)
				{
					cacheService.delValue("exp" + redismobile);
					return 1;	//验证码错误次数过多，已失效，请重新获取
				}
				else
				{
					return 2;	//验证码错误
				}
			}

		} 
		else
		{
			return 3;	//验证码已失效
		}
	}
	
	public String getMobileSignature(String mobile)
	{
		String key = "redisMd5Key";
		return Md5Encrypt.getMD5Mac(mobile + key, "utf-8");
	}
	
	/**
	 * 验证同一个手机号发送验证码，24小时内是否超过>6次
	 * 
	 * @param object
	 * @param phone_number
	 * @return false代表超过6次，true代表没有超过6次
	 */
	private String phoneExpiredTime2 = "600";
	private String phoneTimes2 = "60";
	private String timespan2 = "60";
	
	private boolean validateCaptchaByPhone(String redisMobile)
	{
		int timespan = Integer.parseInt(timespan2); // 时间间隔60秒;
		int phoneExpiredTime = Integer.parseInt(phoneExpiredTime2);// 24小时 24*60*60;
		int phoneTimes = Integer.parseInt(phoneTimes2); // 6次;
		if (cacheService.incr("timespan" + redisMobile, timespan) > 1)
		{
			logger.warn("验证同一个手机号发送验证码，时间间隔小于{}秒", timespan, redisMobile);
			return false;
		}
		long retryTimes = cacheService.incr(redisMobile, phoneExpiredTime);
		logger.debug("验证同一个手机号发送验证码，24小时内是否超过{}次,phoneNum={},次数={}",
				phoneTimes, redisMobile, retryTimes);
		// 如果超过6次
		if (retryTimes > phoneTimes)
		{
			logger.warn("验证同一个手机号发送验证码，24小时内是否超过{}次,phoneNum={},次数={}",
					phoneTimes, redisMobile, retryTimes);
			return false;
		} else
		{
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
	private String ipExpiredTime2 = "600";
	private String ipTimes2 = "60";
	
	private boolean validateCaptchaByIp(String redisip)
	{
//		int ipExpiredTime = Integer.parseInt(configSystemService.getValueByKey("smscaptcha","ipExpiredTime")); // 10分钟;
//		int ipTimes = Integer.parseInt(configSystemService.getValueByKey("smscaptcha","ipTimes")); // 60次;
		
		int ipExpiredTime = Integer.parseInt(ipExpiredTime2);
		int ipTimes = Integer.parseInt(ipTimes2); // 60次;
		
		long retryTimes = cacheService.incr(redisip, ipExpiredTime);
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
