package test.demo.first;


import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class TestOther
{
	
	@Test
	public void test2()
	{
		  String responseStr = "<xml><return_code><![CDATA[SUCCESS]]></return_code>" +
				  "<return_msg><![CDATA[OK]]></return_msg>" +
				  "<appid><![CDATA[wxcd7143c00e5bb6f7]]></appid>" +
				  "<mch_id><![CDATA[1490062962]]></mch_id>" +
				  "<nonce_str><![CDATA[z0xHOcxIly5NwXjZ]]></nonce_str>" +
				  "<sign><![CDATA[EAFD73839A8DC4C8334C12BC12C8CFEC]]></sign>" +
				  "<result_code><![CDATA[SUCCESS]]></result_code>" +
				  "<prepay_id><![CDATA[wx20171030134629d44fc550070616442980]]></prepay_id>" +
				  "<trade_type><![CDATA[NATIVE]]></trade_type>" +
				  "<code_url><![CDATA[weixin://wxpay/bizpayurl?pr=KsLyEDZ]]></code_url>" +
				  "</xml>";
		  String url = "";
		  try {
			  
		  
	      if (responseStr!=null)
	      {
	          DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	          DocumentBuilder builder;
			
				builder = factory.newDocumentBuilder();

	          StringReader sr = new StringReader(responseStr.toString());
	          InputSource is = new InputSource(sr);
	          Document doc;
			
				doc = builder.parse(is);

			  String return_code = doc.getElementsByTagName("return_code").item(0).getFirstChild().getNodeValue();
			  String result_code =	doc.getElementsByTagName("result_code").item(0).getFirstChild().getNodeValue();
	          if("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code))
	          {
		          NodeList prePayNodeList = doc.getElementsByTagName("code_url");
		          if (prePayNodeList.getLength()>=1)
		          {
		          	  url = prePayNodeList.item(0).getFirstChild().getNodeValue();
		          }else
		          {
		          	  String returnMsg = doc.getElementsByTagName("err_code_des").item(0).getFirstChild().getNodeValue();
//			          log.error("code_url Error: orderNo=="+unifiedOrderRequestDTO.getTradeId()+" "+returnMsg);
//		          	  return null;
		          }
	          }else
	          {
	          	  String returnMsg = doc.getElementsByTagName("return_msg").item(0).getFirstChild().getNodeValue();
//		          log.error("code_url Error: orderNo=="+unifiedOrderRequestDTO.getTradeId()+" "+returnMsg);
//	          	  return null;
	          }

	      }
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		System.out.println(url);
		
	}
	
//	@Test
	public void test1()
	{
		JSONObject obj = new JSONObject();
		obj.put("aa", "bbb");
		System.out.println(obj.toString());
		
		Object o = (Object)obj;
		
		System.out.println(obj);
		System.out.println(o);
		
		Map map = new HashMap();
		map.put("cc", "dd");
		System.out.println(map);
		System.out.println(map.toString());
		
		
	}
	
//	@Test
//	public void test1()
//	{
//		String str = "Asad警察a2323";
//		String s = str.toUpperCase();
//		System.out.println(s);
//	}
	
//	@Test
	public void testString()
	{
		String str = "A70";
		String s = str.substring(1, str.length());
		System.out.println(s);
	}
	
//	@Test
	public void testTime()
	{
		Calendar cal = Calendar.getInstance();
		
		String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date());  
	    System.out.println(str);  
		cal.setTime(new Date());
		
		cal.add(Calendar.MONTH, 1);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime()));
		
		cal.add(Calendar.MONTH, 3);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime()));
		
		cal.add(Calendar.YEAR, 1);
		System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(cal.getTime()));
		
		
	}
	
//	@Test
//	public void jsonschema()
//	{
//		ObjectMapper oMapper  = new ObjectMapper();
//		String jsonSchema = "{\"$schema\": \"http://json-schema.org/draft-04/schema#\",\"type\": \"object\",\"properties\": {\"openId\": {\"type\": \"string\"},\"suitId\": {\"type\": \"string\"},\"checkCode\": {\"type\": \"string\"},\"channelId\": {\"type\": \"string\",\"pattern\": \"^\\S+$\"},\"applicants\": {\"type\": \"object\",\"properties\": {\"name\": {\"type\": \"string\"},\"identifyNo\": {\"type\": \"string\",\"pattern\": \"^([0-9]{4}([0-9]{10}|[*]{10})[0-9]{3}[0-9Xx])|([0-9*]{15})$\"},\"phoneNo\": {\"type\": \"string\",\"pattern\": \"1[0-9]{2}([0-9]{4}|[*]{4})[0-9]{4}\"},\"identifyType\": {\"type\": \"string\",\"pattern\": \"^01$\"}},\"required\": [\"name\",\"identifyNo\",\"phoneNo\",\"identifyType\"]},\"genericData\": {\"type\": \"object\",\"properties\": {\"startTime\": {\"type\": \"string\"},\"processHandler\": {\"type\": \"string\"},\"fieldAI\": {\"type\": \"string\"}},\"required\": [\"startTime\",\"processHandler\",\"fieldAI\"]}},\"required\": [\"openId\",\"suitId\",\"checkCode\",\"channelId\",\"applicants\",\"genericData\"]}";
//		final JsonNode fstabSchema = oMapper.readTree(jsonSchema);
//		final JsonNode reqSchema = oMapper.readTree(reqJson);
//		
//		final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
//        final JsonSchema schema = factory.getJsonSchema(fstabSchema);
//        ProcessingReport report = schema.validate(reqSchema);
//        
//        if(!report.isSuccess())
//        {
//        	
//        }
//	}
	
