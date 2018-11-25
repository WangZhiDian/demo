package test.other.JieSuanDanLogin;

import org.junit.Test;

import test.demo.first.Md5;

public class JieSuanDan {

	@Test
	public void testMd5()
	{
		String pass = "taikang321";
		String a = Md5.getMD5Mac(pass);
		System.out.println(a);
		
		pass = "taikang123";
		a = Md5.getMD5Mac(pass);
		System.out.println(a);
		
		pass = "taikang12";
		a = Md5.getMD5Mac(pass);
		System.out.println(a);
		
		pass = "datongbaoxian";
		a = Md5.getMD5Mac(pass);
		System.out.println(a);
	}
	
}


