package cn.model.maven.commons.resource;


import cn.model.maven.commons.utils.ErrorUtil;
import cn.model.maven.exception.BusinessException;
import cn.model.maven.exception.ExceptionCode;
import cn.model.maven.exception.HeraRuntimeException;
import cn.model.maven.exception.IllegalBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.sql.SQLException;

@Slf4j
@Provider
public class HeraExceptionMapper implements ExceptionMapper<Exception>
{
    @Context
    private HttpServletRequest request;
    @Context
    private ServletContext servletContext;

    @Override
    public Response toResponse(Exception exception)
    {
        String errorCode = null;
        String errorMessage = null;
        String referUrl = null;
        if (exception instanceof BusinessException) {//异常码
            errorCode = ((BusinessException) exception).getErrorCode();
            errorMessage = ErrorUtil.getErrorMessage(errorCode);
            if (errorCode.startsWith("ERROR")) {
                referUrl = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getReferUrl();
                log.error("====BusinessException errorCode:{},errorMessage:{}====", errorCode, errorMessage);
                log.error(errorCode, exception);
            } else {
                //TODO 按BusinessExceptionCode区分异常类型

            }
        } else if (exception instanceof IllegalBusinessException) {
            // 业务异常，直接返回前端，不做日志记录
            IllegalBusinessException heraException = (IllegalBusinessException) exception;
            errorCode = heraException.getErrorCode();
            errorMessage = heraException.getErrorMessage();
            referUrl = heraException.getReferUrl();
            log.error(errorCode, exception); // 测试环境为减少日志刷新速度，暂时注释掉
            log.error("====HeraRuntimeException errorCode:{},errorMessage:{}====", errorCode, errorMessage);
        } else if (exception instanceof HeraRuntimeException) {
            // 服务异常，不返回前端，做日志记录
            HeraRuntimeException heraException = (HeraRuntimeException) exception;
            errorCode = heraException.getErrorCode();
            errorMessage = heraException.getErrorMessage();
            referUrl = heraException.getReferUrl();
            log.error(errorCode, exception); // 测试环境为减少日志刷新速度，暂时注释掉
            log.error("====HeraRuntimeException errorCode:{},errorMessage:{}====", errorCode, errorMessage);
            errorMessage = "网络繁忙，请稍后再试";// 返回前端易读错误提示
        } else if (exception instanceof IllegalArgumentException) { // 处理 IllegalArgumentException
            // 非法入参
            errorCode = ExceptionCode.SYSTEM_ILLEGAL_REQUEST_B.getErrorCode();
            errorMessage = ExceptionCode.SYSTEM_ILLEGAL_REQUEST_B.getErrorMessage() + ": " + exception.getMessage();
            referUrl = ExceptionCode.SYSTEM_ILLEGAL_REQUEST_B.getReferUrl();
            log.error("====IllegalArgumentException errorCode:{},errorMessage:{}====", errorCode, errorMessage);
            log.error(errorCode, exception);
            // errorMessage = "网络繁忙，请稍后再试";//返回前端易读错误提示
        } else if (exception instanceof javax.ws.rs.ClientErrorException) {
            // 针对客户端的请求异常，直接抛出异常的返回结果，不进行处理
            // logger.error("javax.ws.rs.ClientErrorException", exception);
            return ((javax.ws.rs.ClientErrorException) exception).getResponse();
        } else if (exception instanceof SQLException) {
            errorCode = ExceptionCode.SYSTEM_INTERFACE_FAIL.getErrorCode();
            errorMessage = ExceptionCode.SYSTEM_INTERFACE_FAIL.getErrorMessage();
            referUrl = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getReferUrl();
        }else if (exception instanceof PersistenceException) {
            errorCode = ExceptionCode.SYSTEM_INTERFACE_FAIL.getErrorCode();
            errorMessage = ExceptionCode.SYSTEM_INTERFACE_FAIL.getErrorMessage();
            referUrl = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getReferUrl();
        }  else { // 处理 其它没有定义在ExceptionEnum里面的异常
            errorCode = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getErrorCode();
            errorMessage = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getErrorMessage() ;
            referUrl = ExceptionCode.SYSTEM_UNDEFINEDE_EXECPTION.getReferUrl();
            log.error("====未定义的异常   errorCode:{},errorMessage:{}====", errorCode, errorMessage);
            log.error(errorCode, exception);
            // errorMessage = "网络繁忙，请稍后再试";//返回前端易读错误提示
        }
        // 返回前端
        APIResponse restResponse = new APIResponse();
        restResponse.removeEncrptKey();//remove 租户加签密钥
        restResponse.setCode(errorCode);
        restResponse.setMessage(errorMessage);
        restResponse.setReferUrl(referUrl);

        return Response.ok().entity(restResponse.toJSONString()).build();
    }

}
