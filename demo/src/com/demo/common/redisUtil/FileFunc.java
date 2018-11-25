package com.demo.common.redisUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class FileFunc
{
	private static Map<String,String> configFile = new ConcurrentHashMap<String,String>();
	
	public static String hexStr2String(String bytestr)  
	//字节流(以字符串表示)存储到文件
	{
		int ll=bytestr.length();
		if (ll%2!=0) return null;
		try
		{
			byte bt_file[]=new byte[ll/2];
			for (int i=0;i<ll/2;i++)
			{
				bt_file[i]=(byte)Integer.parseInt(bytestr.substring(i*2,i*2+2),16);
			}
			String content=new String(bt_file,0,bt_file.length,"GBK");
			return content;
		}
		catch (Exception e)
		{
			System.out.println("com.taikang.utils.FileFunc.byteStr2String:"+e);
			return null;
		}
	}
	
	

	public static boolean saveBytes2File(String filename,byte[] bbb)
	//保存字节流中的数据到文件。
	{
		try
		{
			FileOutputStream out=new FileOutputStream(filename);
//			byte bbb[]=buffer.getBytes("GBK");
			out.write(bbb);
			out.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("com.taikang.utils.FileFunc.saveFile"+e);
			return false;
		}
	}
	public static void logInfo(String filename,String info)
	//按照GBK编码保存数据到文件。
	{
		try
		{
			FileOutputStream out=new FileOutputStream(filename);
			byte bt_prompt[]=info.getBytes("GBK");
			out.write(bt_prompt);
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("com.taikang.utils.FileFunc.logInfo:"+e);
		}
	}

	public static void logInfo(String filename,String info,boolean append)
	//按照GBK编码保存数据到文件,如果文件存在，追加数据到文件中。
	{
		try
		{
			FileOutputStream out=new FileOutputStream(filename,append);
			byte bt_prompt[]=info.getBytes("GBK");
			out.write(bt_prompt);
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("com.taikang.utils.FileFunc.logInfo:"+e);
		}
	}

	public static void logInfo(String filename,String info,boolean append,String encode)
	//按照指定编码保存数据到文件,如果文件存在，追加数据到文件中。
	{
		try
		{
			FileOutputStream out=new FileOutputStream(filename,append);
			byte bt_prompt[]=info.getBytes(encode);
			out.write(bt_prompt);
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("com.taikang.utils.FileFunc.logInfo:"+e);
		}
	}
	
//	public static String getProValue(String path,String keyName)
//	{
//		PropertiesFileMonitor monitor_agent = PropertiesFileMonitor.getInstance();
//        
//        monitor_agent.addFile(new File(path));
//        
//        return monitor_agent.getProperty(path, keyName);
//
//	}
	
	public static String getProValueOld(String path,String name)
	{
		  String value = null;
		  try
		  {
				FileInputStream is=new FileInputStream(new File(path));
				Properties props = new Properties();
				props.load(is);
				value = props.getProperty(name);
				//System.out.println("--value="+value);
				is.close();
		  }
		  catch (Exception e)
		  {
			System.out.println("getProValueOld:"+e.toString());
		  }
		  return value;
	}
	
	public static boolean clear(){
		try {
			configFile.clear();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
}