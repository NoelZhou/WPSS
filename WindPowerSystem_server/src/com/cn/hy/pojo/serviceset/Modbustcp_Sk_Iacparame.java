package com.cn.hy.pojo.serviceset;

import java.io.Serializable;

public class Modbustcp_Sk_Iacparame implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id; 
	private int point_id;
	private int varId;
	private String name;
	private String remark1;
	private int addr;
	private int array_type;
	private int modbus_type;
	private String rolecode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPoint_id() {
		return point_id;
	}
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
	
	public int getVarId() {
		return varId;
	}
	public void setVarId(int varId) {
		this.varId = varId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}
	public int getArray_type() {
		return array_type;
	}
	public void setArray_type(int array_type) {
		this.array_type = array_type;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	
}
