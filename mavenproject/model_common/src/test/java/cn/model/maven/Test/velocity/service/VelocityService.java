package cn.model.maven.Test.velocity.service;

import cn.model.maven.Test.velocity.bean.Person;
import cn.model.maven.commons.velocity.VelocityEngineService;
import cn.model.maven.commons.velocity.impl.VelocityEngineServiceImpl;
import com.alibaba.fastjson.JSONObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.StringWriter;

public class VelocityService
{
	
	public String velocityStr(String template, Person person)
	{
		VelocityEngine ve = new VelocityEngine();
		ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		ve.init();
		
		VelocityContext cxt = new VelocityContext();
		cxt.put("person", person);
		cxt.put("title", "velocity");
		
		StringWriter sw = new StringWriter();
		ve.evaluate(cxt, sw, "", template);
		
		return sw.toString();
	}
	
//	@Test
	public void test()
	{
		String template = "title:$title ,content:{myname:$person.name, mygender:$person.sex, myaddr:$person.addr}";
		Person person = new Person("wangtest", "male", "beijing");
		String out = velocityStr(template, person);
		System.out.println(out);
	}
	
	@Autowired
	private VelocityEngineService velocityEngineService1;
	
	@Test
	public void Tests()
	{
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("name", "wangdian");
		jsonobj.put("sex", "male");
		jsonobj.put("addr", "beijing");
		System.out.println(jsonobj.toString());
		String tempStr = "title:title ,content:{myname:$json.name, mygender:$json.sex, myaddr:$json.addr}";
		VelocityContext context = new VelocityContext();
		context.put("json", jsonobj);
		VelocityEngineServiceImpl velocityEngineService1 = new VelocityEngineServiceImpl();
		String orderinfo = velocityEngineService1.transferString(context, tempStr);
		System.out.println(orderinfo);
	}
}
