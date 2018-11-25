package com.demo.service.wapBind.bean;

public class WapBindInfo
{
	String name;
	
	String identifyNo;
	
	String identifyType;
	
	String phoneNo;
	
	String checkCode;

	String openid;
	//绑定是否成功状态：0  成功；1  失败
	String status = "1";
	//处理逻辑handler
	String handler;
	//是否需要检查验证码
	boolean needCheckCode = true;
	
	String suitId;
	
	public String getSuitId() {
		return suitId;
	}

	public void setSuitId(String suitId) {
		this.suitId = suitId;
	}

	public String getIdentifyType() {
		return identifyType;
	}

	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}

	public boolean isNeedCheckCode() {
		return needCheckCode;
	}

	public void setNeedCheckCode(boolean needCheckCode) {
		this.needCheckCode = needCheckCode;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifyNo() {
		return identifyNo;
	}

	public void setIdentifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}
	
}
