package com.cn.hy.controller.serviceset;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.serviceset.EmailServer;
import com.cn.hy.pojo.serviceset.UserEmail;
import com.cn.hy.pojo.system.User;
import com.cn.hy.service.serviceset.ErrorWarnSetService;
import com.cn.hy.service.system.UserService;

@Controller
@RequestMapping("/ErrorWarnSet")
public class ErrorWarnSetController {
	@Resource
	private ErrorWarnSetService errorWarnSetService;
	@Resource
	private UserService userService;
	/**
	 * 接收邮箱获取
	 */
	@RequestMapping("/listUserEmail")
	@ResponseBody
	public BaseResponseData listUserEmail(){
		BaseResponseData data = new BaseResponseData();
		try {
			List<UserEmail> varList = errorWarnSetService.listUserEmail();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("varList", varList);
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
	
	/**
	 * 添加接收邮箱
	 */
	@RequestMapping("/insertUserEmail")
	@ResponseBody
	public BaseResponseData insertUserEmail( @RequestParam("user_id") int user_id,
			@RequestParam("emailaddr") String emailaddr ) {
		 BaseResponseData data = new BaseResponseData();
		try {
			UserEmail userEmail = new UserEmail();
			userEmail.setUser_id(user_id);
			userEmail.setEmailaddr(emailaddr);
			userEmail.setCreate_time(new Date());
			errorWarnSetService.saveUserEmail(userEmail);
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
	
	/*
	* 修改接收邮箱之前
	*/
	@RequestMapping("/toUpdateUserEmail")
	@ResponseBody
	public BaseResponseData toUpdateUserEmail(@RequestParam("id") int id){
		 BaseResponseData data = new BaseResponseData();
		 try {
				UserEmail userEmail = errorWarnSetService.getUserEmailById(id);
				List<User> userlist = userService.selectAllUser();
				data.setMessage("更新成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("userEmail", userEmail);
				resData.put("userlist", userlist);
				data.setResponseData(resData);
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
	 * 修改接收邮箱
	 */
	@RequestMapping("/updateUserEmail")
	@ResponseBody
	public BaseResponseData updateUserEmail( @RequestParam("id") int id,
			@RequestParam("user_id") int user_id,@RequestParam("emailaddr") String emailaddr ) {
		 BaseResponseData data = new BaseResponseData();
		try {
			UserEmail userEmail = errorWarnSetService.getUserEmailById(id);
			userEmail.setUser_id(user_id);
			userEmail.setEmailaddr(emailaddr);
			userEmail.setUpdate_time(new Date());
			errorWarnSetService.updateUserEmail(userEmail);
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
	 * 删除接收邮箱
	 */
	@RequestMapping("/deleteUserEmail")
	@ResponseBody
	public BaseResponseData deleteUserEmail(@RequestParam("id") int id) {
		 BaseResponseData data = new BaseResponseData();
		try {
			errorWarnSetService.deleteUserEmail(id);
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
	
	/**
	 * 邮箱服务获取
	 */
	@RequestMapping("/getEmailServer")
	@ResponseBody
	public BaseResponseData getEmailServer(){
		BaseResponseData data = new BaseResponseData();
		try {
			EmailServer emailServer = errorWarnSetService.getEmailServer();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("emailServer", emailServer);
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
	
	/**
	 * 修改邮箱服务
	 */
	@RequestMapping("/updateEmailServer")
	@ResponseBody
	public BaseResponseData updateEmailServer( @RequestParam("serveraddr") String serveraddr,
			@RequestParam("emailid") String emailid, @RequestParam("passwd") String passwd,
			@RequestParam("assgin") String assgin) {
		 BaseResponseData data = new BaseResponseData();
		try {
			EmailServer emailServer = errorWarnSetService.getEmailServer();
			if(null == emailServer){
				emailServer = new EmailServer();
				emailServer.setServeraddr(serveraddr);
				emailServer.setEmailid(emailid);
				emailServer.setPasswd(passwd);
				emailServer.setAssgin(assgin);
				emailServer.setCreate_time(new Date());
				errorWarnSetService.saveEmailServer(emailServer);
			}else{
				emailServer.setServeraddr(serveraddr);
				emailServer.setEmailid(emailid);
				if(null != passwd && !"".equals(passwd)){
					emailServer.setPasswd(passwd);
				}
				emailServer.setAssgin(assgin);
				emailServer.setUpdate_time(new Date());
				errorWarnSetService.updateEmailServer(emailServer);
			}
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
