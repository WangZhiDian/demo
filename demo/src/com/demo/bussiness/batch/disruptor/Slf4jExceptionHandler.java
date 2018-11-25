package com.demo.bussiness.batch.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.ExceptionHandler;

/** 
 * @author 王文超
 * @Description 这个类定义了disruptor发生异常时候的处理策略，默认使用slf4j进行记录日志，并且抛出执行过程中的异常方便处理。
 * @time 2016年5月10日 下午4:44:01 
 * @param <T>
 */
public class Slf4jExceptionHandler<T>  implements ExceptionHandler<T>
{
	private static final Logger LOGGER = LoggerFactory.getLogger(Slf4jExceptionHandler.class.getName());
	
	private final Logger logger;
	
	public Slf4jExceptionHandler()
	{
		this.logger=LOGGER;
	}

	/**
	 * 执行过程中发生异常的处理方法
	 */
	@Override
	public void handleEventException(Throwable ex, long sequence, T event)
	{
		logger.error("Exception processing: " + sequence + " " + event,ex);
		throw new RuntimeException(ex);
	}

	/**
	 * 刚开始执行时发生异常的处理方法
	 */
	@Override
	public void handleOnStartException(Throwable ex)
	{
		logger.error("Exception during onStart()",ex);
	}

	/**
	 * 结束时候发生异常的处理方法
	 */
	@Override
	public void handleOnShutdownException(Throwable ex)
	{
		logger.error("Exception during onShutdown()",ex);
	}

}
