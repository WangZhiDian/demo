package com.demo.common.imageutil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfWriter;

public class Images2PdfUtil {
	/**
	 * 合成pdf
	 * @param logKey 日志标示
	 * @param imagesPath 图片本地服务器路径
	 * @param minLength 图片压缩临界
	 * @param compressLength 压缩比例
	 * @param nasPath 图片服务器目录
	 * @return
	 */
	public static String getPdfFromJpgs( String logKey,String imagesPath,int minLength,int compressLength,String nasPath){
		// 压缩图片
		if (!imagesCompress(logKey,imagesPath,minLength,compressLength))return null;
		// 组装Pdf路径
		String pdfLocalPath = new StringBuffer("").append(imagesPath).append(String.valueOf("imageName")).append(".pdf").toString();
		// 合成pdf
		if (!jpgs2Pdf(logKey,imagesPath,pdfLocalPath))return null;
		// 上传NAS
//		if(CommonUtil.isNullOfStr(SaveNas2OracleUtil.getNasPath(logKey,new File(pdfLocalPath),new File(nasPath))))return null;
		
		return pdfLocalPath;
	}
	/**
	 * 批量压缩图片
	 * @param logKey
	 * @param imagesPath
	 * @param minLength
	 * @param compressLength
	 * @return
	 */
	public static boolean imagesCompress(String logKey,String imagesPath,int minLength,int compressLength){
		if(!CommonUtil.haveFiles(imagesPath))return false;
		File[] compressFiles = new File(imagesPath).listFiles();
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},开始压缩图片>>>>>>",logKey,imagesPath);
		for(File file : compressFiles){
			if(!imageCompress(logKey,file.getAbsolutePath(),file.getAbsolutePath(),minLength,compressLength)){
				return false;
			}
		}
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},>>>>>>>>压缩图片结束",logKey,imagesPath);
		return true;
	}
	/**
	 * 压缩单张图片
	 * @param logKey
	 * @param imagePath
	 * @param minLength
	 * @param compressLength
	 * @return
	 */
	public static boolean imageCompress(String logKey,String imagePath,String finalPath,int minLength,int compressLength){
		if(new File(imagePath).length()<=minLength)return true;
		
		try{
			new ImgCompress(imagePath, finalPath).resizeFix(compressLength, compressLength);
		} catch (IOException e){
			CommonParamUtil.common_log.error("logKey:{}压缩图片imagepath:{}时异常",logKey,imagePath,e);
			return false;
		}
		
		return true;
	}
	/**
	 * 定制压缩单张图片
	 * @param logKey
	 * @param imagePath
	 * @param minLength
	 * @param compressLength
	 * @return
	 */
	public static boolean imageCompressOrder(String logKey,String imagePath,String finalPath,int minLength,int compressLength)
	{
		if(new File(imagePath).length()<=minLength){
			CommonParamUtil.common_log.debug(imagePath+"图片不需要压缩");
			return true;
		}
		
		try{
			new ImgCompress(imagePath, finalPath).resizeFix(compressLength, compressLength);
		} catch (IOException e){
			CommonParamUtil.common_log.error("logKey:{}压缩图片imagepath:{}时异常",logKey,imagePath,e);
			return false;
		}
		
		if(new File(finalPath).length()>minLength){
			return imageCompressOrder(logKey,imagePath,finalPath,minLength,compressLength+100);
		}
		
		return true;
	}
	/**
	 * 合成pdf
	 * @param logKey
	 * @param imagesPath
	 * @param pdfname
	 * @return
	 */
	public static boolean jpgs2Pdf(String logKey,String imagesPath,String pdfPath){
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},开始合成pdf:{}>>>>>>",logKey,imagesPath,pdfPath);
		
		if(!CommonUtil.haveFiles(imagesPath))return false;
        File[] pdfFiles = new File(imagesPath).listFiles();
        
        FileOutputStream fos = null;
        Document doc = new Document();
		
		try{
			fos = new FileOutputStream(pdfPath);
			PdfWriter.getInstance(doc, fos);
			doc.open();
			
			for(File file : pdfFiles){
			    if(!CommonUtil.isImage(file.getAbsolutePath())){
			        CommonParamUtil.common_log.error("logKey:{},合成pdf:{}时发现{}中的图片{}格式异常",logKey,pdfPath,imagesPath,file.getAbsolutePath());
			    	continue;
			    }
			    doc.add(getImg(file.getAbsolutePath())); 
			}
			
			if(doc.getPageNumber()<1){
				CommonParamUtil.common_log.error("logKey:{},合成pdf:{}时发现{}中的所有图片格式异常",logKey,pdfPath,imagesPath);
				return false;
			}
		}catch(Exception e){
			CommonParamUtil.common_log.error("logKey:{}图片合成pdf{}时异常",logKey,pdfPath,e);
			return false;
		}finally{
			if(!close(doc,fos)){
				return false;
			}
		}
		
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},>>>>>>>>合成pdf{}结束",logKey,imagesPath,pdfPath);
		return true;
	}
	/**
	 * 关闭doc
	 * @param doc
	 * @param fos
	 * @return
	 */
	public static boolean close( Document doc,FileOutputStream fos){
		try{
			if (doc != null){
				doc.close();doc = null;
			}
			if (fos != null){
				fos.close();fos = null;
			}
		}catch (IOException e){
			e.printStackTrace();return false;
		}
		return true;
	}
	/**
	 * 获取处理后的图片
	 * @param imagePath
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Image getImg(String imagePath) throws BadElementException, MalformedURLException, IOException{
		
		Image jpg1 = Image.getInstance(imagePath); // 原来的图片的路径
		// 获得图片的高度
		float heigth = jpg1.getHeight();
		float width = jpg1.getWidth();
		// 合理压缩，h>w，按w压缩，否则按w压缩
		int percent = getPercentLength(heigth, width);
		// 统一按照宽度压缩
		// int percent=getPercent2(heigth, width);
		// 设置图片居中显示
		jpg1.setAlignment(Image.MIDDLE);
		// 直接设置图片的大小~~~~~~~第三种解决方案，按固定比例压缩
		jpg1.scaleAbsolute(heigth, width);
		// 按百分比显示图片的比例
		jpg1.scalePercent(percent);
		
		return jpg1;
	}
	/**
	 * 按比例压缩
	 * @param h
	 * @param w
	 * @return
	 */
	private static int getPercentLength(float h, float w){
		float p2 = 0.0f;
		if (h > w){
			p2 = 297 / h * 180;
		}else{
			p2 = 210 / w * 180;
		}
		return Math.round(p2);
	}
	/**
	 * 获取本地暂存地址
	 * @param claim_id
	 * @param claim_date
	 * @return
	 */
	public static String  getLocalPath(String claim_id,String claim_date){
		return new StringBuffer("").append(CommonParamUtil.localpath).append(claim_id).append(claim_date).append("/").toString();
	}
	/**
	 * 获取NAS地址
	 * @param claim_id
	 * @param claim_date
	 * @return
	 */
	public static String  getNasPath(String claim_id,String claim_date){
		return new StringBuffer("").append(CommonParamUtil.NASPath).append(claim_id).append(claim_date).append("/").toString();
	}
	/**
	 * 删除文件集合
	 * @param filesList
	 */
	public static void delFileList(List<String> filesList)
	{
		for(String filePath :filesList){
			CopyFileAndMove.delall(filePath);
		}
	}
	public static void main(String[] args) {
		String filePath_after = "C:\\Users\\Public\\Pictures\\Sample Pictures\\17961491_082407720000_00.jpg";
		String filePath_after1 = "C:\\Users\\Public\\Pictures\\Sample Pictures\\17961491_082407720000_001.jpg";
		imageCompress("123",filePath_after,filePath_after1,200*1024,1000);
		System.out.println(500*1024);
	}
}
