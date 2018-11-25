package com.demo.common.util.Function;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

public class HtmlToPdfUtil
{
	public static void main(String[] args) {
/*		String inputFile = "F:\\pdftest\\temp2\\weiyibao.html";//html文件所在路径
		// String inputFile =
		// "D://MyDocuments//itw_leipeng//桌面//taiKangLife//taiKangLife//index.html";
		// String inputFile = "D://每天工作//20160512//1219.html";
		String url;
		try {
			url = new File(inputFile).toURI().toURL().toString();
			// url = new File(inputFile).toURL().toString();
			// url="D:/每天工作/1109A005/1109A00501.html";
			String outputFile = "F:/pdftest/temp2/test2.pdf";//转成pdf文件所在路径
			System.out.println(url);
			OutputStream os = new FileOutputStream(outputFile);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(url);

			// 解决中文支持问题
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("C:/Windows/Fonts/SIMSUN.TTC",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

			// 解决图片的相对路径问题
			renderer.getSharedContext().setBaseURL("file:/C:/image/");

			renderer.layout();
			renderer.createPDF(os);

			os.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	
		try {
			new HtmlToPdfUtil().htmlToPdf("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void htmlToPdf(String htmlPath) throws IOException
	{
		
		String inputFile = "F:\\pdftest\\temp2\\weiyibao.html";
		OutputStream os = null;
		try
		{
			String url = new File(inputFile).toURI().toURL().toString();
			String outFile = "F:/pdftest/temp2/test1.pdf";
			os = new FileOutputStream(outFile);
			ITextRenderer render = new ITextRenderer();
			render.setDocument(url);
			
			// 解决中文支持问题     
	        ITextFontResolver fontResolver = render.getFontResolver();     
	        fontResolver.addFont("F:\\pdftest\\ziti\\simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//	        fontResolver.addFont("C:/Windows/Fonts/SIMSUN.TTC", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		
			 // 解决图片的相对路径问题     
	        render.getSharedContext().setBaseURL("file:/D:/Work/Demo2do/Yoda/branch/Yoda%20-%20All/conf/template/");     
	             
	        render.layout();
	        render.createPDF(os);
			
		}catch (MalformedURLException e) {
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		finally
		{
			if(null != os)
			{
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
