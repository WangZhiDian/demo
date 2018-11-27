package cn.model.maven.init.initMethodInit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitMehodServiceIml {

    public InitMehodServiceIml()
    {
        log.info("==============in InitMehodServiceIml.InitMehodServiceIml");
    }

    public void test()
    {
        log.info("==============in InitMehodServiceIml.test");
    }


}
