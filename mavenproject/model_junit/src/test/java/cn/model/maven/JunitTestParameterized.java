package cn.model.maven;

/**
 * 该类使用参数测试，数据准备必须在一个方法中，方法必须有parameters注解，必须为public static的必须返回collection类型
 * 该方法一次可以组装多组测试数据
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class JunitTestParameterized {

    private int input1;
    private int input2;
    private int expected;

    @Parameterized.Parameters
    public static Collection prepareData()
    {
        Object[][] object = {{-1, -2, -3},{-1, 2, 1},{-1, 2, 3}};
        return Arrays.asList(object);
    }

    public JunitTestParameterized(int input1, int input2, int expected){
        this.input1 = input1;
        this.input2 = input2;
        this.expected = expected;
    }

    @Test
    public void testAdd()
    {
        int ret = add(input1, input2);
        assertEquals(ret, expected);
    }

    public int add(int input1, int input2)
    {
        return input1 + input2;
    }

}
