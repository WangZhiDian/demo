package cn.model.maven.exception;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一的异常编码，特定异常可以继承该类进行扩展
 * 
 * 整个异常体系为：
 * 	ExceptionCode                    :1XX XXX XXX
 * @author       wanghl80
 * @date         2017年9月6日 上午11:01:38
 * @version      V1.0
 */
public class ExceptionCode implements Serializable {
	private static Logger logger = LoggerFactory.getLogger(ExceptionCode.class);
	private static final long serialVersionUID = -4850687483494058414L;

	private String errorCode;
	private String errorMessage;
	private Object data;
	private String referUrl = "";
	private static Map<String, ExceptionCode> mapKeyToExceptionCode = new HashMap<>();

	protected ExceptionCode() {}
	
	protected ExceptionCode(String key, String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		mapKeyToExceptionCode.put(key, this);
	}

	protected ExceptionCode(String key, String errorCode, String errorMessage, String referUrl) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.referUrl = referUrl;
		mapKeyToExceptionCode.put(key, this);
	}

	public ExceptionCode setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
		return this;
	}

	public ExceptionCode setData(Object data)
	{
		this.data = data;
		return this;
	}

	public ExceptionCode setReferUrl(String referUrl)
	{
		this.referUrl = referUrl;
		return this;
	}

	public static ExceptionCode getExceptionCode(String key)
	{
		return (ExceptionCode) mapKeyToExceptionCode.get(key);
	}

	public JSONObject toJSONObject()
	{
		JSONObject object = new JSONObject();
		object.put("error_code", errorCode);
		object.put("error_message", errorMessage);
		if (referUrl != null)
			object.put("refer_url", referUrl);
		if (data != null)
		{
			object.put("data", data);
		}
		return object;

	}

	@Override
	public String toString()
	{
		if (data==null)
		{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"error_code\":\"");
			sb.append(this.errorCode);
			sb.append("\",\"error_message\":\"");
			sb.append(this.errorMessage);
			sb.append("\"}");
			return sb.toString();
		}
		else {
			return toJSONObject().toJSONString();
		}
	}

	public String toString(Object... args)
	{
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("{\"error_code\":\"");
			sb.append(this.errorCode);
			sb.append("\",\"error_message\":\"");
			sb.append(MessageFormat.format(this.errorMessage, args));
			sb.append("\"}");
			return sb.toString();
		}catch(Exception e){
			logger.error("格式化异常错误: " + errorMessage, e);
			return this.toString();
		}
	}

	/**
	 * 获取最终的错误描述 不替换/格式错误描述中的 {0}
	 * 
	 * @return
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	public Object getData()
	{
		return data;
	}

	/**
	 * 获取最终的错误描述 替换/格式错误描述中的 {0}
	 * 
	 * @return
	 */
	public String getErrorMessage(Object... codeArgs)
	{
		try
		{
			return MessageFormat.format(this.errorMessage, codeArgs);
		} catch (Exception ex)
		{
			logger.error("格式化异常错误: " + errorMessage, ex);
			return errorMessage;
		}
	}

	/**
	 * 获取最终的错误编码
	 * 
	 * @return
	 */
	public String getErrorCode()
	{
		return errorCode;
	}

	/**
	 * 获取帮助或者跳转链接
	 * 
	 * @return
	 */
	public String getReferUrl()
	{
		return referUrl;

	}
	
	
	/**
	 * 成功
	 */
	public static final ExceptionCode SUCCESS = new ExceptionCode(
			"SUCCESS", "0", "接口成功执行");
	/**
	 * 未定义的异常
	 */
	public static final ExceptionCode SYSTEM_UNDEFINEDE_EXECPTION = new ExceptionCode(
			"SYSTEM_UNDEFINEDE_EXECPTION", "100001999", "未定义的异常");

	/**
	 * 服务停止
	 */
	public static final ExceptionCode SYSTEM_SERVICE_UNAVAILABLE = new ExceptionCode(
			"SYSTEM_SERVICE_UNAVAILABLE", "100001001", "服务停止");

	/**
	 * 非法请求:{0}
	 */
	public static final ExceptionCode SYSTEM_ILLEGAL_REQUEST = new ExceptionCode(
			"SYSTEM_ILLEGAL_REQUEST", "100001002", "非法请求:{0}");
	/**
	 * 非法请求
	 */
	public static final ExceptionCode SYSTEM_ILLEGAL_REQUEST_B = new ExceptionCode(
			"SYSTEM_ILLEGAL_REQUEST_B", "100001002", "非法请求");

	/**
	 * 参数值非法，需为 ({0})，实际为 ({1})，请参考API文档
	 */
	public static final ExceptionCode SYSTEM_INVALID_PARAM = new ExceptionCode(
			"SYSTEM_INVALID_PARAM", "100001003",
			"参数值非法，需为 ({0})，实际为 ({1})，请参考API文档");

	/**
	 * 不支持的MediaType ({0})
	 */
	public static final ExceptionCode SYSTEM_UNSUPPORT_MEDIATYPE = new ExceptionCode(
			"SYSTEM_UNSUPPORT_MEDIATYPE", "100001004", "不支持的MediaType ({0})");

	/**
	 * 参数为空:{0}
	 */
	public static final ExceptionCode SYSTEM_EMPTY_PARAM = new ExceptionCode(
			"SYSTEM_EMPTY_PARAM", "100001005", "参数为空:{0}");
	/**
	 * 参数为空
	 */
	public static final ExceptionCode SYSTEM_EMPTY_PARAM_B = new ExceptionCode(
			"SYSTEM_EMPTY_PARAM_B", "100001005", "参数为空");

	/**
	 * JSON数据有误:期望{0}为{1},实际为{2}
	 */
	public static final ExceptionCode SYSTEM_INVALID_JSON_DATA = new ExceptionCode(
			"SYSTEM_INVALID_JSON_DATA", "100001006",
			"JSON数据有误:期望{0}为{1},实际为{2}");
	
	/**
	 * JSON数据有误
	 */
	public static final ExceptionCode SYSTEM_INVALID_JSON_DATA_B = new ExceptionCode(
			"SYSTEM_INVALID_JSON_DATA_B", "100001006","JSON数据有误");
	
	/**
	 * {0}的缓存类型没有BusinessType在里面定义，请定义后再使用
	 */
	public static final ExceptionCode SYSTEM_INVALID_BUSINESS_TYPE = new ExceptionCode(
			"SYSTEM_INVALID_BUSINESS_TYPE", "100001007",
			"{0}的缓存类型没有BusinessType在里面定义，请定义后再使用");
	
	/**
	 * {0}的缓存类型没有cacheType不支持
	 */
	public static final ExceptionCode SYSTEM_INVALID_CACHE_TYPE = new ExceptionCode(
			"SYSTEM_INVALID_CACHE_TYPE", "100001008",
			"{0}的缓存类型没有cacheType不支持");
	/**
	 * 接口请求失败
	 */
	public static final ExceptionCode SYSTEM_INTERFACE_FAIL = new ExceptionCode(
			"SYSTEM_INTERFACE_FAIL", "100001009",
			"接口请求失败");
	/**
	 * 接口请求失败：{0}
	 */
	public static final ExceptionCode SYSTEM_INTERFACE_FAIL_B = new ExceptionCode(
			"SYSTEM_INTERFACE_FAIL_B", "100001009",
			"接口请求失败：{0}");
	/**
	 * 查询失败
	 */
	public static final ExceptionCode SYSTEM_FIND_FAIL = new ExceptionCode(
			"SYSTEM_FIND_FAIL", "100001010",
			"查询失败");
	/**
	 * 查询失败：{0}
	 */
	public static final ExceptionCode SYSTEM_FIND_FAIL_B = new ExceptionCode(
			"SYSTEM_FIND_FAIL_B", "100001010",
			"查询失败：{0}");
}
