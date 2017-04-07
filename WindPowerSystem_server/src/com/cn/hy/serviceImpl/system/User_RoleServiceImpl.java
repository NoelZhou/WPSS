package com.cn.hy.serviceImpl.system;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.User_RoleDao;
import com.cn.hy.pojo.system.User_Role;
import com.cn.hy.service.system.User_RoleService;

@Service("User_RoleServiceImpl")
public class User_RoleServiceImpl implements User_RoleService {
  
	@Resource
	private User_RoleDao userRoleDao; 
   /**
    * 新增插入用户角色类型
    */
	@Override
	public void insertUserRole(int userId,int roleId) {
	   User_Role userrole = new User_Role();
	   userrole.setRoleId(roleId);
	   userrole.setUserId(userId);
	   userRoleDao.insertUserRole(userrole);
	}

   /**
    * 更新用户角色类型
    */
	@Override
	public void updateUserRole( int userId,int roleId) {
		// TODO Auto-generated method stub
		User_Role userRole=new User_Role();
	     userRole.setUserId(userId);
	     userRole.setRoleId(roleId);
	     userRoleDao.updateUserRole(userRole);
	}

	/**
	 * 删除当前用户id的角色
	 */
	@Override
	public void deleteUserId(int userId) {
		User_Role userRole=new User_Role();
	    userRole.setUserId(userId);
		userRoleDao.deleteUserId(userRole);
		
	}
	

}
