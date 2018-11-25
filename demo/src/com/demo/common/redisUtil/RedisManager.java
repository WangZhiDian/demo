package com.demo.common.redisUtil;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.demo.common.redis.ShareJedisManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;



//import com.taikang.utils.DbFunc;


public class RedisManager
{

	/**
	 * redis 缓存
	 * @author jty
	 * 2014-12-05
	 */
	
	//private static JedisPool pool;
	private static ShardedJedisPool shardedPool;
	
	/**工程名*/
	private String project;
	/**业务名*/
	private String trade;
	
	public RedisManager(String project,String trade)
	{
		this.project = project;
		this.trade = trade;
	}
	
	static
	{
		shardedPool = ShardJedisManager.getInstance().getShardedJedisPool("ShardRedisCache");
					  
//		try
//        {
//			//开发环境 10.137.46.7:6379
////			String redis_ip = DbFunc.getAppConfig("service", "redis", "redis_ip");
////			int redis_port = Integer.parseInt(DbFunc.getAppConfig("service", "redis", "redis_port"));
////			pool = new JedisPool(new JedisPoolConfig(),redis_ip,redis_port);
//			
//			//读取配置表，IP与端口要一一对应，顺序一致，使用“,”分割
////			String[] ips = DbFunc.getAppConfig("service", "redis", "redis_ip").split(",");
////			String[] ports = DbFunc.getAppConfig("service", "redis", "redis_port").split(",");
//			String[] ips = {"10.137.46.7"};
//			String[] ports = {"6379"};
//			
//			//定义JedisShardInfo的list；
//			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
//			for(int i = 0;i<ips.length;i++){
//				JedisShardInfo info = new JedisShardInfo(ips[i], Integer.parseInt(ports[i]));
//				shards.add(info);
//			}
//			//创建哈希分片连接池
//			shardedPool = new ShardedJedisPool(new Config(), shards);
//        }
//        catch (Exception e)
//        {
//	        e.printStackTrace();
//        }
	}
	
	public Object delValue(String key){
		Object ret = null;
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			ret = jedis.expire(realKey.getBytes(), 0);
		} catch (Exception e) {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setValue " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return ret;
	}
	
