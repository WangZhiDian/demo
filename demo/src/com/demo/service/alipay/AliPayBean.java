package com.demo.service.alipay;

import lombok.Data;

@Data
public class AliPayBean {

	String outTradeNo;
	String subject;
	String totalAmount;
	String body;
	String timeoutExpress;
	String productCode;
	String tradeType;      //PC WAP
    
}
