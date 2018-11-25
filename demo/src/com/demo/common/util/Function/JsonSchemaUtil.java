package com.demo.common.util.Function;

import java.util.HashMap;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@Service
@Slf4j
public class JsonSchemaUtil
{
	public String validate(String section, String keyName,String reqJsonString)
	{
		String tree = "need modify";
		String retString = "";
		try
        {
			ObjectMapper oMapper  = new ObjectMapper();
			final JsonNode fstabSchema = oMapper.readTree(tree);
			final JsonNode reqSchema = oMapper.readTree(reqJsonString);
			
			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	        final JsonSchema schema = factory.getJsonSchema(fstabSchema);
	        ProcessingReport report = schema.validate(reqSchema);
	        if(report.isSuccess())
	        {
	        	retString = "ok";
	        }
	        else
	        {
	        	retString = report.toString();
	        	log.debug("JsonSchemaUtil validate fail {}" , retString);
	        }
        }
        catch (Exception e)
        {
	        retString = "SYSTEM_ILLEGAL_REQUEST";
	        log.debug("JsonSchemaUtil validate Exception {}" , e.toString());
	        e.printStackTrace();
        }
        
        return retString;
	}
	
	public String validate(String section, String keyName,HashMap<String, String> map)
	{
		String retString = "";
		try
        {
			String tree = "need modify";
			ObjectMapper oMapper  = new ObjectMapper();
			final JsonNode fstabSchema = oMapper.readTree(tree);
			final JsonNode reqSchema = oMapper.readTree(JSONObject.toJSONString(map));
			
			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
	        final JsonSchema schema = factory.getJsonSchema(fstabSchema);
	        ProcessingReport report = schema.validate(reqSchema);
	        if(report.isSuccess())
	        {
	        	retString = "ok";
	        }
	        else
	        {
	        	retString = report.toString();
	        }
        }
        catch (Exception e)
        {
	        retString = "SYSTEM_ILLEGAL_REQUEST";
        }
        
        return retString;
	}
}
