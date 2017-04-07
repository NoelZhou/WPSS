package com.cn.hy.controller.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.modbustcp.ModBusErrorCode;
import com.cn.hy.pojo.modbustcp.ModbusSkApp;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.serviceset.UserEmail;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.system.DeviceBaseData;
import com.cn.hy.service.deviceSystemService.DeviceSystemService;
import com.cn.hy.service.modbustcp.ModBusTcpService;
import com.cn.hy.service.modbustcp.ModbusSkAppService;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.serviceset.ErrorWarnSetService;
import com.cn.hy.service.system.DataCqService;
import com.cn.hy.service.system.DeviceBDService;
import com.cn.hy.service.system.DeviceService;

@Controller
@RequestMapping("/DataCq")
public class DataCqController {
	@Resource
	private DataCqService dataCqService;
	@Resource
	private DeviceService deviceService;
	@Resource
	private DeviceBDService deviceBDService;
	@Resource
	private ModbusSkAppService modbusSkAppService;
	@Resource
	private ModBusTcpService modBusTcpService;
	@Resource
	private CollectSetService collectSetService;
	@Resource
	private DeviceSystemService devicesystemsservice;
	@Resource
	private ErrorWarnSetService errorWarnSetService;

	/**
	 * 获取所有设备风机最新实时数据列表，及风机信息
	 * 
	 * @return
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData goDataCqList() {
		BaseResponseData data = new BaseResponseData();
		try {
			// 获取所有设备风机信息数组 故障风机
			List<DataCq> varList = dataCqService.selectErrorDataCq();

			List<UserEmail> UsereamilList = errorWarnSetService.listUserEmail();
			List<DataCq> errorvarList = new ArrayList<DataCq>();
			if (UsereamilList.size() == 0) {
				// 查询无故障风机
				errorvarList = dataCqService.selectDataCqEmail();
			} else {
				// 查询无故障风机
				errorvarList = dataCqService.selectDataCq();
			}
			List<DataCq> startList = dataCqService.selectDataCqStart();
			// 为了吧故障风机放在前面。
			varList.addAll(errorvarList);
			varList.addAll(startList);
			int showtype = 1;
			// 获取基本参数协议地址
			List<DeviceBaseData> deviceBDList = deviceBDService.selectDeviceBaseData(showtype);
			int modbus_type = 0;
			String type_name = "";
			ModbusSkApp msa = new ModbusSkApp();
			// 故障风机数
			int errornum = 0;
			// 待机风机数
			int waitnum = 0;
			// 并网风机数
			int bwnum = 0;
			// 告警风机数
			int alarm = 0;
			for (int i = 0; i < varList.size(); i++) {
				// 获得设备协议类型(0,1,2)
				modbus_type = varList.get(i).getModbus_type();
				if (modbus_type == 0) {
					type_name = "双馈";
				} else if (modbus_type == 1) {
					type_name = "全功率";
				} else if (modbus_type == 2) {
					type_name = "海上风电";
				}
				varList.get(i).setName(type_name + "_" + varList.get(i).getName());

				String dataStr = varList.get(i).getData();

				List<String> deviceAll = new ArrayList<String>();
				// 有采集数据
				if (dataStr != null) {
					String[] dataArray = dataStr.split(",");
					List<String> strList = new ArrayList<String>();
					// 获取风场预览显示参数
					for (int j = 0; j < deviceBDList.size(); j++) {
						if (deviceBDList.get(j).getModbustype() == modbus_type) {
							// 协议地址
							int addr = deviceBDList.get(j).getParamter_id();
							// 获取modbustcp_sk_app中指定参数地址信息
							msa = modbusSkAppService.getModbusSkApp(addr, modbus_type);
							// 地址值
							if (null != msa) {
								if (dataStr != null && dataStr != "" && dataStr != "-1") {
									int addrValue = 0;
									if (modbus_type == 2) {
										addrValue = Integer.parseInt(dataArray[addr - 12788]);
									} else {
										addrValue = Integer.parseInt(dataArray[addr]);
									}
									// 协议解析方法
									strList = modBusTcpService.parseModBusTcp(addr, addrValue, modbus_type, dataStr);
									String unit = msa.getUnit() == null ? "" : msa.getUnit();
									// map.put(msa.getName(),
									// strList.get(1)+""+unit);
									deviceAll.add(msa.getName() + "$" + strList.get(1) + "" + unit);
								} else {
									// map.put(msa.getName(), "");
									deviceAll.add(msa.getName() + "$ ");
								}
							}
						}
					}
					varList.get(i).setDeviceAll(deviceAll);
					// 判断风机运行状态
					// 双馈变流器
					if (dataStr != null && dataStr != "") {
						if (modbus_type == 0) {
							// 并网风机 条件（20.2==1 & 20.4==1）
							// 协议解析
							List<String> bwList = modBusTcpService.parseModBusTcp(20, Integer.parseInt(dataArray[20]),
									modbus_type, dataStr);
							String bwStr = bwList.get(1);
							String bwStrVal1 = "";
							String bwStrVal2 = "";
							if (bwStr.split("[|]").length > 0 && bwStr.contains("|")) {
								bwStrVal1 = bwStr.split("[|]")[2].split(",")[2];
								bwStrVal2 = bwStr.split("[|]")[4].split(",")[2];
								if ("1".equals(bwStrVal1) && "1".equals(bwStrVal2)) {
									bwnum += 1;
									varList.get(i).setRunstate("bw");
								}
							}
							// 故障风机 条件（38!=0）
							// 告警风机条件（34>0）
							String alarmStr = dataArray[34];
							if (!"0".equals(alarmStr)) {
								alarm += 1;
								varList.get(i).setRunstate("alarm");
							}
							String errorStr = dataArray[38];
							if (!"0".equals(errorStr)) {
								errornum += 1;
								varList.get(i).setRunstate("error");
							}
							// 待机风机 条件（35.3==1 & 20.2!=1 & 20.4!=1 & 38==0）
							List<String> waitList = modBusTcpService.parseModBusTcp(35, Integer.parseInt(dataArray[35]),
									modbus_type, dataStr);
							String waitStr = waitList.get(1);
							if (waitStr.split("[|]").length > 0 && bwStr.contains("|")) {
								String waitStrVal = waitStr.split("[|]")[3].split(",")[2];
								if ("1".equals(waitStrVal) && !"1".equals(bwStrVal1) && !"1".equals(bwStrVal2)
										&& "0".equals(errorStr)) {
									waitnum += 1;
									varList.get(i).setRunstate("wait");
								}
							}
						} else if (modbus_type == 1) { // 全功率变流器
							// 并网风机 条件（20.2==1 & 20.4==1）
							List<String> bwList = modBusTcpService.parseModBusTcp(20, Integer.parseInt(dataArray[20]),
									modbus_type, dataStr);
							String bwStr = bwList.get(1);
							String bwStrVal1 = "";
							String bwStrVal2 = "";
							if (bwStr.split("[|]").length > 0 && bwStr.contains("|")) {
								bwStrVal1 = bwStr.split("[|]")[2].split(",")[2];
								bwStrVal2 = bwStr.split("[|]")[4].split(",")[2];
								if ("1".equals(bwStrVal1) && "1".equals(bwStrVal2)) {
									bwnum += 1;
									varList.get(i).setRunstate("bw");
								}
							}

							// 告警风机条件（51>0）
							String alarmStr = dataArray[51];
							if (!"0".equals(alarmStr)) {
								alarm += 1;
								varList.get(i).setRunstate("alarm");
							}

							// 故障风机 条件（38!=0）
							String errorStr = dataArray[38];
							if (!"0".equals(errorStr)) {
								errornum += 1;
								varList.get(i).setRunstate("error");
							}
							// 待机风机 条件（35.3==1 & 20.4!=1 & 38==0）
							List<String> waitList = modBusTcpService.parseModBusTcp(35, Integer.parseInt(dataArray[35]),
									modbus_type, dataStr);
							String waitStr = waitList.get(1);
							if (waitStr.split("[|]").length > 0 && bwStr.contains("|")) {
								String waitStrVal = waitStr.split("[|]")[3].split(",")[2];
								if ("1".equals(waitStrVal) && !"1".equals(bwStrVal2) && "0".equals(errorStr)) {
									waitnum += 1;
									varList.get(i).setRunstate("wait");
								}
							}
						} else if (modbus_type == 2) {// 海上风电变流器
							// 并网风机 条件（12823.3==1 & 12823.4==1）
							List<String> bwList = modBusTcpService.parseModBusTcp(12822,
									Integer.parseInt(dataArray[12822 - 12788]), modbus_type, dataStr);
							String bwStr = bwList.get(1);
							String bwStrVal1 = "";
							String bwStrVal2 = "";
							if (bwStr.split("[|]").length > 0 && bwStr.contains("|")) {
								bwStrVal1 = bwStr.split("[|]")[3].split(",")[2];
								bwStrVal2 = bwStr.split("[|]")[4].split(",")[2];
								if ("1".equals(bwStrVal1) && "1".equals(bwStrVal2)) {
									bwnum += 1;
									varList.get(i).setRunstate("bw");
								}
							}

							// 告警风机条件（12824>0）
							String alarmStr = dataArray[12824 - 12788];
							if (!"0".equals(alarmStr)) {
								alarm += 1;
								varList.get(i).setRunstate("alarm");
							}
							// 故障风机 条件（12837!=0）
							// List<String> errorList =
							// modBusTcpService.parseModBusTcp(12837,Integer.parseInt(dataArray[12837-12788]),modbus_type,dataStr);
							String errorStr = dataArray[12836 - 12788];
							if (!"0".equals(errorStr)) {
								errornum += 1;
								varList.get(i).setRunstate("error");
							}
							// 待机风机 条件（12823.10==1 &12823.4!=1 & 12837==0）
							if (bwStr.split("[|]").length > 0 && bwStr.contains("|")) {
								String waitStrVal1 = bwStr.split("[|]")[10].split(",")[2];
								String waitStrVal2 = bwStr.split("[|]")[4].split(",")[2];
								if ("1".equals(waitStrVal1) && !"1".equals(waitStrVal2) && "0".equals(errorStr)) {
									waitnum += 1;
									varList.get(i).setRunstate("wait");
								}
							}

						}
					}
				} else {
					for (int j = 0; j < deviceBDList.size(); j++) {
						if (deviceBDList.get(j).getModbustype() == modbus_type) {
							int addr = deviceBDList.get(j).getParamter_id();
							// 获取modbustcp_sk_app中指定参数地址信息
							msa = modbusSkAppService.getModbusSkApp(addr, modbus_type);
							String unit = msa.getUnit() == null ? "" : msa.getUnit();
							// map.put(msa.getName(), 0+""+unit);
							deviceAll.add(msa.getName() + "$" + 0 + "" + unit);
						}
					}
					varList.get(i).setDeviceAll(deviceAll);
					varList.get(i).setRunstate("networkerror");
				}

				for (int num = 0; num < startList.size(); num++) {
					int deviceId = startList.get(num).getDevice_id();
					if (varList.get(i).getDevice_id() == deviceId) {
						deviceAll.clear();
						for (int j = 0; j < deviceBDList.size(); j++) {
							if (deviceBDList.get(j).getModbustype() == modbus_type) {
								int addr = deviceBDList.get(j).getParamter_id();
								// 获取modbustcp_sk_app中指定参数地址信息
								msa = modbusSkAppService.getModbusSkApp(addr, modbus_type);
								String unit = msa.getUnit() == null ? "" : msa.getUnit();
								// map.put(msa.getName(), 0+""+unit);
								deviceAll.add(msa.getName() + "$" + 0 + "" + unit);
							}
						}
						varList.get(i).setDeviceAll(deviceAll);
						varList.get(i).setRunstate("start");
					}
				}

			}
			// System.out.println(" 每个风机的运行状态使用时间："+ (System.currentTimeMillis()
			// - a) / 1000f + " 秒 ");
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("varList", varList);
			resData.put("errornum", errornum);
			resData.put("waitnum", waitnum);
			resData.put("bwnum", bwnum);
			resData.put("alarm", alarm);
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
	 * 获取选中设备的数据信息
	 * 
	 * @param device_id
	 * @param id
	 * @return
	 */
	@RequestMapping("/getThisDevice")
	@ResponseBody
	public BaseResponseData getThisDevice(@RequestParam("device_id") int device_id, @RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			DataCq dataCq = dataCqService.getDataCq(id);
			int showtype = 0;
			// 获取风场预览需要显示的参数地址
			List<DeviceBaseData> deviceBDList = deviceBDService.selectDeviceBaseData(showtype);
			Map<String, String> errormap = new HashMap<String, String>();
			ModbusSkApp msa = new ModbusSkApp();
			List<String> strList = new ArrayList<String>();
			// Map<String,String> map = new HashMap<String,String>();
			List<String> deviceAll = new ArrayList<String>();
			if (dataCq != null) {
				// 根据设备id获取数据
				Device device = deviceService.selectDeviceById(dataCq.getDevice_id());
				int device_type = device.getDevice_type_id();
				// 通过id获取设备类型
				DeviceType deviceType = collectSetService.getDeviceTypeById(device_type);
				int modbus_type = deviceType.getModbus_type();
				String dataStr = dataCq.getData();
				if (dataStr != null) {
					String[] dataArray = dataStr.split(",");

					for (int j = 0; j < deviceBDList.size(); j++) {
						if (deviceBDList.get(j).getModbustype() == modbus_type) {
							// 协议地址
							int addr = deviceBDList.get(j).getParamter_id();
							// 获取modbustcp_sk_app中指定参数地址信息
							msa = modbusSkAppService.getModbusSkApp(addr, modbus_type);
							if (null != msa) {
								// 属性 值 单位 （值 单位）前台处理
								String unit = msa.getUnit() == null ? "" : msa.getUnit();
								if (dataStr != null && dataStr != "") {
									// 地址值
									int addrValue = 0;
									if (modbus_type == 2) {
										addrValue = Integer.parseInt(dataArray[addr - 12788]);
									} else {
										addrValue = Integer.parseInt(dataArray[addr]);
									}
									// 协议解析方法
									strList = modBusTcpService.parseModBusTcp(addr, addrValue, modbus_type, dataStr);
									// map.put(msa.getName(),
									// strList.get(1)+","+unit);
									deviceAll.add(msa.getName() + "$" + strList.get(1) + "$" + unit);
								} else {
									// map.put(msa.getName(), " ,"+unit);
									deviceAll.add(msa.getName() + "$" + " $" + unit);
								}
							}
						}
					}
					// dataCq.setMap(map);
					// 故障告警
					// Map<String,String> errormap = new
					// HashMap<String,String>();
					List<ModBusErrorCode> errorcodelist = devicesystemsservice.geterrorcode(modbus_type);
					for (int i = 0; i < errorcodelist.size(); i++) {
						ModBusErrorCode errorcode = errorcodelist.get(i);
						String errorvalue = "0";
						int codeAddr = errorcode.getAddr();
						if (dataCq != null && dataCq != null) {
							if (dataStr != null && dataStr != "") {
								if (codeAddr >= 12788) {
									errorvalue = dataArray[codeAddr - 12788];
								} else {
									errorvalue = dataArray[codeAddr];
								}
							}
						}
						if (!"0".equals(errorvalue)) {
							Boolean flag = true;
							// 获取具体故障地址的Bit位信息
							String errorbitstr = devicesystemsservice.geterrorcodebyaddr(codeAddr, modbus_type);
							// 所有bit位都查看的故障地址
							List<String> warnList = new ArrayList<String>();
							warnList = modBusTcpService.parseModBusTcp(codeAddr, Integer.parseInt(errorvalue),
									modbus_type, dataStr);
							if (errorbitstr != null && !errorbitstr.equals("")) {
								flag = false;
								String warnStr = "";
								String warnstate = "";
								warnStr = warnList.get(1);
								String[] bitArry = errorbitstr.split(",");
								if (warnStr.split("[|]").length > 0 && warnStr.contains("|")) {
									for (int j = 0; j < bitArry.length; j++) {
										warnstate = warnStr.split("[|]")[Integer.parseInt(bitArry[j])].split(",")[2];
										if ("1".equals(warnstate)) {
											flag = true;
											break;
										}
									}
								}
							} else {
								flag = false;
								String[] bitvlaue = warnList.get(1).split("[|]");
								if (bitvlaue.length > 1) {
									for (int j = 0; j < bitvlaue.length; j++) {
										String onebit = bitvlaue[j].split(",")[2];
										if (onebit.equals("1")) {
											flag = true;
											break;
										}

									}
								}
							}
							if (flag) {
								String bstr = getHex4(Integer.toHexString(Integer.parseInt(errorvalue)).toUpperCase())
										+ "H";
								msa = modbusSkAppService.getModbusSkApp(codeAddr, modbus_type);
								// 状态码，地址 bstr
								// errormap.put(bstr+","+errorcode.getAddr(),
								// msa.getName()+","+msa.getCategory());
								errormap.put(bstr + "," + errorcode.getAddr(), errorcode.getErrorname());
							}
						}
					}
				}
			} else {
				dataCq = new DataCq();
				Device device = deviceService.selectDeviceById(device_id);
				int device_type = device.getDevice_type_id();
				DeviceType deviceType = collectSetService.getDeviceTypeById(device_type);
				int modbus_type = deviceType.getModbus_type();
				for (int j = 0; j < deviceBDList.size(); j++) {
					if (deviceBDList.get(j).getModbustype() == modbus_type) {
						// 协议地址
						int addr = deviceBDList.get(j).getParamter_id();
						msa = modbusSkAppService.getModbusSkApp(addr, modbus_type);
						if (null != msa) {
							String unit = msa.getUnit() == null ? "" : msa.getUnit();
							// map.put(msa.getName(), "0,"+unit);
							deviceAll.add(msa.getName() + "$" + "0$" + unit);
						}
					}
				}
				// dataCq.setMap(map);
			}

			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			// resData.put("dataCq", dataCq);
			resData.put("deviceAll", deviceAll);
			resData.put("errormap", errormap);
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
	 * 查看故障信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/getErrorInfo")
	@ResponseBody
	public BaseResponseData getErrorInfo(@RequestParam("id") int id) {
		BaseResponseData data = new BaseResponseData();
		try {
			Map<String, String> errormap = new HashMap<String, String>();
			DataCq dataCq = dataCqService.getDataCq(id);
			Device device = deviceService.selectDeviceById(dataCq.getDevice_id());
			int device_type = device.getDevice_type_id();
			DeviceType deviceType = collectSetService.getDeviceTypeById(device_type);
			int modbus_type = deviceType.getModbus_type();
			String dataStr = dataCq.getData();
			String[] dataArray = dataStr.split(",");
			// 故障告警
			if (dataStr != null && dataStr != "") {
				List<String> errorList = new ArrayList<String>();
				String errorStr = "";
				String errorname = "";
				String errorstate = "";
				List<ModBusErrorCode> errorcodelist = devicesystemsservice.geterrorcode(modbus_type);
				for (int i = 0; i < errorcodelist.size(); i++) {
					ModBusErrorCode errorcode = errorcodelist.get(i);
					String errorvalue = "0";
					int codeAddr = errorcode.getAddr();
					if (dataCq != null && dataCq != null) {
						if (dataStr != null && dataStr != "") {
							if (codeAddr >= 12788) {
								errorvalue = dataArray[codeAddr - 12788];
							} else {
								errorvalue = dataArray[codeAddr];
							}
						}
					}
					errorList = modBusTcpService.parseModBusTcp(codeAddr, Integer.parseInt(errorvalue), modbus_type,
							dataStr);
					errorStr = errorList.get(1);
					if (errorStr.split("[|]").length > 0 && errorStr.contains("|")) {
						// 获取具体故障地址的Bit位信息
						String errorbitstr = devicesystemsservice.geterrorcodebyaddr(codeAddr, modbus_type);
						if (errorbitstr != null && !errorbitstr.equals("")) {
							String[] bitArry = errorbitstr.split(",");
							for (int j = 0; j < bitArry.length; j++) {
								errorname = errorStr.split("[|]")[Integer.parseInt(bitArry[j])].split(",")[1];
								errorstate = errorStr.split("[|]")[Integer.parseInt(bitArry[j])].split(",")[2];
								if ("1".equals(errorstate)) {
									errormap.put(errorname, errorstate);
								}
							}
						} else {
							for (int j = 0; j < errorStr.split("[|]").length; j++) {
								errorname = errorStr.split("[|]")[j].split(",")[1];
								errorstate = errorStr.split("[|]")[j].split(",")[2];
								if ("1".equals(errorstate)) {
									errormap.put(errorname, errorstate);
								}
							}
						}
					}
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errormap", errormap);
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
	 * 查看故障码详情
	 * 
	 * @param id
	 * @param addr
	 * @return
	 */
	@RequestMapping("/viewErrorCode")
	@ResponseBody
	public BaseResponseData viewErrorCode(@RequestParam("id") int id, @RequestParam("addr") int addr) {
		BaseResponseData data = new BaseResponseData();
		try {
			DataCq dataCq = dataCqService.getDataCq(id);
			Device device = deviceService.selectDeviceById(dataCq.getDevice_id());
			int device_type = device.getDevice_type_id();
			DeviceType deviceType = collectSetService.getDeviceTypeById(device_type);
			int modbus_type = deviceType.getModbus_type();
			String dataStr = dataCq.getData();
			String[] dataArray = dataStr.split(",");
			Map<String, String> errormap = new HashMap<String, String>();
			String errorStr = "";
			String errorname = "";
			String errorstate = "";
			List<String> errorList = new ArrayList<String>();
			int addrValue = 0;
			if (modbus_type == 2) {
				addrValue = Integer.parseInt(dataArray[addr - 12788]);
			} else {
				addrValue = Integer.parseInt(dataArray[addr]);
			}

			errorList = modBusTcpService.parseModBusTcp(addr, addrValue, modbus_type, dataStr);
			errorStr = errorList.get(1);
			if (errorStr.split("[|]").length > 0 && errorStr.contains("|")) {
				String errorbitstr = devicesystemsservice.geterrorcodebyaddr(addr, modbus_type);
				if (errorbitstr != null && !errorbitstr.equals("")) {
					String[] bitArry = errorbitstr.split(",");
					for (int i = 0; i < bitArry.length; i++) {
						errorname = errorStr.split("[|]")[Integer.parseInt(bitArry[i])].split(",")[1];
						errorstate = errorStr.split("[|]")[Integer.parseInt(bitArry[i])].split(",")[2];
						if ("1".equals(errorstate)) {
							errormap.put(errorname, errorstate);
						}
					}
				} else {
					for (int i = 0; i < errorStr.split("[|]").length; i++) {
						errorname = errorStr.split("[|]")[i].split(",")[1];
						errorstate = errorStr.split("[|]")[i].split(",")[2];
						if ("1".equals(errorstate)) {
							errormap.put(errorname, errorstate);
						}
					}
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errormap", errormap);
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

	private String getHex4(String upperCase) {
		String hx = "";
		if (upperCase.length() > 4) {
			hx = upperCase.substring(4, upperCase.length());
		} else {
			hx = upperCase;
		}
		return hx;
	}

}
