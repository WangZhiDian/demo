package com.demo.common.util.Function;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesUtil
{
	public static String readProperties(String filePath, String fieldName)
	{
		String fieldContent = "";
		Properties properties = new Properties();
		FileInputStream inputFile = null;
		try{
			inputFile = new FileInputStream(filePath);
			properties.load(inputFile);
			fieldContent = properties.getProperty(fieldName);
		}catch (FileNotFoundException ex) 
        {
			log.debug("读取属性文件--->失败！- 原因：文件路径错误或者文件不存在");
            ex.printStackTrace();
        }catch(IOException ex)
		{
        	log.debug("装载文件--->失败!");
			ex.printStackTrace();
		}finally
		{
			try {
				if(null != inputFile)
					inputFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return fieldContent;
	}
}
