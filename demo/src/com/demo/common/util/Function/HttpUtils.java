package com.demo.common.util.Function;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
/**
 * 获取ip地址和端口
 */
public class HttpUtils
{
	
	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress))
		{
			ipAddress = request.getRemoteAddr();
			if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1"))
			{
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try
				{
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
				if (inet!=null)
				{
					ipAddress = inet.getHostAddress();
				}
			}
		}
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15)
		{ // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0)
			{
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	public static String getMachineIpAndPort() {
		try
        {
	        return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
	        e.printStackTrace();
        }
		
		return "unknown";
	}
	
	public static Map<String, String> getMapByQueryString(String queryString){
		Map<String, String> map = new HashMap<String, String>();
		queryString = queryString == null ? "" : queryString.trim();
		if(!"".equals(queryString)){
			String[] arr1 = queryString.split("&");
			for(String param : arr1){
				String[] arr2 = param.split("=");
				if(arr2.length == 2){
					map.put(arr2[0], arr2[1]);
				}
			}
		}
		return map;
	}
}
