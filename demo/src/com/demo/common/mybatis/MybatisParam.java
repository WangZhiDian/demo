package com.demo.common.mybatis;

public class MybatisParam
{
	private String methodName;
	private String statement;
	private Object parameter;
	public String getMethodName()
	{
		return methodName;
	}
	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}
	public String getStatement()
	{
		return statement;
	}
	public void setStatement(String statement)
	{
		this.statement = statement;
	}
	public Object getParameter()
	{
		return parameter;
	}
	public void setParameter(Object parameter)
	{
		this.parameter = parameter;
	}
	
}
