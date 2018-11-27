package cn.model.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlTest {

    public static void main(String[] args) throws Exception {
        // 1.加载数据访问驱动
        Class.forName("com.mysql.jdbc.Driver");
        //2.连接到数据"库"上去
        Connection conn= DriverManager.getConnection("jdbc:mysql://localhost:3306/mytest?characterEncoding=GBK", "root", "admin");
        //3.构建SQL命令
        Statement state=conn.createStatement();
        String s="select * from t_user";
        ResultSet rs;
        rs = state.executeQuery(s);
        while (rs.next())
        {
            String name = rs.getString("username");
            System.out.println("name:" + name);
        }
        rs.close();
        state.close();
        conn.close();
    }

}
