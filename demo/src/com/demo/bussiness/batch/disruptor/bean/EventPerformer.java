package com.demo.bussiness.batch.disruptor.bean;

/** 
 * @author 王文超
 * @Description 处理具体业务逻辑的接口
 * @time 2016年5月10日 下午4:42:52 
 * @param <V>
 */
public interface EventPerformer<V>
{
	/**
	 * 处理数据的方法
	 * @param value 数据
	 */
	void handle(V value);
}
