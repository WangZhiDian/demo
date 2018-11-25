package com.demo.bussiness.qrcode;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

@Component
public class QrCodeService
{

	private static final Logger logger = LoggerFactory.getLogger(QrCodeService.class);

	private static QrcodeHandlerConfig config;
	
	static
	{
		config = new QrcodeHandlerConfig();
		config.setErrorCorrectionLevel(QrcodeHandlerConfig.ErrorCorrectionLevel_L);
		config.setHeight(300);
		config.setWidth(300);
		config.setMargin(1);
//		config.setForeGroundColor(0xFF005824);
	}
	/*
	 * 写入文件
	 */
	public boolean writeToFile(String content)
	{
		BitMatrix byteMatrix;
		try
		{
			byteMatrix = encode(content);
			File file = new File(config.getFilePath());
			BufferedImage image = toBufferedImage(byteMatrix, config.getForeGroundColor(), config.getBackGroundColor());
			ImageIO.write(image, "png", file);
		}
		catch (Exception e)
		{
			logger.error("write to file error!");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * 写入byte数组
	 */
	public byte[] writeToByte(String content)
	{
		BitMatrix byteMatrix;
		try
		{
			byteMatrix = encode(content);
			BufferedImage image = toBufferedImage(byteMatrix, config.getForeGroundColor(), config.getBackGroundColor());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(image, "png", out);
			return out.toByteArray();
		}
		catch (Exception e)
		{
			logger.error("write to byte error!");
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * TODO ： 解析二维码中内容
	 */
	public String decode(String imgPath)
	{
		BufferedImage image = null;
		Result result = null;
		try
		{
			image = ImageIO.read(new File(imgPath));
			if (image == null)
			{
				logger.error("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Map<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
			hints.put(DecodeHintType.CHARACTER_SET, QrcodeHandlerConfig.CHARSET_UTF8);
			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("decode qrcode error.");
		}
		return null;
	}

	/*
	 * TODO : 生成二维码图片
	 */
	private BitMatrix encode(String content) throws Exception
	{
		BitMatrix byteMatrix = null;
		Map<EncodeHintType, Object> hints = null;

		if (config == null || content == null)
		{
			logger.error("input error");
			throw new Exception("");
		}
		hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		if (config.getErrorCorrectionLevel().equals(QrcodeHandlerConfig.ErrorCorrectionLevel_Q))
		{
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		}
		else if (config.getErrorCorrectionLevel().equals(QrcodeHandlerConfig.ErrorCorrectionLevel_M))
		{
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		}
		else if (config.getErrorCorrectionLevel().equals(QrcodeHandlerConfig.ErrorCorrectionLevel_L))
		{
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		}
		else
		{
			hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		}
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, config.getCharset());
		// 指定二维码周边空隙
		hints.put(EncodeHintType.MARGIN, config.getMargin());

		byteMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, config.getWidth(),
		                                                                                config.getHeight(), hints);

		return byteMatrix;
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix, int foreGroundColor, int backGroundColor)
	{
//		foreGroundColor = 0xFF000000;
//	    backGroundColor = 0xFFFFFFFF;
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
//				boolean flag =  matrix.get(x, y);
//				if(flag)
//					System.out.println("y:" + y + "  " + flag);
				
				image.setRGB(x, y, matrix.get(x, y) ? foreGroundColor : backGroundColor);
			}
		}
		return image;
	}

	// 验证byte数组文件内容
	public void writeToFileFromByte(byte[] imapgeArray, String imgPath)
	{
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try
		{
			file = new File(imgPath);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(imapgeArray);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (bos != null)
			{
				try
				{
					bos.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
	}
}
