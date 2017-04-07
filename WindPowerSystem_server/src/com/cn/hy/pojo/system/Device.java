package com.cn.hy.pojo.system;


public class Device {
	private int id;
	private int device_type_id;
	private String sn;
	private String decription;
	private String avatar;
	private String create_time;
	private String update_time;
	private int create_user;
	private int update_user;
	private String name;
	private String modName;
	private String rw_role_req;
	private String rw_role_res;
	private int modbus_type;
	private String rw_sql;
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
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getDecription() {
		return decription;
	}
	public void setDecription(String decription) {
		this.decription = decription;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getModName() {
		return modName;
	}
	public void setModName(String modName) {
		this.modName = modName;
	}
	public String getRw_role_req() {
		return rw_role_req;
	}
	public void setRw_role_req(String rw_role_req) {
		this.rw_role_req = rw_role_req;
	}
	public String getRw_role_res() {
		return rw_role_res;
	}
	public void setRw_role_res(String rw_role_res) {
		this.rw_role_res = rw_role_res;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public String getRw_sql() {
		return rw_sql;
	}
	public void setRw_sql(String rw_sql) {
		this.rw_sql = rw_sql;
	}
	
}
