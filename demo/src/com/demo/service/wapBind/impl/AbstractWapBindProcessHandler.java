package com.demo.service.wapBind.impl;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.bussiness.Exception.exception.HeraRuntimeException;
import com.demo.common.util.Function.IdcardUtils;
import com.demo.common.util.Function.StringUtil;
import com.demo.service.wapBind.bean.WapBindInfo;
import com.demo.service.wapBind.dao.WapBindMapper;
import com.demo.service.wapBind.service.WapBindProcessHandler;

/**
 * 
 * AbstractProposalProcessHandler抽象默认实现，目的是覆盖70%的投保流程
 * 对于额外的30%，可以继承ProposalProcessHandlerDefault来修改其中的部分实现  
 * 其它实现实现类规则为：  ProposalProcessHandler+<suiteId>
 * 
 */
@Component
public abstract class AbstractWapBindProcessHandler implements WapBindProcessHandler
{
	private static Logger logger = LoggerFactory.getLogger(AbstractWapBindProcessHandler.class);
	
	@Autowired
	private WapBindMapper wapBindService;
	
	/**
	 * 前缀条件断言
	 * 	1.断言数据是否合法，是否为空
	 * @param wapModel
	 * @throws Exception
	 */
	@Override
	public void assertPrecondition(WapBindInfo wapModel)
	{
		if(StringUtil.isNotDefined(wapModel.getOpenid()) || StringUtil.isNotDefined(wapModel.getIdentifyNo()) || StringUtil.isNotDefined(wapModel.getName())
				|| StringUtil.isNotDefined(wapModel.getPhoneNo()))
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_EMPTY_PARAM);
		if(wapModel.isNeedCheckCode() && StringUtil.isEmpty(wapModel.getCheckCode()))
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_EMPTY_PARAM);
	}
	
	/**
	 * 条件断言
	 * 	1.断言验证码是否正确（需判断是否有验证码） 
	 * @param wapModel
	 * @throws Exception
	 */
	@Override
	public void assertCondition(WapBindInfo wapModel)
	{
    	if(wapModel.isNeedCheckCode())
    	{
//    		String mark = wapModel.getCheckCode();
//    		String mobile = wapModel.getPhoneNo();
    		//to-do
    	}
	}
	
	/**
	 * 更新数据库信息
	 * 	1.更新proposal_hera，policy_hera表
	 * 	      的openid为真实openid
	 * 
	 * @param wapModel
	 * @return
	 * @throws ParseException 
	 * @throws Exception
	 */
	@Override
	public void updateOpenidInfo(WapBindInfo wapModel) throws ParseException
	{
		boolean flag = false;
		String identifyNo = wapModel.getIdentifyNo();
    	String identifyType = wapModel.getIdentifyType();
    	if("01".equals(identifyType) && !IdcardUtils.validateCard(identifyNo)){
			throw new HeraRuntimeException(ExceptionCode.LIFE_INSURE_VALIDATE_FAILED,new Object[]{"证件号码格式有误"});
		}
		//如果证件号长度为15位，则转换为18位
		if("01".equals(identifyType) &&  identifyNo.length() == 15){
			identifyNo = IdcardUtils.conver15CardTo18(identifyNo);
		}
		String wapOpenid = StringUtil.getCustomerId(identifyNo, identifyType);
    	
		String encryptMobile = wapModel.getPhoneNo();
		//查询客户表是否有该客户记录
		String openid = wapBindService.selectByWapBindInfo(wapOpenid, encryptMobile);
		flag = !"".equals(StringUtil.killNull(openid));
		if(flag)
		{
			logger.debug(wapModel.getSuitId());
			// 更新保单表
			flag = wapBindService.updatePolicyByOpenid(wapOpenid, wapModel.getOpenid(), wapModel.getSuitId()) > 0;
			// 更新投保单表
			if(flag)
				flag = wapBindService.updateProposalByOpenid(wapOpenid, wapModel.getOpenid(), wapModel.getSuitId()) > 0;
		}
		
		if(flag)
			wapModel.setStatus("0");
		else
			wapModel.setStatus("1");
	}
    	
	/**
	 * 更新数据库的工作
	 * 	1.更新缓存或者其他动作
	 * 
	 * @param wapModel
	 * @throws Exception
	 */
	@Override
	public void afterWapBindInfo(WapBindInfo wapModel)
	{
		
	}
	
}
