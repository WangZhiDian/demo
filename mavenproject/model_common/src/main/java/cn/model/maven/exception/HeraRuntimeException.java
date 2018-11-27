package cn.model.maven.exception;

/**
 * 
 * @description 服务异常，比如调用支付通道失败、数据库访问失败、缓存访问失败要记录并且抛出
 * @author wanghl80
 * @date 2017年9月6日 上午10:46:36
 * @version V1.0
 *
 */
public class HeraRuntimeException extends HeraException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1723355624334958360L;

	// 包含ExceptionCode的构造函数
	public HeraRuntimeException(ExceptionCode code)
	{
		super(code);
	}

	public HeraRuntimeException(ExceptionCode code, Object[] codeArgs)
	{
		super(code, codeArgs);
	}

	public HeraRuntimeException(ExceptionCode code, Throwable cause)
	{
		super(code, cause);
	}

	public HeraRuntimeException(ExceptionCode code, Throwable cause, Object[] codeArgs)
	{
		super(code, cause, codeArgs);
	}
}