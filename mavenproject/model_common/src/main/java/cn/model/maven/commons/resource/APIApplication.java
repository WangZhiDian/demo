package cn.model.maven.commons.resource;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created by wangdian05 on 2018/6/6.
 */
public class APIApplication  extends ResourceConfig {

    public APIApplication()
    {
        // 服务类所在的包路径
        packages("cn.model.api");

        // 注册过滤器
        // register(TkContainerRequestFilter.class);
        // register(TKContainerResponseFilter.class);

        // 注册异常
        register(HeraExceptionMapper.class);

        // 注册 MultiPart，上传必须注册
        register(MultiPartFeature.class);

        // 注册JSON解析器
        register(JacksonJsonProvider.class);
    }
}
