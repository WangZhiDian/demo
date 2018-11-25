package com.demo.bussiness.batch.disruptor;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.InitializingBean;

import com.demo.common.filemonitor.PropertiesFileMonitor;


/**
 * @Description 实现了和SyncBatchExecutor相同的功能:每隔一段时间一个单线程会读一批数，多线程处理后，将处理结果单线程进行更新
 * @time 2016年3月23日 下午5:02:25
 */
public abstract class ScheduledDisruptorBatch<T, V> extends
		ProcessResultDisruptorBatch<T, V> implements InitializingBean
{
	// 读取数据任务的间隔时间(毫秒)
	private volatile int readInterval = 5000;
	// 符合crontab属性的定时任务
	protected Scheduler scheduler;
	// 定时执行的crontab格式的表达式， 使用crontab执行周期性任务，最小间隔时间是分钟.
	// 具体格式是: min hour day month week
	// 比如表示8点到23点期间，每隔1分钟执行一次: * 8-23 * * *
	// 比如表示8点到23点期间，每隔5分钟执行一次: */5 8-23 * * *
	protected String crontabRegex = null;
	private ScheduledExecutorService service =null;
	//配置文件信息
	private String configureFile=null;
	public ScheduledDisruptorBatch()
	{
		super(8192, 4, 1, 0);
	}

	public ScheduledDisruptorBatch(int readInterval)
	{
		super(8192, 4, 1, 0);
		this.readInterval = readInterval;
	}

	/**
	 * 自动启动
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		loadEnv();
		addData();
	}

	class Worker implements Runnable
	{
		@Override
		public void run()
		{
			System.out.println(" task a time");
			await();
			List<T> datas = read_data();
			if (datas!=null&&datas.size()>0)
			{
				for (T data : datas)
				{
					add(data);
				}
				datas.clear();
			}
		}
	}

	public void addData()
	{
		// 优先使用crontab执行任务
		if (crontabRegex != null && !crontabRegex.isEmpty())
		{
			scheduler = new Scheduler();
			scheduler.schedule(crontabRegex, new Worker());
			scheduler.start();
		} else if (readInterval > 0)
		{
			service=Executors.newSingleThreadScheduledExecutor();
			service.scheduleAtFixedRate(new Worker(), 0, this.readInterval,
					TimeUnit.MILLISECONDS);
		}
	}


	@Override
	public V handleData(T value)
	{
		return excute_taskImpl(value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handleResult(V result)
	{
		update_taskImpl(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop()
	{
		super.stop();
		if (service!=null)
		{
			service.shutdown();
		}
		if (scheduler!=null)
		{
			scheduler.stop();
		}
	}

	// 加载环境信息
	public void loadEnv()
	{
		loadConfig();
	}
	private void loadConfig()
	{
		if (configureFile==null)
		{
			File classPathFile = new File(this.getClass().getClassLoader().getResource("").getPath());
			File batchFile = new File(classPathFile, "batch.xml");
			if (batchFile.exists())
			{
				configureFile = batchFile.getAbsolutePath();
			}
		}
		if (configureFile != null)
		{
			initEnvFromConfigure();
		}
	}
	private void initEnvFromConfigure()
	{
		
		PropertiesFileMonitor monitor_agent = PropertiesFileMonitor.getInstance();
		monitor_agent.addFile(configureFile);
		// 从配置文件中读取参数值
		Properties configProp =monitor_agent.getProperty(configureFile) != null ? monitor_agent.getProperty(configureFile)
				: new Properties();
		String prefixName = this.getClass().getSimpleName() + ".";
		// 根据配置文件的参数设置属性
		if (configProp.containsKey(prefixName + "crontabRegex"))
		{
			this.crontabRegex = configProp.getProperty(prefixName + "crontabRegex");
		}

		if (configProp.containsKey(prefixName + "readInterval"))
		{
			String value = configProp.getProperty(prefixName + "readInterval");
			if (value != null)
			{
				this.readInterval = Integer.parseInt(value);
			}
		}
		
	}
	public abstract List<T> read_data();

	public abstract V excute_taskImpl(T value);

	public abstract void update_taskImpl(V result);

	
}
