package cn.model.maven.service.disruptor.simpledisruptor.service;

import cn.model.maven.service.disruptor.simpledisruptor.bean.LongEvent;
import cn.model.maven.service.disruptor.simpledisruptor.beanFactory.LongEventFactory;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by wangdian05 on 2018/7/2.
 * 该service在测试时负责实例化对应disruptor
 */
@Slf4j
@Service
public class DisruptorService {

    Disruptor<LongEvent> disruptor = null;
    ExecutorService executor = null;

    public boolean startDisruptor()
    {
        boolean startFlag = false;

        try
        {
            EventFactory<LongEvent> eventFactory = new LongEventFactory();
            executor = Executors.newSingleThreadExecutor();
            int ringBufferSize = 4;
            this.disruptor = new Disruptor<LongEvent>(eventFactory,ringBufferSize,executor, ProducerType.SINGLE, new YieldingWaitStrategy());

            EventHandler<LongEvent> eventHandler = new LongEventHandler();
            disruptor.handleEventsWith(eventHandler);
            disruptor.start();
            startFlag = true;
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return startFlag;
    }

    //-发布事件的一种实践方法----------------------------------------------------------------------------
    public String publishAnEvent(String name)
    {
        RingBuffer<LongEvent> ringbuffer = disruptor.getRingBuffer();
        long sequence = ringbuffer.next();
        log.info("sequence:{}, name:{}", sequence, name);
        try
        {
            LongEvent event = ringbuffer.get(sequence);
            event.setName(name);
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            ringbuffer.publish(sequence);
        }

        return "";
    }

    //-发布事件的另一种实践方法,创建一个传输数据的Translator类，继承EventTranslatorOneArg类----------------------------------------------------------------------------
    static class Translator implements EventTranslatorOneArg<LongEvent, String>
    {
        @Override
        public void translateTo(LongEvent event, long sequence, String name) {
            // TODO Auto-generated method stub
            event.setName(name);
            log.info("event:" + event.getName() + "|sequence:" + sequence + "|name:" + name);
        }
    }

    public static Translator TRANSLATOR = new Translator();

    // 发布事件；
    public void publishEvent2(String name)
    {
        RingBuffer<LongEvent> ringbuffer = disruptor.getRingBuffer();
        ringbuffer.publishEvent(TRANSLATOR, name);
    }


}
