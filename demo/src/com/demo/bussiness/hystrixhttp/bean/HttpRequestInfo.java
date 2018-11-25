package com.demo.bussiness.hystrixhttp.bean;

import okhttp3.RequestBody;

public class HttpRequestInfo
{

	private String url;
	private RequestBody body;
	private String businessId;//渠道，分为sx，cx等
	
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public RequestBody getBody()
	{
		return body;
	}
	public void setBody(RequestBody body)
	{
		this.body = body;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	
	
	
}
