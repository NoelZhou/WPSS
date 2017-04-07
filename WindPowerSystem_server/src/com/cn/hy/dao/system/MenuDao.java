package com.cn.hy.dao.system;

import java.util.List;

import com.cn.hy.pojo.system.Menu;

public interface MenuDao {
	/**
	 * 查找所有菜单
	 * @return
	 */
	public List<Menu> selectMenu();
	/**
	 * 更新菜单
	 * @param menu
	 */
	public void updateMenu(Menu menu);
}
