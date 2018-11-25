package com.demo.common.id;

import java.security.SecureRandom;

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
}