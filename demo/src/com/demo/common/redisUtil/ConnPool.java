package com.demo.common.redisUtil;

/**
 * Generated by SilverStream XSLT Code Generator, version 1.0.
 * This generated source file may be freely modified.
 */
import java.sql.Connection;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
    ConnPool

*/
public class ConnPool
{
    private static InitialContext jndiCtx = null;
    private static DataSource ds=null;
    
	// 使用单实例模式
	private static class LazyHolder {
		private static final ConnPool INSTANCE = new ConnPool("init");
	}

	// 获取当前实例
	public static ConnPool getInstance() {
		return LazyHolder.INSTANCE;
	}
	
	public void init(){
    	try {
    		if(ds == null){
    			jndiCtx = new InitialContext();
    			ds = (DataSource) jndiCtx.lookup("java:comp/env/jdbc/InsureDB");
    		}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ConnPool:"+e);
		}
	}
	//private DBConnectionManager connMgr;
	
    public ConnPool()
    {
    	ConnPool.getInstance();
    }
    
    public ConnPool(String flag){
    	init();
    }
	
	
	
    public Connection createConnection()
    {
    	if(ds == null) init();
    	Connection conn = null;
        try 
		{
             // 注意在部署描述文件和部署计划文件中对jdbc/InsureDB进行定义     
            conn = ds.getConnection();
		}
		catch (Exception e) 
		{
			
			System.out.println("Error getting JNDI context ");
			e.printStackTrace();
       	}
		finally
		{
			return conn;
		}
		
    }   
}
