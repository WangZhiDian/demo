package test.demo.first;

import java.security.InvalidKeyException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * 数据加密标准，一种使用密钥加密的块算法
 */
@SuppressWarnings("Duplicates")
public class DESUtil {
	private static Logger log = LoggerFactory.getLogger(DESUtil.class);

	/**
	 * 加密
	 */
	public static String encryptECB(String key, String value) {
		try {
			SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] binaryData = cipher.doFinal(value.getBytes("UTF-8"));
			return Base64.encodeBase64String(binaryData);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			log.info("Invalid DES key, not encrypting");
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
			log.info("Error in encryption, not encrypting");
			return null;
		}
	}

	/**
	 * 解密
	 */
	public static String decryptECB(String key, String value) {
		try {
			byte[] binaryValue = Base64.decodeBase64(value);
			SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "DES");
			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] data = cipher.doFinal(binaryValue);
			return new String(data, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
