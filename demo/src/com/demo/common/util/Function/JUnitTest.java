package com.demo.common.util.Function;

import java.io.IOException;
import java.util.Date;

import org.apache.batik.dom.util.HashTable;
import org.junit.Test;

public class JUnitTest
{
//	@Test
	public void testTextUtil()
	{
		String a = "";
		System.out.println(TextUtil.blankStringToNull(a));
	}
	
//	@Test
	public void testDate()
	{
		String birthday = "2015-01-01";
		Date date = DateUtil.parseBirthday(birthday);
		System.out.println(date.toString());
	}
	
//	@Test
	public void testMd5()
	{
		String str = "ss";
		String md5 = Md5Encrypt.getMD5Mac(str);
		System.out.println(md5);
	}
	
//	@Test
	public void testPropertiesRead()
	{
		String filePath = "D:\\webdev_E\\platform\\apache-tomcat-7.0.52\\bin\\data\\config\\alipay.properties";
		String fieldName = "charset";
		String content = PropertiesUtil.readProperties(filePath, fieldName);
		System.out.println(content);
	}
//	@Test
	public void testHtmlToPdfFo()
	{
		//using fop xmlgraphics-commons-1.3.1.jar avalon-framework-4.2.0.jar
		HtmlToPdfFoUtil util = new HtmlToPdfFoUtil();
//		String path = "D:\\webdev_E\\platform\\apache-tomcat-7.0.52\\bin\\data\\pdffop\\pdfpath\\template\\PDFTest.html";
		String fileName = "PDFTest";
		HashTable table = new HashTable();
		util.htmlFileGenerate(table, fileName);
		System.out.println("--------------------------------end");
	}
	
	@Test
	public void testHtmlToPdf2() throws IOException
	{
		HtmlToPdfUtil util = new HtmlToPdfUtil();
		
		util.htmlToPdf("");
		System.out.println("end");
	}
}
