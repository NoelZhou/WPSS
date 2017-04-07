package com.cn.hy.pojo.system;

import java.util.Date;

public class DeviceBaseData {
	private int id;
	private int paramter_id;
	private Date create_time;
	private Date update_time;
	private int create_user;
	private int update_user;
	private int showtype;
	private int modbustype;
	private String name;//显示参数名称
	private String ids;//批量修改id
	private int addr;//参数地址
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParamter_id() {
		return paramter_id;
	}
	public void setParamter_id(int paramter_id) {
		this.paramter_id = paramter_id;
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
	public int getShowtype() {
		return showtype;
	}
	public void setShowtype(int showtype) {
		this.showtype = showtype;
	}
	public int getModbustype() {
		return modbustype;
	}
	public void setModbustype(int modbustype) {
		this.modbustype = modbustype;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}
	
}
