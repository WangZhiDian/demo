package com.demo.bussiness.jsonpath.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

import com.demo.bussiness.jsonpath.bean.Books;

public class JsonPathService
{
/*	{
	    "store": {
	        "book": [
	            {
	                "category": "reference",
	                "author": "Nigel Rees",
	                "title": "Sayings of the Century",
	                "price": 8.95
	            },
	            {
	                "category": "fiction",
	                "author": "Evelyn Waugh",
	                "title": "Sword of Honour",
	                "price": 12.99
	            },
	            {
	                "category": "fiction",
	                "author": "Herman Melville",
	                "title": "Moby Dick",
	                "isbn": "0-553-21311-3",
	                "price": 8.99
	            },
	            {
	                "category": "fiction",
	                "author": "J. R. R. Tolkien",
	                "title": "The Lord of the Rings",
	                "isbn": "0-395-19395-8",
	                "price": 22.99
	            }
	        ],
	        "bicycle": {
	            "color": "red",
	            "price": 19.95
	        }
	    },
	    "expensive": 10
	}*/
	
	public void service()
	{
		String json = "{\"store\": {\"book\": [{\"category\": \"reference\",\"author\": \"Nigel Rees\",\"title\": \"Sayings of the Century\",\"price\": 8.95},{\"category\": \"fiction\",\"author\": \"Evelyn Waugh\",\"title\": \"Sword of Honour\",\"price\": 12.99},{\"category\": \"fiction\",\"author\": \"Herman Melville\",\"title\": \"Moby Dick\",\"isbn\": \"0-553-21311-3\",\"price\": 8.99},{\"category\": \"fiction\",\"author\": \"J. R. R. Tolkien\",\"title\": \"The Lord of the Rings\",\"isbn\": \"0-395-19395-8\",\"price\": 22.99}],\"bicycle\": {\"color\": \"red\",\"price\": 19.95}},\"expensive\": 10}";
		System.out.println(json);
		JSONObject obj = JSONObject.parseObject(json);
		System.out.println(obj.toString());
		List<String> authors = JsonPath.read(obj, "$.store.book[?(@.isbn != null)].author");
		String newStr = JsonPath.parse(json).set("$.store.book[0].author", "wangdian").toString();
		System.out.println("new Str:" + newStr);
		
		String bookName1 = JsonPath.read(obj, "$.store.book[0].author");
		System.out.println("bookName1:" + bookName1);
		List<String> booknames = JsonPath.read(obj, "$.store.book[*].author");
		System.out.println(booknames.toString());
		
		//object
		Books book = JsonPath.parse(json).read("$.store.book[0]", Books.class);
		
		//expression
		List<Map<String, Object>> books =  JsonPath.parse(json).read("$.store.book[?(@.price < 10 && @.category == 'fiction')]");
		
		//filter
		
//		Book book = JsonPath.parse(json).read("$.store.book[0]", Book.class);
		String json1 = "{\"date_as_long\" : 1411455611975}";

		Date date = JsonPath.parse(json1).read("$['date_as_long']", Date.class);
		System.out.println(date.toGMTString());
	}
	
	@Test
	public void maintest()
	{
		service();
	}

	public static void main(String[] args)
	{
		new JsonPathService().service();
	}
}
