package com.cn.hy.controller.DevciceDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.bean.JDBConnection;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.modbustcp.Parameter;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.deviceSystemService.DeviceSystemTestSerivce;
import com.cn.hy.service.deviceSystemService.ParameterService;
import com.cn.hy.service.modbustcp.ModBusTcpService;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DataCqService;
import com.cn.hy.service.system.DeviceService;

@Controller
@RequestMapping("/Parameter")
public class ParameterController {
	@Resource
	private ParameterService parameterService;
	@Resource
	private DataCqService dataCqService;
	@Resource
	private DeviceService deviceService;
	@Resource
	private CollectSetService collectSetService;
	@Resource
	private ModBusTcpService modBusTcpService;
	@Resource
	private DeviceSystemTestSerivce deviceSystemTestSerivce;

	@RequestMapping("/parameterByType")
	@ResponseBody
	public BaseResponseData parameterByType(@RequestParam("parent_type") Integer parent_type,
			@RequestParam("device_id") Integer device_id) {
		// 设备基本信息查询
		BaseResponseData data = new BaseResponseData();
		try {
			DeviceType deviceType = new DeviceType();
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			List<Parameter> parametelist = parameterService.selectbytype(parent_type, deviceType.getModbus_type());
			data.setMessage("查询成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("parametelist", parametelist);
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

	@RequestMapping("/parameterchildid")
	@ResponseBody
	public BaseResponseData parameterByType(@RequestParam("id") String ids, @RequestParam("device_id") int device_id) {
		BaseResponseData data = new BaseResponseData();
		try {
			// 设备详细信息查询
			DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
			int modbustcp_type = 0;
			DeviceType deviceType = new DeviceType();
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			List<Parameter> parametelist = new ArrayList<Parameter>();
			if (ids.split(",").length > 1) {
				String[] id = ids.split(",");
				for (int i = 0; i < id.length; i++) {
					List<Parameter> parametelistA = parameterService.selectbychild(Integer.parseInt(id[i]),
							modbustcp_type);
					parametelist.addAll(parametelistA);
				}
			} else {
				parametelist = parameterService.selectbychild(Integer.parseInt(ids), modbustcp_type);
			}
			for (int i = 0; i < parametelist.size(); i++) {
				Parameter pter = parametelist.get(i);
				int addr = parametelist.get(i).getAddr();
				if (datacq == null || datacq.getData() == null) {
					pter.setHvalue("无法获取该值");
					pter.setHvalue("无法获取该值");
					pter.setBitString("无法获取该值");
				} else {
					// System.out.println(datacq.getData().split(",").length);

					if ((modbustcp_type != 2 && (addr >= datacq.getData().split(",").length))
							|| (modbustcp_type == 2 && (addr - 12788 >= datacq.getData().split(",").length))) {
						pter.setHvalue("无法获取该值");
						pter.setHvalue("无法获取该值");
						pter.setBitString("无法获取该值");
					} else {
						String paramevalue = "";
						if (modbustcp_type == 2) {
							paramevalue = datacq.getData().split(",")[addr - 12788];
						} else {
							paramevalue = datacq.getData().split(",")[addr];
						}

						if (pter.getSysremark().equals("Hex")) {
							if (Integer.parseInt(paramevalue) < 0) {
								pter.setHvalue((Integer.toHexString(Integer.parseInt(paramevalue)).toUpperCase())
										.substring(4, 8) + "H");

							} else {
								pter.setHvalue(Integer.toHexString(Integer.parseInt(paramevalue)).toUpperCase() + "H");

							}
						} else {
							pter.setHvalue(paramevalue);
						}
						List<String> strList = modBusTcpService.parseModBusTcp(addr, Integer.parseInt(paramevalue),
								modbustcp_type, datacq.getData());
						if (strList.size() == 0) {
							pter.setBitString("不解析");
						} else {
							if (!strList.get(0).equals("4")) {
								pter.setBitString(strList.get(1));
							} else {
								pter.setBitString("不解析");
							}
						}
					}
				}
			}

			// 根据设备id查处modbus_type
			Device device = deviceService.findByModbus_type(device_id);
			// System.out.println(device.getModbus_type());
			String code = deviceSystemTestSerivce.getaddrparasewallzh(ids, device.getModbus_type());
			int codeLength = 0;
			if (code != "" && code != null) {
				String codes[] = code.split(",");
				codeLength = codes.length;
			}

			data.setMessage("查询成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("parametelist", parametelist);
			resData.put("codeLength", codeLength);
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

	/**
	 * 设备风机基本参数实时数据列表
	 * 
	 * @param modbus_type
	 *            协议类型
	 * @return
	 */
	@RequestMapping("/Basiclist")
	@ResponseBody
	public BaseResponseData basicByType(int modbus_type) {
		// 基本参数查询
		BaseResponseData data = new BaseResponseData();
		int device_id = 0;
		try {
			String headstr = "设备名称";
			List<String> trlist = new ArrayList<String>();
			// 获取设备名字的集合
			List<Parameter> parametelist = parameterService.selectdervicebasic(modbus_type);
			// 获取设备监控参数的集合
			List<Parameter> desiclist = parameterService.selectdervicename(modbus_type);
			for (int i = 0; i < parametelist.size(); i++) {
				// 对查到的风机进行遍历
				String paramestr = parametelist.get(i).getName();// 获取风机名字
				device_id = parametelist.get(i).getId();// 获取风机id
				// 获取单个风机最新采集数据
				DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
				// 获取设备基本信息
				for (int j = 0; j < desiclist.size(); j++) {
					// 对查到的类型的风机参数进行遍历
					int column = desiclist.get(j).getAddr();// 获取该参数的协议地址
					if (datacq != null) {
						if (datacq.getData() != null && datacq.getData() != "") {
							String paramevalue = "";
							if (parametelist.get(i).getModbus_type() == 2) {
								paramevalue = datacq.getData().split(",")[column - 12788];
							} else {
								paramevalue = datacq.getData().split(",")[column];
							}
							// String paramevalue =
							// datacq.getData().split(",")[column];
							List<String> strList = modBusTcpService.parseModBusTcp(column,
									Integer.parseInt(paramevalue), modbus_type, datacq.getData());

							paramestr += "#" + strList.get(1);
						}
					} else {
						paramestr += "#无数据";
					}

					if (i == 0) {
						if (desiclist.get(j).getUnit() != "") {
							headstr += "?" + desiclist.get(j).getName() + "(" + desiclist.get(j).getUnit() + ")";
						} else {
							headstr += "?" + desiclist.get(j).getName();
						}

					}

				}
				trlist.add(paramestr);
			}
			data.setMessage("查询成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("headstr", headstr);// 封装风机的名字
			resData.put("trlist", trlist);
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

	/**
	 * 谐波参数单位
	 */
	@RequestMapping("/harmonicUnit")
	@ResponseBody
	public void harmonicUnit(@RequestParam("id") int id) {
		System.out.println("谐波参数更改");
		JDBConnection jdbc = new JDBConnection();
		String sqlStr = "";
		if (id == 1 || id == 2) {
			sqlStr = "update modbustcp_sk_app set unit = 'V' where modbus_type = '1' and array_type='128'";
		} else {
			sqlStr = "update modbustcp_sk_app set unit = 'A' where modbus_type = '1' and array_type='128'";
		}
		PreparedStatement pstmt;
		try {
			pstmt = (PreparedStatement) jdbc.connection.prepareStatement(sqlStr);
			int i = pstmt.executeUpdate();
			System.out.println("resutl: " + i);
			pstmt.close();
			jdbc.connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
