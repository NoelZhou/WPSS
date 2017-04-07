package com.cn.hy.serviceImpl.system;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.RoleDao;
import com.cn.hy.dao.system.Role_menuDao;
import com.cn.hy.dao.system.System_menuDao;
import com.cn.hy.pojo.system.Role;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.System_menu;
import com.cn.hy.service.system.RoleService;

@Service("RoleServiceImpl")
public class RoleServiceImpl implements RoleService{
	@Resource
    private RoleDao roleDao;
	@Resource
    private Role_menuDao role_menuDao;
	
	@Resource
    private System_menuDao system_menuDao;
	/**
	 * 查询所有
	 */
	@Override
	public List<Role> selectRole() {
		return roleDao.selectRole();
	}
	
	/**
	 * 新增之后(保存到role表)
	 */
	@Override
	public int  insetrRole(String role_name,int create_user,String describes){
		Role role=new Role();
		role.setRole_name(role_name);
		role.setCreate_user(create_user);
		role.setDescribes(describes);
		roleDao.insetrRole(role);
		return  role.getId();
				
	}
	/**
	 * 新增之后(保存到role_menu表)
	 */
	@Override
	public void insertRole_menu(int role_id, int menu_id, int create_user,int read_p, int write_p) {
		Role_menu role_menu=new Role_menu();
		role_menu.setRole_id(role_id);
		role_menu.setMenu_id(menu_id);
		role_menu.setCreate_user(create_user);
	
		role_menu.setRead_p(read_p);
		role_menu.setWrite_p(write_p);
		role_menuDao.insertRole_menu(role_menu);
	}
	
	
	/**
	 * 新增之前
	 */
	@Override
	public List<System_menu> selectMenu() {
		return system_menuDao.selectMenu();
	}
	
	/**
	 * 删除role表
	 */
	@Override
	public void deleteRole(int id) {
		roleDao.deleteRole(id);
	}
	
	/**
	 * 删除role_menu表
	 */
	@Override
	public void deleteRole_menu(int role_id) {
		role_menuDao.deleteRole_menu(role_id);
		
	}
	
	/**
	 * 修改之前
	 */
	@Override
	public Role findByRoleId(int role_id) {
		return roleDao.findByRoleId(role_id);
	}
	
	/**
	 * 查询所有
	 */
	@Override
	public List<Role_menu> selectRole_menu(int role_id) {
		return role_menuDao.selectRole_menu(role_id);
	}
	
	/**
	 * 修改之后(修改role表)
	 */
	@Override
	public void updateRole(int id, String role_name, String describes) {
		Role role=new Role();
		role.setId(id);
		role.setRole_name(role_name);
		role.setDescribes(describes);
		roleDao.updateRole(role);
		
	}
	
	/**
	 * 修改之后(修改role_menu表)
	 */
	@Override
	public void updateRole_menu(int rold_id, int menu_id,int read_p,int write_p) {
		Role_menu role_menu=new Role_menu();
		role_menu.setRole_id(rold_id);
		role_menu.setMenu_id(menu_id);
		role_menu.setRead_p(read_p);
		role_menu.setWrite_p(write_p);
		role_menuDao.updateRole_menu(role_menu);
	}



	

	
	
	
	
}
