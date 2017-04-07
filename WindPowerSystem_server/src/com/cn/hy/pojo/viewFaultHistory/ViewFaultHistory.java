package com.cn.hy.pojo.viewFaultHistory;

import java.io.Serializable;

public class ViewFaultHistory implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;
	private int modbus_type;
	private int addr;
	private String data;
	private String sysremark;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getSysremark() {
		return sysremark;
	}
	public void setSysremark(String sysremark) {
		this.sysremark = sysremark;
	}
	
	
	
}
