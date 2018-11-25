package com.demo.common.util.Function;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.batik.dom.util.HashTable;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.Fop;
import org.w3c.tidy.Tidy;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HtmlToPdfFoUtil
{
	
	//获取htmlName模板，数据填充
	public String htmlFileGenerate(HashTable table, String htmlName)
	{
//		String pathString = null;
		String SerialNo = String.valueOf(new Date().getTime());
//		String pdfPath = "data/pdffop/pdfpath/";
		String pdfPath = "D:/webdev_E/platform/apache-tomcat-7.0.52/bin/data/pdffop/pdfpath/";
		String html_template_file_name = pdfPath + "template/"+htmlName+".html";
		String html_file_name = pdfPath + "temp/" + htmlName+SerialNo + PdfConfig.html_file;
		//文本内容
		String mDocText = "";
		
		File file = new File(html_template_file_name);
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			String tempStream = null;
			while((tempStream = reader.readLine()) != null)
			{
				mDocText += tempStream;
				mDocText += "\r\n";
			}
			reader.close();
		}catch(FileNotFoundException e)
		{
			e.printStackTrace();
			log.debug("模板 {} 未找到！", htmlName);
			return null;
		}catch(UnsupportedEncodingException e)
		{
			e.printStackTrace();
			log.debug("模板 {} 编码不支持！", htmlName);
			return null;
		}catch(IOException e)
		{
			e.printStackTrace();
			log.debug ("模板 {} 读取失败！", htmlName);
			return null;
		}finally
		{
			if (reader != null)
			{
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		//替换内容
		for(int i=0;i<table.size ( );i++)
		{		
			String key = table.key ( i ).toString ( );
			String value = " ";
			Object o = table.get(table.key (i).toString());
			if(o!=null)
			{
				value = o.toString ( );
			}
			if(value==null||"".equals (value))
			{
				value=" ";
			}
			mDocText = mDocText.replaceAll ( "\\<%= "+key+" %>" , "\\"+value );
			System.out.println("key::"+table.key ( i ).toString ( ));
			System.out.println("value::"+table.get(table.key (i).toString()).toString());
		}
		
		try
        {
	        BufferedWriter out_html = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(html_file_name),"UTF8"));
			out_html.write(mDocText);
			out_html.close();
        }
        catch ( UnsupportedEncodingException e1 )
        {
	        e1.printStackTrace();
	        return null;
        }
        catch ( FileNotFoundException e1 )
        {
	        e1.printStackTrace();
	        return null;
        }
        catch ( IOException e1 )
        {
	        e1.printStackTrace();
	        return null;
        }
		
//		String pdfPath = "F:/pdftest";
//		String SerialNo = String.valueOf(new Date().getTime());
		
    	// 1.转换标准的xhtml
		String xhtml_file_name = pdfPath + "temp/" + htmlName+ SerialNo + PdfConfig.xhtml_file;
		String xhtml_err_file_name = pdfPath + "temp/" + htmlName+ SerialNo + PdfConfig.err_xhtml_file;
		String fo_file_name = pdfPath + "temp/" + htmlName+ SerialNo + PdfConfig.fo_file;
		String pdf_file_name = pdfPath + "pdf/" + htmlName+ SerialNo + PdfConfig.pdf_file;
		File html_file = new File(html_file_name);
		
		BufferedInputStream in = null;
		try
		{
			in = new BufferedInputStream(new FileInputStream(html_file));
			OutputStream out = new FileOutputStream(xhtml_file_name);
			Tidy tidy = new Tidy();
			// 设置配置文件
			tidy.setConfigurationFromFile(pdfPath + PdfConfig.html2xhtml_config_file);
			// 设置错误文件
			tidy.setErrout(new PrintWriter(new FileWriter(xhtml_err_file_name)));
			// 解析
			tidy.parse(in, out);
		}catch(FileNotFoundException e)
		{
			log.debug ("临时文件 {} 未找到！", html_file);
			e.printStackTrace();
			return null;
		}catch (Exception e)
		{
			e.printStackTrace();
			log.debug("Tidy Exception: {}", e.getMessage());
			return null;
		}finally
		{
			try
			{
				in.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		// 2.用xhtml转换fo文件
		Source xml = new javax.xml.transform.stream.StreamSource(xhtml_file_name);
		String xhtml2foPath = pdfPath + PdfConfig.xhtml2fo_config_file_yl;
		Source xsl = new javax.xml.transform.stream.StreamSource(xhtml2foPath);
		Result out = null;
		out = new javax.xml.transform.stream.StreamResult(fo_file_name);
		try
		{
			TransformerFactory xx = TransformerFactory.newInstance();
			Transformer t = xx.newTransformer (xsl);
			t.setParameter ( "realPath" , pdfPath );
			t.setParameter ( "SubCom" ,"123");
			t.setParameter ( "SubComAddress" ,"beijing");
			t.setParameter ( "taiKangPensionMobile" ,"400100");
			t.setParameter ( "taiKangPensionSite" ,"www.baidu.com");
			
			t.transform(xml, out);
		}
		catch (Exception e)
		{
			log.debug("error: {}", e.getMessage());
			e.printStackTrace ( );
			return null;
		}
		
		// 3.用fo文件生成pdf、fop jar包
		File fo_file = new File(fo_file_name);
		File pdf_file = new File(pdf_file_name);
		try
		{
			log.debug("fop2pdf-init-start: {}", System.currentTimeMillis());
			FopFactory factory = FopFactory.newInstance();
			FOUserAgent userAgent = factory.newFOUserAgent();
			OutputStream outpdf = null;
			outpdf = new FileOutputStream(pdf_file);
			outpdf = new BufferedOutputStream(outpdf);
			factory.setUserConfig(pdfPath + PdfConfig.fo2pdf_config_file);
			Fop fop = factory.newFop(MimeConstants.MIME_PDF, userAgent, outpdf);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			Source source = new StreamSource(fo_file);
			Result res = new SAXResult(fop.getDefaultHandler());
			log.debug("fop2pdf-transformer-start: {}", System.currentTimeMillis());
			transformer.transform(source, res);
			log.debug("fop2pdf-transformer-end: {}", System.currentTimeMillis());
			outpdf.close();
			log.debug("fop2pdf-transformer-close: {}", System.currentTimeMillis());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug ("fop2pdf Exception: {}", e.getMessage ( ) );
			return null;
		}
		log.debug("pdf2byte-start: {}", System.currentTimeMillis());
		log.debug("pdf2byte-end: {}", System.currentTimeMillis());
		
		String pathString2 = pdf_file_name;
		
		return pathString2 + "01";
	}
	
	
}
