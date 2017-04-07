package com.cn.hy.controller.serviceset;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.cn.hy.pojo.modbustcp.ModbusSkApp;
import com.cn.hy.pojo.system.DeviceBaseData;
import com.cn.hy.service.modbustcp.ModbusSkAppService;
import com.cn.hy.service.system.DeviceBDService;

/**
 * 页面设置
 * @author HCheng
 *
 */
@Controller
@RequestMapping("/PageSet")
public class PageSetController {
	@Resource
	private ModbusSkAppService modbusSkAppService;
	@Resource
	private DeviceBDService deviceBDService;
	
	/**
	 * 获取基本参数
	 */
	@RequestMapping("/listModbusSkApp")
	@ResponseBody
	public BaseResponseData listModbusSkApp(@RequestParam("modbus_type") int modbus_type){
		BaseResponseData data = new BaseResponseData();
		try {
			List<ModbusSkApp> msList = modbusSkAppService.alistModbusSkApp(modbus_type);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("msList", msList);
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
	 * 修改基本参数选中状态
	 */
	@RequestMapping("/updateModbusSkApp")
	@ResponseBody
	public BaseResponseData updateModbusSkApp(@RequestParam("paramStr") String paramStr,
			@RequestParam("oldparm") String oldparm) {
		 BaseResponseData data = new BaseResponseData();
		try {
			if(!paramStr.equals(oldparm)){
				List<String> newlist = Arrays.asList(paramStr.split(","));
				List<String> oldlist = Arrays.asList(oldparm.split(","));
				List<String> saveList = new ArrayList<String>();
				List<String> deleteList = new ArrayList<String>();
				for (String string : oldlist) {
					if(!newlist.contains(string) && !"".equals(string)){
						deleteList.add(string);
					}
				}
				for (String string : newlist) {
					if(!oldlist.contains(string) && !"".equals(string)){
						saveList.add(string);
					}
				}
				ModbusSkApp modbusSkApp = new ModbusSkApp(); 
				if(saveList !=null && saveList.size() > 0){
					String saveids = "";
					for (int i = 0; i < saveList.size(); i++) {
						saveids += saveList.get(i);
						if(i < saveList.size()-1){
							saveids += ",";
						}
						//添加基本参数配置到风场预览
						modbusSkApp = modbusSkAppService.getModbusSkAppById(Integer.parseInt(saveList.get(i)));
						DeviceBaseData deviceBaseData = new DeviceBaseData();
						deviceBaseData.setParamter_id(modbusSkApp.getAddr());
						deviceBaseData.setModbustype(modbusSkApp.getModbus_type());
						deviceBaseData.setShowtype(0);
						deviceBaseData.setCreate_time(new Date());
						deviceBDService.saveDeviceBaseData(deviceBaseData);
					}
					if(!"".equals(saveids)){
						ModbusSkApp ms = new ModbusSkApp();
						ms.setIdStr(saveids);
						ms.setIsbasicparm(1);
						modbusSkAppService.updateModbusSkApp(ms);
					}
				}
				
				if(deleteList != null && deleteList.size() > 0){
					String delids = "";
					for (int i = 0; i < deleteList.size(); i++) {
						delids += deleteList.get(i);
						if(i < deleteList.size()-1){
							delids += ",";
						}
						//删除风场预览中的基本参数配置
						modbusSkApp = modbusSkAppService.getModbusSkAppById(Integer.parseInt(deleteList.get(i)));
						DeviceBaseData deviceBaseData = new DeviceBaseData();
						deviceBaseData.setParamter_id(modbusSkApp.getAddr());
						deviceBaseData.setModbustype(modbusSkApp.getModbus_type());
						deviceBDService.deleteDeviceBaseData(deviceBaseData);
						
					}
					if(!"".equals(delids)){
						ModbusSkApp ms = new ModbusSkApp();
						ms.setIdStr(delids);
						ms.setIsbasicparm(0);
						modbusSkAppService.updateModbusSkApp(ms);
					}
				}
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
	
	/**
	 * 获取风场预览参数
	 */
	@RequestMapping("/listDeviceBaseData")
	@ResponseBody
	public BaseResponseData listDeviceBaseData(@RequestParam("modbustype") int modbustype){
		BaseResponseData data = new BaseResponseData();
		try {
			List<DeviceBaseData> dbList = deviceBDService.listDeviceBaseData(modbustype);
			ModbusSkApp modbusSkApp = new ModbusSkApp();
			for (DeviceBaseData deviceBaseData : dbList) {
				modbusSkApp = modbusSkAppService.getModbusSkApp(deviceBaseData.getParamter_id(),modbustype);
				deviceBaseData.setName(modbusSkApp.getName());
				deviceBaseData.setAddr(modbusSkApp.getAddr());
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("dbList", dbList);
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
	 * 修改风机显示参数
	 */
	@RequestMapping("/updateDeviceBaseData")
	@ResponseBody
	public BaseResponseData updateDeviceBaseData(@RequestParam("paramStr") String paramStr,
			@RequestParam("oldparm") String oldparm) {
		 BaseResponseData data = new BaseResponseData();
		try {
			if(!paramStr.equals(oldparm)){
				List<String> newlist = Arrays.asList(paramStr.split(","));
				List<String> oldlist = Arrays.asList(oldparm.split(","));
				List<String> saveList = new ArrayList<String>();
				List<String> deleteList = new ArrayList<String>();
				for (String string : oldlist) {
					if(!newlist.contains(string) && !"".equals(string)){
						deleteList.add(string);
					}
				}
				for (String string : newlist) {
					if(!oldlist.contains(string) && !"".equals(string)){
						saveList.add(string);
					}
				}
				if(saveList !=null && saveList.size() > 0){
					String saveids = "";
					for (int i = 0; i < saveList.size(); i++) {
						saveids += saveList.get(i);
						if(i < saveList.size()-1){
							saveids += ",";
						}
					}
					if(!"".equals(saveids)){
						DeviceBaseData bdData = new DeviceBaseData();
						bdData.setIds(saveids);
						bdData.setShowtype(1);
						deviceBDService.updateDeviceBaseData(bdData);
					}
				}
				
				if(deleteList != null && deleteList.size() > 0){
					String delids = "";
					for (int i = 0; i < deleteList.size(); i++) {
						delids += deleteList.get(i);
						if(i < deleteList.size()-1){
							delids += ",";
						}
					}
					if(!"".equals(delids)){
						DeviceBaseData bdData = new DeviceBaseData();
						bdData.setIds(delids);
						bdData.setShowtype(0);
						deviceBDService.updateDeviceBaseData(bdData);
					}
				}
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
