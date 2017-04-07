package com.cn.hy.bean;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cn.hy.pojo.modbustcp.ModbusArrayType;

public class WindPowerReadModbusXmlAll_hsfd {
	// 主表
	String apptab = "modbustcp_sk_app";
	// bit位表
	String appparametab = "modbustcp_sk_appparame";
	// var枚举表
	String iacparametab = "modbustcp_sk_iacparame";
	static List<String> iacliststr_sk = new ArrayList<String>();
	static List<String> iacparameliststr_sk = new ArrayList<String>();
	static List<String> appliststr_sk = new ArrayList<String>();
	static List<String> appparameliststr_sk = new ArrayList<String>();
	List<ModbusArrayType> arraylistall = new ArrayList<ModbusArrayType>();
	private String rolecode="R";

	private static ArrayList<String> filelist = new ArrayList<String>();

	/**
	 * 读协议的所有xml文件
	 * @param filePath
	 * @return
	 */
	public String ReadModbusXmlall(String filePath) {
		String value = "";
		try {
			File f = new File(this.getClass().getResource("/").getPath());
			System.out.println(f);
			// String xmlpath1
			// =this.getClass().getClassLoader().getResource("").getPath()+
			// "xmlconfig/modbustcp/全功率/通讯协议定义控制参数数组xml解析文件--只读参数"
//			String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()	+ "xmlconfig/modbustcp/海上风电/通讯协议定义控制参数数组xml解析文件--读写参数";

			//String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()	+ "xmlconfig/modbustcp/海上风电/通讯协议定义控制参数数组xml解析文件--只读参数";
			//String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()+"xmlconfig/modbustcp/全功率/通讯协议定义控制参数数组xml解析文件--只读参数";
//			String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/modbustcp/全功率/通讯协议定义控制参数数组xml解析文件--读写参数";
//			String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/modbustcp/双馈/通讯协议定义控制参数数组xml解析文件--只读参数";
//		   	String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/modbustcp/双馈/通讯协议定义控制参数数组xml解析文件--读写参数";
			String xmlpath1 = "http://localhost:8080/WindPowerSystem_server/xmlconfig/modbustcp/全功率/通讯协议定义控制参数数组xml解析文件--只读参数";
			File root = new File(xmlpath1);
			File[] files = root.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					ReadModbusXmlall(file.getAbsolutePath());
					filelist.add(file.getAbsolutePath());
					System.out.println("显示" + filePath + "下所有子目录及其文件" + file.getAbsolutePath());
				} else {
					System.out.println("显示" + filePath + "下所有子目录" + file.getAbsolutePath());
					String fileNames = file.getAbsolutePath();
					String[] s = fileNames.split("\\\\");
					String readOrwrite = s[s.length-2];
					if(readOrwrite.equals("通讯协议定义控制参数数组xml解析文件--读写参数")){
						rolecode="W";
					}else{
						rolecode="R";
					}
					setlist(file.getAbsolutePath());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 插入sql语句到数据库里
	 */
	public void insertxmlsql() {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String[] headstr = appliststr_sk.get(0).split(",");
			int modbustcptype = 0;
			int array_type = 1;
			if (headstr[0].equals("双馈变流器")) {
				modbustcptype = 0;
			} else if (headstr[0].equals("全功率变流器")) {
				modbustcptype = 1;
			} else if (headstr[0].equals("海上风电变流器")) {
				modbustcptype = 2;
			}
			String rolecodeONE = rolecode;
			String array_name = "";
			// 数组类型
			for (int j = 0; j < arraylistall.size(); j++) {
				ModbusArrayType mtp = arraylistall.get(j);
				if (mtp.getName().equals(headstr[1])) {
					array_type = mtp.getId();
					rolecodeONE = mtp.getRw_role();
					array_name = mtp.getName();
				}
			}
			// 增加新的arrytype类型
			if (array_name == "") {
				array_type = insertarraytype(headstr[1], rolecode);
				array_name = headstr[1];
			}
			//获取标记为基本参数的字段
			String basestar=selectbase(array_type, modbustcptype, apptab);
			//删除老数据
			deletemodbustcp(array_type, modbustcptype, apptab);
			
		
			for (int i = 1; i < appliststr_sk.size(); i++) {
				String sql = "";
				if (rolecodeONE.equals("W")) {
					sql = "insert into  " + apptab + " (" + "rolecode," + "point_id," + "type," + "addr," + "datalen,"
							+ "datatype," + "datalimitmin," + "datalimitmax," + "cof," + "unit," + "remark,"
							+ "sysremark," + "name," + "category," + "modbus_type," + "array_type" + "," + "array_name"
							+ ")value( '" + rolecodeONE + "'," + appliststr_sk.get(i).split(",")[0] + ",'"
							+ appliststr_sk.get(i).split(",")[1] + "'," + appliststr_sk.get(i).split(",")[2] + ","
							+ appliststr_sk.get(i).split(",")[3] + ",'" + appliststr_sk.get(i).split(",")[4] + "',"
							+ appliststr_sk.get(i).split(",")[5] + "," + appliststr_sk.get(i).split(",")[6] + ",'"
							+ appliststr_sk.get(i).split(",")[7] + "','" + appliststr_sk.get(i).split(",")[8] + "','"
							+ appliststr_sk.get(i).split(",")[9] + "','" + appliststr_sk.get(i).split(",")[10] + "','"
							+ appliststr_sk.get(i).split(",")[11] + "','" + appliststr_sk.get(i).split(",")[12] + "',"
							+ modbustcptype + "," + array_type + ",'" + array_name

							+ "')";
				} else {
					sql = "insert into  " + apptab + " (" + "rolecode," + "point_id," + "type," + "addr," + "datalen,"
							+ "datatype," + "cof," + "unit," + "remark," + "sysremark," + "name," + "category,"
							+ "modbus_type," + "array_type," + "array_name" + ")value( '" + rolecodeONE + "',"
							+ appliststr_sk.get(i).split(",")[0] + ",'" + appliststr_sk.get(i).split(",")[1] + "',"
							+ appliststr_sk.get(i).split(",")[2] + "," + appliststr_sk.get(i).split(",")[3] + ",'"
							+ appliststr_sk.get(i).split(",")[4] + "'," + appliststr_sk.get(i).split(",")[5] + ",'"
							+ appliststr_sk.get(i).split(",")[6] + "','" + appliststr_sk.get(i).split(",")[7] + "','"
							+ appliststr_sk.get(i).split(",")[8] + "','" + appliststr_sk.get(i).split(",")[9] + "','"
							+ appliststr_sk.get(i).split(",")[10] + "'," + modbustcptype + "," + array_type + ",'"
							+ array_name

							+ "')";
				}
				System.out.println(sql);
				st_trd.execute(sql);
			}
            //更新基本参数
			if(basestar!=""&&basestar!=null){
				updatebase(array_type, modbustcptype, apptab,basestar);

			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//更新基本参数
     private void updatebase(int array_type, int modbustcptype, String apptab2, String basestar) {
		// TODO Auto-generated method stub
    	 try{
    			JDBConnection jdbc_trd = new JDBConnection();
    			Connection conn_trd = jdbc_trd.connection;
    			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
    			String sql="update   "+apptab2 +" set isbasicparm =1 where array_type="+array_type +"   and modbus_type="+modbustcptype  +" and addr in("+basestar+")";
    		    System.out.println("更新基本参数:"+sql);
    			st_trd.execute(sql);
    			st_trd.close();
    			conn_trd.close();
    			}catch(Exception e){
    		  	e.printStackTrace();
    		 }
	}

	//获取基本参数
	private String  selectbase(int array_type, int modbustcptype, String apptab2) {
		// TODO Auto-generated method stub
		String  addrstr="";
		try{
		JDBConnection jdbc_trd = new JDBConnection();
		Connection conn_trd = jdbc_trd.connection;
		ResultSet rs_trd=null;
		Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql="select  * from "+apptab2 +" where isbasicparm =1  and  array_type="+array_type +"   and modbus_type="+modbustcptype  ;
		rs_trd=st_trd.executeQuery(sql);
	     while(rs_trd.next()){
		   int addr=rs_trd.getInt("addr");
		   addrstr+=addr+",";
	      }
		
	     rs_trd.close();
	     st_trd.close();
	     conn_trd.close();
		}catch(Exception e){
	  	e.printStackTrace();
	 }
		if(addrstr!=""&&addrstr!=null){
			addrstr=addrstr.substring(0,addrstr.length()-1);
		}

		System.out.println("基本参数："+addrstr);
	   return  addrstr;
	}

	/**
	 * 插入sql语句到数据库中
	 */
	public void insertxmlparamesql() {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String[] headstr = appliststr_sk.get(0).split(",");
			int modbustcptype = 0;
			int array_type = 0;
			if (headstr[0].equals("双馈变流器")) {
				modbustcptype = 0;
			} else if (headstr[0].equals("全功率变流器")) {
				modbustcptype = 1;
			} else if (headstr[0].equals("海上风电变流器")) {
				modbustcptype = 2;
			}
			String array_name = "";
			String  rolecodeONE=rolecode;
			// 数组类型
			// 数组类型
			// 更新数组
			arraylistall = getmodbustcpArraytype();
			for (int j = 0; j < arraylistall.size(); j++) {
				ModbusArrayType mtp = arraylistall.get(j);
				if (mtp.getName().equals(headstr[1])) {
					array_type = mtp.getId();
					rolecodeONE = mtp.getRw_role();
					array_name = mtp.getName();
				}
			}
			if (appparameliststr_sk.size() == 0) {
				return;
			}
			//删除数据库中匹配的旧的数据
			deletemodbustcp(array_type, modbustcptype, appparametab);
			deletemodbustcp(array_type, modbustcptype, iacparametab);

			String[] headstrparame = appparameliststr_sk.get(0).split(",");
			String point_id = headstrparame[0];
			String addr = headstrparame[1];
			for (int i = 1; i < appparameliststr_sk.size(); i++) {
				if (appparameliststr_sk.get(i).split(",").length == 2) {
					headstrparame = appparameliststr_sk.get(i).split(",");
					point_id = headstrparame[0];
					addr = headstrparame[1];
				} else if (appparameliststr_sk.get(i).split(",").length == 1) {
					// headstrparame=appparameliststr_sk.get(i).split(",");
					if ((appparameliststr_sk.get(i).equals("84") && modbustcptype == 0)
							|| (appparameliststr_sk.get(i).equals("54") && modbustcptype == 0)
							|| (appparameliststr_sk.get(i).equals("54") && modbustcptype == 1)
							|| (appparameliststr_sk.get(i).equals("114") && modbustcptype == 1)) {
						point_id = appparameliststr_sk.get(i);
						addr = null;
					} else if ((appparameliststr_sk.get(i).equals("233") && modbustcptype == 0)
							|| (appparameliststr_sk.get(i).equals("73") && modbustcptype == 0)
							|| (appparameliststr_sk.get(i).equals("73") && modbustcptype == 1)
							|| (appparameliststr_sk.get(i).equals("233") && modbustcptype == 1)) {
						point_id = null;
						addr = appparameliststr_sk.get(i);
					}
				} else if (appparameliststr_sk.get(i).contains("bit")) {
					String sql = "insert into " + appparametab + " (" + "rolecode," + "bit_id," + "var0," + "var1,"
							+ "point_id," + "addr," + "modbus_type," + "array_type," + "array_name" + ")value( '"
							+ rolecodeONE + "'," + appparameliststr_sk.get(i).split(",")[0] + ",'"
							+ appparameliststr_sk.get(i).split(",")[1] + "','"
							+ appparameliststr_sk.get(i).split(",")[2] + "'," + point_id + "," + addr + ","
							+ modbustcptype + "," + array_type + ",'" +array_name

							+ "')";
					System.out.println(sql);
					st_trd.execute(sql);
					// }
				} else {
					String sql = "insert into " + iacparametab + " (" + "rolecode," + "var," + "name," + "point_id,"
							+ "addr," + "modbus_type," + "array_type," + "array_name" + ")value( '" + rolecodeONE + "',"
							+ appparameliststr_sk.get(i).split(",")[0] + ",'" + appparameliststr_sk.get(i).split(",")[1]
							+ "'," + point_id + "," + addr + "," + modbustcptype + "," + array_type + ",'" + array_name

							+ "')";
					System.out.println(sql);
					st_trd.execute(sql);
				}
			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String ReadModBusXml() {
		SAXReader reader = new SAXReader();
		String value = "";
//		String path = getClass().getResource("/").getFile().toString();
		try {
			File directory = new File("");// 参数为空
			String courseFile = directory.getCanonicalPath();
			String xmlpath = courseFile + "/WebContent/read/RotMonitorPara_FP.xml";
			File file = new File(xmlpath);
			Document document;
			System.out.println(xmlpath);
			document = reader.read(file);
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> childElements = root.elements();
			// 获取解析定义格式
			getList(childElements);
			// 地址解析
			// 插入数据
			insertxmlsql();
			insertxmlparamesql();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 解析文件xml
	 * 
	 * @param xmlpath
	 */
	public void setlist(String xmlpath) {
		SAXReader reader = new SAXReader();
		try {
			File file = new File(xmlpath);
			Document document;
			System.out.println(xmlpath);
			document = reader.read(file);
			Element root = document.getRootElement();
			@SuppressWarnings("unchecked")
			List<Element> childElements = root.elements();
			// 获取解析定义格式
			getList(childElements);
			// 地址解析
			// 插入数据
			arraylistall = getmodbustcpArraytype();
//			插入sql语句到数据库里
			insertxmlsql();
//			插入sql语句到数据库中
			insertxmlparamesql();

			iacliststr_sk = new ArrayList<String>();
			iacparameliststr_sk = new ArrayList<String>();
			appliststr_sk = new ArrayList<String>();
			appparameliststr_sk = new ArrayList<String>();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入新的array数组
	 * @param arrayname
	 * @param rolecode
	 * @return
	 */
	private int insertarraytype(String arrayname, String rolecode) {
		// TODO Auto-generated method stub
		int id = 0;
		try {

			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = null;
			//插入sql语句
			String sql = "insert into    modbustcp_sk_arrytyp (name,rw_role)values('" + arrayname + "','" + rolecode
					+ "')";
			// System.out.println("正在删除数据：" + sql);
			st_trd.execute(sql);

			//获取上条sql语句中id的最后一条自增域的值
			String idsql = "SELECT @@IDENTITY id";
			rs = st_trd.executeQuery(idsql);
			while (rs.next()) {
				id = rs.getInt("id");
			}
			rs.close();
			st_trd.close();
			conn_trd.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	// 获取arry数组(modbustcp_sk_arrytyp)
	public List<ModbusArrayType> getmodbustcpArraytype() {
		List<ModbusArrayType> arraylist = new ArrayList<ModbusArrayType>();
		try {

			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = null;
			String sql = "select  *  from modbustcp_sk_arrytyp";
			// System.out.println("正在删除数据：" + sql);
			rs = st_trd.executeQuery(sql);
			while (rs.next()) {
				ModbusArrayType mtp = new ModbusArrayType();
				mtp.setId(rs.getInt("id"));
				mtp.setName(rs.getString("name"));
				mtp.setRw_role(rs.getString("rw_role"));
				arraylist.add(mtp);
			}
			rs.close();
			st_trd.close();
			conn_trd.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arraylist;
	}

	/**
	 * 删除数据库旧的数组数据
	 * @param arraytype
	 * @param modbustype
	 * @param table
	 */
	public void deletemodbustcp(int arraytype, int modbustype, String table) {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "delete from " + table + " where modbus_type=" + modbustype + " and  array_type=" + arraytype;
			System.out.println("正在删除数据：" + sql);
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取协议定义
	 * 
	 * @param childElements
	 */
	public static void getList(List<Element> childElements) {
		try {

			// 计算节点中的属性值
			// 计算几点数
			for (Element child : childElements) {
				String listr = "";
				// 未知属性名情况下
				@SuppressWarnings("unchecked")
				List<Attribute> attributeList = child.attributes();
				// 主节点中的属性值
				for (int i = 0; i < attributeList.size(); i++) {
					// System.out.println(attr.getName() + ": " +
					// attr.getValue());
					if (i == attributeList.size() - 1) {
						listr += attributeList.get(i).getValue();
					} else {
						listr += attributeList.get(i).getValue() + ",";
					}

				}
				@SuppressWarnings("unchecked")
				List<Element> elementList = child.elements();
				if (elementList.size() == 0 && child.getQName().getName().equals("list")) {
					// System.out.println(child.getText());
					listr += "," + child.getText();
					listr += ",自定义值";
				}
				if (child.getQName().getName().equals("point") || child.getQName().getName().equals("device")) {
					appliststr_sk.add(listr);
				} else {
					appparameliststr_sk.add(listr);
				}
				// 主节点下面的，应数据库片接处理，
				if (elementList.size() != 0) {
					getList(elementList);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}