package com.cn.hy.pojo.serviceset;

import java.util.Date;

/**
 * 邮箱服务设置
 * @author HCheng
 *
 */
public class EmailServer {
	private int id;
	private String serveraddr;
	private String emailid;
	private String passwd;
	private String assgin;
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
	public String getServeraddr() {
		return serveraddr;
	}
	public void setServeraddr(String serveraddr) {
		this.serveraddr = serveraddr;
	}
	public String getEmailid() {
		return emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getAssgin() {
		return assgin;
	}
	public void setAssgin(String assgin) {
		this.assgin = assgin;
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
