package com.demo.api.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.bussiness.batch.service.BonusHera;
import com.demo.bussiness.batch.service.SimplyDidruptorBatchServiceTest;
import com.demo.bussiness.disruptor.service.DisruptorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import test.core.batch.disruptor.StringAsync;

@Component
@Path("disruptor")
public class DisruptorRest
{
//	http://10.136.16.161:8091/demo/rest/disruptor/constName
	private final Logger logger=LoggerFactory.getLogger(DisruptorRest.class);
	
	@Autowired
	DisruptorService disruptorService;
	
	@Autowired
	SimplyDidruptorBatchServiceTest batchServiceTest;
	
	@GET
	@Path("/searchName")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchName()
	{
		String name = "";
		
		return name;
	}
	
	@GET
	@Path("/startDisruptor")
	@Produces(MediaType.APPLICATION_JSON)
	public String startDisruptor()
	{
		String ret = disruptorService.dealDisruptor();
		
		return ret;
	}
	
	@POST
	@Path("/publishAnEvent")
	@Produces(MediaType.APPLICATION_JSON)
	public String publishAnEvent(@Context HttpServletRequest request,String jsonData)
	{
		System.out.println(request.getParameter("parm1") + " | " + jsonData);
		String name = request.getParameter("parm1");
		String ret = disruptorService.publishAnEvent(name);
		
		return ret;
	}
	
	@POST
	@Path("/publishAnEvent2")
	@Produces(MediaType.APPLICATION_JSON)
	public String publishAnEvent2(@Context HttpServletRequest request,String jsonData)
	{
		System.out.println(request.getParameter("parm1") + " | " + jsonData);
		String name = request.getParameter("parm1");
		disruptorService.publishEvent2(name);
		
		return "";
	}
	
	//----------------------------------------------------------------------------
	@GET
	@Path("/disruptorTest")
	@Produces(MediaType.APPLICATION_JSON)
	public String disruptorTest()
	{
		BonusHera value = new BonusHera();
		value.setCname("wangtian");
		value.setAddr("taikang online");
		batchServiceTest.add(value);
		
		return value.getCname();
	}
	
	@GET
	@Path("/disruptorTask")
	@Produces(MediaType.APPLICATION_JSON)
	public String disruptorTask()
	{
		//利用disruptor调用定时任务
		try
		{
			logger.debug("debug disruptor tase start");
			logger.info("info disruptor tase start");
			logger.error("error disruptor tase start");
			logger.trace("trace disruptor tase start");
			logger.warn("warn disruptor tase start");
			StringAsync async = new StringAsync();
			async.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}
}
