package cn.model.maven.commons.cache;

/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 注意！此注解缓存不能在非public方法使用，使用注解后在一个类里面用this.的方式调用无效
 * <p>
 * 基于注解的缓存，使用时候直接在方法上加入@cacheable(value="EventHeraService")即可
 * <p>
 * <B>注解@Cacheable：先从缓存查询，找不到再从实际方法中查询</B> <br>
 * <pre>
 * value：指缓存的名字，不能为空，不同名字对应不同缓存，建议把每个service的名字作为value的值。
 * key：可以指定缓存的key，默认为空，既表示使用方法的参数类型及参数值作为key，支持SpEL。
 * condition：触发条件，只有满足条件的情况才会加入缓存，默认为空，既表示全部都加入缓存，支持SpEL
 * </pre>
 * <B>注解@CachePut：从实际方法中查询，再放入缓存中</B> <br>
 * <pre>
 * value：缓存位置名称，不能为空，同上
 * key：缓存的key，默认为空，同上
 * condition：触发条件，只有满足条件的情况才会清除缓存，默认为空，支持SpEL
 * allEntries：true表示清除value中的全部缓存，默认为false
 * </pre>
 * <B>注解@CacheEvict：</B> <br>
 * <pre>
 * value：缓存位置名称，不能为空，同上
 * key：缓存的key，默认为空，同上
 * condition：触发条件，只有满足条件的情况才会清除缓存，默认为空，支持SpEL
 * allEntries：true表示清除value中的全部缓存，默认为false
 * </pre>
 * 具有事务回滚功能，在配置文件applicationContext-cache.xml中把transactionAware的值设置为true，该缓存即可使用声明式事务@Transactional，会具有和数据库一样的事务回滚功能
 * <p>
 * 需要 Google Guava 12.0 或者更高.
 *
 * @author wangwc11
 * @see GuavaCache
 */
public class GuavaCacheManager implements CacheManager
{

	private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(
			16);

	private boolean dynamic = true;

	private CacheBuilder<Object, Object> cacheBuilder = CacheBuilder
			.newBuilder().concurrencyLevel(1000).initialCapacity(100)
			.maximumSize(10000)
			// 设置写缓存后半天过期
			.expireAfterWrite(12, TimeUnit.HOURS);

	private CacheLoader<Object, Object> cacheLoader;

	private boolean allowNullValues = true;

	private boolean transactionAware = false;
	/**
	 * Set whether this CacheManager should expose transaction-aware Cache
	 * objects.
	 * <p>
	 * Default is "false". Set this to "true" to synchronize cache put/evict
	 * operations with ongoing Spring-managed transactions, performing the
	 * actual cache put/evict operation only in the after-commit phase of a
	 * successful transaction.
	 */
	public void setTransactionAware(boolean transactionAware)
	{
		this.transactionAware = transactionAware;
	}

	/**
	 * Return whether this CacheManager has been configured to be
	 * transaction-aware.
	 */
	public boolean isTransactionAware()
	{
		return this.transactionAware;
	}

	/**
	 * Construct a dynamic GuavaCacheManager, lazily creating cache instances as
	 * they are being requested.
	 */
	public GuavaCacheManager()
	{
	}

	/**
	 * Construct a static GuavaCacheManager, managing caches for the specified
	 * cache names only.
	 */
	public GuavaCacheManager(String... cacheNames)
	{
		setCacheNames(Arrays.asList(cacheNames));
	}

	/**
	 * Specify the set of cache names for this CacheManager's 'static' mode.
	 * <p>
	 * The number of caches and their names will be fixed after a call to this
	 * method, with no creation of further cache regions at runtime.
	 * <p>
	 * Calling this with a {@code null} collection argument resets the mode to
	 * 'dynamic', allowing for further creation of caches again.
	 */
	public void setCacheNames(Collection<String> cacheNames)
	{
		if (cacheNames != null)
		{
			for (String name : cacheNames)
			{
				this.cacheMap.put(name, createGuavaCache(name));
			}
			this.dynamic = false;
		} else
		{
			this.dynamic = true;
		}
	}

	/**
	 * Set the Guava CacheBuilder to use for building each individual
	 * {@link GuavaCache} instance.
	 * 
	 * @see #createNativeGuavaCache
	 * @see CacheBuilder#build()
	 */
	public void setCacheBuilder(CacheBuilder<Object, Object> cacheBuilder)
	{
		Assert.notNull(cacheBuilder, "CacheBuilder must not be null");
		doSetCacheBuilder(cacheBuilder);
	}

