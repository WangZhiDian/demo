package com.demo.common.cache;

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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;

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

	//是否允许空值，guava不支持null，所以有这个
	private boolean allowNullValues = true;

	//是否允许自动回滚:在执行写入操作的时候，如果使用了spring的声明式事务，发生异常时会像数据库一样自动回滚
	private boolean transactionAware = false;
	public void setTransactionAware(boolean transactionAware)
	{
		this.transactionAware = transactionAware;
	}

	public boolean isTransactionAware()
	{
		return this.transactionAware;
	}

	public GuavaCacheManager()
	{
	}

	public GuavaCacheManager(String... cacheNames)
	{
		setCacheNames(Arrays.asList(cacheNames));
	}

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

	public void setCacheBuilder(CacheBuilder<Object, Object> cacheBuilder)
	{
		Assert.notNull(cacheBuilder, "CacheBuilder must not be null");
		doSetCacheBuilder(cacheBuilder);
	}

	public void setCacheBuilderSpec(CacheBuilderSpec cacheBuilderSpec)
	{
		doSetCacheBuilder(CacheBuilder.from(cacheBuilderSpec));
	}
	public void setCacheSpecification(String cacheSpecification)
	{
		doSetCacheBuilder(CacheBuilder.from(cacheSpecification));
	}

	public void setCacheLoader(CacheLoader<Object, Object> cacheLoader)
	{
		if (!ObjectUtils.nullSafeEquals(this.cacheLoader, cacheLoader))
		{
			this.cacheLoader = cacheLoader;
			refreshKnownCaches();
		}
	}

	public void setAllowNullValues(boolean allowNullValues)
	{
		if (this.allowNullValues != allowNullValues)
		{
			this.allowNullValues = allowNullValues;
			refreshKnownCaches();
		}
	}

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

	private void refreshKnownCaches()
	{
		for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet())
		{
			entry.setValue(createGuavaCache(entry.getKey()));
		}
	}

	protected Cache decorateCache(Cache cache)
	{
		return (isTransactionAware() ? new TransactionAwareCacheDecorator(cache)
				: cache);
	}

}
