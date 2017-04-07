package com.cn.hy.pojo.firmwareUpgrade;


public class Module
{
    public String getModuname() {
		return moduname;
	}
	public void setModuname(String moduname) {
		this.moduname = moduname;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMod() {
		return mod;
	}
	public void setMod(String mod) {
		this.mod = mod;
	}
	public String getDver() {
		return dver;
	}
	public void setDver(String dver) {
		this.dver = dver;
	}
	public String getHver() {
		return hver;
	}
	public void setHver(String hver) {
		this.hver = hver;
	}
	public Pcb[] getPcb() {
		return pcb;
	}
	public void setPcb(Pcb[] pcb) {
		this.pcb = pcb;
	}
	public int getPcbCnt() {
		return pcbCnt;
	}
	public void setPcbCnt(int pcbCnt) {
		this.pcbCnt = pcbCnt;
	}
	private String moduname;     //模组编号 
    private String sn;           // 模组编号
    private String mod;          // 模组型号
    private String dver;         // 模组版本号
    private String hver;         // 硬件版本号
    private Pcb[] pcb;           // 该台模组下所含的PCB信息
    private int pcbCnt;          // 该台模组下所含的PCB个数
}