//	@Test
	public void testZ()
	{
		String str = "��sda3s";
//		String pattern = "^[0-9]{6}$";
		String pattern = "^\\S{2,5}$";
//		String pattern = "^\\S+$";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		 if (m.find()) {
	         System.out.println("Found value: " + m.group(0) );
	      } else {
	         System.out.println("NO MATCH");
	      }
		
//		assertTrue(m.find());
		System.out.println("aa");
	}
	
//	@Test
	public  void token()
	{
//		String str = "{\"token\":\"123456\",\"req\":{\"startDate\":\"1462440744\",\"customerId\":\"728125015438274560\",\"requestId\":\"51753270773510856818183583274698929960411608819426\",\"payWayId\":71,\"payCallBackUrl\":\"http://weit.taikang.com/wechat/rest/v4/insure/policy\",\"failUrl\":\"http://weit.taikang.com/WHO/page/payFail.html\",\"payAccount\":\"wangdianceshi03\",\"issueDate\":\"1462444746673\",\"applicantList\":[{\"name\":\"����\",\"identifyNumber\":\"11010119801218821X\",\"identifyType\":\"01\",\"mobile\":\"13800132118\"}],\"successUrl\":\"http://weit.taikang.com/WHO/page/paySuccess.html\"}}";
//		String key = "alitkang";
//		String ret = changeToken(str, key);
//		System.out.println(ret);
		
		HashMap map = new HashMap();
		JSONObject obj = new JSONObject();
		obj.put("fieldAA", "11");
		obj.put("fieldAd", "11");
		System.out.println(obj.toString());
		JSONObject obj2 = new JSONObject();
		obj2.put("a", obj.toJSONString());
		System.out.println(obj2.toJSONString());
		
		
		String str = "{\"b\":{\"fieldAA\":\"11\"}}";
//		String str = "{\"b\":\"{\\\"fieldAA\\\":\\\"11\\\",\\\"fieldAd\\\":\\\"11\\\"}\"}";
		System.out.println(str);
		
		try
		{
			a a1 = JSONObject.parseObject(str ,a.class );
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	class a{
		Map b;
		
	}
	
	public String changeToken(String input,String key){
		Map<String,Object> map = (Map<String, Object>) JSON.parse(input);
		String req = String.valueOf(map.get("req"));

		Map<String, Object> map1 = JSON.parseObject(req);
		Set<String> keySet = map1.keySet();
		List<String> li = new ArrayList<String>();
		for (String string : keySet) {
			li.add(string);
		}
		Collections.sort(li);
		String req1 = "";
		for (int i = 0; i < li.size(); i++) {
			if(li.get(i).equals("applicantList")){
				Object Object1 = map1.get("applicantList");
				JSONArray parseArray = JSON.parseArray(Object1.toString());
				Map<String,Object> app = (Map<String,Object>)parseArray.get(0);
				Set<String> keySet2 = app.keySet();
				List<String> list = new ArrayList<String>();
				for (String string : keySet2) {
					list.add(string);
				}
				Collections.sort(list);
				for (int j = 0; j < list.size(); j++) {
					req1 += list.get(j)+app.get(list.get(j));
				}
			}else if (li.get(i).equals("insuredList")) {
				Object Object1 = map1.get("insuredList");
				JSONArray parseArray = JSON.parseArray(Object1.toString());
				Map<String, Object> app = (Map<String, Object>) parseArray.get(0);
				Set<String> keySet2 = app.keySet();
				List<String> list = new ArrayList<String>();
				for (String string : keySet2) {
					list.add(string);
				}
				Collections.sort(list);
				for (int j = 0; j < list.size(); j++) {
					req1 += list.get(j) + app.get(list.get(j));
				}
			} 
			else if (li.get(i).equals("parameterMap")) {
				String parameterMap = String.valueOf(map1.get("parameterMap"));
				Map<String, Object> app = JSON.parseObject(parameterMap);
				Set<String> keySet2 = app.keySet();
				List<String> list = new ArrayList<String>();
				for (String string : keySet2) {
					list.add(string);
				}
				Collections.sort(list);
				for (int j = 0; j < list.size(); j++) {
					req1 += list.get(j) + app.get(list.get(j));
				}
			} 
			else{
				req1 += li.get(i)+map1.get(li.get(i)); 
			}
		}

		String token = Md5.getMD5Mac(req1+key, "utf-8");
		map.put("token", token);
		String result = JSON.toJSONString(map);
		return result;
	}
	
//	@Test
	public void testMapToJson()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("name", "wang");
		map.put("addr", "laifeng");
		System.out.println(map.toString());
//		JSONObject json = JSONObject.
//		System.out.println(json.toString());
		
	}
	
	
}
