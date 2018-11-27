package cn.model.maven.service.disruptor.simpledisruptor.bean;

import lombok.Data;

/**
 * Created by wangdian05 on 2018/7/2.
 *
 * 建立一个事件Event，通过时间进行数据交换
 *
 */
@Data
public class LongEvent {

    private long value;

    private String name;

/*    public void setValue(long value)
    {
        this.value = value;
    }

    public long getValue() {
        return value;
    }*/

}
