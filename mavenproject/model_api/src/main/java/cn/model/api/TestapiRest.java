package cn.model.api;

/**
 * jersey 注解总结:
 * 一 jersey使用jar包：
 ·      核心服务器：jersey-core.jar，jersey-server.jar，jsr311-api.jar，asm.jar
 ·      核心客户端：（用于测试）jersey-client.jar
 ·      JAXB 支持：（在高级样例中使用）jaxb-impl.jar，jaxb-api.jar，activation.jar，stax-api.jar，wstx-asl.jar
 ·      JSON 支持：（在高级样例中使用）jersey-json.jar
 ·      Spring支持：（在高级样例中使用）jersey-spirng.jar
   二 生存周期
 1.       默认不使用注解，表示生存周期等于request，请求过后自动销毁，默认是线程安全的。
 2.       application，使用@Singleton注解。生存周期等于整个应用程序的生存周期。
 3.       session,使用@PerSession注解。生存周期等于一个session请求，session销毁，该rest资源实例也同时销毁。
 * 三 jersey bean常用注解：
 * 1.@Path
 路径信息，表示映射出去的访问路径。
 范例如下：@Path("/myResource")
 2. @Produces
 用于限制post和get方法返回的参数类型，支持json、string、xml、html
 范例如下：@Produces({"application/xml", "application/json"})   @produces(MediaType.TEXT_HTML);
 3. @Consumes
 用于限制输入的参数的类型，支持json、string、xml、html
 范例如下：@Consumes("text/plain")
 4. @QueryParam
 通过request传入的参数，可以转换任何有以String为参数的构造函数的类。
 5.@DefaultValue
 @DefaultValue表示默认参数。
 范例如下：@DefaultValue("2") @QueryParam("step")        int step,
 5. @PathParam
 @ MatrixParam，@ HeaderParam，@ CookieParam和@ QueryParam FormParam听从以相同的规则。
 @ MatrixParam提取URL路径段的信息。 @ HeaderParam提取的HTTP头信息。 @ CookieParam提取信息的Cookie饼干宣布相关的HTTP标头。
 @ FormParam略有特殊，因为它提取请求表示，该类型匹配前面的@Consumes所声明的类型。
 范例如下：
 @POST
 @Consumes("application/x-www-form-urlencoded")
 public void post(@FormParam("name")   String name) {
 6.pojo层面等相关注解，@XmlRootElement,支持JPA注解。
 7.Spring相关注解，比如@Autowired(required=true) 、@Qualifier("persionDao")、@Component
 @Scope("request")
 8.Context 在一个大型的server中，因为参数的多变，参数结构的调整都会因为以上几种方式而遇到问题，这时可以考虑使用@Context 注释
 通过@Context 注释获取ServletConfig、ServletContext、HttpServletRequest、HttpServletResponse和HttpHeaders等
 */


import cn.model.maven.domain.UserBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * Created by wangdian05 on 2018/6/5.
 */
@Slf4j
@Path("/rest")
@Controller
public class TestapiRest {

    /**
     * 1  常规get请求，获取数据，无入参，出参类型：字符串String或者json
     */
    @GET
    @Path("/getLink")
    @Produces("application/json;charset=utf-8")
    public String getLink(@Context HttpServletRequest request)
    {
        //   http://localhost:8080/model_api/rest/rest/getLink
        HttpServletRequest rin = request;
        log.info(".......getLink........");
        String link = "my test getLink";
        return link;
    }

    /**
     * 2  常规get请求，获取数据, 接收入参QueryParam, 该参数获取get的url链接问号后面的参数
     */
    @GET
    @Path("/getParamLink")
    @Produces("application/json;charset=utf-8")
    public String getParamLink(@QueryParam("name") String name,
                               @QueryParam("age") int age)
    {
        //   http://localhost:8080/model_api/rest/rest/getParamLink?name=wang&age=10
        String inName = name;
        int inAge = age;
        log.debug("name: {}, age {}", name, age);
        JSONObject obj = new JSONObject();  obj.put("name", inName);
        return obj.toJSONString();
    }

    /**
     * 3  常规get请求，获取数据, 接收入参QueryParam, 该参数获取get的url链接问号后面的参数,拥有默认值
     */
    @GET
    @Path("/getDefaultParamLink")
    @Produces("application/json;charset=utf-8")
    public String getDefaultParamLink(@DefaultValue("wang_tian") @QueryParam("name") String name,
                               @QueryParam("age") int age)
    {
        //   http://localhost:8080/model_api/rest/rest/getDefaultParamLink?age=10
        int inAge = age;
        String inName = name;
        log.debug("name: {}, age {}", name, age);
        JSONObject obj = new JSONObject();  obj.put("name", inName);
        return obj.toJSONString();
    }

