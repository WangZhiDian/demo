package cn.model.maven.commons.utils;

import org.apache.http.util.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 文本检测工具
 */
public class TextUtil {
    /**
     * 判读url是不是http开头，不是则补充http
     */
    public static String CheckWebUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        /*
         * 判断url是否是http开头,不是则补充http
         */
        if (url.contains("http")) {
            return url;
        }
        else {
            return "http://" + url;
        }
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 总结起来就是第一位必定为1，其他位置的可以为0-9
         */
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"\\d{9}"代表后面是可以是0～9的数字，有10位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 验证密码 6位纯数字
     */
    public static boolean isPassWord(String pwd) {
        /*
        */
        String telRegex = "^\\d{6}$";
        if (TextUtils.isEmpty(pwd))
            return false;
        else
            return pwd.matches(telRegex);
    }

    /**
     * 将获取的推荐用户列表转成String
     * 
     * @param selected
     */
    public static String onString(ArrayList<String> selected) {
        StringBuilder sb = new StringBuilder();
        for (String sel : selected) {
            sb.append('@').append(sel).append(' ');
        }
        return sb.toString();
    }

    /**
     * 隐藏用户名
     * 
     * @param name
     *            待隐藏手机号
     * @return name
     */
    public static String hideUserName(String name) {

        if ((!TextUtils.isEmpty(name)) && name.length() > 0) {
            name = "*" + name.substring(1, name.length());
        }

        return name;
    }

    /**
     * 隐藏身份证 出生月.日
     * 
     * @param IdCard
     *            待隐藏身份证号
     * @return IdCard
     */
    public static String hideIdCard(String IdCard) {
        // 18位身份证隐藏出生月.日
        if (IdCard.length() == 18) {
            String head = IdCard.substring(0, 10);
            String body = IdCard.substring(10, IdCard.length() - 4);
            String foot = IdCard.substring(IdCard.length() - 4, IdCard.length());

            body = body.replaceAll("\\d", "*");
            IdCard = head + body + foot;
        }
        // 15位身份证隐藏出生月.日
        if (IdCard.length() == 15) {
            String head = IdCard.substring(0, 8);
            String body = IdCard.substring(8, IdCard.length() - 3);
            String foot = IdCard.substring(IdCard.length() - 3, IdCard.length());

            body = body.replaceAll("\\d", "*");
            IdCard = head + body + foot;
        }
        return IdCard;
    }

    /**
     * 隐藏身份证 中间的11位
     * 
     * @param IdCard
     *            待隐藏身份证号
     * @return IdCard
     */
    public static String hideIdCard_11(String IdCard) {

        // 身份证隐藏11位
        if (IdCard.length() > 7) {
            String head = IdCard.substring(0, 3);
            String body = IdCard.substring(3, IdCard.length() - 4);
            String foot = IdCard.substring(IdCard.length() - 4, IdCard.length());

            body = body.replaceAll("\\d", "*");
            IdCard = head + body + foot;
        }

        return IdCard;
    }

    /**
     * 隐藏手机号部分字符
     * 
     * @param mobile
     *            待隐藏手机号
     * @return mobile
     */
    public static String hidePhone(String mobile) {

        String head = mobile.substring(0, 3);
        String body = mobile.substring(3, 7);
        String foot = mobile.substring(7, mobile.length());
        body = body.replaceAll("\\d", "*");
        mobile = head + body + foot;

        return mobile;
    }

    /**
     * 隐藏银行卡号部分字符
     * 
     * @param bankcard
     *            待隐藏银行卡号
     * @return bankcard
     */
    public static String hideBankCard(String bankcard) {

        String head = "", body = "", foot = "";

        // //16位银行卡隐藏9位
        // if(bankcard.length() == 16){
        // head = bankcard.substring(0, 4);
        // body = bankcard.substring(4, 13);
        // foot = bankcard.substring(13,bankcard.length());
        // }
        // //19位银行卡隐藏12位
        // if(bankcard.length() == 19){
        // head = bankcard.substring(0, 4);
        // body = bankcard.substring(4, 16);
        // foot = bankcard.substring(16,bankcard.length());
        // }

        if (bankcard.length() > 4) {
            head = bankcard.substring(0, 4);
            body = bankcard.substring(4, bankcard.length() - 3);
            foot = bankcard.substring(bankcard.length() - 3, bankcard.length());

            body = body.replaceAll("\\d", "*");
            bankcard = head + body + foot;
        }

        return bankcard;
    }

    /**
     * 隐藏银行卡号部分字符,显示最后的4位
     * 
     * @param bankcard
     *            待隐藏银行卡号
     * @return bankcard
     */
    public static String hideBankCard4(String bankcard) {

        String head = "**** **** **** ", foot = "";

        if (bankcard.length() > 4) {
            foot = bankcard.substring(bankcard.length() - 4, bankcard.length());
            bankcard = head + foot;
        }

        return bankcard;
    }

