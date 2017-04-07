package com.cn.hy.controller.DevciceDetail;

import java.util.ArrayList;
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
import com.cn.hy.bean.SunModbusTcpbyIp;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.modbustcp.DeviceControlParme;
import com.cn.hy.pojo.modbustcp.ModBusVar;
import com.cn.hy.pojo.serviceset.DeviceInfo;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.service.deviceSystemService.DeviceSystemTestSerivce;
import com.cn.hy.service.modbustcp.ModBusTcpService;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DataCqService;
import com.cn.hy.service.system.DeviceService;
import com.cn.hy.service.system.UserService;

@Controller
@RequestMapping("/DeviceTest")
public class DeviceSystemTest {
	@Resource
	private DeviceSystemTestSerivce devicesystemtestservice;
	@Resource
	private DataCqService dataCqService;
	@Resource
	private DeviceService deviceService;
	@Resource
	private CollectSetService collectSetService;
	@Resource
	private ModBusTcpService modBusTcpService;
	@Resource
	private UserService userService;

	public int totcal_device_id = 0;

	/**
	 * 获取控制模式模块的数据
	 * 
	 * @param device_id
	 * @param request
	 * @return
	 */
	@RequestMapping("/getControlModel")
	@ResponseBody
	public BaseResponseData getControlModelTest(@RequestParam("device_id") int device_id, HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<String> ControlModel = new ArrayList<String>();
			DeviceType deviceType = new DeviceType();
			int modbustcp_type = 0;
			// 获取单个风机最新采集数据
			DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			// 海上风电测试数据

			/* String str="1"; for(int j=10000;j<15000;j++){ str+=","+j; } */

			// 获取控制模块知道的值
			List<DeviceControlParme> Cplist = devicesystemtestservice.getControlModelTestList(modbustcp_type, 1);
			for (int i = 0; i < Cplist.size(); i++) {
				String colnumstr = "";
				DeviceControlParme dp = Cplist.get(i);
				int addr = Integer.parseInt(dp.getBit_id().split("\\.")[0]);
				int bitid = Integer.parseInt(dp.getBit_id().split("\\.")[1]);
				String colnumvalue = "0";
				if (datacq != null && datacq != null) {
					if (datacq.getData() != null && datacq.getData() != "") {
						if (addr >= 12788) {
							colnumvalue = datacq.getData().split(",")[addr - 12788];
						} else {
							colnumvalue = datacq.getData().split(",")[addr];
						}
						// colnumvalue = datacq.getData().split(",")[addr];
						// colnumvalue = str.split(",")[addr];
					}
				}

				// 协议解析方法
				List<String> strList = modBusTcpService.parseModBusTcp(addr, Integer.parseInt(colnumvalue),
						modbustcp_type, datacq.getData());
				String[] bitstr = strList.get(1).split("[|]")[bitid].split(",");
				String bitcolnumvalue = bitstr[2];
				int bitintvalue = Integer.parseInt(bitcolnumvalue);
				if (bitintvalue == 0) {
					String value = "";
					if (modbustcp_type != 2) {
						value = bitstr[3];
					} else {

						if (dp.getName().equals("Net1/选择")) {
							value = "网侧从站/Net1禁止";
						} else if (dp.getName().equals("Net2/选择")) {
							value = "网侧从站/Net2禁止";
						} else if (dp.getName().equals("Rot1/选择")) {
							value = "机侧从站/Net1禁止";
						} else if (dp.getName().equals("Rot2/选择")) {
							value = "机侧从站/Net2禁止";
						} else if (dp.getName().equals("模组1/选择")) {
							value = "第一套模组禁止";
						} else if (dp.getName().equals("模组2/选择")) {
							value = "第二套模组禁止";
						} else if (dp.getName().equals("模组3/选择")) {
							value = "第三套模组禁止";
						} else if (dp.getName().equals("模组4/选择")) {
							value = "第四套模组禁止";
						} else if (dp.getName().equals("跟踪/选择")) {
							value = "保有功";
						} else if (dp.getName().equals("输入/控制")) {
							value = "远程";
						} else if (dp.getName().equals("LVRT/选择")) {
							value = "LVRT激活";
						} else if (dp.getName().equals("调制/选择")) {
							value = "电动模式";
						} else if (dp.getName().equals("功率/曲线")) {
							value = "LVRT禁止";
						} else if (dp.getName().equals("保存/选择")) {
							value = "USB变量保存功能禁止";
						} else if (dp.getName().equals("撬棒模组1")) {
							value = "撬棒模组1禁止";
						} else if (dp.getName().equals("撬棒模组2")) {
							value = "撬棒模组2禁止";
						} else if (dp.getName().equals("撬棒模组3")) {
							value = "撬棒模组3禁止";
						} else if (dp.getName().equals("撬棒模组4")) {
							value = "撬棒模组4禁止";
						}
					}
					colnumstr = dp.getName() + ":" + value + ":" + dp.getCode();

				} else {
					String value = "";
					if (modbustcp_type != 2) {
						value = bitstr[1];
					} else {
						if (dp.getName().equals("Net1/选择")) {
							value = "网侧从站/Net1激活";
						} else if (dp.getName().equals("Net2/选择")) {
							value = "网侧从站/Net2激活";
						} else if (dp.getName().equals("Rot1/选择")) {
							value = "机侧从站/Net1激活";
						} else if (dp.getName().equals("Rot2/选择")) {
							value = "机侧从站/Net2激活";
						} else if (dp.getName().equals("模组1/选择")) {
							value = "第一套模组激活";
						} else if (dp.getName().equals("模组2/选择")) {
							value = "第二套模组激活";
						} else if (dp.getName().equals("模组3/选择")) {
							value = "第三套模组激活";
						} else if (dp.getName().equals("模组4/选择")) {
							value = "第四套模组激活";
						} else if (dp.getName().equals("跟踪/选择")) {
							value = "保无有功";
						} else if (dp.getName().equals("输入/控制")) {
							value = "本地";
						} else if (dp.getName().equals("LVRT/选择")) {
							value = "LVRT激活";
						} else if (dp.getName().equals("调制/选择")) {
							value = "发电模式";
						} else if (dp.getName().equals("功率/曲线")) {
							value = "LVRT激活";
						} else if (dp.getName().equals("保存/选择")) {
							value = "USB变量保存功能激活";
						} else if (dp.getName().equals("撬棒模组1")) {
							value = "撬棒模组1激活";
						} else if (dp.getName().equals("撬棒模组2")) {
							value = "撬棒模组2激活";
						} else if (dp.getName().equals("撬棒模组3")) {
							value = "撬棒模组3激活";
						} else if (dp.getName().equals("撬棒模组4")) {
							value = "撬棒模组4激活";
						}
					}
					colnumstr = dp.getName() + ":" + value + ":" + dp.getCode();
				}
				ControlModel.add(colnumstr);

			}
			// 风场当前模式
			String c_23 = "";
			int msaddr = 23;
			if (modbustcp_type == 2) {
				msaddr = 12809;
			}
			if (datacq != null && datacq != null) {
				if (datacq.getData() != null && datacq.getData() != "") {
					if (msaddr >= 12788) {
						c_23 = datacq.getData().split(",")[msaddr - 12788];
					} else {
						c_23 = datacq.getData().split(",")[msaddr];
					}
				}
			}

			List<ModBusVar> mbv = devicesystemtestservice.getparamevalue(msaddr, modbustcp_type);
			List<String> strList = modBusTcpService.parseModBusTcp(msaddr, Integer.parseInt(c_23), modbustcp_type,
					datacq.getData());
			List<String> hsfdstr = new ArrayList<String>();
			if (modbustcp_type == 2) {
				for (int i = 9; i < 14; i++) {
					hsfdstr.add(strList.get(1).split("[|]")[i]);
				}
			}
			String modelstr = strList.get(1);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("ControlModel", ControlModel);
			resData.put("modelstr", modelstr);
			if (modbustcp_type == 2) {
				resData.put("modellist", hsfdstr);
			} else {
				resData.put("modellist", mbv);
			}
			data.setResponseData(resData);
			return data;
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

	/**
	 * 获取菜单权限
	 */
	@RequestMapping("/selectMenu_writpP")
	@ResponseBody
	public BaseResponseData selectMenu_writpP(HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 获取session中的菜单权限
			HttpSession session = request.getSession();
			String employeeId = (String) session.getAttribute("employeeId");
			Role_menu role_menu = userService.findByUserId(employeeId);
			int write_p = role_menu.getWrite_p();
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("write_p", write_p);
			data.setResponseData(resData);
			return data;
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

	/**
	 * 获取单机调试状态控制的内容
	 * 
	 * @param device_id
	 * @return
	 */
	@RequestMapping("/getControlState")
	@ResponseBody
	public BaseResponseData getControlStateTest(@RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<String> ControlModel = new ArrayList<String>();
			DeviceType deviceType = new DeviceType();
			int modbustcp_type = 0;
			// 获取单个风机最新采集数据
			DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
			// 海上风电测试数据

			// String str="1"; for(int i=0;i<30000;i++){ str+=","+i; }

			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			// 通过id获取设备类型
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			// 获取控制模块知道的值
			List<DeviceControlParme> Cplist = devicesystemtestservice.getControlModelTestList(modbustcp_type, 2);
			for (int i = 0; i < Cplist.size(); i++) {
				String colnumstr = "";
				DeviceControlParme dp = Cplist.get(i);
				if (!dp.getBit_id().equals("不解析")) {
					int addr = Integer.parseInt(dp.getBit_id().split("\\.")[0]);
					int bitid = Integer.parseInt(dp.getBit_id().split("\\.")[1]);
					String colnumvalue = "0";
					if (datacq != null && datacq != null) {
						if (datacq.getData() != null && datacq.getData() != "") {
							// 海上风电测试数据
							// colnumvalue = datacq.getData().split(",")[addr];
							if (addr >= 12788) {
								colnumvalue = datacq.getData().split(",")[addr - 12788];
							} else {
								colnumvalue = datacq.getData().split(",")[addr];
							}
						}
					}
					// 协议解析方法
					List<String> strList = modBusTcpService.parseModBusTcp(addr, Integer.parseInt(colnumvalue),
							modbustcp_type, datacq.getData());
					String bitstr = strList.get(1).split("[|]")[bitid];
					colnumstr = dp.getName() + "," + bitstr.split(",")[2];
				} else {
					colnumstr = dp.getName() + ",0";
				}
				ControlModel.add(colnumstr);

			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("ControlModel", ControlModel);
			data.setResponseData(resData);
			return data;
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

	/**
	 * 基本类型的发送指令操作性。操作完成改变数据中改设备的读写状态
	 * 
	 * @param device_id
	 * @param code
	 * @param addr
	 * @param addr
	 * @return
	 */
	@RequestMapping("/setControlCode")
	@ResponseBody
	public BaseResponseData setControlCodeTest(@RequestParam("device_id") int device_id,
			@RequestParam("code") String code, @RequestParam("addr") String addr,
			@RequestParam(value = "type", required = false, defaultValue = "0") String type) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 根据设备id获取数据
			Device d = deviceService.selectDeviceById(device_id);
			if (d.getRw_role_req().equals("SJ") && d.getRw_role_res().equals("OK")) {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("设备正在升级中.....");
				return data;
			}
			Device dd2 = new Device();
			dd2.setId(device_id);
			dd2.setRw_role_req("W");
			dd2.setRw_role_res("OK");
			dd2.setRw_sql("基本类型发送指令操作,地址为：" + addr + "类型为：" + type + "编号为：" + code);
			deviceService.updateDevice(dd2);
			DeviceType deviceType = new DeviceType();
			int modbustcp_type = 0;
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			// 通过id获取设备类型
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			// 根据目标设备id查找设备arm板的ip信息
			DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(device_id);
			totcal_device_id = device_id;
			// 获取地址的数组类型
			String ids = devicesystemtestservice.getaddrparasewall(Integer.parseInt(addr, 16), modbustcp_type);
			String modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
					modbustcp_type, addr, code, ids);
			// 如果指令失败了循环发送5次成功就退出
			int suc1 = modbusstr.indexOf(",");
			if (suc1 < 0) {
				for (int i = 0; i < 3; i++) {
					int suc = modbusstr.indexOf(",");
					if (!modbusstr.equals("connect") && suc < 0) {
						modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
								modbustcp_type, addr, code, ids);
					}
				}
			}

			if (type.equals("1")) {
				Device dd = new Device();
				dd.setId(device_id);
				dd.setRw_role_req("R");
				dd.setRw_role_res("OK");
				deviceService.updateDevice(dd);
			}
			if (modbusstr.indexOf(",") >= 0) {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("解析成功");
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("获取失败,服务器异常，请稍后重试！");
			}

			return data;
		} catch (Exception e) {
			if (type.equals("1")) {
				Device dd = new Device();
				dd.setId(device_id);
				dd.setRw_role_req("R");
				dd.setRw_role_res("OK");
				deviceService.updateDevice(dd);
			}
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/**
	 * 系统参数中发送指令
	 * 
	 * @param paramestr
	 * @param device_id
	 * @param ids
	 * @return
	 */
	@RequestMapping("/sendcodeall")
	@ResponseBody
	public BaseResponseData sendcodeall(@RequestParam("paramestr") String paramestr,
			@RequestParam("device_id") int device_id, @RequestParam("ids") String ids) {
		BaseResponseData data = new BaseResponseData();
		try {
			System.out.println("参数：" + paramestr);
			System.out.println("设备：" + device_id);
			System.out.println("参数类型：" + ids);
			int modbustcp_type = 0;
			String modbusstr = "";
			// 获取设备基本信息
			DeviceType deviceType = new DeviceType();
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(device_id);
			totcal_device_id = device_id;
			// 网侧EEPROM参数,机侧EEPROM参数
			// 没有确认指令
			if (ids.equals("9") || ids.equals("10") || ids.equals("128")) {
				String addr = "0000";
				String[] p = paramestr.split("[|]");
				for (int i = 0; i < p.length; i++) {
					// 多条发送。0,1,2类型指令 地址0修改指令，1地址编号，2编号值
					String codestr = devicesystemtestservice.getaddrparasewallzh(ids, modbustcp_type).split(",")[0];
					String pstr = codestr + "," + p[i].split(",")[0] + "," + p[i].split(",")[3];
					modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
							modbustcp_type, addr, pstr, ids);

					// 如果指令失败了循环发送5次成功就退出
					// 如果指令失败了循环发送5次成功就退出
					int suc1 = modbusstr.indexOf(",");
					if (suc1 < 0) {
						for (int j = 0; j < 3; j++) {
							int suc = modbusstr.indexOf(",");
							if (!modbusstr.equals("connect") && suc < 0) {
								modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
										modbustcp_type, addr, pstr, ids);
							}
						}
					}
				}

			} else {
				// 一个确认按钮，多个参数
				String[] p = paramestr.split("[|]");
				// 发送多个参数值
				for (int i = 0; i < p.length; i++) {
					String[] coedlist = p[i].split(",");
					// 拿到位10进制地址
					String addr = coedlist[1];
					// 16进制地址
					String hexaddr = SunModbusTcpbyIp.gethex4(Integer.toHexString(Integer.parseInt(addr)));
					String value = coedlist[3];
					// if(){}
					// 写指令，发送参数
					modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
							modbustcp_type, hexaddr, value, ids);
					// 如果指令失败了循环发送5次成功就退出
					int suc1 = modbusstr.indexOf(",");
					if (suc1 < 0) {
						for (int j = 0; j < 3; j++) {
							int suc = modbusstr.indexOf(",");
							if (!modbusstr.equals("connect") && suc < 0) {
								modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
										modbustcp_type, hexaddr, value, ids);
							}
						}
					}
				}
				// 参数指令有返回值
				if (modbusstr.contains(",")) {
					// 一个确定指令
					String codestr = devicesystemtestservice.getaddrparasewallzh(ids, modbustcp_type).split(",")[0];
					if (modbustcp_type == 2) {
						modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
								modbustcp_type, "31f4", codestr, ids);
						// 如果指令失败了循环发送5次成功就退出
						int suc1 = modbusstr.indexOf(",");
						if (suc1 < 0) {
							for (int j = 0; j < 3; j++) {
								int suc = modbusstr.indexOf(",");
								if (!modbusstr.equals("connect") && suc < 0) {
									modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(),
											devinfo.getPort(), modbustcp_type, "31f4", codestr, ids);
								}
							}
						}
					} else {
						modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
								modbustcp_type, "0000", codestr, ids);
						// 如果指令失败了循环发送5次成功就退出
						int suc1 = modbusstr.indexOf(",");
						if (suc1 < 0) {
							for (int j = 0; j < 3; j++) {
								int suc = modbusstr.indexOf(",");
								if (!modbusstr.equals("connect") && suc < 0) {
									modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(),
											devinfo.getPort(), modbustcp_type, "0000", codestr, ids);
								}
							}
						}
					}
				}
			}

			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			if (modbusstr.indexOf(",") >= 0) {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("解析成功");
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("指令发送失败！");
			}

			return data;
		} catch (Exception e) {
			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/**
	 * 查询指令
	 * 
	 * @param device_id
	 * @param ids
	 * @return
	 */
	@RequestMapping("/sendcodeselect")
	@ResponseBody
	public BaseResponseData sendcodeselect(@RequestParam("device_id") int device_id, @RequestParam("ids") String ids) {
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			String modbusstr = "";
			// 获取设备基本信息
			DeviceType deviceType = new DeviceType();
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(device_id);
			totcal_device_id = device_id;
			// 获取参数监控下发指令
			String codestr = devicesystemtestservice.getaddrparasewallzh(ids, modbustcp_type).split(",")[1];
			if (modbustcp_type == 2) {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"31f4", codestr, ids);
				// 如果指令失败了循环发送5次成功就退出
				int suc1 = modbusstr.indexOf(",");
				if (suc1 < 0) {
					for (int j = 0; j < 3; j++) {
						int suc = modbusstr.indexOf(",");
						if (!modbusstr.equals("connect") && suc < 0) {
							modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "31f4", codestr, ids);
						}
					}
				}
			} else {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"0000", codestr, ids);
				// 如果指令失败了循环发送5次成功就退出
				int suc1 = modbusstr.indexOf(",");
				if (suc1 < 0) {
					for (int j = 0; j < 3; j++) {
						int suc = modbusstr.indexOf(",");
						if (!modbusstr.equals("connect") && suc < 0) {
							modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "0000", codestr, ids);
						}
					}
				}
			}

			if (modbusstr.indexOf(",") >= 0) {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("解析成功");
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("查询失败！");
			}
			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			return data;
		} catch (Exception e) {
			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/**
	 * 复位指令
	 * 
	 * @param device_id
	 * @param ids
	 * @return
	 */
	@RequestMapping("/sendcodefw_code")
	@ResponseBody
	public BaseResponseData sendcodefw_code(@RequestParam("device_id") int device_id, @RequestParam("ids") String ids) {
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			String modbusstr = "";
			// 获取设备基本信息
			DeviceType deviceType = new DeviceType();
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(device_id);
			totcal_device_id = device_id;
			String codestr = devicesystemtestservice.getaddrparasewallzh_fw(ids, modbustcp_type);
			if (modbustcp_type == 2) {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"31f4", codestr, ids);
				// 如果指令失败了循环发送5次成功就退出
				int suc1 = modbusstr.indexOf(",");
				if (suc1 < 0) {
					for (int j = 0; j < 3; j++) {
						int suc = modbusstr.indexOf(",");
						if (!modbusstr.equals("connect") && suc < 0) {
							modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "31f4", codestr, ids);
						}
					}
				}
			} else {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"0000", codestr, ids);
				// 如果指令失败了循环发送5次成功就退出
				int suc1 = modbusstr.indexOf(",");
				if (suc1 < 0) {
					for (int j = 0; j < 3; j++) {
						int suc = modbusstr.indexOf(",");
						if (!modbusstr.equals("connect") && suc < 0) {
							modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "0000", codestr, ids);
						}
					}
				}

			}

			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			if (modbusstr.indexOf(",") >= 0) {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("解析成功");
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("获取失败,服务器异常，请稍后重试！");
			}

			return data;
		} catch (Exception e) {
			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/**
	 * 默认参数设置
	 * 
	 * @param device_id
	 * @param ids
	 * @return
	 */
	@RequestMapping("/sendcodemrsz_code")
	@ResponseBody
	public BaseResponseData sendcodemrsz_code(@RequestParam("device_id") int device_id,
			@RequestParam("ids") String ids) {
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			String modbusstr = "";
			// 获取设备基本信息
			DeviceType deviceType = new DeviceType();
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(device_id);
			totcal_device_id = device_id;
			String codestr = devicesystemtestservice.getaddrparasewallzh(ids, modbustcp_type).split(",")[0];
			if (modbustcp_type == 2) {
				/*
				 * modbusstr = wtcp.WriteSunModbusTcpStrAll(devinfo.getIp(),
				 * devinfo.getPort(), modbustcp_type, "31f4", codestr, ids);
				 * //如果指令失败了循环发送5次成功就退出 int suc1=modbusstr.indexOf(",");
				 * if(suc1<0){ for(int j=0;j<3;j++){ int
				 * suc=modbusstr.indexOf(",");
				 * if(!modbusstr.equals("connect")&&suc<0){ modbusstr =
				 * wtcp.WriteSunModbusTcpStrAll(devinfo.getIp(),
				 * devinfo.getPort(), modbustcp_type, "31f4", codestr, ids); } }
				 * }
				 */
			} else {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"0000", codestr, ids);
				// 如果指令失败了循环发送5次成功就退出,不包括连接失败
				int suc1 = modbusstr.indexOf(",");
				if (suc1 < 0) {
					for (int j = 0; j < 3; j++) {
						int suc = modbusstr.indexOf(",");
						if (!modbusstr.equals("connect") && suc < 0) {
							modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "0000", codestr, ids);
						}
					}
				}

			}

			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			if (modbusstr.indexOf(",") >= 0) {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("解析成功");
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("获取失败,服务器异常，请稍后重试！");
			}

			return data;
		} catch (Exception e) {
			Device dd = new Device();
			dd.setId(device_id);
			dd.setRw_role_req("R");
			dd.setRw_role_res("OK");
			deviceService.updateDevice(dd);
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}

	/**
	 * 查询版本信息
	 *
	 * @param device_id
	 * @return
	 */
	@RequestMapping("/getbqxx")
	@ResponseBody
	public BaseResponseData getbqxx(@RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			// 获取单个风机最新采集数据
			DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
			// 获取设备基本信息
			DeviceType deviceType = new DeviceType();
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			totcal_device_id = device_id;
			// 查询版本
			/*
			 * 版本信息如下: 网侧DSP软件版本:DSP_WGxxxxKDF_Vxx_GRID_Vx.x 地址:94
			 * 机侧DSP软件版本:DSP_WGxxxxKDF_Vxx_ROT_Vx.x 地址:42 通讯ARM软件版本:
			 * ARM_WGxxxxKDF_Vxx _Vx.x 地址:416,417,410 网侧DSP参数版本: Vx.x 地址:95
			 * 机侧DSP参数版本: Vx.x 地址:54 通讯ARM参数版本: Vx.x 地址:468
			 * 
			 * 版本信息如下: 网侧DSP软件版本:DSP_WGxxxxKDF_Vxx_GRID_Vx.x 地址:97
			 * 机侧DSP软件版本:DSP_WGxxxxKDF_Vxx_ROT_Vx.x 地址:264 通讯ARM软件版本:
			 * ARM_WGxxxxKDF_Vxx _Vx.x 地址:50
			 */
			String bbstr = "";
			String bqxxstr = "DSP_";
			String bqxxstr_jc = "DSP_";
			String bqxxstr_arm = "ARM_";
			String bqxxstr_wcdspcs = "V";
			String bqxxstr_jcdspcs = "V";
			String bqxxstr_armcs = "V";
			if (modbustcp_type == 0) {
				// 网测DSP版本
				String colnumvalue_wc = datacq.getData().split(",")[94];
				List<String> strList = modBusTcpService.parseModBusTcp(94, Integer.parseInt(colnumvalue_wc),
						modbustcp_type, datacq.getData());
				String dspstr = strList.get(1);
				if (dspstr.length() >= 5) {
					if (dspstr.substring(0, 1).equals("1")) {
						bqxxstr += "WG1500KDF";
					} else {
						bqxxstr += "WG2000KDF";
					}
					bqxxstr += "_V" + dspstr.substring(1, 2);
					bqxxstr += "_GRID_";
					bqxxstr += "_V" + dspstr.substring(3, 4) + "." + dspstr.substring(4, 5);
				} else {
					bqxxstr = "无版本信息";
				}
				// 机测DSP版本
				String colnumvalue_jc = datacq.getData().split(",")[42];
				List<String> strList_jc = modBusTcpService.parseModBusTcp(42, Integer.parseInt(colnumvalue_jc),
						modbustcp_type, datacq.getData());
				String dspstr_jc = strList_jc.get(1);
				if (dspstr_jc.length() >= 5) {
					if (dspstr_jc.substring(0, 1).equals("1")) {
						bqxxstr_jc += "WG1500KDF";
					} else {
						bqxxstr_jc += "WG2000KDF";
					}
					bqxxstr_jc += "_V" + dspstr_jc.substring(1, 2);
					bqxxstr_jc += "_MOTOR_";
					bqxxstr_jc += "_V" + dspstr_jc.substring(3, 4) + "." + dspstr_jc.substring(4, 5);
				} else {
					bqxxstr_jc = "无版本信息";
				}

				// arm版本信息
				String colnumvalue_arm416 = datacq.getData().split(",")[416];
				List<String> strList_arm416 = modBusTcpService.parseModBusTcp(416, Integer.parseInt(colnumvalue_arm416),
						modbustcp_type, datacq.getData());
				bqxxstr_arm += "WG" + strList_arm416.get(1) + "KDF";

				String colnumvalue_arm417 = datacq.getData().split(",")[417];
				List<String> strList_arm417 = modBusTcpService.parseModBusTcp(417, Integer.parseInt(colnumvalue_arm417),
						modbustcp_type, datacq.getData());
				bqxxstr_arm += "_V" + strList_arm417.get(1);

				String colnumvalue_arm410 = datacq.getData().split(",")[410];
				List<String> strList_arm410 = modBusTcpService.parseModBusTcp(410, Integer.parseInt(colnumvalue_arm410),
						modbustcp_type, datacq.getData());
				if (strList_arm410.get(1).length() > 1) {
					bqxxstr_arm += "_V" + strList_arm410.get(1).substring(0, 1) + "."
							+ strList_arm410.get(1).substring(1, 2);

				} else {
					bqxxstr_arm += "无版权信息";
				}

				// 网侧DSP参数版本: Vx.x
				String colnumvalue_wcdsp = datacq.getData().split(",")[95];
				List<String> strList_wcdsp = modBusTcpService.parseModBusTcp(95, Integer.parseInt(colnumvalue_wcdsp),
						modbustcp_type, datacq.getData());
				if (strList_wcdsp.get(1).length() > 1) {
					bqxxstr_wcdspcs += strList_wcdsp.get(1).substring(0, 1) + "."
							+ strList_wcdsp.get(1).substring(1, 2);

				} else {
					bqxxstr_wcdspcs += "无版权信息";
				}

				// 机侧DSP参数版本: Vx.x
				String colnumvalue_jcdsp = datacq.getData().split(",")[54];
				List<String> strList_jcdsp = modBusTcpService.parseModBusTcp(54, Integer.parseInt(colnumvalue_jcdsp),
						modbustcp_type, datacq.getData());
				if (strList_jcdsp.get(1).length() > 1) {
					bqxxstr_jcdspcs += strList_jcdsp.get(1).substring(0, 1) + "."
							+ strList_jcdsp.get(1).substring(1, 2);
				} else {
					bqxxstr_jcdspcs += "无版权信息";
				}
				// 通讯ARM参数版本: Vx.x
				String colnumvalue_armcs = datacq.getData().split(",")[468];
				List<String> strList_armcs = modBusTcpService.parseModBusTcp(468, Integer.parseInt(colnumvalue_armcs),
						modbustcp_type, datacq.getData());
				bqxxstr_armcs += strList_armcs.get(1);
				// 总和
				bbstr = bqxxstr + "," + bqxxstr_jc + "," + bqxxstr_arm + "," + bqxxstr_wcdspcs + "," + bqxxstr_wcdspcs
						+ "," + bqxxstr_jcdspcs + "," + bqxxstr_armcs;

			} else {
				/*
				 * 版本信息如下: 网侧DSP软件版本:DSP_WGxxxxKFP_Vxx_GRID_Vx.x 地址:97
				 * 机侧DSP软件版本:DSP_WGxxxxKDF_Vxx_ROT_Vx.x 地址:264 通讯ARM软件版本:
				 * ARM_WGxxxxKDF_Vxx _Vx.x 地址:50
				 */
				String wc_dsp_qgl = "DSP_";
				String jc_dsp_qgl = "DSP_";
				String arm_dsp_qgl = "ARM_";
				// 网侧DSP软件版本:
				String colnumvalue_qgl = datacq.getData().split(",")[97];
				List<String> qgl = modBusTcpService.parseModBusTcp(97, Integer.parseInt(colnumvalue_qgl),
						modbustcp_type, datacq.getData());
				String dspstr_qgl = qgl.get(1);
				if (dspstr_qgl.length() >= 5) {
					if (dspstr_qgl.substring(0, 1).equals("1")) {
						wc_dsp_qgl += "WG1500KFP";
					} else {
						wc_dsp_qgl += "WG2000KFP";
					}
					wc_dsp_qgl += "_V" + dspstr_qgl.substring(1, 3);
					wc_dsp_qgl += "_GRID_";
					wc_dsp_qgl += "_V" + dspstr_qgl.substring(3, 4) + "." + dspstr_qgl.substring(4, 5);
				} else {
					wc_dsp_qgl = "无版本信息";
				}

				// 机测DSP软件版本:
				String colnumvalue_jc_qgl = datacq.getData().split(",")[264];
				List<String> qgl_jc = modBusTcpService.parseModBusTcp(264, Integer.parseInt(colnumvalue_jc_qgl),
						modbustcp_type, datacq.getData());
				String dspstr_qgl_jc = qgl_jc.get(1);
				if (dspstr_qgl_jc.length() >= 5) {
					if (jc_dsp_qgl.substring(0, 1).equals("1")) {
						jc_dsp_qgl += "WG1500KFP";
					} else {
						jc_dsp_qgl += "WG2000KFP";
					}
					jc_dsp_qgl += "_V" + dspstr_qgl_jc.substring(1, 3);
					jc_dsp_qgl += "_MOTOR_";
					jc_dsp_qgl += "_V" + dspstr_qgl_jc.substring(3, 4) + "." + dspstr_qgl_jc.substring(4, 5);
				} else {
					jc_dsp_qgl = "无版本信息";
				}

				// 通讯ARM软件版本:
				String colnumvalue_aem_qgl = datacq.getData().split(",")[50];
				List<String> qgl_arm = modBusTcpService.parseModBusTcp(50, Integer.parseInt(colnumvalue_aem_qgl),
						modbustcp_type, datacq.getData());
				String dspstr_qgl_arm = qgl_arm.get(1);
				if (dspstr_qgl_arm.length() >= 5) {
					if (dspstr_qgl_arm.substring(0, 1).equals("1")) {
						arm_dsp_qgl += "WG1500KFP";
					} else {
						arm_dsp_qgl += "WG2000KFP";
					}
					arm_dsp_qgl += "_V" + dspstr_qgl_arm.substring(1, 3);
					arm_dsp_qgl += "_V" + dspstr_qgl_arm.substring(3, 4) + "." + dspstr_qgl_arm.substring(4, 5);
				} else {
					arm_dsp_qgl = "无版本信息";
				}
				bbstr = wc_dsp_qgl + "," + jc_dsp_qgl + "," + arm_dsp_qgl;
			}

			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("bbstr", bbstr);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");

			return data;
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

	/**
	 * 发送写指令，更改设备 读写状态
	 * 
	 * @param device_id
	 * @return
	 */
	@RequestMapping("/setControlCodeReq")
	@ResponseBody
	public BaseResponseData setControlCodeTestReq(@RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 发送写指令请求
			Device d = new Device();
			d.setId(device_id);
			d.setRw_role_req("W");
			d.setRw_role_res("OK");
			d.setRw_sql("发送写指令，更改设备 读写状态");
			deviceService.updateDevice(d);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			data.setCode(WebResponseCode.SUCCESS);
			return data;
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

	/**
	 * 获取设备的 读写状态
	 * 
	 * @param device_id
	 * @return
	 */
	@RequestMapping("/getControlCodeReq")
	@ResponseBody
	public BaseResponseData getControlCodeTestReq(@RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			Device d = deviceService.selectDeviceById(device_id);
			data.setMessage("解析成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("Device", d);
			data.setResponseData(resData);
			return data;
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

	/**
	 * 获取设备协议类型
	 * 
	 * @param device_id
	 * @return
	 */
	@RequestMapping("/getDeviceType")
	@ResponseBody
	public BaseResponseData getDeviceType(@RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceType deviceType = new DeviceType();
			int modbustcp_type = 0;
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("modbustcp_type", modbustcp_type);
			data.setResponseData(resData);
			return data;
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

	/**
	 * 时间兑时
	 * 
	 * @return
	 */
	@RequestMapping("/sendtimecode")
	@ResponseBody
	public BaseResponseData sendtimecode(@RequestParam("timeList") String timestr) {
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			String modbusstr = "";
			String headmodbusstr = "";
			// 修改所有设备的读写状态
			dataCqService.updateDevice_rwrole("W", "OK");
			// 查找所有设备
			List<Device> devicelist = dataCqService.geAllDevice();

			List<Device> suc_device = new ArrayList<Device>();
			List<Device> error_device = new ArrayList<Device>();
			System.out.println("对时操作：" + timestr);
			// 获取设备基本信息
			for (int i = 0; i < devicelist.size(); i++) {
				Device dev = devicelist.get(i);
				DeviceType deviceType = new DeviceType();
				// 获取当前设备协议类型（双馈，全功率，海上风电）
				deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
				modbustcp_type = deviceType.getModbus_type();
				// 获取设备的arm信息
				DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(dev.getId());
				Device d = deviceService.selectDeviceById(dev.getId());
				if (d != null && devinfo != null) {
					if (modbustcp_type == 2) {
						modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
								modbustcp_type, "31f4", timestr, "0");
					} else {
						// 672-677 /13179-13184
						modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
								modbustcp_type, "02A0", timestr, "0");
					}
					// 确认指令,第一条指令有正确的返回时间时才去发送确认指令
					if (modbusstr.contains(",")) {
						if (modbustcp_type == 2) {
							headmodbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "31f4", "8AH", "0");
						} else {
							// 672-677 /13179-13184
							headmodbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
									modbustcp_type, "0000", "8DH", "0");
						}
					}
					if (modbusstr.equals("") || headmodbusstr.equals("")) {
						error_device.add(d);
					} else {
						suc_device.add(d);
					}
				}

			}
			// 修改所有设备的读写状态
			dataCqService.updateDevice_rwrole("R", "OK");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			// 修改成功的设备
			resData.put("error_device", error_device);
			// 修改失败的设备
			resData.put("suc_device", suc_device);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("兑时成功");
		} catch (Exception e) {
			dataCqService.updateDevice_rwrole("R", "OK");
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}

	/**
	 * 一直修改状态
	 * 
	 * @param device_id
	 * @return
	 */
	public String setdevicetime(int device_id, String timestr) {
		dataCqService.updateDevice_rwrole("W", "OK");
		int modbustcp_type = 0;
		String modbusstr = "";
		Device dev = deviceService.selectDeviceById(device_id);
		DeviceType deviceType = new DeviceType();
		deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
		modbustcp_type = deviceType.getModbus_type();
		DeviceInfo devinfo = collectSetService.getDeviceInfoBydid(dev.getId());
		System.out.println("对时操作：" + timestr);
		Device d = deviceService.selectDeviceById(dev.getId());
		if (d.getRw_role_req().equals("R") && d.getRw_role_res().equals("OK")) {
			setdevicetime(device_id, timestr);
		} else {
			if (modbustcp_type == 2) {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"337b", timestr, "0");
			} else {
				// 672-677 /13179-13184
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(), modbustcp_type,
						"02A0", timestr, "0");
			}
			// 确认指令
			if (modbusstr.contains(",")) {
				if (modbustcp_type == 2) {
					modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
							modbustcp_type, "31f4", "8AH", "0");
				} else {
					// 672-677 /13179-13184
					modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(devinfo.getIp(), devinfo.getPort(),
							modbustcp_type, "0000", "8DH", "0");
				}
			}
		}

		return modbusstr;
	}
}
