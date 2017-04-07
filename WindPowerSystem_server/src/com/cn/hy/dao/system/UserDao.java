package com.cn.hy.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.system.Menu;

import com.cn.hy.pojo.system.User;

public interface UserDao {
	/**
	 * 根据id和password查找用户是否存在
	 * @param employeeId
	 * @param passwd
	 * @return
	 */
	public User selectUser(@Param(value="employeeId") String employeeId, @Param(value="passwd") String passwd);
	/**
	 * 当前用户操作菜单权限
	 * @param i
	 * @return
	 */
	public List<Menu> selectmenubyuserid(int i);
	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public int insertUser(User user); 
	/**
	 * 删除用户
	 * @param id
	 */
	public void deleteUser(int id);
	/**
	 * 更新用户
	 * @param user
	 */
	public void updateUser(User user);	
	/**
	 * 更新role(弃用)
	 * @param roleId
	 */
	public void updateRole(Integer roleId);
	/**
	 * 通过id查找用户
	 * @param id
	 * @return
	 */
	public User selectUserById(Integer id);
	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> selectAllUser();
	/**
	 * 根据用户账号获取当前用户信息
	 * @param employeeId
	 * @return
	 */
	public User getUserByEmployeeId(String employeeId);
	/**
	 * 更新用户状态
	 * @param user
	 */
	public void updateState(User user);
	/**
	 * 更新设置用户密码
	 * @param user
	 */
	public void updatepass(User user);
	/**
	 * 通过id和empid查找用户
	 * @param user
	 * @return
	 */
	public List<User> findByIdAndEmpID(User user);
	/**
	 * 更新用户错误登陆次数
	 * @param user
	 */
	public void updateUserTimes(User user);
	/**
	 * 更新用户状态
	 * @param user
	 */
	public void updateStateTime(User user);
}
