package cn.model.maven.commons.cache.service.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 支持的缓存 business type
 * 
 * 新增加的类型需要先配置在这里
 * 否则抛出 SYSTEM_INVALID_BUSINESS_TYPE 异常
 * 
 * @author wanghl80
 * @date 2016年6月25日 下午4:01:24
 *
 */
public class BusinessType
{
	private static Map<String, String> typeMap =new HashMap<>();

	public static final String APPLICATION_CACHE = "application";
	public static final String TRADE_CACHE = "trade";
	public static final String ORDER_CACHE = "order";
	public static final String APPLICATION_LOCK_CACHE = "application_lock";
	public static final String TRADE_LOCK_CACHE = "trade_lock";
	public static final String PAYMENT_CACHE = "payment";

	// 反射机制获取所有的 BusinessType，并放入 typeMap中
	static
	{
		Field[] fields = BusinessType.class.getFields();
		for (Field field : fields)
		{
			try
			{
				typeMap.put(field.getName(), String.valueOf(field.get(field.getName())));
			} catch (Exception e)
			{
				throw new RuntimeException(e);
			}
		}
	}

	public static boolean isLegalBusinessType(String businessType)
	{
		return typeMap.containsKey(businessType);
	}
}
