package com.cn.hy.controller.firmwareUpgrade;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgrade;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgradeDeviceInfoDetail;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.firmwareUpgrade.FirmwareUpgradeService;
import com.cn.hy.service.system.DeviceService;

/**
 * 固件升级Controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/wfp/firmwareUpgrade")
public class FirmwareUpgradeController {

	@Resource
	private FirmwareUpgradeService firmwareUpgradeService;
	@Resource
	private DeviceService deviceService;
	@Autowired
	private HttpServletRequest request;
     
	
	/**
	 * 固件升级页面跳转
	 */
	@RequestMapping("/toFirmwareUpgrade")
	public ModelAndView dspLine(ModelAndView model) {
		model.setViewName("jsp/firmwareUpgrade/firmwareUpgrade");
		return model;
	}

	/**
	 * 列表数据
	 * 
	 * @return
	 */
	@RequestMapping("/selectAllList")
	@ResponseBody
	public BaseResponseData selectAllUser() {
		BaseResponseData data = new BaseResponseData();
		try {
			FirmwareUpgrade firmwareUpgrade = new FirmwareUpgrade();
			String modbusType = request.getParameter("modbusType");
			firmwareUpgrade.setModbusType(modbusType);
			List<FirmwareUpgrade> firmwareUpgradeList = firmwareUpgradeService.getDeviceinfoList(firmwareUpgrade);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("firmwareUpgradeList", firmwareUpgradeList);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("查询成功");
		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("查询失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}

	@RequestMapping("/fileUpload")
	@ResponseBody
	public BaseResponseData fileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("deviceIds") String deviceId) {
		BaseResponseData data = new BaseResponseData();
		try {
			/**
			 * 生存文件存储路径
			 */
			StringBuffer filePath = new StringBuffer();
			filePath.append(request.getSession().getServletContext().getRealPath("/"));
			filePath.append("upload/");
			filePath.append(System.currentTimeMillis());
			filePath.append(file.getOriginalFilename());
			file.transferTo(new File(filePath.toString()));// 转存文件

			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("filePath", filePath);

			if (StringUtils.isEmpty(deviceId)) {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("升级失败!");
				//清除固件升级session信息
				FirmwareUpgradeUtils.updateProgressSessionMsgClear();
				return data;
			}

			String[] deviceIds = deviceId.split(",");
			if (deviceIds.length > 0 && StringUtils.isEmpty(deviceIds[0])) {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("升级失败!");
				//清除固件升级session信息
				FirmwareUpgradeUtils.updateProgressSessionMsgClear();
				return data;
			}

			String resultMsg = "";
			FirmwareUpgradeUtils.setRequest(request);
			//批量升级
		    resultMsg = FirmwareUpgradeUtils.upgradeList(deviceIds, filePath.toString());
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage(resultMsg);
			data.setResponseData(resData);
		} catch (Exception e) {
			e.printStackTrace();
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("升级失败!");
			request.getSession().setAttribute("upgradeDeviceName", null);
			request.getSession().setAttribute("upgradeDeviceProgress", null);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/getProgress")
	@ResponseBody
	public BaseResponseData getProgress() {
		BaseResponseData data = new BaseResponseData();
		HashMap<String, Object> resData = new HashMap<String, Object>();
		Map<String, String> upgradeDeviceName = (Map<String, String>) request.getSession().getAttribute("upgradeDeviceName");
		if (upgradeDeviceName != null) {
			String resultUpgradeDeviceName = "";
			for (Map.Entry<String, String> entry : upgradeDeviceName.entrySet()) {
				if (!StringUtils.isEmpty(resultUpgradeDeviceName)) {
					resultUpgradeDeviceName += "<br/>";
				}
				resultUpgradeDeviceName += entry.getValue();
			}
			resData.put("upgradeDeviceName", resultUpgradeDeviceName);
		}
		Map<String, String> upgradeDeviceOver = (Map<String, String>) request.getSession().getAttribute("upgradeDeviceOver");
		if (upgradeDeviceOver != null) {
			String resultUpgradeDeviceOver = "";
			for (Map.Entry<String, String> entry : upgradeDeviceOver.entrySet()) {
				if (!StringUtils.isEmpty(resultUpgradeDeviceOver)) {
					resultUpgradeDeviceOver += "<br/>";
				}
				resultUpgradeDeviceOver += entry.getValue();
			}
			resData.put("upgradeDeviceOver", resultUpgradeDeviceOver);
		}

		data.setResponseData(resData);
		return data;
	}

	/**
	 * 更新反馈信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/updateMsg")
	@ResponseBody
	public BaseResponseData updateMsg() {
		
		
		BaseResponseData data = new BaseResponseData();

		Map<String, String> upgradeStatusMap = new HashMap<String, String>();
		upgradeStatusMap.put("1", "未升级");
		upgradeStatusMap.put("2", "正在升级");
		upgradeStatusMap.put("3", "故障");
		upgradeStatusMap.put("4", "升级成功");
		upgradeStatusMap.put("5", "正在向下级升级");
		upgradeStatusMap.put("6", "向下升级结束");
		upgradeStatusMap.put("7", "本级升级结束(DSP升级成功！)");

		
		int i = 0;
		Object obj = null;
		Map<String, String> upgradeDeviceOver = null;
		obj = request.getSession().getAttribute("upgradeDeviceOver");
		if (obj != null) {
			upgradeDeviceOver = (Map<String, String>) obj;
		}

		obj = null;
		obj = request.getSession().getAttribute("upgradeDeviceWaitStatusIndex");
		Map<String, Integer> upgradeDeviceWaitStatusIndex = null;
		if (obj != null) {
			upgradeDeviceWaitStatusIndex = (Map<String, Integer>)obj;
		}
		
		obj = null;
		obj = request.getSession().getAttribute("upgradeDeviceWaitStatus");
		
		if (obj != null && upgradeDeviceOver != null) {
			Map<String, String> upgradeDeviceWaitStatus = (Map<String, String>) obj;
			if(upgradeDeviceWaitStatusIndex == null){
				upgradeDeviceWaitStatusIndex = new HashMap<String, Integer>();
			}
			for (Map.Entry<String, String> entry : upgradeDeviceWaitStatus.entrySet()) {
				String[] udwses = entry.getValue().split("&");
				String txstate=FirmwareUpgradeUtils.getUpgradeStatus(udwses[0], Integer.parseInt(udwses[1]), 0,entry.getKey());
				int upgradeStatus = FirmwareUpgradeUtils.getUpgradeStatus();
				if(txstate.equals("1")){
					String ud = upgradeDeviceOver.get(entry.getKey());
					ud = ud.replaceAll("等待反馈","通讯故障,关闭窗口");
					upgradeDeviceOver.put(entry.getKey(), ud);
					upgradeDeviceWaitStatus.remove(entry.getKey());
					upgradeDeviceWaitStatusIndex.remove(entry.getKey());
					
				}
				if (upgradeStatus > 0 && upgradeStatus != 2 && upgradeStatus != 5&&upgradeStatus!=110) { //反馈结果大于0，直接更新
					String ud = upgradeDeviceOver.get(entry.getKey());
					ud = ud.replaceAll("等待反馈", upgradeStatusMap.get(String.valueOf(upgradeStatus)));
					upgradeDeviceOver.put(entry.getKey(), ud);
					upgradeDeviceWaitStatus.remove(entry.getKey());
					upgradeDeviceWaitStatusIndex.remove(entry.getKey());
				}else if(upgradeStatus==110){
					System.out.println("110停止升级状态");
					String ud = upgradeDeviceOver.get(entry.getKey());
					ud = ud.replaceAll("等待反馈","强制停止升级");
					upgradeDeviceOver.put(entry.getKey(), ud);
					upgradeDeviceWaitStatus.remove(entry.getKey());
					upgradeDeviceWaitStatusIndex.remove(entry.getKey());
					//i=0;
				}else{ //反馈结果等于0，发送60以内不继续发送，到达第60次如果继续无反馈，则更新结果为升级失败。
					Integer sendNum = upgradeDeviceWaitStatusIndex.get(entry.getKey());
					if(sendNum == null){
						sendNum = 0;
					}
					//故障的时候只需查询20次
					if(upgradeStatus==3){
						if(sendNum < 20){
							upgradeDeviceWaitStatusIndex.put(entry.getKey(), sendNum+1);
							 request.getSession().setAttribute("upgradeDeviceWaitStatusIndex", upgradeDeviceWaitStatusIndex);
						}else{
							String ud = upgradeDeviceOver.get(entry.getKey());
							ud = ud.replaceAll("等待反馈", "升级失败，升级状态位故障！");
							upgradeDeviceOver.put(entry.getKey(), ud);
							upgradeDeviceWaitStatus.remove(entry.getKey());
							upgradeDeviceWaitStatusIndex.remove(entry.getKey());
						}
					}else{
					if(sendNum <90){
						upgradeDeviceWaitStatusIndex.put(entry.getKey(), sendNum+1);
						 request.getSession().setAttribute("upgradeDeviceWaitStatusIndex", upgradeDeviceWaitStatusIndex);
					}else{
						String ud = upgradeDeviceOver.get(entry.getKey());
						ud = ud.replaceAll("等待反馈", "升级失败");
						upgradeDeviceOver.put(entry.getKey(), ud);
						upgradeDeviceWaitStatus.remove(entry.getKey());
						upgradeDeviceWaitStatusIndex.remove(entry.getKey());
					}
				  }
				}
				Integer sendNum1 = upgradeDeviceWaitStatusIndex.get(entry.getKey());
				System.out.println("sendNum:"+sendNum1);
				i++;
			}
		}
		
		if(i == 0){
			Map<String, String> upgradeDeviceName = (Map<String, String>) request.getSession().getAttribute("upgradeDeviceName");
			if(upgradeDeviceName != null && upgradeDeviceName.entrySet().size() > 0){
				i = 9;
			}
		}
	    System.out.println("i的标志位"+i);
		data.setCode(i);
		return data;
	}

	@RequestMapping("/getDeviceInfo")
	@ResponseBody
	public BaseResponseData getDeviceInfo(@RequestParam("deviceId") Integer deviceId, @RequestParam("ip") String ip,
			@RequestParam("port") int port) {
		// 设备基本信息查询
		BaseResponseData data = new BaseResponseData();
		try {
			//获取设备详细信息
			FirmwareUpgradeDeviceInfoDetail firmwareUpgradeDeviceInfoDetail = FirmwareUpgradeDeviceUtils.getDeviceInfo(ip, port);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("firmwareUpgradeDeviceInfoDetail", firmwareUpgradeDeviceInfoDetail);
			data.setResponseData(resData);
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取设备信息成功");
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取设备信息失败，服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}
	@RequestMapping("/updatesjjs")
	@ResponseBody
	public BaseResponseData updatesjjs(@RequestParam("deviceId") String deviceId) {
		// 设备基本信息查询
		BaseResponseData data = new BaseResponseData();
		try {
			String[] devstr= deviceId.split(",");
			for(int i=0;i<devstr.length;i++){
				Device dd = new Device();
				dd.setId(Integer.parseInt(devstr[i]));
				dd.setRw_role_req("SJJS");
				dd.setRw_role_res("OK");
				deviceService.updateDevice(dd);
				System.out.println("设备ID"+devstr[i]+"停止升级请求！");
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("请求发送成功！");
			return data;
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("修改设备停止升级状态失败！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
			return data;
		}
	}
}
