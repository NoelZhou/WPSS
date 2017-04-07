package com.cn.hy.dao.system;

import java.util.List;


import com.cn.hy.pojo.system.System_menu;

public interface System_menuDao {
	/**
	 * 查询所有
	 * @return
	 */
	public List<System_menu> selectMenu();
}