    /**
     * 验证名字只能是中文汉字或英文
     * 
     * @param name
     * @return true || false
     */
    public static boolean isName(String name) {

        // String telRegex = "(^[a-zA-Z\u4e00-\u9fa5]+$)";//1.\u4e00-\u9fa5 中文区间
        String telRegex = "[\u4e00-\u9fa5]{1,10}";
        // 1. 中文区间 1-10位
        if (TextUtils.isEmpty(name))
            return false;
        else
            return name.matches(telRegex);
    }

    /**
     * 验证昵称是否符合要求（不能含有特殊字符）
     * 
     * @param str
     *            昵称
     * @return true昵称合法 false昵称不合法
     */
    public static boolean validateNickName(String str) {
        // String telRegex="(^[a-zA-Z\u4e00-\u9fa50-9]+$)";


        String telRegex = "^[a-zA-Z\u4e00-\u9fa50-9]{1,10}";
        // 1.a-zA-Z 字母
        // 2.\u4e00-\u9fa5 中文区间 // 3.^[0-9]*$ 数字
        if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(telRegex);
    }

    /**
     * 验证str只能是中文
     * 
     * @param str
     * @return true || false
     */
    public static boolean isChinese(String str) {
        String telRegex = "[\u4e00-\u9fa5]+$";
        /*
         * 1.\u4e00-\u9fa5 中文区间
         */
       if (TextUtils.isEmpty(str))
            return false;
        else
            return str.matches(telRegex);
    }

    /**
     * 将emoji表情替换成*
     * 
     * @param source
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {

        if (!TextUtils.isEmpty(source)) {
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
        }
        else {
            return source;
        }
    }

    /**
     * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，
     * 使它们与汉字同占两个字节，这样就可以避免由于占位导致的排版混乱问题了。 半角转为全角的代码如下，只需调用即可
     * 
     * @param input
     * @return
     */
    public static String converString(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 将 String 转化为 Bytes
     * 
     * @param str
     * @return bytes
     */
    public static byte[] StringToByte(String str) {
        int len = str.length();
        byte[] bytes = new byte[len];

        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (str.charAt(i));
        }
        return bytes;
    }

    /**
     * 将Bytes 转化为 String
     * 
     * @param
     * @return bytes
     */
    public static String ByteToString(byte[] bytes) {
        StringBuffer sbuf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sbuf.append(bytes[i]);
        }
        return sbuf.toString();
    }

    /**
     * 格式化小数点2位数
     * 
     * @param f
     * @return
     * 
     *         BigDecimal.setScale()方法用于格式化小数点 setScale(1)表示保留一位小数，默认用四舍五入方式
     *         setScale(1,BigDecimal.ROUND_DOWN)直接删除多余的小数位，如2.35会变成2.3
     *         setScale(1,BigDecimal.ROUND_UP)进位处理，2.35变成2.4
     *         setScale(1,BigDecimal.ROUND_HALF_UP)四舍五入，2.35变成2.4
     *         setScaler(1,BigDecimal.ROUND_HALF_DOWN)四舍五入，2.35变成2.3，如果是5则向下舍
     */

    /**
     * 强转2位小数
     * 
     * @param f
     * @return
     */
    public static String getPoint2String(float f) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.parseDouble(String.valueOf(f)));
    }

    /**
     * 数字string格式化成2位
     * 
     * @param s
     * @return
     */
    public static double getStringToDouble2(String s) {
        BigDecimal b = new BigDecimal(s.toString());
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        return Double.parseDouble(s.toString());
    }

    /**
     * 数字string格式化成float2位
     * 
     * @param s
     * @return
     */
    public static float getStringToFloat2(String s) {
        BigDecimal b = new BigDecimal(s.toString());
        float f1 = b.setScale(2, BigDecimal.ROUND_DOWN).floatValue();
        return f1;
    }

    /**
     * 数字格式化成2位，舍掉2位后面的数字
     * 
     * @param s
     * @return
     */
    public static String getStringTo2Down(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("0.00");
        BigDecimal b = new BigDecimal(s.toString());
        b = b.setScale(2, BigDecimal.ROUND_DOWN);
        return format.format(b);
    }

    /**
     * 数字string格式化成2位
     * 
     * @param s
     * @return
     */
    public static String getStringTo2(String s) {
        if (TextUtils.isEmpty(s)) {
            return "";
        }
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(new BigDecimal(s));
    }


    /**
     * 格式化字符串
     * 
     * @param str
     * @return
     */
    public static String removeAllSpace(String str) {
        if (!TextUtils.isEmpty(str)) {
            String tmpstr = str.replace(" ", "");
            return tmpstr;
        }
        return str;
    }
    
    /**
     * 空白字符处理为NULl
     * @param str
     * @return
     */
    public static String blankStringToNull(String str){
    	if(TextUtils.isBlank(str)) return null;
    	return str;
    }

}