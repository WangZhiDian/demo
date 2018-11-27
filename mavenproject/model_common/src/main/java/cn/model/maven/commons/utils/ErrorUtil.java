package cn.model.maven.commons.utils;

import java.text.MessageFormat;

/**
 * 错误工具类
 * @author itw_zhaoss
 *
 */
public class ErrorUtil {
	/**
	 * 根据错误码获取错误信息
	 * @param errorCode
	 * @return
	 */
	public static String getErrorMessage(String errorCode){
		return PropertyConfigurer.getStringProperty(errorCode);
	}
	
	/**
	 * 根据错误码获取错误信息，同时替换错误信息中的 占位符
	 * @param errorCode
	 * @param args
	 * @return
	 */
	public static String getErrorMessage(String errorCode, Object... args) {
		String errorMessage = PropertyConfigurer.getStringProperty(errorCode);
		return MessageFormat.format(errorMessage, args);
	}
}
