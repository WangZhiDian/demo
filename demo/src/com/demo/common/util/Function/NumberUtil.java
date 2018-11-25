package com.demo.common.util.Function;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {

	/**
	 * 把小数乘以一个系数返回小数点前的数据
	 * @param d
	 * @param multiplyNum
	 * @return
	 */
	public static long getLongByMultiply(double d, int multiplyNum){
		BigDecimal bd = new BigDecimal(d);
        bd = bd.multiply(new BigDecimal(multiplyNum)).setScale(0, RoundingMode.HALF_UP);
        return bd.longValue();
	}
	
	/**
	 * 把数字除以一个系数返回一个数据
	 * @param l
	 * @param divideNum
	 * @return
	 */
	public static double getDoubleByDivide(long l, int divideNum){
		return new BigDecimal(l).divide(new BigDecimal(divideNum)).doubleValue();
	}
	
	/**
	 * 格式化double类型的数据，保留两位小数
	 * @param l
	 * @param divideNum
	 * @return
	 */
	public static String getDoubleByDivideString(long l, int divideNum){
		double numDouble = getDoubleByDivide(l,divideNum);
		DecimalFormat df = new DecimalFormat("######0.00");
		return df.format(numDouble);
	}
	
}
