package com.demo.bussiness.Exception.exception;

import java.io.Serializable;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public enum ExceptionCode implements Serializable
{
	SUCCESS("SUCCESS"                                                                   , "0"           , "接口成功执行")                              ,
	SYSTEM_UNDEFINEDE_EXECPTION("SYSTEM_UNDEFINEDE_EXECPTION"                           , "100001999"   , "未定义的异常")                              ,
	SYSTEM_SERVICE_UNAVAILABLE("SYSTEM_SERVICE_UNAVAILABLE"                             , "100001001"   , "服务停止")                                  ,
	SYSTEM_ILLEGAL_REQUEST("SYSTEM_ILLEGAL_REQUEST"                                     , "100001002"   , "非法请求:{0}")                              ,
	SYSTEM_INVALID_PARAM("SYSTEM_INVALID_PARAM"                                         , "100001003"   , "参数值非法，需为 ({0})，实际为 ({1})，请参考A,档")   ,
	SYSTEM_UNSUPPORT_MEDIATYPE("SYSTEM_UNSUPPORT_MEDIATYPE"                             , "100001004"   , "不支持的MediaType ({0})")                   ,
	SYSTEM_EMPTY_PARAM("SYSTEM_EMPTY_PARAM"                                             , "100001005"   , "参数为空:{0}")                              ,
	SYSTEM_INVALID_JSON_DATA("SYSTEM_INVALID_JSON_DATA"                                 , "100001006"   , "投保信息输入有误:期望{0}为{1}")              ,
	SYSTEM_INVALID_BUSINESS_TYPE("SYSTEM_INVALID_BUSINESS_TYPE"                         , "100001007"   , "{0}的缓存类型没有BusinessType在里面定义，请定义后再使用")  ,
	SYSTEM_INVALID_CACHE_TYPE("SYSTEM_INVALID_CACHE_TYPE"                         		, "100001008"   , "{0}的缓存类型没有cacheType不支持")  			,
	WECHAT_INVOKE_ERROR("WECHAT_INVOKE_ERROR"                                           , "200001999"   , "调用微信接口异常:{0}")                      ,
	WECHAT_USERINFO_FAILED("WECHAT_USERINFO_FAILED"                                     , "200001001"   , "获取用户微信信息失败:{0}")                  ,
	WECHAT_ENCRYPTCODE_FAILED("WECHAT_ENCRYPTCODE_FAILED"                               , "200001002"   , "微信卡券解码失败")                          ,
	MOBILE_EXCEED_MAX_TIMES("MOBILE_EXCEED_MAX_TIMES"                                   , "200002001"   , "验证码发送次数过于频繁:{0}")                ,
	MOBILE_CAPTCHA_FAILED("MOBILE_CAPTCHA_FAILED"                                       , "200002002"   , "验证码错误")                                ,
	MOBILE_CAPTCHA_INPUT_TIMES("MOBILE_CAPTCHA_INPUT_TIMES"                             , "200002003"   , "验证码错误次数过多，已失效，请重新获取")          ,
	MOBILE_CAPTCHA_EXPIRED("MOBILE_CAPTCHA_EXPIRED"                                     , "200002004"   , "验证码失效")                                ,
	MOBILE_CAPTCHA_STILL_AVAILABLE("MOBILE_CAPTCHA_STILL_AVAILABLE"                     , "200002011"   , "验证码还可以使用， 请使用之前验证码:{0}")                ,
	CUSTOMER_EMPTY_RESULT("CUSTOMER_EMPTY_RESULT"                                       , "200003001"   , "获取客户信息为空:{0}")                      ,
	CUSTOMER_QUERY_ERROR("CUSTOMER_QUERY_ERROR"                                         , "200003002"   , "获取客户信息异常")                          ,
	PROPOSAL_NOT_ONSALE_ERROR("PROPOSAL_NOT_ONSALE_ERROR"                               , "200004001"   , "该产品已停售")                              ,
	PROPOSAL_SAVE_CUSTOMER_FAILED("PROPOSAL_SAVE_CUSTOMER_FAILED"                       , "200004002"   , "保存客户失败")                              ,
	PROPOSAL_ALREADY_BOUGHT("PROPOSAL_ALREADY_BOUGHT"                                   , "200004003"   , "您已成功购买过该产品")                      ,
	PROPOSAL_TRADE_CREATION_FAILED("PROPOSAL_TRADE_CREATION_FAILED"                     , "200004004"   , "订单生成失败")                              ,
	PROPOSAL_REFUSED("PROPOSAL_REFUSED"                                                 , "200004009"   , "您不符合投保条件")                           ,
	PROPOSAL_VOUCHERCONSUME_CREATION_FAILED("PROPOSAL_VOUCHERCONSUME_CREATION_FAILED"   , "200004005"   , "保存优惠券消费记录表失败")                   ,
	POLICY_TOKEN_VERIFICATION_FAILED("POLICY_TOKEN_VERIFICATION_FAILED"                 , "200005001"   , "token不一致")                               ,
	POLICY_FAILED_PAYMENT_ERROR("POLICY_FAILED_PAYMENT_ERROR"                           , "200005002"   , "支付失败")                                  ,
	POLICY_NO_TRADE_INFO("POLICY_NO_TRADE_INFO"                                         , "200005003"   , "查不到对应订单记录")                        ,
	POLICY_TRADE_ALREADY_PAID("POLICY_TRADE_ALREADY_PAID"                               , "200005004"   , "该订单已经支付过")                          ,
	POLICY_NO_PROPOSAL_INFO("POLICY_NO_PROPOSAL_INFO"                                   , "200005005"   , "查不到对应投保单记录")                      ,
	POLICY_UPDATE_TRADE_STATUS_FAILED("POLICY_UPDATE_TRADE_STATUS_FAILED"               , "200005006"   , "更新订单的支付状态失败")                    ,
	POLICY_CREATION_FAILED("POLICY_CREATION_FAILED"                                     , "200005007"   , "保单生成失败")                              ,
	POLICY_PAYCALLBACK_REQUEST_PARSE_ERROR("POLICY_PAYCALLBACK_REQUEST_PARSE_ERROR"     , "200005008"   , "解析支付回调请求异常：{0}")                  ,
	POLICY_ALREADY_EXISTING("POLICY_ALREADY_EXISTING"                                   , "200005009"   , "相应投保单{0}的保单已经存在：{1}")          ,
	POLICY_ALREADY_EXISTINGID("POLICY_ALREADY_EXISTINGID"                               , "200005010"   , "保单号{0}已经存在")                         ,
	POLICY_PAYAMOUNT_TAMPERING("POLICY_ALREADY_EXISTINGID"                              , "200005011"   , "支付金额篡改")                              ,
	BONUS_GROUP_EXCEED_TOTALNUM_MAX("BONUS_GROUP_EXCEED_TOTALNUM_MAX"                   , "200006001"   , "红包发放完毕")                              ,
	BONUS_GROUP_EXCEED_AMOUNT_MAX("BONUS_GROUP_EXCEED_AMOUNT_MAX"                       , "200006002"   , "红包奖金发放完毕")                          ,
	LIFE_INSURE_INVOKE_ERROR("LIFE_INSURE_INVOKE_ERROR"                                 , "200007999"   , "调用寿险接口异常:{0}")                      ,
	LIFE_INSURE_PROPOSAL_FAILED("LIFE_INSURE_PROPOSAL_FAILED"                           , "200007001"   , "提交寿险订单失败：{0}")                     ,
	LIFE_INSURE_VALIDATE_FAILED("LIFE_INSURE_VALIDATE_FAILED"                           , "200007002"   , "{0}")                                       ,
	LOTTERY_AWARD_ERROR("LOTTERY_AWARD_ERROR"                                           , "200008001"   , "奖品配置错误，请联系客服")                   ,
	LOTTERY_EVENT_ERROR("LOTTERY_EVENT_ERROR"                                           , "200008002"   , "当前抽奖活动已停止")                         ,
	LOTTERY_SAVE_ERROR("LOTTERY_SAVE_ERROR"                                             , "200008003"   , "数据操作异常")                               ,
	LOTTERY_TIMES_NOINIT("LOTTERY_TIMES_NOINIT"                                         , "200008004"   , "抽奖机会正在生成中")                         ,
	LOTTERY_SYS_ERROR("LOTTERY_SYS_ERROR"                                               , "200008005"   , "系统异常，抽奖机会重新生成中，请重试！")     ,
	LOTTERY_TIMES_LEFT_ZERO("LOTTERY_TIME_LEFT_ZERO"                                    , "200008006"   , "您没有剩余的抽奖机会")                       ,
	SMOKE_FIRST_SIGN("SMOKE_FIRST_SIGN"                                                 , "200009001"   , "首次打卡提示，戒烟日开始")                   ,
	SMOKE_RESET_ERROR("SMOKE_RESET_ERROR"                                               , "200009002"   , "重置打卡失败")                               ,
	SMOKE_SIGN_ERROR("SMOKE_SIGN_ERROR"                                                 , "200009003"   , "打卡失败")                                   ,
	SMOKE_SIGNED_AREADY("SMOKE_SIGNED_AREADY"                                           , "200009004"   , "您今天已经打卡，不能重复打卡")               ,
	SMOKE_SIGNTOTAL_ERROR("SMOKE_SIGNTOTAL_ERROR"                                       , "200009005"   , "获取累计打卡次数失败")                       ,
	SMOKE_HOSPITAL_ERROR("SMOKE_HOSPITAL_ERROR"                                         , "200009006"   , "医院列表获取失败，请重试")                   ,
	SMOKE_HOSPITAL_EMPTY_ERROR("SMOKE_HOSPITAL_EMPTY_ERROR"                             , "200009007"   , "该地区暂无定点医院")                         ,
	SMOKE_SAVEDRUGSTRADE_ERROR("SMOKE_SAVEDRUGSTRADE_ERROR"                             , "200009008"   , "保存药品配送信息失败")                       ,
	SMOKE_LOGS_SIGN_ERROR("SMOKE_LOGS_SIGN_ERROR"                                       , "200009009"   , "获取打卡信息失败")                           ,
	MAX_APPOINTMENT_TIMES("MAX_APPOINTMENT_TIMES"                                       , "200010001"   , "该帐号已达到预约做大次数")                   ,
	MAX_APPOINTMENT_END("MAX_APPOINTMENT_END"                                           , "200010002"   , "预约活动结束"),
	MEDICAL_IS_ACTIVATED("MEDICAL_IS_ACTIVATED"                                         , "200011001"   , "体检资格已经被激活，无法再次激活"),
	MEDICAL_CID_IS_ACTIVATED("MEDICAL_CID_IS_ACTIVATED"                                 , "200011002"   , "证件号三个月内已有激活记录，无法再次激活")
	;
	

	private static Logger logger = LoggerFactory.getLogger(ExceptionCode.class);
	private static final long serialVersionUID = -4850687483494058414L;
	
	private String errorCode;
	private String errorMessage;
	private Object data;
	private String referUrl;

	private ExceptionCode(String key, String errorCode, String errorMessage)
	{

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	private ExceptionCode(String key, String errorCode, String errorMessage,
			String referUrl)
	{

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.referUrl = referUrl;
	}

	public ExceptionCode setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
		return this;
	}

	public ExceptionCode setData(Object data)
	{
		this.data = data;
		return this;
	}

	public ExceptionCode setReferUrl(String referUrl)
	{
		this.referUrl = referUrl;
		return this;
	}

	public JSONObject toJSONObject()
	{
		JSONObject object = new JSONObject();
		object.put("error_code", errorCode);
		object.put("error_message", errorMessage);
		if (referUrl != null)
			object.put("refer_url", referUrl);
		if (data != null)
		{
			object.put("data", data);
		}
		return object;

	}

	@Override
	public String toString()
	{
		if (data==null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"error_code\":\"");
			sb.append(this.errorCode);
			sb.append("\",\"error_message\":\"");
			sb.append(this.errorMessage);
			sb.append("\"}");
			return sb.toString();
		}
		else {
			return toJSONObject().toJSONString();
		}
	}

	public String toString(Object... args)
	{
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"error_code\":\"");
			sb.append(this.errorCode);
			sb.append("\",\"error_message\":\"");
			sb.append(MessageFormat.format(this.errorMessage, args));
			sb.append("\"}");
			return sb.toString();
		}catch(Exception e){
			logger.error("格式化异常错误: " + errorMessage, e);
			return this.toString();
		}
	}

	/**
	 * 获取最终的错误描述 不替换/格式错误描述中的 {0}
	 * 
	 * @return
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	public Object getData()
	{
		return data;
	}

	/**
	 * 获取最终的错误描述 替换/格式错误描述中的 {0}
	 * 
	 * @return
	 */
	public String getErrorMessage(Object... codeArgs)
	{
		try
		{
			return MessageFormat.format(this.errorMessage, codeArgs);
		} catch (Exception ex)
		{
			logger.error("格式化异常错误: " + errorMessage, ex);
			return errorMessage;
		}
	}

	/**
	 * 获取最终的错误编码
	 * 
	 * @return
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * 获取帮助或者跳转链接
	 * 
	 * @return
	 */
	public String getReferUrl()
	{
		return referUrl;

	}

	
}
