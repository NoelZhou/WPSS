package com.cn.hy.controller.reportstatistics;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.reportstatistics.ErrorDataRepair;
import com.cn.hy.pojo.reportstatistics.FormRunData;
import com.cn.hy.pojo.reportstatistics.RowDevice;
import com.cn.hy.pojo.serviceset.FormColnum;
import com.cn.hy.service.reportstatistics.ReportStatService;
import com.cn.hy.service.serviceset.ReportSetService;
import com.cn.hy.util.ExcelExport;

/**
 * 报表统计
 * @author HCheng
 *
 */
@Controller
@RequestMapping("/ReportSta")
public class ReportStaController {
	@Resource
	private ReportStatService reportStatService;
	@Resource
	private ReportSetService reportSetService;
	
	/**
	 * 获取报表列表
	 */
	@RequestMapping("/listReportInfo")
	@ResponseBody
	public BaseResponseData listReportInfo(@RequestParam("form_type") int form_type,
			@RequestParam("timename") String timename){
		BaseResponseData data = new BaseResponseData();
		try {
//			timename = new String(timename.getBytes("ISO-8859-1"),"UTF-8"); 
			List<RowDevice> rowdeviceList = reportStatService.listRowDevice(form_type, timename);
			Map<String, List<Integer>> map =reportStatService.listMainReport(form_type, timename);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("rowdeviceList", rowdeviceList);
			resData.put("map", map);
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
	
	@RequestMapping("/exportExcel")
	public String exportExcel(@RequestParam("form_type") int form_type,@RequestParam("timename") String timename,
			HttpServletRequest request, HttpServletResponse response) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			timename = URLDecoder.decode(timename, "UTF-8");
			String typename = "";
			if(form_type == 0){
				typename = "故障报表";
			}else if(form_type == 1){
				typename = "告警报表";
			}else if(form_type == 2){
				typename = "运行报表";
			}
			String fileName = new String((typename+"_"+timename+"_"+format.format(new Date())).getBytes("gb2312"), "iso8859-1")+ ".xlsx";
			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
	        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	        response.setCharacterEncoding("utf-8");
	        ServletOutputStream outputStream = response.getOutputStream();
	        if(form_type == 0 || form_type == 1){
	        	List<RowDevice> rowdeviceList = reportStatService.listRowDevice(form_type, timename);
	 			Map<String, List<Integer>> map =reportStatService.listMainReport(form_type, timename);
	 			ExcelExport.ExportExcel(rowdeviceList, map, outputStream);
	        }else if(form_type == 2){
	        	FormColnum formColnum = new FormColnum();
				formColnum.setForm_type(2);
				formColnum.setShowin(1);
				List<FormColnum> colnumList = reportSetService.listFormColnum(formColnum);
				List<FormRunData> runList = reportStatService.listFormRunData(timename);
				ExcelExport.RunExportExcel(colnumList, runList, outputStream);
	        }
	       
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取故障派单列表
	 */
	@RequestMapping("/listErrorRepair")
	@ResponseBody
	public BaseResponseData listErrorRepair(@RequestParam("repair_state") int repair_state){
		BaseResponseData data = new BaseResponseData();
		try {
			List<ErrorDataRepair> varList = reportStatService.listErrorDataRepair(repair_state);
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
	 * 获取故障派单
	 */
	@RequestMapping("/getErrorRepair")
	@ResponseBody
	public BaseResponseData getErrorRepair(@RequestParam("id") int id){
		BaseResponseData data = new BaseResponseData();
		try {
			ErrorDataRepair errorDataRepair = reportStatService.getErrorDataRepair(id);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorDataRepair", errorDataRepair);
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
	 * 修改故障派单
	 */
	@RequestMapping("/updateErrorRepair")
	@ResponseBody
	public BaseResponseData updateErrorRepair(
			@RequestParam("id") int id,
			@RequestParam("repair_user") String repair_user,
			@RequestParam("repair_result") String repair_result){
		BaseResponseData data = new BaseResponseData();
		try {
			reportStatService.updateErrorDataRepair(id,repair_user,repair_result,2);
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
	 * 获取运行时数据
	 */
	@RequestMapping("/listRunData")
	@ResponseBody
	public BaseResponseData listRunData(@RequestParam("timename") String timename){
		BaseResponseData data = new BaseResponseData();
		try {
//			timename = new String(timename.getBytes("ISO-8859-1"),"UTF-8"); 
			FormColnum formColnum = new FormColnum();
			formColnum.setForm_type(2);
			formColnum.setShowin(1);
			List<FormColnum> colnumList = reportSetService.listFormColnum(formColnum);
			List<FormRunData> runList = reportStatService.listFormRunData(timename);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("colnumList", colnumList);
			resData.put("runList", runList);
			data.setResponseData(resData);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}
	
}
