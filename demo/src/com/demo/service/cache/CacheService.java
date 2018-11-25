package com.demo.service.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {

	/**
	 * 保存缓存值
	 * @param key	键值
	 * @param value	待缓存值
	 * @param seconds 缓存值有效时间
	 * @return NULl
	 */
	public Object setString(String key, String value, int seconds);
	/**
	 * 查询缓存值
	 * @param key	键值
	 * @return
	 */
	public String getString(String key);
	/**
	 * 查询缓存对象
	 * @param key	键值
	 * @return
	 */
	public Object getObject(String key);
	/**
	 * 缓存hash数据
	 * @param mapname
	 * @param key
	 * @param value
	 * @param seconds
	 * @return
	 */
	public Object hset(String mapname, String key,
			String value, int seconds);
	/**
	 * 查询hash数据中的指定值
	 * @param mapname
	 * @param key
	 * @return
	 */
	public String hget(String mapname, String key);
	/**
	 * 查询hash数据的所有key值
	 * @param mapname
	 * @return
	 */
	public Set<String> hkeys(String mapname);
	/**
	 * 查询hash数据的所有value值
	 * @param mapname
	 * @return
	 */
	public List<String> hvals(String mapname);
	/**
	 * 查询所有hash数据
	 * @param mapname
	 * @return
	 */
	public Map<String, String> hgetAll(String mapname);
	/**
	 * 判断hash数据中是否存在某个key
	 * @param mapname
	 * @param key
	 * @return
	 */
	public Boolean hexists(String mapname, String key);
	/**
	 * 查询hash数据中的key值个数
	 * @param mapname
	 * @return
	 */
	public Long hlen(String mapname);
	/**
	 * 删除hash数据中的指定key值
	 * @param mapname
	 * @param key
	 * @return
	 */
	public Long hdel(String mapname, String key);
	/**
	 * 自增
	 * @param key
	 * @return
	 */
	public Long incr(String key);
	/**
	 * 自增并设定该键值的有效期
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long incr(String key, int seconds);
	/**
	 * 按增量自增
	 * @param key
	 * @param increment
	 * @return
	 */
	public Long incrBy(String key, long increment);
	/**
	 * 自减
	 * @param key
	 * @return
	 */
	public Long decr(String key);
	
	/**
	 * 删除指定key值
	 * @param key
	 * @return
	 */
	public Object delValue(String key);
	/**
	 * 保存缓存值
	 * @param key
	 * @param value
	 * @return	旧值
	 */
	public String getSet(String key, String value);
	/**
	 * 当key值不存在时保存缓存值
	 * @param key
	 * @param value
	 * @return	0-key值已存在,1-操作之前key值不存在，此次保存成功
	 */
	public Long setnx(String key, String value);
	/**
	 * 设定指定值的有效期
	 * @param key	键值
	 * @param seconds	有效期，单位秒
	 * @return
	 */
	public Long expire(String key, int seconds);
	/**
	 * 锁定指定key
	 * @param key
	 * @return 锁定成功返回ture，已经被锁定过或锁定失败返回false
	 */
	public boolean lock(String key);
	/**
	 * 指定时间内锁定指定key
	 * @param key
	 * @param seconds
	 * @return
	 */
	public boolean lock(String key, int seconds);
	/**
	 * 解锁
	 * @param key
	 */
	public void unlock(String key);
}
