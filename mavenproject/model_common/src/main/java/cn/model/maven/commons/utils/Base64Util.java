package cn.model.maven.commons.utils;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {
	
	private Base64Util() {
    }

	/**
	 * Base64解码
	 * @param base64
	 * @return
	 */
    public static byte[] decode(String base64) {
        return Base64.decodeBase64(base64);
    }

    /**
     * Base64编码
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }
    
    /**
     * 判断字符串是否为Base64编码
     * @param test
     * @return
     */
    public static boolean isBase64(String test){
        byte[] decode = decode(test);
        String s = encode(decode);
        return  s.equals(test);
    }
}
