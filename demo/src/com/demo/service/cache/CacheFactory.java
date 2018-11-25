package com.demo.service.cache;

import org.apache.commons.lang.StringUtils;

import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.bussiness.Exception.exception.HeraRuntimeException;


/**
 * 目前只支持创建cacheType:redis cache
 * 通过CacheFactory 屏蔽不同cache的实现细节
 * 
 * @author wanghl80
 * @date 2016年6月25日 下午12:27:24
 *
 */
public class CacheFactory
{
	private static final String DEFAULT_CACHE="redis";
	
	public static CacheService createCache(String businessType)
	{
		return createCache(DEFAULT_CACHE,businessType);
	}
	
	
	public static CacheService createCache(String cacheType,String businessType)
	{
		CacheService cacheService;
		
		if(!StringUtils.equals(cacheType, DEFAULT_CACHE))
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_INVALID_CACHE_TYPE,new Object[]{cacheType}); 
		
		cacheService=new RedisCacheService(businessType);

		return cacheService;
	}

}
