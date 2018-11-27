package cn.model.maven.exception;

/**
 * 异常原则：便于运维人员统计系统服务或者接口调用异常，减少不必要异常抛出
 * 	1.运行时异常，比如调用支付通道、数据库访问失败、缓存访问失败要记录并且抛出
 * 	2.业务异常，验证签名失败只需要记录，不需要抛出
 * 
 * 整个异常体系为：
 * 	HeraException                        
 * 		HeraRuntimeException			   ：记录并且抛出	
 * 		IllegalBusinessException     	   ：记录，但不抛出，错误信息返回前端
 * 
 * @author       wanghl80
 * @date         2017年9月6日 上午10:51:19
 * @version      V1.0
 *
 */
public  class HeraException extends RuntimeException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4266084173643656133L;
	
	
	/**
	 * 直接从ExceptionCode定义的静态变量中取
	 */
	private ExceptionCode code;

	/**
	 * 用于替换错误描述里面的 {0}
	 */
	private Object[] codeArgs;
	
	
	
	public HeraException(ExceptionCode code)
	{
		super(code.getErrorMessage());
		this.code = code;
	}

	public HeraException(ExceptionCode code, Object[] codeArgs)
	{
		super(code.getErrorMessage());
		this.code = code;
		this.codeArgs=codeArgs;
	}

	public HeraException(ExceptionCode code, Throwable cause)
	{
		super(code.getErrorMessage(), cause);
		this.code = code;
	}

	public HeraException(ExceptionCode code, Throwable cause, Object[] codeArgs)
	{
		super(code.getErrorMessage(), cause);
		this.code = code;
		this.codeArgs=codeArgs;
	}
	

	
	/**
	 * 获取最终的错误描述
	 * 
	 * @return
	 */
	
	public String getErrorMessage()
	{
		String errorMessage = null;

		errorMessage=super.getMessage();
		
		if (this.code != null)
			errorMessage = this.code.getErrorMessage();

		if (codeArgs != null && codeArgs.length > 0 && errorMessage != null)
			errorMessage = this.code.getErrorMessage(codeArgs);
		
		return errorMessage;
	}

	/**
	 * 获取最终的错误编码
	 * 
	 * @return
	 */
	public String getErrorCode()
	{
		return code!=null? code.getErrorCode():null;
	}
	
	/**
	 * 获取帮助或者跳转链接
	 * 
	 * @return
	 */
	public String getReferUrl()
	{
		return code!=null? code.getReferUrl():null;
	}
	
	@Override
	public String toString()
	{
		return this.getErrorMessage();
	}
	
	@Override
	public String getMessage()
	{
		return this.getErrorMessage();
	}

}
