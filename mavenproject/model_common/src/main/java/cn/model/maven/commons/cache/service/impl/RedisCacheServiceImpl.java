package cn.model.maven.commons.cache.service.impl;


import cn.model.maven.commons.cache.service.CacheService;
import cn.model.maven.commons.redis.ShareJedisManager;
import cn.model.maven.exception.ExceptionCode;
import cn.model.maven.exception.HeraRuntimeException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 根据businessType返回指定的businessType，如果没有定义businessType，抛出SYSTEM_INVALID_CACHE_TYPE
 * 
 * 
 * @author wanghl80
 * @date 2016年6月25日 下午1:11:31
 *
 */
@Component
public class RedisCacheServiceImpl implements CacheService
{
	private static final String JEDIS_PROJECT = "hera_payment";
	private static final String BUSINESS_TYPE = "PAYMENT_CACHE";
	
	private static ConcurrentHashMap<String, ShareJedisManager> cacheMap = new ConcurrentHashMap<String, ShareJedisManager>();

	private ShareJedisManager jedisMgr = null;
	
	public RedisCacheServiceImpl()
	{
		this(BUSINESS_TYPE);
	}

	private RedisCacheServiceImpl(String businessType)
	{
		if(!BusinessType.isLegalBusinessType(businessType))
		{
			throw new HeraRuntimeException(ExceptionCode.SYSTEM_INVALID_BUSINESS_TYPE,new Object[]{businessType});
		}
		
		jedisMgr = cacheMap.get(businessType);
		if (jedisMgr == null)
		{
			synchronized (RedisCacheServiceImpl.class)
			{
				jedisMgr = new ShareJedisManager(JEDIS_PROJECT, businessType);
				cacheMap.put(businessType, jedisMgr);
			}
		}
	}

	@Override
	public Map<String, Object> keysAll(String key) {
		return jedisMgr.keys(key);
	}

	@Override
	public Object setString(String key, String value, int seconds)
	{
		return jedisMgr.setString(key, value, seconds);
	}

	@Override
	public String getString(String key)
	{
		return jedisMgr.getString(key);
	}

	@Override
	public Object getObject(String key)
	{
		return jedisMgr.getObject(key);
	}
	
	@Override
	public Object hset(String mapname, String key, String value)
	{
		return jedisMgr.hset(mapname, key, value);
	}
	
	@Override
	public Object hset(String mapname, String key, String value, int seconds)
	{
		return jedisMgr.hset(mapname, key, value, seconds);
	}

	@Override
	public String hget(String mapname, String key)
	{
		return jedisMgr.hget(mapname, key);
	}

	@Override
	public Set<String> hkeys(String mapname)
	{
		return jedisMgr.hkeys(mapname);
	}

	@Override
	public List<String> hvals(String mapname)
	{
		return jedisMgr.hvals(mapname);
	}

	@Override
	public Map<String, String> hgetAll(String mapname)
	{
		return jedisMgr.hgetAll(mapname);
	}

	@Override
	public Boolean hexists(String mapname, String key)
	{
		return jedisMgr.hexists(mapname, key);
	}

	@Override
	public Long hlen(String mapname)
	{
		return jedisMgr.hlen(mapname);
	}

	@Override
	public Long hdel(String mapname, String key)
	{
		return jedisMgr.hdel(mapname, key);
	}

	@Override
	public Long incr(String key)
	{
		return jedisMgr.incr(key);
	}

	@Override
	public Long incr(String key, int seconds)
	{
		return jedisMgr.incr(key, seconds);
	}

	@Override
	public Long incrBy(String key, long increment)
	{
		return jedisMgr.incrBy(key, increment);
	}

	@Override
	public Long decr(String key)
	{
		return jedisMgr.decr(key);
	}

	@Override
	public Object delValue(String key)
	{
		return jedisMgr.delValue(key);
	}

	@Override
	public String getSet(String key, String value)
	{
		return jedisMgr.getSet(key, value);
	}

	@Override
	public Long setnx(String key, String value)
	{
		return jedisMgr.setnx(key, value);
	}

	@Override
	public Long expire(String key, int seconds)
	{
		return jedisMgr.expire(key, seconds);
	}
	
	@Override
	public boolean lock(String key){
		return jedisMgr.setnx(key, "true") == 1;
	}
	
	@Override
	public boolean lock(String key, int seconds) {
		// TODO Auto-generated method stub
		return jedisMgr.setnx(key, "true", seconds) == 1;
	}
	
	@Override
	public void unlock(String key){
		jedisMgr.delValue(key);
	}

	@Override
	public String setObject(String key,Object obj,int seconds){
		return jedisMgr.setObject(key,obj,seconds);
	}
	
	@Override
	public Long expireAt(String key,int unixTime){
		return jedisMgr.expireAt(key,unixTime);
	}

	public Object delAll()
	{
		return jedisMgr.delAll();
	}

	@Override
	public Object ttl(String key)
	{
		return jedisMgr.ttl(key);
	}

	@Override
	public Object pttl(String key)
	{
		return jedisMgr.pttl(key);
	}


}
