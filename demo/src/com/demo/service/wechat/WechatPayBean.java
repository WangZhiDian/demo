package com.demo.service.wechat;

import lombok.Data;

@Data
public class WechatPayBean
{

	String outTradeNo;
	String subject;
	String totalAmount;
	String body;
	String timeoutExpress;
	String productCode;
	String tradeType;      //JSAPI NATIVE MWEB
	String requestIp;
	long residualTime;
	String openid;
}
