package com.cn.hy.controller.serviceset;



import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.serviceset.Mqtt_device;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.serviceset.Mqtt_deviceService;
import com.cn.hy.service.system.DeviceService;

@Controller
@RequestMapping("/Mqtt_device")
public class Mqtt_deviceController {

	@Resource
	private Mqtt_deviceService mqtt_deviceService;
	@Resource
	private DeviceService deviceService;
	/*
	 * 查询所有
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData list() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Mqtt_device> Mqtt_deviceList = mqtt_deviceService
					.selectMqtt_device();
			if (Mqtt_deviceList != null) {
				data.setMessage("获取成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("Mqtt_deviceList", Mqtt_deviceList);
				data.setResponseData(resData);
			} else {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("没有数据！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("Mqtt_deviceList", "");
				data.setResponseData(resData);
			}
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}

	/*
	 * 查询所有变流器设备（新增之前）
	 */
	@RequestMapping("/listDevice")
	@ResponseBody
	public BaseResponseData listDevice() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Device> deviceList = deviceService.listDevice();
			List<Mqtt_device> Mqtt_deviceList = mqtt_deviceService
					.selectMqtt_device();
			if (deviceList != null) {
				for (int j = 0; j < Mqtt_deviceList.size(); j++) {
					for (int i = 0; i < deviceList.size(); i++) {
						if(deviceList.get(i).getId()==Mqtt_deviceList.get(j).getDevice_id()){
							deviceList.remove(i);
							i--;
						}
				 }
				}
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("获取成功");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("deviceList", deviceList);
				data.setResponseData(resData);
			} else {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("没有数据！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("deviceList", "");
				data.setResponseData(resData);
			}
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}
	/*
	 * 新增之后
	 */
	@RequestMapping("/insertMqtt_device")
	@ResponseBody
	public BaseResponseData insertMqtt_device(@RequestParam("device_id") int device_id,
			@RequestParam("sn") String sn,@RequestParam("name") String name,@RequestParam("device_lx_mh") int device_lx_mh,
			@RequestParam("device_CJ") String device_CJ,@RequestParam("device_xh") String device_xh){
		BaseResponseData data = new BaseResponseData();
		try {
			Mqtt_device mqtt_device=new Mqtt_device();
			mqtt_device.setDevice_id(device_id);
			mqtt_device.setSn(sn);
			mqtt_device.setName(name);
			mqtt_device.setDevice_yd_lx(28);
			mqtt_device.setDevice_lx_mh(device_lx_mh);
			mqtt_device.setDevice_CJ(device_CJ);
			mqtt_device.setDevice_xh(device_xh);
			mqtt_device.setRun_state(0);
			mqtt_deviceService.insertMqtt_device(mqtt_device);
			data.setMessage("添加成功");
			data.setCode(WebResponseCode.SUCCESS);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}
	/*
	 * 修改之前
	 */
	@RequestMapping("/toEditMqtt_device")
	@ResponseBody
	public BaseResponseData toEditMqtt_device(@RequestParam("id") int id,@RequestParam("device_id") int device_id){
		BaseResponseData data = new BaseResponseData();
		try {
			List<Device> deviceList = deviceService.listDevice();
			List<Mqtt_device> Mqtt_deviceList = mqtt_deviceService
					.selectMqtt_device();
			for (int j = 0; j < Mqtt_deviceList.size(); j++) {
				for (int i = 0; i < deviceList.size(); i++) {
					if(deviceList.get(i).getId()==Mqtt_deviceList.get(j).getDevice_id() && device_id!=Mqtt_deviceList.get(j).getDevice_id()){
						deviceList.remove(i);
						i--;
					}
				}
			}
			Mqtt_device mqtt_deviceList=mqtt_deviceService.findById(id);
			data.setMessage("查询成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceList", deviceList);
			resData.put("mqtt_deviceList", mqtt_deviceList);
			data.setResponseData(resData);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}
	/*
	 * 修改之后
	 */
	@RequestMapping("/editMqtt_device")
	@ResponseBody
	public BaseResponseData editMqtt_device(
			@RequestParam("id") int id,@RequestParam("device_id") int device_id,
			@RequestParam("sn") String sn,@RequestParam("name") String name,@RequestParam("device_lx_mh") int device_lx_mh,
			@RequestParam("device_CJ") String device_CJ,@RequestParam("device_xh") String device_xh){
		BaseResponseData data = new BaseResponseData();
		try {
			Mqtt_device mqtt_device=new Mqtt_device();
			mqtt_device.setDevice_id(device_id);
			mqtt_device.setSn(sn);
			mqtt_device.setName(name);
			mqtt_device.setDevice_lx_mh(device_lx_mh);
			mqtt_device.setDevice_CJ(device_CJ);
			mqtt_device.setDevice_xh(device_xh);
			mqtt_device.setId(id);
			mqtt_deviceService.editMqtt_device(mqtt_device);
			data.setMessage("更新成功");
			data.setCode(WebResponseCode.SUCCESS);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}
	/*
	 * 删除
	 */
	@RequestMapping("/deleteMqtt_device")
	@ResponseBody
	public BaseResponseData deleteMqtt_device(@RequestParam("id") int id){
		BaseResponseData data = new BaseResponseData();
		try {
			mqtt_deviceService.deleteMqtt_device(id);
			data.setMessage("删除成功");
			data.setCode(WebResponseCode.SUCCESS);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}
}

