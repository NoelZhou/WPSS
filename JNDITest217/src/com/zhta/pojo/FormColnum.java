package com.zhta.pojo;
/**
 * 报表设置-显示字段设置
 * @author HCheng
 *
 */
public class FormColnum {
	private int id;
	private String errorcode;
	private String codesx;//缩写
	private int form_type;
	private int showin;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public int getForm_type() {
		return form_type;
	}
	public void setForm_type(int form_type) {
		this.form_type = form_type;
	}
	public int getShowin() {
		return showin;
	}
	public void setShowin(int showin) {
		this.showin = showin;
	}
	public String getCodesx() {
		return codesx;
	}
	public void setCodesx(String codesx) {
		this.codesx = codesx;
	}
	
	
}
