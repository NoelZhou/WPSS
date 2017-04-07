package com.cn.hy.pojo.firmwareUpgrade;

import com.cn.hy.pojo.windFieldPreview.DeviceInfo1;

/**
 * 固件升级
 */
@SuppressWarnings("serial")
public class FirmwareUpgrade extends DeviceInfo1{
	/**
	 * ID
	 */
	private long id;
	
	/**
	 * 变流器名称
	 */
	private String name;
	
	/**
	 * 协议类型
	 * 0：双馈；1：全功率；2：海上风电
	 */
	private String modbusType;
	

	public String getModbusType() {
		return modbusType;
	}

	public void setModbusType(String modbusType) {
		this.modbusType = modbusType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
}
