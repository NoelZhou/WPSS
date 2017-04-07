package com.cn.hy.pojo.reportstatistics;
/**
 * 故障派单列表
 * @author HCheng
 *
 */
public class ErrorDataRepair {
	private int id;
	private String device_name;//设备名
	private String errorname;//事件类型
	private String createtime;//入库时间
	private String error_column;//故障告警记录
	private String cometime;//发生时间
	private String repair_user;//维修人
	private String repair_result;//维修结果
	private int repair_state;//维修状态
	private String endtime;//截止时间
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getErrorname() {
		return errorname;
	}
	public void setErrorname(String errorname) {
		this.errorname = errorname;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getError_column() {
		return error_column;
	}
	public void setError_column(String error_column) {
		this.error_column = error_column;
	}
	public String getCometime() {
		return cometime;
	}
	public void setCometime(String cometime) {
		this.cometime = cometime;
	}
	public String getRepair_user() {
		return repair_user;
	}
	public void setRepair_user(String repair_user) {
		this.repair_user = repair_user;
	}
	public String getRepair_result() {
		return repair_result;
	}
	public void setRepair_result(String repair_result) {
		this.repair_result = repair_result;
	}
	public int getRepair_state() {
		return repair_state;
	}
	public void setRepair_state(int repair_state) {
		this.repair_state = repair_state;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	
}
