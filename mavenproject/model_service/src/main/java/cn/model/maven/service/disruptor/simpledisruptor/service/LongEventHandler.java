package cn.model.maven.service.disruptor.simpledisruptor.service;

import cn.model.maven.service.disruptor.simpledisruptor.bean.LongEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by wangdian05 on 2018/7/2.
 * 时间处理器，接收到时间触发，执行onEvent请求
 */
@Slf4j
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {

        String name = event.getName();
        log.info("name:{}, sequence：---{}, endOfBatch:{}", name, sequence, endOfBatch);
    }
}
