package com.demo.common.redisUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

//import com.taikang.utils.DbFunc;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
/**
 * redis切片集群管理
 * <P>File name : ShardJedisManager.java </P>
 * <P>Author : zouzhihua </P> 
 * <P>Date : 2013-4-13 </P>
 */
public class ShardJedisManager {
	
	private static class LazyHolder{
		private static final ShardJedisManager INSTANCE = new ShardJedisManager();
	}
	private Map<String,ShardedJedisPool> shardedJedisPoolMap = new HashMap<String, ShardedJedisPool>();
	
	private ShardJedisManager(){
		try {
			initShardJedis();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.out.println("ShardJedisManager.java 未找到 data/config/shardconfignew.xml");
			try
	        {
				//读取配置表，IP与端口要一一对应，顺序一致，使用“,”分割
				String ip_String = DbFunc.getAppConfig("service", "redis", "redis_ip");
				if(ip_String == null || "".equals(ip_String))
				{
					ip_String = "10.7.3.198,10.7.3.198,10.7.3.198,10.7.3.199,10.7.3.199,10.7.3.199,10.7.3.199";
				}
				String ports_String = DbFunc.getAppConfig("service", "redis", "redis_port");
				if(ports_String == null || "".equals(ports_String))
				{
					ports_String = "7379,8379,9379,6379,7379,8379,9379";
				}
//				String ip_String = "10.7.3.198,10.7.3.198,10.7.3.198,10.7.3.198,10.7.3.199,10.7.3.199,10.7.3.199,10.7.3.199";
//				String ports_String = "6379,7379,8379,9379,6379,7379,8379,9379";
				
				String[] ips = ip_String.split(",");
				String[] ports = ports_String.split(",");
				//定义JedisShardInfo的list；
				List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
				for(int i = 0;i<ips.length;i++){
					JedisShardInfo info = new JedisShardInfo(ips[i], Integer.parseInt(ports[i]));
					shards.add(info);
				}
				//创建哈希分片连接池
				JedisPoolConfig config = new JedisPoolConfig();
				
//				config.setMaxActive(-1);
				config.setMaxIdle(60000);
//				config.setMaxWait(60000);
				//config.setTestOnBorrow(true);
				
				ShardedJedisPool shardedPool = new ShardedJedisPool(config, shards);
				HashMap<String,ShardedJedisPool> redisConnectionMap = new HashMap<String,ShardedJedisPool>();
				redisConnectionMap.put("ShardRedisCache", shardedPool);
				this.shardedJedisPoolMap = redisConnectionMap;
	        }
	        catch (Exception e1)
	        {
	        	System.out.println("ShardJedisManager.java 加载旧redis服务器配置异常");
		        e1.printStackTrace();
	        }
		}
	}
	
	public static ShardJedisManager getInstance(){
		return LazyHolder.INSTANCE;
	}
	/**
	 * 初始化切片集群
	 * ShardJedisManager.initShardJedis()<BR>
	 * <P>Author : zouzhihua </P>  
	 * <P>Date : 2013-4-13 </P>
	 * @throws ConfigurationException
	 */
	private void initShardJedis() throws ConfigurationException{
		HashMap<String,ShardedJedisPool> redisConnectionMap = new HashMap<String,ShardedJedisPool>();
		
		File directory = new File("");//设定为当前文件夹
		try{
		    System.out.println(directory.getCanonicalPath());//获取标准的路径
		    System.out.println(directory.getAbsolutePath());//获取绝对路径
		}catch(IOException e){}
		
		XMLConfiguration routingConfig = new XMLConfiguration("data/config/shardconfignew.xml");
		
		
		List<Object> serverNodesList = routingConfig.getList("servernode.node.id");
		for(int clusterIndex=0;clusterIndex<serverNodesList.size();clusterIndex++){
			String nodeId = (String)serverNodesList.get(clusterIndex);
			//最大活动连接
			int maxActive = routingConfig.getInt("servernode.node("+clusterIndex+").maxActive",20);
			//最大
			int maxIdle = routingConfig.getInt("servernode.node("+clusterIndex+").maxIdle",20);
			//最长等待时间
			int maxWait = routingConfig.getInt("servernode.node("+clusterIndex+").maxWait",20);
			String hosts = routingConfig.getString("servernode.node("+clusterIndex+").hosts");
			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxActive(maxActive);
			config.setMaxIdle(maxIdle);
//			config.setMaxWait(maxWait);
			config.setTestOnBorrow(false);
			if (hosts == null) {
				throw new ConfigurationException("RedisPool init():hosts config error!");
			} else {
				List<JedisShardInfo> jedisShardInfos= new ArrayList<JedisShardInfo>();
				String[] hoststr = hosts.split("#");
				JedisShardInfo jsi;
				String[] hostarrt = null;
				for (String host : hoststr) {
					hostarrt = host.split(":");
					jsi = new JedisShardInfo(hostarrt[0].trim(), Integer.parseInt(hostarrt[1].trim()));
					jedisShardInfos.add(jsi);
				}
				ShardedJedisPool pool = new ShardedJedisPool(config, jedisShardInfos);
				redisConnectionMap.put(nodeId, pool);
			}
		}
		this.shardedJedisPoolMap = redisConnectionMap;
	}
	/**
	 * 获取某个节点的切片集群
	 * ShardJedisManager.getShardedJedisPool()<BR>
	 * <P>Author : zouzhihua </P>  
	 * <P>Date : 2013-4-14 </P>
	 * @param nodeId
	 * @return 切片集群池
	 */
	public ShardedJedisPool getShardedJedisPool(String nodeId){
		return shardedJedisPoolMap.get(nodeId);
	}
	public static void main(String[] args){
		ShardJedisManager.getInstance();
	}

}
