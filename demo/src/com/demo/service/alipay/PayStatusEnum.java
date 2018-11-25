package com.demo.service.alipay;

/**
 * 支付状态
 */
public enum PayStatusEnum {

    WAIT("0","等待支付"),// 等待支付
    PAYING("1","处理中"),// 处理中
    TIME_OUT("2", "订单超时"),//订单超时
    SUCCESS("3","支付成功"),// 支付成功
    FAIL("4","支付失败"),// 支付失败
    CLOSE("5","订单关闭"),// 订单关闭
	DELAY_PAY("6","延迟扣款"),// 延迟扣款
    CANCEL("7", "订单取消");//订单取消
	
    private String code;
    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private PayStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PayStatusEnum getByCode(String code) {
        for (PayStatusEnum e : PayStatusEnum.values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        return null;
    }
}
