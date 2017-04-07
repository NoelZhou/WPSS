package com.cn.hy.dao.system;

import com.cn.hy.pojo.system.User_Role;

public interface User_RoleDao {
	/**
	 * 新增插入用户角色类型
	 * @param userrole
	 */
	public void insertUserRole(User_Role userrole);
	/**
	 * 更新用户角色类型
	 * @param userRole
	 */
	public void updateUserRole(User_Role userRole);
	/**
	 * 删除当前用户id的角色
	 * @param userId
	 */
	public void deleteUserId(User_Role userId);
}
