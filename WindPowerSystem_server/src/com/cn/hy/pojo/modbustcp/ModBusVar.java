package com.cn.hy.pojo.modbustcp;

public class ModBusVar {
private int id;
private String name;
private int var;
private  String addr;
private int modbus_type;
private int array_type;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getVar() {
	return var;
}
public void setVar(int var) {
	this.var = var;
}
public String getAddr() {
	return addr;
}
public void setAddr(String addr) {
	this.addr = addr;
}
public int getModbus_type() {
	return modbus_type;
}
public void setModbus_type(int modbus_type) {
	this.modbus_type = modbus_type;
}
public int getArray_type() {
	return array_type;
}
public void setArray_type(int array_type) {
	this.array_type = array_type;
}
}
