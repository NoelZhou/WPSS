package com.cn.hy.pojo.serviceset;

import java.util.Date;

/**
 * 报表时间设置
 * @author HCheng
 */
public class FormTimeSet {
	private int id;
	private String time_name;
	private int form_type;
	private Date create_time;
	private int create_user;
	private Date update_time;
	private int update_user;
	private int showin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime_name() {
		return time_name;
	}
	public void setTime_name(String time_name) {
		this.time_name = time_name;
	}
	public int getForm_type() {
		return form_type;
	}
	public void setForm_type(int form_type) {
		this.form_type = form_type;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public int getCreate_user() {
		return create_user;
	}
	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public int getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(int update_user) {
		this.update_user = update_user;
	}
	public int getShowin() {
		return showin;
	}
	public void setShowin(int showin) {
		this.showin = showin;
	}
}
