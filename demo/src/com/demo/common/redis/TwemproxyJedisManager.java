package com.demo.common.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

public class TwemproxyJedisManager implements Cache
{
	private static final Logger logger = LoggerFactory.getLogger(TwemproxyJedisManager.class);

	/**
     * redis 缓存
     * 
     * 		         与RedisManager区别是不再使用ShardedJedisPool进行分片读写，
     *         而是通过JedisPool与redis中间代理Twemproxy建立连接池，通过Twemproxy实现分片读写
     *         twemproxy是 Twitter 开源出来的 Redis 和 Memcached 代理 其功能：
     *         通过代理的方式减少缓存服务器的连接数。 自动在多台缓存服务器间共享数据。 通过不同的策略与散列函数支持一致性散列。
     *         通过配置的方式禁用失败的结点。 运行在多个实例上，客户端可以连接到首个可用的代理服务器。
     *         支持请求的流式与批处理，因而能够降低来回的消耗。 其缺点： 不支持针对多个值的操作，比如取sets的子交并补等。
     *         不支持Redis的事务操作。 错误消息、日志信息匮乏，排查问题困难。
     */
	private String id;
	// private static JedisPool pool;
	private static JedisPool jedisPool;

	private String projecttrade;

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	/**
     * @param project
     *            工程名
     * @param trade
     *            业务名
     */
	public TwemproxyJedisManager(String project, String trade)
	{
		projecttrade = project + "_" + trade + "_";
	}

	/**
     * @param project
     *            工程名
     * @param trade
     *            业务名
     */
	public TwemproxyJedisManager(String id)
	{
		this.id = id;
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>MybatisRedisCache:id=" + id);
	}

	static
	{
		jedisPool = TwemproxyJedisPoolFactory.getInstance().getJedisPool("JedisPool");
	}

	protected boolean handleJedisException(JedisException jedisException)
	{
		if (jedisException instanceof JedisConnectionException)
		{
			logger.error("Redis connection " + " lost.", jedisException);
		} else if (jedisException instanceof JedisDataException)
		{
			if ((jedisException.getMessage() != null) && (jedisException.getMessage().indexOf("READONLY") != -1))
			{
				logger.error("Redis connection " + " are read-only slave.", jedisException);
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

	protected void destroyJedis(Jedis jedis)
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

	private <T> T execute(TwemproxyJedisCallback<T> callback)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			return callback.doWithRedis(jedis, this.projecttrade);
		} catch (JedisException e)
		{
			handleJedisException(e);
			throw e;
		} finally
		{
			if (jedis!=null)
			{
				jedis.close();
			}
		}
	}

	/**
     * 缓存java bean 有性能瓶颈
     */
	public String getString(final String key)
	{
		return execute(new TwemproxyJedisCallback<String>()
		{

			public String doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.get(projecttrade + key);
			}
		});
	}

	/**
     * 缓存java bean 有性能瓶颈
     */
	public Object setString(final String key, final String value)
	{
		return execute(new TwemproxyJedisCallback<String>()
		{

			public String doWithRedis(Jedis jedis, String projecttrade)
			{
				jedis.set(projecttrade + key, value);
				return null;
			}
		});
	}

	/**
     * 缓存java bean 有性能瓶颈
     */
	public Object setString(final String key, final String value, final int seconds)
	{
		return execute(new TwemproxyJedisCallback<String>()
		{

			public String doWithRedis(Jedis jedis, String projecttrade)
			{
                jedis.setex(projecttrade + key, seconds, value);
				return null;
			}
		});
	}
	
	public Object setString(final String key, final String value, final int times, final TimeUnit unit) {
		long seconds = unit.toSeconds(times);
		return setString(key, value, (seconds > 0x7fffffffL)?0x7fffffff:(int)seconds);
	}

	/**
     * 缓存成功返回1； 失败返回0（key值已存在）
     */
	public long setStringIncr(final String key, final String value)
	{
		return  execute(new TwemproxyJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.setnx(projecttrade + key, value);
			}
		});
	}

	/**
     * 缓存成功返回1； 失败返回0（key值已存在）
     */
	public long setStringIncr(final String key, final String value, final int seconds)
	{
		return  execute(new TwemproxyJedisCallback<Long>()
		{

			@Override
			public Long doWithRedis(Jedis jedis, String projecttrade)
			{
				String realKey = projecttrade + key;
                jedis.setex(realKey, seconds, value);
				return null;
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
		return execute(new TwemproxyJedisCallback<String>()
		{
			public String doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.getSet(projecttrade + key, value);
			}
		});
	}

	/**
     * 删除指定缓存
     */
	public Object delValue(final String key)
	{
		return execute(new TwemproxyJedisCallback<String>()
		{

			public String doWithRedis(Jedis jedis, String projecttrade)
			{
				jedis.del(projecttrade + key);
				return null;
			}
		});

	}

	/**
     * 计数器
     */
	public long incr(final String key)
	{
		return execute(new TwemproxyJedisCallback<Long>()
		{
			public Long doWithRedis(Jedis jedis, String projecttrade)
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
		return  execute(new TwemproxyJedisCallback<Long>()
		{
			public Long doWithRedis(Jedis jedis, String projecttrade)
			{
				long ret = jedis.incr(projecttrade + key);
				jedis.expire(projecttrade + key, seconds);
				return ret;
			}
		});
	}

	/**
     * 计数器,自定义增长长度
     */
	public long incrBy(final String key, final long loadThreshold)
	{
		return execute(new TwemproxyJedisCallback<Long>()
		{
			public Long doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.incrBy(projecttrade + key, loadThreshold);
			}
		});
	}
	
	public long decr(final String key)
	{
		return execute(new TwemproxyJedisCallback<Long>()
		{
			public Long doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.decr(projecttrade + key);
			}
		});
	}

	@Override
	public void clear()
	{

	}

	@Override
	public String getId()
	{
		return this.id;
	}

	@Override
	public Object getObject(final Object key)
	{
		return execute(new TwemproxyJedisCallback<Object>()
		{
			@Override
			public Object doWithRedis(Jedis jedis, String projecttrade)
			{
				Object value = SerializeUtil.unserialize(jedis.get(SerializeUtil.serialize(key.toString())));
				logger.debug("getObject key={},value={}", key, value);
				return value;
			}
		});
	}

	@Override
	public ReadWriteLock getReadWriteLock()
	{
		return readWriteLock;
	}

	@Override
	public int getSize()
	{
		return execute(new TwemproxyJedisCallback<Integer>()
		{
			@Override
			public Integer doWithRedis(Jedis jedis, String projecttrade)
			{
				return Integer.valueOf(jedis.dbSize().toString());
			}
		});
	}

	@Override
	public void putObject(final Object key, final Object value)
	{
		logger.debug("putObject key:{}, value{}", key, value);
		execute(new TwemproxyJedisCallback<Object>()
		{
			@Override
			public Object doWithRedis(Jedis jedis, String projecttrade)
			{
                jedis.setex(SerializeUtil.serialize(key.toString()), 600, SerializeUtil.serialize(value));
				return null;
			}
		});

	}

	@Override
	public Object removeObject(final Object key)
	{
		return execute(new TwemproxyJedisCallback<Object>()
		{
			@Override
			public Object doWithRedis(Jedis jedis, String projecttrade)
			{
				return jedis.del(key.toString());
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
				e.printStackTrace();
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
				e.printStackTrace();
			}
			return null;
		}
	}
}