package com.cn.hy.service.system;
import java.util.List;

import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.User;

public interface UserService {
	/**
	 * 新增用户
	 * @param employeeId
	 * @param passwd
	 * @param name
	 * @param employeeName
	 * @param telephone
	 * @param state
	 * @return
	 */
	public int insertUser( String employeeId,String passwd,String name, String employeeName,String telephone,Integer state);
	/**
	 * 删除用户
	 * @param id
	 */
	public void deleteUser(int id);
	/**
	 * 根据id和password查找用户是否存在
	 * @param employeeId
	 * @param passwd
	 * @return
	 */
	public User selectUser(String employeeId,String passwd);
	/**
	 * 更新用户
	 * @param name
	 * @param employeeId
	 * @param employeeName
	 * @param telephone
	 * @param state
	 * @param id
	 */
	public void updateUser(String name,String employeeId,String employeeName ,String telephone,Integer state, int id);
	/**
	 * 更新role(弃用)
	 * @param roleId
	 */
	public void updateRole(Integer roleId);
	/**
	 * 获取所有用户
	 * @return
	 */
	public List<User> selectAllUser();
	/**
	 * 通过id查找用户
	 * @param id
	 * @return
	 */
	public User selectUserById(Integer id);
	/**
	 * 根据用户账号获取当前用户信息
	 * @param employeeId
	 * @return
	 */
	public User getUserByEmployeeId(String employeeId);
	/**
	 * 更新用户状态
	 * @param state
	 * @param id
	 */
	public void updateState(Integer state, Integer id);
	/**
	 * 更新设置用户密码
	 * @param passwd
	 * @param id
	 */
	public void updatepass(String passwd,Integer id);
	/**
	 * 通过userId获取菜单权限
	 * @param employeeId
	 * @return
	 */
	public Role_menu findByUserId(String employeeId);
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
