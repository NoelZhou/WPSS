package com.cn.hy.service.system;


import java.util.List;





import com.cn.hy.pojo.system.Role;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.System_menu;



public interface RoleService {
	/**
	 * 查询所有
	 * @return
	 */
	public List<Role> selectRole();
	/**
	 * 新增之后(保存到role表)
	 * @param role_name
	 * @param create_user
	 * @param describes
	 * @return
	 */
	public int insetrRole(String role_name,int create_user,String describes);
	/**
	 * 新增之后(保存到role_menu表)
	 * @param role_id
	 * @param menu_id
	 * @param create_user
	 * @param read_p
	 * @param write_p
	 */
	public void insertRole_menu(int role_id,int menu_id,int create_user,int read_p,int write_p);
	/**
	 * 新增之前
	 * @return
	 */
	public List<System_menu>  selectMenu();
	/**
	 * 删除role表
	 * @param id
	 */
	public void deleteRole(int id);
	/**
	 * 删除role_menu表
	 * @param id
	 */
	public void deleteRole_menu(int id);
	/**
	 * 修改之前
	 * @param role_id
	 * @return
	 */
	public Role findByRoleId(int role_id); 
	/**
	 * 查找menu
	 * @param role_id
	 * @return
	 */
	public List<Role_menu> selectRole_menu(int role_id);
	/**
	 * 修改之后(修改role表)
	 * @param id
	 * @param role_name
	 * @param describes
	 */
	public void updateRole(int id,String role_name,String describes);
	/**
	 * 修改之后(修改role_menu表)
	 * @param rold_id
	 * @param menu_id
	 * @param read_p
	 * @param write_p
	 */
	public void updateRole_menu(int rold_id,int menu_id,int read_p,int write_p);
}
