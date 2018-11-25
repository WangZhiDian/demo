package com.demo.common.md5;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * @author admin
 *
 */
public class Md5Encrypt {

	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param text
	 *            明文
	 * 
	 * @return 密文
	 */
	public static String md5(String text) {
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(text.getBytes("GBK"));

		} catch (UnsupportedEncodingException e) {

			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}

	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}
	
    /**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String input_charset) {
    	text = text + key;
        return DigestUtils.md5Hex(getContentBytes(text, input_charset));
    }
    

    /**
     * @param content
     * @param charset
     * @return
     * @throws SignatureException
     * @throws UnsupportedEncodingException 
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }
    
    /*
	 * MD5加密函数,函数名:getMD5Mac输入:字符数组(byte[])输出:字符数组(byte[])
	 * 功能:仅对获得的字符数组进行MD5操作,无填充,无MAC密钥函数头:public byte[] getMD5Mac(byte[]
	 * bySourceByte)
	 */
	public static byte[] getMD5Mac(byte[] bySourceByte)
	{
		byte[] byDisByte;
		MessageDigest md;

		try
		{
			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(bySourceByte);
			byDisByte = md.digest();
		} catch (NoSuchAlgorithmException n)
		{
			return (null);
		}
		return (byDisByte);
	}

	/*
	 * MD5加密函数,函数名:getMD5Mac输入:字符串(String)输出:字符串(String)
	 * 功能:仅对获得的字符串进行MD5操作,无填充,无MAC密钥函数头:public String getMD5Mac(String
	 * stSourceString)
	 */
	public static String getMD5Mac(String stSourceString)
	{
		String mystring;
		byte getbyte[];
		try
		{
			getbyte = getMD5Mac(stSourceString.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e)
		{
			return null;
		}
		mystring = bintoascii(getbyte);
		return (mystring);
	}

	/**
	 * 根据指定的编码对数据操作
	 * 
	 * @param stSourceString
	 * @param decode
	 * @return
	 */
	public static String getMD5Mac(String stSourceString, String decode)
	{
		String mystring;
		byte getbyte[];
		try
		{
			getbyte = getMD5Mac(stSourceString.getBytes(decode));

			mystring = bintoascii(getbyte);
		} catch (Exception e)
		{
			mystring = null;
		}
		return mystring;
	}

	public static String bintoascii(byte[] bySourceByte)
	{
		int len, i;
		byte tb;
		char high, tmp, low;
		String result = new String();
		len = bySourceByte.length;
		for (i = 0; i < len; i++)
		{
			tb = bySourceByte[i];

			tmp = (char) ((tb >>> 4) & 0x000f);
			if (tmp >= 10)
				high = (char) ('a' + tmp - 10);
			else
				high = (char) ('0' + tmp);
			result += high;
			tmp = (char) (tb & 0x000f);
			if (tmp >= 10)
				low = (char) ('a' + tmp - 10);
			else
				low = (char) ('0' + tmp);

			result += low;

		}
		return result;
	}

	public static boolean MACCompare(String message, String mac)
	{
		String mystring;
		mystring = getMD5Mac(message);
		return (mystring.equals(mac));

	}
}
