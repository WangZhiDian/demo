package cn.model.maven;

import mockit.Mocked;
import org.junit.Test;

import javax.servlet.http.HttpSession;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * jmockit 帮助测试用例在应用对象的时候，生成一个mock对象实例，但对象里面的属性为空或者为mock类型
 */
public class JunitMocketTest {

    @Mocked
    Locale locale;

    @Test
    public void testMockedClass()
    {
        //静态方法不起作用，返回null
        assertTrue(Locale.getDefault() == null);

        //非静态方法（返回类型为string）也不起作用，返回null
        assertTrue(locale.getCountry() == null);

        Locale cl = new Locale("zh", "CN");
        assertTrue(cl.getCountry() == null);
    }

    @Mocked
    HttpSession session;

    @Test
    public void testMockedInterface()
    {
        //原始类型方法返回String，返回null
        assertTrue(session.getId() == null);
        //原始类型返回非String，返回null
        assertTrue(session.getCreationTime() == 0L);
        //返回非原始类型，返回不为空，mockit帮助实例化一个mock对象
        assertTrue(session.getServletContext() != null);
        //mockit对象返回的mockit对象，里面的方法也不起作用，返回null
        assertTrue(session.getServletContext().getContextPath() == null);
    }



}
