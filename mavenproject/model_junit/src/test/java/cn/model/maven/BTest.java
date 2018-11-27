package cn.model.maven;

import org.junit.*;
import org.junit.runners.MethodSorters;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.DEFAULT)  //该配置注解指定test类运行的顺序，Default 按照方法名称的hash值来顺序，name_ascending按照命名字典来顺序
@Component
public class BTest {

    @BeforeClass //该方法在所有Test运行之前执行，用static修饰，如果有父类，则父类的在之类之前执行
    public static void beforeClass() {
        System.out.println("run before all test===============");
    }

    @AfterClass //该方法在所有Test运行完之后执行，用static修饰
    public static void afterClass() {
        System.out.println("run after all test================");
    }

    @Before //该方法在每一个Test运行之前执行，在test运行前可能需要创建对象等场景
    public void before() {
        System.out.println("run before test case ---------------");
    }

    @After //该方法在每一个Test运行完之后执行，用于释放资源等操作，及时test方法抛出异常，该方法同样会执行
    public void after() {
        System.out.println("run after all test case ------------");
    }


    @Test
    public void t1(){
        System.out.println("run t1.");
        assertTrue("aaa".equals("aaa"));
    }

    @Test(timeout = 2000)
    public void t2(){
        System.out.println("run t2.");
        assertTrue("aaa".equals("aaa")); }

/*    @Test(expected = XXX.class)
    public void t3(){
        assertTrue("aaa".equals("aaa"));
    }*/

    //该注解，将@Test的注解类在执行的时候忽略
    @Ignore
    @Test
    public void ignore() { assertTrue(true); }



}
