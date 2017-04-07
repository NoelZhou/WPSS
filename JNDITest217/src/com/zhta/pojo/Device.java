package com.zhta.pojo;

public class Device {
	int device_id ;
	String ip ;
	int port;
	int startaddr ;
	int modbuslength ;
	int d_type;
	int length ;
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getStartaddr() {
		return startaddr;
	}
	public void setStartaddr(int startaddr) {
		this.startaddr = startaddr;
	}
	public int getModbuslength() {
		return modbuslength;
	}
	public void setModbuslength(int modbuslength) {
		this.modbuslength = modbuslength;
	}
	public int getD_type() {
		return d_type;
	}
	public void setD_type(int d_type) {
		this.d_type = d_type;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
}
