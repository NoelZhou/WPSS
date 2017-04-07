package com.cn.hy.pojo.reportstatistics;
/**
 * 报表故障列
 * @author HCheng
 *
 */
public class RowErrorData {
	private String error_column;
	private String error_name;
	private String error_types;//故障、告警
	private String date_area_sql;//时间区域sql语句
	
	private String device_name;
	public String getError_column() {
		return error_column;
	}
	public void setError_column(String error_column) {
		this.error_column = error_column;
	}
	public String getError_name() {
		return error_name;
	}
	public void setError_name(String error_name) {
		this.error_name = error_name;
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
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	
	
	
}
