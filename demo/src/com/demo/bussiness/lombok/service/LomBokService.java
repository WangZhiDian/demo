package com.demo.bussiness.lombok.service;

import com.demo.bussiness.lombok.bean.Person;

public class LomBokService
{
	
	public void getAddr()
	{
		Person person = new Person();
		person.setName("xiaoming");
		person.setAddr("laifeng");
		
		System.out.println("person.name:" + person.getName());
		System.out.println("person.addr:" + person.getAddr());
	}
}
