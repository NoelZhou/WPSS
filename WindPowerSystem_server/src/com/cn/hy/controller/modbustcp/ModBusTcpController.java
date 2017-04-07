package com.cn.hy.controller.modbustcp;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.service.modbustcp.ModBusTcpService;

/**
 * （测试用例）
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/ModBusTcp")
public class ModBusTcpController {
	@Resource
	private ModBusTcpService modbustcpsservice;
	@RequestMapping("/parseaddr")
	@ResponseBody
	public BaseResponseData parseaddr(@RequestParam("addr") int addr, @RequestParam("shortvalue") int shortvalue,
			@RequestParam("device_type") int device_type) {
		BaseResponseData data = new BaseResponseData();
		try {
			//协议解析
			List<String> modbuslist = modbustcpsservice.parseModBusTcp(addr, shortvalue, device_type,"");
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("解析成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("modbuslist", modbuslist);
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
}
