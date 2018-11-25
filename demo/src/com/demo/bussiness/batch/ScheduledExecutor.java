/**
 * 
 */
package com.demo.bussiness.batch;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.demo.bussiness.batch.manager.ParamControllable;

import it.sauronsoftware.cron4j.Scheduler;

/**
 * 定时执行器
 * 
 * @author zhaopuqing
 * @created 2015年10月27日 下午6:29:26
 */
public abstract class ScheduledExecutor extends AbstractExecutor
{
	protected ScheduledExecutorService scheduledExecutor;

	// 符合crontab属性的定时任务
	protected Scheduler scheduler;

	// 定时执行的crontab格式的表达式， 使用crontab执行周期性任务，最小间隔时间是分钟.
	// 具体格式是: min hour day month week
	// 比如表示8点到23点期间，每隔1分钟执行一次: * 8-23 * * *
	// 比如表示8点到23点期间，每隔5分钟执行一次: */5 8-23 * * *
	@ParamControllable(desc = "Crontab执行周期(分钟)")
	protected String crontabRegex = null;

	/**
	 * 多久执行一次任务，单位毫秒
	 */
	@ParamControllable(desc = "执行间隔(毫秒)")
	protected int runInterval = 3000;

	/**
	 * 启动至今执行的次数
	 */
	protected int runCount;

	public void stop()
	{
		super.stop();
		crontabRegex = null;
		if (scheduler != null)
		{
			scheduler.stop();
		}
		if (scheduledExecutor != null)
		{
			scheduledExecutor.shutdown();
		}
	}

	public void start()
	{
		logger.info("启动定时任务[" + getName() + "]");
		scheduledExecutor = Executors.newScheduledThreadPool(1);
		if (crontabRegex != null)
		{// 当crontab表达式不为空时，使用cron4j来执行读取数据任务
			scheduler = new Scheduler();
			scheduler.schedule(crontabRegex, new Worker());
			scheduler.start();
		} else if (runInterval > 0)
		{
			scheduledExecutor.scheduleAtFixedRate(new Worker(), 0, this.runInterval, TimeUnit.MILLISECONDS);
		}

	}

	class Worker implements Runnable
	{

		public void run()
		{
			try
			{
				runCount++;
				logger.info("定时执行器[" + getName() + "]开始执行，本次是启动后第" + runCount + "次执行");
				long time = System.currentTimeMillis();
				handle();
				long runTime = (System.currentTimeMillis() - time) / 1000;
				logger.info("定时执行器[" + getName() + "],执行结束，本次耗时" + runTime + "秒");
			} catch (Exception e)
			{
				logger.error(e.getMessage());
			}
		}

	}

	/**
	 * 执行任务
	 * 
	 * @return 执行结果，成功返回true 失败返回false
	 */
	protected abstract boolean handle();

}
