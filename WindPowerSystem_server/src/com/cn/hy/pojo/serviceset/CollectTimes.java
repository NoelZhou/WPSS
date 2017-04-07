package com.cn.hy.pojo.serviceset;

import java.util.Date;

/**
 * 采集频率设置
 * @author HCheng
 *
 */
public class CollectTimes {
	private int id;
	private int real_time;
	private int history;
	private Date create_time;
	private int create_user;
	private Date update_time;
	private int update_user;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReal_time() {
		return real_time;
	}
	public void setReal_time(int real_time) {
		this.real_time = real_time;
	}
	public int getHistory() {
		return history;
	}
	public void setHistory(int history) {
		this.history = history;
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
	
}
