package com.cn.hy.pojo.system;

public class Menu {
private int id;
private String menu_name;
private int parent_id;
private String menu_url;
private String state;
private int read_p;
private int write_p;
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getMenu_name() {
	return menu_name;
}
public void setMenu_name(String menu_name) {
	this.menu_name = menu_name;
}
public int getParent_id() {
	return parent_id;
}
public void setParent_id(int parent_id) {
	this.parent_id = parent_id;
}
public String getMenu_url() {
	return menu_url;
}
public void setMenu_url(String menu_url) {
	this.menu_url = menu_url;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public int getRead_p() {
	return read_p;
}
public void setRead_p(int read_p) {
	this.read_p = read_p;
}
public int getWrite_p() {
	return write_p;
}
public void setWrite_p(int write_p) {
	this.write_p = write_p;
}


}
