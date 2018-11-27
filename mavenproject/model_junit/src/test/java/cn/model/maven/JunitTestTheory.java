package cn.model.maven;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

/**
 * 该测试运行类，可以将dataponits中的数据已排列组合的方式，在测试用例中运行
 * 测试用例中使用的数据，是根据数据集的类型来匹配的，下列为测试数据和运行结果，一共运行排列组合
 */
@RunWith(Theories.class)
public class JunitTestTheory {

    @DataPoints
    public static String[] names = {"wang", "dian"};

    @DataPoints
    public static int[] ages = {12, 23};

    @DataPoints
    public static double[] age = {152, 23};

    /*
    WANG age is 12
    WANG age is 23
    DIAN age is 12
    DIAN age is 23
     */
    @Theory
    public void testMethod(String namead, int age)
    {
        System.out.println(String.format("%S age is %s", namead, age));
    }

}
