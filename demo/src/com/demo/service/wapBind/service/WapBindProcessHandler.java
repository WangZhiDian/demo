package com.demo.service.wapBind.service;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.demo.service.wapBind.bean.WapBindInfo;

@Component
public interface WapBindProcessHandler
{
	
	/**
	 * 前缀条件断言
	 * 	1.断言数据是否合法，是否为空
	 * @param wapModel
	 * @throws Exception
	 */
	public void assertPrecondition(WapBindInfo wapModel);
	
	/**
	 * 条件断言
	 * 	1.断言验证码是否正确（需判断是否有验证码） 
	 * @param wapModel
	 * @throws Exception
	 */
	public void assertCondition(WapBindInfo wapModel);
	
	/**
	 * 更新数据库信息
	 * 	1.更新proposal_hera，policy_hera，customer，customer_history表
	 * 	      的openid为真实openid
	 * 
	 * @param wapModel
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	public void updateOpenidInfo(WapBindInfo wapModel) throws ParseException;
	
	/**
	 * 更新数据库的工作
	 * 	1.更新缓存或者其他动作
	 * 
	 * @param wapModel
	 * @throws Exception
	 */
	public void afterWapBindInfo(WapBindInfo wapModel);
	
}
