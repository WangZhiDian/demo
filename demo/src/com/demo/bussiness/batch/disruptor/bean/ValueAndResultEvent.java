/** 
 * @author 王文超 
 * @time 2016年3月23日 下午3:30:17  
 * 类说明 
*/ 
package com.demo.bussiness.batch.disruptor.bean;

/** 
 * @author 王文超
 * @Description 带有处理结果的事件发布器
 * @time 2016年3月23日 下午3:30:17 
 */
public class ValueAndResultEvent<T,V>
{
	private T value;
	private V result;
	public T getValue()
	{
		return value;
	}
	public void setValue(T value)
	{
		this.value = value;
	}
	public V getResult()
	{
		return result;
	}
	public void setResult(V result)
	{
		this.result = result;
	}
	
}
