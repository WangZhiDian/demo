package test.demo.first;

public class Static1
{
	private static String parm = "aaa";

	public static String getParm() {
		return parm;
	}

	public static void setParm(String parm) {
		Static1.parm = parm;
	}
	
}
