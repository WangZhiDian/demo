package test.demo.first;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DecAndEnc {
	private static byte[] stringToByte(String str){
		str = str.replace("A", "D").replace("B", "D").replace("C", "D").replace("E", "-").replace("F", "-");
		String[] keys = str.split("D");
		byte[] b = new byte[keys.length];
		for(int i=0;i<b.length;i++){
			b[i] = Byte.valueOf(keys[i]);
		}
		return b;
	}
	public static String dec(String content){
		try{
			String sk = "80B"+"78C"+"F116CE"+"111AE74"+"AF98D6BF55"+"BF30AF75"+"D76CF123B"+"6AF37CF"+"123AF40";
			byte[] data =stringToByte(content);
			Key key =  new SecretKeySpec(stringToByte(sk), "A"+"ES");   
			Cipher cipher = Cipher.getInstance("A"+"ES/E"+"CB/P"+"KCS"+"5Pa"+"dding");   
			cipher.init(Cipher.DECRYPT_MODE, key);  
			return new String(cipher.doFinal(data)); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}
}
