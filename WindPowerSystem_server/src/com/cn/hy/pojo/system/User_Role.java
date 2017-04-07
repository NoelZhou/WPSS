package com.cn.hy.pojo.system;

public class User_Role {
    private Integer id;
    private Integer userId;
    private Integer roleId;
    private Integer createUser;
    private String  createDate;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Integer getCreateUser() {
		return createUser;
	}
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public User_Role(Integer id, Integer userId, Integer roleId, Integer createUser, String createDate) {
		super();
		this.id = id;
		this.userId = userId;
		this.roleId = roleId;
		this.createUser = createUser;
		this.createDate = createDate;
	}
	public User_Role() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
    
    
}
