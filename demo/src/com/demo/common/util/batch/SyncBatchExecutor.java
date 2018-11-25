package com.demo.common.util.batch;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.common.util.connectionmanager.ConnectionManager;
import com.google.common.io.Files;

import java.nio.charset.Charset;

// author: 王启荣
/*
 * =================================定时批处理抽象类=================================
 * 此类一个针对定时批量处理的抽象类，能够满足下列场景：一个定时线程定时从一个地方获取一批数据并形成线程放入到线程池中；
 * 并能够智能根据当前线程数及排队长度，自动调整线程池中线程的个数。 自动使用线程池批量执行任务，并将执行结果方法到状态队列中 ；
 * 一个定时任务定时从状态队列中获取数据并执行后续操作。批处理框架流程是： 1. 当需要更新队列不为空时，使用单线程定时获取数据。否则，则等待队列为空的通知
 * 2. 将获取到的数据形成线程对象添加到线程池中，并发执行，并将执行结果放到更新队列中。 3. 使用单线程将执行后的结果更新。
 * =================================具体定时批处理类=================================
 * //具体类，使用方法 (当读出的数据跟更新的实体类相同时，可以传入相同类型；否则传入不同的类型，便于编程)： 1.
 * 继承类SyncBatchExecutor，比如： public classTaiKangEmailSys extends
 * SyncBatchExecutor<InsureEmail, InsureEmailResult> 2. 实现下面几个方法： 2.1
 * 读取批量数据方法,批处理框架将会自动将这些数据包装成不同的线程 public List<T> read_data(); 2.2 数据执行任务方法,
 * 如果不需要处理结果则excute_taskImpl方法返回null public T2 excute_taskImpl(T value); 2.3
 * 单条数据更新方法, 如果不需要更新数据则保持为空方法 public void update_taskImpl(T2 value); 2.4 加载环境信息
 * (一方面可以把一些配置信息加载到context变量里面，另一方面可以将清理上次意外终止时环境信息) public void loadEnv() 优化建议:
 * 当需要高性能批量更新状态数据时，直接覆盖下列方法 (比如获取一个数据库连接,进行多条数据更新，然后关闭连接) public void
 * update_batchTaskImpl(Iterator<T2> values){ }
 */

public abstract class SyncBatchExecutor<T, T2>{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected boolean running;

	private volatile int readInterval = 10000;

	private volatile int updateInterval = 2000;

	// 定时任务
	private ScheduledThreadPoolExecutor scheduleExecutor = null;

	// 定时执行的crontab格式的表达式， 使用crontab执行周期性任务，最小间隔时间是分钟.
	// 具体格式是: min hour day month week
	// 比如表示8点到23点期间，每隔1分钟执行一次: * 8-23 * * *
	// 比如表示8点到23点期间，每隔5分钟执行一次: */5 8-23 * * *
	private String crontabRegex = null;

	// 符合crontab属性的定时任务
	private Scheduler scheduler = new Scheduler();

	// 执行任务的线程池
	private ThreadPoolExecutor executor = null;

	// 待执行任务的对列
	private LinkedBlockingQueue<Runnable> task_queue = new LinkedBlockingQueue<Runnable>();

	// 更新队列使用的锁
	private Lock updatelock = new ReentrantLock();

	// 更新队列为空的条件
	private Condition update_queue_empty = updatelock.newCondition();

	// 保存状态数据的队列
	private LinkedBlockingQueue<T2> update_queue = new LinkedBlockingQueue<T2>();

	// 停止标志位
	// protected volatile boolean stopFlag = true;

	// 线程池初始线程大小
	private volatile int initCorePoolSize = 10;

	// 线程池最大线程大小
	private volatile int coreMaxSize = 20;

	// 当前设置的线程大小
	private volatile int currPoolSize = initCorePoolSize;

	// 线程池调整的阈值
	private static final int AdjustLatchNum = 10;

	// 调整线程池的阈值，缺省是10次
	private int adjustLatch = AdjustLatchNum;

	// 读取数据的次数计数器
	private AtomicInteger readCount = new AtomicInteger(0);

	// 更新数据的次数计数器
	private AtomicInteger updateCount = new AtomicInteger(0);

	// 执行任务的次数计数器
	private AtomicInteger taskCount = new AtomicInteger(0);

	// 执行成功任务的次数计数器
	private AtomicInteger completedTaskCount = new AtomicInteger(0);

