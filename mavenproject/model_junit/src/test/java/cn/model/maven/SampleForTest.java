package cn.model.maven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SampleForTest {

    @Autowired
    SampleInSampleTest sampleInSampleTest;

    public void makeSample()
    {
        sampleInSampleTest = new SampleInSampleTest();
    }

    public String getSampleStr()
    {
        return sampleInSampleTest.getSampleStr();
    }

    public int getInt()
    {
        return 2;
    }

    public String getSolidName()
    {
        return "solid";
    }

    public String getName(String name)
    {
        return "wang";
    }

    public static String getStaticStr()
    {
        return "staticstr";
    }

    public String getArrayList(List<String> param)
    {
        String str = sampleInSampleTest.getList(param);
        return "voi";
    }

}
