package com.cn.hy.dao.system;

import java.util.List;



import com.cn.hy.pojo.system.Role_menu;

public interface Role_menuDao {

	/**
	 * 查询所有
	 * @param role_id
	 * @return
	 */
	public List<Role_menu> selectRole_menu(int role_id);
	
	/**
	 * 保存
	 * @param role_menu
	 */
	public void  insertRole_menu(Role_menu role_menu);
	
	/**
	 * 修改之后(修改role_menu表)
	 * @param role_menu
	 */
	public void updateRole_menu(Role_menu role_menu);
	
	/**
	 * 删除role_menu表
	 * @param role_id
	 */
	public void deleteRole_menu(int role_id);
	
	/**
	 * 通过userId获取菜单权限
	 * @param employeeId
	 * @return
	 */
	public Role_menu findByUserId(String employeeId);
}
