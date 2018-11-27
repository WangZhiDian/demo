package cn.model.test;

import cn.model.TestClass.MyTestClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wangdian05 on 2018/7/6.
 */
public class JunitTest3
{
//    @Test
    public void Test1() throws Exception {
        String[] s = {"a","b","c"};
        List<String> list = Arrays.asList(s);

        final String t = "123";
        list.forEach((str)->{
            System.out.println("----------- str = " + str + " --- " + t);
        });

    }

//    @Test
    public void Test2()
    {
        String[] s = {"a","b","c"};
        List<String> list = Arrays.asList(s);

        for (String str:list)
        {
            System.out.println("--"+str);
        }

        list.forEach((str)->{
            System.out.println("----" + str);
        });

    }

    @Test
    public void test3()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("----aaa");
            }
        }).start();

        new Thread(()->{
            System.out.println("---bbb");
        }).start();

        new Thread(()-> System.out.println("---ccc") ).start();

        MyTestClass a = new MyTestClass();
//        String b = a()->"b";



    }



}
