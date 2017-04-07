package com.cn.hy.pojo.modbustcp;

public class Parameter {
	private int id;
	private String name;
	private String creat_time;
	private int parent_type;
	private int crent_user;
	private int modbustcp_type;
	private int addr;//参数id
	private int array_type;//故障id
	private String unit;//参数单位
	private String category;//参数描述
	private String	remark;//参数备注
	private String sysremark;
	private String Hvalue;
	private String bitString;
	private int modbus_type;//风机名字
	private String basicname;//风机状态名字
	private String rolecode;
	private  String array_type_list;
	public String getRolecode() {
		return rolecode;
	}
	public void setRolecode(String rolecode) {
		this.rolecode = rolecode;
	}
	public int getModbus_type() {
		return modbus_type;
	}
	public void setModbus_type(int modbus_type) {
		this.modbus_type = modbus_type;
	}
	public String getBasicname() {
		return basicname;
	}
	public void setBasicname(String basicname) {
		this.basicname = basicname;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getModbustcp_type() {
		return modbustcp_type;
	}
	public void setModbustcp_type(int modbustcp_type) {
		this.modbustcp_type = modbustcp_type;
	}
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
	public String getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}
	public int getParent_type() {
		return parent_type;
	}
	public void setParent_type(int parent_type) {
		this.parent_type = parent_type;
	}
	public int getCrent_user() {
		return crent_user;
	}
	public void setCrent_user(int crent_user) {
		this.crent_user = crent_user;
	}
	public int getArray_type() {
		return array_type;
	}
	public void setArray_type(int array_type) {
		this.array_type = array_type;
	}
	public String getSysremark() {
		return sysremark;
	}
	public void setSysremark(String sysremark) {
		this.sysremark = sysremark;
	}
	public String getHvalue() {
		return Hvalue;
	}
	public void setHvalue(String hvalue) {
		Hvalue = hvalue;
	}
	public String getBitString() {
		return bitString;
	}
	public void setBitString(String bitString) {
		this.bitString = bitString;
	}
	public String getArray_type_list() {
		return array_type_list;
	}
	public void setArray_type_list(String array_type_list) {
		this.array_type_list = array_type_list;
	}
	

}
