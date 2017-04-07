package com.zhta.pojo;

import java.io.Serializable;

public class DeviceAndInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private int id;
	private int device_type_id;
	private String create_time;
	private String name;
	private int run_state;
	private String ip;
	private String mtname;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDevice_type_id() {
		return device_type_id;
	}
	public void setDevice_type_id(int device_type_id) {
		this.device_type_id = device_type_id;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRun_state() {
		return run_state;
	}
	public void setRun_state(int run_state) {
		this.run_state = run_state;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMtname() {
		return mtname;
	}
	public void setMtname(String mtname) {
		this.mtname = mtname;
	}
	
	
	
}
