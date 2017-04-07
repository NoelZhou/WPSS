package com.cn.hy.pojo.windFieldPreview;

import java.io.Serializable;

/**
 * 波形
 */
@SuppressWarnings("serial")
public class DeviceInfoDspWaveform implements Serializable{
	
	private String id;
	
	/**
	 * 波形编号
	 */
	private String code;
	
	/**
	 * 波形名称
	 */
	private String name;
	
	/**
	 * 类别，1：示波器；2：故障录波
	 */
	private String model;
	
	/**
	 * 协议类型，0：双馈；1：全功率；2：海上风电
	 */
	private String modbusType;
	
	/**
	 * 波形分类，1：网侧波形；2：机侧波形；
	 */
	private String type;
	
	/**
	 * 描述
	 */
	private String description;
	
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 修改时间
	 */
	private String updateTime;
	
	/**
	 * 创建人
	 */
	private String createUser;
	
	/**
	 * 修改人
	 */
	private String updateUser;
	
	//***查询参数start***//
	/**设备ID**/
	private String deviceId;
	
	/**DSP类型，1：机侧波形；2：网侧波形**/
	private String dspType;
	//***查询参数end***//

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModbusType() {
		return modbusType;
	}

	public void setModbusType(String modbusType) {
		this.modbusType = modbusType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDspType() {
		return dspType;
	}

	public void setDspType(String dspType) {
		this.dspType = dspType;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
