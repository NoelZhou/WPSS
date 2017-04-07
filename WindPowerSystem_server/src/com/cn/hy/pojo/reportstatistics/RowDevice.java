package com.cn.hy.pojo.reportstatistics;
/**
 * 列 风机名称显示
 * @author HCheng
 *
 */
public class RowDevice {
	private String device_name;//风机名称
	
	private String error_types;//故障、告警
	
	private String date_area_sql;//时间区域sql语句

	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getError_types() {
		return error_types;
	}
	public void setError_types(String error_types) {
		this.error_types = error_types;
	}
	public String getDate_area_sql() {
		return date_area_sql;
	}
	public void setDate_area_sql(String date_area_sql) {
		this.date_area_sql = date_area_sql;
	}
}
