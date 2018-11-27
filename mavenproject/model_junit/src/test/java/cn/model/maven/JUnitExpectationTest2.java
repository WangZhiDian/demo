package cn.model.maven;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertTrue;


/**
 * 该注解Expectations的作用主要用于录制，即录制对象调用的返回值，可以对外部类的任意成员变量方法进行调用
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-config.xml"})
public class JUnitExpectationTest2 {

//    @Mocked
    @Autowired
    SampleForTest sample;

//    @Mocked
    @Autowired
    SampleInSampleTest inSample;

    @Test
    public void testRecordOutsice()
    {
        new Expectations(sample.getClass(), inSample.getClass()){
            {
                inSample.getList(new ArrayList<String>());
                result = "wang";
            }
        };

        List<String> list = new ArrayList<String>();
        sample.getArrayList(list);


    }

    @Test
    public void testMockedMethod()
    {
/*        SampleForTest test = new SampleForTest();
        new Expectations(SampleForTest.class){ //在此处，传入待mock类的class
            {
                //只是mockgetInt的方法
                test.getInt();
                result = 10;
            }
        };
        assertTrue(test.getInt() == 10);
        assertTrue(test.getSolidName().equals("solid"));*/
    }





}
