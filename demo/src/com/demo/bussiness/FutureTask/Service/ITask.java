package com.demo.bussiness.FutureTask.Service;

public interface ITask<T>
{
	/**
	 * 执行job任务
	 * @return 执行完job任务
	 */
	public T execute_task();
	
}
