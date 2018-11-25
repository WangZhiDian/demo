
package test.core.batch.disruptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.demo.bussiness.batch.disruptor.ScheduledDisruptorBatch;

/** 
 * @Description ProcessResultDisruptorBatch的一个使用实例
 * @time 2016年3月23日 下午4:06:37 
 */
//@Service
public class StringAsync extends ScheduledDisruptorBatch<String, String>
{
	private final Logger logger=LoggerFactory.getLogger(StringAsync.class);
	
//	StringAsync()
//	{
//		super(2000);
//	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> read_data()
	{
		ArrayList<String> data = new ArrayList<String>();

        Random rand = new Random();
        int count = rand.nextInt(10);

        for (int i = 0; i < count; i++) {

            data.add(i+"test");
        }
        logger.debug("submit:"+data);
        logger.error("submit:"+data);
        
		return data;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String excute_taskImpl(String value)
	{
		logger.debug("execute:"+value);
		return value+"dealed";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void update_taskImpl(String result)
	{
		logger.debug("update:"+result);
	}

}
