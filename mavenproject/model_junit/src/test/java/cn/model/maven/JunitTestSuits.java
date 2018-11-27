package cn.model.maven;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.Assert.assertTrue;

/**
 * 该方式聚合suite的所有类中的测试方法
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ATest.class,BTest.class})
@ContextConfiguration(locations={"classpath:spring-config.xml"})
public class JunitTestSuits {

    @Test
    public void t1(){
        assertTrue("aaa".equals("aaa"));
    }




}
