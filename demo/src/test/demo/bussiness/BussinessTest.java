package test.demo.bussiness;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.demo.bussiness.jsonschema.service.JsonSchemaServcie;

public class BussinessTest
{
	/**
	 * test JsonSchema
	 */
	@Test
	public void testJsonSchema()
	{
		String reqJson = "{\"billno\": \"0000004670205074740272\",\"amount\": 1.1,\"isdistort\": false}";
		
		JsonSchemaServcie jsonSchemaService; jsonSchemaService = new JsonSchemaServcie();
		boolean flag = jsonSchemaService.jsonSchemaValidation(reqJson);
		assertTrue(flag);
	}
	
	
}
