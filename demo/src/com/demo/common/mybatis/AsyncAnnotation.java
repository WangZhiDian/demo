package com.demo.common.mybatis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
 * @author 王文超
 * @Description 在dao里面加入注解，会将delete、insert、update异步化到disruptor执行
 * @time 2016年3月30日 上午9:32:49 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncAnnotation
{

}
