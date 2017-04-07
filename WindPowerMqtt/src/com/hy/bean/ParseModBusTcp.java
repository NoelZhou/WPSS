package com.hy.bean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hy.pojo.ModbusBit;

public class ParseModBusTcp {
	private static Connection conn = null;
	private static Statement st = null;
	private static JDBConnection jdbc = null;
	private static ResultSet rs = null;

	/**
	 * 解析
	 * @param addr
	 * @param shortvalue
	 * @param device_type
	 * @param modbustsr
	 * @return
	 */
	public static List<String> parseModBusTcp(int addr, int shortvalue, int device_type,String modbustsr ) {
		int ctbit = getaddrbit(addr, device_type);
		int ctvar = getaddrvar(addr, device_type);
		List<String> modbusvaluelist = new ArrayList();
		int array_type=getaddrarrarytype(addr,device_type);
		
		// bit位解析
		// 特殊位置的解析
		// 双馈 使用字符比较 不能转换成int
		if (addr == 233 && device_type == 0) {
			if (modbustsr.split(",")[232].equals("51")) {
				// 获取84位的bit位置
				List<ModbusBit> bitlist =getaddrbitlistbyid(84,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
			}else if(modbustsr.split(",")[232].equals("68")){
				List<ModbusBit> bitlist = getaddrbitlist(addr,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
			}
		}else if (addr == 73 && device_type == 0) {
			if (modbustsr.split(",")[72].equals("51")) {
				// 获取84位的bit位置
				List<ModbusBit> bitlist =getaddrbitlistbyid(54,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
			}else if(modbustsr.split(",")[72].equals("68")){
				List<ModbusBit> bitlist =getaddrbitlist(addr,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
			}
		}  
		else if (addr == 233 && device_type == 1) {
			//全功率
			if (modbustsr.split(",")[232].equals("4147")) {
				// 获取84位的bit位置
			String getvar =getvarbyid(device_type,shortvalue,114);
			modbusvaluelist.add("1");
			modbusvaluelist.add(getvar);
			}else if(modbustsr.split(",")[232].equals("238")){
				List<ModbusBit> bitlist =getaddrbitlist(addr,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
			}
		}  else if (addr == 73 && device_type == 1) {
			//全功率
			if (modbustsr.split(",")[72].equals("4147")) {
				// 获取84位的bit位置   位4147是 解析到id位54位上
				
				String getvar =getvarbyid(device_type,shortvalue,54);
				modbusvaluelist.add("1");
				modbusvaluelist.add(getvar);
			}else if(modbustsr.split(",")[72].equals("238")){
				List<ModbusBit> bitlist =getaddrbitlist(addr,device_type);
				modbusvaluelist= getbitlist(bitlist,shortvalue);
				
			}
		}  
		else if (ctbit != 0) {
			//bit值组合
			List<ModbusBit> bitlist =getaddrbitlist(addr,device_type);
			modbusvaluelist= getbitlist(bitlist,shortvalue);

		} else if (ctvar != 0) {
			// var值解析
			String str = getaddvarvalue(addr,device_type,shortvalue);
			if (str == null || str == "") {
				modbusvaluelist.add("4");
				modbusvaluelist.add("地址解析错误：" + addr + "位的值" + shortvalue + "，设备类型为：" + device_type);
			} else {
				modbusvaluelist.add("1");
				modbusvaluelist.add(str);
			}
		} 
		 else{
			modbusvaluelist.add(shortvalue + "");
			modbusvaluelist.add("2");
		}
		// 需要16进制显示的addr事哪些
		// return "地址解析错误";
		return modbusvaluelist;
	}

	private static String getvarbyid(int device_type, int shortvalue, int id) {
		String value="";
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select ap.* from  modbustcp_sk_iacparame_copy ap where  ap.point_id="
					+ id + " and ap.modbus_type=" + device_type +" and ap.var="+ shortvalue;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				value=rs_trd.getString("name");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	private static List<ModbusBit> getaddrbitlistbyid(int id, int device_type) {
		List<ModbusBit> blist = new ArrayList();
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select ap.*   from  modbustcp_sk_appparame ap where  ap.point_id="
					+ id + " and ap.modbus_type=" + device_type;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				ModbusBit md = new ModbusBit();
				md.setBit_id(rs.getInt("bit_id"));
				md.setName(rs.getString("var1"));
				// md.setShowstate(showstate);
				blist.add(md);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blist;
	}

	private static int getaddrarrarytype(int addr, int device_type) {
		int i=0;
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select array_type from  modbustcp_sk_app where addr="+addr+" and modbus_type="+device_type;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				i= rs_trd.getInt("array_type") ;
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
	/**
	 * 值和Bit位的片接
	 * 
	 * @param bitlist
	 * @param shortvalue
	 * @return
	 */
	public static List<String> getbitlist(List<ModbusBit> bitlist, int shortvalue) {
		List<String> modbusvaluelist = new ArrayList();
		String bstr = Integer.toBinaryString(shortvalue);
		if(shortvalue<0){
			bstr=bstr.substring(16,32);
		}
		String bitstr = "";
		for (int i = bstr.length() - 1; i >= 0; i--) {
			int state = Integer.parseInt((bstr.substring(i, i + 1)));
			// System.out.println(state);
			ModbusBit bitdemo = bitlist.get(bstr.length() - i - 1);
			bitdemo.setShowstate(state);
			bitstr += bitdemo.toString() + "|";
		}
		for (int j = bstr.length(); j < bitlist.size(); j++) {
			ModbusBit bitdemonew = bitlist.get(j);
			bitdemonew.setShowstate(0);
			if (j == bitlist.size() - 1) {
				bitstr += bitdemonew.toString();
			} else {
				bitstr += bitdemonew.toString() + "|";
			}
		}
		modbusvaluelist.add("0");
		modbusvaluelist.add(bitstr);
		return modbusvaluelist;

	}
	
	private static String getaddvarvalue(int addr, int device_type,int shortvalue) {
	  String value="";
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select ap.* from  modbustcp_sk_iacparame ap  where  ap.addr="
					+ addr + " and ap.modbus_type=" + device_type +" and ap.var="+ shortvalue;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				value= rs_trd.getString("name");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	public static List<ModbusBit> getaddrbitlist(int addr, int device_type) {
		List<ModbusBit> blist = new ArrayList();
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select ap.*   from  modbustcp_sk_appparame ap where  ap.addr="
					+ addr + " and ap.modbus_type=" + device_type;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				ModbusBit md = new ModbusBit();
				md.setBit_id(rs_trd.getInt("bit_id"));
				md.setName(rs_trd.getString("var1"));
				// md.setShowstate(showstate);
				blist.add(md);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blist;
	}

	public static int getaddrvar(int addr, int device_type) {
		int i=0;
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select  count(*) ct from  modbustcp_sk_iacparame  where addr=" + addr + " and modbus_type="
					+ device_type;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				i= rs_trd.getInt("ct");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}

	public static int getaddrbit(int addr, int device_type) {
		int i=0;
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd=null;
			String sql = "select  count(*) ct from  modbustcp_sk_appparame  where addr=" + addr + " and modbus_type="
					+ device_type;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				i= rs_trd.getInt("ct");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;
	}
}
