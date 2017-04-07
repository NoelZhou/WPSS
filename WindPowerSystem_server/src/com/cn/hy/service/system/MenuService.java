package com.cn.hy.service.system;

import java.util.List;

import com.cn.hy.pojo.system.Menu;

public interface MenuService {
	/**
	 * 查找所有菜单
	 */
	public List<Menu> selectMenu();
	/**
	 * 更新菜单
	 * @param state
	 * @param id
	 */
	public void updateMenu(String state,Integer id);
}