	// 执行 失败任务的次数计数器
	private AtomicInteger failedTaskCount = new AtomicInteger(0);

	// 数据连接名称
	private String jdbc_datasource = null;// "jdbc/InsureDB";

	// 数据库连接池
	private ConnectionManager pool = null;

	// 保存环境信息的上下文,可用于读取配置文件数据
	protected HashMap<String, Object> context = new HashMap<String, Object>();

	private List<String> systemContext = new ArrayList<String>();

	public void addSystemContext(String str, Object obj) {
		systemContext.add(str);
		context.put(str, obj);
	}

	public List<String> getSystemContext() {
		return systemContext;
	}

	// 配置文件
	protected String configureFile = null;
	// 模板文件
	protected String templatePath = this.getClass().getClassLoader()
			.getResource("").getPath()
			+ File.separator + "template" + File.separator + "monitor.html";

	// 监视器
	// 构造函数
	protected SyncBatchExecutor() {
		// 读取配置文件
		loadConfig();

		// 初始化日志
		initLogback();

		// 创建数据源
		initDataSouce();

	}

	// 通过配置文件读取批处理配置
	protected SyncBatchExecutor(String configureFile) {
		File batchFile = new File(configureFile);
		if (batchFile.exists()) {
			this.configureFile = configureFile;

			initEnvFromConfigure();
		}
		initLogback();

		// 从配置文件中读取参数值

		// 生成数据库连接池对象
		initDataSouce();

	}

	// 使用缺失参数启动
	public synchronized void start() {
		loadConfig();

		start(this.initCorePoolSize, this.coreMaxSize, this.readInterval,
				this.updateInterval, this.crontabRegex);
	}

	private void loadConfig() {
		if (configureFile == null) {
			// String batchFileName = System.getProperty("user.dir") +
			// File.separator + "config" + File.separator
			// + this.getClass().getSimpleName() + ".xml";
			// File batchFile = new File(batchFileName);
			// if (batchFile.exists()) {
			// configureFile = batchFileName;
			// }
			// else {
			// System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
			// System.out.println(this.getClass().getClassLoader().getResource(""));

			File classPathFile = new File(this.getClass().getClassLoader()
					.getResource("").getPath());

			File batchFile = new File(classPathFile, "batch.xml");

			// String batchFileName = System.getProperty("user.dir") +
			// File.separator + "config" + File.separator
			// + "batch.xml";
			// File batchFile = new File(batchFileName);
			if (batchFile.exists()) {
				configureFile = batchFile.getAbsolutePath();
			}
			// }
		}
		if (configureFile != null) {
			initEnvFromConfigure();
		}
	}

	// 使用缺失参数启动
	public synchronized void startThreads(String configureFile) {
		File batchFile = new File(configureFile);
		if (batchFile.exists()) {
			this.configureFile = configureFile;
			initEnvFromConfigure();
		}

		start(this.initCorePoolSize, this.coreMaxSize, this.readInterval,
				this.updateInterval, this.crontabRegex);
	}

	// 使用定时线程池执行数据读取任务,一般以秒或者毫秒为间隔
	public synchronized void start(int corePoolSize, int coreMaxSize,
			int _readInterval, int _updateInterval) {
		start(corePoolSize, coreMaxSize, _readInterval, _updateInterval, null);
	}

	// 使用crontab执行周期性任务,最小间隔时间是分钟，具体格式是: min hour day month week
	// 比如 * 8-23 * * *,表示8点到23点期间，每隔1分钟执行一次
	// 比如 */5 8-23 * * *,表示8点到23点期间，每隔5分钟执行一次
	public synchronized void start(int corePoolSize, int coreMaxSize,
			String crontab, int _updateInterval) {
		start(corePoolSize, coreMaxSize, 0, _updateInterval, crontab);
	}

