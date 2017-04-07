package com.cn.hy.pojo.serviceset;

import java.io.Serializable;

//bit位表
public class Modbustcp_Sk_Appparame implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id; 
	private int bit_id;
	private String var0;
	private String var1;
	private String remark1;
	private int point_id;
	private int addr;
	private int modbus_type;
	private int array_type;
	private String rolecode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBit_id() {
		return bit_id;
	}
	public void setBit_id(int bit_id) {
		this.bit_id = bit_id;
	}
	public String getVar0() {
		return var0;
	}
	public void setVar0(String var0) {
		this.var0 = var0;
	}
	public String getVar1() {
		return var1;
	}
	public void setVar1(String var1) {
		this.var1 = var1;
	}
	public String getRemark1() {
		return remark1;
	}
	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}
	public int getPoint_id() {
		return point_id;
	}
	public void setPoint_id(int point_id) {
		this.point_id = point_id;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public int getArray_type() {
		return array_type;
	}
	public void setArray_type(int array_type) {
		this.array_type = array_type;
	}
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	
}
