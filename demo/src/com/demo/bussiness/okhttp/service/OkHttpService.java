package com.demo.bussiness.okhttp.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import okhttp3.*;

/**
 * 测试okhttp功能
 * @author wangdian05
 */
@Service
public class OkHttpService
{
	
	private static OkHttpService mInstance;
	
	private OkHttpClient client;
	
	public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
	
	public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
	
	public static OkHttpService getInstance()
	{
		if(mInstance == null)
		{
			synchronized(OkHttpService.class)
			{
				if(mInstance == null)
				{
					mInstance = new OkHttpService();
				}
			}
		}
		return mInstance;
	}
	
	private OkHttpService()
	{
		client = new OkHttpClient();
	}
	
	/**
	 * 同步 http get
	 * @param url
	 * @return Response
	 * @throws IOException
	 */
	public Response getAsResponse(String url) throws IOException
	{
		Request request = new Request.Builder().url(url).build();
		Call call = client.newCall(request);
		Response response = call.execute();
		if(!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		return response;
	}
	
	/**
	 * 同步 get
	 * @param url
	 * @return String
	 * @throws IOException
	 */
	public String getAsString(String url) throws IOException
	{
		Response response = getAsResponse(url);
		return response.body().string();
	}
	
	/**
	 * 同步post
	 * @param url
	 * @param postBody
	 * @return response
	 * @throws IOException
	 */
	public Response postStringAsResponse(String url, String postBody)
			throws IOException
	{
		RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody);
		Request request = new Request.Builder().url(url).post(body).build();
		Response response = client.newCall(request).execute();
		checkRespSuccessful(response);
		return response;
	}
	
	/**
	 * 同步post
	 * @param url
	 * @param postBody
	 * @return string
	 * @throws IOException
	 */
	public String postStringAsString(String url, String postBody)
			throws IOException
	{
		return postStringAsResponse(url, postBody).body().string();
	}
	
	private void checkRespSuccessful(Response response) throws IOException
	{
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
	}
	
	
	
}
