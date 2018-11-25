package com.demo.common.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 与redis中间件Twemproxy创建链接尺，协议同redis
 * <P>
 * File name : JedisPoolFactory.java
 * </P>
 * <P>
 * Author : chpl05
 * </P>
 * <P>
 * Date : 2016-01-05
 * </P>
 */
public class TwemproxyJedisPoolFactory
{

	private static class LazyHolder
	{
		private static final TwemproxyJedisPoolFactory INSTANCE = new TwemproxyJedisPoolFactory();
	}

	private Map<String, JedisPool> JedisPoolMap = new HashMap<String, JedisPool>();

	private TwemproxyJedisPoolFactory()
	{
		try
		{
			initJedis();
		} catch (ConfigurationException e)
		{
			e.printStackTrace();
		}
	}

	public static TwemproxyJedisPoolFactory getInstance()
	{
		return LazyHolder.INSTANCE;
	}

	/**
	 * 初始化切片集群 ShardJedisManager.initShardJedis()<BR>
	 * <P>
	 * Author : zouzhihua
	 * </P>
	 * <P>
	 * Date : 2013-4-13
	 * </P>
	 * 
	 * @throws ConfigurationException
	 */
	private void initJedis() throws ConfigurationException
	{
		HashMap<String, JedisPool> redisConnectionMap = new HashMap<String, JedisPool>();

		XMLConfiguration routingConfig = new XMLConfiguration("TwemproxyJedisPoolConfig.xml");

		List<Object> serverNodesList = routingConfig.getList("servernode.node.id");
		for (int clusterIndex = 0; clusterIndex < serverNodesList.size(); clusterIndex++)
		{
			String nodeId = (String) serverNodesList.get(clusterIndex);
			// 最大活动连接
			int maxActive = routingConfig.getInt("servernode.node(" + clusterIndex + ").maxActive", 20);
			// 最大
			int maxIdle = routingConfig.getInt("servernode.node(" + clusterIndex + ").maxIdle", 20);
			// 最长等待时间
			int maxWait = routingConfig.getInt("servernode.node(" + clusterIndex + ").maxWait", 20);
			String hosts = routingConfig.getString("servernode.node(" + clusterIndex + ").hosts");
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(maxActive + maxIdle);
			// config.setMaxActive(maxActive);
			config.setMaxIdle(maxIdle);
			config.setMaxWaitMillis(maxWait);
			// config.setMaxWait(maxWait);
			config.setTestOnBorrow(false);
			if (hosts == null)
			{
				throw new ConfigurationException("RedisPool init():hosts config error!");
			} else
			{
				String[] hostarrt = hosts.split(":");
				JedisPool pool = new JedisPool(config, hostarrt[0].trim(), Integer.parseInt(hostarrt[1].trim()));
				redisConnectionMap.put(nodeId, pool);
			}
		}
		this.JedisPoolMap = redisConnectionMap;
	}

	/**
	 * 获取某个节点的切片集群 ShardJedisManager.getShardedJedisPool()<BR>
	 * <P>
	 * Author : zouzhihua
	 * </P>
	 * <P>
	 * Date : 2013-4-14
	 * </P>
	 * 
	 * @param nodeId
	 * @return 切片集群池
	 */
	public JedisPool getJedisPool(String nodeId)
	{
		return JedisPoolMap.get(nodeId);
	}

	public static void main(String[] args)
	{
		TwemproxyJedisPoolFactory.getInstance();
	}

}
