package test.demo.first;

import java.util.HashMap;

public class Te 
{
	public String print1()
	{
		
		t2();
		System.out.println("2");
		return "a";
	}
	
	void t2()
	{
//		Be b = getB();
//		b.print1();
		
		HashMap b = getH();
		b.put("a", "a");
		
		System.out.println("suc");
	}
	
	HashMap getH()
	{
		HashMap m = new HashMap();
		return m;
	}
	
	Be getB()
	{
		Be b = new Be();
		
		return null;
	}
	
	public class Be
	{
		public String print1()
		{
			
			return "a";
		}
	}
	
	
}

