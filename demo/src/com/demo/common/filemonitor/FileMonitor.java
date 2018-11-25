package com.demo.common.filemonitor;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 监控磁盘文件变化类. 使用说明:
 * 
 * 1. 实现一个监听器类来实现FileListener接口，并注册给FileMonitor 2. 创建FileMonitor实例 3. 添加要监控的文件
 * 4. 监听器在文件修改时间发生变化时被触发
 * 
 */
public class FileMonitor
{
	private Timer timer_;
	private HashMap<File, Long> files_; // File -> Long
	private Collection<WeakReference<FileListener>> listeners_; // of
																// WeakReference(FileListener)

	/**
	 * 创建能按照定时间隔监控的实例
	 * 
	 * @param pollingInterval
	 *            监控间隔时间，单位为毫秒.
	 */
	public FileMonitor(long pollingInterval)
	{
		files_ = new HashMap<File, Long>();

		listeners_ = new ArrayList<WeakReference<FileListener>>();

		timer_ = new Timer(true);
		timer_.schedule(new FileMonitorNotifier(), 0, pollingInterval);
	}

	/**
	 * 停止文件监控
	 */
	public void stop()
	{
		timer_.cancel();
	}

	/*
	 * 增加需要监控的文件名
	 */
	public void addFile(File file)
	{
		if (!files_.containsKey(file))
		{
			long modifiedTime = file.exists() ? file.lastModified() : -1;
			files_.put(file, new Long(modifiedTime));
		}
	}

	/*
	 * 删除需要监控的文件名
	 */
	public void removeFile(File file)
	{
		files_.remove(file);
	}

	/**
	 * 增加文件变化监听
	 */
	public void addListener(FileListener fileListener)
	{
		// 检查是否已经添加过该监听，如果已添加则不再添加
		for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext();)
		{
			WeakReference<FileListener> reference = i.next();
			FileListener listener = reference.get();
			if (listener == fileListener)
				return;
		}

		// 使用弱一致性检查，以保证没有僵尸引用
		listeners_.add(new WeakReference<FileListener>(fileListener));
	}

	/**
	 * 删除文件监听内容
	 */
	public void removeListener(FileListener fileListener)
	{
		for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext();)
		{
			WeakReference<FileListener> reference = i.next();
			FileListener listener = reference.get();
			if (listener == fileListener)
			{
				i.remove();
				break;
			}
		}
	}

	/**
	 * 定时检查文件是否发生变化的监控任务
	 */
	private class FileMonitorNotifier extends TimerTask
	{
		public void run()
		{
			// 扫描所有已注册的文件，遍历查看是否文件修改时间发生变化，
			// 如果文件发生变化，通知所有的监听,以跟踪文件变化
			Collection<File> files = new ArrayList<File>(files_.keySet());

			for (Iterator<File> i = files.iterator(); i.hasNext();)
			{
				File file = i.next();
				long lastModifiedTime = ((Long) files_.get(file)).longValue();
				long newModifiedTime = file.exists() ? file.lastModified() : -1;

				// 检查文件修改时间是否发生变化
				if (newModifiedTime != lastModifiedTime)
				{

					// 保持新的文件修改时间
					files_.put(file, new Long(newModifiedTime));

					// 通知监听器
					for (Iterator<WeakReference<FileListener>> j = listeners_.iterator(); j.hasNext();)
					{
						WeakReference<FileListener> reference = j.next();
						FileListener listener = reference.get();

						// 从列表中删除已被GC回收的对象
						if (listener == null)
							j.remove();
						else
							listener.fileChanged(file);
					}
				}
			}
		}
	}
}
