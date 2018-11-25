package com.demo.common.cache;

import java.io.Serializable;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
public class GuavaCache implements Cache
{

	private static final Object NULL_HOLDER = new NullHolder();

	private final String name;

	private final com.google.common.cache.Cache<Object, Object> cache;

	private final boolean allowNullValues;

	public GuavaCache(String name,
			com.google.common.cache.Cache<Object, Object> cache)
	{
		this(name, cache, true);
	}

	public GuavaCache(String name,
			com.google.common.cache.Cache<Object, Object> cache,
			boolean allowNullValues)
	{
		Assert.notNull(name, "Name must not be null");
		Assert.notNull(cache, "Cache must not be null");
		this.name = name;
		this.cache = cache;
		this.allowNullValues = allowNullValues;
	}

	@Override
	public final String getName()
	{
		return this.name;
	}

	@Override
	public final com.google.common.cache.Cache<Object, Object> getNativeCache()
	{
		return this.cache;
	}

	public final boolean isAllowNullValues()
	{
		return this.allowNullValues;
	}

	@Override
	public ValueWrapper get(Object key)
	{
		if (this.cache instanceof LoadingCache)
		{
			try
			{
				Object value = ((LoadingCache<Object, Object>) this.cache)
						.get(key);
				return toWrapper(value);
			} catch (ExecutionException ex)
			{
				throw new UncheckedExecutionException(ex.getMessage(), ex);
			}
		}
		return toWrapper(this.cache.getIfPresent(key));
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type)
	{
		Object value = fromStoreValue(this.cache.getIfPresent(key));
		if (value != null && type != null && !type.isInstance(value))
		{
			throw new IllegalStateException(
					"Cached value is not of required type [" + type.getName()
							+ "]: " + value);
		}
		return (T) value;
	}

	@Override
	public void put(Object key, Object value)
	{
		if (key != null && value != null)
		{
			this.cache.put(key, toStoreValue(value));
		}
	}

	public void singlePut(String key, Object value)
	{
		if (key != null && value != null)
		{
			this.cache.put(key, toStoreValue(value));
		}
	}

	public ValueWrapper putIfAbsent(Object key, final Object value)
	{
		try
		{
			PutIfAbsentCallable callable = new PutIfAbsentCallable(value);
			Object result = this.cache.get(key, callable);
			return (callable.called ? null : toWrapper(result));
		} catch (ExecutionException ex)
		{
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void evict(Object key)
	{
		if (key != null)
		{
			this.cache.invalidate(key);
		}
	}

	public void singleEvict(Object key)
	{
		this.cache.invalidate(key);
	}

	@Override
	public void clear()
	{
		this.cache.invalidateAll();
	}
	protected Object fromStoreValue(Object storeValue)
	{
		if (this.allowNullValues && storeValue == NULL_HOLDER)
		{
			return null;
		}
		return storeValue;
	}

	protected Object toStoreValue(Object userValue)
	{
		if (this.allowNullValues && userValue == null)
		{
			return NULL_HOLDER;
		}
		return userValue;
	}

	private ValueWrapper toWrapper(Object value)
	{
		return (value != null ? new SimpleValueWrapper(fromStoreValue(value))
				: null);
	}

	@SuppressWarnings("serial")
	private static class NullHolder implements Serializable
	{

		private Object readResolve()
		{
			return NULL_HOLDER;
		}
	}

	private class PutIfAbsentCallable implements Callable<Object>
	{

		private final Object value;

		private boolean called;

		public PutIfAbsentCallable(Object value)
		{
			this.value = value;
		}

		@Override
		public Object call() throws Exception
		{
			this.called = true;
			return toStoreValue(this.value);
		}
	}

}
