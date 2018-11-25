package com.demo.service.wapBind.service;

import java.text.ParseException;

import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import com.demo.service.wapBind.bean.WapBindInfo;

@Component
public class WapBindProcessService extends ApplicationObjectSupport {

    /**
     * @throws ParseException 
     * @Description:根据绑定更新数据库后，是否会做其他操作选择handler
     */
    public void generateWapBind(WapBindInfo wapModel) throws ParseException  {
    	
        WapBindProcessHandler proposalHandler = (WapBindProcessHandler) super.getApplicationContext()
                .getBean("WapBindProcessHandler"+wapModel.getHandler());
        
        // 前缀条件断言
        proposalHandler.assertPrecondition(wapModel);
        // 断言验证码是否正确
        proposalHandler.assertCondition(wapModel);
        // 绑定：更新数据库信息
        proposalHandler.updateOpenidInfo(wapModel);
        // 更新openid后的工作
        proposalHandler.afterWapBindInfo(wapModel);

    }

}
