package com.demo.common.resource;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.collect.Sets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.demo.bussiness.Exception.exception.ExceptionCode;
import com.demo.common.util.Function.DateUtil;

/**
 * 定义调用接口返回的数据格式
 * 
 * { "code": "错误代码", "message": "错误描述", "data": {} } 错误代码和错误描述 定义在
 * ExceptionCode中,约定 0为正确相应
 * 然后通过HeraExceptionMapper统一处理所有的异常，并且通过APIResponse返回给前端。
 * 
 * @author wanghl80
 *
 */
public class APIResponse implements Serializable
{
	private static final long serialVersionUID = -3272004536045592039L;

	private static final String DATE_PATTERN="yyyy-MM-dd HH:mm:ss";
	private JSONObject jsonResult=null;
	private static final String DATA = "data";
	private static final String CODE = "code";
	private static final String MESSAGE = "message";
	private static final String REFER_URL = "refer_url";
	private static final String PROCESS_DATE = "process_date";
	private static final String CLIENT_IP = "client_ip";
	// 默认的正确响应
	private static final String SUCCESS_CODE = "0";
	private static final String SUCCESS_MESSAGE = "接口成功执行";

	public APIResponse(Object data)
	{
		jsonResult=new JSONObject();
		setData(data);
		setCode(SUCCESS_CODE);
		setMessage(SUCCESS_MESSAGE);
		setReferUrl("");
		setProcessDate(new Date());
	}

	public APIResponse()
	{
		jsonResult=new JSONObject();
		setCode(SUCCESS_CODE);
		setMessage(SUCCESS_MESSAGE);
		setReferUrl("");
		setProcessDate(new Date());
	}

	public boolean success()
	{
		return getCode().equals(SUCCESS_CODE);
	}

	public APIResponse setData(Object data)
	{
		jsonResult.put(DATA, data);
		return this;
	}
	
	public APIResponse setProcessDate(Date date)
	{
		jsonResult.put(PROCESS_DATE, DateUtil.formatDate(date, DATE_PATTERN));
		return this;
	}
	
	public APIResponse setClientIp(String clientIp)
	{
		jsonResult.put(CLIENT_IP, clientIp);
		return this;
	}

	public Object getCode()
	{
		return jsonResult.getString(CODE);
	}

	public APIResponse setCode(Object code)
	{
		jsonResult.put(CODE, code);
		return this;
	}

	public String getMessage()
	{
		return jsonResult.getString(MESSAGE);
	}

	public APIResponse setMessage(String message)
	{
		jsonResult.put(MESSAGE, message);
		return this;
	}

	public String getReferUrl()
	{
		return jsonResult.getString(REFER_URL);
	}

	public APIResponse setReferUrl(String referUrl)
	{
		jsonResult.put(REFER_URL, referUrl);
		return this;
	}
	public APIResponse setExceptionCode(ExceptionCode code)
	{
		setCode(code.getErrorCode());
		setMessage(code.getErrorMessage());
		setReferUrl(code.getReferUrl());
		setData(code.getData());
		return this;
	}
	public String toJSONString()
	{
		return jsonResult.toJSONString();
	}
	/**
	 * @time 2016年4月5日 下午4:02:48 
	 * @param args 要显示的值
	 * @return
	 */
	public String toJSONString(String ...args)
	{
		SimplePropertyPreFilter filter =new SimplePropertyPreFilter();
		Set<String> includes = filter.getIncludes();
		Set<String> set = Sets.newHashSet(args);
		set.add(DATA);
		set.add(CODE);
		set.add(MESSAGE);
		set.add(REFER_URL);
		includes.addAll(set);
		return JSON.toJSONString(jsonResult,filter);
	}
	public String toJSONString(SimplePropertyPreFilter filter)
	{
		return JSON.toJSONString(jsonResult,filter);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return jsonResult.toJSONString();
	}
	
	public Response getResponse(){
		return Response.status(Response.Status.OK).entity(this.toJSONString()).build();
	}

}
