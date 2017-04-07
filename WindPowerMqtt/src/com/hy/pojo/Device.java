package com.hy.pojo;

public class Device {
	int device_id ;
	String name;
	String sn;
	int port;
	int startaddr ;
	int modbuslength ;
	int d_type;
	int lx;
	int device_addrid;
	float device_lx_mh;//设备类型编号
	String device_CJ;//设备厂家
	String device_xh;//设备类型
	String errorstate;//设备是否出现故障
	public String getErrorstate() {
		return errorstate;
	}
	public void setErrorstate(String errorstate) {
		this.errorstate = errorstate;
	}
	public float getDevice_lx_mh() {
		return device_lx_mh;
	}
	public void setDevice_lx_mh(float device_lx_mh) {
		this.device_lx_mh = device_lx_mh;
	}
	public String getDevice_CJ() {
		return device_CJ;
	}
	public void setDevice_CJ(String device_CJ) {
		this.device_CJ = device_CJ;
	}
	public String getDevice_xh() {
		return device_xh;
	}
	public void setDevice_xh(String device_xh) {
		this.device_xh = device_xh;
	}
	
	public int getDevice_addrid() {
		return device_addrid;
	}
	public void setDevice_addrid(int device_addrid) {
		this.device_addrid = device_addrid;
	}
	public int getLx() {
		return lx;
	}
	public void setLx(int lx) {
		this.lx = lx;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
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
	int length ;
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
}
