package com.demo.common.cache;

@SuppressWarnings("rawtypes")
public class CacheInfo
{
	//缓存的名字，一般为service的名字
	private String serviceName;
	private String key;
	private Object value;
	private Class valueClass;
	public String getServiceName()
	{
		return serviceName;
	}
	public void setServiceName(String serviceName)
	{
		this.serviceName = serviceName;
	}
	public String getKey()
	{
		return key;
	}
	public void setKey(String key)
	{
		this.key = key;
	}
	public Object getValue()
	{
		return value;
	}
	public void setValue(Object value)
	{
		this.value = value;
		setValueClass(value.getClass());
	}
	
	public Class getValueClass()
	{
		return this.valueClass;
	}
	
	public void setValueClass(Class valueClass)
	{
		this.valueClass = valueClass;
	}
	public CacheInfo(String serviceName, String key, Object value)
	{
		this.serviceName = serviceName;
		this.key = key;
		this.value = value;
		setValueClass(value.getClass());
	}
	
	public CacheInfo()
	{
	}
	@Override
	public String toString()
	{
		return "CacheInfo [ServiceName=" + serviceName + ", key=" + key
				+ ", value=" + value + ", valueClass=" + valueClass + "]";
	}
	
	
}
