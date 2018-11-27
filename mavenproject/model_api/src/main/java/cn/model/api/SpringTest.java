package cn.model.api;

import cn.model.maven.test.InterceptorTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

@Slf4j
@Path("/rest")
@Controller
public class SpringTest {

    @Autowired
    InterceptorTest interceptorTest;

    /**
     * 1  常规get请求，获取数据，无入参，出参类型：字符串String或者json
     */
    @GET
    @Path("/interceptorTest")
    @Produces("application/json;charset=utf-8")
    public String interceptorTest(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/interceptorTest
        interceptorTest.springaoptest1();
        log.info(".......interceptorTest........");
        String link = "my test interceptorTest";
        return link;
    }

    @GET
    @Path("/interceptornotTest")
    @Produces("application/json;charset=utf-8")
    public String interceptornotTest(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/interceptornotTest
        interceptorTest.springnotaoptest2();
        log.info(".......interceptorTest........");
        String link = "my test interceptorTest";
        return link;
    }

}
