package com.cn.hy.service.system;


public interface User_RoleService {
	/**
	 * 新增插入用户角色类型
	 * @param userId
	 * @param roleId
	 */
	public void insertUserRole(int userId,int roleId);
	/**
	 * 更新用户角色类型
	 * @param userId
	 * @param roleId
	 */
	public void updateUserRole(int userId,int roleId);
	/**
	 * 删除当前用户id的角色
	 * @param userId
	 */
	public void deleteUserId(int userId);
}