	// 启动线程池,并执行方法 (当cron不为空，并且_readInterval为0时使用cron4j执行定时任务)
	public synchronized void start(int corePoolSize, int coreMaxSize,
			int _readInterval, int _updateInterval, String cron) {
		// 初始化日志配置
		initLogback();

		logger.info("startThreads(int, int, int, int) - start");

		if (!this.running) {
			this.running = true;
			logger
					.info("startThreads1(int, int, int, int) - Start the batch system");

			// 初始化启动前环境(比如：清理上次中断的任务获取读取配置文件等)
			loadEnv();

			// 设置读取间隔时间及线程池大小
			this.readInterval = _readInterval;
			this.updateInterval = _updateInterval;
			this.initCorePoolSize = corePoolSize;
			this.coreMaxSize = coreMaxSize;
			this.crontabRegex = cron;

			// 计数器
			this.readCount.set(0);
			this.updateCount.set(0);
			this.taskCount.set(0);
			this.completedTaskCount.set(0);
			this.failedTaskCount.set(0);

			// 启动线程池
			executor = new ThreadPoolExecutor(this.initCorePoolSize,
					this.coreMaxSize, 5, TimeUnit.SECONDS, task_queue);

			// 定时任务线程池,线程数为2个，一个为读线程使用，一个为更新线程使用
			scheduleExecutor = new ScheduledThreadPoolExecutor(2);

			// 启动读取未数据线程
			Common_Reader_Thread reader_thread = new Common_Reader_Thread();
			if (crontabRegex != null) {// 当crontab表达式不为空时，使用cron4j来执行读取数据任务
				scheduler.schedule(crontabRegex, reader_thread);
				scheduler.start();
			} else if (_readInterval > 0) {
				scheduleExecutor.scheduleAtFixedRate(reader_thread, 0,
						this.readInterval, TimeUnit.MILLISECONDS);
			}

			// 启动更新数据库状态的线程,在读取数据线程启动后再启动
			if (_updateInterval > 0) {
				Common_Update_Thread status_update_thread = new Common_Update_Thread();
				scheduleExecutor.scheduleAtFixedRate(status_update_thread,
						this.readInterval, this.updateInterval,
						TimeUnit.MILLISECONDS);
			}
		} else {
			logger
					.info("startThreads(int, int, int, int) - Batch system already started");
		}

		logger.info("startThreads(int, int, int, int) - end");
	}

	// 停止所用线程
	public synchronized void stop() {
		logger.info("stopThreads() - start");

		if (running) {
			this.running = false;

			// 关闭定时任务线程池
			if (scheduleExecutor != null) {
				logger.info("shutdown scheduleExecutor!");
				scheduleExecutor.shutdown();
				scheduleExecutor = null;
			}
			if (scheduler.isStarted())
				scheduler.stop();

			// 关闭批量执行任务线程池
			if (executor != null) {
				logger.info("shutdown executor!");
				executor.shutdown();
				executor = null;
			}

			logger.info("stopThreads() - Stop batch system");
		} else {
			logger
					.info("stopThreads(int, int, int, int) - Batch system already stop");
		}

	}

	public String getConfigure(String key) {
		if (configureFile != null) {
			PropertiesFileMonitor monitor_agent = PropertiesFileMonitor
					.getInstance();

			if (monitor_agent.getProperty(configureFile) != null) {
				System.out.println(this.getClass().getSimpleName() + "." + key);
				return monitor_agent.getProperty(configureFile).getProperty(
						this.getClass().getSimpleName() + "." + key);
			} else
				return null;
		} else {
			return null;
		}
	}

	public String getName() {
		return this.getClass().getSimpleName();
	}

	// 读取配置文件
	private Properties readConfig() {
		if (configureFile != null) {
			PropertiesFileMonitor monitor_agent = PropertiesFileMonitor
					.getInstance();

			return monitor_agent.getProperty(configureFile) != null ? monitor_agent
					.getProperty(configureFile)
					: new Properties();
		} else {
			return new Properties();
		}
	}

