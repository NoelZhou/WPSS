package com.cn.hy.pojo.recordingManagement;

import java.io.Serializable;

public class RecordingManagement implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int device_id;
	private String device_name;
	private int error_type;
	private String create_time;
	private String error_time;
	private int errordata_id;
	private String error_excel;
	
	private String start_time;//开始时间
	private String end_time;//结束时间
	private String errorname;//故障类型
	private String error_name;//参数解析
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getError_time() {
		return error_time;
	}
	public void setError_time(String error_time) {
		this.error_time = error_time;
	}
	public int getErrordata_id() {
		return errordata_id;
	}
	public void setErrordata_id(int errordata_id) {
		this.errordata_id = errordata_id;
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
	public String getError_name() {
		return error_name;
	}
	public void setError_name(String error_name) {
		this.error_name = error_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	
	
}
