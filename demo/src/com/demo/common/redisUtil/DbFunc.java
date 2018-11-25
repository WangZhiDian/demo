package com.demo.common.redisUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;

public class DbFunc {

	private static ConcurrentHashMap<String, String> appConfigMap=new ConcurrentHashMap<String, String>();

	
	//从数据库中取得应用配置参数的值
	 public static String getAppConfig(String operationType,String keyCategory,String keyName)
	{
	    String key=operationType+"|"+keyCategory+"|"+keyName;
	    String retval = appConfigMap.get(key);
	    if(retval == null) {
   		Connection conn=null;
   		Statement st=null;
   		ResultSet rs = null;
   		try
   		{
   			ConnPool pool=new ConnPool();
   			conn=pool.createConnection();
   			String strSql = "select key_value from p_appconfig where "+" OPERATION_TYPE='"+operationType+"' AND KEY_CATEBORY='"+keyCategory+"' and KEY_NAME='"+keyName+"' and VALID='Y'";
   			//System.out.println(strSql);
   			st=conn.createStatement();
   			rs = st.executeQuery(strSql);
   			if (rs.next())
   			{
   				retval = rs.getString(1);
   			}
   		}
   		catch (Exception e)
   		{
   			System.out.println("getAppConfig:"+e);
   			e.printStackTrace();
   		}
   		finally
   		{
   			try
   			{
   				if(rs != null)	rs.close();
   				if(st != null )	st.close();
   				if(conn != null)
   				{
   					conn.close();
   				}
   			}
   			catch (Exception e)
   			{
   				System.out.println("Error in getAppconfig:close connection:"+e);
   			}
   		}

   		if(retval!=null) {
   		    appConfigMap.put(key, retval);
   		}
	    }
		return retval;
	}	
	 
}
