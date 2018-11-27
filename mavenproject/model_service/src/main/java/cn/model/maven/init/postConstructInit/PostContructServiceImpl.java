package cn.model.maven.init.postConstructInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PostContructServiceImpl {

    public PostContructServiceImpl()
    {
        log.info("PostContructServiceImpl.PostContructServiceImpl----");
    }

    public void initTest()
    {
        log.info("PostContructServiceImpl.initTest----");
    }

}
