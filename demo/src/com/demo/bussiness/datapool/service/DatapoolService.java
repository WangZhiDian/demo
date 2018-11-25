package com.demo.bussiness.datapool.service;

import java.sql.Connection;

import org.junit.Test;

import com.demo.common.util.connectionmanager.ConnectionManager;

public class DatapoolService
{
	@Test
	public void tests() throws Exception
	{
		ConnectionManager manager = new ConnectionManager();
		Connection conn = manager.createConnection();
		if(!conn.isClosed())
            System.out.println("Succeeded connecting to the Database!");
        else
        	System.out.println("get connecting to the Database fail!");
		conn.close();
	}
}
