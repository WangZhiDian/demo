package com.demo.service.wechat;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.http.Header;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Component;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import lombok.extern.slf4j.Slf4j;

import com.demo.bussiness.httpclient.HttpClientUtil;
import com.demo.bussiness.httpclient.builder.HCB;
import com.demo.bussiness.httpclient.common.HttpConfig;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;

import com.demo.bussiness.httpclient.common.HttpHeader;
import com.demo.bussiness.httpclient.common.HttpProcessException;
import com.demo.bussiness.httpclient.common.SSLs;
import com.demo.common.md5.Md5Encrypt;
import com.demo.common.util.Function.DateUtil;

@Slf4j
@Component("wechatChannelImpl")
public class WechatPayServiceImpl
{
	private final Joiner.MapJoiner mapJoiner = Joiner.on("&").withKeyValueSeparator("=");

	/**下单*/
	public String generateDeeplink(WechatPayBean bean) throws ParserConfigurationException, IOException, SAXException
	{
		String tradeType = bean.getTradeType();
		
		String deeplink = "";
		
		if("JSAPI".equals(tradeType))
		{
			deeplink = getJsapiUrl(bean);
		}else if("NATIVE".equals(tradeType))
		{
			deeplink = getQrCodeUrl(bean);
		}else if("MWEB".equals(tradeType))
		{
			deeplink = getH5MWebUrl(bean);
			deeplink = getWXPayHtml(deeplink, "127.0.0.1");  //获取http wx html
		}else
		{
			return "";
		}
		
		return deeplink;
	}
	
	/**
	 * 解决问题：1 设置访问请求头中的访问ip，处理微信H5方式对ip的校验，解决提示问题：网络环境未能通过安全验证，请稍后再试。
	 *           2 设置Referer头域名，获取微信支付中间页页面数据，调起支付窗，解决提示问题：商家参数格式有误，请联系商家解决。
	 */
	String getWXPayHtml(String wxUrl, String reqIp)
	{
		log.debug("##########-------wxUrl :"+wxUrl + "   | " + reqIp);
		String result = null;
		try{
			Header[] header = HttpHeader.custom().other("Referer", "https://" + "aaa.test.com").other("x-forwarded-for",reqIp).build();
			HttpConfig config = HttpConfig.custom();
			config.headers(header);
			config.url(wxUrl);
			config.client(HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1).timeout(5000).build());
			result =  HttpClientUtil.get(config);
			log.debug("response=-================="+result);
		} catch (HttpProcessException e){
			log.error("wechat sendHttpRequest:" , e);
		}

