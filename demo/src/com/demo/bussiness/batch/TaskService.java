/**
 * 
 */
package com.demo.bussiness.batch;

/**
 * @author zhaopuqing
 * @created 2015年10月27日 下午4:30:29
 */
public interface TaskService
{
	/**
	 * 停止任务
	 */
	void stop();

	/**
	 * 启动任务
	 */
	void start();

	/**
	 * 判断任务是否正在运行
	 * 
	 * @return 正在运行返回 true，否则返回false
	 */
	boolean isRunning();

}
