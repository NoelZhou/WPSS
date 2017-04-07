package com.cn.hy.pojo.system;

public class Role {
  private int  roleId;
  private String roleName;
  
  private int id;
  private String role_name;
  private  int create_user;
  private String create_date;
  private String describes;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRole_name() {
		return role_name;
	}
	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}
	public int getCreate_user() {
		return create_user;
	}
	public void setCreate_user(int create_user) {
		this.create_user = create_user;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public String getDescribes() {
		return describes;
	}
	public void setDescribes(String describes) {
		this.describes = describes;
	}
	
public int getRoleId() {
	return roleId;
}
public void setRoleId(int roleId) {
	this.roleId = roleId;
}
public String getRoleName() {
	return roleName;
}
public void setRoleName(String roleName) {
	this.roleName = roleName;
}
public Role(int roleId, String roleName) {
	super();
	this.roleId = roleId;
	this.roleName = roleName;
}
public Role() {
	super();
	// TODO Auto-generated constructor stub
}
  
  
  
}
