package com.zhta.pojo;

import java.sql.Timestamp;

public class AcqData {
private int id;
private int device_id;
private String data;
private String name;
private Timestamp create_time;
private int addr;
private String addrname;
public int getAddr() {
	return addr;
}
public void setAddr(int addr) {
	this.addr = addr;
}
public String getAddrname() {
	return addrname;
}
public void setAddrname(String addrname) {
	this.addrname = addrname;
}
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
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public Timestamp getCreate_time() {
	return create_time;
}
public void setCreate_time(Timestamp create_time) {
	this.create_time = create_time;
}
}
