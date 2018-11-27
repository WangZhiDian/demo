package cn.model.maven;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * 该测试运行器在Suite的基础上增加类别，在具体的执行类中的测试用例上加上类别，则运行时只运行有类别
 * 注解的用例，没有的都不会运行，如果某一个测试用例有多个注解，则可以用ExcludeCategory来去除执行用例
 */
@RunWith(Categories.class)
@Categories.IncludeCategory(Feature1.class)
@Categories.ExcludeCategory(Feature2.class)
@Suite.SuiteClasses(ATest.class)
public class JunitTestCategory {
}
