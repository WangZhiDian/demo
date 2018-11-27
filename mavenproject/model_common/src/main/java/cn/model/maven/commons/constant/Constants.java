package cn.model.maven.commons.constant;

/**
 * @Auther: <a mailto:xuyongming@ennew.cn>xuyongming</a>
 * description: 系统常量配置类
 */
public class Constants {
    /**
     * 支付接口调用字段-业务系统用户ID
     */
    public final static String BUSINESS_USER_ID = "member_id";
    public final static String TENANT_ID = "tenant_id";
    public final static String REQUEST_TIME = "req_time";
    public final static String SALT = "salt";
    public final static String SIGNATURE = "signature";

    /**
     * 调用成功码
     */
    public final static String INVOKE_OK = "TKPAY00000";

    /**
     * 唯一支持的货币类型
     */
    public final static String CURRENY = "CNY";

    public final static String TRUE = "1";
    public final static String FALSE = "0";


    public final static String SUCCESS = "SUCCESS";
    public final static String FAIL = "FAIL";
    public final static String CLOSE = "CLOSE";

    public final static String TENANTINFO_KEY = "TENANTINFO|";

    public final static int DEFAULT_CACHE_SECONDS = 60*60*24;//缓存时间

}
