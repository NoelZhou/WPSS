package com.cn.hy.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.bean.Md5Util;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.system.Role;
import com.cn.hy.pojo.system.User;
import com.cn.hy.service.system.RoleService;
import com.cn.hy.service.system.UserService;
import com.cn.hy.service.system.User_RoleService;

@Controller
@RequestMapping("/User")
public class UserController {
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private User_RoleService user_RoleService;

	@RequestMapping("/login")
	@ResponseBody
	public BaseResponseData login(@RequestParam("employeeId") String employeeId, @RequestParam("passwd") String passwd,
			/* @RequestParam("vcode") String vcode, */ HttpServletRequest c) {
		BaseResponseData data = new BaseResponseData();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		int state = 0;
		try {
			HashMap<String, Object> users = new HashMap<String, Object>();
			String pwd = Md5Util.GetMD5Code(passwd.trim());
			users.put("employeeId", employeeId.trim());
			users.put("passwd", pwd);
			User user = userService.selectUser(employeeId, pwd);
			if (user != null) {
				data.setCode(WebResponseCode.SUCCESS);
				HttpSession session = c.getSession();
				/*
				 * String code = (String) session.getAttribute("hy.vcode"); if
				 * (vcode == null || !vcode.toLowerCase().equals(code)) {
				 * 
				 * data.setMessage("验证不正确"); return data; }
				 */
				String errorDate = user.getLoginerrordate().substring(0, 10);
				String date = df1.format(new Date());
				if (java.sql.Date.valueOf(date).after(java.sql.Date.valueOf(errorDate))) {
					user.setState(0);
					// user.setLogintimes(30*60);
					user.setLogintimes(0);
					userService.updateUserTimes(user);
				}
				if (user.getLogintimes() <= 5) {
					user.setLogintimes(0);
					user.setLoginerrordate(df.format(new Date()));
					userService.updateUserTimes(user);
				}
				session.setAttribute("employeeId", user.getEmployeeId());
				session.setMaxInactiveInterval(30 * 60);
				data.setMessage("获取成功");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("users", user);
				data.setResponseData(resData);
				return data;
			} else {
				// 一个账户，密码五次都输入错误，账户锁定
				User userOne = userService.getUserByEmployeeId(employeeId);
				if (userOne != null) {
					String errorDate = userOne.getLoginerrordate().substring(0, 10);
					String date = df1.format(new Date());
					if (java.sql.Date.valueOf(date).after(java.sql.Date.valueOf(errorDate))) {
						// 起始日期大于结束日期
						userOne.setState(0);
						userOne.setLogintimes(1);
						userOne.setLoginerrordate(df.format(new Date()));
						userService.updateUserTimes(userOne);
					} else {
						if (userOne.getLogintimes() < 5) {
							userOne.setLogintimes(userOne.getLogintimes() + 1);
							userOne.setLoginerrordate(df.format(new Date()));
							userService.updateUserTimes(userOne);
						}
						if (userOne.getLogintimes() >= 5) {
							userOne.setState(1);
							userOne.setLoginerrordate(df.format(new Date()));
							userService.updateStateTime(userOne);
							state = 1;
							/*
							 * long a = new Date().getTime(); long b =
							 * getTimeOf12().getTimeInMillis(); int c3 = (int)(b
							 * - a); timer1(userOne,60*1000);
							 */
						}
					}
				}
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("用户名或密码错误！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("users", user);
				resData.put("state", state);
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
	 * protected Calendar getTimeOf12() { Calendar cal = Calendar.getInstance();
	 * cal.setTime(new Date()); cal.set(Calendar.HOUR_OF_DAY, 0);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
	 * cal.set(Calendar.MILLISECOND, 0); cal.add(Calendar.DAY_OF_MONTH, 1);
	 * return cal; }
	 * 
	 * //定时器的写法 protected void timer1(final User userOne,int c3) { Timer timer =
	 * new Timer(); timer.schedule(new TimerTask() { public void run() {
	 * userOne.setState(0); userService.updateStateTime(userOne);
	 * System.out.println("-------设定要指定任务--------"); } }, c3);//
	 * 设定指定的时间time,此处为2000毫秒 }
	 */

	@RequestMapping("/selectAllUser")
	@ResponseBody
	public BaseResponseData selectAllUser() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<User> userlist = userService.selectAllUser();
			List<Role> rolelist = roleService.selectRole();
			data.setMessage("查询成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("userlist", userlist);
			resData.put("rolelist", rolelist);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("查询失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}

	@RequestMapping("/selectUserById")
	@ResponseBody
	public BaseResponseData selectUserById(@RequestParam("id") Integer id) {
		BaseResponseData data = new BaseResponseData();
		try {
			User user = userService.selectUserById(id);
			List<Role> rolelist = roleService.selectRole();
			data.setMessage("查询成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("user", user);
			resData.put("rolelist", rolelist);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("查询失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}

	@RequestMapping("/insert")
	@ResponseBody
	public BaseResponseData insert(@RequestParam("employeeId") String employeeId, @RequestParam("passwd") String passwd,
			@RequestParam("roleId") Integer roleId, @RequestParam("name") String name,
			@RequestParam("employeeName") String employeeName, @RequestParam("telephone") String telephone,
			@RequestParam("state") Integer state) {
		BaseResponseData data = new BaseResponseData();
		try {
			int userId = userService.insertUser(employeeId, Md5Util.GetMD5Code(passwd), name, employeeName, telephone,
					state);
			user_RoleService.insertUserRole(userId, roleId);
			data.setMessage("添加成功");
			data.setCode(WebResponseCode.SUCCESS);
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("添加失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}

	}

	@RequestMapping("/delete")
	@ResponseBody
	public BaseResponseData delete(@RequestParam("id") String id) {
		BaseResponseData data = new BaseResponseData();
		try {
			String[] ss = id.split(",");
			for (int i = 0; i < ss.length; i++) {
				int mid = Integer.parseInt(ss[i]);
				userService.deleteUser(mid);
				user_RoleService.deleteUserId(mid);
			}

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

	@RequestMapping("/updateCheck")
	@ResponseBody
	public BaseResponseData updateCheck(@RequestParam(value = "id", required = false) int id,
			@RequestParam(value = "employeeId", required = false) String employeeId) {
		BaseResponseData data = new BaseResponseData();
		try {
			User user = new User();
			user.setId(id);
			user.setEmployeeId(employeeId);
			List<User> userList = userService.findByIdAndEmpID(user);
			String message = "";
			if (userList.size() == 0) {// 有相同的用户名
				User userNameTooList = userService.getUserByEmployeeId(employeeId);
				if (userNameTooList != null) {
					message = "帐号已存在，请重新输入";
				}
			}
			data.setMessage("更新成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("message", message);
			data.setResponseData(resData);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("删除失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
		return data;
	}

	@RequestMapping("/update")
	@ResponseBody
	public BaseResponseData update(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "employeeId", required = false) String employeeId,
			@RequestParam(value = "employeeName", required = false) String employeeName,
			@RequestParam(value = "roleId", required = false) Integer roleId,
			@RequestParam(value = "telephone", required = false) String telephone,
			@RequestParam(value = "state", required = false) Integer state, @RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			userService.updateUser(name, employeeId, employeeName, telephone, state, id);
			user_RoleService.updateUserRole(id, roleId);
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

	/*
	 * @RequestMapping("/insertRole")
	 * 
	 * @ResponseBody public BaseResponseData insertRole(@RequestParam(value =
	 * "roleId", required = false) Integer roleId,
	 * 
	 * @RequestParam(value = "userId", required = false) Integer userId) {
	 * BaseResponseData data = new BaseResponseData(); try {
	 * userService.insertRole(userId,roleId); data.setMessage("编辑成功");
	 * data.setCode(WebResponseCode.SUCCESS); return data; } catch (Exception e)
	 * { data.setCode(WebResponseCode.ERROR);
	 * data.setMessage("编辑失败,服务器异常，请稍后重试！"); HashMap<String, Object> resData =
	 * new HashMap<String, Object>(); resData.put("errorcode",
	 * WebResponseCode.EXECUTIONERROR); resData.put("errormessage",
	 * WebResponseCode.EXECUTIONERRORMESSAGE); data.setResponseData(resData);
	 * return data; }
	 * 
	 * }
	 */

	@RequestMapping("/updateState")
	@ResponseBody
	public BaseResponseData updateState(@RequestParam(value = "state", required = false) Integer state,
			@RequestParam(value = "id", required = false) Integer id) {
		BaseResponseData data = new BaseResponseData();
		try {
			int st = 0;
			if (state == 0) {
				st = 1;
			} else {
				st = 0;
			}
			userService.updateState(st, id);
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

	/*
	 * @RequestMapping("/get") public BaseResponseData get(@RequestParam("id")
	 * long id) { BaseResponseData data = new BaseResponseData(); try { String
	 * str = "编号" + id; // String user=userService.selectUser(user).getName();
	 * String user = ""; data.setCode(WebResponseCode.SUCCESS);
	 * data.setMessage("获取成功"); HashMap<String, Object> resData = new
	 * HashMap<String, Object>(); resData.put("用户名", user);
	 * data.setResponseData(resData); return data; } catch (Exception e) {
	 * data.setCode(WebResponseCode.ERROR); data.setMessage("获取失败");
	 * HashMap<String, Object> resData = new HashMap<String, Object>();
	 * resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
	 * resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
	 * data.setResponseData(resData); return data; }
	 * 
	 * }
	 */
	@RequestMapping("/updatePass")
	@ResponseBody
	public BaseResponseData updatepass(@RequestParam(value = "passwd", required = false) String passwd,
			@RequestParam(value = "id", required = false) Integer id) {
		/*
		 * 密码重置逻辑ceng
		 */
		BaseResponseData data = new BaseResponseData();
		try {
			String pass = "0";
			if (passwd != null) {
				pass = "123456";
			}
			userService.updatepass(Md5Util.GetMD5Code(pass), id);
			System.out.print(id);

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

}
