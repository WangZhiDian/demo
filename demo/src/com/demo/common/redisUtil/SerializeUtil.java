package com.demo.common.redisUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil
{
	public static byte[] serialize(Object object)
	{
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try
		{
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		}
		catch (Exception e)
		{
			System.out.println("/marketing_1017/java/src/com/taikang/redis/SerializeUtil.java serialize " + e);
			e.printStackTrace();
		}
		return null;
	}

	public static Object unserialize(byte[] bytes)
	{
		ByteArrayInputStream bais = null;
		try
		{
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e)
		{
			System.out.println("/marketing_1017/java/src/com/taikang/redis/SerializeUtil.java unserialize " + e);
			e.printStackTrace();
		}
		return null;
	}
}