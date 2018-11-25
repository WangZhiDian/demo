package com.demo.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * ����SimpleDateFormat�Ĵ�����Ҫռ�ô�����Դ��Ϊ���������ܣ�������������̲߳��滺����SimpleDateFormat��<br/>
 * ���ڸ�ʽ�� <br/>
 * �ø�ʽ�������̰߳�ȫ�ģ���ÿ���߳���ʹ��DateFormatʱ��Ӧ��ʹ��DateFormat�����getInstance������ȡ��<br/>
 * 
 * ��ͼ����ǰ�߳��л�ȡ��DateFormatʵ��������һ���߳�ʹ�ã����ܻ���ֲ������⡣
 * <p>
 * 
 * ���磺
 * 
 * <pre>
 * Date date = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).parse(
 * 		&quot;2012-06-22 19.49.28&quot;);
 * String str = DateFormat.getInstance(&quot;yyyy-MM-dd HH.mm.ss&quot;).format(date);
 * </pre>
 * 
 * @author zhaopuqing
 * @created 2015��12��19�� ����4:28:20
 * @see
 */
public class DateFormatUtil {

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * ���õ����ڸ�ʽ��
	 */
	private SimpleDateFormat dateFormat;

	/**
	 * ���Ի���
	 */
	private Locale locale;

	/**
	 * �������߳��ڻ������ڸ�ʽ��
	 */
	private static ThreadLocal<Map<Key, DateFormatUtil>> formatMapThreadLocal = new ThreadLocal<Map<Key, DateFormatUtil>>();

	/**
	 * 
	 * ���캯���������˽����ǿ���û�ʹ��DateFormat.getXXInstance()�ȷ�����ȡ��ʽ��
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
	 * ��ȡ��ʽ��ʵ��,�ø�ʽ���ĸ�ʽ����"yyyy-MM-dd HH:mm:ss", ʱ�������Ի����ӷ������ĵ�ǰ�����л�ȡ��
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public static DateFormatUtil getInstance() {
		return getInstance(DEFAULT_FORMAT);
	}

	/**
	 * 
	 * ��ȡ��ʽ��ʵ��
	 * 
	 * @param pattern
	 *            ģʽ
	 * @param timeZone
	 *            ʱ��
	 * @param locale
	 *            ���Ի���
	 * @return ��ʽ��
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
	 * ��ȡ��ʽ��ʵ��,�ø�ʽ����ʱ�������Ի����ӷ������ĵ�ǰ�����л�ȡ��
	 * 
	 * @param pattern
	 *            ģʽ
	 * @return
	 * @author zhaopuqing
	 */
	public static DateFormatUtil getInstance(String pattern) {
		return getInstance(pattern, TimeZone.getDefault(), Locale.getDefault());
	}

	/**
	 * 
	 * ��java.util.Date�����ʽ����String
	 * 
	 * @param date
	 *            ����
	 * @return �ַ���
	 * @author zhaopuqing
	 */
	public String format(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 
	 * ��long�ͣ��ӱ�׼��׼ʱ�伴 1970 �� 1 �� 1 �� 00:00:00 GMT������ָ������������ʽ����String<br/>
	 * 
	 * @param date
	 *            �ӱ�׼��׼ʱ�伴 1970 �� 1 �� 1 �� 00:00:00 GMT������ָ��������
	 * @return �ַ���
	 * @author zhaopuqing
	 */
	public String format(long date) {
		return dateFormat.format(new Date(date));
	}

	/**
	 * 
	 * ��ʱ�䴮������java.util.Date,����ʧ�ܽ��׳�RuntimeException
	 * 
	 * @param source
	 *            ʱ�䴮
	 * @return
	 * @author zhaopuqing
	 */
	public Date parse(String source) throws ParseException {
		return dateFormat.parse(source);
	}

	/**
	 * 
	 * ��ʱ�䴮�����ɾ����׼ʱ��ĺ�����,����ʧ�ܽ��׳�RuntimeException
	 * 
	 * @param source
	 *            ʱ�䴮
	 * @return
	 * @author zhaopuqing
	 */
	public long parseToLong(String source) throws ParseException {
		return parse(source).getTime();
	}

	/**
	 * 
	 * ��ȡ��ʽ���ĸ�ʽ
	 * 
	 * @return ��ʽ
	 * @author zhaopuqing
	 */
	public String getPattern() {
		return dateFormat.toPattern();
	}

	/**
	 * 
	 * ��ȡʱ��
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public TimeZone getTimeZone() {
		return dateFormat.getTimeZone();
	}

	/**
	 * 
	 * ����ʱ��
	 * 
	 * @param zone
	 * @author zhaopuqing
	 */
	public void setTimeZone(TimeZone zone) {
		dateFormat.setTimeZone(zone);
	}

	/**
	 * 
	 * ��ȡ��ʽ���ڲ������Ի���
	 * 
	 * @return
	 * @author zhaopuqing
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * ������ʾһ��Format
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
