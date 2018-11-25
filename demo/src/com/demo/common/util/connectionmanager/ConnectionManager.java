package com.demo.common.util.connectionmanager;


import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionManager {
    private InitialContext jndiCtx = null;
    private DataSource     ds      = null;
    private static Logger logger = LoggerFactory.getLogger(ConnectionManager.class);
    private String         datasourceName;

    public ConnectionManager(String _datasourceName) {
        this.datasourceName = "java:comp/env/" + _datasourceName;
        try {
            jndiCtx = new InitialContext();
            ds = (DataSource) jndiCtx.lookup(this.datasourceName);
        }
        catch (Exception e) {

            logger.error("Error getting JNDI context ");
            e.printStackTrace();
        }
    }
    
    public ConnectionManager() {
        this.datasourceName = "java:comp/env/jdbc/InsureDB";
        try
        {
            jndiCtx = new InitialContext();
            ds = (DataSource) jndiCtx.lookup(this.datasourceName);
        }
        catch (Exception e)
        {
            logger.error("Error getting JNDI context ");
            e.printStackTrace();
        }
    }

    public Connection createConnection() throws Exception {
        Connection conn = null;
        try {
            // 注意在部署描述文件和部署计划文件中对jdbc/InsureDB进行定义
            conn = ds.getConnection();
            return conn;
        }
        catch (Exception e) {

            logger.error("Error getting Connectioin");
            e.printStackTrace();
            throw e;
        }

    }
}
