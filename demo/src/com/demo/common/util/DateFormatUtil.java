package com.demo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 由于SimpleDateFormat的创建需要占用大量资源，为了提升性能，这个工具类在线程层面缓存了SimpleDateFormat。<br/>
 * 日期格式器 <br/>
 * 该格式器不是线程安全的，在每个线程中使用DateFormat时都应该使用DateFormat的相关getInstance方法获取。<br/>
 * 
 * 试图将当前线程中获取的DateFormat实例传给另一个线程使用，可能会出现并发问题。
 * <p>
 * 
 * 例如：
 * 
 * <pre>
 * Date date = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).parse(
 * 		&quot;2012-06-22 19.49.28&quot;);
 * String str = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).format(date);
 * </pre>
 * 
 * @author zhaopuqing
 * @created 2015年12月19日 下午4:28:20
 * @see
 */
public class DateFormatUtil {

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 内置的日期格式器
	 */
	private SimpleDateFormat dateFormat;

	/**
	 * 语言环境
	 */
	private Locale locale;

	/**
	 * 用来在线程内缓存日期格式器
	 */
	private static ThreadLocal<Map<Key, DateFormatUtil>> formatMapThreadLocal = new ThreadLocal<Map<Key, DateFormatUtil>>();

	/**
	 * 
	 * 构造函数，设成了私有来强制用户使用DateFormat.getXXInstance()等方法获取格式器
	 * 
	 * @param dateFormat
	 * @param locale
	 * @author zhaopuqing
	 */
	private DateFormatUtil(SimpleDateFormat dateFormat, Locale locale) {
		this.dateFormat = dateFormat;
		this.locale = locale;
	}

	/**
	 * 获取格式器实例,该格式器的格式采用"yyyy-MM-dd HH:mm:ss", 时区、语言环境从服务器的当前环境中获取。
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public static DateFormatUtil getInstance() {
		return getInstance(DEFAULT_FORMAT);
	}

	/**
	 * 
	 * 获取格式器实例
	 * 
	 * @param pattern
	 *            模式
	 * @param timeZone
	 *            时区
	 * @param locale
	 *            语言环境
	 * @return 格式器
	 * @author zhaopuqing
	 */
	public static DateFormatUtil getInstance(String pattern, TimeZone timeZone,
			Locale locale) {
		Map<Key, DateFormatUtil> formatMap = formatMapThreadLocal.get();
		if (formatMap == null) {
			formatMap = new HashMap<Key, DateFormatUtil>();
			formatMapThreadLocal.set(formatMap);
		}

		Key key = new Key(pattern, locale);

		DateFormatUtil format = formatMap.get(key);
		if (format == null) {
			format = new DateFormatUtil(new SimpleDateFormat(pattern, locale),
					locale);
			formatMap.put(key, format);
		}
		format.setTimeZone(timeZone);
		return format;
	}
	
	

	/**
	 * 
	 * 获取格式器实例,该格式器的时区、语言环境从服务器的当前环境中获取。
	 * 
	 * @param pattern
	 *            模式
	 * @return
	 * @author zhaopuqing
	 */
	public static DateFormatUtil getInstance(String pattern) {
		return getInstance(pattern, TimeZone.getDefault(), Locale.getDefault());
	}

	/**
	 * 
	 * 将java.util.Date对象格式化成String
	 * 
	 * @param date
	 *            日期
	 * @return 字符串
	 * @author zhaopuqing
	 */
	public String format(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 
	 * 将long型（从标准基准时间即 1970 年 1 月 1 日 00:00:00 GMT以来的指定毫秒数）格式化成String<br/>
	 * 
	 * @param date
	 *            从标准基准时间即 1970 年 1 月 1 日 00:00:00 GMT以来的指定毫秒数
	 * @return 字符串
	 * @author zhaopuqing
	 */
	public String format(long date) {
		return dateFormat.format(new Date(date));
	}

	/**
	 * 
	 * 将时间串解析成java.util.Date,解析失败将抛出RuntimeException
	 * 
	 * @param source
	 *            时间串
	 * @return
	 * @author zhaopuqing
	 */
	public Date parse(String source) throws ParseException {
		return dateFormat.parse(source);
	}

	/**
	 * 
	 * 将时间串解析成距离标准时间的毫秒数,解析失败将抛出RuntimeException
	 * 
	 * @param source
	 *            时间串
	 * @return
	 * @author zhaopuqing
	 */
	public long parseToLong(String source) throws ParseException {
		return parse(source).getTime();
	}

	/**
	 * 
	 * 获取格式器的格式
	 * 
	 * @return 格式
	 * @author zhaopuqing
	 */
	public String getPattern() {
		return dateFormat.toPattern();
	}

	/**
	 * 
	 * 获取时区
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public TimeZone getTimeZone() {
		return dateFormat.getTimeZone();
	}

	/**
	 * 
	 * 设置时区
	 * 
	 * @param zone
	 * @author zhaopuqing
	 */
	public void setTimeZone(TimeZone zone) {
		dateFormat.setTimeZone(zone);
	}

	/**
	 * 
	 * 获取格式器内部的语言环境
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * 用来标示一个Format
	 * 
	 * @author zhaopuqing
	 */
	private static class Key {
		String pattern;

		Locale locale;

		Key(String pattern, Locale locale) {
			this.pattern = pattern;
			this.locale = locale;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((locale == null) ? 0 : locale.hashCode());
			result = prime * result
					+ ((pattern == null) ? 0 : pattern.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (locale == null) {
				if (other.locale != null)
					return false;
			} else if (!locale.equals(other.locale))
				return false;
			if (pattern == null) {
				if (other.pattern != null)
					return false;
			} else if (!pattern.equals(other.pattern))
				return false;
			return true;
		}
	}

	public static void main(String[] args) {

		DateFormatUtil format = DateFormatUtil.getInstance("yyyy-MM-dd HH.mm.ss");
		String d = format.format(new Date());
		System.out.println(d);

		// SimpleDateFormat format = new
		// SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SZ");

		// String dateStr = "2010-10-13-09.17.07.099+480";
		// try {
		// Date date = format.parse(dateStr);
		// System.out.println(date.getTime());
		// } catch (ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// Date date = new Date();
		// System.out.println(format.format(date));
		// // format.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
		// System.out.println(format.format(date));
	}

}
