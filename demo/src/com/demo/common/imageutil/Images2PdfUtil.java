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
	 * �ϳ�pdf
	 * @param logKey ��־��ʾ
	 * @param imagesPath ͼƬ���ط�����·��
	 * @param minLength ͼƬѹ���ٽ�
	 * @param compressLength ѹ������
	 * @param nasPath ͼƬ������Ŀ¼
	 * @return
	 */
	public static String getPdfFromJpgs( String logKey,String imagesPath,int minLength,int compressLength,String nasPath){
		// ѹ��ͼƬ
		if (!imagesCompress(logKey,imagesPath,minLength,compressLength))return null;
		// ��װPdf·��
		String pdfLocalPath = new StringBuffer("").append(imagesPath).append(String.valueOf("imageName")).append(".pdf").toString();
		// �ϳ�pdf
		if (!jpgs2Pdf(logKey,imagesPath,pdfLocalPath))return null;
		// �ϴ�NAS
//		if(CommonUtil.isNullOfStr(SaveNas2OracleUtil.getNasPath(logKey,new File(pdfLocalPath),new File(nasPath))))return null;
		
		return pdfLocalPath;
	}
	/**
	 * ����ѹ��ͼƬ
	 * @param logKey
	 * @param imagesPath
	 * @param minLength
	 * @param compressLength
	 * @return
	 */
	public static boolean imagesCompress(String logKey,String imagesPath,int minLength,int compressLength){
		if(!CommonUtil.haveFiles(imagesPath))return false;
		File[] compressFiles = new File(imagesPath).listFiles();
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},��ʼѹ��ͼƬ>>>>>>",logKey,imagesPath);
		for(File file : compressFiles){
			if(!imageCompress(logKey,file.getAbsolutePath(),file.getAbsolutePath(),minLength,compressLength)){
				return false;
			}
		}
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},>>>>>>>>ѹ��ͼƬ����",logKey,imagesPath);
		return true;
	}
	/**
	 * ѹ������ͼƬ
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
			CommonParamUtil.common_log.error("logKey:{}ѹ��ͼƬimagepath:{}ʱ�쳣",logKey,imagePath,e);
			return false;
		}
		
		return true;
	}
	/**
	 * ����ѹ������ͼƬ
	 * @param logKey
	 * @param imagePath
	 * @param minLength
	 * @param compressLength
	 * @return
	 */
	public static boolean imageCompressOrder(String logKey,String imagePath,String finalPath,int minLength,int compressLength)
	{
		if(new File(imagePath).length()<=minLength){
			CommonParamUtil.common_log.debug(imagePath+"ͼƬ����Ҫѹ��");
			return true;
		}
		
		try{
			new ImgCompress(imagePath, finalPath).resizeFix(compressLength, compressLength);
		} catch (IOException e){
			CommonParamUtil.common_log.error("logKey:{}ѹ��ͼƬimagepath:{}ʱ�쳣",logKey,imagePath,e);
			return false;
		}
		
		if(new File(finalPath).length()>minLength){
			return imageCompressOrder(logKey,imagePath,finalPath,minLength,compressLength+100);
		}
		
		return true;
	}
	/**
	 * �ϳ�pdf
	 * @param logKey
	 * @param imagesPath
	 * @param pdfname
	 * @return
	 */
	public static boolean jpgs2Pdf(String logKey,String imagesPath,String pdfPath){
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},��ʼ�ϳ�pdf:{}>>>>>>",logKey,imagesPath,pdfPath);
		
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
			        CommonParamUtil.common_log.error("logKey:{},�ϳ�pdf:{}ʱ����{}�е�ͼƬ{}��ʽ�쳣",logKey,pdfPath,imagesPath,file.getAbsolutePath());
			    	continue;
			    }
			    doc.add(getImg(file.getAbsolutePath())); 
			}
			
			if(doc.getPageNumber()<1){
				CommonParamUtil.common_log.error("logKey:{},�ϳ�pdf:{}ʱ����{}�е�����ͼƬ��ʽ�쳣",logKey,pdfPath,imagesPath);
				return false;
			}
		}catch(Exception e){
			CommonParamUtil.common_log.error("logKey:{}ͼƬ�ϳ�pdf{}ʱ�쳣",logKey,pdfPath,e);
			return false;
		}finally{
			if(!close(doc,fos)){
				return false;
			}
		}
		
		CommonParamUtil.common_log.debug("logKey:{},imagesPath:{},>>>>>>>>�ϳ�pdf{}����",logKey,imagesPath,pdfPath);
		return true;
	}
	/**
	 * �ر�doc
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
	 * ��ȡ������ͼƬ
	 * @param imagePath
	 * @return
	 * @throws BadElementException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private static Image getImg(String imagePath) throws BadElementException, MalformedURLException, IOException{
		
		Image jpg1 = Image.getInstance(imagePath); // ԭ����ͼƬ��·��
		// ���ͼƬ�ĸ߶�
		float heigth = jpg1.getHeight();
		float width = jpg1.getWidth();
		// ����ѹ����h>w����wѹ��������wѹ��
		int percent = getPercentLength(heigth, width);
		// ͳһ���տ��ѹ��
		// int percent=getPercent2(heigth, width);
		// ����ͼƬ������ʾ
		jpg1.setAlignment(Image.MIDDLE);
		// ֱ������ͼƬ�Ĵ�С~~~~~~~�����ֽ�����������̶�����ѹ��
		jpg1.scaleAbsolute(heigth, width);
		// ���ٷֱ���ʾͼƬ�ı���
		jpg1.scalePercent(percent);
		
		return jpg1;
	}
	/**
	 * ������ѹ��
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
	 * ��ȡ�����ݴ��ַ
	 * @param claim_id
	 * @param claim_date
	 * @return
	 */
	public static String  getLocalPath(String claim_id,String claim_date){
		return new StringBuffer("").append(CommonParamUtil.localpath).append(claim_id).append(claim_date).append("/").toString();
	}
	/**
	 * ��ȡNAS��ַ
	 * @param claim_id
	 * @param claim_date
	 * @return
	 */
	public static String  getNasPath(String claim_id,String claim_date){
		return new StringBuffer("").append(CommonParamUtil.NASPath).append(claim_id).append(claim_date).append("/").toString();
	}
	/**
	 * ɾ���ļ�����
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
