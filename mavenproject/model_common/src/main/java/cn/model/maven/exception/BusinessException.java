package cn.model.maven.exception;

import cn.model.maven.commons.utils.PropertyConfigurer;
import lombok.Data;

/**
 * description: 业务异常封装类
 */
@Data
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = -8043502107809002745L;

    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 异常描述信息
     */
    private String errorMessage;

    public BusinessException(String errorCode) {
        super(PropertyConfigurer.getStringProperty(errorCode));
        this.errorCode = errorCode;
        this.errorMessage = PropertyConfigurer.getStringProperty(errorCode);
    }
}
