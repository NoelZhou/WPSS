package com.cn.hy.controller.serviceset;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.bean.WindPowerReadModbusXmlAll_hsfd;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.modbustcp.ModbusSkApp;
import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Appparame;
import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Iacparame;
import com.cn.hy.service.modbustcp.ModbusSkAppService;
import com.cn.hy.service.serviceset.ProtocolSettingsService;

@Controller
@RequestMapping("/ProtocolSettings")
public class ProtocolSettingsController {
	@Resource
	private ModbusSkAppService modbusSkAppService;
	@Resource
	private ProtocolSettingsService protocolSettingsService;

	/*
	 * 导入xml开始解析
	 */
	@RequestMapping("/importXml")
	@ResponseBody
	public BaseResponseData listFormTime(@RequestParam("xmlUrl") MultipartFile file, HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			String fileName = "";
			String filePath = "";
			if (null != file && !file.isEmpty()) {
				filePath = "D:/uploadFile/";
				fileName = com.cn.hy.util.FileUpload.fileUp(file, filePath, "uploadsxml");
			}
			WindPowerReadModbusXmlAll_hsfd xmlall = new WindPowerReadModbusXmlAll_hsfd();
			// 解析xml文件
			xmlall.setlist(filePath + fileName);
			File dir = new File(filePath);
			FileUtils.deleteQuietly(dir);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
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
	 * 根据modbus_type获取协议
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData list(@RequestParam("modbus_type") int modbus_type) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<ModbusSkApp> modbusSkapplist = modbusSkAppService.listModbusSkApp(modbus_type);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("modbusSkapplist", modbusSkapplist);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
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
	 * 根据addr查看枚举值
	 */
	@RequestMapping("/findByAddr")
	@ResponseBody
	public BaseResponseData findByAddr(@RequestParam("addr") int addr, @RequestParam("name") String name,
			@RequestParam("modbus_type") int modbus_type) {
		BaseResponseData data = new BaseResponseData();
		try {
			Modbustcp_Sk_Appparame modbustcp_Sk_Appparame = new Modbustcp_Sk_Appparame();
			modbustcp_Sk_Appparame.setAddr(addr);
			modbustcp_Sk_Appparame.setModbus_type(modbus_type);
			List<Modbustcp_Sk_Appparame> mskAppameList = protocolSettingsService.findByAddr(modbustcp_Sk_Appparame);

			Modbustcp_Sk_Iacparame modbustcp_Sk_Iacparame = new Modbustcp_Sk_Iacparame();
			modbustcp_Sk_Iacparame.setAddr(addr);
			modbustcp_Sk_Iacparame.setModbus_type(modbus_type);
			List<Modbustcp_Sk_Iacparame> mskIappameList = protocolSettingsService
					.findByMskIAppAddr(modbustcp_Sk_Iacparame);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			if (mskAppameList.size() != 0) {
				resData.put("mskAppameList", mskAppameList);
			}
			if (mskIappameList.size() != 0) {
				resData.put("mskIappameList", mskIappameList);
			}
			if (mskAppameList.size() == 0 && mskIappameList.size() == 0) {
				resData.put("listnull", "");
			}
			resData.put("addr", addr);
			resData.put("name", name);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
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
