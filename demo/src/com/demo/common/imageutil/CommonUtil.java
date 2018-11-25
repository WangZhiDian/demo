package com.demo.common.imageutil;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;
import com.demo.common.util.DateFormatUtil;

//import eu.medsea.mimeutil.MimeUtil;

/**
 * @Description
 * @author 王文超
 * @time 2016年1月26日 上午9:31:22
 */
public class CommonUtil {

	public static void main(String[] args) {
//		String fullToHalf = CommonUtil.fullToHalf("ＡａＣｃ０９；；－　");
//		System.out.println(fullToHalf);
		InputStream is=null;
//		BufferedInputStream bis=null;
//		    bis = new BufferedInputStream(is);
//			boolean flag = isImage("D:\\MyDocuments\\liumf07\\桌面\\TKC0000729420170122\\a.jpg");
//			if(!flag){
//				System.out.println("图片格式有问题...");
//			}
//		isImage(new File("D:\\2\\新建文本文档 (2).jpg"));
//		if(CommonUtil.judgeDate("2017-03-02", "^\\d{4}-\\d{2}-\\d{2}$")){
//			System.out.println("y");
//		}else{
//			System.out.println("n");
//		}
//		System.out.println(getDateOfYMD("1490455079000","yyyyMMdd"));
//		isImage("D:\\MyDocuments\\liumf07\\桌面\\TKC0009109920170308\\564190_invoice.jpg");
//		File file = new File("D:\\MyDocuments\\liumf07\\桌面\\568947.pdf");
//		System.out.println(file.length()/1024/1024+":" +Integer.MAX_VALUE/1024/1024+":"+Integer.MIN_VALUE+":"+Integer.MAX_VALUE/1024/1024);
		String msg = getBase64Encode("阿萨斯134598");
		System.out.println(msg);
//		msg = getBase64Decode("JTI1RTklMjVBOSUyNUJFJTI1RTklMjVBOSUyNUI2JTI1RTglMjVBRiUyNTgxJTI1MjAzNzAxMDAy");
//		System.out.println(msg);
//		msg = CommonServiceUtil.getLicCid("JTI1RTklMjVBOSUyNUJFJTI1RTklMjVBOSUyNUI2JTI1RTglMjVBRiUyNTgxJTI1MjAzNzAxMDAy","Y");
//		System.out.println(msg);
//		try {
//			msg = URLDecoder.decode(URLDecoder.decode(CommonUtil.getBase64Decode("JTI1RTklMjVBOSUyNUJFJTI1RTklMjVBOSUyNUI2JTI1RTglMjVBRiUyNTgxJTI1MjAzNzAxMDAy"),"utf-8"),"utf-8");
//			System.out.println(msg);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		File dir = new File("D:/A");  
        File[] files = dir.listFiles();// 将文件或文件夹放入文件集
        System.out.println(files.length);
	}