	public String[] getKeys(String key) throws Exception{
		String[] a = null ;
		ShardedJedis jedis = shardedPool.getResource();
		try {
			Collection<Jedis> js = (Collection<Jedis>) jedis.getAllShards();
		      for(Jedis j:js){
		    	  Set<String> keys = j.keys(""+this.project+"_"+this.trade+"_"+key);
		    	  a=keys.toArray(new String[keys.size()]);
		      }
		} catch (Exception e) {
			System.out.println("/com/taikang/redis/RedisManager.java getKeys() " + e);
	        e.printStackTrace();
	        throw e;
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return a;
	}
	
	public void clear() throws Exception{
		ShardedJedis jedis = shardedPool.getResource();
		try {
			Collection<Jedis> js = (Collection<Jedis>) jedis.getAllShards();
		      for(Jedis j:js){
		    	  Set<String> keys = j.keys(""+this.project+"_"+this.trade+"*");
		    	  String[] a = new String[1];
		    	  j.del(keys.toArray(a));
//		    	  j.flushDB();
//		    	  j.flushAll();
		      }
		} catch (Exception e) {
			System.out.println("/com/taikang/redis/RedisManager.java clear() " + e);
	        e.printStackTrace();
	        throw e;
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
	}
	
	public static ShardedJedisPool getPool(){
		return shardedPool;
	}
	
	public  int getSize(){
		ShardedJedis jedis = null;
	    int result = 0;
	    boolean borrowOrOprSuccess = true;
	    try {
	      jedis   = shardedPool.getResource();
	      List<Jedis> js = (List<Jedis>) jedis.getAllShards();
	      for(Jedis j:js){
	    	  result+= Integer.valueOf(j.dbSize().toString());
	      }
	    } catch (JedisConnectionException e) {
	      borrowOrOprSuccess = false;
	      if (jedis != null)
	    	  shardedPool.returnBrokenResource(jedis);
	    } finally {
	      if (borrowOrOprSuccess)
	    	  shardedPool.returnResource(jedis);
	    }
	    return result;

	}
	
	/*
	 * 设置KEY的过期时间_以Byte[]形式的KEY
	 */
	public void setExpireByte(String key,int seconds){
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
				jedis.expire(realKey.getBytes(), seconds);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java setExpireByte() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
	}
	
	/*
	 * 设置KEY的过期时间_以String形式的KEY
	 */
	public void setExpireString(String key,int seconds){
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
				jedis.expire(realKey,seconds);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java setExpireString() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
	}
	
	public long incr(String key){
		long incNum = 0L;
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			incNum = jedis.incr(realKey);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java incr() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return incNum;
	}
	public String getByStringKey(String key){
		String val = "";
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			val = jedis.get(realKey);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java getByStringKey() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return val;
	}
	
	public String setByStringKey(String key,String value){
		String val = "";
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			val = jedis.set(realKey,value);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java setByStringKey() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return val;
	}

	public String setByStringKey(String key,String value,int seconds){
		String val = "";
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			val = jedis.setex(realKey, seconds, value);
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java setByStringKey() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return val;
	}

	/**
	 * 当key 不存在时，返回-2 。
	 * 当key 存在但没有设置剩余生存时间时，返回-1 。
	 * 否则，以秒为单位，返回key 的剩余生存时间。
	 * @param key
	 * @return
	 */
	public long ttl(String key){
		long s = -1;
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try {
			s = jedis.ttl(realKey.getBytes());
		} catch (Exception e) {
	        System.out.println("/com/taikang/redis/RedisManager.java ttl() " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
		return s;
	}
	
	public Object getValue(String key)
	{
		long start = System.currentTimeMillis();
		Object ret = null;
		//Jedis jedis = pool.getResource(); 
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//去redis中取回序列化后的对象
			//System.out.println(realKey);
			byte[] object = jedis.get(realKey.getBytes());
			//取回的对象反序列化
			if(object != null)
			{
				ret = SerializeUtil.unserialize(object);
			}
			
        }
        catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java getValue " + e);
	        e.printStackTrace();
        }
        finally
        {
        	shardedPool.returnResource(jedis); 
        }
        //System.out.println("[redis] getValue " + (System.currentTimeMillis() - start));
		return ret;
	}
	
	public void setValue(String key, Object value)
	{
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			byte[] object = SerializeUtil.serialize(value);
			//把序列化后的对象存入redis
			jedis.set(realKey.getBytes(), object);
			//jedis.sadd(realKey.getBytes(), object);
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setValue " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
		
	}
	
	public void setValue(String key, Object value,int seconds)
	{
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			byte[] object = SerializeUtil.serialize(value);
			//把序列化后的对象存入redis
			jedis.set(realKey.getBytes(), object);
			//jedis.sadd(realKey.getBytes(), object);
			jedis.expire(realKey.getBytes(), seconds);//seconds秒过期
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setValue " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
		
	}

	//缓存成功返回1； 失败返回0（key值已存在）
	public long setValueNX(String key, Object value)
	{
		long ret = 0;
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			byte[] object = SerializeUtil.serialize(value);
			//把序列化后的对象存入redis
			ret = jedis.setnx(realKey.getBytes(), object);
			//jedis.sadd(realKey.getBytes(), object);
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setValueNX " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
		return ret;
	}
	
	//缓存成功返回1； 失败返回0（key值已存在）
	public long setValueNX(String key, Object value, int seconds)
	{
		long ret = 0;
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			byte[] object = SerializeUtil.serialize(value);
			//把序列化后的对象存入redis
			ret = jedis.setnx(realKey.getBytes(), object);
			//jedis.sadd(realKey.getBytes(), object);
			if(ret == 1)
			{
				jedis.expire(realKey.getBytes(), seconds);//seconds秒过期
			}
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setValueNX " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
		return ret;
	}	
	
	public String setIncr(String key)
	{
		String ret = "0";
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			//把序列化后的对象存入redis
			ret = "" + jedis.incr(realKey);
			//jedis.sadd(realKey.getBytes(), object);
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setIncr " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
        
        return ret;
	}
	
	public String getIncr(String key)
	{
		String ret = "0";
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			//把序列化后的对象存入redis
			ret = jedis.get(realKey);
			if(ret == null)
			{
				ret = "0";
			}
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java getIncr " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
        return ret;
	}
	
	public void del(String key)
	{
		long start = System.currentTimeMillis();
		ShardedJedis jedis = shardedPool.getResource();
		try
        {
			//将工程名，业务名和KEY拼成在redis中真实储存的key
			String realKey = this.project+"_"+this.trade+"_"+key;
			//将要存储的对象序列化
			//把序列化后的对象存入redis
			jedis.del(realKey);
        }
		catch (Exception e)
        {
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java del " + e);
	        e.printStackTrace();
        }
        finally
        {
        	//System.out.println("[redis] setValue " + (System.currentTimeMillis() - start));
        	shardedPool.returnResource(jedis); 
        }
	}
	
	public void setInAllServer(String key, String value,int s)
	{
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try
		{
			Collection<Jedis> js = (Collection<Jedis>) jedis.getAllShards();
			for(Jedis j : js)
			{
				j.set(realKey, value);
				j.expire(realKey, s);
			}
		}
		catch (Exception e)
		{
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setInAllServer " + e);
	        e.printStackTrace();
		}
		finally
		{
			shardedPool.returnResource(jedis);
		}
	}
	
	public void setInAllServer(String key, String value)
	{
		ShardedJedis jedis = shardedPool.getResource();
		String realKey = this.project+"_"+this.trade+"_"+key;
		try
		{
			Collection<Jedis> js = (Collection<Jedis>) jedis.getAllShards();
			for(Jedis j : js)
			{
				j.set(realKey, value);
			}
		}
		catch (Exception e)
		{
	        System.out.println("/externalsource/java/src/com/taikang/redis/RedisManager.java setInAllServer " + e);
	        e.printStackTrace();
		}
		finally
		{
			shardedPool.returnResource(jedis);
		}
	}
	
}