	/**
	 * Set the Guava CacheBuilderSpec to use for building each individual
	 * {@link GuavaCache} instance.
	 *
	 * @see #createNativeGuavaCache
	 * @see CacheBuilder#from(CacheBuilderSpec)
	 */
	public void setCacheBuilderSpec(CacheBuilderSpec cacheBuilderSpec)
	{
		doSetCacheBuilder(CacheBuilder.from(cacheBuilderSpec));
	}

	/**
	 * Set the Guava cache specification String to use for building each
	 * individual {@link GuavaCache} instance. The given value needs to comply
	 * with Guava's {@link CacheBuilderSpec} (see its javadoc).
	 *
	 * @see #createNativeGuavaCache
	 * @see CacheBuilder#from(String)
	 */
	public void setCacheSpecification(String cacheSpecification)
	{
		doSetCacheBuilder(CacheBuilder.from(cacheSpecification));
	}

	/**
	 * Set the Guava CacheLoader to use for building each individual
	 * {@link GuavaCache} instance, turning it into a LoadingCache.
	 *
	 * @see #createNativeGuavaCache
	 * @see CacheBuilder#build(CacheLoader)
	 * @see com.google.common.cache.LoadingCache
	 */
	public void setCacheLoader(CacheLoader<Object, Object> cacheLoader)
	{
		if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader))
		{
			this.cacheLoader = cacheLoader;
			refreshKnownCaches();
		}
	}

	/**
	 * Specify whether to accept and convert {@code null} values for all caches
	 * in this cache manager.
	 * <p>
	 * Default is "true", despite Guava itself not supporting {@code null}
	 * values. An internal holder object will be used to store user-level
	 * {@code null}s.
	 */
	public void setAllowNullValues(boolean allowNullValues)
	{
		if (this.allowNullValues != allowNullValues)
		{
			this.allowNullValues = allowNullValues;
			refreshKnownCaches();
		}
	}

	/**
	 * Return whether this cache manager accepts and converts {@code null}
	 * values for all of its caches.
	 */
	public boolean isAllowNullValues()
	{
		return this.allowNullValues;
	}

	@Override
	public Collection<String> getCacheNames()
	{
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	@Override
	public Cache getCache(String name)
	{
		Cache cache = this.cacheMap.get(name);
		if (cache == null && this.dynamic)
		{
			cache = createGuavaCache(name);
			this.cacheMap.put(name, cache);
		}
		return cache;
	}

	/**
	 * Create a new GuavaCache instance for the specified cache name.
	 * 
	 * @param name
	 *            the name of the cache
	 * @return the Spring GuavaCache adapter (or a decorator thereof)
	 */
	protected Cache createGuavaCache(String name)
	{
		Cache cache = new GuavaCache(name, createNativeGuavaCache(name),
				isAllowNullValues());
		if (cache != null)
		{
			cache = decorateCache(cache);
		}
		return cache;
	}

	/**
	 * Create a native Guava Cache instance for the specified cache name.
	 * 
	 * @param name
	 *            the name of the cache
	 * @return the native Guava Cache instance
	 */
	protected com.google.common.cache.Cache<Object, Object> createNativeGuavaCache(
			String name)
	{
		if (this.cacheLoader != null)
		{
			return this.cacheBuilder.build(this.cacheLoader);
		} else
		{
			return this.cacheBuilder.build();
		}
	}

	private void doSetCacheBuilder(CacheBuilder<Object, Object> cacheBuilder)
	{
		if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder))
		{
			this.cacheBuilder = cacheBuilder;
			refreshKnownCaches();
		}
	}

	/**
	 * Create the known caches again with the current state of this manager.
	 */
	private void refreshKnownCaches()
	{
		for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet())
		{
			entry.setValue(createGuavaCache(entry.getKey()));
		}
	}

	/**
	 * Decorate the given Cache object if necessary.
	 * It used to support Transaction
	 * @param cache the Cache object to be added to this CacheManager
	 * @return the decorated Cache object to be used instead,
	 * or simply the passed-in Cache object by default
	 */
	protected Cache decorateCache(Cache cache)
	{
		return (isTransactionAware() ? new TransactionAwareCacheDecorator(cache)
				: cache);
	}

}
