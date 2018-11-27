package cn.model.maven.commons.id;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

public class Guid
{
	private static final SecureRandom random = new SecureRandom();

	// create 16 byte 'GUID'
	public static String CreateId()
	{
		byte[] b = new byte[16];
		random.nextBytes(b);

		b[6] &= 0x0F;
		b[6] |= 0x40;
		b[8] &= 0x3F;
		b[8] |= 0x80;

		return ConvertId(b);
	}

	// convert binary GUID to string
	public static String ConvertId(byte[] id)
	{
		StringBuffer b = new StringBuffer(32);

		for (int i = 0; i < id.length; i++)
		{
			b.append(GUIDDigits[0x0F & (id[guidByteOrder[i]] >> 4)]);
			b.append(GUIDDigits[0x0F & id[guidByteOrder[i]]]);
		}

		return b.toString();
	}

	// get a random number within a range
	public static int getNumber(int high)
	{
		return random.nextInt(high);
	}

	// fill callers buffer with random bytes
	public void fill(byte[] buf)
	{
		random.nextBytes(buf);
	}

	private static final char[] HexDigits = "0123456789abcdef".toCharArray();

	public static String toHex(byte[] data)
	{
		String hex = new String();
		for (int i = 0; i < data.length; i++)
		{
			int index = (data[i] & 0xF0) >> 4;
			hex += HexDigits[index];
			index = (data[i] & 0x0F);
			hex += HexDigits[index];
		}
		return hex;
	}

	private static final byte[] guidByteOrder = new byte[]
	{ 3, 2, 1, 0, 5, 4, 7, 6, 8, 9, 10, 11, 12, 13, 14, 15 };
	private static final char[] GUIDDigits = "0123456789ABCDEF".toCharArray();

	/**
	 * 生存指定位数的随机数
	 */
	public  static String getUniqueString(int num) {
		Random rm = new Random();
		// 获取随机数
		double rmNum = (1 + rm.nextDouble()) * Math.pow(10,num);
		// 获取随机数转为字符串
		String result = String.valueOf(rmNum);
		// 返回固定长度的随机数
		return result.substring(1,num + 1);
	}

	/**
	 * UUID生成(32位)
	 *
	 * @return
	 */
	public static String generateLongUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replace("-", "");
		return uuid.toUpperCase();
	}

	/**
	 * UUID生成(16位)
	 * @return
	 */
	public static String generateShortUUID() {
		return UUID.randomUUID().toString().replace("-", "").substring(16).toUpperCase();
	}

	/**
	 * 生成六位验证码
	 *
	 * @return
	 */
	public static String getRamdPw6() {
		String ret = "";
		int array[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Random rand = new Random();
		for (int i = 10; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = array[index];
			array[index] = array[i - 1];
			array[i - 1] = tmp;
		}

		int result = 0;
		for (int i = 0; i < 6; i++)
			result = result * 10 + array[i];

		if (String.valueOf(result).length() == 5)
			ret = (new StringBuilder("0")).append(result).toString();
		else
			ret = String.valueOf(result);
		return ret;
	}

}