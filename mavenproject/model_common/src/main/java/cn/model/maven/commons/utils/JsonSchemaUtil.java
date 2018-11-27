package cn.model.maven.commons.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class JsonSchemaUtil
{
	/*
	section : 需要被验证的schema格式；
	reqJsonString：需要被验证的jsonString；
	 */
	public String validate(String section, String reqJsonString)
	{
		String tree = section;
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
	
	public String validate(String section, HashMap<String, String> map)
	{
		String retString = "";
		try
        {
			String tree = section;
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
