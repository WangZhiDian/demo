package com.demo.bussiness.jsonschema.service;


import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class JunitTest 
{
	
	JsonSchemaServcie jsonSchemaService;
	
	@Test
	public void testJsonSchema()
	{
		jsonSchemaService = new JsonSchemaServcie();
		JSONObject obj = new JSONObject();
		JSONObject objApplicant = new JSONObject();
		objApplicant.put("name", "wang");
		obj.put("name", "wangtest");
		obj.put("applicants", objApplicant);
		jsonSchemaService.jsonSchemaValidation(obj.toString());
	}
	
	
}
