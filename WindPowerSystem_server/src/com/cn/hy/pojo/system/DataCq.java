package com.cn.hy.pojo.system;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataCq {
	private int id;
	private int device_id;
	private String data;
	private Date create_time;
	private Date update_time;
	private int create_user;
	private int update_user;
	private Map<String,String> map;
	private List<String> deviceAll;
	private String errorstate;
	private String name;//设备
	private String runstate;//运行状态
	private String time_area;
	private int modbus_type;
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
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public int getCreate_user() {
		return create_user;
	}
	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}
	public int getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(int update_user) {
		this.update_user = update_user;
	}
	
	public List<String> getDeviceAll() {
		return deviceAll;
	}
	public void setDeviceAll(List<String> deviceAll) {
		this.deviceAll = deviceAll;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRunstate() {
		return runstate;
	}
	public void setRunstate(String runstate) {
		this.runstate = runstate;
	}
	public String getTime_area() {
		return time_area;
	}
	public void setTime_area(String time_area) {
		this.time_area = time_area;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public String getErrorstate() {
		return errorstate;
	}
	public void setErrorstate(String errorstate) {
		this.errorstate = errorstate;
	}
	
	
	
}
