package cn.model.maven.commons.http.util;


import java.util.Map;

public interface IRequest
{
	public String doPost(String url, String content, String ctype, String charset) throws Exception;
	
	public String doPostFile(String url, String fileName, byte[] content, String charset) throws Exception;
	
	public String doGet(String url) throws Exception;
	
	public Map<String, Object> doGetFile(String url) throws Exception;
	
}
