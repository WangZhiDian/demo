package cn.model.maven.init.postConstructInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PostConstructInit
{

    @Autowired
    PostContructServiceImpl postContructServiceImpl;

    public PostConstructInit()
    {
        log.info("postContructInit start----------------");
//        postContructServiceImpl.initTest();
    }

    @PostConstruct
    public void init()
    {
        log.info("in postContructInit  init method---------------");
        postContructServiceImpl.initTest();
    }

}
