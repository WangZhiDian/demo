package cn.model.maven;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SampleInSampleTest {

    public String getSampleStr()
    {
        return "sampleStr";
    }

    public String getList(List<String> list)
    {
        List<String> ll = new ArrayList();
        return "myList";
    }

}
