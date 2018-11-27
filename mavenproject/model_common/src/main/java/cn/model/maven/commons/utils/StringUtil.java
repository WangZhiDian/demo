package cn.model.maven.commons.utils;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String工具类
 * 
 */
public class StringUtil {
	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

	/**
	 * 字符串不为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str){
		return ((str != null) && (str.trim().length() > 0));
	}
	
	/**
	 * 字符串为空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return ((str == null) || (str.trim().length() == 0));
	}
	
	/**
	 * 生成随机字符串
	 * 
	 * @param length
	 *            生成字符串的长度
	 * @return
	 */
	public static String getRandomString(int length) {
		String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * 写日志信息
	 * 
	 * @param fileName
	 * @param sWord
	 *            void
	 */
	public static void logResult(String fileName, String sWord) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.write("\r\n" + sWord);
			writer.flush();
		} catch (Exception e) {
			logger.error("stringUtil logResult Exception:",e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					logger.error("stringUtil logResult IOException:",e);
				}
			}
		}
	}
	
	public static void logResult(String sWord) {
		logResult("/usr/local/tomcat/logs/wxpay.txt", sWord);
    }

	/**
	 * 字符串转为二进制字符串
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException String
	 */
	public static String stringToBinary(String str) throws UnsupportedEncodingException {
		if(isNotEmpty(str)) {
			char[] strChar=str.toCharArray();
	        String result="";
	        for(int i=0;i<strChar.length;i++){
	            result +=Integer.toBinaryString(strChar[i])+ " ";
	        }
	        return result;
		} else {
			return "";
		}
	}
	

	/**
	 * 二进制字符串转为正常字符串
	 * 
	 * @param str
	 * @return String
	 */

	public static String binaryToString(String str) {
		if(isNotEmpty(str)) {
			String result = "";
			String[] tempStr=StrToStrArray(str);
	        char[] tempChar=new char[tempStr.length];
	        for(int i=0;i<tempStr.length;i++) {
	            tempChar[i]=BinstrToChar(tempStr[i]);
	        }
	        result = String.valueOf(tempChar);
			return result;
		} else {
			return "";
		}
		
	}
	

	/**
	 * 字符串转为字符串数组，已空格区分 主要为了二进制字符串转换使用
	 * 
	 * @param str
	 * @return String[]
	 */
	public static String[] StrToStrArray(String str) {
        return str.split(" ");
    }
	
	/**
	 * 二进制字符串转换为字符
	 * 
	 * @param binStr
	 * @return char
	 */
	public static char BinstrToChar(String binStr){
        int[] temp=BinstrToIntArray(binStr);
        int sum=0;   
        for(int i=0; i<temp.length;i++){
            sum +=temp[temp.length-1-i]<<i;
        }   
        return (char)sum;
    }
	
	/**
	 *	二进制字符串转为数组
	 * 
	 * @param binStr
	 * @return int[]
	 */
	public static int[] BinstrToIntArray(String binStr) {       
        char[] temp=binStr.toCharArray();
        int[] result=new int[temp.length];   
        for(int i=0;i<temp.length;i++) {
            result[i]=temp[i]-48;
        }
        return result;
    }
	
	public static Boolean isInclude(String[] strs, String str){
		Boolean isInclude = false;
		if(strs != null && strs.length > 0 && isNotEmpty(str)){
			for (String string : strs) {
				if(isNotEmpty(string) && string.equals(str)){
					isInclude = true;
				}
			}
		}
		return isInclude;
	}
	
	/**
	 * 判断两个字符串是否相同
	 * @param args1
	 * @param args2
	 * @return
	 */
	public static Boolean isEquals(String args1, String args2) {
		if(args1 != null) {
			return args1.equals(args2) ? true : false;
		}
		if(args2 != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为数字形式
	 * @param str
	 * @return
	 */
	public static Boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*"); 
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
	       return false; 
		} 
		return true; 
	}


	/**
	 * 拼接url参数字符串
	 */
	public static String appendParam(String returns, String paramId, String paramValue) {
		if (StringUtil.isNotEmpty(returns)) {
			if (StringUtil.isNotEmpty(paramValue)) {
				returns += "&" + paramId + "=" + paramValue;
			}
		} else {
			if (StringUtil.isNotEmpty(paramValue)) {
				returns = paramId + "=" + paramValue;
			}
		}
		return returns;
	}

	/**
	 * 拼接url参数字符串
	 */
	public static StringBuffer appendParam(StringBuffer returnStr, String paramId, String paramValue){
		if (paramValue == null) {
			paramValue = "";
		}
		if (returnStr == null) {
			returnStr = new StringBuffer();
			if (!"".equals(paramValue)) {
				returnStr.append(paramId).append("=").append(paramValue);
			}
		}
		else if (!"".equals(paramValue)) {
			returnStr.append("&").append(paramId).append("=").append(paramValue);
		}

		return returnStr;
	}

	/**
	 *  生成24位字符串
	 *  	yyyyMMddhhmmss + 10 random
	 *  	年月日时分秒+10位随机数
	 */
	public static String get24ReandomNum(){

		//年月日时分秒
		String time = sdf.format(new Date());
		//10位随机数
		BigDecimal bd = new BigDecimal(1 + Math.random());
		BigDecimal pow = new BigDecimal(Math.pow(10,9));
		String randomNum = bd.multiply(pow).toPlainString();

		return time + randomNum.substring(0,10);

	}

	/**
	 * 将字符串格式化为指定长度
	 * @param str
	 * @param length
	 * @return
	 */
	public static String formatStr(String str, int length) {
		if(str == null) {
			str = "";
		}
		int strTotalLen = 0;
		for (int i = 0; i < str.length(); i++) {
			if(isChinese(str.charAt(i))) {
				strTotalLen += 2;
			}else {
				strTotalLen += 1;
			}
		}

		int remain = length - strTotalLen;
		if(strTotalLen < length) {
			for (int i = 0; i < remain; i++) {
				str += " ";
			}
		}
		return str;
	}

	/**
	 * 判断字符串是否为中文
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		char[] ch = str.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if(isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符是否为中文
	 * @param c
	 * @return
	 */
	public static  boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public  static String appendString(List<String> strings, String chars)
	{
		String ret_str = "";
		for (int i = 0, count = strings.size(); i < count; i++) {
			String str = strings.get(i);
			if (Strings.isNullOrEmpty(str)) str = "";
			if (i == count - 1) ret_str += str;
			else ret_str += str + ",";
		}
		return ret_str;
	}
}
