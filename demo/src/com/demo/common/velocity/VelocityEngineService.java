package com.demo.common.velocity;

import org.apache.velocity.VelocityContext;

public interface VelocityEngineService {
	/**
     * 替换模板
     * 
     * @param context
     *            待替换数据
     * @param inString
     *            模板内容
     * @return
     */
    public String transferString(VelocityContext context, String templateKey, String templeContent);

    public String transferString(VelocityContext context, String templeContent);
}
