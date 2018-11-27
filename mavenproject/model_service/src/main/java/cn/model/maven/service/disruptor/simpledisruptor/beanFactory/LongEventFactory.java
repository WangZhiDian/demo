package cn.model.maven.service.disruptor.simpledisruptor.beanFactory;

import cn.model.maven.service.disruptor.simpledisruptor.bean.LongEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Created by wangdian05 on 2018/7/2.
 * 定义一个处理工程，实例化LongEvent对象，该对象从ringbuffer中获取，然后填充数据
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }


}
