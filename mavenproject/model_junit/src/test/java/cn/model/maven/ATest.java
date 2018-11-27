package cn.model.maven;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.assertTrue;

interface Feature1{}

interface Feature2{}

class Feature3{}

public class ATest {

    @Test
    public void t1(){
        assertTrue("aaa".equals("aaa"));
    }

    @Category(Feature1.class)
    @Test
    public void feature1(){ assertTrue(true); }

    @Category(Feature2.class)
    @Test
    public void feature2(){ assertTrue(true); }

    @Category({Feature1.class, Feature2.class})
    @Test
    public void feature1And2(){ assertTrue(true); }

    @Category({Feature3.class})
    @Test
    public void feature3(){ assertTrue(true); }

}
