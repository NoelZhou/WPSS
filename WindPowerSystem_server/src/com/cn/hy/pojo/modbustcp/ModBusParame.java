package com.cn.hy.pojo.modbustcp;

public class ModBusParame {
	private int id;
	private int addr;
	private int shortvalue;
	private int device_type;
	private int point_id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getShortvalue() {
		return shortvalue;
	}

	public void setShortvalue(int shortvalue) {
		this.shortvalue = shortvalue;
	}

	public int getDevice_type() {
		return device_type;
	}

	public void setDevice_type(int device_type) {
		this.device_type = device_type;
	}

	public int getAddr() {
		return addr;
	}

	public void setAddr(int addr) {
		this.addr = addr;
	}

	public int getPoint_id() {
		return point_id;
	}

	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}

}
