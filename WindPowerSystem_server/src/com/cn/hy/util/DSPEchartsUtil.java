package com.cn.hy.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class DSPEchartsUtil {
	
	/**
	 * 获取图表绘制数据
	 * @param ips
	 * @param port
	 * @param mfCodes
	 * @return
	 */
	public static Map<String, Object> getEchartsData(String[] ips, int[] ports, String[] waveformCodes, int pageNum, int pageSize){
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> datas1 = DSPEchartsUtil.getDataList(ips[0], ports[0], waveformCodes[0], pageNum, pageSize);
		List<Map<String, Object>> datas2 = DSPEchartsUtil.getDataList(ips[1], ports[1], waveformCodes[1], pageNum, pageSize);
		
		if(CollectionUtils.isEmpty(datas1) || CollectionUtils.isEmpty(datas2) || datas1.size() != pageSize || datas1.size() != datas2.size()){
			result.put("xdata", null);
			result.put("datas", null);
			return result;
		}
		
		List xdatas = new ArrayList<List>();
		List<List> datas = new ArrayList<List>();
		List data = null;
		for(int i = 0; i < datas1.size(); i++){
			if(datas1.get(i) == null || 
					datas2.get(i) == null || 
					StringUtils.isEmpty(datas1.get(i).get("value")) || 
					StringUtils.isEmpty(datas2.get(i).get("value"))){
				continue;
			}
			xdatas.add(datas1.get(i).get("nowTime"));
			data = new ArrayList<List>();
			data.addAll(new ArrayList(Arrays.asList(((String)datas1.get(i).get("value")).split(","))));
			data.addAll(new ArrayList(Arrays.asList(((String)datas2.get(i).get("value")).split(","))));
			datas.add(i, data);
		}
		
		result.put("xdata", xdatas);
		result.put("datas", datas);
		return result;
	}
	
	/**
	 * 获取原始数据
	 * @param ip
	 * @param port
	 * @return
	 */
	public static List<Map<String, Object>> getDataList(String ip, int port, String waveformCodes, int pageNum, int pageSize){
		List<Map<String, Object>> result = new ArrayList<>();
		
		String sql = "SELECT value, now_time FROM windpower_deviceinfo_dsp_record WHERE ip = ? AND port = ? AND cmd = ? ORDER BY ID ASC LIMIT ?, ?";//SQL语句  
	    DBUtils db1 = new DBUtils(); 
        try {
        	db1.runSql(sql);
        	db1.pst.setString(1, ip);
        	db1.pst.setInt(2, port);
        	db1.pst.setString(3, waveformCodes);
        	db1.pst.setInt(4, (pageNum - 1) * pageSize);
        	db1.pst.setInt(5, pageSize);
        	ResultSet rs = db1.pst.executeQuery();
        	
        	Map<String, Object> data = null;
        	while (rs.next()) {
                data = new HashMap<String, Object>();
        		data.put("value", rs.getString(1));
        		data.put("nowTime", DSPEchartsUtil.formatDate(rs.getString(2)));
        		result.add(data);
            }
        	
        	rs.close();
			db1.close();
        } catch (SQLException e) {  
            e.printStackTrace();  
        }
		return result;
	}
	
	public static String formatDate(String str){
		try {
			java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");
			java.text.DateFormat format1 = new java.text.SimpleDateFormat("ss-S");
			Date date = format.parse(str);
			return format1.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}
