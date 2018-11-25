package com.demo.common.security;

public class RequestParam
{

	private String bussinessId;
	private long timestamp;
	private String sign;
	private String[] paramArrays;
	private int requestMaxTimes = 0;
	private int timeout = 0;
	
	public long getTimestamp()
	{
		return timestamp;
	}
	public void setTimestamp(long timestamp)
	{
		this.timestamp = timestamp;
	}
	public String getSign()
	{
		return sign;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	public String[] getParamArrays()
	{
		return paramArrays;
	}
	public void setParamArrays(String[] paramArrays)
	{
		this.paramArrays = paramArrays;
	}
	public int getRequestMaxTimes()
	{
		return requestMaxTimes;
	}
	public void setRequestMaxTimes(int requestMaxTimes)
	{
		this.requestMaxTimes = requestMaxTimes;
	}
	public int getTimeout()
	{
		return timeout;
	}
	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}
	public String getBussinessId()
    {
	    return bussinessId;
    }
	public void setBussinessId(String bussinessId)
    {
	    this.bussinessId = bussinessId;
    }
}