	// 初始化数据源
	public void initDataSouce() {
		Properties configProp = readConfig();

		if (configProp.containsKey("jdbc_datasource")) {
			this.jdbc_datasource = configProp.getProperty("jdbc_datasource");
		}

		try {
			if (this.jdbc_datasource != null) {
				pool = new ConnectionManager(this.jdbc_datasource);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private void initEnvFromConfigure() {
		PropertiesFileMonitor monitor_agent = PropertiesFileMonitor
				.getInstance();
		monitor_agent.addFile(new File(configureFile));
		// 从配置文件中读取参数值
		Properties configProp = readConfig();
		String prefixName = this.getClass().getSimpleName() + ".";
		// 根据配置文件的参数设置属性
		if (configProp.containsKey(prefixName + "crontabRegex")) {
			this.crontabRegex = configProp.getProperty(prefixName
					+ "crontabRegex");
		}

		if (configProp.containsKey(prefixName + "readInterval")) {
			String value = configProp.getProperty(prefixName + "readInterval");
			if (value != null) {
				this.readInterval = Integer.parseInt(value);
			}
		}

		if (configProp.containsKey(prefixName + "updateInterval")) {
			String value = configProp
					.getProperty(prefixName + "updateInterval");
			if (value != null) {
				this.updateInterval = Integer.parseInt(value);
			}
		}

		if (configProp.containsKey(prefixName + "initCorePoolSize")) {
			String value = configProp.getProperty(prefixName
					+ "initCorePoolSize");
			if (value != null) {
				this.initCorePoolSize = Integer.parseInt(value);
			}
		}

		if (configProp.containsKey(prefixName + "coreMaxSize")) {
			String value = configProp.getProperty(prefixName + "coreMaxSize");
			if (value != null) {
				this.coreMaxSize = Integer.parseInt(value);
			}
		}

		if (configProp.containsKey(prefixName + "stopFlag")) {
			String value = configProp.getProperty(prefixName + "stopFlag");
			if (value != null) {
				this.running = !Boolean.parseBoolean(value);
			}
		}
	}

	// 根据批处理配置指定的logback配置文件进行指定
	private void initLogback() {
		try {
			String logbackFile = "config/logback.xml";
			if (readConfig().getProperty("logback") != null) {
				logbackFile = readConfig().getProperty("logback");
			}

			File batchFile = new File(logbackFile);
			if (batchFile.exists()) {
				JoranConfigurator configurator = new JoranConfigurator(); // 定义一个(JoranConfigurator)配置器
				LoggerContext logback_context = (LoggerContext) LoggerFactory
						.getILoggerFactory(); // 得到当前应用中logger上下文
				configurator.setContext(logback_context); // 将当前应用的logger
				logback_context.reset(); // 清除以前的配置器中的所有内容
				configurator.doConfigure(logbackFile);
			}
		} catch (JoranException je) {
			logger.error("JoranException occur at:" + je.getMessage()); // 将此处异常也记录到日志
			je.printStackTrace(); // 在控制打印出异常跟踪信息
		}
	}

	// 获得读取间隔时间
	public int getReadInterval() {
		return readInterval;
	}

	// 设置读取间隔时间
	public void setReadInterval(int _readInterval) {
		this.readInterval = _readInterval;
		if (this.readInterval > 0) {
			this.crontabRegex = null;
		}
	}

	// 获取定时任务表达式
	public String getCrontabRegex() {
		return crontabRegex;
	}

	// 设置定时任务表达式
	public void setCrontabRegex(String crontabRegex) {
		this.crontabRegex = crontabRegex;
		if (this.crontabRegex != null) {
			this.readInterval = 0;
		}
	}

	// 设置数据库连接池
	public void setDataSouceName(String dataSource) {
		jdbc_datasource = dataSource;
	}

	// 获取线程池最大线程数
	public int getMaximumPoolSize() {
		return coreMaxSize;
	}

	// 设置最大的线程数
	public void setMaximumPoolSize(int coreMaxSize) {
		logger.info("setMaximumPoolSize(int) - start");

		this.coreMaxSize = coreMaxSize;
		boolean decreasePoolSize = false;
		// 如果最大线程数小于当前正在运行的线程数，则调整当前线程个数
		if (currPoolSize > this.coreMaxSize) {
			currPoolSize = this.coreMaxSize;
			logger
					.warn("max size is less than current pool size, so decrease the current pool size");
			decreasePoolSize = true;
		}
		// 如果最大线程数小于最小的初始化线程数，则调整最初线程数
		if (coreMaxSize < this.initCorePoolSize) {
			initCorePoolSize = this.coreMaxSize;
			logger
					.warn("max size is less than init pool size, so decrease the init pool size");
			decreasePoolSize = true;
		}

		// 如果需要调整最小线程数，则进行调整
		if (decreasePoolSize && executor != null) {
			logger.info("set the pool thread max ore size to:{}"
					+ this.coreMaxSize);
			executor.setCorePoolSize(initCorePoolSize);
		}

		// 设置最大线程数
		if (executor != null) {
			executor.setMaximumPoolSize(coreMaxSize);
		}

		logger.info("setMaximumPoolSize(int) - end");
	}

	// 获取线程池当前可并发的最大线程数
	public int getCurrPoolSize() {
		return currPoolSize;
	}

	// 设置最小的线程数
	public void setCurrPoolSize(int currPoolSize) {
		logger.info("setCurrPoolSize(int) - start");

		this.currPoolSize = currPoolSize;
		if (currPoolSize > this.initCorePoolSize
				&& currPoolSize <= this.coreMaxSize) {
			logger
					.info("set the current thread size to:{}"
							+ this.currPoolSize);
			currPoolSize = this.currPoolSize;
			if (executor != null) {
				executor.setCorePoolSize(this.currPoolSize);
			}
		} else {
			logger.error("current pool size is not changed");
		}

		logger.info("setCurrPoolSize(int) - end");
	}

	// 读取初始的线程数
	public int getCorePoolSize() {
		return initCorePoolSize;
	}

	// 设置最小的线程数
	public void setCorePoolSize(int corePoolSize) {
		if (corePoolSize < coreMaxSize) {
			this.initCorePoolSize = corePoolSize;
			logger.info("set the pool thread core size to:{}"
					+ this.initCorePoolSize);

			// 如果当前可并发线程数小于最小线程数，则增大可并发线程数
			if (currPoolSize < corePoolSize) {
				currPoolSize = this.initCorePoolSize;
			}
			if (executor != null) {
				executor.setCorePoolSize(this.initCorePoolSize);
			}
		}
	}

	// 获取批处理中上下文
	public HashMap<String, Object> getContext() {
		return context;
	}

	// 获得更新间隔时间
	public int getUpdateInterval() {
		return updateInterval;
	}

	// 设置更新间隔时间
	public void setUpdateInterval(int _updateInterval) {
		this.updateInterval = _updateInterval;
	}

	// 获取数据库连接
	public Connection getConnection() throws Exception {
		if (pool != null) {
			return pool.createConnection();
		} else {
			return null;
		}
	};

	// 关闭数据库连接
	public void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			logger
					.error(
							"closeConnection(Connection) - close connection exception:",
							e);
		}
	}

	// 获取当前任务队列的长度
	public long getTaskCount() {
		return executor.getTaskCount();
	}

	// 获取当前读取数据次数
	public int getReadCount() {
		return readCount.intValue();
	}

	// 读取数据次数加1，并返回值
	public int increaseReadCount() {
		return readCount.incrementAndGet();
	}

	// 获取当前更新次数
	public int getUpdateCount() {
		return updateCount.intValue();
	}

	// 更新执行次数加1
	public int increaseUpdateCount() {
		return updateCount.incrementAndGet();
	}

	// 任务执行成功次数
	public int getCompletedTaskCount() {
		return completedTaskCount.intValue();
	}

	// 任务执行次数加1，并返回
	public int increaseCompletedTask() {
		return completedTaskCount.incrementAndGet();
	}

	// 任务执行失败次数
	public int getFailedTaskCount() {
		return failedTaskCount.intValue();
	}

	// 任务执行失败次数加1，并返回
	public int increaseFailTask() {
		return failedTaskCount.incrementAndGet();
	}

	// 增加对象到状态队列中
	public void addStatueQueue(T2 value) {
		if (value != null)
			update_queue.add(value);
	}

	// 获取当前系统状况,使用json格式
	public String getStatus() {
		StringBuffer result = new StringBuffer(1000);
		result.append("{");
		result.append("'corePoolSize':" + initCorePoolSize);
		result.append("'maxPoolSize':" + coreMaxSize);
		result.append("'readInterval':" + readInterval);
		result.append("'updateInterval':" + updateInterval);
		if (!this.running) {
			result.append("status:stopped}");
		} else {
			result.append("{status:running,");
			result.append(",activeThreads:" + executor.getActiveCount());
			result.append(",waitThreads:" + executor.getTaskCount());
			result.append(",maximumPoolSize:" + executor.getMaximumPoolSize());
			result.append(",corePoolSize:" + executor.getCorePoolSize());
			result.append(" statusQueue:" + update_queue.size());
		}
		result.append("}");

		return result.toString();
	}

	public int getUpdateQueueSize() {
		return update_queue.size();
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append("{ readInterval:" + getReadInterval()
				+ ", crontabRegex:\"" + getCrontabRegex() + "\""
				+ ", updateInterval:" + getUpdateInterval() + ", stopFlag:"
				+ !running + ", initCorePoolSize:" + getCorePoolSize()
				+ ", coreMaxSize:" + getMaximumPoolSize() + ", currPoolSize:"
				+ getCurrPoolSize() + ", readCount:" + getReadCount()
				+ ", updateCount:" + getUpdateCount() + ", completedTaskCount:"
				+ getCompletedTaskCount() + ", failedTaskCount:"
				+ getFailedTaskCount() + ", update_Queue_size:"
				+ getUpdateQueueSize() + ", context:"
				+ JSON.toJSONString(getContext()));
		if (running) {
			result.append(", taskCount:" + getTaskCount());
		}
		result.append(",jdbc_datasource:\"" + getDatasourceName() + "\"}");
		return result.toString();
	}

	public String getHtml() throws IOException {
		String template = "";
		try {
			template = Files.toString(new File(templatePath), Charset.forName("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(template);
		return toHtml(template);
	}

	public SyncBatchExecutor getBatchSys() {
		return this;
	}

	private String toHtml(String template) throws IOException {
		VelocityEngine ve = new VelocityEngine();
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		JSONObject json = JSONObject.parseObject(toString());
		Boolean stopFlag = json.getBoolean("stopFlag");
		if (stopFlag == false) {
			context.put("status", "running");
			context.put("status2", "运行");
			context.put("status3", "switchOff");
			context.put("status4", "终止");
		} else if (stopFlag == true) {
			context.put("status", "stopped");
			context.put("status2", "停止");
			context.put("status3", "switchOn");
			context.put("status4", "启动");
		}
		int taskCount = stopFlag ? 0 : json.getIntValue("taskCount");
		context.put("TaskCount", taskCount);
		int completedTasks = json.getIntValue("completedTaskCount");
		context.put("completedTasks", completedTasks);
		int failedTasks = json.getIntValue("failedTaskCount");
		context.put("failedTasks", failedTasks);
		int queue = json.getIntValue("update_Queue_size");
		context.put("queue", queue);
		int core = json.getIntValue("initCorePoolSize");
		context.put("core", core);
		int curr = json.getIntValue("currPoolSize");
		context.put("curr", curr);
		int maximum = json.getIntValue("coreMaxSize");
		context.put("max", maximum);
		int readCount = json.getIntValue("readCount");
		context.put("readCount", readCount);
		int readInterval = json.getIntValue("readInterval");
		context.put("readInterval", readInterval);
		int updateCount = json.getIntValue("updateCount");
		context.put("updateCount", updateCount);
		int updateInterval = json.getIntValue("updateInterval");
		context.put("updateInterval", updateInterval);
		String source = json.getString("jdbc_datasource");
		context.put("source", source);
		String cron = json.getString("crontabRegex");
		if (cron == null || cron.equals("null") || cron.equals(""))
			cron = "";
		context.put("cronexpress", cron);
		context.put("batchName", getName());

		JSONObject cont = json.getJSONObject("context");
		List<Entry<String, Object>> list1 = new ArrayList<Entry<String, Object>>();
		List<Entry<String, Object>> list2 = new ArrayList<Entry<String, Object>>();
		for (Entry<String, Object> entry : cont.entrySet()) {
			String key = entry.getKey();
			if (systemContext.contains(key))
				list2.add(entry);
			else
				list1.add(entry);
		}
		Collections.sort(list1, new Comparator<Entry<String, Object>>() {
			public int compare(Entry<String, Object> a, Entry<String, Object> b) {
				return a.getKey().compareTo(b.getKey());
			}
		});
		context.put("list", list1);
		context.put("list2", list2);
		ve.evaluate(context, writer, "", template);
		return new String(writer.toString().getBytes("utf-8"), "iso-8859-1");
	}

	public String getDatasourceName() {
		return jdbc_datasource;
	}

	public void destroy() {
		stop();
	}

	/*
	 * 智能的动态在获取数据线程中调整线程池的大小,调整原则： ：1.当超过线程池最大值的2倍时，增大基本线程池的大小.
	 * 2。当线程池长期处于空闲时，减少线程池的大小. 3.当连续10次满足条件时进行调整
	 */
	private void adjustThreadPool() {
		if (executor.getTaskCount() == 0
				&& executor.getCorePoolSize() <= executor.getMaximumPoolSize()
				&& getCurrPoolSize() > getCorePoolSize()) {
			adjustLatch--;

			// 减少线程数
			if (adjustLatch == 0) {
				this.currPoolSize = this.initCorePoolSize;
			}
		} else if (executor.getTaskCount() >= executor.getMaximumPoolSize() * 2
				&& getCurrPoolSize() < executor.getMaximumPoolSize()) {
			// 调整 阀门
			adjustLatch--;

			// 增加线程数,使用折半增加算法
			if (adjustLatch == 0) {
				int incr = (executor.getMaximumPoolSize() - executor
						.getCorePoolSize()) / 2;

				this.currPoolSize = executor.getCorePoolSize()
						+ (incr > 0 ? incr : 1);
			}
		}

		// 调整线程池并发线程数
		if (adjustLatch == 0) {
			adjustLatch = AdjustLatchNum;
			executor.setCorePoolSize(this.currPoolSize);

			logger.info("adjust the thread pool:{}", this.currPoolSize);
		}
	}

	// 获取数据并放到等待队列中
	public class Common_Reader_Thread implements Runnable {// extends Thread {
		public Common_Reader_Thread() {
			logger.debug("Common_Reader_Thread() - Start read thread");
		}

		// 读取数据，并生成等待线程对象. 当等待更新的对象队列为空时才能继续读取新的数据,否则就等待
		public void run() {
			if (running) {
				updatelock.lock();
				try {
					while (update_queue.size() > 0) {
						update_queue_empty.await();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					updatelock.unlock();
				}

				// 读取数据
				List<T> data = null;
				try {
					data = read_data();
					readCount.incrementAndGet();
				} catch (Exception e) {
					logger.error(e.getStackTrace().toString());
				}
				// 根据数据生成执行线程加入到队列中
				if (data != null && data.size() > 0) {
					for (T value : data) {
						// 生成任务并添加到队列中
						taskCount.incrementAndGet();
						if (value != null) {
							executor.execute(new Common_Execute_Worker(value));
						}
						// Common_Execute_Worker worker = new
						// Common_Execute_Worker(value);
						// addTask(worker);
					}
					data.clear();

					// if (debugFlag) {
					logger.debug(toString());
					// }

					// 在增加新的任务之后，动态调整线程数
					adjustThreadPool();
				}
			}
		}
	}

	// 负责执行线程,当stopFlag设置为true后，新的任务将停止执行
	private class Common_Execute_Worker implements Runnable {
		private T data_info;

		public Common_Execute_Worker(T data_info) {
			this.data_info = data_info;
		}

		// 执行任务并添加到状态队列中,如果执行结果为空，则不向状态队列添加
		public void run() {
			if (running) {
				try {
					T2 value = excute_taskImpl(data_info);
					increaseCompletedTask();
					if (value != null) {
						addStatueQueue(value);
					}
				} catch (Exception e) {
					logger.error(e.getStackTrace().toString());
				}
			}
		}
	}

	// 更新状态的线程
	public class Common_Update_Thread implements Runnable { // extends Thread {
		public Common_Update_Thread() {
			logger.debug("Common_Update_Thread() - Start update thread");
		}

		// 队列不为空或者停止标志为假, 在队列为空并且stop_flag为false时停止执行
		public void run() {
			if (update_queue.size() > 0 || running) {
				logger.debug("run() - before: status queue size={}",
						update_queue.size());
				updateCount.incrementAndGet();
				if (update_queue.size() > 0) {
					Iterator<T2> values = update_queue.iterator();
					update_batchTaskImpl(values);

					updatelock.lock();
					try {
						update_queue_empty.signal();// 唤醒读线程
					} finally {
						updatelock.unlock();
					}

					logger.debug("run() - after: status queue size={}",
							update_queue.size());
				}
			}
		}
	}

	// 批量执行更新,如果使用批量更新SQL语句则在子类中覆盖这个方法，否则实现update_taskImpl方法
	public void update_batchTaskImpl(Iterator<T2> values) {
		while (values.hasNext()) {
			T2 value = values.next();
			try {
				if (value != null) {
					update_taskImpl(value);
				}
			} catch (Exception e) {
				logger.error(e.getStackTrace().toString());
			} finally {
				values.remove();
			}
		}
	}

	// 加载环境信息, 可以在此设置连接池的名称
	public void loadEnv() {

	}

	// 读取信息
	public abstract List<T> read_data();

	// 在线程池执行
	public abstract T2 excute_taskImpl(T value);

	// 单个实体更新
	public abstract void update_taskImpl(T2 value);

}
