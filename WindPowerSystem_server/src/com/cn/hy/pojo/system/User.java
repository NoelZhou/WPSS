package com.cn.hy.pojo.system;

import java.util.List;

public class User {
   private int id;
   private String name;
   private String employeeId;
   private String passwd;
   private String employeeName;
   private String registerDate;
   private Integer state;
   private String telephone;
   private Integer roleId;
   private List<Menu> menu;
   private List<Role> role;
   private String role_name;
   private int logintimes;
   private String loginerrordate;
   
   public String getTelephone() {
	return telephone;
}
public void setTelephone(String telephone) {
	this.telephone = telephone;
}
public String getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(String employeeId) {
	this.employeeId = employeeId;
}
public String getPasswd() {
	return passwd;
}
public void setPasswd(String passwd) {
	this.passwd = passwd;
}
public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeName) {
	this.employeeName = employeeName;
}
public String getRegisterDate() {
	return registerDate;
}
public void setRegisterDate(String registerDate) {
	this.registerDate = registerDate;
}

public List<Menu> getMenu() {
	return menu;
}
public void setMenu(List<Menu> menu) {
	this.menu = menu;
}
public Integer getRoleId() {
	return roleId;
}
public void setRoleId(Integer roleId) {
	this.roleId = roleId;
}
public List<Role> getRole() {
	return role;
}
public void setRole(List<Role> role) {
	this.role = role;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}


public String getRole_name() {
	return role_name;
}
public void setRole_name(String role_name) {
	this.role_name = role_name;
}
public User(int id, String name, String employeeId, String passwd, String employeeName, String registerDate,
		Integer state, String telephone, List<Menu> menu, List<Role> role, Integer roleId) {
	super();
	this.id = id;
	this.name = name;
	this.employeeId = employeeId;
	this.passwd = passwd;
	this.employeeName = employeeName;
	this.registerDate = registerDate;
	this.state = state;
	this.telephone = telephone;
	this.menu = menu;
	this.role = role;
	this.roleId = roleId;
}
public User() {
	super();
	// TODO Auto-generated constructor stub
}
public Integer getState() {
	return state;
}
public void setState(Integer state) {
	this.state = state;
}
public int getLogintimes() {
	return logintimes;
}
public void setLogintimes(int logintimes) {
	this.logintimes = logintimes;
}
public String getLoginerrordate() {
	return loginerrordate;
}
public void setLoginerrordate(String loginerrordate) {
	this.loginerrordate = loginerrordate;
}

  
}
