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
import com.cn.hy.pojo.serviceset.FormColnum;
import com.cn.hy.pojo.serviceset.FormTimeSet;
import com.cn.hy.service.modbustcp.ModbusSkAppService;
import com.cn.hy.service.serviceset.ReportSetService;

/**
 * 报表设置
 * @author HCheng
 *
 */
@Controller
@RequestMapping("/ReportSet")
public class ReportSetController {
	@Resource
	private ReportSetService reportSetService;
	@Resource 
	private ModbusSkAppService modbusSkAppService;
	
	/**
	 * 获取报表时间
	 */
	@RequestMapping("/listFormTime")
	@ResponseBody
	public BaseResponseData listFormTime(@RequestParam("form_type") int form_type,
			@RequestParam("showin") int showin){
		BaseResponseData data = new BaseResponseData();
		try {
			FormTimeSet formTimeSet = new FormTimeSet();
			formTimeSet.setForm_type(form_type);
			if(showin == 1){
				formTimeSet.setShowin(1);
			}
			List<FormTimeSet> formTimeList = reportSetService.listFormTimeSet(formTimeSet);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("formTimeList", formTimeList);
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
	 * 添加报表时间
	 */
	@RequestMapping("/insertFormTime")
	@ResponseBody
	public BaseResponseData insertFormTime( @RequestParam("time_name") String time_name,
			@RequestParam("form_type") int form_type) {
		 BaseResponseData data = new BaseResponseData();
		try {
			FormTimeSet formTimeSet = new FormTimeSet();
			formTimeSet.setTime_name(time_name);
			formTimeSet.setForm_type(form_type);
			formTimeSet.setCreate_time(new Date());
			formTimeSet.setShowin(0);//初始时不选中
			reportSetService.saveFormTimeSet(formTimeSet);
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
	
	/**
	 * 修改报表时间选中状态
	 */
	@RequestMapping("/updateShowin")
	@ResponseBody
	public BaseResponseData updateShowin( @RequestParam("id") int id,
			@RequestParam("showin") int showin) {
		 BaseResponseData data = new BaseResponseData();
		try {
			FormTimeSet formTimeSet = new FormTimeSet();
			formTimeSet.setId(id);
			formTimeSet.setShowin(showin);
			reportSetService.updateShowin(formTimeSet);
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
	 * 获取显示字段
	 */
	@RequestMapping("/listFormColnum")
	@ResponseBody
	public BaseResponseData listFormColnum(@RequestParam("form_type") int form_type){
		BaseResponseData data = new BaseResponseData();
		try {
			FormColnum formColnum = new FormColnum();
			formColnum.setForm_type(form_type);
			List<FormColnum> formColnumList = reportSetService.listFormColnum(formColnum);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("formColnumList", formColnumList);
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
	 * 修改报表时间选中状态
	 */
	@RequestMapping("/updateFormColnum")
	@ResponseBody
	public BaseResponseData updateFormColnum( @RequestParam("id") int id,
			@RequestParam("showin") int showin) {
		 BaseResponseData data = new BaseResponseData();
		try {
			FormColnum formColnum = new FormColnum();
			formColnum.setId(id);
			formColnum.setShowin(showin);
			reportSetService.updateFormColnum(formColnum);
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
