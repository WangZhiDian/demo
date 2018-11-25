package com.demo.service.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeWapPayResponse;
import com.demo.common.util.Function.StringUtil;

@Component("aliPayChannelImpl")
public class AliPayServiceImpl {

	/**
	 * 下单
	 */
	public String getPayUrl(AliPayBean bean)
	{
		// SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签     
	    //调用RSA签名方式
		System.out.println("sign type:" + AlipayConfig.RSA_PRIVATE_KEY);
		System.out.println("appid:" + AlipayConfig.APPID);
		System.out.println("private_key:" + AlipayConfig.RSA_PRIVATE_KEY);
		System.out.println("public_key:"  + AlipayConfig.ALIPAY_PUBLIC_KEY);
		
		String timeoutExpress = "5m";
		String productCode = "FAST_INSTANT_TRADE_PAY";

	    // form表单生产
	    String form = "";
	    
		String tradeType = bean.tradeType;
		if("PCPAY".equals(tradeType))
		{
			AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

			JSONObject param = new JSONObject();
			param.put("out_trade_no", bean.outTradeNo);
			param.put("total_amount", bean.totalAmount);
			param.put("subject", bean.subject);
			param.put("body", bean.body);
			param.put("timeout_express", timeoutExpress);
//			param.put("time_expire", time_expire);
			param.put("product_code", "FAST_INSTANT_TRADE_PAY");

			alipayRequest.setBizContent(param.toJSONString());
			// 设置异步通知地址
			alipayRequest.setNotifyUrl(AlipayConfig.notify_url);
		    // 设置同步地址
			alipayRequest.setReturnUrl(AlipayConfig.return_url);

			try {
				form = client.pageExecute(alipayRequest).getBody();
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}else if("WAPPAY".equals(tradeType))
		{
			AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		    AlipayTradeWapPayRequest alipay_request=new AlipayTradeWapPayRequest();
			
		    // 封装请求支付信息
		    AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
		    model.setOutTradeNo(bean.outTradeNo);
		    model.setSubject(bean.subject);
		    model.setTotalAmount(bean.totalAmount);
		    model.setBody(bean.body);
		    model.setTimeoutExpress(timeoutExpress);
		    model.setProductCode(productCode);
		    alipay_request.setBizModel(model);
		    // 设置异步通知地址
		    alipay_request.setNotifyUrl(AlipayConfig.notify_url);
		    // 设置同步地址
		    alipay_request.setReturnUrl(AlipayConfig.return_url);
		    
			try {
				// 调用SDK生成表单
				AlipayTradeWapPayResponse response = client.pageExecute(alipay_request);
				form = response.getBody();
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}else if("QRCODE".equals(tradeType))
		{
			AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
			AlipayTradePrecreateRequest precreateRequest = new AlipayTradePrecreateRequest();

			// 目前只考虑必须需要传入的参数
			AlipayTradePrecreateModel model  = new AlipayTradePrecreateModel();
			model.setBody(bean.body);
			model.setSubject(bean.subject);
			model.setOutTradeNo(bean.outTradeNo);
			// 失效时间 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
			model.setTimeoutExpress(timeoutExpress);
			// 订单金额订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] 如果同时传入了【打折金额】，【不可打折金额】，【订单总金额】三者，则必须满足如下条件：【订单总金额】=【打折金额】+【不可打折金额】
			model.setTotalAmount(bean.totalAmount);

			precreateRequest.setBizModel(model);
			precreateRequest.setNotifyUrl(AlipayConfig.notify_url);
			precreateRequest.setReturnUrl(AlipayConfig.return_url);

			try {
				// 调用SDK生成表单
				AlipayTradePrecreateResponse response = client.execute(precreateRequest);
				if(response.isSuccess()){
					form = response.getBody();
					System.out.println("QRCODE 调用成功 precreate:" + form);
				} else {
					System.out.println("调用失败");
				}
			} catch (AlipayApiException e) {
				e.printStackTrace();
			}
		}else
		{
			return "";
		}
		
		System.out.println("form:" + form);
	    return form;
	}
	
	/**
	 * 查询订单状态报文
	 */
	public String queryOrderOriginal(AliPayBean bean){
		
		System.out.println("sign type:" + AlipayConfig.RSA_PRIVATE_KEY);
		System.out.println("appid:" + AlipayConfig.APPID);
		System.out.println("private_key:" + AlipayConfig.RSA_PRIVATE_KEY);
		System.out.println("public_key:" + AlipayConfig.ALIPAY_PUBLIC_KEY);
		
		//获得初始化的AlipayClient
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
		
		//商户订单号，商户网站订单系统中唯一订单号
		String out_trade_no = bean.outTradeNo;
		
		AlipayTradeQueryModel model = new AlipayTradeQueryModel();
		model.setOutTradeNo(out_trade_no);
		alipayRequest.setBizModel(model);
		
		String result  = "";
		//请求
		try {
			result = client.execute(alipayRequest).getBody();
			System.out.println("aliQuery result");
		} catch (AlipayApiException e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 支付回调
	 */
	public String payCallback(String payResultStr)
	{
//		payResultStr = "gmt_create=2017-12-26+15%3A13%3A01&charset=UTF-8&gmt_payment=2017-12-26+15%3A13%3A07&notify_time=2017-12-26+15%3A13%3A08&subject=%E6%B3%B0%E5%BA%B7%E5%9C%A8%E7%BA%BF%E6%94%AF%E4%BB%98%E5%AE%9D%E6%B5%8B%E8%AF%95&sign=WY3ihoEmzzAZICFsUw3pDfGYPGZydfmy8j%2FSRWLv7yt%2F3WzdVPgPK3XDYwjGzCYMb1iDnKGHyEhzJQRgFGW6R%2FSJ2YAsPQ4yGikLUFW0HF1hmyg1DJMR8NVBuOmlNJi3rtbEtsUWng8ayc01YoAdfos1Sla4GRbwLr07PoceoCFyDUWnzaI95PSrmrwjXf1Puz3ewJOkEQjr%2F8bhoHS45oL8r5NtFdW9Izemcs4mybQHQcbjQqEk0os6%2BjzJZ7j7OUpvdE8Qg9rGo0UtBy2ZUShlV27ggw%2Fq7PJUJ%2B4cdXOSGqY3n8eJn8JELnFNcyjBnT89kch2n5xt%2FmhMK7ffyQ%3D%3D&buyer_id=2088802797139208&body=N&invoice_amount=0.01&version=1.0&notify_id=7f0a5072a06f7d5b162808a75656478hjm&fund_bill_list=%5B%7B%22amount%22%3A%220.01%22%2C%22fundChannel%22%3A%22ALIPAYACCOUNT%22%7D%5D&notify_type=trade_status_sync&out_trade_no=100000555&total_amount=0.01&trade_status=TRADE_SUCCESS&trade_no=2017122621001004200276516618&auth_app_id=2017101709352036&receipt_amount=0.01&point_amount=0.00&app_id=2017101709352036&buyer_pay_amount=0.01&sign_type=RSA2&seller_id=2088821471538834";
		
		System.out.println("public_key:" + AlipayConfig.ALIPAY_PUBLIC_KEY);
		System.out.println("AliPayCallBackServiceImpl params:" + payResultStr);
		Map<String, String> params = getParamMap(payResultStr);

		String ALIPAY_PUBLIC_KEY = AlipayConfig.ALIPAY_PUBLIC_KEY;
		boolean verify_result = false;
		try {
			verify_result = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		
		if(verify_result)
		{
			System.out.println("verify_success..");
		}
		
		//固定返回success，alipay才不会反复回调业务系统通知
		return "success";
	}
	
	/**
	 * 关订单
	 */
	public String clossOrder(AliPayBean bean)
	{
		String closeInfo = "FAIL";
		String out_trade_no = bean.getOutTradeNo();
		AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET, AlipayConfig.ALIPAY_PUBLIC_KEY,AlipayConfig.SIGNTYPE);
		AlipayTradeCloseRequest alipay_request = new AlipayTradeCloseRequest();
		AlipayTradeCloseModel model =new AlipayTradeCloseModel();
		model.setOutTradeNo(out_trade_no);
		alipay_request.setBizModel(model);

		AlipayTradeCloseResponse alipay_response;
		String code = "", sub_code = "";
		try {
			alipay_response = client.execute(alipay_request);
			System.out.println("alipay closetrade body:" + alipay_response);
			code = alipay_response.getCode();
			sub_code = alipay_response.getSubCode();
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		System.out.println("alipay closetrade code:" + code);
		if("10000".equals(code))
		{
			closeInfo = "SUCCESS";
		}else if("40004".equals(code))
		{//支付宝二维码有可能打开后并没有扫码，这时候支付宝该单状态为订单不存在。但这时候客户是可以重新下单的。
			if("ACQ.TRADE_NOT_EXIST".equals(sub_code))
				closeInfo = "SUCCESS";
		}
		return closeInfo;
	}
	
	/**
	 * 解析时间
	 */
	private Map<String, String> getParamMap(String responseStr)
	{
		Map<String, String> params = new HashMap<String, String>();
		SortedMap<String,String> sortedMap = new TreeMap<String, String>();
		String key;
		String value;
		String[] pair;
		String[] responseArr = responseStr.split("&");
		try
		{
			for(int i = 0; i < responseArr.length; i++)
			{
				pair = responseArr[i].split("=");
				key = pair[0];
				value = pair[1];
				value = URLDecoder.decode(value, "UTF-8");

				if(StringUtil.isNotEmpty(value))
				{
					params.put(key, value);
					sortedMap.put(key, value);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(params.toString());
		System.out.println(sortedMap.toString());
		return sortedMap;
	}
	
	public String getInnerTradeState(String trade_state)
	{
		//微信通道状态归类：支付成功（TRADE_SUCCESS：交易支付成功，TRADE_FINISHED：交易结束，不可退款）、等待支付（WAIT_BUYER_PAY:交易创建，等待买家付款）、支付关闭（TRADE_CLOSED：未付款交易超时关闭，或支付完成后全额退款）
		String innerState = "0";
//		switch(trade_state)
//		{
//			case "TRADE_SUCCESS"	: innerState = PayStatusEnum.SUCCESS.getCode(); 	break;
//			case "TRADE_FINISHED"   : innerState = PayStatusEnum.SUCCESS.getCode(); 	break;
//			case "WAIT_BUYER_PAY" 	: innerState = PayStatusEnum.WAIT.getCode(); 		break;
//			case "TRADE_CLOSED" 	: innerState = PayStatusEnum.CLOSE.getCode(); 		break;
//			default			  		: innerState = PayStatusEnum.FAIL.getCode(); 		break;
//		}
		return innerState;
	}
	
	
}
