package cn.model.maven.init.initMethodInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitMethodInit {

    @Autowired
    InitMehodServiceIml initMehodServiceIml;

    public InitMethodInit()
    {
        log.info("in InitMethodInit.contruct===========");
    }

    public void init()
    {
        log.info("in initMethodInit.init==========");
        initMehodServiceIml.test();

    }

}
