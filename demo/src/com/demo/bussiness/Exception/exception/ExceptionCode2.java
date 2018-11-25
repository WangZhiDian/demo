package com.demo.bussiness.Exception.exception;

import java.io.Serializable;

public enum ExceptionCode2 implements Serializable
{
	SUCCESS("SUCCESS"                                                                   , "0"           , "�ӿڳɹ�ִ��")  ,
	SYSTEM_UNDEFINEDE_EXECPTION("SYSTEM_UNDEFINEDE_EXECPTION"                           , "100001999"   , "δ������쳣")  ,
	SYSTEM_SERVICE_UNAVAILABLE("SYSTEM_SERVICE_UNAVAILABLE"                             , "100001001"   , "����ֹͣ")      
	;
	
	private static final long serialVersionUID = -4850687483494058414L;
	
	private String errorCode;
	private String errorMessage;
	private Object data;
	private String referUrl;
	
	private ExceptionCode2(String desc, String errorCode, String errorMessage)
	{
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public ExceptionCode2 setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
		return this ;
	}
	
	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Object getData() {
		return data;
	}

	public String getReferUrl() {
		return referUrl;
	}
}
