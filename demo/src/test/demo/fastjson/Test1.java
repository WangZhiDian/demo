package test.demo.fastjson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test1
{
//	@Test
	public void test1()
	{
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		String dateStr = "1989-09-23";
		
		try {
			Date date = format.parse(dateStr); 
			System.out.println(date.getTime());
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
//	@Test
	public void test()
	{
		String str = "{\"code\":\"200\",\"msg\":\"policyB2Bcreatesuccess!\",\"result\":{\"resultList\":[{\"claimAmount\":1290000,\"policyNo\":\"601021702201604845995442614\",\"reportDate\":1459526400446,\"handletext\":\"拒绝\"}]}}";
		str = "{\"code\":\"200\",\"msg\":\"policyB2Bcreatesuccess!\",\"result\":{\"resultList\":[{\"claimAmount\":1290000,\"policyNo\":\"601021702201604845995442614\",\"reportDate\":1459526400446,\"handletext\":\"拒绝\"},{\"claimAmount\":5540000,\"policyNo\":\"601021702201604845995442614\",\"reportDate\":1459526400524,\"handletext\":\"通过\"}]}}";
		JSONObject json = JSON.parseObject(str);
		System.out.println(json);
		JSONArray arr = (JSONArray) json.getJSONObject("result").get("resultList");
		
		System.out.println(arr.toString());
		System.out.println("");
		
		long time = arr.getJSONObject(0).getLongValue("reportDate");
		System.out.println(time);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date(time)));
		
		int length = arr.size();
		String applydate = "";
		for(int i = 0; i < length; i++)
		{
			time = arr.getJSONObject(i).getLongValue("reportDate");
			applydate = sdf.format(new Date(time));
			System.out.println("i:" + i + "   date:" + applydate);
			arr.getJSONObject(i).put("reportDate", applydate);
		}
		System.out.println(arr.toString());
		
	}
	
//	@Test
	public void test2()
	{
		String[] a = null;
		JSONObject obj = new JSONObject();
		obj.put("list", a);
		System.out.println("arr:" + obj.toString());
		
		HashMap map = new HashMap();
		map.put("imageUrl", "aaa.jpg");
		HashMap map1 = new HashMap();
		map1.put("imageUrl", "bbb.jpg");
		ArrayList list = new ArrayList();
		list.add(map);
		list.add(map1);
		System.out.println(list.toString());
		
		Date d = new Date();
		System.out.println(d.getTime());
	}
	
//	@Test
	public void test3()
	{
//		InputStream is = getTestIs();
		String a = "HpmI-OVsXUBFhrt7hOXl_XvCMWY6b9rWTmcANxt7nK1yNP2Fs7sA_9L7xYGeMBvA=";
		System.out.println("a.length:" + a.length());
			   a =	"09051865933986568234574375617122606163883348144788";
		System.out.println("a.length:" + a.length());
	}
	
	public InputStream getTestIs()
	{
		String fileName = "F:\\other\\me.png";
		File file = new File(fileName);
		System.out.println("file:" + file.getName() + "  length:" + file.length());
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return is;
	}
	
	@Test
	public void test4()
	{
		try
		{
			String time = "1989-09-23 00:00:00";
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date = sdf.parse(time);
			long a = date.getTime();
			
			System.out.println("time:" + a);
			
			String d = sdf.format(a);
			
			System.out.println("d:" + d);
			
			long s = -287827200000L;
			
			System.out.println("s:" + sdf.format(s));
			
			long m = -287827200000L;
			
			System.out.println("s:" + sdf.format(m));
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
//	@Test
	public void fakeClaimProcess()
	{
		JSONArray arr = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("policyNo", "601021702201604845995442614");
		obj.put("claimAmount", 1290000);
		obj.put("handletext", "拒绝");
		obj.put("reportDate", "2016-10-15");
		arr.add(obj);
		System.out.println(arr.toString());
		
		JSONObject obj2 = new JSONObject();
		obj2.put("data", arr);
		System.out.println(obj2.toString());
	}
}
