package com.cn.hy.controller.system;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.system.Menu;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.pojo.system.User;
import com.cn.hy.service.system.MenuService;
import com.cn.hy.service.system.RoleService;
import com.cn.hy.service.system.UserService;

@Controller
@RequestMapping("/Menu")
public class MenuController {
	@Resource
	private MenuService menuService;
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData goMenuList() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Menu> menuList = menuService.selectMenu();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("menuList", menuList);
			data.setResponseData(resData);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}
	
	@RequestMapping("/updateState")
	@ResponseBody
	public BaseResponseData updateRole(@RequestParam(value="id",required = false ) Integer id,
			@RequestParam(value="state",required = false ) String state){
		BaseResponseData data = new BaseResponseData();
		try {
				menuService.updateMenu(state, id);
				data.setMessage("更新成功");
				data.setCode(WebResponseCode.SUCCESS);
				return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("更新失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}
	
	/**
	 * 获取权限菜单
	 */
	@RequestMapping("/listRoleMenu")
	@ResponseBody
	public BaseResponseData listRoleMenu(HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			String employeeId = (String)request.getSession().getAttribute("employeeId");
			User user = userService.getUserByEmployeeId(employeeId);
			List<Role_menu> rolemenuList = null;
			if(null != user){
				rolemenuList = roleService.selectRole_menu(user.getRoleId());
				for (int i = 0; i < rolemenuList.size(); i++) {
					//有读权限&菜单启用&用户状态为正常
					if(rolemenuList.get(i).getRead_p() == 0 && rolemenuList.get(i).getState() == 0 && user.getState() == 0){
						rolemenuList.get(i).setMenu_qx(1);
					}
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("rolemenuList", rolemenuList);
			if(employeeId==null){
				int login=0;
				resData.put("login", login);
			}
			data.setResponseData(resData);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}
	
	@RequestMapping("/chooseMenu")
	public ModelAndView chooseMenu(String flag,HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView();
		try {
			String viewName = "";
			if("1".equals(flag)){//风场预览
				viewName = "jsp/serviceset/deviceview";
			}else if("2".equals(flag)){//基本参数
				viewName = "jsp/basic/basiclist";
			}else if("3".equals(flag)){//趋势图
				viewName = "jsp/trendchart/trendchart";
			}else if("4".equals(flag)){//事件记录
				viewName = "jsp/errordataparse/errordataparse_list";
			}else if("5".equals(flag)){//报表统计
				viewName = "jsp/reportstatistics/reportsta_main";
			}else if("6".equals(flag)){//录波管理
				viewName = "jsp/recordingManagement/recordingManagement_list";
			}else if("7".equals(flag)){//服务设置
				viewName = "jsp/serviceset/serviceset_left";
			}else if("8".equals(flag)){//系统设置
				viewName = "jsp/systemjsp/systemSet_left";
			}else if("9".equals(flag)){//帮助信息
				viewName = "jsp/helpMessage/helpMessage";
			}
			mv.setViewName(viewName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mv;
	}
	//退出系统
	@RequestMapping("/exitSystem")
	public void exitSystem(HttpServletRequest request,HttpServletResponse response){
		try {
			HttpSession session=request.getSession();
			session.invalidate();
			String name=request.getContextPath();
			PrintWriter out = response.getWriter();
			out.println("<script>top.location.href='"+name+"/login.jsp';</script>");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
