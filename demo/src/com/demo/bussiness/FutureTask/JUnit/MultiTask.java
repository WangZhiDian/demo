package com.demo.bussiness.FutureTask.JUnit;

import com.demo.bussiness.FutureTask.Service.ITask;
import com.demo.bussiness.FutureTask.bean.Person;

public class MultiTask implements ITask<Person>{

	Person p;
	
	public MultiTask(Person p)
	{
		this.p = p;
	}
	
	@Override
	public Person execute_task()
	{
		if(p != null)
			System.out.println("name:" + this.p.getName());
		p.setName("myName :" + p.getName());
		return p;
	}

}
