package com.cn.hy.controller.recordingManagement;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.recordingManagement.RecordingManagement;
import com.cn.hy.service.recordingManagement.RecordingManagementService;
import com.cn.hy.util.FileDownload;

@Controller
@RequestMapping("/RecordingManagement")
public class RecordingManagementController {
	
	@Resource
	private RecordingManagementService recordingManagementService;
	
	/*
	 * 查询所有
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData list(@RequestParam(value="device_name",required=false) String device_name,
			@RequestParam(value="errorname",required=false) int errorname,
			@RequestParam(value="start_time",required=false) String start_time,
			@RequestParam(value="end_time",required=false) String end_time) {
		BaseResponseData data = new BaseResponseData();
		try {
			/*if(start_time!=null && start_time!=""){
				start_time=start_time.substring(0,10)+" "+start_time.substring(10);
			}
			if(end_time!=null && end_time!=""){
				end_time=end_time.substring(0,10)+" "+end_time.substring(10);
			}*/
			RecordingManagement recordingManagement=new RecordingManagement();
			recordingManagement.setDevice_name(device_name);
			recordingManagement.setError_type(errorname);
			recordingManagement.setStart_time(start_time);
			recordingManagement.setEnd_time(end_time);
			 List<RecordingManagement> recordingManagementList = recordingManagementService.list(recordingManagement);
			 if (recordingManagementList!=null) {
				data.setMessage("获取成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("recordingManagementList", recordingManagementList);
				data.setResponseData(resData);
				return data;
			} else {
				data.setCode(WebResponseCode.SUCCESS);
				data.setMessage("没有数据！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("roleList", "");
				data.setResponseData(resData);
				return data;
			}
			 
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
	
	/*
	*下载记录文件 
	*/
	@RequestMapping("/downfile")
	public void downfile(@RequestParam("file") String error_excel,
			@RequestParam("device_name") String device_name,
			HttpServletResponse response,
			HttpServletRequest request){
		try {
			if(device_name!=null && device_name!=""){
				device_name=URLDecoder.decode(device_name, "UTF-8");
				error_excel=URLDecoder.decode(error_excel, "UTF-8");
			}
			String fileName[]=error_excel.split("/");
			String fileNameFive=fileName[fileName.length-1];
			String fileNameFiveLeft[]=fileNameFive.split("[.]");
			String downloadName=fileNameFiveLeft[0];
			//DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			//String downloadName=device_name+df.format(new Date());
			FileDownload.fileDownload(response,request, error_excel,downloadName);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}
