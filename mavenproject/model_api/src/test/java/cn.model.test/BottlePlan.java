package cn.model.test;

import org.junit.Test;

/**
 * Created by wangdian05 on 2018/6/22.
 * 例子：三个啤酒盖换一瓶啤酒，能够喝多少平啤酒。
 */
public class BottlePlan {

    public int getBottleCount(int lid)
    {
        int bottles = 0;
        if(lid/3 < 1)
            return 0;
        else
            bottles = lid/3 + getBottleCount(lid/3 + lid%3);
        return bottles;
    }

    @Test
    public void getBottle()
    {
        int bottle = 10;
        bottle = bottle + getBottleCount(bottle);
        System.out.println("bottle:" + bottle);
    }



}

