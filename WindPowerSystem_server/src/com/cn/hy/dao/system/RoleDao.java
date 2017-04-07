package com.cn.hy.dao.system;
import java.util.List;




import com.cn.hy.pojo.system.Role;

public interface RoleDao {
	/**
	 * 查询所有
	 * @return
	 */
	public List<Role> selectRole();
	/**
	 * 根据id查询所有
	 * @param id
	 * @return
	 */
	public Role findByRoleId(int id);
	/**
	 * 新增之后
	 * @param role
	 * @return
	 */
	public int insetrRole(Role role);
	/**
	 * 删除
	 * @param id
	 */
	public void deleteRole(int id);
	/**
	 * 修改之后
	 * @param role
	 */
	public void updateRole(Role role);
	
	
}
