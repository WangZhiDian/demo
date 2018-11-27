package cn.model.maven.commons.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Slf4j
public class DateUtil {
	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

	public static final String timePattern = "HH:mm:ss";
	public static final String milliTimePattern = "HH:mm:ss:SSS";
	public static final String datePattern = "yyyy-MM-dd";
	public static final String dateTimePattern = "yyyy-MM-dd HH:mm:ss";
	public static final String milliDateTimePattern = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String HHMMSS = "HHmmss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddmmssSSS";
	public static final String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
	public static final String YYYYMMWW = "yyyy-MM-WW";					//按照每月第几周切割

	/**
	 * 解析出生日期
	 * @param birthday 
	 * 支持yyyy-MM-dd，yyyy/MM/dd，yyyyMMdd三种格式
	 * @return
	 */
	public static Date parseBirthday(String birthday)
	{
		if(StringUtils.isEmpty(birthday))
			return null;
		Date customerBirthday = null;
		 try {
			if(birthday.contains("-"))
			 	customerBirthday = DateUtils.parseDate(birthday, "yyyy-MM-dd");
			 else if(birthday.contains("/"))
			 	customerBirthday = DateUtils.parseDate(birthday, "yyyy/MM/dd");
			 else
			 	customerBirthday = DateUtils.parseDate(birthday, "yyyyMMdd");
		} catch (ParseException e) {
			log.error("====parseBirthday Exception:{}===",birthday);
		}
		return customerBirthday;
	}
	
	/**
	 * 根据出生日期计算年龄
	 * @param birthday
	 * @return
	 */
	public static Integer getAgeByBirthday(Date birthday){
		Integer age = null;
		if(birthday != null){
			//当前时间
			Calendar now = Calendar.getInstance();
			//出生日期
			Calendar birth = Calendar.getInstance();
			//今年生日
			Calendar nowBirth = Calendar.getInstance();
			birth.setTime(birthday);
			nowBirth.setTime(birthday);
			int year = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
			nowBirth.add(Calendar.YEAR, year);
			if(nowBirth.after(now)){
				//还未过生日年龄减一
				age = year -1;
			} else {
				age = year;
			}
		}
		return age;
	}
	
	public static Date parseDate(String dateStr, String pattern){
		try {
			return DateUtils.parseDate(dateStr, pattern);
		} catch (ParseException e) {
			log.error("====parseDate Exception dateStr:{},pattern:{}===",dateStr,pattern);
			return null;
		}
	}
	
	public static String formatDate(Date date, String pattern){
		return DateFormatUtils.format(date, pattern);
	}

	/**
	 * 使用yyyy-MM-dd格式将字符串转换成日期
	 *
	 * @param strDate the date to convert (in format yyyy-MM-dd)
	 * @return a date object
	 *
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate)
			throws ParseException {
		Date aDate = null;

		if (log.isDebugEnabled()) {
			log.debug("converting date with pattern: " + datePattern);
		}
		aDate = convertStringToDate(strDate,datePattern);
		return aDate;
	}

	/**
	 * 根据指定格式将字符串转换为日期
	 *
	 * @param strDate 日期字符串
	 * @param aMask 输入字符的日期格式
	 * @return java.util.Date
	 * @see SimpleDateFormat
	 * @throws ParseException
	 */
	public static final Date convertStringToDate(String strDate,String aMask)
			throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat(aMask);
		Date date = null;
		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}
		date = df.parse(strDate);
		return (date);
	}

	/**
	 * 格式化当前日期为yyyy-MM-dd
	 * @return 当前日期字符串yyyy-MM-dd
	 */
	public static final String getCurrDate() {
		SimpleDateFormat df = new SimpleDateFormat(datePattern);
		Date today = new Date();
		return df.format(today);
	}

	/**
	 * 格式化当前日期为yyyy-MM-dd HH:mm:ss
	 * @return 当前日期字符串yyyy-MM-dd HH:mm:ss
	 */
	public static final String getCurrTime() {
		SimpleDateFormat df = new SimpleDateFormat(dateTimePattern);
		Date today = new Date();
		return df.format(today);
	}

	/**
	 * 获取两个日期之间的毫秒数
	 * @param start
	 * @param end
	 * @return
	 */
	public static long getSecondsBetweenDate(Date start, Date end) {
		return end.getTime() - start.getTime();
	}
	
	/**
	 * 获取指定日期到第二天0点之间的毫秒数
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static long getCurrDateEndTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);//将指定日期格式化为yyyy-MM-dd形式
		String dateStr = sdf.format(date);
		Date end = null;
		try {
			end = convertStringToDate(dateStr + " 23:59:59", dateTimePattern);//将字符串类型转换为Date类型
		} catch (ParseException e) {
			logger.error("getCurrDateEndTime Exception:",e);
		}
		return end.getTime() - date.getTime();
	}
	

	
	/**
	 * 按指定的格式sFormat将日期dDate转化为字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String date2String(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}


	/**
	 * 获取当前日期
	 * @return Date
	 */
	public static final Date getNowDate() {
		return new Date();
	}

	/**
	 * 返回指定时间 年月日时分秒
	 * @param date
	 * @return
	 */
	public static String  getTimeByCalendar(Date date) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		return year + "年" + month + "月" + day + "日 " + hour + ":" + minute + ":" +second;
	}


	/**订单失效时间判断，创建时间，失效时间（分钟）
	 * 如果当前时间在失效时间之前，返回true
	 * @param createtimedate,expiretime
	 * @return
	 */
	public static boolean getdateNextdate(Date createtimedate, String expiretime) {
		if(StringUtil.isEmpty(expiretime)){
			return false;
		}
		//当前时间
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(createtimedate);
		//得到多少分钟后的时间
		calendar.add(calendar.MINUTE, Integer.parseInt(expiretime));
		createtimedate = calendar.getTime();
		return date.before(createtimedate);
	}

	/**订单失效时间判断，创建时间，失效时间（分钟）
	 * 计算订单超时剩余时间，返回分钟
	 * @param createtimedate,expiretime
	 * @return
	 */
	public static long  ordeResidualtime(Date createtimedate, String expiretime) {
		//当前时间
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(createtimedate);
		//得到多少分钟后的时间
		calendar.add(calendar.MINUTE, Integer.parseInt(expiretime));
		createtimedate = calendar.getTime();
		long time= Math.abs(createtimedate.getTime() - date.getTime());
		return time/(1000*60);
	}

	/**
	 * 根据过期分钟数和具体格式，根据当前时间算出绝对超时时间
	 * param createtimedate
	 * param expiretime
	 * return
	 */
	public static String  residualDate(int expiretime, String format) {
		Date now = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(now);

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		calendar.add(Calendar.MINUTE, expiretime);
		String residualDateStr = sdf.format(calendar.getTime());
		return residualDateStr;
	}


	/**
	 * 校验字符串日期格式
	 * @param dateStr
	 * @return
	 */
	public static boolean validateDate(String dateStr){
		SimpleDateFormat sdf = new SimpleDateFormat(dateTimePattern);
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
			return true;
		} catch (ParseException e) {
			logger.error("validateDate Exception:",e);
		}
		return false;
	}


	/** 多少分钟后
	 * 获取当前时间加多少分钟后
	 * @param minute 分钟
	 * @return
	 */
	public static String getdateMinute(int minute) {
		//当前时间
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		//得到多少分钟后的时间
		calendar.add(calendar.MINUTE, minute);
		Date date_minute = calendar.getTime();
		SimpleDateFormat df = new SimpleDateFormat(dateTimePattern);
		return df.format(date_minute);
	}
}