	/**
	 * 判断一个字符串是否在一个数组中
	 * 
	 * @param value
	 * @param arry
	 * @return
	 */
	public static boolean isInArray(String value, String[] arry) {
		for (String str : arry) {
			if (value.equals(str)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 截取字符串中两个字符之间的字符串
	 * 
	 * @param str
	 * @param beg
	 * @param end
	 * @return
	 */
	public static String getSubString(String str, char beg, char end) {
		int begstr = str.indexOf(beg);
		int endstr = str.indexOf(end);
		return str.substring(begstr + 1, endstr);
	}

	/**
	 * 证件号后缀是四个型号
	 * 
	 * @param cid_number
	 * @return
	 */
	public static String getCidOfStar(String cid_number) {
		if (cid_number.length() > 3) {
			cid_number = cid_number.substring(0, cid_number.length() - 4) + "****";
		}
		return cid_number;
	}

	/**
	 * 手机号中间四位加星
	 * 
	 * @param mobile
	 * @return
	 */
	public static String getMobileOfStar(String mobile) {
		if (mobile != null && mobile.length() == 11) {
			String mobile1 = mobile.substring(0, 3);
			String mobile2 = mobile.substring(7);
			mobile = mobile1 + "****" + mobile2;
		}
		return mobile;
	}

	/**
	 * 去除空格或空
	 * 
	 * @param str
	 * @return
	 */
	public static String getString(String str) {
		if (str != null) {
			str = str.trim();
		} else {
			str = "";
		}
		return str;
	}

	/**
	 * base64解码
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64Decode(String str) {
		return new String(Base64.decodeBase64(str.getBytes()));
	}
	/**
	 * base64编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getBase64Encode(String str) {
		return new String(Base64.encodeBase64(str.getBytes()));
	}

	/**
	 * 生成日志文件
	 * 
	 * @param file
	 * @return
	 */
	public static String fileName(String file) {
		file = "log/claimService/" + file;// 把所有日志都保存在此目录下，以便查看
		Calendar ca = new GregorianCalendar();
		int yy = ca.get(Calendar.YEAR);
		int mm = ca.get(Calendar.MONTH) + 1;
		int dd = ca.get(Calendar.DAY_OF_MONTH);
		String filename = file + yy + mm + dd + ".log";
		return filename;
	}

	/**
	 * 获取格式化后的当前日期
	 * 
	 * @return
	 */
	public static String getDateStrOld() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String today = dfs.format(new Date());
		return today;
	}
	/**
	 * 判断是不是Date格式
	 * @param dateStr
	 * @param formatStr
	 * @return
	 */
    public static boolean isDate(String dateStr,String formatStr){
    	try {
			new SimpleDateFormat(formatStr).parse(dateStr);
			return true;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
    }
	/**
	 * @author 王文超
	 * @description 获取当前格式化的日期
	 * @time 2016年1月26日 上午9:31:04
	 * @return
	 */
	public static String getDateStr() {
		DateFormatUtil dfs = DateFormatUtil.getInstance();
		String today = dfs.format(new Date());
		return today;
	}

	/**
	 * @author 王文超
	 * @description 将时间转换为字符串
	 * @time 2016年1月26日 上午9:31:25
	 * @param date
	 * @return
	 */
	public static String getDateStr(Date date) {
		DateFormatUtil dfs = DateFormatUtil.getInstance();
		return dfs.format(date);
	}

	/**
	 * @author 王文超
	 * @description 将字符串转为时间
	 * @time 2016年1月26日 上午9:31:47
	 * @param source
	 * @return
	 * @throws ParseException
	 */
	public static Date getDate(String source) throws ParseException {
		DateFormatUtil dfs = DateFormatUtil.getInstance();
		return dfs.parse(source);
	}

	/**
	 * 不足8位的前面补0
	 * 
	 * @param str
	 * @return
	 */
	public static String get8Str(String str) {
		if (str != null) {
			int length = str.length();
			if (str.length() < 8) {
				int n = 8 - length;
				for (int i = 0; i < n; i++) {
					str = "0" + str;
				}
			} else {
				int n = 12 - length;
				for (int i = 0; i < n; i++) {
					str = "0" + str;
				}
			}
		}
		return str;
	}

	/**
	 * 判断字符串是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOfStr(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断对象是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOfObj(Object str) {
		if (str == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断List是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOfList(List list) {
		if (list == null || list.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断Map是否是空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNullOfMap(Map map) {
		if (map == null || map.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 手机号校验
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean validateMobile(String mobile) {
		boolean flag = true;
		if (mobile == null || mobile.length() != 11) {
			flag = false;
		}
		mobile = mobile.trim();
		String test = "^1[35478][0-9]{9}$";
		if (!mobile.matches(test)) {
			System.out.println("电话校验失败,手机号是" + mobile);
			flag = false;
		}
		return flag;
	}

	/**
	 * 座机校验
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean validatePhone(String phone) {
		boolean flag = true;
		if (phone == null || phone.length() != 11 || phone.length() != 12) {
			flag = false;
		}
		phone = phone.trim();
		String test = "^0[0-9]{2,3}[0-9]{7,8}$";
		if (!phone.matches(test)) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 手机号或者是座机校验
	 * 
	 * @param mobile
	 * @return
	 */
	public static boolean validateMobileAndPhone(String mobPho) {
		boolean flag = true;
		if (mobPho == null) {
			flag = false;
		}
		mobPho = mobPho.trim();
		String test = "^((0[0-9]{2,3}-[0-9]{7,8})|1[35478][0-9]{9})$";
		if (!mobPho.matches(test)) {
			flag = false;
		}
		return flag;
	}

	// 邮箱校验
	public static boolean validateEmail(String email) {
		boolean flag = false;
		String check = "^([a-z0-9A-Z]+[_-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		try {
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * map转化为jsonstr
	 * 
	 * @param map
	 * @return
	 */
	public static String map2Json(Map<String, Object> map) {
		if (map != null) {
//			JSONObject jsonObject = JSONObject.fromObject(map);
			JSONObject jsonObject = JSONObject.parseObject(map.toString());
			
//			System.out.println(jsonObject.toString());
			return jsonObject.toString();
		} else {
			return null;
		}
	}

	/**
	 * map转化为jsonstr
	 * 
	 * @param map
	 * @return
	 */
	public static String mapTJson(Map<String, String> map) {
		if (map != null) {
//			JSONObject jsonObject = JSONObject.fromObject(map);
			JSONObject jsonObject = JSONObject.parseObject(map.toString());
			return jsonObject.toString();
		} else {
			return null;
		}
	}

	/**
	 * 将全角转换成半角
	 * 
	 * @param str
	 * @return
	 */
	public static String fullToHalf(String input) {
		char c[] = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);

			}
		}
		String returnString = new String(c);

		return returnString;
	}

	/**
	 * 对日期进行校验
	 * 
	 * @param dateStr
	 *            matchStr正则表达式
	 * @return
	 */
	public static boolean judgeDate(String dateStr, String matchStr) {
		boolean flag = true;
		if (!dateStr.matches(matchStr)) {
			flag = false;
		}
		return flag;
	}

	/**
	 * 暂一次性理赔500张
	 * 
	 * @param map
	 * @return
	 */
	public static boolean outOfimgs(Map<String, String> map, int index) {
		if (map.keySet().size() >= index) {
			return false;
		}
		return true;

	}

	/**
	 * 获取返回报文
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static String getResultMap(String result) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNullOfStr(result)) {
			map.put("success", "N");
			map.put("desc", result);
		} else {
			map.put("success", "Y");
			map.put("desc", "");
		}
		return map2Json(map);
	}
	/**
	 * 获取返回报文
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static String getResultCode(String code,String result) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNullOfStr(result)) {
			map.put("success", "N");
			map.put("code", code);//500开头编码
			map.put("desc", result);//logkey:业务标识；logdesc:文字原因
			map.put("time", getDateStrOld());
		} else {
			map.put("success", "Y");
			map.put("code", "200");
			map.put("desc", "");
			map.put("time", getDateStrOld());
		}
		return map2Json(map);
	}

	/**
	 * 
	 * @param key
	 * @param result
	 * @param desc
	 * @return
	 */
	public static String getResultCode(String key,String result,String desc) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (!CommonUtil.isNullOfStr(desc)) {
			map.put("success", "N");
			map.put("desc", desc);//logkey:业务标识；logdesc:文字原因
			map.put("flag", "1");
		} else {
			map.put("success", "Y");
			map.put("desc", "");
			map.put(key, result);
		}
		return map2Json(map);
	}

