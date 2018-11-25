package com.demo.bussiness.Exception.exception;

public class HeraRuntimeException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1723355624334958360L;

	/**
	 * 直接从ExceptionCode定义的静态变量中取
	 */
	private ExceptionCode code;
	private ExceptionCode2 code2;

	/**
	 * 用于替换错误描述里面的 {0}
	 */
	private Object[] codeArgs;

	// 包含ExceptionCode的构造函数
	public HeraRuntimeException(ExceptionCode code)
	{
		super(code.getErrorMessage());
		this.code = code;
	}

	public HeraRuntimeException(ExceptionCode2 code)
	{
		super(code.getErrorMessage());
		this.code2 = code;
	}
	
	public HeraRuntimeException(ExceptionCode code, Object[] codeArgs)
	{
		super(code.getErrorMessage());
		this.code = code;
		this.codeArgs=codeArgs;
	}

	public HeraRuntimeException(ExceptionCode code, Throwable cause)
	{
		super(code.getErrorMessage(), cause);
		this.code = code;
	}

	public HeraRuntimeException(ExceptionCode code, Throwable cause, Object[] codeArgs)
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