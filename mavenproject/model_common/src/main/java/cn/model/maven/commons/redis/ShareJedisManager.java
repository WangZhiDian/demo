package cn.model.maven.commons.redis;

import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ShareJedisManager
{
	protected static final Logger logger = LoggerFactory
			.getLogger(ShareJedisManager.class);

	private static ShardedJedisPool shardedPool;

	private String projecttrade;

	/**
	 * @param project
	 *            工程名
	 * @param trade
	 *            业务名
	 */
	public ShareJedisManager(String project, String trade)
	{
		projecttrade = project + "_" + trade + "_";
	}

	protected ShareJedisManager()
	{
	}

	static
	{
		shardedPool = ShareJedisFactory.getInstance().getShardedJedisPool(
				"JedisPool");
	}

	protected boolean handleJedisException(JedisException jedisException)
	{
		if (jedisException instanceof JedisConnectionException)
		{
			logger.error("Redis connection " + " lost.", jedisException);
		} else if (jedisException instanceof JedisDataException)
		{
			if ((jedisException.getMessage() != null)
					&& (jedisException.getMessage().indexOf("READONLY") != -1))
			{
				logger.error("Redis connection " + " are read-only slave.",
						jedisException);
			} else
			{
				// dataException, isBroken=false
				return false;
			}
		} else
		{
			logger.error("Jedis exception happen.", jedisException);
		}
		return true;
	}

	protected void destroyJedis(ShardedJedis jedis)
	{
		if (jedis != null)
		{
			try
			{
				jedis.disconnect();
			} catch (Exception e)
			{
				logger.error("destroyJedis :", e);
			}
		}
	}

	protected <T> T execute(ShareJedisCallback<T> callback)
	{
		ShardedJedis jedis = null;
		try
		{
			jedis = shardedPool.getResource();
			return callback.doWithRedis(jedis, this.projecttrade);
		} catch (JedisException e)
		{
			handleJedisException(e);
			throw e;
		} finally
		{
			if (jedis != null)
			{
				jedis.close();
			}
		}
	}

	/****************************************************** String ***********************************************************/
	/**
	 * 获取String
	 */
	public String getString(final String key)
	{
		return execute(new ShareJedisCallback<String>()
		{

			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.get(projecttrade + key);
			}
		});
	}

	/**
	 * 缓存String 有性能瓶颈
	 */
	public Object setString(final String key, final String value,
			final int seconds)
	{
		return execute(new ShareJedisCallback<String>()
		{

			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.setex(projecttrade + key, seconds, value);
				return null;
			}
		});
	}

	/**
	 * 缓存String 有性能瓶颈
	 */
	public Object setString(final String key, final String value,
			final int interval, final TimeUnit timeUnit)
	{
		long seconds = timeUnit.toSeconds(interval);
		return setString(key, value, (seconds > 0x7fffffffL) ? 0x7fffffff
				: (int) seconds);
	}

	/**
	 * 缓存成功返回1； 失败返回0（key值已存在）
	 */
	public long setnx(final String key, final String value)
	{
		return execute(new ShareJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.setnx(projecttrade + key, value);
			}
		});
	}

	/**
	 * 缓存成功返回1； 失败返回0（key值已存在）
	 */
	public long setnx(final String key, final String value,
			final int seconds)
	{
		return execute(new ShareJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				long ret = jedis.setnx(realKey, value);
				jedis.expire(realKey, seconds);
				return ret;
			}
		});
	}
	
	/**
	 * 设置key值的有效期
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(final String key, final int seconds)
	{
		return execute(new ShareJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				return jedis.expire(realKey, seconds);
			}
		});
	}

	/**
	 * 设置key值的有效期,unix时间戳
	 * @param key
	 * @param unixTime unix时间戳
	 * @return
	 */
	public Long expireAt(final String key, final long unixTime)
	{
		return execute(new ShareJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				return jedis.expireAt(realKey, unixTime);
			}
		});
	}
	
	/**
	 * 原子性的设置该key为指定的value，同时返回该key的原有值
	 * 
	 * @param key
	 * @param value
	 * @return 返回该key的原有值，如果该key知情不存在则返回null
	 */
	public String getSet(final String key, final String value)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.getSet(projecttrade + key, value);
			}
		});
	}

	/****************************************************** List ***********************************************************/
	/**
	 * 在名称为key的list头添加一个值为value的 元素
	 */
	public Object lpush(final String listname, final String value,
			final int seconds)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.lpush(projecttrade + listname, value);
				jedis.expire(projecttrade + listname, seconds);
				return null;
			}
		});
	}

	/**
	 * 在名称为key的list尾添加一个值为value的元素
	 */
	public Object rpush(final String listname, final String value,
			final int seconds)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.rpush(projecttrade + listname, value);
				jedis.expire(projecttrade + listname, seconds);
				return null;
			}
		});
	}

	/**
	 * 返回名称为key的list的长度
	 */
	public long llen(final String listname)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.llen(projecttrade + listname);
			}
		});
	}

	/**
	 * 返回名称为key的list中start至end之间的元素（下标从0开始，下同）
	 */
	public List<String> lrange(final String listname, final long start,
			final long end)
	{
		return execute(new ShareJedisCallback<List<String>>()
		{
			public List<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.lrange(projecttrade + listname, start, end);
			}
		});
	}

	/**
	 * 截取名称为key的list，保留start至end之间的元素
	 */
	public String ltrim(final String listname, final long start, final long end)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.ltrim(projecttrade + listname, start, end);
			}
		});
	}

	/**
	 * 返回名称为key的list中index位置的元素
	 */
	public String lindex(final String listname, final long index)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.lindex(projecttrade + listname, index);
			}
		});
	}

	/**
	 * 给名称为key的list中index位置的元素赋值为value
	 */
	public Object lset(final String listname, final long index,
			final String value, final int seconds)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.lset(projecttrade + listname, index, value);
				jedis.expire(projecttrade + listname, seconds);
				return null;
			}
		});
	}

	/**
	 * 删除count个名称为key的list中值为value的元素。 count为0，删除所有值为value的元素， count>0
	 * 从头至尾删除count个值为value的元素， count<0从尾到头删除|count|个值为value的元素。
	 */
	public Object lrem(final String listname, final long count,
			final String value)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.lrem(projecttrade + listname, count, value);
				return null;
			}
		});
	}

	/**
	 * 返回并删除名称为key的list中的首元素
	 */
	public String lpop(final String listname)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.lpop(projecttrade + listname);
			}
		});
	}

	/**
	 * 返回并删除名称为key的list中的尾元素
	 */
	public String rpop(final String listname)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.rpop(projecttrade + listname);
			}
		});
	}

	/**
	 * lpop 命令的block版本。即 当timeout为0时，若遇到名称为key i的list不存在或该list为空，则命令结束。 如果
	 * timeout>0，则遇到上述情况时，等待timeout秒，如果问题没有解决，则对key i+1开始的list执行pop操作。
	 */
	public List<String> blpop(final int timeout, final String listname)
	{
		return execute(new ShareJedisCallback<List<String>>()
		{
			public List<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.blpop(timeout, projecttrade + listname);
			}
		});
	}

	/**
	 * rpop的block版本。即 当timeout为0时，若遇到名称为key i的list不存在或该list为空，则命令结束。 如果
	 * timeout>0，则遇到上述情况时，等待timeout秒，如果问题没有解决，则对key i+1开始的list执行pop操作。
	 */
	public List<String> brpop(final int timeout, final String listname)
	{
		return execute(new ShareJedisCallback<List<String>>()
		{
			public List<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.brpop(timeout, projecttrade + listname);
			}
		});
	}

	/****************************************************** Set 不重复无序 ***********************************************************/

	/**
	 * 向名称为key的set中添加元素member
	 */
	public Object sadd(final String setname, final int seconds,
			final String... members)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.sadd(projecttrade + setname, members);
				jedis.expire(projecttrade + setname, seconds);
				return null;
			}

		});
	}

	/**
	 * 向名称为key的set中添加元素member
	 */
	public Object srem(final String setname, final String... members)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.srem(projecttrade + setname, members);
				return null;
			}

		});
	}

	/**
	 * 随机返回并删除名称为key的set中一个元素
	 */
	public String spop(final String setname)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.spop(projecttrade + setname);
			}

		});
	}

	/**
	 * 返回名称为key的set的基数
	 */
	public long scard(final String setname)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.scard(projecttrade + setname);
			}

		});
	}

	/**
	 * 测试member是否是名称为key的set的元素
	 */
	public boolean sismember(final String setname, final String members)
	{
		return execute(new ShareJedisCallback<Boolean>()
		{
			public Boolean doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.sismember(projecttrade + setname, members);
			}

		});
	}

	/**
	 * 返回名称为key的set的所有元素
	 */
	public Set<String> smembers(final String setname)
	{
		return execute(new ShareJedisCallback<Set<String>>()
		{
			public Set<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.smembers(projecttrade + setname);
			}
		});
	}

	/**
	 * 返回名称为key的set的所有元素
	 */
	public String srandmember(final String setname)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.srandmember(projecttrade + setname);
			}
		});
	}

	/****************************************************** zSet 不重复自定义排序 ***********************************************************/
	/**
	 * 向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。
	 */
	public Object zadd(final String zsetname, final double score,
			final String member, final int seconds)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.zadd(projecttrade + zsetname, score, member);
				jedis.expire(projecttrade + zsetname, seconds);
				return null;
			}
		});
	}

	/**
	 * 向名称为key的zset中添加元素member，score用于排序。如果该元素已经存在，则根据score更新该元素的顺序。
	 */
	public Object zrem(final String zsetname, final String member)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.zrem(projecttrade + zsetname, member);
				return null;
			}
		});
	}

	/**
	 * 如果在名称为key的zset中已经存在元素member，则该元素的score增加increment；否则向集合中添加该元素，
	 * 其score的值为increment 适合做根据PV排序的热门车系车型列表等
	 */
	public Object zincrby(final String zsetname, final double increment,
			final String member, final int seconds)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.zincrby(projecttrade + zsetname, increment, member);
				jedis.expire(projecttrade + zsetname, seconds);
				return null;
			}
		});
	}

	/**
	 * 返回名称为key的zset（元素已按score从小到大排序）中member元素的rank（即index，从0开始），若没有member元素，返回“
	 * nil”
	 */
	public long zrank(final String zsetname, final String member)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.zrank(projecttrade + zsetname, member);
			}
		});
	}

	/**
	 * 返回名称为key的zset（元素已按score从大到小排序）中member元素的rank（即index，从0开始），若没有member元素，返回“
	 * nil”
	 */
	public long zrevrank(final String zsetname, final String member)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.zrevrank(projecttrade + zsetname, member);
			}
		});
	}

	/**
	 * 返回名称为key的zset（元素已按score从小到大排序）中的index从start到end的所有元素
	 */
	public Set<String> zrange(final String zsetname, final long startindex,
			final long endindex)
	{
		return execute(new ShareJedisCallback<Set<String>>()
		{
			public Set<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.zrange(projecttrade + zsetname, startindex,
						endindex);
			}
		});
	}

	/**
	 * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素
	 */
	public Set<String> zrevrange(final String zsetname, final long startindex,
			final long endindex)
	{
		return execute(new ShareJedisCallback<Set<String>>()
		{
			public Set<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.zrevrange(projecttrade + zsetname, startindex,
						endindex);
			}
		});
	}

	/**
	 * 返回名称为key的zset（元素已按score从大到小排序）中的index从start到end的所有元素
	 */
	public Set<String> zrangeByScore(final String zsetname, final long min,
			final long max)
	{
		return execute(new ShareJedisCallback<Set<String>>()
		{
			public Set<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.zrangeByScore(projecttrade + zsetname, min, max);
			}
		});
	}

	/**
	 * 返回名称为key的zset的基数
	 */
	public long zcard(final String zsetname)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.zcard(projecttrade + zsetname);
			}
		});
	}

	/**
	 * 返回名称为key的zset中元素element的score
	 */
	public double zscore(final String zsetname, final String member)
	{
		return execute(new ShareJedisCallback<Double>()
		{
			public Double doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.zscore(projecttrade + zsetname, member);
			}
		});
	}

	/**
	 * 删除名称为key的zset中rank >= min且rank <= max的所有元素
	 */
	public Object zremrangeByRank(final String zsetname, final long min,
			final long max)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.zremrangeByRank(projecttrade + zsetname, min, max);
				return null;
			}
		});
	}

	/**
	 * 删除名称为key的zset中score >= min且score <= max的所有元素
	 */
	public Object zremrangeByScore(final String zsetname, final long min,
			final long max)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.zremrangeByScore(projecttrade + zsetname, min, max);
				return null;
			}
		});
	}

	/****************************************************** Hash ***********************************************************/
	/**
	 * 向reids中名为mapname的map添加元素
	 */
	public Object hset(final String mapname, final String key,
			final String value)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.hset(projecttrade + mapname, key, value);
				return null;
			}
		});
	}
	
	/**
	 * 向reids中名为mapname的map添加元素
	 */
	public Object hset(final String mapname, final String key,
			final String value, final int seconds)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.hset(projecttrade + mapname, key, value);
				jedis.expire(projecttrade + mapname, seconds);
				return null;
			}
		});
	}

	/**
	 * 取回redis中指定map中的指定key的值
	 */
	public String hget(final String mapname, final String key)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.hget(projecttrade + mapname, key);
			}
		});
	}

	/**
	 * 向reids中名为mapname的map添加多个元素
	 */
	public Object hmset(final String mapname,
			final HashMap<String, String> map, final int seconds)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.hmset(projecttrade + mapname, map);
				jedis.expire(projecttrade + mapname, seconds);
				return null;
			}
		});
	}

	/**
	 * 取回多个redis中指定map中的指定key的值
	 */
	public List<String> hmget(final String mapname, final String... keyn)
	{
		return execute(new ShareJedisCallback<List<String>>()
		{
			public List<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.hmget(projecttrade + mapname, keyn);
			}
		});
	}

	/**
	 * 将名称为key的hash中field的value增加long
	 */
	public Object hincrBy(final String mapname, final String key,
			final long value, final int seconds)
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.hincrBy(projecttrade + mapname, key, value);
				jedis.expire(projecttrade + mapname, seconds);
				return null;
			}
		});
	}

	/**
	 * 名称为key的hash中是否存在键为field的域
	 */
	public Boolean hexists(final String mapname, final String key)
	{
		return execute(new ShareJedisCallback<Boolean>()
		{
			public Boolean doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.hexists(projecttrade + mapname, key);
			}
		});
	}

	/**
	 * 删除名称为key的hash中键为field的域
	 */
	public Long hdel(final String mapname, final String key)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.hdel(projecttrade + mapname, key);
			}
		});
	}

	/**
	 * 返回名称为key的hash中元素个数
	 */
	public long hlen(final String mapname)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.hlen(projecttrade + mapname);
			}
		});
	}

	/**
	 * 返回名称为key的hash中所有键
	 */
	public Set<String> hkeys(final String mapname)
	{
		return execute(new ShareJedisCallback<Set<String>>()
		{
			public Set<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.hkeys(projecttrade + mapname);
			}
		});
	}

	/**
	 * 返回名称为key的hash中所有键对应的value
	 */
	public List<String> hvals(final String mapname)
	{
		return execute(new ShareJedisCallback<List<String>>()
		{
			public List<String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.hvals(projecttrade + mapname);
			}
		});
	}

	/**
	 * 返回名称为key的hash中所有的键（field）及其对应的value
	 */
	public Map<String, String> hgetAll(final String mapname)
	{
		return execute(new ShareJedisCallback<Map<String, String>>()
		{
			public Map<String, String> doWithRedis(ShardedJedis jedis,
					String projecttrade)
			{
				return jedis.hgetAll(projecttrade + mapname);
			}
		});
	}

	//
	// hgetall(key)：返回名称为key的hash中所有的键（field）及其对应的value

	/**
	 * 删除指定缓存
	 */
	public Object delValue(final String key)
	{
		return execute(new ShareJedisCallback<String>()
		{

			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				jedis.del(projecttrade + key);
				return null;
			}
		});

	}

	public Map<String, Object> keys(final String key) {
		return execute(new ShareJedisCallback<Map<String, Object>>() {
			@Override
			public Map<String, Object> doWithRedis(ShardedJedis jedis, String projecttrade) {
				Map<String, Object> map = new HashedMap();
				Collection<Jedis> js = jedis.getAllShards();
				for (Jedis j : js) {
					Set<String> keys = j.keys(projecttrade + "*");
					for(String key : keys) {
						String keyType = j.type(key);
						switch(keyType) {
							case "string":
								String string = j.get(key);
								map.put(key, string);
								break;
							case "list":
								List<String> list = j.lrange(key, 0, -1);
								map.put(key, list);
								break;
							case "set":
								Set<String> set = j.smembers(key);
								map.put(key, set);
								break;
							case "zset":
								Set<String> zset = j.zrange(key, 0, -1);
								map.put(key, zset);
								break;
							case "hash":
								Map<String, String> hash = j.hgetAll(key);
								map.put(key, hash);
								break;
							default:
								break;
						}
					}
				}
				return map;
			}
		});
	}

	public Object delAll()
	{
		return execute(new ShareJedisCallback<String>()
		{
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				Collection<Jedis> js = (Collection<Jedis>)jedis.getAllShards();
				for (Jedis j : js) {
					Set<String> keys = j.keys(projecttrade + "*");
					if(!keys.isEmpty()){
						String[] a = new String[1];
						j.del(keys.toArray(a));
					}
				}
				return null;
			}
		});
	}

	/**
	 * 计数器
	 */
	public long incr(final String key)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.incr(projecttrade + key);
			}
		});
	}

	/**
	 * 计数器,有效期
	 */
	public long incr(final String key, final int seconds)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				long ret = jedis.incr(realKey);
				jedis.expire(realKey, seconds);
				return ret;
			}
		});
	}

	/**
	 * 计数器,自定义增长长度
	 */
	public long incrBy(final String key, final long increment)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.incrBy(projecttrade + key, increment);
			}
		});
	}

	public long decr(final String key)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				return jedis.decr(projecttrade + key);
			}
		});
	}

	public Object getObject(final String key)
	{
		return execute(new ShareJedisCallback<Object>()
		{
			@Override
			public Object doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				Object value = SerializeUtil.unserialize(jedis
						.get((projecttrade + key).getBytes()));
				return value;
			}
		});
	}

	public String setObject(final String key ,final Object obj,final int seconds)
	{
		return execute(new ShareJedisCallback<String>()
		{
			@Override
			public String doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				String ret = jedis.set((projecttrade + key).getBytes(), SerializeUtil.serialize(obj));
				jedis.expire(realKey, seconds);
				return ret;
			}
		});
	}
	public Object ttl(final String key)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				Long ret = jedis.ttl(realKey);
				return ret;
			}
		});
	}

	public Object pttl(final String key)
	{
		return execute(new ShareJedisCallback<Long>()
		{
			@Override
			public Long doWithRedis(ShardedJedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
				Long ret = jedis.pttl(realKey);
				return ret;
			}
		});
	}


	static class SerializeUtil
	{
		public static byte[] serialize(Object object)
		{
			ObjectOutputStream oos = null;
			ByteArrayOutputStream baos = null;
			try
			{
				// 序列化
				baos = new ByteArrayOutputStream();
				oos = new ObjectOutputStream(baos);
				oos.writeObject(object);
				byte[] bytes = baos.toByteArray();
				return bytes;
			} catch (Exception e)
			{
				logger.error("serializeUtil serialize Exception:",e);
			}
			return null;
		}

		public static Object unserialize(byte[] bytes)
		{
			if (bytes == null)
				return null;
			ByteArrayInputStream bais = null;
			try
			{
				// 反序列化
				bais = new ByteArrayInputStream(bytes);
				ObjectInputStream ois = new ObjectInputStream(bais);
				return ois.readObject();
			} catch (Exception e)
			{
				logger.error("serializeUtil unserialize Exception:",e);
			}
			return null;
		}
	}

}