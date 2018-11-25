package com.demo.bussiness.wapbindhandler.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.demo.common.redis.TwemproxyJedisManager;
import com.demo.common.util.Function.StringUtil;
import com.demo.service.wapBind.bean.WapBindInfo;
import com.demo.service.wapBind.impl.AbstractWapBindProcessHandler;

@Component()
public class WapBindProcessHandlerP extends AbstractWapBindProcessHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(WapBindProcessHandlerP.class);
	private static final TwemproxyJedisManager policyCacheRedisMgr = new TwemproxyJedisManager("demo", "po");
	
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
		try
		{
			//逻辑数据
			String rvalue = policyCacheRedisMgr.getString("XXX");
			logger.debug("wap WHOHospitalCode Info:" + rvalue + " | " + wapModel.getIdentifyNo() + "| " + wapModel.getOpenid());
			
			if("0".equals(wapModel.getStatus()))
			{
				if(!"".equals(StringUtil.killNull(rvalue)))
				{
					//to-do
				}else
				{
					wapModel.setStatus("X");
				}
			}
			logger.debug(wapModel.toString());
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("err:" + e.getMessage());
		}
	}
}
