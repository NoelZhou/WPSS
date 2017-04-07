package com.cn.hy.controller.DevciceDetail;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.bean.ReadModbusXml;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.modbustcp.ModBusErrorCode;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.system.Role_menu;
import com.cn.hy.service.deviceSystemService.DeviceSystemService;
import com.cn.hy.service.modbustcp.ModBusTcpService;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DataCqService;
import com.cn.hy.service.system.DeviceService;
import com.cn.hy.service.system.UserService;

@Controller
@RequestMapping("/Devicetail")
public class DeviceSystemParame {
	@Resource
	private DeviceSystemService devicesystemsservice;
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

	@RequestMapping("/getSystemParame")
	@ResponseBody
	public BaseResponseData parseaddr(@RequestParam("device_id") int device_id, HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<String> errorstr = new ArrayList<String>();
			DeviceType deviceType = new DeviceType();
			int modbustcp_type = 0;
			// 获取单个风机最新采集数据
			DataCq datacq = dataCqService.getDataCqBydeviceid(device_id);
			String[] dataArray = datacq.getData().split(",");
			// 获取设备基本信息
			Device dev = deviceService.selectDeviceById(device_id);
			deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
			modbustcp_type = deviceType.getModbus_type();
			// 获取故障协议地址及bit位
			List<ModBusErrorCode> errorcodelist = devicesystemsservice.geterrorcode(modbustcp_type);
			for (int i = 0; i < errorcodelist.size(); i++) {
				ModBusErrorCode errorcode = errorcodelist.get(i);
				String errorvalue = "0";
				if (datacq != null && datacq != null) {
					if (datacq.getData() != null && datacq.getData() != "") {
						if (errorcode.getAddr() >= 12788) {
							errorvalue = datacq.getData().split(",")[errorcode.getAddr() - 12788];
						} else {
							errorvalue = datacq.getData().split(",")[errorcode.getAddr()];
						}
					}
				}
				if (!errorvalue.equals("0")) {
					// 协议解析方法
					List<String> strList = modBusTcpService.parseModBusTcp(errorcode.getAddr(),
							Integer.parseInt(errorvalue), modbustcp_type, datacq.getData());
					// 获取具体故障地址的Bit位信息
					String errorbitstr = devicesystemsservice.geterrorcodebyaddr(errorcode.getAddr(), modbustcp_type);
					// 所有bit位都查看的故障地址
					if (errorbitstr == null || errorbitstr.equals("")) {
						String[] bitvlaue = strList.get(1).split("[|]");
						String bitshow = "";
						// 如果该地址位 不存在bit位信息 不做 故障处理
						if (bitvlaue.length > 1) {
							for (int j = 0; j < bitvlaue.length; j++) {
								String onebit = bitvlaue[j].split(",")[2];
								if (onebit.equals("1")) {
									bitshow += bitvlaue[j] + "|";
								}
							}
						}
						if (bitshow.contains("|")) {
							bitshow = bitshow.substring(0, bitshow.length() - 1);
							errorstr.add(errorcode.getErrorname() + ":" + errorcode.getName() + "&"
									+ errorcode.getAddr() + "&"
									+ getHex4(Integer.toHexString(Integer.parseInt(errorvalue)).toUpperCase()) + "H"
									+ "&" + errorvalue + "&" + bitshow);
						}
					} else {
						String[] errorbitlist = errorbitstr.split(",");
						String[] bitvlaue = strList.get(1).split("[|]");
						String bitshow = "";
						// 如果该地址位 不存在bit位信息 不做 故障处理
						if (bitvlaue.length > 1) {
							for (int j = 0; j < bitvlaue.length; j++) {
								String onebit = bitvlaue[j].split(",")[2];
								String addbit = bitvlaue[j].split(",")[0];
								if (onebit.equals("1")) {
									// 循环子的Bit值
									for (int k = 0; k < errorbitlist.length; k++) {
										if (errorbitlist[k].equals(addbit)) {
											bitshow += bitvlaue[j] + "|";
										}
									}
								}
							}
						}
						if (bitshow.contains("|")) {
							bitshow = bitshow.substring(0, bitshow.length() - 1);
							errorstr.add(errorcode.getErrorname() + ":" + errorcode.getName() + "&"
									+ errorcode.getAddr() + "&"
									+ getHex4(Integer.toHexString(Integer.parseInt(errorvalue)).toUpperCase()) + "H"
									+ "&" + errorvalue + "&" + bitshow);
						}
					}
				}
			}
			// 开始获取电网和内部参数值，更加设备类型读取不同xml
			List<String> dzparamelist = new ArrayList<String>();
			List<String> djparamelist = new ArrayList<String>();
			List<String> nbparamelist = new ArrayList<String>();
			List<String> dzparamelistgai = new ArrayList<String>();
//			 List<String> djparamelistgai=new ArrayList<String>();
			List<String> nbparamelistgai = new ArrayList<String>();
			// File f = new File(this.getClass().getResource("/").getPath());
			String xmlpath1 = this.getClass().getClassLoader().getResource("").getPath() + "xmlconfig/系统参数";
			String xmlpath2 = this.getClass().getClassLoader().getResource("").getPath() + "xmlconfig/系统参数改";
			// String xmlpath
			// =DeviceSystemParame.class.getClass().getResource("/").getPath() +
			// "/WebContent/WEB-INF/系统参数";
			if (modbustcp_type == 0) {
				xmlpath1 += "/StandaloneSystemPara_DF.xml";
				xmlpath2 += "/StandaloneSystemPara_DF.xml";
			} else if (modbustcp_type == 1) {
				xmlpath1 += "/StandaloneSystemPara_FP.xml";
				xmlpath2 += "/StandaloneSystemPara_FP.xml";
			} else {
				xmlpath1 += "/StandaloneSystemPara_offshorewind.xml";
				xmlpath2 += "/StandaloneSystemPara_offshorewind.xml";
			}
			List<String> Systemxml = new ReadModbusXml().setlist(xmlpath1);
			List<String> Systemxmlgai = new ReadModbusXml().setlist(xmlpath2);
			for (int i = 0; i < Systemxml.size(); i++) {
				String strone = Systemxml.get(i);
				String[] str = strone.split(",");
				if (str.length > 2) {
					// 解析值
					String errorvalue = "0";
					if (datacq != null && datacq != null) {
						if (datacq.getData() != null && datacq.getData() != "") {
							if (Integer.parseInt(str[2]) >= 12788) {
								errorvalue = datacq.getData().split(",")[Integer.parseInt(str[2]) - 12788];
							} else {
								errorvalue = datacq.getData().split(",")[Integer.parseInt(str[2])];
							}
						}
					}
					String showvalue = modBusTcpService.parseModBusTcp(Integer.parseInt(str[2]),
							Integer.parseInt(errorvalue), modbustcp_type, datacq.getData()).get(1);
					// 全部放在内部参数
					nbparamelist.add(str[9] + "," + showvalue + "," + str[6] + "," + str[2] + "," + str[7]);
				}
			}
			for (int i = 0; i < Systemxmlgai.size(); i++) {
				String strone = Systemxmlgai.get(i);
				String[] str = strone.split(",");
				if (str.length > 2) {
					// 解析值
					String errorvalue = "0";
					if (datacq != null && datacq != null) {
						if (datacq.getData() != null && datacq.getData() != "") {
							if (Integer.parseInt(str[2]) >= 12788) {
								errorvalue = datacq.getData().split(",")[Integer.parseInt(str[2]) - 12788];
							} else {
								errorvalue = datacq.getData().split(",")[Integer.parseInt(str[2])];
							}
						}
					}
					String showvalue = modBusTcpService.parseModBusTcp(Integer.parseInt(str[2]),
							Integer.parseInt(errorvalue), modbustcp_type, datacq.getData()).get(1);
					if (str[10].equals("其他参数")) {
						dzparamelistgai.add(str[9] + "," + showvalue + "," + str[6] + "," + str[2] + "," + str[7]);
					} else {
						nbparamelistgai.add(
								str[9] + "," + showvalue + "," + str[6] + "," + str[2] + "," + str[7] + "," + str[10]);
					}
				}
			}
			String pararmimg = "";
			String qgl_zc = "";
			// 获取头部的图片
			if (modbustcp_type == 0) {
				String errorvalue = "0";
				String bitvalue21_5 = "0";
				String bitvalue71_5 = "0";
				String bitvalue231_5 = "0";
				String bitvalue231_4 = "0";
				if (datacq != null && datacq != null) {
					if (datacq.getData() != null && datacq.getData() != "") {
						errorvalue = datacq.getData().split(",")[21];
						bitvalue71_5 = datacq.getData().split(",")[71];
						bitvalue231_5 = datacq.getData().split(",")[231];
					}
				}
				List<String> strList21 = modBusTcpService.parseModBusTcp(21, Integer.parseInt(errorvalue),
						modbustcp_type, datacq.getData());
				List<String> strList71 = modBusTcpService.parseModBusTcp(71, Integer.parseInt(bitvalue71_5),
						modbustcp_type, datacq.getData());
				List<String> strList231 = modBusTcpService.parseModBusTcp(231, Integer.parseInt(bitvalue231_5),
						modbustcp_type, datacq.getData());
				if (strList21.get(1).split("[|]").length > 0) {
					bitvalue21_5 = strList21.get(1).split("[|]")[5].split(",")[2];
				}
				if (strList71.get(1).split("[|]").length > 0) {
					bitvalue71_5 = strList71.get(1).split("[|]")[5].split(",")[2];
				}
				if (strList231.get(1).split("[|]").length > 0) {
					bitvalue231_5 = strList231.get(1).split("[|]")[5].split(",")[2];
				}
				if (strList231.get(1).split("[|]").length > 0) {
					bitvalue231_4 = strList231.get(1).split("[|]")[4].split(",")[2];
				}
				String allbit = bitvalue21_5 + bitvalue71_5 + bitvalue231_5 + bitvalue231_4;
				// 4个开关16种组合状态
				if (allbit.equals("0000")) {
					pararmimg = "sk/sk_0000.png";
				} else if (allbit.equals("0001")) {
					pararmimg = "sk/sk_0001.png";
				} else if (allbit.equals("0010")) {
					pararmimg = "sk/sk_0010.png";
				} else if (allbit.equals("0100")) {
					pararmimg = "sk/sk_0100.png";
				} else if (allbit.equals("1000")) {
					pararmimg = "sk/sk_1000.png";
				} else if (allbit.equals("0011")) {
					pararmimg = "sk/sk_0011.png";
				} else if (allbit.equals("0101")) {
					pararmimg = "sk/sk_0101.png";
				} else if (allbit.equals("1001")) {
					pararmimg = "sk/sk_1001.png";
				} else if (allbit.equals("1010")) {
					pararmimg = "sk/sk_1010.png";
				} else if (allbit.equals("1100")) {
					pararmimg = "sk/sk_1100.png";
				} else if (allbit.equals("0111")) {
					pararmimg = "sk/sk_0111.png";
				} else if (allbit.equals("1101")) {
					pararmimg = "sk/sk_1101.png";
				} else if (allbit.equals("1011")) {
					pararmimg = "sk/sk_1011.png";
				} else if (allbit.equals("1110")) {
					pararmimg = "sk/sk_1110.png";
				} else if (allbit.equals("1111")) {
					pararmimg = "sk/sk_1111.png";
				} else {
					pararmimg = "sk/sk_0000.png";
				}
			} else if (modbustcp_type == 1) {
				// 全功率
				String bitvalue_zhu = "0";
				String bitvalue_ce = "0";
				String bitvalue71_5 = "0";
				String bitvalue231_5 = "0";
				String errorvalue_455 = "0";
				if (datacq != null && datacq != null) {
					if (datacq.getData() != null && datacq.getData() != "") {
						errorvalue_455 = datacq.getData().split(",")[455];
						bitvalue71_5 = datacq.getData().split(",")[71];
						bitvalue231_5 = datacq.getData().split(",")[231];
					}
				}
				List<String> strList455 = modBusTcpService.parseModBusTcp(455, Integer.parseInt(errorvalue_455),
						modbustcp_type, datacq.getData());
				List<String> strList71 = modBusTcpService.parseModBusTcp(71, Integer.parseInt(bitvalue71_5),
						modbustcp_type, datacq.getData());
				List<String> strList231 = modBusTcpService.parseModBusTcp(231, Integer.parseInt(bitvalue231_5),
						modbustcp_type, datacq.getData());
				if (strList455.get(1).split("[|]").length > 0) {
					String str455 = strList455.get(1);
					if (str455.equals("1") || str455.equals("3") || str455.equals("4")) {
						bitvalue_zhu = "1";
					}
					if (str455.equals("2") || str455.equals("3") || str455.equals("4")) {
						bitvalue_ce = "1";
					}
				}
				if (strList71.get(1).split("[|]").length > 0) {
					bitvalue71_5 = strList71.get(1).split("[|]")[5].split(",")[2];
				}
				if (strList231.get(1).split("[|]").length > 0) {
					bitvalue231_5 = strList231.get(1).split("[|]")[10].split(",")[2];
				}
				String allbit2 = bitvalue71_5 + bitvalue231_5;
				qgl_zc = bitvalue_zhu + bitvalue_ce;
				// 2个开关4种组合状态
				if (allbit2.equals("00")) {
					pararmimg = "qgl/qgl_00.png";
				} else if (allbit2.equals("01")) {
					pararmimg = "qgl/qgl_01.png";
				} else if (allbit2.equals("10")) {
					pararmimg = "qgl/qgl_10.png";
				} else if (allbit2.equals("11")) {
					pararmimg = "qgl/qgl_11.png";
				} else {
					pararmimg = "qgl/qgl_00.png";
				}

			} else if (modbustcp_type == 2) {
				String errorvalue = "0";
				String bitvalue3 = "0";
				String bitvalue4 = "0";
				if (datacq != null && datacq != null) {
					if (datacq.getData() != null && datacq.getData() != "") {
						errorvalue = datacq.getData().split(",")[12822 - 12788];
					}
				}
				List<String> strList12823 = modBusTcpService.parseModBusTcp(12822, Integer.parseInt(errorvalue),
						modbustcp_type, datacq.getData());
				if (strList12823.get(1).split("[|]").length > 0) {
					bitvalue3 = strList12823.get(1).split("[|]")[3].split(",")[2];
					// bitvalue12=strList12823.get(1).split("[|]")[12].split(",")[2];
					bitvalue4 = strList12823.get(1).split("[|]")[4].split(",")[2];
					// bitvalue13=strList12823.get(1).split("[|]")[13].split(",")[2];
				}
				String bit_3_4 = bitvalue3 + bitvalue4;
				// 2个开关4种组合状态
				if (bit_3_4.equals("10")) {
					pararmimg = "hsfd/hsfd_10.png";
				} else if (bit_3_4.equals("11")) {
					pararmimg = "hsfd/hsfd_11.png";
				} else if (bit_3_4.equals("01")) {
					pararmimg = "hsfd/hsfd_01.png";
				} else if (bit_3_4.equals("00")) {
					pararmimg = "hsfd/hsfd_00.png";
				} else {
					pararmimg = "hsfd/hsfd_00.png";
				}

			}

			// 获取session中的菜单权限
			HttpSession session = request.getSession();
			String employeeId = (String) session.getAttribute("employeeId");
			Role_menu role_menu = userService.findByUserId(employeeId);
			int write_p = role_menu.getWrite_p();

			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("value38", dataArray[38]);
			resData.put("errorstr", errorstr);
			resData.put("dzparamelist", dzparamelist);
			resData.put("djparamelist", djparamelist);
			resData.put("nbparamelist", nbparamelist);
			resData.put("dzparamelistgai", dzparamelistgai);
//			resData.put("djparamelistgai", djparamelistgai);
			resData.put("nbparamelistgai", nbparamelistgai);
			resData.put("pararmimg", pararmimg);
			resData.put("qgl_zc", qgl_zc);
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

	private String getHex4(String upperCase) {
		String hx = "";
		if (upperCase.length() > 4) {
			hx = upperCase.substring(4, upperCase.length());
		} else {
			hx = upperCase;
		}
		return hx;
	}

	// 单机调试采用.do访问jsp
	@RequestMapping("/chooseMenu")
	public ModelAndView chooseMenu(@RequestParam(value = "device_id", required = false) int device_id,
			@RequestParam(value = "modbus_type", required = false) int modbus_type,
			@RequestParam(value = "type", required = false) int type,
			@RequestParam(value = "modbusid", required = false) int modbusid, HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView();
		try {
			response.setContentType("text/html;charset=UTF-8");
			request.setCharacterEncoding("UTF-8");
			HttpSession session = request.getSession();
			String employeeId = (String) session.getAttribute("employeeId");
			if (employeeId == null || employeeId == "") {
				PrintWriter out = response.getWriter();
				out.print("<script>alert('您长时间没有操作系统，请重新登录!');window.parent.location.href='../login.jsp'</script>");
				out.close();
			}
			String viewName = "";
			if (type == 1) {
				viewName = "jsp/devicedetailjsp/DeviceSystemParame.jsp?device_id=" + device_id + "&modbus_type="
						+ modbusid;
			} else if (type == 2) {
				viewName = "jsp/devicedetailjsp/parametorlist.jsp?device_id=" + device_id + "&modbus_type=" + modbusid;
			} else if (type == 3) {
				viewName = "wfp/osc/toPage.do?device_id=" + device_id + "&modbus_type=" + modbus_type;
				mv.setViewName("redirect:/" + viewName);
				return mv;
			} else if (type == 4) {
				if (modbusid == 2) {
					viewName = "jsp/devicedetailjsp/DeviceSystemTest_hsfd.jsp?device_id=" + device_id + "&modbus_type="
							+ modbusid;
				} else {
					viewName = "jsp/devicedetailjsp/DeviceSystemTest.jsp?device_id=" + device_id + "&modbus_type="
							+ modbusid;
				}
			}

			mv.setViewName(viewName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;

	}
}
