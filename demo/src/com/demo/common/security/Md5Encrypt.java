package com.demo.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/** 
 * ��׼��MD5���� 
 */
public class Md5Encrypt {
	/**
	 * MD5���ܺ���,������:getMD5Mac
	 * ����:�ַ�����(byte[])
	 * ���:�ַ�����(byte[])
	 * ����:���Ի�õ��ַ��������MD5����,�����,��MAC��Կ
	 * ����ͷ:public byte[] getMD5Mac(byte[] bySourceByte)
	 */
	public static byte[] getMD5Mac(byte[] bySourceByte) {
		byte[] byDisByte;
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(bySourceByte);
			byDisByte = md.digest();
		} catch (NoSuchAlgorithmException n) {
			return (null);
		}
		return (byDisByte);
	}


	/**
	 * @time 2016��5��12�� ����3:05:04 
	 * @param stSourceString
	 * @return
	 */
	public static String getMD5Mac(String stSourceString) {
		String mystring;
		byte getbyte[];
		getbyte = getMD5Mac(stSourceString.getBytes());
		mystring = bintoascii(getbyte);
		return (mystring);
	}

	/**
	 * ����ָ���ı�������ݲ���
	 * 
	 * @param stSourceString
	 * @param decode
	 * @return
	 */
	public static String getMD5Mac(String stSourceString, String decode) {
		String mystring;
		byte getbyte[];
		try {
			getbyte = getMD5Mac(stSourceString.getBytes(decode));

			mystring = bintoascii(getbyte);
		} catch (Exception e) {
			mystring = null;
		}
		return mystring;
	}

	public static String bintoascii(byte[] bySourceByte) {
		int len, i;
		byte tb;
		char high, tmp, low;
		String result = new String();
		len = bySourceByte.length;
		for (i = 0; i < len; i++) {
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

	public static boolean MACCompare(String message, String mac) {
		String mystring;
		mystring = getMD5Mac(message, "utf-8");
		return (mystring.equals(mac));

	}
}