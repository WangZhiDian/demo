package test.demo.first;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.common.util.Function.StringUtil;
import com.google.common.collect.Maps;


public class TestFirst
{
	private static HashMap<String, String> cidTypeMap;
	static
	{
		cidTypeMap = new HashMap<String, String>();
		cidTypeMap.put("01", "IDENTITY_CARD");
	};
	
	@Test
	public void test18()
	{
		String a = cidTypeMap.get("01");
		System.out.println("change:" + a);
	}
	
//	@Test
	public void test17()
	{
		double dou = 11.42;
		String change = TestFirst.digitUppercase(dou);
		System.out.println("change:" + change);
	}
	
	public static String digitUppercase(double n) {
		String fraction[] = { "角", "分" };
		String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		String unit[][] = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };

		String head = n < 0 ? "负" : "";
		n = Math.abs(n);

		String s = "";
		for (int i = 0; i < fraction.length; i++) {
			//优化double计算精度丢失问题
			BigDecimal nNum = new BigDecimal(n);
			BigDecimal decimal = new BigDecimal(10);
			BigDecimal scale = nNum.multiply(decimal).setScale(2, RoundingMode.HALF_EVEN);
			double d = scale.doubleValue();
			s += (digit[(int) (Math.floor(d * Math.pow(10, i)) % 10)] + fraction[i])
					.replaceAll("(零.)+", "");
		}
		if (s.length() < 1) {
			s = "整";
		}
		int integerPart = (int) Math.floor(n);

		for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
			String p = "";
			for (int j = 0; j < unit[1].length && n > 0; j++) {
				p = digit[integerPart % 10] + unit[1][j] + p;
				integerPart = integerPart / 10;
			}
			s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i]
					+ s;
		}
		return head
				+ s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "")
						.replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}
	
