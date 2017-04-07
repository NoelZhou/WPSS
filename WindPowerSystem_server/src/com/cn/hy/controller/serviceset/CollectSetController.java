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
import com.cn.hy.pojo.serviceset.CollectTimes;
import com.cn.hy.pojo.serviceset.DeviceInfo;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.serviceset.ModbustcpType;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DeviceService;

@Controller
@RequestMapping("/CollectSet")
public class CollectSetController {
	@Resource
	private CollectSetService collectSetService;
	@Resource
	private DeviceService deviceService;

	/**
	 * 获取采集频率
	 */
	@RequestMapping("/getCollectTimes")
	@ResponseBody
	public BaseResponseData getCollectTimes() {
		BaseResponseData data = new BaseResponseData();
		try {
			CollectTimes collectTimes = collectSetService.getCollectTimes();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("collectTimes", collectTimes);
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
	 * 修改采集频率
	 */
	@RequestMapping("/updateCollectTimes")
	@ResponseBody
	public BaseResponseData updateCollectTimes(@RequestParam("real_time") int real_time,
			@RequestParam("history") int history) {
		BaseResponseData data = new BaseResponseData();
		try {
			CollectTimes collectTimes = collectSetService.getCollectTimes();
			if (null == collectTimes) {
				collectTimes = new CollectTimes();
				collectTimes.setReal_time(real_time);
				collectTimes.setHistory(history);
				collectTimes.setCreate_time(new Date());
				collectSetService.saveCollectTimes(collectTimes);
			} else {
				collectTimes.setReal_time(real_time);
				collectTimes.setHistory(history);
				collectTimes.setUpdate_time(new Date());
				// 修改人...
				collectSetService.updateCollectTimes(collectTimes);
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
	 * 获取设备类型列表
	 */
	@RequestMapping("/listDeviceType")
	@ResponseBody
	public BaseResponseData getDeviceType() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<DeviceType> deviceTypeList = collectSetService.listDeviceType();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceTypeList", deviceTypeList);
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
	 * 添加设备类型
	 */
	@RequestMapping("/insertDeviceType")
	@ResponseBody
	public BaseResponseData insertDeviceType(@RequestParam("modbus_type") int modbus_type,
			@RequestParam("name") String name) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceType deviceType = new DeviceType();
			deviceType.setModbus_type(modbus_type);
			deviceType.setName(name);
			deviceType.setCreate_time(new Date());
			collectSetService.saveDeviceType(deviceType);
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
	 * 修改设备类型之前
	 */
	@RequestMapping("/toUpdateDeviceType")
	@ResponseBody
	public BaseResponseData toUpdateDeviceType(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceType deviceType = collectSetService.getDeviceTypeById(id);
			List<ModbustcpType> modbustcptypeList = collectSetService.listModbustcpType();
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceType", deviceType);
			resData.put("modbustcptypeList", modbustcptypeList);
			data.setResponseData(resData);
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
	 * 修改设备类型
	 */
	@RequestMapping("/updateDeviceType")
	@ResponseBody
	public BaseResponseData updateDeviceType(@RequestParam("id") int id, @RequestParam("modbus_type") int modbus_type,
			@RequestParam("name") String name) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceType deviceType = collectSetService.getDeviceTypeById(id);
			deviceType.setModbus_type(modbus_type);
			deviceType.setName(name);
			deviceType.setUpdate_time(new Date());
			collectSetService.updateDeviceType(deviceType);
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
	 * 协议类型选择获取
	 */
	@RequestMapping("/listModbustcpType")
	@ResponseBody
	public BaseResponseData listModbustcpType() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<ModbustcpType> modbustcptypeList = collectSetService.listModbustcpType();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("modbustcptypeList", modbustcptypeList);
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
	 * 删除设备类型
	 */
	@RequestMapping("/deleteDeviceType")
	@ResponseBody
	public BaseResponseData deleteDeviceType(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			collectSetService.deleteDeviceType(id);
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
	 * 设备列表获取
	 */
	@RequestMapping("/listDevice")
	@ResponseBody
	public BaseResponseData listDevice() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<Device> deviceList = deviceService.listDevice();
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

	/**
	 * 添加设备
	 */
	@RequestMapping("/insertDevice")
	@ResponseBody
	public BaseResponseData insertDevice(@RequestParam("device_type_id") int device_type_id,
			@RequestParam("name") String name) {
		BaseResponseData data = new BaseResponseData();
		try {
			Device device = new Device();
			device.setDevice_type_id(device_type_id);
			device.setName(name);
			deviceService.saveDevice(device);
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
	 * 修改设备之前
	 */
	@RequestMapping("/toUpdateDevice")
	@ResponseBody
	public BaseResponseData toUpdateDevice(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			Device device = collectSetService.getDeviceById(id);
			List<DeviceType> deviceTypeList = collectSetService.listDeviceType();
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("device", device);
			resData.put("deviceTypeList", deviceTypeList);
			data.setResponseData(resData);
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
	 * 修改设备
	 */
	@RequestMapping("/updateDevice")
	@ResponseBody
	public BaseResponseData updateDevice(@RequestParam("id") int id, @RequestParam("device_type_id") int device_type_id,
			@RequestParam("name") String name) {
		BaseResponseData data = new BaseResponseData();
		try {
			Device device = deviceService.selectDeviceById(id);
			device.setDevice_type_id(device_type_id);
			device.setName(name);
			deviceService.updateDevice(device);
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
	 * 删除设备
	 */
	@RequestMapping("/deleteDevice")
	@ResponseBody
	public BaseResponseData deleteDevice(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			deviceService.deleteDevice(id);
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
	 * 设备通讯列表
	 */
	@RequestMapping("/listDeviceInfo")
	@ResponseBody
	public BaseResponseData listDeviceInfo() {
		BaseResponseData data = new BaseResponseData();
		try {
			List<DeviceInfo> deviceInfoList = collectSetService.listDeviceInfo();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceInfoList", deviceInfoList);
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

	/*
	 * 添加设备和通讯
	 */
	@RequestMapping("/insertDeviceMangInfo")
	@ResponseBody
	public BaseResponseData insertDeviceMangInfo(@RequestParam("xtype") int device_id,
			@RequestParam("typeName") String typeName, @RequestParam("armIp") String armIp,
			@RequestParam("armAddr") int armAddr, @RequestParam("wdspIp") String wdspIp,
			@RequestParam("wdspAddr") int wdspAddr, @RequestParam("jdspIp") String jdspIp,
			@RequestParam("jdspAddr") int jdspAddr) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 存设备表
			Device device = new Device();
			device.setDevice_type_id(device_id);
			device.setName(typeName);
			int deviceId = deviceService.saveDevice(device);
			// 存如设备通讯表
			// arm
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDevice_id(deviceId);
			deviceInfo.setD_type("arm");
			deviceInfo.setIp(armIp);
			deviceInfo.setPort(armAddr);
			deviceInfo.setCreate_time(new Date());
			collectSetService.saveDeviceInfo(deviceInfo);

			// dsp网测
			DeviceInfo deviceInfoJ = new DeviceInfo();
			deviceInfoJ.setDevice_id(deviceId);
			deviceInfoJ.setD_type("dsp");
			deviceInfoJ.setDsp_type(1);
			deviceInfoJ.setIp(wdspIp);
			deviceInfoJ.setPort(wdspAddr);
			deviceInfoJ.setCreate_time(new Date());
			collectSetService.saveDeviceInfo(deviceInfoJ);

			// dsp机测
			DeviceInfo deviceInfoW = new DeviceInfo();
			deviceInfoW.setDevice_id(deviceId);
			deviceInfoW.setD_type("dsp");
			deviceInfoW.setDsp_type(2);
			deviceInfoW.setIp(jdspIp);
			deviceInfoW.setPort(jdspAddr);
			deviceInfoW.setCreate_time(new Date());
			collectSetService.saveDeviceInfo(deviceInfoW);

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
	 * 验证ip地址的独一性
	 */
	@RequestMapping("/checkIp")
	@ResponseBody
	public BaseResponseData checkIp(@RequestParam("armIp") String armIp, @RequestParam("wdspIp") String wdspIp,
			@RequestParam("jdspIp") String jdspIp) {
		BaseResponseData data = new BaseResponseData();
		try {
			// arm ip
			DeviceInfo dinfo = new DeviceInfo();
			dinfo.setIp(armIp);
			List<DeviceInfo> adeviceInfoList = collectSetService.findByIp(dinfo);
			// 网侧dsp
			DeviceInfo wdinfo = new DeviceInfo();
			wdinfo.setIp(wdspIp);
			List<DeviceInfo> wdeviceInfoList = collectSetService.findByIp(wdinfo);
			// 机侧dsp
			DeviceInfo jdinfo = new DeviceInfo();
			jdinfo.setIp(jdspIp);
			List<DeviceInfo> jdeviceInfoList = collectSetService.findByIp(jdinfo);

			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("adeviceInfoList", adeviceInfoList.size());
			resData.put("wdeviceInfoList", wdeviceInfoList.size());
			resData.put("jdeviceInfoList", jdeviceInfoList.size());

			data.setResponseData(resData);
			data.setMessage("查询成功");
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
	 * 修改设备和通讯之前
	 */
	@RequestMapping("/updateDeviceInfoMang")
	@ResponseBody
	public BaseResponseData updateDeviceInfoMang(@RequestParam("id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<DeviceType> deviceTypeList = collectSetService.listDeviceType();
			List<DeviceInfo> deviceInfoList = collectSetService.getDeviceInfoByDevice_id(device_id);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceInfoList", deviceInfoList);
			resData.put("deviceTypeList", deviceTypeList);
			data.setResponseData(resData);
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
	 * 修改设备和通讯检查ip的唯一性
	 */
	@RequestMapping("/editCheckIp")
	@ResponseBody
	public BaseResponseData editCheckIp(@RequestParam("id") int id, @RequestParam("armIp") String armIp,
			@RequestParam("wdspIp") String wdspIp, @RequestParam("jdspIp") String jdspIp) {
		BaseResponseData data = new BaseResponseData();
		try {
			String checkIp = "";
			List<DeviceInfo> deviceInfoAll = collectSetService.getDeviceInfoByDevice_id(id);
			DeviceInfo deviceInfo = new DeviceInfo();
			for (int i = 0; i < deviceInfoAll.size(); i++) {
				if (i == 0) {
					deviceInfo.setId(deviceInfoAll.get(i).getId());
					deviceInfo.setIp(armIp);
				} else if (i == 1) {
					deviceInfo.setId(deviceInfoAll.get(i).getId());
					deviceInfo.setIp(wdspIp);
				} else if (i == 2) {
					deviceInfo.setId(deviceInfoAll.get(i).getId());
					deviceInfo.setIp(jdspIp);
				}
				List<DeviceInfo> deviceInfoList = collectSetService.findByIdAndIp(deviceInfo);
				DeviceInfo dinfo = new DeviceInfo();
				if (deviceInfoList.size() == 0) { // 新ip地址
					if (i == 0) {
						dinfo.setIp(armIp);
						List<DeviceInfo> adeviceInfoList = collectSetService.findByIp(dinfo);
						if (adeviceInfoList.size() > 0) { // 有相同ip地址，提示
							checkIp = "armIp地址已存在！";
						}

					} else if (i == 1) {
						dinfo.setIp(wdspIp);
						List<DeviceInfo> adeviceInfoList = collectSetService.findByIp(dinfo);
						if (adeviceInfoList.size() > 0) { // 有相同ip地址，提示
							checkIp = "网侧dspIp地址已存在！";
						}

					} else if (i == 2) {
						dinfo.setIp(jdspIp);
						List<DeviceInfo> adeviceInfoList = collectSetService.findByIp(dinfo);
						if (adeviceInfoList.size() > 0) { // 有相同ip地址，提示
							checkIp = "机侧dspIp地址已存在！";
						}
					}
				}
			}
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("checkIp", checkIp);
			data.setResponseData(resData);
			data.setMessage("更新成功");
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
	 * 修改设备和通讯之后
	 */
	@RequestMapping("/editDeviceMangInfo")
	@ResponseBody
	public BaseResponseData editDeviceMangInfo(@RequestParam("id") int id, @RequestParam("xtype") int device_id,
			@RequestParam("typeName") String typeName, @RequestParam("armIp") String armIp,
			@RequestParam("armAddr") int armAddr, @RequestParam("wdspIp") String wdspIp,
			@RequestParam("wdspAddr") int wdspAddr, @RequestParam("jdspIp") String jdspIp,
			@RequestParam("jdspAddr") int jdspAddr) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 修改设备表
			Device device = deviceService.selectDeviceById(id);
			device.setDevice_type_id(device_id);
			device.setName(typeName);
			deviceService.updateDevice(device);
			// 修改设备通讯表

			List<DeviceInfo> deviceInfo = collectSetService.getDeviceInfoByDevice_id(device.getId());
			for (int i = 0; i < deviceInfo.size(); i++) {
				// 1.修改arm
				if (deviceInfo.get(i).getDsp_type() == 0) {
					DeviceInfo deviceInfoedit = collectSetService.getDeviceInfo(deviceInfo.get(i).getId());
					deviceInfoedit.setIp(armIp);
					deviceInfoedit.setPort(armAddr);
					collectSetService.updateDeviceInfo(deviceInfoedit);
				}
				// 2.修改dsp网测
				if (deviceInfo.get(i).getDsp_type() == 1) {
					DeviceInfo deviceInfoedit = collectSetService.getDeviceInfo(deviceInfo.get(i).getId());
					deviceInfoedit.setIp(wdspIp);
					deviceInfoedit.setPort(wdspAddr);
					collectSetService.updateDeviceInfo(deviceInfoedit);
				}
				// 3.修改dsp机测
				if (deviceInfo.get(i).getDsp_type() == 2) {
					DeviceInfo deviceInfoedit = collectSetService.getDeviceInfo(deviceInfo.get(i).getId());
					deviceInfoedit.setIp(jdspIp);
					deviceInfoedit.setPort(jdspAddr);
					collectSetService.updateDeviceInfo(deviceInfoedit);
				}
			}
			data.setMessage("更新成功");
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
	 * 删除设备和通讯
	 */
	@RequestMapping("/deleteDeviceInfoMang")
	@ResponseBody
	public BaseResponseData deleteDeviceInfoMang(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 删除设备通讯表
			Device device = deviceService.selectDeviceById(id);
			List<DeviceInfo> deviceInfoList = collectSetService.getDeviceInfoByDevice_id(device.getId());
			for (int i = 0; i < deviceInfoList.size(); i++) {
				collectSetService.deleteDeviceInfo(deviceInfoList.get(i).getId());
			}
			// 删除设备表
			deviceService.deleteDevice(id);
			data.setMessage("删除成功");
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
	 * 添加通讯
	 */
	@RequestMapping("/insertDeviceInfo")
	@ResponseBody
	public BaseResponseData insertDeviceInfo(@RequestParam("device_id") int device_id,
			@RequestParam("d_type") String d_type, @RequestParam("ip") String ip, @RequestParam("port") int port) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceInfo deviceInfo = new DeviceInfo();
			deviceInfo.setDevice_id(device_id);
			deviceInfo.setD_type(d_type);
			deviceInfo.setIp(ip);
			deviceInfo.setPort(port);
			deviceInfo.setCreate_time(new Date());
			collectSetService.saveDeviceInfo(deviceInfo);
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
	 * 修改通讯之前
	 */
	@RequestMapping("/toUpdateDeviceInfo")
	@ResponseBody
	public BaseResponseData toUpdateDeviceInfo(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceInfo deviceInfo = collectSetService.getDeviceInfo(id);
			List<Device> deviceList = deviceService.listDevice();
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("deviceInfo", deviceInfo);
			resData.put("deviceList", deviceList);
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
	 * 修改通讯
	 */
	@RequestMapping("/updateDeviceInfo")
	@ResponseBody
	public BaseResponseData updateDeviceInfo(@RequestParam("id") int id, @RequestParam("device_id") int device_id,
			@RequestParam("d_type") String d_type, @RequestParam("ip") String ip, @RequestParam("port") int port) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceInfo deviceInfo = collectSetService.getDeviceInfo(id);
			deviceInfo.setDevice_id(device_id);
			deviceInfo.setD_type(d_type);
			deviceInfo.setIp(ip);
			deviceInfo.setPort(port);
			deviceInfo.setUpdate_time(new Date());
			collectSetService.updateDeviceInfo(deviceInfo);
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
	 * 删除通讯
	 */
	@RequestMapping("/deleteDeviceInfo")
	@ResponseBody
	public BaseResponseData deleteDeviceInfo(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			collectSetService.deleteDeviceInfo(id);
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

}
