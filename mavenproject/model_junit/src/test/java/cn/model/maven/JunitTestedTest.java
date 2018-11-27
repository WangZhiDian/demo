package cn.model.maven;

import mockit.Injectable;
import mockit.Tested;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Tested参数注解配合着Injection参数共同使用,被Tested注解的对象,使用该对象里面的非原生属性时,Junit会将测试用例中被
 * Injection注解的属性赋值给呗Tested注解的对象里面的非原生属性
 */
public class JunitTestedTest {

    @Tested
    SampleForTest sample;


    @Test
    public void testCase(@Injectable SampleInSampleTest sampleInSampleTest)
    {
        //在sample中，使用了对象SampleInSameTest，由于Tested注解，多以Injection注解的对象被注解给了samle对象属性。
        String str = sample.getSampleStr();
        assertTrue(str == null);
    }


}
