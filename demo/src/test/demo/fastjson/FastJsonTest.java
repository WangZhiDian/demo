
package test.demo.fastjson;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;


public class FastJsonTest
{
	@Test
	public void map2json()
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "22");
		map.put("3", "22");
		map.put("2", "22");
		String jsonString = JSON.toJSONString(map);
		System.out.println(jsonString);
		Map<String, String> parse = (Map<String, String>) JSON.parse(jsonString);
		System.out.println(parse.toString());
	}

	@Test
	public void javabean2json()
	{
		JPerson person = new JPerson();
		person.setName("wangwc");
		person.setAge("20");
		person.setAddress("520");
		String jsonString = JSON.toJSONString(person);
		System.out.println(jsonString);
		JPerson person2 = JSON.parseObject(jsonString, JPerson.class);
		System.out.println(person2);
	}
	@Test
	public void fastJsonFilter()
	{
		JPerson person = new JPerson();
		person.setName("wangwc");
		person.setAge("20");
		person.setAddress("520");
		SimplePropertyPreFilter filter=new SimplePropertyPreFilter("name","age");
		System.out.println(JSON.toJSONString(person,filter));
	}
	@Test
	public void fastJsonFilterExcludes()
	{
		JPerson person = new JPerson();
		person.setName("wangwc");
		person.setAge("20");
		person.setAddress("520");
		SimplePropertyPreFilter filter=new SimplePropertyPreFilter();
		Set<String> excludes = filter.getExcludes();
		excludes.add("address");
		System.out.println(JSON.toJSONString(person,filter));
	}
}
