package com.demo.common.filemonitor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/*
 * 监控属性文件变化情况的类, 目的: 1) 跟踪属性文件的变化，实现了属性文件监听器，并增加了读取属性内容的接口: getProperty 2)
 * 为了提高性能比以减少对应用服务器磁盘的读写，将属性文件内容缓存到内存中 3) 当文件修改时间发生变化时，自动重新读取文件内容到内存中
 */
public class PropertiesFileMonitor extends FileMonitor
{
	// private static PropertiesFileMonitor monitor_instance = null;

	// 保存属性文件内容缓存
	private ConcurrentHashMap<String, Properties> properties_file_content_cache;

	// 使用单实例模式 (10秒钟扫描一次文件内容是否发生变化)
	private static class LazyHolder
	{
		private static final PropertiesFileMonitor INSTANCE = new PropertiesFileMonitor(10000);
	}

	// 获取当前实例
	public static PropertiesFileMonitor getInstance()
	{
		return LazyHolder.INSTANCE;
	}

	// // 单实例模式
	// public static PropertiesFileMonitor getInstance() {
	// if (monitor_instance == null) {
	// monitor_instance = new PropertiesFileMonitor(1000);
	// }
	//
	// return monitor_instance;
	// }

	// 设置监控间隔的构造
	public PropertiesFileMonitor(long pollingInterval)
	{
		super(pollingInterval);
		properties_file_content_cache = new ConcurrentHashMap<String, Properties>();
		addListener(new PropertiesFileListener());
	}

	// 向内存中添加对应的文件中属性内容到
	public void addProperties(File file)
	{
		Properties props = new Properties();
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(file));

			if (file.getName().endsWith(".xml"))
			{
				props.loadFromXML(in);
			} else
			{
				props.load(in);
			}
			properties_file_content_cache.put(file.getPath(), props);
			in.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// 从内存中清空相应的文件名对应的属性内容
	public void removeProperties(File properites_file)
	{
		if (!properties_file_content_cache.containsKey(properites_file.getName()))
		{
			properties_file_content_cache.remove(properites_file.getPath());
		}
	}

	// 获取对应文件中的相应属性项的值,缺省值为""
	public String getProperty(String fileName, String key)
	{
		return getProperty(fileName, key, "");
	}

	// 获取对应文件中的相应属性项的值
	public String getProperty(String fileName, String key, String defaultValue)
	{
		String configuration_path = new File(fileName).getPath();
		Properties pros = properties_file_content_cache.get(configuration_path);
		if (pros != null)
		{
			return pros.getProperty(key, defaultValue);
		}

		return defaultValue;
	}

	public Properties getProperty(String fileName)
	{
		String configuration_path = new File(fileName).getPath();
		Properties pros = properties_file_content_cache.get(configuration_path);

		return pros;
	}

	public Properties getPropertyByFullPatch(String filePath)
	{
		Properties pros = properties_file_content_cache.get(filePath);

		return pros;
	}

	/*
	 * 1. 增加需要监控的属性文件名 2. 并加装内容到内存中
	 */
	@Override
	public void addFile(File properites_file)
	{
		super.addFile(properites_file);
		if (!properties_file_content_cache.containsKey(properites_file.getName()))
		{
			addProperties(properites_file);
		}
	}
	public void addFile(String fileName)
	{
		File properites_file=new File(fileName);
		addFile(properites_file);
	}

	/*
	 * 1. 从内从中删除属性文件内容 2. 并停止文件监控
	 */
	public void removeFile(File properites_file)
	{
		super.removeFile(properites_file);
		if (properties_file_content_cache.containsKey(properites_file.getName()))
		{
			removeProperties(properites_file);
		}
	}

	/*
	 * 监听属性文件变化的监听器,当文件发生变化时，重新读取属性文件内容，并加载到内存中
	 */
	public class PropertiesFileListener implements FileListener
	{
		public void fileChanged(File properites_file)
		{
			addProperties(properites_file);
		}
	}

	/**
	 * 测试用例: test.properties文件如下：
	 * 
	 * #测试环境配置：平台路径配置
	 */
	public static void main(String args[])
	{
		// 获取监控实例
		PropertiesFileMonitor monitor = PropertiesFileMonitor.getInstance();

		// 需要监控的文件名
		String fileName = "E:\\source_projects\\smalltest\\smalltest\\src\\com\\taikang\\utils\\test1.properties";
		// fileName =
		// "E:\\eclipse_btt\\jakarta-tomcat-5.0.19\\bin\\data\\config\\ipconfig.properties";
		fileName = "bin/data/config/ipconfig.properties";
		// 添加文件到监控中
		monitor.addFile(fileName);

		// Avoid program exit
		while (true)
		{
			// 打印出ip对应属性值，检测是否变化

			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
