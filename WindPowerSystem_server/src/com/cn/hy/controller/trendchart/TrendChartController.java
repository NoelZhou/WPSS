package com.cn.hy.controller.trendchart;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.trendchart.EchartData;
import com.cn.hy.service.trendchart.TrendChartService;


/**
 * 趋势图
 * @author HCheng
 *
 */
@Controller
@RequestMapping("/TrendChart")
public class TrendChartController {
	@Resource 
	private TrendChartService trendChartService;
	
	@RequestMapping("/devicelist")
	@ResponseBody
	public BaseResponseData devicelist(@RequestParam("modbustype_id") int modbustype_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Device> deviceList = trendChartService.listDeviceByModbustype(modbustype_id);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceList", deviceList);
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
	
	
	@RequestMapping("/getChartData")
	@ResponseBody
	public BaseResponseData getChartData(String deviceids,String addrs,
			@RequestParam("modbustype_id") int modbustype_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			EchartData echartData = trendChartService.getEchartData(deviceids, addrs, modbustype_id);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("echartData", echartData);
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
	
}