		return result;
	}
	
	/**
	 * 查询获取状态原始报文
	 */
	public String queryOrderOriginal(WechatPayBean bean) throws ParserConfigurationException, IOException, SAXException
	{
		String response = "";
		String requestXml = getQueryOrderRequestXml(bean);
	    response = sendHttpRequest(requestXml, WechatPayConfig.orderqueryUrl);
		return response;
	}
	
	/**
	 * 支付回调
	 */
	public String payCallBack(String payResultStr) throws ParserConfigurationException, SAXException, IOException {
//		String notifyXmlContent = payResultStr;
		Map<String,String> map = Maps.newHashMap();
		StringReader sr = new StringReader(payResultStr);
		InputSource is = new InputSource(sr);
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder=builderFactory.newDocumentBuilder();
		org.w3c.dom.Document doc = builder.parse(is);
		org.w3c.dom.Element rootElement = doc.getDocumentElement();
		NodeList nodeList = rootElement.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			map.put(nodeList.item(i).getNodeName(),nodeList.item(i).getFirstChild().getNodeValue());
		}
		String partnerKey = WechatPayConfig.parternerKey;
		boolean signOk = verifySign(map, partnerKey);

		if(signOk){
			System.out.println("sign is ok");
		}
		String returnStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
		return returnStr;
	}
	
	/**
	 * 关单
	 */
	public String closeOrder(WechatPayBean bean) throws ParserConfigurationException, IOException, SAXException, DocumentException
	{
		String closeInfo = "FAIL";
		String requestXml = getQueryOrderRequestXml(bean);
		String response = sendHttpRequest(requestXml, WechatPayConfig.closeorderUrl);
		Document doc =  (Document) DocumentHelper.parseText(response);
		Element responseHeader = ((org.dom4j.Document) doc).getRootElement();// 根节点
		//微信通道关单归类：关闭成功（ORDERCLOSED：订单已关闭）、关闭失败(ORDERPAID：订单已支付、SYSTEMERROR：系统错误、SIGNERROR：签名错误
		//REQUIRE_POST_METHOD：请使用post方法、XML_FORMAT_ERROR：XML格式错误)
		if ("SUCCESS".equals(responseHeader.elementText("return_code")))
		{
			String result_code = responseHeader.elementText("result_code");
			if("SUCCESS".equals(result_code))
			{
				closeInfo = "SUCCESS";
			}else
			{
				String err_code = responseHeader.elementText("err_code");
				if("ORDERCLOSED".equals(err_code))
					closeInfo = "SUCCESS";
			}
		}else {
			//日志记录
			log.debug("FAIL");
			closeInfo = "FAIL";
		}
		return closeInfo;
	}
	
	private String getH5MWebUrl(WechatPayBean bean) throws ParserConfigurationException, IOException, SAXException
	{
	      String requestXml = getH5RequestXml(bean);
	      String responseStr = sendHttpRequest(requestXml, WechatPayConfig.unifiedorderUrl);
		  String url = "";
	      if (responseStr!=null)
	      {
	          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder builder=factory.newDocumentBuilder();
	          StringReader sr = new StringReader(responseStr);
	          InputSource is = new InputSource(sr);
	          Document doc = builder.parse(is);
	          String return_code = doc.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			  
	          if("SUCCESS".equals(return_code))
	          {
	        	  String result_code =	doc.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
	        	  if("SUCCESS".equals(result_code))
	        	  {
	        		  NodeList prePayNodeList = doc.getElementsByTagName("mweb_url");
			          if (prePayNodeList.getLength()>=1)
			          {
			          	  url = prePayNodeList.item(0).getFirstChild().getNodeValue();
			          }else
			          {
			          	  String returnMsg = doc.getElementsByTagName("err_code_des").item(0).getFirstChild().getNodeValue();
			          	  System.out.println(returnMsg);
			          	  return null;
			          }
	        	  }else
	        	  {
	        		  String returnMsg = doc.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
	        		  System.out.println(returnMsg);
	        		  return null;
	        	  }
	          }else
	          {
	          	  String returnMsg = doc.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
	          	  System.out.println(returnMsg);
	          	  return null;
	          }
	
	      }
	    return url;
	}
	
	private String getQrCodeUrl(WechatPayBean bean) throws ParserConfigurationException, SAXException, IOException
	{
		  String requestXml = getQrCodeRequestXml(bean);
		  String responseStr = sendHttpRequest(requestXml, WechatPayConfig.unifiedorderUrl);
		  
		  String url = "";
	      if (responseStr!=null)
	      {
	          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder builder=factory.newDocumentBuilder();
	          StringReader sr = new StringReader(responseStr);
	          InputSource is = new InputSource(sr);
	          Document doc = builder.parse(is);
	          String return_code = doc.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			  
	          if("SUCCESS".equals(return_code))
	          {
	        	  String result_code =	doc.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
	        	  if("SUCCESS".equals(result_code))
	        	  {
	        		  NodeList prePayNodeList = doc.getElementsByTagName("code_url");
			          if (prePayNodeList.getLength()>=1)
			          {
			          	  url = prePayNodeList.item(0).getFirstChild().getNodeValue();
			          }else
			          {
			          	  String returnMsg = doc.getElementsByTagName("err_code_des").item(0).getFirstChild().getNodeValue();
			          	  System.out.println(returnMsg);
			          	  return null;
			          }
	        	  }else
	        	  {
	        		  String returnMsg = doc.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
	        		  System.out.println(returnMsg);
	        		  return null;
	        	  }
	          }else
	          {
	          	  String returnMsg = doc.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
	          	  System.out.println(returnMsg);
	          	  return null;
	          }

	      }
		return url;
	}
	
	private String getJsapiUrl(WechatPayBean bean) throws ParserConfigurationException, SAXException, IOException
	{
		String prepayId = getPrepayId(bean);
		String timeStamp = System.currentTimeMillis()/1000 + "";
		String nonceStr = generateNoceStr();

		Map<String,String> param = Maps.newHashMap();
		param.put("appId", WechatPayConfig.APPID);
		param.put("timeStamp", timeStamp); //时间戳到秒
		param.put("nonceStr", nonceStr);
		param.put("package", "prepay_id=" + prepayId);
		param.put("signType", "MD5");
		String sign = sign(param);
		param.put("paySign", sign);

		log.info("---param:"+param.toString());

		String url = WechatPayConfig.requestSendUrl+"?appId="+WechatPayConfig.APPID
				+"&timeStamp="+timeStamp+"&nonceStr="+nonceStr+"&prepayId="+prepayId+"&paySign="+sign;
		log.info("---wechat pay:"+url);
		return url;
	}
	
	private String getQrCodeRequestXml(WechatPayBean bean) throws UnsupportedEncodingException
	{
		  String appId = WechatPayConfig.APPID;
		  String tradeType = "NATIVE";
		  String merchanId = WechatPayConfig.mercherId;
		  String attach = "N";
		  String body = "泰康在线微信支付";
		  String totalFee = "1";
		  
		  String noceStr = generateNoceStr();
		  String notifyUrl = WechatPayConfig.notifyUrl + "/";
		  String tradeBillNo = bean.getOutTradeNo();
		  String spbillCreateIp = "127.0.0.1";
		  String timeExpired = DateUtil.residualDate((int)bean.getResidualTime(), DateUtil.YYYYMMDDHHMMSS);
		  
	      Map<String,String> map = Maps.newHashMap();
	      map.put("appid",appId);
	      map.put("attach",attach);
	      map.put("body",body);
	      map.put("mch_id",merchanId);
		  map.put("time_expire",timeExpired);
	      map.put("nonce_str",noceStr);
	      map.put("notify_url",notifyUrl);
	      map.put("out_trade_no",tradeBillNo);
	      map.put("spbill_create_ip",spbillCreateIp);
	      map.put("total_fee",totalFee);
	      map.put("trade_type",tradeType);
	      
	      String sign = sign(map);
	      
	      String xmlMsg = "<xml><appid>" + appId + "</appid><attach>" + attach + "</attach><body>" +
				  body + "</body><mch_id>" + merchanId +"</mch_id><time_expire>"+timeExpired+"</time_expire>"+
	              "<nonce_str>" + noceStr + "</nonce_str><notify_url>" + notifyUrl +
	              "</notify_url><out_trade_no>" + tradeBillNo + "" +
	              "</out_trade_no>" + "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip><total_fee>" +
	              totalFee + "</total_fee><trade_type>"+tradeType+"</trade_type>" +
	              "<sign>" + sign + "</sign></xml>";
	      xmlMsg = new String(xmlMsg.getBytes("utf-8"),Charset.forName("utf-8"));
	      log.info("xmlMsg=="+xmlMsg);
		  return xmlMsg;
	}
	
	private String getH5RequestXml(WechatPayBean bean) throws UnsupportedEncodingException
	{
		  String appId = WechatPayConfig.APPID;
		  String tradeType = "MWEB";
		  String merchanId = WechatPayConfig.mercherId;
		  String attach = "N";
		  String body = "泰康在线微信支付";
		  String totalFee = "1";
		  String noceStr = generateNoceStr();
		  String notifyUrl = WechatPayConfig.notifyUrl + "1111";
		  String tradeBillNo = bean.getOutTradeNo();
		  String spbillCreateIp = bean.requestIp;
		  String timeExpired = DateUtil.residualDate((int)bean.getResidualTime(), DateUtil.YYYYMMDDHHMMSS);
		  String sceneInfo = "{\"h5_info\": {\"type\":\"Wap\",\"wap_name\": \"测试支付\",\"wap_url\": \"http://ecuat.tk.cn\"}}";
		  
	      Map<String,String> map = Maps.newHashMap();
	      map.put("appid",appId);
	      map.put("attach",attach);
	      map.put("body",body);
	      map.put("mch_id",merchanId);
		  map.put("time_expire",timeExpired);
	      map.put("nonce_str",noceStr);
	      map.put("notify_url",notifyUrl);
	      map.put("out_trade_no",tradeBillNo);
	      map.put("spbill_create_ip",spbillCreateIp);
	      map.put("total_fee",totalFee);
	      map.put("trade_type",tradeType);
	      map.put("scene_info", sceneInfo);
	      
	      String sign = sign(map);
	      
	      String xmlMsg = "<xml><appid>" + appId + "</appid><attach>" + attach + "</attach><body>" +
				  body + "</body><mch_id>" + merchanId +"</mch_id><time_expire>"+timeExpired+"</time_expire>"+
	              "<nonce_str>" + noceStr + "</nonce_str><notify_url>" + notifyUrl +
	              "</notify_url><out_trade_no>" + tradeBillNo + "" +
	              "</out_trade_no>" + "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip><total_fee>" +
	              totalFee + "</total_fee><trade_type>"+tradeType+"</trade_type><scene_info>" + sceneInfo +
	              "</scene_info><sign>" + sign + "</sign></xml>";
	      xmlMsg = new String(xmlMsg.getBytes("utf-8"));
	      log.info("xmlMsg=="+xmlMsg);
		  return xmlMsg;
	}
	
	private String getPrepayId(WechatPayBean bean) throws ParserConfigurationException, IOException, SAXException
	{
		  String requestXml = getPrepayIdRequestXml(bean);
		  String responseStr = sendHttpRequest(requestXml, WechatPayConfig.unifiedorderUrl);
		  
		  String prepayId = "";
	      if (responseStr!=null)
	      {
	    	  DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder builder=factory.newDocumentBuilder();
	          StringReader sr = new StringReader(responseStr);
	          InputSource is = new InputSource(sr);
	          Document doc = builder.parse(is);
	          //先获取返回return_code，再处理
			  NodeList prePayNodeList = doc.getElementsByTagName("prepay_id");
			  if (prePayNodeList.getLength()>=1){
				  prepayId = prePayNodeList.item(0).getFirstChild().getNodeValue();
			  }else {
				  String returnMsg = doc.getElementsByTagName("err_code_des").item(0).getFirstChild().getNodeValue();
				  System.out.println("returnMsg:" + returnMsg);
			  }
	      }
	      log.debug("retStr:" + prepayId);
		  return prepayId;
	}
	
	private String getPrepayIdRequestXml(WechatPayBean bean) throws UnsupportedEncodingException
	{
		  String appId = WechatPayConfig.APPID;
		  String tradeType = "NATIVE";
		  String merchanId = WechatPayConfig.mercherId;
		  String attach = "N";
		  String body = "泰康在线微信支付";
		  String totalFee = "1";
		  String noceStr = generateNoceStr();
		  String notifyUrl = WechatPayConfig.notifyUrl + "/";
		  String tradeBillNo = bean.getOutTradeNo();
		  String spbillCreateIp = bean.getRequestIp();
		  String openId = bean.getOpenid();
		  String timeExpired = DateUtil.residualDate((int)bean.getResidualTime(), DateUtil.YYYYMMDDHHMMSS);

	      Map<String,String> map = Maps.newHashMap();
	      map.put("appid",appId);
	      map.put("attach",attach);
	      map.put("body",body);
	      map.put("mch_id",merchanId);
		  map.put("time_expire",timeExpired);
	      map.put("nonce_str",noceStr);
	      map.put("notify_url",notifyUrl);
	      map.put("openid", openId);
	      map.put("out_trade_no",tradeBillNo);
	      map.put("spbill_create_ip",spbillCreateIp);
	      map.put("total_fee",totalFee);
	      map.put("trade_type",tradeType);

	      String sign = sign(map);
	      
	      String xmlMsg = "<xml><appid>" + appId + "</appid><attach>" + attach + "</attach><body>" +
	              body + "</body><mch_id>" + merchanId +"</mch_id><time_expire>"+timeExpired+"</time_expire>"+
	              "<nonce_str>" + noceStr + "</nonce_str><notify_url>" + notifyUrl +
	              "</notify_url><openid>" + openId + "</openid><out_trade_no>" + tradeBillNo + "" +
	              "</out_trade_no>" + "<spbill_create_ip>" + spbillCreateIp + "</spbill_create_ip><total_fee>" +
	              totalFee + "</total_fee><trade_type>"+tradeType+"</trade_type>" +
	              "<sign>" + sign + "</sign></xml>";
	      xmlMsg = new String(xmlMsg.getBytes("utf-8"),Charset.forName("utf-8"));
	      log.info("xmlMsg=="+xmlMsg);
   	      return xmlMsg;
	}
	

	
	/**
	 * 订单号查询支付是否成功请求报文
	 * param out_trade_no
	 * return
	 */
	private String getQueryOrderRequestXml(WechatPayBean bean) {

		String appId = WechatPayConfig.APPID;
		String mch_id = WechatPayConfig.mercherId;
		String key = WechatPayConfig.parternerKey;
		String nonce_str = getRandomString(20);

		StringBuilder signTemp = new StringBuilder();
		signTemp.append("appid=").append(appId);
		signTemp.append("&mch_id=").append(mch_id);
		signTemp.append("&nonce_str=").append(nonce_str);
		signTemp.append("&out_trade_no=").append(bean.getOutTradeNo());
		signTemp.append("&key=").append(key);

		log.debug("-----sign前----->"+signTemp.toString());
		String sign = Md5Encrypt.getMD5Mac(signTemp.toString());
		log.debug("-----sign后----->"+sign);
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		sb.append("<appid>").append(appId).append("</appid>");
		sb.append("<mch_id>").append(mch_id).append("</mch_id>");
		sb.append("<nonce_str>").append(nonce_str).append("</nonce_str>");
		sb.append("<out_trade_no>").append(bean.getOutTradeNo()).append("</out_trade_no>");
		sb.append("<sign>").append(sign).append("</sign>");
		sb.append("</xml>");

		log.info("xmlMsg=="+sb.toString());
		return sb.toString();
	}
	
	//加密sign
	public String sign(Map<String, String> parmMap){
	
/*		      Map<String, String> filteredResult = Maps.filterEntries(parmMap, new Predicate<Map.Entry>() {
		          public boolean apply(Map.Entry entry) {
		              String value = (String) entry.getValue();
					    if(StringUtil.isEmpty(value)){
		                  return false;
		              }
		              return true;
		          }
		      });
//		      SortedMap<String,String> sortedMap = new TreeMap<String, String>(filteredResult);*/
//		      SortedMap<String,String> sortedMap = new TreeMap<String, String>(parmMap);
			  
		      TreeMap<String, String> tree = new TreeMap<String, String>();
		      Iterator<Entry<String, String>> it = parmMap.entrySet().iterator();
			  while (it.hasNext()) {
				Entry<String, String> en = (Entry<String, String>) it.next();
					if ((null != (String) en.getKey()) &&  !"signature".equals(((String) en.getKey()).trim())) {
						tree.put(en.getKey(), en.getValue());
				  }
				}
			  
		      System.out.println(parmMap);
		      System.out.println(tree);
		      String beforeSign = mapJoiner.join(tree) + "&key=" + WechatPayConfig.parternerKey;
		      log.info("preSign=="+beforeSign);
		      String signStr = Hashing.md5().hashString(beforeSign, Charset.forName("UTF-8")).toString().toUpperCase();
		      
		      log.info("sign:"+signStr);
		      return signStr;
		    }
	
	//加密随机数
	public String generateNoceStr(){
	       String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	       Random random = new Random();
	       StringBuilder buf = new StringBuilder();
	       for (int i = 0; i < 32; i++) {
	           int num = random.nextInt(62);
	           buf.append(str.charAt(num));
	       }
	       return buf.toString();
	   }
	
	  private String sendHttpRequest(String xmlMsg, String url) throws ParserConfigurationException, IOException, SAXException {
		  String responseStr = null;
		  try{
			  Header[] header = HttpHeader.custom().other("Content-type", "text/xml;charset=utf-8").build();
			  HttpConfig config = HttpConfig.custom();
			  config.xml(xmlMsg);
			  config.encoding("utf-8");
			  config.headers(header);
			  config.url(url);
			  config.client(HCB.custom().sslpv(SSLs.SSLProtocolVersion.TLSv1).timeout(5000).build());
			  responseStr =  HttpClientUtil.post(config);
			  log.info("response=="+responseStr);
		  } catch (HttpProcessException e){
			  log.error("wechat sendHttpRequest:" + e);
		  }
		  return responseStr;
	  }
	  
		/**
		 * 请求验签
		 * param map
		 * return
		 */
		private boolean verifySign(Map<String, String> map, String partnerKey) {

			String sign = map.get("sign");
			map.remove("sign");
			SortedMap<String,String> sortedMap = new TreeMap<String, String>(map);
			String beforeSign = mapJoiner.join(sortedMap) + "&key="+partnerKey;
			String signStr = Hashing.md5().hashString(beforeSign, Charset.forName("GBK")).toString().toUpperCase();
			log.info("sign=="+sign);
			log.info("mySign=="+signStr);
			return sign.equals(signStr);
		}
		
		private String getRandomString(int length) {
			String base = "abcdefghijklmnopqrstuvwxyz0123456789";
			Random random = new Random();
			StringBuilder sb = new StringBuilder();
			int number;
			for (int i = 0; i < length; i++) {
				number = random.nextInt(base.length());
				sb.append(base.charAt(number));
			}
			return sb.toString();
		}
		
		/**
		 * 根据微信查询状态码转换内部字段对应编码
		 */
		public String getInnerTradeState(String trade_state)
		{
			//微信通道状态归类：支付成功（SUCCESS）、支付失败(REFUND：转入退款、CLOSED：关闭、REVOKED：撤销、PAYERROR：支付失败)、处理中（USERPAYING）、等待支付（NOTPAY）
			//订单不存在（ORDERNOTEXIST）, 将处理中状态，处理成在线对应的等待支付状态,jdk1.7不支持case String
//			String tradeState = trade_state;
			String innerState = "0";
//			switch(trade_state)
//			{
//				case "SUCCESS"	  	: innerState = PayStatusEnum.SUCCESS.getCode(); 	break;
//				case "NOTPAY"     	: innerState = PayStatusEnum.WAIT.getCode(); 		break;
//				case "USERPAYING" 	: innerState = PayStatusEnum.PAYING.getCode(); 		break;
//				case "CLOSED" 	  	: innerState = PayStatusEnum.CLOSE.getCode(); 		break;
//				case "REVOKED"	  	: innerState = PayStatusEnum.FAIL.getCode(); 		break;
//				case "REFUND"     	: innerState = PayStatusEnum.FAIL.getCode(); 		break;
//				case "PAYERROR"   	: innerState = PayStatusEnum.FAIL.getCode(); 		break;
//				case "ORDERNOTEXIST": innerState = PayStatusEnum.WAIT.getCode(); 		break;
//				default			  	: innerState = PayStatusEnum.FAIL.getCode(); 		break;
//			}
			return innerState;
		}
	  
}
