package com.cn.hy.pojo.datadspacq;

import java.io.Serializable;

public class Datadspacp implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
    private	String employeeid;
    private String datastr;
    private String data_type;
    private String create_time;
    private int xh;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmployeeid() {
		return employeeid;
	}
	public void setEmployeeid(String employeeid) {
		this.employeeid = employeeid;
	}
	public String getDatastr() {
		return datastr;
	}
	public void setDatastr(String datastr) {
		this.datastr = datastr;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getXh() {
		return xh;
	}
	public void setXh(int xh) {
		this.xh = xh;
	}
    
}
