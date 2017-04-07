package com.cn.hy.controller.errordataparse;


import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.errordataparse.ErrorDataParse;
import com.cn.hy.pojo.errortype.ErrorType;
import com.cn.hy.pojo.viewFaultHistory.ViewFaultHistory;
import com.cn.hy.service.errordataparse.ErrorDataParseService;
import com.cn.hy.service.modbustcp.ModBusTcpService;
import com.cn.hy.util.FileDownload;


@Controller
@RequestMapping("/ErrorDataParse")
public class ErrorDataParseController {

	@Resource
	private ErrorDataParseService errordataparseService;
	@Resource
	private ModBusTcpService modbustcpsservice;
	/*
	 * 查询所有
	 */
	@RequestMapping("/list")
	@ResponseBody
	public BaseResponseData list(
			@RequestParam(value="device_name",required=false) String device_name,
			@RequestParam(value="errorname",required=false) String errorname,
			@RequestParam(value="start_time",required=false) String start_time,
			@RequestParam(value="end_time",required=false) String end_time){
		BaseResponseData data = new BaseResponseData();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<ErrorDataParse>  errorDataParseList= errordataparseService.selectErrorDataParse(device_name,errorname, start_time, end_time);
			//查处所有事件类型
			List<ErrorType> errorType=errordataparseService.selectErrorType();
			String start=df.format(new Date());
			String end=sdf.format(new Date());
			 if (errorDataParseList!=null) {
				data.setMessage("获取成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("errorDataParseList", errorDataParseList);
				resData.put("errorType", errorType);
				resData.put("start", start);
				resData.put("end", end);
				data.setResponseData(resData);
				return data;
			} else {
				data.setCode(WebResponseCode.ERROR);
				data.setMessage("没有数据！");
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("errorDataParseList", "");
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
	@RequestMapping("/downloadLogFile")
	public void downTj(@RequestParam("device_name") String device_name,
			@RequestParam("error_excel") String error_excel,
			HttpServletResponse response,
			HttpServletRequest request){
		try {
			if(!device_name.equals(null) && !device_name.equals("")){
				device_name=URLDecoder.decode(device_name, "UTF-8");
				error_excel=URLDecoder.decode(error_excel, "UTF-8");
			}
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String downloadName=device_name+"_"+df.format(new Date());
			FileDownload.fileDownload(response,request, error_excel,downloadName);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
	}
	/*
	*编辑标签（模糊查询） 
	*/
	@RequestMapping("/editLabel")
	@ResponseBody
	public BaseResponseData editLabel(){
		BaseResponseData data=new BaseResponseData();
		try {
			List<ErrorType>  errorTypeList=errordataparseService.selectErrorType();
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorTypeList", errorTypeList);
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
	/*
	 * 查看故障历史(弃用)
	 */
	@RequestMapping("/viewFaultHistory")
	@ResponseBody
	public BaseResponseData viewFaultHistory(
			@RequestParam("errordata_id") int id){
		BaseResponseData data=new BaseResponseData();
		List<Map<String, Object>> modbusAddlist=new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> otherList=new ArrayList<Map<String, Object>>();
		try {
			List<ViewFaultHistory> viewFaultHistorylist=errordataparseService.viewFaultHistoryListAll(id);
			for (int i = 0; i < viewFaultHistorylist.size(); i++) {
				String[] allData=viewFaultHistorylist.get(i).getData().split(",");
				int addrone=Integer.parseInt(String.valueOf(viewFaultHistorylist.get(i).getAddr()));
				if(addrone>=12788){
					addrone=addrone-12788;
				}
				String shortvalue=allData[addrone];
				if(viewFaultHistorylist.get(i).getSysremark().equals("Hex")){
					shortvalue=Integer.toHexString(Integer.parseInt(shortvalue)).toUpperCase()+"H";
				}
				String addr=String.valueOf(viewFaultHistorylist.get(i).getAddr());
				String name=String.valueOf(viewFaultHistorylist.get(i).getName());
				Map<String,Object> modbuslist=new HashMap<String,Object>();
				if(i<=5){
					modbuslist.put("addr", addr);
					modbuslist.put("name", name);
					modbuslist.put("shortvalue", shortvalue);
					modbusAddlist.add(modbuslist);
				}
				if(i>5){
					modbuslist.put("addr", addr);
					modbuslist.put("name", name);
					modbuslist.put("shortvalue", shortvalue);
					otherList.add(modbuslist);
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("modbusAddlist", modbusAddlist);
			resData.put("otherList", otherList);
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
