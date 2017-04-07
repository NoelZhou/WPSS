package com.zhta.bean;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JDBConnection {
	
	public static Connection getConnect(){
		Connection conn=null; 
		  try
		  {
			   //初始化查找命名空间
			   Context ctx = new InitialContext();
			   //找到DataSource,对名称进行定位java:comp/env是必须加的,后面跟你的DataSource名
			   DataSource ds = (DataSource)ctx.lookup("java:comp/env/jdbc/mysql");
			   //取出连接
			   conn = ds.getConnection();
		  } catch (NamingException e) {
			  System.err.println("数据源没找到!");
		  } catch (SQLException e) {
			  System.err.println("获取数据连接失败!");
		  }
		 return conn;
	}
}
