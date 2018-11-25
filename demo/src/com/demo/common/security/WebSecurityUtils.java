package com.demo.common.security;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class WebSecurityUtils
{
	
	public String getIpAddr(HttpServletRequest request)
	{
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
				// ��������ȡ�������õ�IP
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
		// ����ͨ�����������������һ��IPΪ�ͻ�����ʵIP,���IP����','�ָ�
		if (ipAddress != null && ipAddress.length() > 15)
		{ // "***.***.***.***".length() = 15
			if (ipAddress.indexOf(",") > 0)
			{
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}
	
	public String getSignature(RequestParam questParam)
	{
		String signString = "";
		
		try
        {
			Arrays.sort(questParam.getParamArrays());

			int arraysLength = questParam.getParamArrays().length;
			for (int i = 0; i < arraysLength; i++)
			{
				signString += questParam.getParamArrays()[i];
			}
        }
        catch (Exception e)
        {
        	System.out.println("getSignature ��������");
        	e.printStackTrace();
        	return "";
        }
        
		return Md5Encrypt.getMD5Mac(signString + "key", "utf-8");
	}
	

}


