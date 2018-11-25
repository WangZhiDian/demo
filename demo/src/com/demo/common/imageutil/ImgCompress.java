package com.demo.common.imageutil;

/** 
 * @author 王文超 
 * @version 
 * @time 2015年10月26日 下午1:40:48  
 * 类说明 
*/ 

/**
 * @Description 图片压缩处理
 * @author 王文超
 * @version 
 * @time 2015年10月26日 下午1:40:48 
 */

import java.io.*;
import java.util.Date;
import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.*;

public class ImgCompress {
	private Image img;
	private int width;//源图宽
	private int height;//源图长
	private String pathname;//目标路径
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		System.out.println("开始：" + new Date().toLocaleString());
		String fileName = "";
		String pathname = "";
		ImgCompress imgCom = new ImgCompress(fileName,pathname);
		imgCom.resizeFix(1366, 768);
		
		System.out.println("结束：" + new Date().toLocaleString());
	}
	/**
	 * 构造函数
	 */
	public ImgCompress(String fileName,String pathname)  {
		File file = new File(fileName);// 读入文件
		this.pathname=pathname;
		try
		{
			img = ImageIO.read(file);
		} catch (IOException e)
		{
			e.printStackTrace();
		}      // 构造Image对象
		width = img.getWidth(null);    // 得到源图宽
		height = img.getHeight(null);  // 得到源图长
	}
	/**
	 * 按照宽度还是高度进行压缩
	 * @param w int 最大宽度
	 * @param h int 最大高度
	 */
	public void resizeFix(int w, int h) throws IOException {
		if (width / height > w / h) {
			resizeByWidth(w);
		} else {
			resizeByHeight(h);
		}
	}
	/**
	 * 以宽度为基准，等比例放缩图片
	 * @param w int 新宽度
	 */
	public void resizeByWidth(int w) throws IOException {
		int h = (int) (height * w / width);
		resize(w, h);
	}
	/**
	 * 以高度为基准，等比例缩放图片
	 * @param h int 新高度
	 */
	public void resizeByHeight(int h) throws IOException {
		int w = (int) (width * h / height);
		resize(w, h);
	}
	/**
	 * 强制压缩/放大图片到固定的大小
	 * @param w int 新宽度
	 * @param h int 新高度
	 */
	public void resize(int w, int h) throws IOException {
		// SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
		BufferedImage image = new BufferedImage(w, h,BufferedImage.TYPE_INT_RGB ); 
		image.getGraphics().drawImage(img, 0, 0, w, h, null); // 绘制缩小后的图		
		File destFile = new File(pathname);
		FileOutputStream out = new FileOutputStream(destFile); // 输出到文件流
		// 可以正常实现bmp、png、gif转jpg
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		encoder.encode(image); // JPEG编码
		out.close();
	}
}