package com.demo.bussiness.http;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class JUnitTest
{
//	@Test
	public void testRequestGet() throws Exception
	{
		String url = "http://my.taikang.com:8091/demo/rest/nomaltest/constName?parm=mytest";		
		String ret = RequestUtil.initHttp().doGet(url);
		System.out.println(ret);
		
		
	}
	
	@Test
	public void testRequestPost() throws Exception
	{
//		String url = "http://my.taikang.com:8091/demo/rest/nomaltest/constPostName?parm=testpost";
		String url = "http://10.130.201.180:8080/tk-link/rest";
		JSONObject json = new JSONObject();
		json.put("name", "test1");
		json.put("age", 21);
		
//		url = "http://pre.insurance.qubao100.com/taikang/notify";
//		url = "http://119.29.146.46/taikang/notify";
		String content = json.toString();
		content = "{\"coop_id\": \"da_bao_te\",\"service_id\": \"01\",\"sign_type\": \"md5\",\"sign\": \"c829d2de6d98d6c939a97498204696f2\",\"format\": \"json\",\"charset\": \"utf-8\",\"version\": \"1.0\",\"timestamp\": 1490251927000,\"serial_no\": \"149025192713907460\",\"product_type\": \"propertiestwo\",\"apply_content\": {\"holder_name\": \"测试\",\"holder_cid_type\": \"02\",\"holder_cid_number\": \"468976658854\",\"holder_mobile\": \"18301595067\",\"holder_email\": \"service@datebao.com\",\"holder_birthday\": \"1980-12-01\",\"holder_sex\": 2,\"insurants_name\": \"测试\",\"insurants_cid_type\": \"02\",\"insurants_cid_number\": \"468976658854\",\"insurants_mobile\": \"18301595067\",\"insurants_email\": \"\",\"insurants_birthday\": \"1980-12-01\",\"insurants_sex\": 2,\"amount\": \"50000.00\",\"premium\": \"120.00\",\"fromId\": \"60685\",\"relatedperson\": \"01\",\"comboId\": \"1017A00001\",\"kindList\": [{\"kindCode\": \"1017001\",\"kindName\": \"e顺重大疾病保险B款\",\"amount\": \"50000.00\"}],\"issueDate\": \"2017-03-23\",\"startDate\": \"2017-03-24 00:00:00\",\"endDate\": \"2018-03-23 23:59:59\",\"holder_insuredFlag\": \"1\",\"holder_insuredType\": \"1\",\"insurants_insuredFlag\": \"2\",\"insurants_insuredType\": \"1\",\"insurants_detailedAddress\": \"N/A\",\"channelTip\": \"02040101\",\"intermediarycode\": \"010000000000400070\",\"businessChannel\": \"05\",\"solutionCode\": \"0100000000004000700004\",\"salesmanCode\": \"S131990022\",\"FieldAA\": \"1017A00001\",\"FieldAC\": \"\",\"FieldAD\": \"0\"}}";
		
		content = "{\"apply_content\": {\"FieldGA\": \"1\",\"FieldGJ\": \"康照无生\",\"FieldGK\": \"02\",\"FieldGL\": \"G33333331\",\"FieldGM\": \"1\",\"FieldGO\": \"2017-05-23\",\"FieldGP\": \"0.01\",\"FieldGQ\": \"1\",\"FieldGR\": \"800000116092600012\",\"FieldGT\": \"18000000000\",\"amount\": 7000.0,\"comboId\": \"1204A00201\",\"endDate\": \"2017-11-23 23:59:59\",\"issueDate\": \"2017-05-23 15:52:46\",\"kindList\": [{\"amount\": 7000.0,\"kindCode\": \"1204001\",\"kindName\": \"健康体检误判责任保险\"}],\"premium\": 1.5,\"startDate\": \"2017-05-24 00:00:00\"},\"charset\": \"utf-8\",\"coop_id\": \"ci_ji\",\"format\": \"json\",\"product_type\": \"1204A002\",\"serial_no\": \"20170612155008761\",\"service_id\": \"02\",\"sign\": \"be3666d4563048a3811cc6bd084c6827\",\"sign_type\": \"md5\",\"timestamp\": 1497253808761,\"version\": \"1.0\"}";
		
//		content = "{\"coop_id\":\"bai_chuan_xin\",\"service_id\":\"01\",\"sign_type\":\"md5\",\"sign\":\"a7e3741bb8adbe69b87f60cf78ddda65\",\"format\":\"json\",\"charset\":\"utf-8\",\"version\":\"1.0\",\"timestamp\":\"1466478472154\",\"serial_no\":\"20174444151332226233006\",\"product_type\":\"1301A003\",\"apply_content\":{\"requestId\":\"201755555000000000003\",\"issueDate\":\"2017-04-04\",\"startDate\":\"2017-04-0700:00:00\",\"endDate\":\"2018-04-0623:59:59\",\"businessChannel\":\"05\",\"comboId\":\"1301A00301\",\"customerId\":\"500227198909235887\",\"fromId\":\"62967\",\"amount\":\"1044000\",\"premium\":\"540\",\"salesmanCode\":\"S131990026\",\"channelTip\":\"01010106\",\"solutionCode\":\"0100000000006001120001\",\"groupInd\":\"2\",\"applicantList\":[{\"name\":\"阳光国际\",\"identifyType\":\"2\",\"identifyNumber\":\"YY56734\",\"itemprovinceCode\":\"110000\",\"itemcityCode\":\"110000\",\"itemdistrictCode\":\"110101\",\"detailedAddress\":\"五菱打发斯蒂芬\",\"contactName\":\"斯蒂芬\",\"mobile\":\"13565897541\",\"mail\":\"aa@163.com\",\"postCode\":\"100000\",\"insuredType\":\"2\",\"insuredFlag\":\"1\",\"officePhoneNumber\":\"000-00000000\"}],\"insuredList\":[{\"name\":\"张明明\",\"sex\":\"1\",\"identifyType\":\"01\",\"identifyNumber\":\"230805199904056736\",\"birthday\":\"1999-04-05\",\"mobile\":\"13515845982\",\"occupationCode\":\"00601054\",\"occupationName\":\"装饰装修设计制图人员\",\"insuredFlag\":\"2\",\"healthStatus\":\"0\",\"insuredType\":\"1\",\"projectCode\":\"1301A00301\",\"relatedperson\":\"99\",\"remark\":\"01\"},{\"name\":\"张信息\",\"sex\":\"1\",\"identifyType\":\"01\",\"identifyNumber\":\"360122198704047336\",\"birthday\":\"1987-04-04\",\"mobile\":\"13515845982\",\"occupationCode\":\"00601054\",\"occupationName\":\"装饰装修设计制图人员\",\"insuredFlag\":\"2\",\"healthStatus\":\"0\",\"insuredType\":\"1\",\"projectCode\":\"1301A00301\",\"relatedperson\":\"99\",\"remark\":\"01\"},{\"name\":\"张吱吱\",\"sex\":\"1\",\"identifyType\":\"01\",\"identifyNumber\":\"361129198704040252\",\"birthday\":\"1987-04-04\",\"mobile\":\"13515845982\",\"occupationCode\":\"00601054\",\"occupationName\":\"装饰装修设计制图人员\",\"insuredFlag\":\"2\",\"healthStatus\":\"0\",\"insuredType\":\"1\",\"projectCode\":\"1301A00301\",\"relatedperson\":\"99\",\"remark\":\"01\"}],\"parameterMap\":{\"fieldAL\":\"1\",\"fieldAB\":\"大众点评\",\"fieldAC\":\"88776555E7\",\"fieldAD\":\"01\",\"fieldAT\":\"大众点评有限公式\",\"fieldAU\":\"889900000000\",\"fieldAJ\":\"110000\",\"fieldAK\":\"110000\",\"fieldAI\":\"102\",\"fieldAA\":\"1\",\"fieldAE\":\"北京市昌平区北清路中关村科技园\",\"fieldAF\":\"010-223333\"}}}";
		
		long time = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());
		String ret = RequestUtil.initHttp().doPost(url, content, "application/json", "UTF-8");
		long time2 = System.currentTimeMillis();
		System.out.println(System.currentTimeMillis());
		System.out.println(time2 - time);
		System.out.println(ret);
		
	}
	
}
