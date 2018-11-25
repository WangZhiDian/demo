package com.demo.common.rest;

public class RestParam
{
	/**
	 * 系统版本
	 */
	public static final String VERSION_PARAM = "version";

	/**
	 * 来源，可能为eventId，也可能为 suiteId
	 */
	public static final String FROMID_PARAM = "fromid";
	
	/**
	 * 来源，可能为eventId，也可能为 suiteId
	 */
	public static final String FORMID_PARAM  = "formid";

	/**
	 * 平台ID wx ali
	 */
	public static final String PLATFORM_PARAM = "platform_id";
	/**
	 * 渠道ID
	 */
	public static final String CHANNELID_PARAM = "channelId";

	/**
	 * 活动ID
	 */
	public static final String EVENT_ID = "eventId";
	
	
	/**
	 * 客户编号, 用于导线索
	 */
	public static final String CUSTOMER_ID = "customerId";

	/**
	 * 产品类型
	 */
	public static final String PRODUCT_TYPE_PARAM = "productType";

	/**
	 * openId
	 */
	public static final String OPENID_PARAM = "openid";

	/**
	 * 父一级城市编码
	 */
	public static final String PARENT_CITY_CODE_PARAM = "parentCityCode";

	/**
	 * 父openId
	 */
	public static final String PARENT_OPENID_PARAM = "parentOpenId";

	/**
	 * 子openId
	 */
	public static final String CHILD_OPENID_PARAM = "childOpenId";

	/**
	 * 上传json数据时的参数名
	 */
	public static final String JSON_DATA_PARAM = "data";

	/**
	 * url
	 */
	public static final String URL_PARAM = "url";

	/**
	 * 手机号
	 */
	public static final String MOBILE_PARAM = "mobile";

	/**
	 * code
	 */
	public static final String PARAM_CODE = "code";

	/** timestamp */
	public static final String TIMESTAMP = "timestamp";
	/** sign */
	public static final String SIGN = "sign";
	
	public static final String TRADE_ID = "tradeId";
}
