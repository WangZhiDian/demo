package cn.model.maven.commons.utils.qrcode;

public class QrcodeHandlerConfig
{

	public static final String CHARSET_UTF8 = "UTF-8";

	public static final String CHARSET_GBK = "GBK";

	/*
	 * 纠错级别 L = ~7% correction L(0x01), M = ~15% correction M(0x00), Q = ~25%
	 * correction Q(0x03), H = ~30% correction H(0x02);
	 */
	public static final String ErrorCorrectionLevel_L = "L";

	public static final String ErrorCorrectionLevel_M = "M";

	public static final String ErrorCorrectionLevel_Q = "Q";

	public static final String ErrorCorrectionLevel_H = "H";

	/*
	 * 二维码颜色
	 */
	private static final int BLACK = 0xFF000000;

	private static final int WHITE = 0xFFFFFFFF;

	/*
	 * 编码
	 */
	private String charset = CHARSET_UTF8;

	/*
	 * 纠错级别
	 */
	private String errorCorrectionLevel = ErrorCorrectionLevel_H;

	/*
	 * 尺寸
	 */
	private int width = 280;

	private int height = 280;

	/*
	 * 二维码边距
	 */
	private int margin;

	private String filePath;

	/*
	 * 二维码字体颜色
	 */
	private int backGroundColor = WHITE;

	private int foreGroundColor = BLACK;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public String getErrorCorrectionLevel() {
		return errorCorrectionLevel;
	}

	public void setErrorCorrectionLevel(String errorCorrectionLevel) {
		this.errorCorrectionLevel = errorCorrectionLevel;
	}

	public int getBackGroundColor() {
		return backGroundColor;
	}

	public void setBackGroundColor(int backGroundColor) {
		this.backGroundColor = backGroundColor;
	}

	public int getForeGroundColor() {
		return foreGroundColor;
	}

	public void setForeGroundColor(int foreGroundColor) {
		this.foreGroundColor = foreGroundColor;
	}
}
