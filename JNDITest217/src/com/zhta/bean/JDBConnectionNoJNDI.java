package com.zhta.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JDBConnectionNoJNDI {
	public Connection connection = null;
	public JDBConnectionNoJNDI() {
		ResourceBundle bundle = ResourceBundle.getBundle("db");
		String driver = bundle.getString("dbDriver");
		String url = bundle.getString("url");
		String user = bundle.getString("userName");
		String password = bundle.getString("password");
		
		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.out.println(e.toString());
			System.out.println("数据库加载失败");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}