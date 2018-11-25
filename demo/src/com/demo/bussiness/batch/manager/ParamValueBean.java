/**
 * 
 */
package com.demo.bussiness.batch.manager;

/**
 * 封装对象属性数据的bean
 * 
 * @author zhaopuqing
 * @created 2015年10月27日 下午5:06:42
 */
public class ParamValueBean
{
	/**
	 * 参数描述
	 */
	private String desc;
	/**
	 * 参数名字
	 */
	private String name;
	/**
	 * 参数值
	 */
	private Object value;

	/**
	 * 参数是否可以热修改，即：在项目启动后运行过程中通过管理页面修改这个参数
	 */
	private boolean writable;

	public boolean isWritable()
	{
		return writable;
	}

	public void setWritable(boolean writable)
	{
		this.writable = writable;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

}
