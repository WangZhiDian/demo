package com.demo.common.util.Function;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

public class FunctionUtil
{
	
	/**
	 * @Description: ǩ���㷨
	 * @param map Ҫ����ǩ���ļ�����Ϣ
	 * @param key ǩ����key
	 * @return ǩ��
	 */
	public static String getSign(Map<String, Object> map, String key)
	{
		ArrayList<String> list = new ArrayList<String>();
		for (Map.Entry<String, Object> entry : map.entrySet())
		{
			String k = entry.getKey();
			Object v = entry.getValue();
			if (null != v) {
				list.add(k + "=" + v + "&");
			}
		}
		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.append("key=" + key).toString();
		
		System.out.println(result);
//		result = EncodeUtil.encodeByMD5(result).toUpperCase();
		try {
			result = DigestUtils.md5Hex(result.getBytes("utf-8")).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ���������յ��ļ���
	 * @param file
	 * @return
	 */
	public static String fileName(String file)
	{
		Calendar ca = Calendar.getInstance();
		int yy = ca.get(Calendar.YEAR);
		int mm = ca.get(Calendar.MONTH) + 1;
		int dd = ca.get(Calendar.DAY_OF_MONTH);
		String filename = file + yy + mm + dd + ".log";
		return filename;
	}
	
	/**
	 * �������capacityΪ���������
	 */
	private static String num[] = {"0","1","2","3","4","5","6","7","8","9"};
	public static String randomIntegerStr(int capacity)
	{
		 StringBuffer sb = new StringBuffer();
		 for(int ig=0;ig<capacity;ig++)
		 {
			 int n = (int) (10*Math.random());
			 sb.append(num[n]);
		 }
		 return sb.toString(); 
	}
	
	/**
	 * �������capacityλ�����֣�Сд����д��ĸ��ɵ�����ַ���
	 */
	private static String str[] = {"0","1","2","3","4","5","6","7","8","9","q","w","e","r","t","y","u","i","o",
			 "p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m","Q","W","E","R","T","Y","U","I","O",
			 "P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M"};
	public String makeStr(int capacity)
	{
		 
		 StringBuffer sb = new StringBuffer();
		 for(int ig=0;ig<capacity;ig++){
			 int n = (int) (62*Math.random());
			 sb.append(str[n]);
		 }
		 return sb.toString(); 
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
