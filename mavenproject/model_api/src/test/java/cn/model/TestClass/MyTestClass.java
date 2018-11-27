package cn.model.TestClass;

/**
 * Created by wangdian05 on 2018/7/6.
 */
public class MyTestClass implements MyInterface{
    public MyTestClass() {
    }

    @Override
    public String getDesc(String str) {
        return str;
    }
}
