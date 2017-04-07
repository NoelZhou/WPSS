package com.cn.hy.pojo.firmwareUpgrade;


public class DevAttr
    {
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
	public String getSver() {
		return sver;
	}
	public void setSver(String sver) {
		this.sver = sver;
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
	public Module[] getModule() {
		return module;
	}
	public void setModule(Module[] module) {
		this.module = module;
	}
	public int getModuCnt() {
		return moduCnt;
	}
	public void setModuCnt(int moduCnt) {
		this.moduCnt = moduCnt;
	}
		private String mod;           // 型号
        private String dver;          // 整机版本号
        private String hver;          // 硬件版本号
        private String sver;          // 软件包版本号
        private Pcb[] pcb;            // PCB属性
        private int pcbCnt;           // PCB个数
        private Module[] module;      // 模组属性信息
        private int moduCnt;          // 模组个数
}
