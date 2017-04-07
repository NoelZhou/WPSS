package com.zhta.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhta.bean.JDBConnection;
import com.zhta.pojo.DeviceAndInfo;

@WebServlet("/DeviceServlet")
public class DeviceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeviceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<DeviceAndInfo> DeviceAndInfoList = new ArrayList<DeviceAndInfo>();
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd
					.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
							ResultSet.CONCUR_UPDATABLE);
			String sql = "SELECT wd.id,  group_concat(wdif.ip order by wdif.ip separator ';   ')as ip,wd.device_type_id,wd.create_time,wd.name,wd.run_state,mbt.name as mtname FROM windpower_device wd left join windpower_deviceinfo wdif on wd.id=wdif.device_id left join windpower_devicetype  wpdt on wpdt.id=wd.device_type_id left join modbustcp_type mbt on mbt.id=wpdt.modbus_type group by wd.id";
			ResultSet rs = st_trd.executeQuery(sql);
			while (rs.next()) {
				DeviceAndInfo deviceAndInfo = new DeviceAndInfo();
				deviceAndInfo.setId(rs.getInt("id"));
				deviceAndInfo.setDevice_type_id(rs.getInt("device_type_id"));
				deviceAndInfo.setCreate_time(rs.getString("create_time"));
				deviceAndInfo.setName(rs.getString("name"));
				deviceAndInfo.setRun_state(rs.getInt("run_state"));
				deviceAndInfo.setIp(rs.getString("ip"));
				deviceAndInfo.setMtname(rs.getString("mtname"));
				DeviceAndInfoList.add(deviceAndInfo);
			}
			st_trd.close();
			conn_trd.close();
			request.setAttribute("DeviceAndInfoList", DeviceAndInfoList);
			request.getRequestDispatcher("/jsp/deviceList.jsp").forward(
					request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
