package test.demo.first;

import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * @author zhaopuqing
 * @created 2015年11月5日 下午2:10:15
 */
public class DateUtil {
	
	/**
	 * 格式化时间
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String toString(Date date,String pattern){
		return new DateTime(date.getTime()).toString(pattern);
	}
	public static Date toDate(String time,String pattern)
	{
		return DateTime.parse(time,DateTimeFormat.forPattern(pattern)).toDate();
	}
	/**
	 * 获取给定日期的凌晨零点零分的日期对象
	 * @param date
	 * @return
	 */
	public static Date getTimeOf12(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * 获取给定日期下个月的第一天凌晨零点零分的日期对象
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfNextMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	/**
	 * 获取给定日期所在周的第一天的凌晨零点零分的日期对象
	 * @param date
	 * @param dayNum
	 * @return
	 */
	public static Date getFirstDateOfWeek(Date date ,int dayNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	
	/**
	 * 获取给定日期所属月份的第一天凌晨零点零分的日期对象
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	/**
	 * 获取加减日期后的凌晨零点零分的日期对象
	 * @param date
	 * @param dayNum
	 * @return
	 */
	public static Date changedByDateNum(Date date ,int dayNum){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, dayNum);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * 判断给定的两个日期是否是同一天
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);

		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2
						.get(Calendar.DAY_OF_YEAR);
	}
	

	/**
	 * 将指定格式的日期字符串解析成Date类型
	 * 
	 * @param dateStr
	 *            格式为：yyyy-MM-dd
	 * @return
	 */
	public static Date parse(String dateStr) {
		if (dateStr == null) {
			return null;
		}
		String[] split = dateStr.split("-");

		String errorInfo = "合法的格式是：yyyy-MM-dd";
		if (split.length != 3) {
			throw new IllegalArgumentException(errorInfo);
		}
		Calendar result = Calendar.getInstance();

		int year = 0;
		try {
			year = Integer.valueOf(split[0]);
			result.set(Calendar.YEAR, year);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(errorInfo);
		}

		int month = 0;
		try {
			month = Integer.valueOf(split[1]);
			result.set(Calendar.MONTH, month - 1);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(errorInfo);
		}

		int day = 0;
		try {
			day = Integer.valueOf(split[2]);
			result.set(Calendar.DAY_OF_MONTH, day);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(errorInfo);
		}
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result.getTime();

	}
	
	public static boolean isEqualMonth(Date date1,Date date2){
		boolean flag=false;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int month1=cal.get(Calendar.MONTH);
		int year1=cal.get(Calendar.YEAR);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int year2=cal2.get(Calendar.YEAR);
		int month2=cal2.get(Calendar.MONTH);
		if(year1==year2 && month1==month2){
			flag=true;
		}else{
			flag=false;
		}
		return flag;
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
	
	/**
	 * @Description:获取保险期间
	 */
	public static long getPeriod(String startDate, String endDate)
	{
		long period = 0;
		Date dStartDate = DateUtil.toDate(startDate, "yyyy年MM月dd日H时");
		Date dEndDate = DateUtil.toDate(endDate, "yyyy年MM月dd日24时");
		long l = dEndDate.getTime() - dStartDate.getTime();
		long p = l / (60 * 60 * 1000);
		if (p % 24 == 0)
			period = p / 24;
		else
			period = p / 24 + 1;
		return period;
	}
}
