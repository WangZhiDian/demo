package com.demo.bussiness.lombok.bean;

import lombok.Data;

/**
 * 使用lombok的优势：利用Data注解，可以在bean类中代码省略getter和setter，在编译运行时，自动生成get和set方法
 * 弊病：增加可读性复杂度，在没有共同环境下，多人合作容易保代码错误
 * jar:lombok-1.16.6.jar
 * @author wangdian05
 */
@Data
public class Person
{
	private String name;
	
	private String addr;
	
}
