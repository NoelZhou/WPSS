package com.cn.hy.controller.recordingManagement;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.bean.ReadModbusXml;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DeviceService;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

/**
 * 波形分析
 * @author HCheng
 *
 */
@Controller
@RequestMapping("/WaveAnalyze")
public class WaveAnalyzeController {
	@Resource
	private DeviceService deviceService;
	@Resource
	private CollectSetService collectSetService;
	
	/**
	 * 获取故障波形名称
	 */
	@RequestMapping("/listWaveNames")
	@ResponseBody
	public BaseResponseData listWaveNames(@RequestParam("device_id") int device_id,
			@RequestParam("tcp_type") String tcp_type){
		BaseResponseData data = new BaseResponseData();
		try {
			int modbustcp_type = 0;
			// 获取设备基本信息
			if(tcp_type != null && tcp_type != ""){
				if("0".equals(tcp_type)){
					modbustcp_type = 0;
				}else if("1".equals(tcp_type)){
					modbustcp_type = 1;
				}
			}else{
				Device dev = deviceService.selectDeviceById(device_id);
				DeviceType deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
				modbustcp_type = deviceType.getModbus_type();
			}
			
			String xmlpath =this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/waveanalyze";
			if (modbustcp_type == 0) {
				xmlpath += "/OSCdefine_DF.xml";
			}else if(modbustcp_type == 1){
				xmlpath += "/OSCdefine_FP.xml";
			}
			List<String> Systemxml=new ReadModbusXml().setlist(xmlpath);
			List<String> wcnameList = new ArrayList<String>();//网侧故障波形名称
			List<String> jcnameList = new ArrayList<String>();//机侧故障波形名称
			String [] errorArray;
			for (int i = 0; i < Systemxml.size(); i++) {
				if(i > 66 && i < 99){
					errorArray = Systemxml.get(i).split(",");
					wcnameList.add(errorArray[1]);
				}
				if(i > 99 && i < 132){
					errorArray = Systemxml.get(i).split(",");
					jcnameList.add(errorArray[1]);
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("wcnameList", wcnameList);
			resData.put("jcnameList", jcnameList);
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
	 * 获取波形数据
	 */
	@RequestMapping("/getWaveData")
	@ResponseBody
	public BaseResponseData getWaveData(@RequestParam("filepath") String filepath,
			@RequestParam("ids") String ids){
		BaseResponseData data = new BaseResponseData();
		try {
			//读取excel文件
			String[] idArray = ids.split(",");
			File file = new File(filepath);
			Workbook wb = Workbook.getWorkbook(file);
			Sheet sheet = wb.getSheet(0);
			int rows = sheet.getRows();//行
			//int cols = sheet.getColumns();//列
			List<int[]> datalist = new ArrayList<>();
			for (int i = 0; i < idArray.length; i++) {
				int[] strs = new int[rows];
				for (int j = 0; j < rows; j++) {  
			    	Cell cell = sheet.getCell(Integer.parseInt(idArray[i]), j);  
			    	strs[j] = Integer.parseInt(cell.getContents());
			    } 
				datalist.add(strs);
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("datalist", datalist);
			data.setResponseData(resData);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
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
	 * 获取波形文件
	 * @param file
	 * @param wavetype
	 * @param tcptype
	 * @return
	 */
	@RequestMapping(value="/loadwaves", method = RequestMethod.POST)
	@ResponseBody
	public BaseResponseData goMenuList(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		BaseResponseData data = new BaseResponseData();
		try {
			List<int[]> datalist = new ArrayList<>();
			if (!file.isEmpty()) {
				InputStream in = file.getInputStream();
				Workbook wb = Workbook.getWorkbook(in);
				Sheet sheet = wb.getSheet(0);
				int rows = sheet.getRows();//行
				int cols = sheet.getColumns();//列
				
				for (int i = 0; i < cols; i++) {
					int[] strs = new int[rows];
					for (int j = 0; j < rows; j++) {  
				    	Cell cell = sheet.getCell(i, j);  
				    	strs[j] = Integer.parseInt(cell.getContents());
				    } 
					datalist.add(strs);
				}
			}
			data.setCode(WebResponseCode.SUCCESS);
			data.setMessage("获取成功");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("datalist", datalist);
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
}
