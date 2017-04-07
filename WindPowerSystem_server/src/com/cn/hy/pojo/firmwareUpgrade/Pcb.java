package com.cn.hy.pojo.firmwareUpgrade;

public class Pcb {
	public String getPcbname() {
		return pcbname;
	}
	public void setPcbname(String pcbname) {
		this.pcbname = pcbname;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
	}
	public String getPbno() {
		return pbno;
	}
	public void setPbno(String pbno) {
		this.pbno = pbno;
	}
	public String getPbom() {
		return pbom;
	}
	public void setPbom(String pbom) {
		this.pbom = pbom;
	}
	public String getPbsn() {
		return pbsn;
	}
	public void setPbsn(String pbsn) {
		this.pbsn = pbsn;
	}
	public Mcu[] getMcu() {
		return mcu;
	}
	public void setMcu(Mcu[] mcu) {
		this.mcu = mcu;
	}
	public int getMcuCnt() {
		return mcuCnt;
	}
	public void setMcuCnt(int mcuCnt) {
		this.mcuCnt = mcuCnt;
	}
	private String pcbname; // PCB编号
	private String pn; // PCB整机编号
	private String pbno; // PCB板号
	private String pbom; // PCB BOM号
	private String pbsn; // PCB板序列号
	private Mcu[] mcu; // MCU属性
	private int mcuCnt; // MCU个数
}
