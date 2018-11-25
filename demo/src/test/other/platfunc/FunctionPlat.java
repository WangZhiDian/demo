package test.other.platfunc;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;

import test.other.platkey.CreateKey;

public class FunctionPlat
{

    public static void  main(String[] args) {  
    	CreateKey test = new CreateKey();  
        //测试  
    	System.out.println("---");
    	FunctionPlat a = new FunctionPlat();
//    	a.funcHandle();
    	a.test();
    }  
    
	public void test()
	{
		Date d = new Date(-571910400000L);
		System.out.println(d.toString());
	}
	
	public void funcHandle()  {
		// TODO Auto-generated method stub
		Map<String,Object> reMap = new HashMap<String,Object>();
		String charSet2 = "utf-8";
		String charSet = "GBK";
		
		//md5加密key

		String channelKey = "6CUHa0Pl94v1Y1OW810QIw7Tbvm280M27Zh28Y6Suo7Qgu4058434t8sV82V7E55Z02aG4Oz2m4bz68896v58sL692So835XksZ08htNpE2MJamq0764Mo36sC91o54T";
//		String content = "{\"identifyNumber\":\"341621199307300126\",\"policyNo\":\"600051109201610000125327126\",\"endorDate\":1479289405817,\"tradeId\":\"147927846941910378595\",\"holder_name\":\"黄倩男\"}";
		String content = "{\"insurants_birthday\":743961600000,\"kindlist\":[{\"amount\":9000,\"kindCode\":\"1109005\",\"premium\":50},{\"amount\":100000,\"kindCode\":\"1109001\",\"premium\":20},{\"amount\":100000,\"kindCode\":\"1109002\",\"premium\":20},{\"amount\":10000,\"kindCode\":\"1109003\",\"premium\":40},{\"amount\":10000,\"kindCode\":\"1109004\",\"premium\":105}],\"comboid\":\"1109A00901\",\"holder_cid_number\":\"341621199307300126\",\"insurants_name\":\"黄倩男\",\"insurants_email\":\"1351574711@qq.com\",\"relatedperson\":\"01\",\"holder_cid_type\":\"01\",\"fromId\":\"60225\",\"holder_email\":\"1351574711@qq.com\",\"holder_sex\":\"2\",\"insuresubjectList\":[{\"fieldAg\":\"611000\",\"fieldAl\":\"610000\"}],\"insurants_sex\":\"2\",\"insurants_cid_number\":\"341621199307300126\",\"holder_mobile\":\"15600173350\",\"holder_birthday\":743961600000,\"issueDate\":1479278468666,\"holder_name\":\"黄倩男\",\"insurants_cid_type\":\"01\",\"insurants_mobile\":\"15600173350\"}";
		
		byte[] keyJson = null;
		try {
			keyJson = IOUtils.toByteArray(IOUtils.toInputStream(channelKey + content,charSet2));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultStr = DigestUtils.md5DigestAsHex(keyJson);

		System.out.println("-----------------生成的签名--------------:"+resultStr);
		System.out.println("-----------------报文中签名--------------:"+"df451baba1353d27d9cdd06624faeb1b");

		
	}
    
	private String bintoascii(byte[] bySourceByte) {
		int len, i;
		byte tb;
		char high, tmp, low;
		String result = new String();
		len = bySourceByte.length;
		for (i = 0; i < len; i++) {
			tb = bySourceByte[i];

			tmp = (char) ((tb >>> 4) & 0x000f);
			if (tmp >= 10)
				high = (char) ('a' + tmp - 10);
			else
				high = (char) ('0' + tmp);
			result += high;
			tmp = (char) (tb & 0x000f);
			if (tmp >= 10)
				low = (char) ('a' + tmp - 10);
			else
				low = (char) ('0' + tmp);

			result += low;

		}
		return result;
	}
	
	private  String toHex(byte input[])
	    {
	        if(input == null)
	            return null;
	        StringBuffer output = new StringBuffer(input.length * 2);
	        for(int i = 0; i < input.length; i++)
	        {
	            int current = input[i] & 0xff;
	            if(current < 16)
	                output.append("0");
	            output.append(Integer.toString(current, 16));
	        }

	        return output.toString();
	    }
}
