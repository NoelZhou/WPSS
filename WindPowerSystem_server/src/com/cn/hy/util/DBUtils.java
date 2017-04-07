package com.cn.hy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtils {
	public static final String url = "jdbc:mysql://localhost:3306/windpower_db";
	public static final String name = "com.mysql.jdbc.Driver";
	public static final String user = "root";
	public static final String password = "123456";

	public Connection conn = null;
	public PreparedStatement pst = null;

	public DBUtils() {
		try {
			Class.forName(name);//指定连接类型
			conn = DriverManager.getConnection(url, user, password);//获取连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runSql(String sql){
		try {
			pst = conn.prepareStatement(sql);//准备执行语句
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			this.pst.close();
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

