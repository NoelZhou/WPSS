package com.cn.hy.pojo.firmwareUpgrade;

public class Mcu {

	public String getMcuname() {
		return mcuname;
	}
	public void setMcuname(String mcuname) {
		this.mcuname = mcuname;
	}
	public String getChip() {
		return chip;
	}
	public void setChip(String chip) {
		this.chip = chip;
	}
	public String getBoot() {
		return boot;
	}
	public void setBoot(String boot) {
		this.boot = boot;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	private String mcuname; // MCU编号
	private String chip; // MCU类型
	private String boot; // boot版本
	private String os; // OS版本
	private String app; // 应用程序版本
}