//	@Test
	public void test16()
	{
		DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");         
		Date date = null, dateInsu = null;
		String str = "2015-01-30";
		String str2 = "2085-01-29"; //2085年01月28
		try {    
            date = format1.parse(str);
            dateInsu = format1.parse(str2);
		} catch (ParseException e) {    
		    e.printStackTrace();    
		}
		int age = getAgeByBirth(date, dateInsu);
		System.out.println("age:" + age);
	}
	
	public static int getAgeByBirth(Date birthday,Date insureday)
	{
		int iAge = 0;
		
		try
		{
			Calendar birthDate = Calendar.getInstance();
			birthDate.setTime(birthday);
			Calendar insureDate = Calendar.getInstance();
			insureDate.setTime(insureday);
			
			iAge = insureDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
			
			birthDate.add(Calendar.YEAR, iAge);
			if((insureDate.getTime()).before(birthDate.getTime()))
			{
				iAge --;
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return iAge;
	}
	
//	@Test
	public void test15()
	{
		String str = "ConfirmPay-redirectKey-123456";
		String key = str.substring(str.lastIndexOf("-") + 1);
		System.out.println("key:" + key);
	}
	
//	@Test
	public void test14()
	{
		Date now = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);
		
		String format = "yyyy-MM-dd HH:mm";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		calendar.add(Calendar.MINUTE, 5);
		String time = sdf.format(calendar.getTime());
		
		
		System.out.println("time:" + time);
	}
	
	
//	@Test
	public void test13()
	{
//		String responseStr = "gmt_create=2017-11-15+11%3A06%3A14&charset=UTF-8&gmt_payment=2017-11-15+11%3A06%3A22&notify_time=2017-11-15+11%3A06%3A23&subject=%E6%94%AF%E4%BB%98%E5%AE%9DPC&sign=o1dXglRpm2LTFMikMsQc60V2l1PN7IfE8CynxFyDcDkHadYx4D%2BVHYGo1nmtn%2FzMK5Wko87pU9FNgClcFAK5x3%2FlnqXBoKj6LeAUsasPclMBZzkMZqZRLLq%2FTXp4zwutwM4hs0e42MrOsoqLLYBdt3MgCiNIH76YFx4tmW2VdbUUIFrzNZPpTlf33bGepjvMqHl2V683TQC53yTI0u5lLjsLlSeDNQ3iGOX9HkVLCOCYneNepI0vIkt0wq9w2AhJFooniwwKlGFRBA9sO9xaj0Fpq1JS8ufnWjEp%2FqDqwAbB%2B8IR6C2joTTWFVT8wUmQ8XqZrshLuq9vT8r2yQnC4g%3D%3D&buyer_id=2088802797139208&body=PC-ZHIFU&invoice_amount=0.01&version=1.0&notify_id=b0af72a89122b745ce10e23a87bf917hjm&fund_bill_list=%5B%7B%22amount%22%3A%220.01%22%2C%22fundChannel%22%3A%22ALIPAYACCOUNT%22%7D%5D&notify_type=trade_status_sync&out_trade_no=0-11111-11010-767831&total_amount=0.01&trade_status=TRADE_SUCCESS&trade_no=2017111521001104200219030950&auth_app_id=2017101709352036&receipt_amount=0.01&point_amount=0.00&app_id=2017101709352036&buyer_pay_amount=0.01&sign_type=RSA2&seller_id=2088821471538834";
		String responseStr = "total_amount=0.01&timestamp=2017-11-15+11%3A06%3A34&sign=IXzcSTkhyqMk4iIZRiQAe4RGEZCwbLRKWfTRkAeKWcQvmzOniawxbwXCir3cPOaBcUVfLjBauA1NZilVb5Z6CoikLsAeKbWRcWUvsZ8ZAhyctXSy8VMll4juIIdWohm7fAchRgUUwi%2F9I3zFmKCb5xpRcv6W3vDSjV%2BL8jDkx7Gph%2BTJ0Z0Cm%2FkJGP7P%2BOKeodscqDvxBRwssT8BRstk0Ci09ICE%2BwNT2sESUuv06P5Zt6UV%2BdFVz431wM1jDHxlxWi%2BOf24AP8gXvvMgALcdzTA37bSKLPTUmjIQNMUtOK7d19RKWLmYyjftVVrg9f9nrbn1fPUGXHPDHH9otVOXw%3D%3D&trade_no=2017111521001104200219030950&sign_type=RSA2&auth_app_id=2017101709352036&charset=UTF-8&seller_id=2088821471538834&method=alipay.trade.page.pay.return&app_id=2017101709352036&out_trade_no=0-11111-11010-767831&version=1.0";
		String strDecode = "";
		try {
			strDecode = URLDecoder.decode(responseStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(strDecode);
		Map<String, String> params = new HashMap<String, String>();
		SortedMap<String,String> sortedMap = new TreeMap<String, String>();
		String key = "", value = "";
		String[] pair = null;
		String[] responseArr = strDecode.split("&");
		for(int i = 0; i < responseArr.length; i++)
		{
			pair = responseArr[i].split("=");
			key = pair[0];
			value = pair[1];
			if(StringUtil.isNotEmpty(value))
			{
				params.put(key, value);
				sortedMap.put(key, value);
			}
		}
		System.out.println(params.toString());
		System.out.println(sortedMap.toString());
		
		
	}
	
//	@Test
	public void test12()
	{
		String str =  "omCIGj8Q2emXDl2BrJMRa3dYFEAw";
		String str2 = "omCIGj8Q2emXDI2BrJMRa3dYFEAw";
		boolean b = str.equals(str2);
		 System.out.println(b);
	}
//	@Test
	public void test11()
	{
		 String var =  new String("±£ÏÕµ¥ºÅ");
		 byte[] t_gbk;
		 String str = "";
		try {
			t_gbk = var.getBytes("GBK");
			str = new String(t_gbk, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 System.out.println(str);
	}
	
//	@Test
	public void test10()
	{
		JSONArray arr = new JSONArray();
		String str = "交易时间,公众账号ID,商户号,子商户号,设备号,微信订单号,商户订单号,用户标识,交易类型,交易状态,付款银行,货币种类,总金额,企业红包金额,商品名称,商户数据包,手续费,费率" +
				"`2017-10-26 20:48:11,`wxcd7143c00e5bb6f7,`1490062962,`0,`,`4200000022201710260493815066,`0-11111-370-3100002,`omCIGj3EF3zAx2YB3u4OW8dql1d4,`NATIVE,`SUCCESS,`CFT,`CNY,`0.01,`0.00,`NATIVE支付测试,`N,`0.00000,`0.60%" +
				"`2017-10-26 20:40:59,`wxcd7143c00e5bb6f7,`1490062962,`0,`,`4200000023201710260493442845,`0-11111-370-3100001,`omCIGj3EF3zAx2YB3u4OW8dql1d4,`NATIVE,`SUCCESS,`CFT,`CNY,`0.01,`0.00,`NATIVE支付测试,`N,`0.00000,`0.60%" +
				"`2017-10-26 20:51:30,`wxcd7143c00e5bb6f7,`1490062962,`0,`,`4200000027201710260494013661,`0-11111-370-3100003,`omCIGj3EF3zAx2YB3u4OW8dql1d4,`NATIVE,`SUCCESS,`CFT,`CNY,`0.01,`0.00,`NATIVE支付测试,`N,`0.00000,`0.60%" +
				"总交易单数,总交易额,总退款金额,总企业红包退款金额,手续费总金额" +
				"`3,`0.03,`0.00,`0.00,`0.00000"	;
		String billInfo=str.split("费率")[1];
		String[] strs=billInfo.split("%`");
		String[] temp=null;
		for(int i=0;i<strs.length;i++){
			temp=strs[i].split(",`");
			JSONObject obj = new JSONObject();
			obj.put("sourcecode", "interface");
			obj.put("paywayid", "71");
			obj.put("mch_id", temp[2]);
			obj.put("out_trade_no", temp[6]);
			obj.put("paydate", temp[0].replace("`", ""));
			obj.put("amount", Double.parseDouble(temp[12]));
			arr.add(obj);
			}
		
		System.out.println(arr.toJSONString());
	}
	
	public static java.util.Date string2Date(String strDate,String fmt)
	//将一个字符串转化为指定格式的日期:
	//yyyy年 MM月 dd 日 HH 24小时:mm 分钟:ss秒
	//by dingping 20030627
	{
		try{
			//String strdatefmt="yyyy"+separetor+"MM"+separetor+"dd HH:mm:ss";
			SimpleDateFormat formatter = new SimpleDateFormat(fmt);
			ParsePosition pos = new ParsePosition(0);
			java.util.Date date=formatter.parse(strDate,pos);		
			return date;
		}
		catch(Exception e)
		{
			System.out.println("com.taikang.util.StringFuncs.string2Date:"+e);
			return null;
		}		
	}
	
//	@Test
	public void test9() throws ParseException
	{
		URLDecoder decoder = new URLDecoder();
		String old = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx520c15f417810387&redirect_uri=https%3A%2F2Fchong.qq.com%2Fphp%2Findex.php%3Fd%3D%26c%3DwxAdapter%26m%3DmobileDeal%26showwxpaytitle%3D1%26vb2ctag%3D4_2030_5_1194_60&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		String str = decoder.decode(old);
		System.out.println(str);
	}
	
//	@Test
	public void test8() throws ParseException
	{
//		String time = "2014-11-27 15:45:57";
//		String str = time.replace(" ", "").replace("-", "").replace(":", "");
		
//		String total_fee = "12.11";
//		double d  = Double.parseDouble(total_fee);
//		d = d*100;
//		int i = BigDecimal.valueOf(d).intValue();
//		int amount = (int)Double.parseDouble(total_fee) * 100;
		
		String str = "20141127";
		Date date1 = new SimpleDateFormat("yyyyMMdd").parse(str);
		
		String str2 = new SimpleDateFormat("yyyy-MM-dd").format(date1).toString();
		
		System.out.println("str:" + str2 );
	}
	
//	@Tes
	public void test7()
	{
		Map<String,String> param = Maps.newHashMap();
		param.put("Test", "test");
		
		System.out.println(param.toString());
		
		String str = JSON.toJSONString(param);
		
		System.out.println(str);
	}
	
//	@Test
	public void splet6()
	{
//		String str = "{\"service_id\":\"02\",\"messageBody\":\" \",\"coop_id\":\"BOSHENG_CL\",\"logger_info\":\"(10233212-BOSHENG_CL)\",\"apply_content\":{\"fromId\":\"63767\",\"startDate\":\"2017-07-20 00:00:00\",\"FieldGQ\":\"1\",\"holder_name\":\"北京慈记网络科技有限公司\",\"FieldGR\":\"800000117020112078456\",\"channelTip\":\"01010106\",\"FieldGO\":\"2017-08-29\",\"FieldGP\":\"0.01\",\"holder_insuredFlag\":\"1\",\"FieldGM\":\"2\",\"FieldGN\":\"1986-12-15\",\"holder_email\":\"shuai@126.com\",\"endDate\":\"2018-07-19 23:59:59\",\"holder_cid_type\":\"2\",\"payTime\":\"1491408000000\",\"salesmanCode\":\"S133011004\",\"holder_cid_number\":\"452101197107101338\",\"solutionCode\":\"0100000000006002130001\",\"amount\":\"300000\",\"payaccount\":\"0000\",\"insurants_insuredType\":\"2\",\"insurants_cid_number\":\"452626198612157008\",\"insurants_name\":\"小明\",\"insurants_insuredFlag\":\"2\",\"insurants_sex\":\"2\",\"holder_mobile\":\"13601228991\",\"businessChannel\":\"05\",\"holder_insuredType\":\"2\",\"premium\":\"1\",\"holder_contactName\":\"初光磊\",\"insurants_birthday\":\"1986-12-15\",\"payWayId\":\"212\",\"FieldAD\":\"0\",\"issueDate\":\"2017-07-19 00:00:00\",\"insurants_cid_type\":\"01\",\"FieldAA\":\"1015A00201\",\"kindList\":[{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任一\",\"kindCode\":\"1015008\"},{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任二\",\"kindCode\":\"1015009\"},{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任三\",\"kindCode\":\"1015010\"}],\"intermediarycode\":\"010000000000600213\",\"relatedperson\":\"01\",\"payMoney\":\"1\",\"comboId\":\"1015A00201\",\"insurants_mobile\":\"13601228991\",\"insurants_email\":\"shuai@126.com\",\"FieldGJ\":\"王钿\",\"FieldGL\":\"340823198612159008\",\"FieldGK\":\"01\"},\"sign_type\":\"md5\",\"charset\":\"utf-8\",\"F_CD_1015A0021\":{\"service_id\":\"02\",\"sign\":\"9aa166905ee73df8610662a5e989a1c3\",\"timestamp\":\"\",\"coop_id\":\"BOSHENG_CL\",\"apply_content\":{\"fromId\":\"63767\",\"startDate\":\"2017-07-20 00:00:00\",\"FieldGQ\":\"1\",\"FieldGR\":\"800000117020112078456\",\"holder_name\":\"北京慈记网络科技有限公司\",\"channelTip\":\"01010106\",\"FieldGO\":\"2017-08-29\",\"FieldGP\":\"0.01\",\"holder_insuredFlag\":\"1\",\"FieldGM\":\"2\",\"FieldGN\":\"1986-12-15\",\"holder_email\":\"shuai@126.com\",\"endDate\":\"2018-07-19 23:59:59\",\"holder_cid_type\":\"2\",\"payTime\":\"1491408000000\",\"salesmanCode\":\"S133011004\",\"holder_cid_number\":\"452101197107101338\",\"solutionCode\":\"0100000000006002130001\",\"amount\":\"300000\",\"payaccount\":\"0000\",\"insurants_insuredType\":\"2\",\"insurants_cid_number\":\"452626198612157008\",\"insurants_name\":\"小明\",\"insurants_insuredFlag\":\"2\",\"insurants_sex\":\"2\",\"holder_mobile\":\"13601228991\",\"businessChannel\":\"05\",\"holder_insuredType\":\"2\",\"premium\":\"1\",\"payWayId\":\"212\",\"holder_contactName\":\"初光磊\",\"insurants_birthday\":\"1986-12-15\",\"issueDate\":\"2017-07-19 00:00:00\",\"FieldAD\":\"0\",\"insurants_cid_type\":\"01\",\"kindList\":[{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任一\",\"kindCode\":\"1015008\"},{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任二\",\"kindCode\":\"1015009\"},{\"amounta\":\"1\",\"kindName\":\"母婴特定疾病综合医疗保险责任三\",\"kindCode\":\"1015010\"}],\"FieldAA\":\"1015A00201\",\"intermediarycode\":\"010000000000600213\",\"relatedperson\":\"01\",\"payMoney\":\"1\",\"insurants_mobile\":\"13601228991\",\"comboId\":\"1015A00201\",\"insurants_email\":\"shuai@126.com\",\"FieldGJ\":\"王钿\",\"FieldGL\":\"340823198612159008\",\"FieldGK\":\"01\"},\"sign_type\":\"md5\",\"charset\":\"utf-8\",\"product_type\":\"1015A002\",\"format\":\"json\",\"serial_no\":\"10233212\",\"version\":\"1.0\"},\"product_type\":\"1015A002\",\"format\":\"json\",\"serial_no\":\"10233212\",\"version\":\"1.0\",\"CHANNEL_KEY\":\"1234567890ABCDEF\",\"timestamp\":\"\",\"sign\":\"9aa166905ee73df8610662a5e989a1c3\",\"flowList\":\"F_CD_1015A002\",\"sysCharset\":\"utf-8\"}";
//		String str = "{\"service_id\":\"02\",\"messageBody\":\"{\\n        \\\"FieldAA\\\": \\\"1015A00201\\\",\\n        \\\"FieldAD\\\": \\\"0\\\",\\n        \\\"FieldGJ\\\": \\\"王钿\\\",\\n        \\\"FieldGK\\\": \\\"01\\\",\\n        \\\"FieldGL\\\": \\\"340823198612159008\\\",\\n        \\\"FieldGM\\\": \\\"2\\\",\\n        \\\"FieldGN\\\": \\\"1986-12-15\\\",\\n        \\\"FieldGO\\\": \\\"2017-08-29\\\",\\n        \\\"FieldGP\\\": \\\"0.01\\\",\\n        \\\"FieldGQ\\\": \\\"1\\\",\\n        \\\"FieldGR\\\": \\\"800000117020112078456\\\",\\n        \\\"amount\\\": \\\"300000\\\",\\n        \\\"businessChannel\\\": \\\"05\\\",\\n        \\\"channelTip\\\": \\\"01010106\\\",\\n        \\\"comboId\\\": \\\"1015A00201\\\",\\n        \\\"endDate\\\": \\\"2018-07-19 23:59:59\\\",\\n        \\\"fromId\\\": \\\"63767\\\",\\n        \\\"holder_cid_number\\\": \\\"452101197107101338\\\",\\n        \\\"holder_cid_type\\\": \\\"2\\\",\\n        \\\"holder_contactName\\\": \\\"初光磊\\\",\\n        \\\"holder_email\\\": \\\"shuai@126.com\\\",\\n        \\\"holder_insuredFlag\\\": \\\"1\\\",\\n        \\\"holder_insuredType\\\": \\\"2\\\",\\n        \\\"holder_mobile\\\": \\\"13601228991\\\",\\n        \\\"holder_name\\\": \\\"北京慈记网络科技有限公司\\\",\\n        \\\"insurants_birthday\\\": \\\"1986-12-15\\\",\\n        \\\"insurants_cid_number\\\": \\\"452626198612157008\\\",\\n        \\\"insurants_cid_type\\\": \\\"01\\\",\\n        \\\"insurants_email\\\": \\\"shuai@126.com\\\",\\n        \\\"insurants_insuredFlag\\\": \\\"2\\\",\\n        \\\"insurants_insuredType\\\": \\\"2\\\",\\n        \\\"insurants_mobile\\\": \\\"13601228991\\\",\\n        \\\"insurants_name\\\": \\\"小明\\\",\\n        \\\"insurants_sex\\\": \\\"2\\\",\\n        \\\"intermediarycode\\\": \\\"010000000000600213\\\",\\n        \\\"issueDate\\\": \\\"2017-07-19 00:00:00\\\",\\n        \\\"kindList\\\": [\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015008\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任一\\\"\\n            },\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015009\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任二\\\"\\n            },\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015010\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任三\\\"\\n            }\\n        ],\\n        \\\"payMoney\\\": \\\"1\\\",\\n        \\\"payTime\\\": \\\"1491408000000\\\",\\n        \\\"payWayId\\\": \\\"212\\\",\\n        \\\"payaccount\\\": \\\"0000\\\",\\n        \\\"premium\\\": \\\"1\\\",\\n        \\\"relatedperson\\\": \\\"01\\\",\\n        \\\"salesmanCode\\\": \\\"S133011004\\\",\\n        \\\"solutionCode\\\": \\\"0100000000006002130001\\\",\\n        \\\"startDate\\\": \\\"2017-07-20 00:00:00\\\"\\n    }\",\"coop_id\":\"BOSHENG_CL\",\"logger_info\":\"(102332266112-BOSHENG_CL)\",\"apply_content\":{\"fromId\":\"63767\",\"startDate\":\"2017-07-20 00:00:00\",\"FieldGQ\":\"1\",\"holder_name\":\"北京慈记网络科技有限公司\",\"FieldGR\":\"800000117020112078456\",\"channelTip\":\"01010106\",\"FieldGO\":\"2017-08-29\",\"FieldGP\":\"0.01\",\"holder_insuredFlag\":\"1\",\"FieldGM\":\"2\",\"FieldGN\":\"1986-12-15\",\"holder_email\":\"shuai@126.com\",\"endDate\":\"2018-07-19 23:59:59\",\"holder_cid_type\":\"2\",\"payTime\":\"1491408000000\",\"salesmanCode\":\"S133011004\",\"holder_cid_number\":\"452101197107101338\",\"solutionCode\":\"0100000000006002130001\",\"amount\":\"300000\",\"payaccount\":\"0000\",\"insurants_insuredType\":\"2\",\"insurants_cid_number\":\"452626198612157008\",\"insurants_name\":\"小明\",\"insurants_insuredFlag\":\"2\",\"insurants_sex\":\"2\",\"holder_mobile\":\"13601228991\",\"businessChannel\":\"05\",\"holder_insuredType\":\"2\",\"premium\":\"1\",\"holder_contactName\":\"初光磊\",\"insurants_birthday\":\"1986-12-15\",\"payWayId\":\"212\",\"FieldAD\":\"0\",\"issueDate\":\"2017-07-19 00:00:00\",\"insurants_cid_type\":\"01\",\"FieldAA\":\"1015A00201\",\"kindList\":[{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任一\",\"kindCode\":\"1015008\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任二\",\"kindCode\":\"1015009\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任三\",\"kindCode\":\"1015010\"}],\"intermediarycode\":\"010000000000600213\",\"relatedperson\":\"01\",\"payMoney\":\"1\",\"comboId\":\"1015A00201\",\"insurants_mobile\":\"13601228991\",\"insurants_email\":\"shuai@126.com\",\"FieldGJ\":\"王钿\",\"FieldGL\":\"340823198612159008\",\"FieldGK\":\"01\"},\"sign_type\":\"md5\",\"charset\":\"utf-8\",\"product_type\":\"1015A002\",\"format\":\"json\",\"serial_no\":\"102332266112\",\"version\":\"1.0\",\"CHANNEL_KEY\":\"1234567890ABCDEF\",\"timestamp\":\"\",\"sign\":\"9aa166905ee762652a5e989a1c3\",\"flowList\":\"F_CD_1015A002\",\"sysCharset\":\"utf-8\"}";
		String str = "{\"service_id\":\"02\",\"messageBody\":\"{\\n        \\\"FieldAA\\\": \\\"1015A00201\\\",\\n        \\\"FieldAD\\\": \\\"0\\\",\\n        \\\"FieldGJ\\\": \\\"王钿\\\",\\n        \\\"FieldGK\\\": \\\"01\\\",\\n        \\\"FieldGL\\\": \\\"340823198612159008\\\",\\n        \\\"FieldGM\\\": \\\"2\\\",\\n        \\\"FieldGN\\\": \\\"1986-12-15\\\",\\n        \\\"FieldGO\\\": \\\"2017-08-29\\\",\\n        \\\"FieldGP\\\": \\\"0.01\\\",\\n        \\\"FieldGQ\\\": \\\"1\\\",\\n        \\\"FieldGR\\\": \\\"800000117020112078456\\\",\\n        \\\"amount\\\": \\\"300000\\\",\\n        \\\"businessChannel\\\": \\\"05\\\",\\n        \\\"channelTip\\\": \\\"01010106\\\",\\n        \\\"comboId\\\": \\\"1015A00201\\\",\\n        \\\"endDate\\\": \\\"2018-07-19 23:59:59\\\",\\n        \\\"fromId\\\": \\\"63767\\\",\\n        \\\"holder_cid_number\\\": \\\"452101197107101338\\\",\\n        \\\"holder_cid_type\\\": \\\"2\\\",\\n        \\\"holder_contactName\\\": \\\"初光磊\\\",\\n        \\\"holder_email\\\": \\\"shuai@126.com\\\",\\n        \\\"holder_insuredFlag\\\": \\\"1\\\",\\n        \\\"holder_insuredType\\\": \\\"2\\\",\\n        \\\"holder_mobile\\\": \\\"13601228991\\\",\\n        \\\"holder_name\\\": \\\"北京慈记网络科技有限公司\\\",\\n        \\\"insurants_birthday\\\": \\\"1986-12-15\\\",\\n        \\\"insurants_cid_number\\\": \\\"452626198612157008\\\",\\n        \\\"insurants_cid_type\\\": \\\"01\\\",\\n        \\\"insurants_email\\\": \\\"shuai@126.com\\\",\\n        \\\"insurants_insuredFlag\\\": \\\"2\\\",\\n        \\\"insurants_insuredType\\\": \\\"2\\\",\\n        \\\"insurants_mobile\\\": \\\"13601228991\\\",\\n        \\\"insurants_name\\\": \\\"小明\\\",\\n        \\\"insurants_sex\\\": \\\"2\\\",\\n        \\\"intermediarycode\\\": \\\"010000000000600213\\\",\\n        \\\"issueDate\\\": \\\"2017-07-19 00:00:00\\\",\\n        \\\"kindList\\\": [\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015008\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任一\\\"\\n            },\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015009\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任二\\\"\\n            },\\n            {\\n                \\\"amount\\\": \\\"amount\\\",\\n                \\\"kindCode\\\": \\\"1015010\\\",\\n                \\\"kindName\\\": \\\"母婴特定疾病综合医疗保险责任三\\\"\\n            }\\n        ],\\n        \\\"payMoney\\\": \\\"1\\\",\\n        \\\"payTime\\\": \\\"1491408000000\\\",\\n        \\\"payWayId\\\": \\\"212\\\",\\n        \\\"payaccount\\\": \\\"0000\\\",\\n        \\\"premium\\\": \\\"1\\\",\\n        \\\"relatedperson\\\": \\\"01\\\",\\n        \\\"salesmanCode\\\": \\\"S133011004\\\",\\n        \\\"solutionCode\\\": \\\"0100000000006002130001\\\",\\n        \\\"startDate\\\": \\\"2017-07-20 00:00:00\\\"\\n    }\",\"coop_id\":\"BOSHENG_CL\",\"logger_info\":\"(102332266112-BOSHENG_CL)\",\"apply_content\":{\"fromId\":\"63767\",\"startDate\":\"2017-07-20 00:00:00\",\"FieldGQ\":\"1\",\"holder_name\":\"北京慈记网络科技有限公司\",\"FieldGR\":\"800000117020112078456\",\"channelTip\":\"01010106\",\"FieldGO\":\"2017-08-29\",\"FieldGP\":\"0.01\",\"holder_insuredFlag\":\"1\",\"FieldGM\":\"2\",\"FieldGN\":\"1986-12-15\",\"holder_email\":\"shuai@126.com\",\"endDate\":\"2018-07-19 23:59:59\",\"holder_cid_type\":\"2\",\"payTime\":\"1491408000000\",\"salesmanCode\":\"S133011004\",\"holder_cid_number\":\"452101197107101338\",\"solutionCode\":\"0100000000006002130001\",\"amount\":\"300000\",\"payaccount\":\"0000\",\"insurants_insuredType\":\"2\",\"insurants_cid_number\":\"452626198612157008\",\"insurants_name\":\"小明\",\"insurants_insuredFlag\":\"2\",\"insurants_sex\":\"2\",\"holder_mobile\":\"13601228991\",\"businessChannel\":\"05\",\"holder_insuredType\":\"2\",\"premium\":\"1\",\"holder_contactName\":\"初光磊\",\"insurants_birthday\":\"1986-12-15\",\"payWayId\":\"212\",\"FieldAD\":\"0\",\"issueDate\":\"2017-07-19 00:00:00\",\"insurants_cid_type\":\"01\",\"FieldAA\":\"1015A00201\",\"kindList\":[{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任一\",\"kindCode\":\"1015008\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任二\",\"kindCode\":\"1015009\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任三\",\"kindCode\":\"1015010\"}],\"intermediarycode\":\"010000000000600213\",\"relatedperson\":\"01\",\"payMoney\":\"1\",\"comboId\":\"1015A00201\",\"insurants_mobile\":\"13601228991\",\"insurants_email\":\"shuai@126.com\",\"FieldGJ\":\"王钿\",\"FieldGL\":\"340823198612159008\",\"FieldGK\":\"01\"},\"sign_type\":\"md5\",\"charset\":\"utf-8\",\"F_CD_1015A0021\":{\"service_id\":\"02\",\"sign\":\"9aa166905ee762652a5e989a1c3\",\"timestamp\":\"\",\"coop_id\":\"BOSHENG_CL\",\"apply_content\":{\"fromId\":\"63767\",\"startDate\":\"2017-07-20 00:00:00\",\"FieldGQ\":\"1\",\"FieldGR\":\"800000117020112078456\",\"holder_name\":\"北京慈记网络科技有限公司\",\"channelTip\":\"01010106\",\"FieldGO\":\"2017-08-29\",\"FieldGP\":\"0.01\",\"holder_insuredFlag\":\"1\",\"FieldGM\":\"2\",\"FieldGN\":\"1986-12-15\",\"holder_email\":\"shuai@126.com\",\"endDate\":\"2018-07-19 23:59:59\",\"holder_cid_type\":\"2\",\"payTime\":\"1491408000000\",\"salesmanCode\":\"S133011004\",\"holder_cid_number\":\"452101197107101338\",\"solutionCode\":\"0100000000006002130001\",\"amount\":\"300000\",\"payaccount\":\"0000\",\"insurants_insuredType\":\"2\",\"insurants_cid_number\":\"452626198612157008\",\"insurants_name\":\"小明\",\"insurants_insuredFlag\":\"2\",\"insurants_sex\":\"2\",\"holder_mobile\":\"13601228991\",\"businessChannel\":\"05\",\"holder_insuredType\":\"2\",\"premium\":\"1\",\"payWayId\":\"212\",\"holder_contactName\":\"初光磊\",\"insurants_birthday\":\"1986-12-15\",\"issueDate\":\"2017-07-19 00:00:00\",\"FieldAD\":\"0\",\"insurants_cid_type\":\"01\",\"kindList\":[{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任一\",\"kindCode\":\"1015008\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任二\",\"kindCode\":\"1015009\"},{\"amount\":\"amount\",\"kindName\":\"母婴特定疾病综合医疗保险责任三\",\"kindCode\":\"1015010\"}],\"FieldAA\":\"1015A00201\",\"intermediarycode\":\"010000000000600213\",\"relatedperson\":\"01\",\"payMoney\":\"1\",\"insurants_mobile\":\"13601228991\",\"comboId\":\"1015A00201\",\"insurants_email\":\"shuai@126.com\",\"FieldGJ\":\"王钿\",\"FieldGL\":\"340823198612159008\",\"FieldGK\":\"01\"},\"sign_type\":\"md5\",\"charset\":\"utf-8\",\"product_type\":\"1015A002\",\"format\":\"json\",\"serial_no\":\"102332266112\",\"version\":\"1.0\"},\"product_type\":\"1015A002\",\"format\":\"json\",\"serial_no\":\"102332266112\",\"version\":\"1.0\",\"CHANNEL_KEY\":\"1234567890ABCDEF\",\"timestamp\":\"\",\"sign\":\"9aa166905ee762652a5e989a1c3\",\"flowList\":\"F_CD_1015A002\",\"sysCharset\":\"utf-8\"}";
		
		JSONObject obj = JSONObject.parseObject(str);
		JSONArray applycontent = obj.getJSONObject("apply_content").getJSONArray("kindList");
		
		String str2 = (String)obj.get("messageBody");
		JSONObject obj2 = JSONObject.parseObject(str2);
		
		System.out.println(obj2);
		
		JSONArray arr = new JSONArray();
		Map map1 = new HashMap();
		map1.put("amounta", "2");
		map1.put("kindName", "责任一");
		
		Map map2 = new HashMap();
		map2.put("amounta", "2");
		map2.put("kindName", "责任二");
		
		arr.add(map1);
		arr.add(map2);
		
		System.out.println(applycontent);
		System.out.println(obj);

		obj.getJSONObject("apply_content").put("kindList", arr);
		System.out.println(obj);
		
		JSONArray arr2 = new JSONArray();
		Map map3 = new HashMap();
		map3.put("amounta", "2");
		map3.put("kindName", "责任一");
		
		Map map4 = new HashMap();
		map4.put("amounta", "2");
		map4.put("kindName", "责任二");
		
		arr2.add(map3);
		arr2.add(map4);
		
		obj.getJSONObject("F_CD_1015A0021").getJSONObject("apply_content").put("kindList", arr2);
		System.out.println(obj);
		
		JSONArray arr3 = new JSONArray();
		Map map5 = new HashMap();
		map5.put("amounta", "2");
		map5.put("kindName", "责任一");
		
		Map map6 = new HashMap();
		map6.put("amounta", "2");
		map6.put("kindName", "责任二");
		
		arr3.add(map5);
		arr3.add(map6);
		
		obj2.put("kindList", arr3);
		obj.put("messageBody", obj2.toString());
		System.out.println(obj);
		
		Map<String, Object> map = new HashMap<String, Object>();
//		String s = obj.getJSONArray("apply_content").toString();
		map.put("apply_content", obj.getJSONObject("apply_content"));
		System.out.println(map);
	}
	
	
//	@Test
	public void splet5()
	{
		Te t = new Te();
		t.print1();
		
		System.out.println("");
	}
	
	
//	@Test
	public void splet4() throws UnsupportedEncodingException
	{
		String solvencyRatio = "A3A0217%3A+%D5%CB%BB%A7%CD%B8%D6%A7";
		String a = URLDecoder.decode(solvencyRatio, "GBK");
		
		System.out.println(a);
	}
	
//	@Test
	public void splet3() throws UnsupportedEncodingException
	{
		String solvencyRatio = "240.19|240.19|A";
		String arr[] = solvencyRatio.split("\\|");
		
		System.out.println(arr);
	}
	
//	@Test
	public void splet2() throws UnsupportedEncodingException
	{
		String str = "2017年05月17日";
		System.out.println(str);
		
		str = new String(str.getBytes("UTF-8"), "GBK");
		
		System.out.println(str);
	}
	
//	@Test
	public void splet()
	{
		String str = "a|b|c|222.000|2.33";
		String[] accountArr = str.split("\\|");
		System.out.println(accountArr.toString());
	}
	
//	@Test
	public void getTime()
	{
//		String str = "2017-02-16";
		
		String str = "3FE7C23C0C8E93E723A5D69720A8DEF489872C114722F030";
		String key = "2A1C3F4G9S6S7S8";
		System.out.println(key.length());
		String value = DESUtil.decryptECB(key, str);
		System.out.println(value);
		
	}
	
//	@Test
	public void testMd5() throws UnsupportedEncodingException
	{
		String pass = "taikang321";
//		String a = Md5.getMD5Mac(pass);
//		System.out.println(a);
//		
//		pass = "taikang12";
//		a = Md5.getMD5Mac(pass);
//		System.out.println(a);
		
//		ba11100f0408be68fe789247ac3e21d9
//		3f50114e419ef184c7cab3369d17f197
//		3f50114e419ef184c7cab3369d17f197
		
		pass = "true150907024860301594256199.0%E6%B3%B0%E5%BA%B7%E6%94%AF%E4%BB%98%E5%B9%B3%E5%8F%B0%E6%94%AF%E4%BB%98%E6%88%90%E5%8A%9F72sadk23jkd4as9x";
		System.out.println(Md5.getMD5Mac(pass));
		
		pass = "true150907024860301594256199" + URLDecoder.decode("%25E6%25B3%25B0%25E5%25BA%25B7%25E6%2594%25AF%25E4%25BB%2598%25E5%25B9%25B3%25E5%258F%25B0%25E6%2594%25AF%25E4%25BB%2598%25E6%2588%2590%25E5%258A%259F", "utf8") + "72sadk23jkd4as9x";
		System.out.println(Md5.getMD5Mac(pass));
		
		pass = "true150907024860301594256199" + URLDecoder.decode(URLDecoder.decode("%25E6%25B3%25B0%25E5%25BA%25B7%25E6%2594%25AF%25E4%25BB%2598%25E5%25B9%25B3%25E5%258F%25B0%25E6%2594%25AF%25E4%25BB%2598%25E6%2588%2590%25E5%258A%259F", "utf8"),"utf8") + "72sadk23jkd4as9x";
		System.out.println(Md5.getMD5Mac(pass));
		
		pass = "true150907024860301594256199泰康支付平台支付成功72sadk23jkd4as9x";
		System.out.println(Md5.getMD5Mac(pass));
		
//		String str = URLDecoder.decode("%25E6%25B3%25B0%25E5%25BA%25B7%25E6%2594%25AF%25E4%25BB%2598%25E5%25B9%25B3%25E5%258F%25B0%25E6%2594%25AF%25E4%25BB%2598%25E6%2588%2590%25E5%258A%259F", "utf8");
//		str = URLDecoder.decode(str, "UTF-8");
//		pass = "true150907024860301594256199" + URLDecoder.decode("%25E6%25B3%25B0%25E5%25BA%25B7%25E6%2594%25AF%25E4%25BB%2598%25E5%25B9%25B3%25E5%258F%25B0%25E6%2594%25AF%25E4%25BB%2598%25E6%2588%2590%25E5%258A%259F", "utf8") + "72sadk23jkd4as9x";
//		
//		String a = Md5.getMD5Mac(pass);
//		System.out.println(a);
//		a = Md5.getMD5Mac(pass, "utf8");
//		System.out.println(a);
	}
	
//	@Test
	public void testSubstr()
	{
		Integer a = new Integer(1);
		Integer b = new Integer(1);
		System.out.println(a==b);
		
		Integer c = 201;
		Integer d = 201;
		System.out.println(c==d);
		System.out.println(c.intValue() == d.intValue());
		
		System.out.println(c);
		
		changer(a);
		System.out.println(a);
		
		String one = "123456";
		String two = one.substring(3);
//		System.out.println(two);
		assertEquals("456", two);
	}
	
	private void changer(Integer j) 
	{
		
	}
	
//	@Test
	public void testMultiply()
	{
		int one = 2 * 3;
		assertEquals(6, one);
	}
	
//	@Test
	public void test1()
	{
		String str = "omCIGj4_UjZYQMNOdJ_k-uZ6ov_c";
		System.out.println("length:" + str.length());
		str = "omCIGj38Ehz2kb82DWSLhXqzCNrk";
		System.out.println("length2:" + str.length());
		
	}
	
//	@Test
	public void test2()
	{
		String a = Static1.getParm();
		String b = Static2.getParm();
		System.out.println(a + "|" + b);
		Static2.setParm("ccc");
		b = Static2.getParm();
		System.out.println(a + "|" + b);
	}

//	@Test
	public void test3()
	{
//		String mobile_md5 = ibsmac.getMD5Mac(mobile);
		String aa = "{\"code\":\"200\",\"msg\":\"success\",\"result\":{\"resultList\":[{\"claimAmount\":\"\",\"handletext\":\"\",\"policyNo\":\"\",\"reportDate\":\"\"}]}}";
		Object o = handleProcessReturn(aa);
		System.out.println(o.toString());
		
	}
//	@Test
	public void test4()
	{
//		省-市-县/区-动态标的-手机号
		String addr = "北京市_&海淀区_&人民医院_&1025525";
		String as[] = addr.split("_&");
		for(int i = 0; i < as.length; i++)
		System.out.println(as[i]);
	}
	
//	@Test
	public void test5()
	{
		String aa = "{\"amount\":1290000,\"businessChannel\":\"05\",\"comboId\":\"1702Q101\",\"premium\":100,\"programType\":\"1\",\"riskKind\":[{\"comboId\":\"1702Q101\",\"itemdetailcode\":\"010058\",\"kindAmount\":200000,\"kindCode\":\"1702001\",\"kindName\":\"泰康在线家庭财产综合险\",\"kindPremium\":14.66,\"kindRate\":0.073307},{\"comboId\":\"1702Q101\",\"itemdetailcode\":\"010059\",\"kindAmount\":50000,\"kindCode\":\"1702001\",\"kindName\":\"泰康在线家庭财产综合险\",\"kindPremium\":5.5,\"kindRate\":0.10996},{\"comboId\":\"1702Q101\",\"itemdetailcode\":\"010061\",\"kindAmount\":1000000,\"kindCode\":\"1702001\",\"kindName\":\"泰康在线家庭财产综合险\",\"kindPremium\":71.84,\"kindRate\":0.071841},{\"comboId\":\"1702Q101\",\"itemdetailcode\":\"0\",\"kindAmount\":30000,\"kindCode\":\"1702027\",\"kindName\":\"附加燃气意外伤害保险\",\"kindPremium\":3,\"kindRate\":0.1},{\"comboId\":\"1702Q101\",\"itemdetailcode\":\"0\",\"kindAmount\":10000,\"kindCode\":\"1702028\",\"kindName\":\"附加燃气责任保险\",\"kindPremium\":5,\"kindRate\":0.5}],\"status\":\"1\",\"unvalidTime\":253386374400000,\"validTime\":1464710400000}";
		System.out.println(aa);
	}
	
	
	
	private Object handleProcessReturn(String returnMsg)
	{
		JSONArray arr = null;
		JSONObject backInfo = JSONObject.parseObject(returnMsg);
		String queryStatus = backInfo.getString("code");
		queryStatus = queryStatus.trim();
		if (queryStatus != null && "200".equals(queryStatus))
		{
			arr = (JSONArray) backInfo.getJSONObject("result").get("resultList");
			if(arr != null && arr.size() > 0)
			{
				String firstProcesspolicyNo = arr.getJSONObject(0).getString("policyNo");
				if(firstProcesspolicyNo == null || "".equals(firstProcesspolicyNo))
					return "";
				int length = arr.size();
				long time ;
				String applydate = "";
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				for(int i = 0; i < length; i++)
				{
					time = arr.getJSONObject(i).getLongValue("reportDate");
					applydate = sdf.format(new Date(time));
					arr.getJSONObject(i).put("reportDate", applydate);
				}
			}
		}else
		{
			String msg = backInfo.getString("msg");
		}
		return arr;
	}
	
	
	
}
