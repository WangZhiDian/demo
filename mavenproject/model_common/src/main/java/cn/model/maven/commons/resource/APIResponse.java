package cn.model.maven.commons.resource;

import cn.model.maven.commons.constant.Constants;
import cn.model.maven.commons.data.MD5SignAndValidate;
import cn.model.maven.commons.utils.PropertyConfigurer;
import cn.model.maven.commons.utils.StringUtil;
import cn.model.maven.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import jersey.repackaged.com.google.common.collect.Sets;

import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * 定义调用接口返回的数据格式
 * { 
 * 		"ret_code": "错误代码", 
 * 		"ret_msg": "返回信息", 
 * 		"result": {返回数据} 
 * }
 * 错误代码和错误描述 定义在
 * 然后通过HeraExceptionMapper统一处理所有的异常，并且通过APIResponse返回给前端。
 * 
 * @author wanghl80
 *
 */
public class APIResponse implements Serializable {
	
	private static final long serialVersionUID = -3272004536045592039L;

	private JSONObject jsonResult = new JSONObject();
	
	private static final String CODE = "ret_code";//返回码
	private static final String MESSAGE = "ret_msg";//返回信息
	private static final String DATA = "result";//返回数据
	
	
	private static final String BUSINESSUSERID = Constants.BUSINESS_USER_ID;//业务系统用户ID
	private static final String SALT = Constants.SALT;//随机字符串
	private static final String SIGNATURE = Constants.SIGNATURE;//签名
	private static final String TENANT_ID = Constants.TENANT_ID;//租户ID
	
	private static final String REQTIME = "req_time";//请求时间

	public  static final String ENCRYPT_KEY = "encryptKey";//必传
	

	private static final String REFER_URL = "refer_url";
	private static final String CLIENT_IP = "client_ip";
	
	public APIResponse() {
		setCode(Constants.INVOKE_OK);
		setMessage(PropertyConfigurer.getStringProperty(Constants.INVOKE_OK));
	}
	
	public APIResponse(Object data) {
		this();
		setData(data);
	}
	
	/*public APIResponse(BaseRequestDTO requestDTO) {
		this();
		setbusinessUserId(requestDTO.getMemberId());
		setTenantId(requestDTO.getTenantId());
		setSalt(requestDTO.getSalt());
		setReqtime(requestDTO.getRequestTime());

	}

	public APIResponse(BaseRequestDTO requestDTO,String encryptKey) {
		this();
		setbusinessUserId(requestDTO.getMemberId());
		setTenantId(requestDTO.getTenantId());
		setSalt(requestDTO.getSalt());
		setReqtime(requestDTO.getRequestTime());
		setEncryptKey(encryptKey);
	}*/

	public boolean success() {
		return getCode().equals(Constants.INVOKE_OK);
	}

	public Object getData() {
		return jsonResult.get(DATA);
	}

	public void setData(Object data) {
		jsonResult.put(DATA, data);
	}
	
	public void setClientIp(String clientIp) {
		jsonResult.put(CLIENT_IP, clientIp);
	}

	public String getCode() {
		return jsonResult.getString(CODE);
	}

	public void setCode(Object code) {
		jsonResult.put(CODE, code);
	}

	public String getMessage() {
		return jsonResult.getString(MESSAGE);
	}

	public void setMessage(String message) {
		jsonResult.put(MESSAGE, message);
	}

	/**
	 * 获取业务系统用户Id
	 * @return
	 */
	public String getBusinessUserId() {
		return jsonResult.getString(BUSINESSUSERID);
	}
	
	/**
	 * 设置业务系统用户Id
	 * @param businessUserId
	 * @return
	 */
	public void setbusinessUserId(String businessUserId) {
		jsonResult.put(BUSINESSUSERID, businessUserId);
	}

	public String getTenantId() {
		return jsonResult.getString(TENANT_ID);
	}
	
	public void setTenantId(String tenant_id) {
		jsonResult.put(TENANT_ID, tenant_id);
	}

	public String getEncryptKey() {
		return jsonResult.getString(ENCRYPT_KEY);
	}
	
	public void setEncryptKey(String encryptKey) {
		jsonResult.put(ENCRYPT_KEY, encryptKey);
	}

	public void removeEncrptKey(){
		jsonResult.remove(ENCRYPT_KEY);
	}

	public String getSalt() {
		return jsonResult.getString(SALT);
	}
	
	public void setSalt(String salt) {
		jsonResult.put(SALT, salt);
	}

	public String getReqtime() {
		return jsonResult.getString(REQTIME);
	}
	
	public void setReqtime(String reqtime) {
		jsonResult.put(REQTIME, reqtime);
	}

	public String getSignature() {
		return jsonResult.getString(SIGNATURE);
	}
	
	public void setSignature(String signature) {
		jsonResult.put(SIGNATURE, signature);
	}

	public String getReferUrl()
	{
		return jsonResult.getString(REFER_URL);
	}

	public void setReferUrl(String referUrl) {
		jsonResult.put(REFER_URL, referUrl);
	}
	
	@Override
	public String toString() {
		return jsonResult.toJSONString();
	}
	
	/*
	public APIResponse setExceptionCode(ExceptionCode code) {
		setCode(code.getErrorCode());
		setMessage(code.getErrorMessage());
		setData(code.getData());
		return this;
	}
	
	
	public String toJSONString() {
		//removeEncrptKey();
		return jsonResult.toJSONString();
	}
	
	public String toJSONString(SimplePropertyPreFilter filter) {
		return JSON.toJSONString(jsonResult,filter);
	}
	*/
	
	/**
	 * @time 2016年4月5日 下午4:02:48 
	 * @param args 要显示的值
	 * @return
	 */
	public String toJSONString(String ...args) {
		SimplePropertyPreFilter filter =new SimplePropertyPreFilter();
		Set<String> includes = filter.getIncludes();
		Set<String> set = Sets.newHashSet(args);
		set.add(CODE);
		set.add(MESSAGE);
		set.add(DATA);
		set.add(BUSINESSUSERID);
		set.add(SALT);
		set.add(SIGNATURE);
		set.add(TENANT_ID);
		includes.addAll(set);
		return JSON.toJSONString(jsonResult,filter);
	}
	
	/**
	 * 获取未经过加签的响应对象
	 * @return
	 */
	public Response getResponse() {
		return Response.status(Response.Status.OK).entity(this.toString()).build();
	}

	/**
	 * 获取经过加签后的响应对象
	 * @param encryptKey
	 * 			MD5密钥
	 * @return
	 * 			加签后的响应对象
	 */
	public Response getResponse(String encryptKey) {
		return Response.status(Response.Status.OK).entity(packResponse(encryptKey)).build();
	}

	/**
	 * 对响应数据加签
	 * @param encryptKey
	 * 			MD5密钥
	 * @return
	 * 			签名字符串
	 */
    private String packResponse(String encryptKey) {
        Map<String, String> paramMap = JSON.parseObject(this.toJSONString(), new TypeReference<Map<String, String>>() {});

        if (!StringUtil.isEmpty(encryptKey)) {
            paramMap = MD5SignAndValidate.signData(paramMap, encryptKey);
        } else {
            throw new BusinessException("ERROR00000");
        }
        return JSON.toJSONString(paramMap);
    }

}
