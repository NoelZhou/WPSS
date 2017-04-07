package com.cn.hy.pojo.firmwareUpgrade;

import java.io.Serializable;

/**
 * 设备详细信息
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class FirmwareUpgradeDeviceInfoDetail implements Serializable{
	/**
	 * 序列号 5
	 */
	private String serialNumber;
	
	/**
	 * 设备类型 7
	 */
	private String deviceType;
	
	/**
	 * 设备型号 6
	 */
	private String deviceModule;
	
	/**
	 * 设备版本 8
	 */
	private String deviceVersion;
	
	/**
	 * mac1  11
	 */
	private String mac1;
	
	/**
	 * mac2  12
	 */
	private String mac2;
	
	/**
	 * 属性协议号  2
	 */
	private String propertyAgreementNumber;
	
	/**
	 * 属性协议版本  3
	 */
	private String propertyAgreementVersion;
	
	/**
	 * 应用程序协议号  9
	 */
	private String applicationProtocolNumber;
	
	/**
	 * 应用程序版本号  10
	 */
	private String applicationProtocolVersion;
	
	
	/**
	 * 维护协议号 13
	 */
	private String maintenanceContractNumber;
	
	/**
	 * 固件版本数量  22
	 */
	private String firmwareVersionNumber;
	
	/**
	 * IAC协议号  16
	 */
	private String iacAgreementNumber;
	
	/**
	 * IAC版本号  17
	 */
	private String iacAgreementVersion;
	
	/**
	 * IAD协议号  18
	 */
	private String iadAgreementNumber;
	
	/**
	 * IAD版本号  19
	 */
	private String iadAgreementVersion;
	
	/**
	 * IAP协议号 14
	 *             
	 */
	private String iapAgreementNumber;
	
	/**
	 * IAP版本号 15
	 */
	private String iapAgreementVersion;
	
	/**
	 * IAT协议号  20
	 */
	private String iatAgreementNumber;
	
	/**
	 * IAT版本  21
	 */
	private String iatAgreementVersion;
	
	/**
	 * MCU固件版本号1  23
	 */
	private String mcuFirmwareVersionNumber1;
	
	/**
	 * MCU固件版本号2  24
	 */
	private String mcuFirmwareVersionNumber2;
	
	/**
	 * MCU固件版本号3  25
	 */
	private String mcuFirmwareVersionNumber3;

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceModule() {
		return deviceModule;
	}

	public void setDeviceModule(String deviceModule) {
		this.deviceModule = deviceModule;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getMac1() {
		return mac1;
	}

	public void setMac1(String mac1) {
		this.mac1 = mac1;
	}

	public String getMac2() {
		return mac2;
	}

	public void setMac2(String mac2) {
		this.mac2 = mac2;
	}

	public String getPropertyAgreementNumber() {
		return propertyAgreementNumber;
	}

	public void setPropertyAgreementNumber(String propertyAgreementNumber) {
		this.propertyAgreementNumber = propertyAgreementNumber;
	}

	public String getPropertyAgreementVersion() {
		return propertyAgreementVersion;
	}

	public void setPropertyAgreementVersion(String propertyAgreementVersion) {
		this.propertyAgreementVersion = propertyAgreementVersion;
	}

	public String getApplicationProtocolNumber() {
		return applicationProtocolNumber;
	}

	public void setApplicationProtocolNumber(String applicationProtocolNumber) {
		this.applicationProtocolNumber = applicationProtocolNumber;
	}

	public String getApplicationProtocolVersion() {
		return applicationProtocolVersion;
	}

	public void setApplicationProtocolVersion(String applicationProtocolVersion) {
		this.applicationProtocolVersion = applicationProtocolVersion;
	}

	public String getMaintenanceContractNumber() {
		return maintenanceContractNumber;
	}

	public void setMaintenanceContractNumber(String maintenanceContractNumber) {
		this.maintenanceContractNumber = maintenanceContractNumber;
	}

	public String getFirmwareVersionNumber() {
		return firmwareVersionNumber;
	}

	public void setFirmwareVersionNumber(String firmwareVersionNumber) {
		this.firmwareVersionNumber = firmwareVersionNumber;
	}

	public String getIacAgreementNumber() {
		return iacAgreementNumber;
	}

	public void setIacAgreementNumber(String iacAgreementNumber) {
		this.iacAgreementNumber = iacAgreementNumber;
	}

	public String getIacAgreementVersion() {
		return iacAgreementVersion;
	}

	public void setIacAgreementVersion(String iacAgreementVersion) {
		this.iacAgreementVersion = iacAgreementVersion;
	}

	public String getIadAgreementNumber() {
		return iadAgreementNumber;
	}

	public void setIadAgreementNumber(String iadAgreementNumber) {
		this.iadAgreementNumber = iadAgreementNumber;
	}

	public String getIadAgreementVersion() {
		return iadAgreementVersion;
	}

	public void setIadAgreementVersion(String iadAgreementVersion) {
		this.iadAgreementVersion = iadAgreementVersion;
	}

	public String getIapAgreementNumber() {
		return iapAgreementNumber;
	}

	public void setIapAgreementNumber(String iapAgreementNumber) {
		this.iapAgreementNumber = iapAgreementNumber;
	}

	public String getIapAgreementVersion() {
		return iapAgreementVersion;
	}

	public void setIapAgreementVersion(String iapAgreementVersion) {
		this.iapAgreementVersion = iapAgreementVersion;
	}

	public String getIatAgreementNumber() {
		return iatAgreementNumber;
	}

	public void setIatAgreementNumber(String iatAgreementNumber) {
		this.iatAgreementNumber = iatAgreementNumber;
	}

	public String getIatAgreementVersion() {
		return iatAgreementVersion;
	}

	public void setIatAgreementVersion(String iatAgreementVersion) {
		this.iatAgreementVersion = iatAgreementVersion;
	}

	public String getMcuFirmwareVersionNumber1() {
		return mcuFirmwareVersionNumber1;
	}

	public void setMcuFirmwareVersionNumber1(String mcuFirmwareVersionNumber1) {
		this.mcuFirmwareVersionNumber1 = mcuFirmwareVersionNumber1;
	}

	public String getMcuFirmwareVersionNumber2() {
		return mcuFirmwareVersionNumber2;
	}

	public void setMcuFirmwareVersionNumber2(String mcuFirmwareVersionNumber2) {
		this.mcuFirmwareVersionNumber2 = mcuFirmwareVersionNumber2;
	}

	public String getMcuFirmwareVersionNumber3() {
		return mcuFirmwareVersionNumber3;
	}

	public void setMcuFirmwareVersionNumber3(String mcuFirmwareVersionNumber3) {
		this.mcuFirmwareVersionNumber3 = mcuFirmwareVersionNumber3;
	}
	
	public void setAgainMcuFirmwareVersionNumber2() {
		this.mcuFirmwareVersionNumber2 = "N" + this.mcuFirmwareVersionNumber3.substring(1,
				this.mcuFirmwareVersionNumber3.length() - this.mcuFirmwareVersionNumber2.length()) + this.mcuFirmwareVersionNumber2;
	}
}
