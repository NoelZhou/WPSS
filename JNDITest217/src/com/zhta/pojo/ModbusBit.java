package com.zhta.pojo;

public class ModbusBit {

private int id;
private  int bit_id;
private String name;
private int addr;
private  String parent_name;
private int showstate;
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
public int getAddr() {
	return addr;
}
public void setAddr(int addr) {
	this.addr = addr;
}
public int getBit_id() {
	return bit_id;
}
public void setBit_id(int bit_id) {
	this.bit_id = bit_id;
}
public int getShowstate() {
	return showstate;
}
public void setShowstate(int showstate) {
	this.showstate = showstate;
}

public String toString(){
	return bit_id+","+name+","+showstate;
}
public String getParent_name() {
	return parent_name;
}
public void setParent_name(String parent_name) {
	this.parent_name = parent_name;
}
}
