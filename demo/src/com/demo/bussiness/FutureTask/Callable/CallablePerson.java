package com.demo.bussiness.FutureTask.Callable;

import java.util.concurrent.Callable;

import com.demo.bussiness.FutureTask.bean.Person;

public class CallablePerson  extends CallableItem<Person> implements Callable<Person>{

	public CallablePerson(Person input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Person call() throws Exception
	{
		Person p = super.obj;
		if("testName4".equals(p.getName()))
			Thread.sleep(3000);
		System.out.println("my name is :" + p.getName());
		p.setName(p.getName() + "change");
		return p;
	}
	
}
