package com.demo.api.rest;

import java.sql.Connection;

import javax.annotation.Resource;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.bussiness.Exception.service.ExceptionService;
import com.demo.bussiness.cacheable.service.CacheAbleService;
import com.demo.bussiness.mybatisLocal.service.MybatisService;
import com.demo.bussiness.redis.service.RedisService;

@Component
@Path("nomaltest")
public class NormalTestRest
{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GET
	@Path("/constName")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchName(@Context HttpServletRequest request,String jsonData)
	{
		//http://localhost:8080/demo/rest/nomaltest/constName
		System.out.println(request.getParameter("parm") + "|" + jsonData);
		String name = "const-name";
		System.out.println("name:" + name);
		
		String path1 = request.getRealPath(request.getRequestURI());
		String path2 = request.getRealPath("/");
		String path3 = request.getSession().getServletContext().getRealPath("/");
		
		System.out.println("path1:" + path1);
		System.out.println("path2:" + path2);
		System.out.println("path3:" + path3);
		
		
		return name;
	}
	
	@POST
	@Path("/constPostName")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchPostName(@Context HttpServletRequest request,String jsonData)
	{
		System.out.println(request.getParameter("parm") + "|" + jsonData);
		String name = "const-name";
		System.out.println("name:" + name);
		return name;
	}
	
	@GET
	@Path("/mysql")
	@Produces(MediaType.APPLICATION_JSON)
	public String mysqlTest(@Context HttpServletRequest request,String jsonData) throws Exception
	{
		//  http://10.136.16.161:8091/demo/rest/nomaltest/mysql
//		System.out.println(request.getParameter("parm") + "|" + jsonData);
//		ConnectionManager manager = new ConnectionManager();
//		Connection conn = manager.createConnection();
//		if(!conn.isClosed())
//            System.out.println("Succeeded connecting to the Database!");
//        else
//        	System.out.println("get connecting to the Database fail!");
//		conn.close();
		
		String datasourceName = "java:comp/env/jdbc/InsureDB";
        try
        {
        	InitialContext jndiCtx = new InitialContext();
        	DataSource data = (DataSource) jndiCtx.lookup(datasourceName);
        	Connection conn = data.getConnection();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		
		return "";
	}
	
	@POST
	@Path("/exceptiontest")
	@Produces(MediaType.APPLICATION_JSON)
	public String exceptionTest(@Context HttpServletRequest request,String jsonData)
	{
//		http://my.taikang.com:8091/demo/rest/nomaltest/exceptiontest
		String mobile = "15122255544";
		ExceptionService service = new ExceptionService();
		service.testHrea(mobile);
		try
        {
			service.testHrea(mobile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return "exceptiontest";
	}
	
	@Resource
	MybatisService mybatisService;
	
	@GET
	@Path("/mybatistest")
	@Produces(MediaType.APPLICATION_JSON)
	public String mybatisTest(@Context HttpServletRequest request,String jsonData)
	{
//		http://my.taikang.com:8091/demo/rest/nomaltest/mybatistest
		logger.info(jsonData);
		String name = mybatisService.getSuitNameById("");
		logger.info(name);
		
		return name;
	}
	
	@Autowired
	CacheAbleService cacheTest;
	
	@GET
	@Path("/cacheAbleTest")
	@Produces(MediaType.APPLICATION_JSON)
	public String cacheAbleTest(@Context HttpServletRequest request,String jsonData)
	{
		
//		http://my.taikang.com:8091/demo/rest/nomaltest/cacheAbleTest
		String name = "";
		logger.info(jsonData);
//		CacheAbleService cacheTest = new CacheAbleService();
		cacheTest.testCacheable();
		logger.info(name);
		
		return name;
	}
	
	@GET
	@Path("/redisTest")
	@Produces(MediaType.APPLICATION_JSON)
	public String redisTest(@Context HttpServletRequest request,String jsonData)
	{
		
//		http://10.136.16.161:8091/demo/rest/nomaltest/redisTest
		RedisService service = new RedisService();
		service.service();
		
		return "";
	}
}
