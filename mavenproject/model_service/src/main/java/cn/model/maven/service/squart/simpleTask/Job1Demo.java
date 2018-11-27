package cn.model.maven.service.squart.simpleTask;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class Job1Demo {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    Logger logger2 = LoggerFactory.getLogger("mylogger");

    public void sayHello()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        String time = sdf.format(new Date());
        log.info("log2randomtaskjob1=== execute ====== " + time + "  count: " + Math.round(Math.random() * 20) + 1);
//        logger.info("logger taskjob1=== execute ====== " + time);
//        logger2.info("logger2 taskjob1=== execute ====== " + time);
    }

}