    /**
     * 4  常规get请求，获取数据, 接收入参PathParam, 将路径总的值作为参数
     */
    @GET
    @Path("/getPathParamLink/{password}")
    @Produces("application/json;charset=utf-8")
    public String getPathParamLink( @PathParam("password") String password)
    {
        //   http://localhost:8080/model_api/rest/rest/getPathParamLink/123
        String inpathword = password;
        log.debug("inpathword: {}", inpathword);
        JSONObject obj = new JSONObject();  obj.put("inpathword", inpathword);
        return obj.toJSONString();
    }

    /**
     * 5  常规post请求，获取数据，无入参，出参类型：字符串String或者json
     */
    @POST
    @Path("/postLink")
    @Produces("application/json;charset=utf-8")
    public String postLink()
    {
        //   http://localhost:8080/model_api/rest/rest/postLink
        String link = "my post test postLink";
        JSONObject obj = new JSONObject();
        obj.put("aa", "aa");
        return obj.toJSONString();
    }

    /**
     * 6  常规post请求，获取数据,获取一般post中请求体中的数据
     */
    @POST
    @Path("/getPostLink")
    @Produces("application/json;charset=utf-8")
    public String getPostLink(@Context HttpServletRequest request,
                                String jsonData)
    {
        //   http://localhost:8080/model_api/rest/rest/getPostLink
        JSONObject obj = JSON.parseObject(jsonData);
        log.info(obj.toString());
        return obj.toJSONString();
    }

    /**
     * 7  常规post请求，获取数据,获取一般form表单中的某个或者某些数据值，需要设置consumes类型
     * 该接收方式时，jsonData接收的为所有key=value的组合，FormParam为单个form表单元素
     */
    @POST
    @Path("/getFormPostLink")
    @Produces("application/json;charset=utf-8")
    @Consumes("application/x-www-form-urlencoded")
    public String getFormPostLink(@FormParam("name") String name,
                              String jsonData)
    {
        //   http://localhost:8080/model_api/rest/rest/getFormPostLink
        log.info(jsonData);
        String inName = name;
        return inName;
    }

    /**
     * 8  常规post请求，获取数据,获取bean对象数据并转化成bean对象
     */
    @POST
    @Path("/getBeanPostLink")
    @Produces("application/json;charset=utf-8")
    public String getBeanPostLink(@Context HttpServletRequest request,
                              String jsonData)
    {
        //   http://localhost:8080/model_api/rest/rest/getBeanPostLink
        UserBean bean = JSONObject.parseObject(jsonData, UserBean.class);
        log.info(bean.toString());
        return bean.toString();
    }

    /**-- rest 跳转 ----------------------------------------------------------------------------------------------------------------------------------------------------------------- */

    /**
     * 9  返回类型为MediaType.TEXT_HTML时，直接打来浏览器,当返回的数据为表单或者为html页面，直接打开表单.当返回的数据不为表单时，直接返回string 内容
     */
    @GET
    @Path("/getHtmlRedirect")
    @Produces(MediaType.TEXT_HTML)
    public String getHtmlRedirect(@Context HttpServletRequest request, String value)
    {
        //   http://localhost:8080/model_api/rest/rest/getHtmlRedirect
        log.info("开始跳转,跳转传入得参数为.payOrderRedirect notify..:{}", value);
        String linkValue = "http://localhost:8080/model_api/restTest.html";          //该形式不会被浏览器解析，浏览器打印 http://localhost:8080/model_api/restTest.html
//        String linkValue = "<html><body><h2>test my restTest</h2></body></html>";  //该形式会被浏览器解析，打印test my restTest 字符串

        return linkValue;
    }

    /**
     * 10 使用JAX-RS Response跳转,该跳转方式，于9很相似，会将entity里面的数据当做字符串解析，如果带了html标签，会将html标签解析成网页
     * 当entity为表单或者html时，浏览器可以解析
     */
    @GET
    @Path("/jumpTo")
    public Response jumpToEntity(@Context HttpServletRequest request, String value)
    {
        //   http://localhost:8080/model_api/rest/rest/jumpToEntity
        log.info("开始跳转,跳转传入得参数为.jumpToEntity ..:{}", value);
        String linkValue = "http://localhost:8080/model_api/restTest.html";
        String linkValue2 = "http://localhost:8080/model_api/rest/rest/getLink";
        return Response.status(Response.Status.FOUND).entity(linkValue).build();
    }

    /**
     * 11 使用JAX-RS Response跳转, 该方法跳转，直接response跳转到对应的uri链接
     */
    @GET
    @Path("/jumpToLocation")
    public Response jumpToLocation(@Context HttpServletRequest request, String value) throws URISyntaxException {
        //   http://localhost:8080/model_api/rest/rest/jumpToLocation
        log.info("开始跳转,跳转传入得参数为.jumpToLocation ..:{}", value);
        String linkValue = "http://localhost:8080/model_api/restTest.html";
        String linkValue2 = "http://localhost:8080/model_api/rest/rest/getLink";
        return Response.status(Response.Status.FOUND).location(new URI(linkValue2)).header("Referer", "http://my.test.com").build();
    }



}
