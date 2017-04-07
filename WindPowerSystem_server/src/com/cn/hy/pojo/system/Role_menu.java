package com.cn.hy.pojo.system;

import java.io.Serializable;

public class Role_menu implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int role_id;
	private int menu_id;
	private int create_user;
	private String create_date;
	private int read_p;
	private int write_p;
	private int state;
	private String menu_name;
	private int menu_qx;//菜单权限
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRole_id() {
		return role_id;
	}
	public void setRole_id(int role_id) {
		this.role_id = role_id;
	}
	public int getMenu_id() {
		return menu_id;
	}
	public void setMenu_id(int menu_id) {
		this.menu_id = menu_id;
	}
	public int getCreate_user() {
		return create_user;
	}
	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
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
	public String getMenu_name() {
		return menu_name;
	}
	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getMenu_qx() {
		return menu_qx;
	}
	public void setMenu_qx(int menu_qx) {
		this.menu_qx = menu_qx;
	}
}
