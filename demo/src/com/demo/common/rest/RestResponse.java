package com.demo.common.rest;

import java.io.Serializable;
import java.util.Set;

import jersey.repackaged.com.google.common.collect.Sets;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.demo.bussiness.Exception.exception.ExceptionCode;

/**
 * 定义调用接口返回的数据格式
 * 
 * { "error_code": "错误代码", "error_message": "错误描述", "data": {} } 错误代码和错误描述 定义在
 * ExceptionCode中,约定 0为正确相应
 * 然后通过HeraExceptionMapper统一处理所有的异常，并且通过RestResponse返回给前端。
 * 
 * @author wanghl80
 *
 */
public class RestResponse implements Serializable
{
	private static final long serialVersionUID = -3272004536045592039L;

	private JSONObject jsonResult=null;
	private static final String DATA = "data";
	private static final String ERROR_CODE = "error_code";
	private static final String ERROR_MESSAGE = "error_message";
	private static final String REFER_URL = "refer_url";
	// 默认的正确响应
	private static final String SUCCESS_CODE = "0";
	private static final String SUCCESS_MESSAGE = "接口成功执行";

	public RestResponse(Object data)
	{
		jsonResult=new JSONObject();
		setData(data);
		setErrorCode(SUCCESS_CODE);
		setErrorMessage(SUCCESS_MESSAGE);
		setReferUrl(REFER_URL);
	}

	public RestResponse()
	{
		jsonResult=new JSONObject();
		setErrorCode(SUCCESS_CODE);
		setErrorMessage(SUCCESS_MESSAGE);
		setReferUrl(REFER_URL);
	}

	public boolean success()
	{
		return getErrorCode().equals(SUCCESS_CODE);
	}

	public RestResponse setData(Object data)
	{
		jsonResult.put(DATA, data);
		return this;
	}

	public Object getErrorCode()
	{
		return jsonResult.getString(ERROR_CODE);
	}

	public RestResponse setErrorCode(Object errorCode)
	{
		jsonResult.put(ERROR_CODE, errorCode);
		return this;
	}

	public String getErrorMessage()
	{
		return jsonResult.getString(ERROR_MESSAGE);
	}

	public RestResponse setErrorMessage(String errorMessage)
	{
		jsonResult.put(ERROR_MESSAGE, errorMessage);
		return this;
	}

	public String getReferUrl()
	{
		return jsonResult.getString(REFER_URL);
	}

	public RestResponse setReferUrl(String referUrl)
	{
		jsonResult.put(REFER_URL, referUrl);
		return this;
	}
	public RestResponse setExceptionCode(ExceptionCode code)
	{
		setErrorCode(code.getErrorCode());
		setErrorMessage(code.getErrorMessage());
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
		set.add(ERROR_CODE);
		set.add(ERROR_MESSAGE);
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

}
