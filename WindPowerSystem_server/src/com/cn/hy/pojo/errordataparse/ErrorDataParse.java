package com.cn.hy.pojo.errordataparse;

import java.io.Serializable;

public class ErrorDataParse implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;              //设备故障表id
	private String device_name;  //设备名称
	private int  error_type;     //故障类型
	private String createtime;   //创建时间
	private String error_column; //参数值
	private String error_name;   //参数解析
	private String error_excel;   //文件路径
	private String cometime;      //发生时间
	
	private String errorname;//故障名称
	private int errordata_id;//故障历史表id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public int getError_type() {
		return error_type;
	}
	public void setError_type(int error_type) {
		this.error_type = error_type;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
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
	public String getError_excel() {
		return error_excel;
	}
	public void setError_excel(String error_excel) {
		this.error_excel = error_excel;
	}
	public String getErrorname() {
		return errorname;
	}
	public void setErrorname(String errorname) {
		this.errorname = errorname;
	}
	public int getErrordata_id() {
		return errordata_id;
	}
	public void setErrordata_id(int errordata_id) {
		this.errordata_id = errordata_id;
	}
	public String getCometime() {
		return cometime;
	}
	public void setCometime(String cometime) {
		this.cometime = cometime;
	}
	
	
}
