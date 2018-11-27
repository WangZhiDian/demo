package cn.model.maven.exception;

/**
 * 
 * @description  业务异常，日志里面只需要记录错误，不需要抛出异常，错误信息需要返回前端，该异常包含两类：
 * 					1.调用接口传递的参数异常，该异常日志里面不需要抛出exception
 * 					2.业务处理逻辑异常，比如在犹豫期申请理赔
 * @author       wanghl80
 * @date         2017年9月6日 上午10:45:51
 * @version      V1.0
 *
 */
public class IllegalBusinessException extends HeraException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7322532127211286132L;

	public IllegalBusinessException(ExceptionCode code)
	{
		super(code);
	}

	public IllegalBusinessException(ExceptionCode code, Object[] codeArgs)
	{
		super(code,codeArgs);
	}

	public IllegalBusinessException(ExceptionCode code, Throwable cause)
	{
		super(code,cause);
	}

	public IllegalBusinessException(ExceptionCode code, Throwable cause, Object[] codeArgs)
	{
		super(code,cause,codeArgs);
	}
}
