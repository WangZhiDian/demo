package test.core.batch.daybatch;

import java.util.ArrayList;
import java.util.List;

import com.demo.common.util.batch.SyncBatchExecutor;

/**
 * 从启动tomcat开始运行定时任务,通过web.xml中的servlet启动配置
 */
public class TimedNotifyFilte extends SyncBatchExecutor<TimedNotifyBean, TimedNotifyBean> {

	//单线程读取的数据列表
	@Override
	public List<TimedNotifyBean> read_data() {
		// TODO Auto-generated method stub
		
		TimedNotifyBean bean = new TimedNotifyBean();
		bean.setCid_number("123456789");
		List<TimedNotifyBean> data = new ArrayList<TimedNotifyBean>();
		
		try
		{
			data.add(bean);
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("err:" + e.getMessage());
		}
		
		System.out.println("read data msg satate change:===========");
		return data;
	}
	
	//多线程处理的方法
	@Override
	public TimedNotifyBean excute_taskImpl(TimedNotifyBean value) {
		// TODO Auto-generated method stub
		
		try
		{
			System.out.println(" execute  msg satate change :" + value.getCid_number());
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("err:" + e.getMessage());
		}
//		System.out.println("execute  msg satate change:===========");
		return value;
	}

	
	//单线程处理的列表结果数据
	@Override
	public void update_taskImpl(TimedNotifyBean value) {
		// TODO Auto-generated method stub
		System.out.println("finally getNotify_id:" + value.getCid_number() + "   ==============================");
	}

	
	private static class SingletonHelper {
		private static final TimedNotifyFilte INSTANCE = new TimedNotifyFilte();
	}
	
	public static TimedNotifyFilte getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public TimedNotifyFilte() {
		super();
	}
	

}
