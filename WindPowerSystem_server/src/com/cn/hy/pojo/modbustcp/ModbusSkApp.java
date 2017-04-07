package com.cn.hy.pojo.modbustcp;

public class ModbusSkApp {
	private int id;
	private String name; //属性名
	private int addr; //协议地址
	private String unit;//单位
	private String category;//说明
	private int modbus_type;//协议类型
	private int isbasicparm;//是否为基本参数
	
	private String type;
	private int datalen;
	private String datatype;
	private String cof;
	private String remark;
	
	private String idStr;//批量修改id
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAddr() {
		return addr;
	}
	public void setAddr(int addr) {
		this.addr = addr;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public int getIsbasicparm() {
		return isbasicparm;
	}
	public void setIsbasicparm(int isbasicparm) {
		this.isbasicparm = isbasicparm;
	}
	public String getIdStr() {
		return idStr;
	}
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getDatalen() {
		return datalen;
	}
	public void setDatalen(int datalen) {
		this.datalen = datalen;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getCof() {
		return cof;
	}
	public void setCof(String cof) {
		this.cof = cof;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
