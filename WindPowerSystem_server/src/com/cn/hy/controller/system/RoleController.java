package com.cn.hy.controller.system;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.system.Role;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.System_menu;
import com.cn.hy.service.system.RoleService;

@Controller
@RequestMapping("/Role")
public class RoleController {

	@Resource
	private RoleService roleService;

	/*
	 * 查询所有
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData list() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Role> roleList = roleService.selectRole();
			if (roleList != null) {
				data.setMessage("获取成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("roleList", roleList);
				data.setResponseData(resData);
				return data;
			} else {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("没有数据！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("roleList", "");
				data.setResponseData(resData);
				return data;
			}

		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/*
	 * @RequestMapping(value="/list") public ModelAndView list(){ ModelAndView
	 * mv =new ModelAndView(); List<Role> roleList = roleService.selectRole();
	 * mv.setViewName("jsp/system/role/role_list"); mv.addObject("roleList",
	 * roleList); return mv; }
	 */

	/*
	 * 新增之前
	 */
	@RequestMapping("/toInsert")
	public ModelAndView toInsert() {
		ModelAndView mv = new ModelAndView();
		List<System_menu> MenuList = roleService.selectMenu();
		mv.setViewName("jsp/systemjsp/role_add");
		mv.addObject("MenuList", MenuList);
		mv.addObject("msg", "insert");
		return mv;
	}

	/*
	 * @RequestMapping("/toInsert")
	 * 
	 * @ResponseBody public BaseResponseData toInsert() { BaseResponseData data
	 * = new BaseResponseData(); try { List<System_menu> MenuList =
	 * roleService.selectMenu(); if (MenuList!=null) { data.setMessage("获取成功");
	 * data.setCode(WebResponseCode.SUCCESS); HashMap<String, Object> resData =
	 * new HashMap<String, Object>(); resData.put("MenuList", MenuList);
	 * data.setResponseData(resData); return data; } else {
	 * data.setCode(WebResponseCode.SUCCESS); data.setMessage("没有数据！");
	 * HashMap<String, Object> resData = new HashMap<String, Object>();
	 * resData.put("MenuList", ""); data.setResponseData(resData); return data;
	 * }
	 * 
	 * } catch (Exception e) { data.setCode(WebResponseCode.ERROR);
	 * data.setMessage("获取失败,服务器异常，请稍后重试！"); HashMap<String, Object> resData =
	 * new HashMap<String, Object>(); resData.put("errorcode",
	 * WebResponseCode.EXECUTIONERROR); resData.put("errormessage",
	 * WebResponseCode.EXECUTIONERRORMESSAGE); data.setResponseData(resData);
	 * return data; } }
	 */

	/*
	 * 新增之后
	 */
	@RequestMapping("/insert")
	@ResponseBody
	public BaseResponseData insert(@RequestParam(value = "role_name", required = false) String role_name,
			@RequestParam(value = "create_user", required = false) int create_user,
			@RequestParam(value = "describes", required = false) String describes,
			@RequestParam(value = "MENUID", required = false) String menuId,
			@RequestParam(value = "READ_P", required = false) String read_p,
			@RequestParam(value = "WRITE_P", required = false) String write_p) {
		BaseResponseData data = new BaseResponseData();
		try {
			int role_id = roleService.insetrRole(role_name, create_user, describes);
			String[] menuIded = menuId.split(",");
			String[] read_ped = read_p.split(",");
			String[] write_ped = write_p.split(",");
			for (int i = 0; i < menuIded.length; i++) {
				roleService.insertRole_menu(role_id, Integer.valueOf(menuIded[i]).intValue(), create_user,
						Integer.valueOf(read_ped[i]).intValue(), Integer.valueOf(write_ped[i]).intValue());
			}
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}

	/*
	 * 删除
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponseData delete(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			roleService.deleteRole(id);
			roleService.deleteRole_menu(id);
			data.setMessage("删除成功");
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("删除失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/*
	 * 修改之前
	 */

	@RequestMapping("/toEdit")
	public ModelAndView toEdit(@RequestParam("id") int role_id) {
		ModelAndView mv = new ModelAndView();
		Role roleAll = roleService.findByRoleId(role_id);
		List<Role_menu> role_menuList = roleService.selectRole_menu(role_id);
		mv.setViewName("jsp/systemjsp/role_add");
		mv.addObject("msg", "edit");
		mv.addObject("roleAll", roleAll);
		mv.addObject("role_menuList", role_menuList);
		return mv;
	}

	/*
	 * @RequestMapping("/toEdit")
	 * 
	 * @ResponseBody public BaseResponseData toEdit(@RequestParam("id") int
	 * role_id) { BaseResponseData data = new BaseResponseData(); try { Role
	 * roleAll=roleService.findByRoleId(role_id); List<Role_menu> role_menuList
	 * = roleService.selectRole_menu(role_id); if (role_menuList!=null) {
	 * data.setMessage("获取成功"); data.setCode(WebResponseCode.SUCCESS);
	 * HashMap<String, Object> resData = new HashMap<String, Object>();
	 * resData.put("role_menuList", role_menuList); resData.put("roleAll",
	 * roleAll); data.setResponseData(resData); return data; } else {
	 * data.setCode(WebResponseCode.SUCCESS); data.setMessage("没有数据！");
	 * HashMap<String, Object> resData = new HashMap<String, Object>();
	 * resData.put("role_menuList", ""); data.setResponseData(resData); return
	 * data; }
	 * 
	 * } catch (Exception e) { data.setCode(WebResponseCode.ERROR);
	 * data.setMessage("获取失败,服务器异常，请稍后重试！"); HashMap<String, Object> resData =
	 * new HashMap<String, Object>(); resData.put("errorcode",
	 * WebResponseCode.EXECUTIONERROR); resData.put("errormessage",
	 * WebResponseCode.EXECUTIONERRORMESSAGE); data.setResponseData(resData);
	 * return data; } }
	 */

	/*
	 * 修改之后
	 */
	@RequestMapping("/edit")
	@ResponseBody
	public BaseResponseData edit(@RequestParam(value = "create_user", required = false) int id,
			@RequestParam(value = "MENUID", required = false) String menuId,
			@RequestParam(value = "READ_P", required = false) String read_p,
			@RequestParam(value = "WRITE_P", required = false) String write_p,
			@RequestParam(value = "role_name", required = false) String role_name,
			@RequestParam(value = "describes", required = false) String describes) {
		BaseResponseData data = new BaseResponseData();
		try {
			roleService.updateRole(id, role_name, describes);
			String[] menuIded = menuId.split(",");
			String[] read_ped = read_p.split(",");
			String[] write_ped = write_p.split(",");
			for (int i = 0; i < menuIded.length; i++) {
				roleService.updateRole_menu(id, Integer.valueOf(menuIded[i]).intValue(),
						Integer.valueOf(read_ped[i]).intValue(), Integer.valueOf(write_ped[i]).intValue());
			}
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}

}
