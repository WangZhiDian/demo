package com.demo.bussiness.jsonschema.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

/**
 * jsonSchema 校验前端传参数的json格式是否正确,可以用正则表达式验证json中的各个字段
 * 例子：{"$schema": "http://json-schema.org/draft-04/schema#","type": "object","properties": {"openId": {"type": "string","pattern": "^\\\\S+$"},"suitId": {"type": "string"},"checkCode": {"type": "string","pattern": "^[0-9]{6}$"},"channelId": {"type": "string","pattern": "^wx$"},"applicants": {"type": "object","properties": {"name": {"type": "string","pattern": "^\\\\S{2,15}$"},"identifyNo": {"type": "string","pattern": "^([0-9]{4}([0-9]{10}|[*]{10})[0-9]{3}[0-9Xx])|([0-9*]{15})$"},"phoneNo": {"type": "string","pattern": "1[0-9]{2}([0-9]{4}|[*]{4})[0-9]{4}"},"identifyType": {"type": "string","pattern": "^01$"}},"required": ["name","identifyNo","phoneNo","identifyType"]},"genericData": {"type": "object","properties": {"startTime": {"type": "string"},"processHandler": {"type": "string","pattern": "^property$"},"provinceName": {"type": "string","pattern": "^\\\\S+$"},"cityName": {"type": "string","pattern": "^\\\\S+$"},"areaName": {"type": "string","pattern": "^\\\\S+$"},"fieldAi": {"type": "string","pattern": "^\\\\S+$"}},"required": ["startTime","processHandler","provinceName","cityName","areaName","fieldAi"]}},"required": ["openId","suitId","checkCode","channelId","applicants","genericData"]}
 * @author wangdian05
 *
 */

@Service
public class JsonSchemaServcie
{
	public boolean jsonSchemaValidation(String reqJson)
	{
		String jsonSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"},\"applicants\":{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}},\"required\":[\"name\"]}},\"required\":[\"name\",\"applicants\"]}";
		boolean ret = false;
		try
		{
			ObjectMapper oMapper = new ObjectMapper();
		
			final JsonNode fsSchema = oMapper.readTree(jsonSchema);
			final JsonNode reqSchema = oMapper.readTree(reqJson);
			
			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
			JsonSchema schema = factory.getJsonSchema(fsSchema);
			ProcessingReport report = schema.validate(reqSchema);
			
			if(report.isSuccess())
			{
				ret = true;
				System.out.println("true");
			}else
				System.out.println("false");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ProcessingException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
