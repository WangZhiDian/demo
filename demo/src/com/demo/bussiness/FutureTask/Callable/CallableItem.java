package com.demo.bussiness.FutureTask.Callable;

import java.util.concurrent.Callable;

public class CallableItem<T> implements Callable<T>{

	protected T obj;
	
	public CallableItem(T input)
	{
		this.obj = input;
	}
	
	/* �÷�����Ҫ��ʵ�ָ���,����ҵ���߼�
	 * (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	@Override
	public T call() throws Exception 
	{
		
		return obj;
	}

}
