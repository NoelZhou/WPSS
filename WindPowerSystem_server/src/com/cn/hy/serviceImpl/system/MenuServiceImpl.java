package com.cn.hy.serviceImpl.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.MenuDao;
import com.cn.hy.pojo.system.Menu;
import com.cn.hy.service.system.MenuService;
@Service("MenuServiceImpl")
public class MenuServiceImpl implements MenuService {
	@Resource
	private MenuDao menuDao;
	/**
	 * 查找所有菜单
	 */
	@Override
	public List<Menu> selectMenu() {
		return menuDao.selectMenu();
	}
	/**
	 * 更新菜单
	 */
	@Override
	public void updateMenu(String state, Integer id) {
		Menu menu = new Menu();
		menu.setId(id);
		menu.setState(state);
		menuDao.updateMenu(menu);
	}

}
