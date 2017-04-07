package com.cn.hy.pojo.windFieldPreview;

import java.io.Serializable;

/**
 * 变流器设备明细信息表
 */
@SuppressWarnings("serial")
public class DeviceInfo1 implements Serializable{
	
	/**
	 * 电流器ID
	 */
	private String deviceId;
	
	/**
	 * 电流器类型是arm,还是DSP
	 */
	private String dType;
	
	/**
	 * DSP分类，1：网侧；2：机侧；
	 */
	private String dspType;
	
	/**
	 * IP地址
	 */
	private String ip;
	
	/**
	 * 端口
	 */
	private int port;
	
	/**
	 * 是否接收数据，用于示波器数据同步，0：不同步（默认值）；1：同步
	 */
	private String isSync;
	
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getdType() {
		return dType;
	}

	public void setdType(String dType) {
		this.dType = dType;
	}

	public String getDspType() {
		return dspType;
	}

	public void setDspType(String dspType) {
		this.dspType = dspType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getIsSync() {
		return isSync;
	}

	public void setIsSync(String isSync) {
		this.isSync = isSync;
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
	
	
	
}
