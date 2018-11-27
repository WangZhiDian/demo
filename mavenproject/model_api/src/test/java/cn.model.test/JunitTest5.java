package cn.model.test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangdian05 on 2018/7/23.
 */
public class JunitTest5
{

//    @Test
    public static void Test5() throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();

        for(int i = 0; i < 1000; i++)
        {
            Thread.sleep(50);;
            list.add(new OOMObject());
            System.out.println("aaa:" + i);
        }
        System.gc();
    }

    public static void main(String[] args) throws InterruptedException {
        Test5();
    }

}

class OOMObject{
    public byte[] place  = new byte[64*1024];
}