package com.demo.service.wechat;

public class WechatPayConfig
{
	// 公众号appid
	static String APPID = "wxcd7143c00e5bb6f7";
	//商户号
	static String mercherId = "1490062962";
	//验证key
	static String parternerKey = "dXOcckjH2xZMaWbgLsgZsmVrjUNrJ5GB";
	//统一下单
	static String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//下载对账单
	static String downloadbillUrl = "https://api.mch.weixin.qq.com/pay/downloadbill";
	//单笔查询订单
	static String orderqueryUrl   = "https://api.mch.weixin.qq.com/pay/orderquery";
	//关闭订单
	static String closeorderUrl   = "https://api.mch.weixin.qq.com/pay/closeorder";
	//公众号支付，前端解析调用地址
	static String requestSendUrl  = "http://ecuat.tk.cn/service/heraPayment/wechatPay.html";
	
	//支付成功回调地址
	static String notifyUrl       = "http://ecuat.tk.cn/demo/v1/callBack/pay";

	static String return_url      = "http://ecuat.tk.cn/demo/v1/payFrontCallback";

	static String h5redirectUrl   = "http://ecuat.tk.cn/service/heraPayment/wechatPayTest.html";
}
