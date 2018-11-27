package cn.model.maven;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertTrue;


/**
 * 该注解Expectations的作用主要用于录制，即录制对象调用的返回值，可以对外部类的任意成员变量方法进行调用
 */
public class JUnitExpectationTest {

    @Mocked
    SampleForTest sample;

    @Mocked
    Calendar cal;

    @Test
    public void testRecordOutsice()
    {
        new Expectations(){
            {
                //对cal.get方法进行录制,无论哪年,该方法输出都是2016
                cal.get(Calendar.YEAR);
                result = 2016;
                cal.get(Calendar.HOUR_OF_DAY);
                result = 7;
            }
        };
        assertTrue(cal.get(Calendar.YEAR) == 2016);
        assertTrue(cal.get(Calendar.HOUR_OF_DAY) == 7);
        assertTrue(cal.get(Calendar.WEEK_OF_YEAR) == 0);
    }

    /**
     * 该方法只是录制待mock的类的部分指定方法,其他方法不受mock影响
     */
    @Test
    public void testMockedMethod()
    {
        SampleForTest test = new SampleForTest();
        new Expectations(SampleForTest.class){ //在此处，传入待mock类的class
            {
                //只是mockgetInt的方法
                test.getInt();
                result = 10;
            }
        };
        assertTrue(test.getInt() == 10);
        assertTrue(test.getSolidName().equals("solid"));
    }


    /**
     * 该方法只是录制待mock的类的部分指定的类实例，其他实例不受影响
     */
    @Test
    public void testMockedObject()
    {
        SampleForTest test = new SampleForTest();
        new Expectations(test){ //在此处，传入待mock类的实例，只有这个实例的返回，会返回mock的值
            {
                //只是mockgetInt的方法
                test.getInt();
                result = 10;
            }
        };
        assertTrue(test.getInt() == 10);
        assertTrue(test.getSolidName().equals("solid"));

        //下面的对象是新对象，还是使用对象的值
        SampleForTest testnew = new SampleForTest();
        assertTrue(testnew.getInt() == 2);
        testnew.getSolidName();

        //Verifications标签，是在测试test中统计某个方法被调用了多少次，格式 XXX.getXXX(); times=N; t统计某方法
        //调用的次数是否符合定义的次数
        new Verifications(){
            {
                testnew.getSolidName();
                times = 1;  //该处即表示上述方法，调用了1次，若不为一次，该处test将失败
            }
        };



    }



}
