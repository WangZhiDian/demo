package cn.model.maven;

import mockit.Mock;
import mockit.MockUp;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * mock 注解，可以完全模拟接口，想模拟哪个方法，就给哪个方法上增加@Mock注解
 */
public class JunitMockUpTest {


    @Test
    public void testMockUp()
    {
        /**
         * 该方法类似于重新定义了一次方法，后面实例化该类的时候，都用的这个方法调用
         */
        new MockUp<SampleForTest>(SampleForTest.class)
        {
            @Mock
            public String getSolidName()
            {
                return "changesolidname";
            }
        };

        SampleForTest test = new SampleForTest();
        assertTrue(test.getSolidName().equals("changesolidname"));
    }

}
