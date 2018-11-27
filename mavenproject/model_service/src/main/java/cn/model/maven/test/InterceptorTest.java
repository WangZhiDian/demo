package cn.model.maven.test;

import org.springframework.stereotype.Component;

@Component
public class InterceptorTest {

    public void springaoptest1()
    {
        System.out.println("in intercept object");
    }

    public void springnotaoptest2()
    {
        System.out.println("in intercept object");
    }

}