	/**
	 * 
	 * @param key
	 * @param result
	 * @param desc
	 * @return
	 */
//	public static String getResultAccountDetail(String key,JSONArray fromObject,String desc) {
//		Map<String, Object> map = new HashMap<String, Object>();
//		if (!CommonUtil.isNullOfStr(desc)) {
//			map.put("success", "N");
//			map.put("desc", desc);//logkey:业务标识；logdesc:文字原因
//		} else {
//			map.put("success", "Y");
//			map.put("desc", "");
//			map.put(key, fromObject);
//		}
//		return map2Json(map);
//	}
	
	
	/**
	 * 获取返回报文
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static String getResultMap_json(String result) {
		if(!CommonUtil.isNullOfStr(result)&&result.contains("{")){
			return result;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		if (!isNullOfStr(result)) {
			map.put("success", "N");
			map.put("desc", result);
		} else {
			map.put("success", "Y");
			map.put("desc", "");
		}
		return map2Json(map);
	}
	/**
	 * 获取返回报文
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static String getResultMap_info(Object object) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", "Y");
		map.put("desc", "");
		map.put("info", object);
		return map2Json(map);
	}

	/**
	 * 返回特定描述的结果
	 * 
	 * @param flag
	 * @param result
	 * @return
	 */
	public static String getResultMap(String flag, String result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", flag);
		map.put("desc", result);
		System.out.println(result);
		return map2Json(map);
	}
	/**
	 * 组合返回结果
	 * @param result
	 * @param str
	 * @return
	 */
	public static String getAddResult(String result,Object str){
		if(CommonUtil.isNullOfStr(result)){
			return CommonUtil.getResultMap_info(str);
		}else{
			return CommonUtil.getResultMap(result);
		}
	}
	/**
	 * 获取某个节点的值
	 * 
	 * @param document
	 * @param nodeName
	 * @return
	 */
	public static String getText(Element document, String nodeName) {
		return getString(document.selectSingleNode(nodeName).getText());
	}
	/**
	 * 判断下载的图片是不是合法的
	 * @param bis
	 * @return
	 */
//	public static boolean checkMap(String fileName){
//		byte[] bytes;
//		boolean flag = false;
//		InputStream bis = null;
//		try {
//			File file = new File(fileName);
//		    bis = new FileInputStream(file);
//			bytes = new byte[bis.available()];
//			bis.read(bytes);//注意内存
//		    MimeUtil util = new MimeUtil();
//		    MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
//		    Collection co = util.getMimeTypes(bytes);
//		    String contType = co.toString();
//		    String type = contType.split("/")[1];
//		    System.out.println("fileName="+fileName+";type:"+type);
//		    String[] types = {"gif","jpeg","bmp","png","x-png","x-ms-bmp","jpg"};
//		    for(String allowType : types){
//		    	if(type.equals(allowType)){
//		    		flag = true;
//		    	}
//		    }
//		} catch (IOException e) {
//			e.printStackTrace();
//		}finally{
//			try{
//				bis.close();
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
//	    return flag;
//	}
	/**
	 * 判断是否可以获取到文件的宽高
	 * @param imageFile
	 * @return
	 */
   public static boolean isImage(String fileName) {
	   File imageFile = new File(fileName);
        if (!imageFile.exists()) {
            return false;
        }
        Image img = null;
        try {
            img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
            	return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            img = null;
        }
    }
   /**
    * 通过时间毫秒数获取yyyyMMdd格式的时间
    * @param longStr
    * @return
    */
    public static String getDateOfYMD(String longStr,String formatStr){
    	SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
    	return sdf.format(new Date(new Long(longStr)));
    }
    public static String getRandomStr(String key){
    	String[] randomStr = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
    			"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        int length = randomStr.length;
        Random rd = new Random();
        StringBuffer sb = new StringBuffer("");
        for(int i=0;i<6;i++){
        	int index = rd.nextInt(length);
        	sb.append(randomStr[index]);
        }
        String str = sb.toString();//随机值
        List<String> list = (List<String>)CommonParamUtil.rm.getValue(key+"_tkc");
        if(list==null){
        	list = new ArrayList<String>();
        }
        for(String s : list){
        	if(str.equals(s)){
        		getRandomStr(key);
        	}
        }
        list.add(str);
        CommonParamUtil.rm.setValue(key+"_tkc", list);
    	return str;
    }
    /**
     * 判断目录下是否有文件
     * @param filePath
     * @return
     */
    public static boolean haveFiles(String filePath){
    	File dir = new File(filePath);
    	File[] files =dir.listFiles();
    	if(isNullOfObj(dir)||files.length<1){
    		return false;
    	}
    	return true;
    }
    /**
	 * 入参访问缓存限制
	 * @param key
	 * @param seconds
	 * @return
	 */
	public static boolean isControlled(String key,int seconds){
		boolean flag = true;
		String rmStr = (String) CommonParamUtil.rmc.getValue(key);
		if(CommonUtil.isNullOfStr(rmStr)){
			CommonParamUtil.rmc.setValue(key, key,seconds);
		}else{
			return flag = false;
		}
		return flag;
		
	}
	/**
	 * 获取格式化后的当前日期
	 * 
	 * @return
	 */
	public static String getDateStrOld(Date date,String formatStr) {
		return new SimpleDateFormat(formatStr).format(date);
	}
	/**
	 * 把结果数组转化成map
	 * key和value以:分隔作为入参数组的一个值
	 * @param results
	 * @return
	 */
	public static Map<String,Object> getResultMap(String[] results){
		String[] array = null;
		Map<String,Object> map = new HashMap<String,Object>();
		for(String keyValue : results){
			array = keyValue.split(":");
			map.put(array[0], array[1]);
		}
		return map;
	}
	/**
	 * 把结果数组转化成map
	 * key和value以逗号分隔作为入参数组的一个值
	 * 最终把map转化为json字符串
	 * @param results
	 * @return
	 */
	public static String getResultJson(String[] results){
		return map2Json(getResultMap(results));
	}

	/**
	 * 获取返回报文
	 * 
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public static String getResultFace(String result,String step) {
		Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", result);
			map.put("step", step);
		return map2Json(map);
	}
	/**
	 * 组织servlet的出参合入参
	 * @param desc
	 * @param result
	 * @param request
	 * @param response
	 */
//	public static void getResult(String desc,String result,HttpServletRequest request, HttpServletResponse response){
//		CommonServiceUtil.getParamLog(desc+"入参", request);
//		CommonParamUtil.common_log.debug("{}出参{}",desc,result);
//		try {
//			response.getWriter().print(result);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
