package com.cn.hy.serviceImpl.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.Role_menuDao;
import com.cn.hy.dao.system.UserDao;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.User;
import com.cn.hy.service.system.UserService;

@Service("UserServiceImpl")
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userdao;
	@Resource
	private Role_menuDao role_menuDao;
	/**
	 * 根据id和password查找用户是否存在
	 */
	@Override
	public User selectUser(String employeeId, String passwd) {
		User user = userdao.selectUser(employeeId, passwd);
		return user;
	}
	/**
	 * 新增用户
	 */
	@Override
	public int insertUser(String employeeId, String passwd, String name, String employeeName, String telephone,
			Integer state) {
		User user = new User();
		user.setName(name);
		user.setPasswd(passwd);
		user.setEmployeeId(employeeId);
		user.setTelephone(telephone);
		user.setEmployeeName(employeeName);
		user.setState(state);
		userdao.insertUser(user);
		int userId = user.getId();
		return userId;

	}
	/**
	 * 删除用户
	 */
	@Override
	public void deleteUser(int id) {
		userdao.deleteUser(id);
	}
	/**
	 * 更新用户
	 */
	@Override
	public void updateUser(String name, String employeeId, String employeeName, String telephone,
			Integer state, int id) {
		User user = new User();
		user.setId(id);
		user.setEmployeeId(employeeId);
		user.setEmployeeName(employeeName);
		user.setName(name);
		user.setState(state);
		user.setTelephone(telephone);
		userdao.updateUser(user);
	}
	/**
	 * 更新role(弃用)
	 */
	@Override
	public void updateRole(Integer roleId) {
		// TODO Auto-generated method stub
		userdao.updateRole(roleId);
	}
	/**
	 * 获取所有用户
	 */
	public List<User> selectAllUser() {
		List<User> user = userdao.selectAllUser();
		return user;
	}
	/**
	 * 通过id查找用户
	 */
	@Override
	public User selectUserById(Integer id) {
		// TODO Auto-generated method stub
		return userdao.selectUserById(id);
	}
	/**
	 * 根据用户账号获取当前用户信息
	 */
	@Override
	public User getUserByEmployeeId(String employeeId) {
		// TODO Auto-generated method stub
		return userdao.getUserByEmployeeId(employeeId);
	}
	/**
	 * 更新用户状态
	 */
	@Override
	public void updateState(Integer state, Integer id) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setState(state);
		user.setId(id);
		userdao.updateState(user);
	}
	/**
	 * 更新设置用户密码
	 */
	@Override
	public void updatepass(String passwd, Integer id) {
		// TODO Auto-generated method stub
		User user = new User();
		user.setPasswd(passwd);
		user.setId(id);
		userdao.updatepass(user);

	}
	/**
	 * 通过userId获取菜单权限
	 */
	@Override
	public Role_menu findByUserId(String employeeId) {
		return role_menuDao.findByUserId(employeeId);
	}
	/**
	 * 通过id和empid查找用户
	 */
	@Override
	public List<User> findByIdAndEmpID(User user) {
		return userdao.findByIdAndEmpID(user);
	}
	/**
	 * 更新用户错误登陆次数
	 */
	@Override
	public void updateUserTimes(User user) {
		 userdao.updateUserTimes(user);
	}
	/**
	 * 更新用户状态
	 */
	@Override
	public void updateStateTime(User user) {
		userdao.updateStateTime(user);
	}


}
