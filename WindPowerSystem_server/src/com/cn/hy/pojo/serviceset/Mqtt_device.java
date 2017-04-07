package com.cn.hy.pojo.serviceset;

import java.io.Serializable;

public class Mqtt_device implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String sn;           //设备编号
	private String create_time;
	private int run_state;
	private String name;          //风机名称
	private int device_yd_lx;     //设备云端类型
	private int device_lx_mh;     //设备类型编码
	private String device_CJ;    //设备厂家
	private String device_xh;    //设备型号
	private int device_id;       //变流器设备id
	private String device_name;  //变流器名称
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getRun_state() {
		return run_state;
	}
	public void setRun_state(int run_state) {
		this.run_state = run_state;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getDevice_yd_lx() {
		return device_yd_lx;
	}
	public void setDevice_yd_lx(int device_yd_lx) {
		this.device_yd_lx = device_yd_lx;
	}
	public int getDevice_lx_mh() {
		return device_lx_mh;
	}
	public void setDevice_lx_mh(int device_lx_mh) {
		this.device_lx_mh = device_lx_mh;
	}
	public String getDevice_CJ() {
		return device_CJ;
	}
	public void setDevice_CJ(String device_CJ) {
		this.device_CJ = device_CJ;
	}
	public String getDevice_xh() {
		return device_xh;
	}
	public void setDevice_xh(String device_xh) {
		this.device_xh = device_xh;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	
}
