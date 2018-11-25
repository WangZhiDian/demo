/** 
 * @author 王文超 
 * @time 2016年4月20日 下午2:44:17  
 * 类说明 
 */
package com.demo.common.log;

import java.util.HashSet;
import java.util.Set;

import com.demo.common.ReflectionUtils;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * @author 王文超
 * @Description 日志敏感数据脱敏的过滤器，在sensitiveDatas中添加敏感关键词，记录日志时候该关键词没有脱敏会自动脱敏
 * @time 2016年4月20日 下午2:44:17
 */
public class TKLogbackFilter extends Filter<LoggingEvent>
{
	static Set<String> sensitiveDatas = new HashSet<String>();
	static
	{
		sensitiveDatas.add("password");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FilterReply decide(LoggingEvent event)
	{
		for (String sensitiveData : sensitiveDatas)
		{
			if (event.getMessage().contains(sensitiveData))
			{
				String message = event.getMessage();
				Object[] argumentArray = event.getArgumentArray();
				String formattedMessage = TKMessageFormatter.arrayFormat(
						message, argumentArray, sensitiveData).getMessage();
				ReflectionUtils.setFieldValue(event, "formattedMessage",
						formattedMessage);
			}
		}
		return FilterReply.ACCEPT;
	}

}
