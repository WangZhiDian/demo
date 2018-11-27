package cn.model.maven;

import mockit.Injectable;
import mockit.Mocked;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * 该测试注解@injectable 与mock的区别
 * 1 mock的对象实例，实例里面的原始数据为null，对象数据为mock类型
 * 2 injection的对象实例，类的静态方法可以使用，new 的实例可以使用，非静态方法返回null
 */
public class JunitMockitInjectableTest {

    @Test
    public void testMockedClass(@Mocked Locale locale)
    {
        //静态方法不起作用，返回null
        assertTrue(Locale.getDefault() == null);
        //非静态方法（返回类型为string）也不起作用，返回null
        assertTrue(locale.getCountry() == null);
        //new 一个对象，mock的数据也是返回的null
        Locale cl = new Locale("zh", "CN");
        assertTrue(cl.getCountry() == null);
    }


    @Test
    public void testInjectableTest(@Injectable Locale locale)
    {
        //静态方法起作用，返回null
        assertTrue(Locale.getDefault() != null);
        //非静态方法（返回类型为string）也不起作用，返回null
        assertTrue(locale.getCountry() == null);
        //new 一个对象并不受影响
        Locale cl = new Locale("zh", "CN");
        assertTrue(cl.getCountry() != null);
    }


}
