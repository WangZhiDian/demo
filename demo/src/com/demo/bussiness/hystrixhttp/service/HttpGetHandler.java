package com.demo.bussiness.hystrixhttp.service;

import com.demo.bussiness.hystrixhttp.bean.HttpRequestInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class HttpGetHandler extends HystrixCommand<String>
{
	private OkHttpClient  client = new OkHttpClient();
	
	private HttpRequestInfo httpRequestInfo;

	
	public HttpGetHandler()
	{
		 super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("httpRequest")));
	}
	
	public HttpGetHandler(HttpRequestInfo httpRequestInfo)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("httpRequest"))
	    .andCommandKey(HystrixCommandKey.Factory.asKey("httpRequestget"))
		.andCommandPropertiesDefaults
		(
			HystrixCommandProperties.Setter()
                        /** Command 配置 */
                        // 使用命令调用隔离方式,默认:采用线程隔离,ExecutionIsolationStrategy.THREAD
			.withExecutionIsolationStrategy(ExecutionIsolationStrategy.THREAD)
                        // 使用线程隔离时，调用超时时间，毫秒
             .withExecutionIsolationThreadTimeoutInMilliseconds(
            		 Integer.parseInt("10000"))
                        // 线程池的key,用于决定命令在哪个线程池执行
                        // 使用信号量隔离时，命令调用最大的并发数,默认:10
             .withExecutionIsolationSemaphoreMaxConcurrentRequests(
            		 Integer.parseInt("10"))
                        // 使用信号量隔离时，命令fallback(降级)调用最大的并发数,默认:10
             .withFallbackIsolationSemaphoreMaxConcurrentRequests(
            		 Integer.parseInt("10"))
                        // 是否开启fallback降级策略 默认:true
             .withFallbackEnabled(true)
                        // 使用线程隔离时，是否对命令执行超时的线程调用中断（Thread.interrupt()）操作.默认:true
             .withExecutionIsolationThreadInterruptOnTimeout(true)
                        // 统计滚动的时间窗口,默认:5000毫秒circuitBreakerSleepWindowInMilliseconds
             .withCircuitBreakerSleepWindowInMilliseconds(Integer.parseInt("5000"))
                        // 统计窗口的Buckets的数量,默认:10个,每秒一个Buckets统计
             .withMetricsRollingStatisticalWindowBuckets(
            		 Integer.parseInt("10"))
                        // 是否开启监控统计功能,默认:true
             .withMetricsRollingPercentileEnabled(true)
                        // 是否开启请求日志,默认:true
             .withRequestLogEnabled(true)
                        // 是否开启请求缓存,默认:true
             .withRequestCacheEnabled(true)
                        /** 熔断器（Circuit Breaker）配置 */
                        // 熔断器在整个统计时间内是否开启的阀值，默认20秒。也就是10秒钟内至少请求20次，熔断器才发挥起作用
             .withCircuitBreakerRequestVolumeThreshold(
            		 Integer.parseInt("20"))
                        // 熔断器默认工作时间,毫秒.熔断器中断请求xx毫秒后会进入半打开状态,放部分流量过去重试
             .withCircuitBreakerSleepWindowInMilliseconds(100)
                        // 是否启用熔断器,默认true. 启动
             .withCircuitBreakerEnabled(true)
                        // 默认:50%。当出错率超过50%后熔断器启动.
             .withCircuitBreakerErrorThresholdPercentage(
            		 Integer.parseInt("50"))
                        // 是否强制开启熔断器阻断所有请求,默认:false,不开启
             .withCircuitBreakerForceOpen(false)
                        // 是否允许熔断器忽略错误,默认false, 不开启
             .withCircuitBreakerForceClosed(false)
          )
		);
		this.httpRequestInfo = httpRequestInfo;
	}

	@Override
    protected String run() throws Exception
    {
		Request request = new Request.Builder().url(this.httpRequestInfo.getUrl()).get().build();
		Response response = client.newCall(request).execute();
//		if (!response.isSuccessful())
//		{
//			throw new IOException("Unexpected code " + response);
//		} 
		String ret = response.body().string();
	    return ret;
    }

	@Override
    protected String getFallback() 
	{
        return "time out 111";
    }
}
