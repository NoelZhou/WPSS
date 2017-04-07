package com.cn.hy.controller.windFieldPreview;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.datadspacq.Datadspacp;
import com.cn.hy.pojo.windFieldPreview.DeviceInfoDspWaveform;
import com.cn.hy.service.datadspacq.DatadspacqService;
import com.cn.hy.service.windFieldPreview.OscilloscopeService;
import com.cn.hy.util.CollectFiles;
import com.cn.hy.util.DSPEchartsUtil;
import com.cn.hy.util.ExcelUtils;
import com.cn.hy.util.FileDownload;

/**
 * 示波器数据加载
 * @author lihao
 *
 */
@Controller
@RequestMapping("/wfp/osc")
public class OscilloscopeController {
	
	@Resource
	private OscilloscopeService oscilloscopeService;
	@Resource
	private DatadspacqService datadspacqService;
	/**
	 * 示波器页面跳转
	 */
    @RequestMapping("/index")
	public ModelAndView tt(ModelAndView model, HttpServletRequest request) {
    	model.setViewName("jsp/windFieldPreview/oscilloscope/index");
    	return model;
    }

	/**
	 * 示波器页面跳转
	 */
    @RequestMapping("/toPage")
	public ModelAndView dspLine(ModelAndView model, HttpServletRequest request) {
    	String deviceId = request.getParameter("device_id");
    	String modbusid = request.getParameter("modbus_type");
//    	String modbusType = request.getParameter("modbus_type");
//    	String type = request.getParameter("type");
    	//根据设备ID获取机侧、网侧波形列表
    	Map<String, List<DeviceInfoDspWaveform>> waveformMap = oscilloscopeService.getDeviceinfoDspWaveformList(deviceId);
    	//获取变流器DSP信息
    	Map<String, Object> dspMap = oscilloscopeService.getDeviceInfoDspMap(deviceId);
    	model.addAllObjects(waveformMap);
    	model.addAllObjects(dspMap);
    	model.addObject("deviceId", deviceId);
    	model.addObject("modbusid", modbusid);
    	model.setViewName("jsp/windFieldPreview/oscilloscope/osc");
    	return model;
	}
    
    /**
	 * 示波器数据获取
	 */
    @RequestMapping("/getData")
    @ResponseBody
	public Map<String,Object> sync(ModelAndView model, HttpServletRequest request) {
    	int pageSize = Integer.parseInt(request.getParameter("pageSize"));
    	int pageNum = Integer.parseInt(request.getParameter("pageNum"));
    	
    	String dsp1WaveformCodes = request.getParameter("dsp1WaveformCodes");
    	String dsp2WaveformCodes = request.getParameter("dsp2WaveformCodes");
    	String[] dspWaveformCodes = {dsp1WaveformCodes, dsp2WaveformCodes};
    	
    	String dsp1Ip = request.getParameter("dsp1Ip");
    	String dsp2Ip = request.getParameter("dsp2Ip");
    	String[] dspIps = {dsp1Ip, dsp2Ip};
    	
    	String dsp1Port = request.getParameter("dsp1Port");
    	String dsp2Port = request.getParameter("dsp2Port");
    	int[] dspPorts = {Integer.parseInt(dsp1Port), Integer.parseInt(dsp2Port)};
//    	获取图表绘制数据
    	return DSPEchartsUtil.getEchartsData(dspIps, dspPorts, dspWaveformCodes, pageNum, pageSize);
    }
    
    @RequestMapping("/start")
    public void start(HttpServletRequest request){
    	String deviceId = request.getParameter("deviceId");
    	String dsp1WaveformCodes = request.getParameter("dsp1WaveformCodes");
    	String dsp2WaveformCodes = request.getParameter("dsp2WaveformCodes");
    	//开始获取变流器数据
    	oscilloscopeService.start(deviceId,  new String[]{dsp1WaveformCodes, dsp2WaveformCodes});
    	
    }
    
    @RequestMapping("/stop")
    public void stop(HttpServletRequest request){
    	String deviceId = request.getParameter("deviceId");
    	//停止获取变流器数据
    	oscilloscopeService.stop(deviceId);
    }
    
    
    @RequestMapping("/DataAcq")
    public void DataAcq(HttpServletRequest request){
    	String employeeId = (String)request.getSession().getAttribute("employeeId");
    	String dspWaveformCodes = request.getParameter("dspWaveformCodes");
    	String dspWaveformName = request.getParameter("dspWaveformName");
    	//开启采集DSP数据线程
    	OscilloscopeWebSocket.StartDataAcq(employeeId,dspWaveformCodes,dspWaveformName);
    	
    }
    
    /**
     * 导出excel文件
     * @param request
     * @param response
     */
    @RequestMapping("/exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
		// Enumeration paramNames = request.getParameterNames();
		// while (paramNames.hasMoreElements()) {
		// String paramName = (String) paramNames.nextElement();
		// System.out.println(paramName);
		// }
		String waveformDataFlags = request.getParameter("waveformDataFlags");
		List<String[]> datas = new ArrayList<String[]>();
		for (String waveformDataFlag : waveformDataFlags.split(",")) {
			datas.add(request.getParameter(waveformDataFlag).split(","));
		}

		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
		String waveformNames = request.getParameter("waveformNames");
		Map<String, Object> header = null;
		int headerIndex = 1;
		for(String waveformName : waveformNames.split(",")){
			header = new HashMap<>();
			header.put("title", waveformName);
			header.put("dataKey", "waveformName" + headerIndex);
			headInfoList.add(header);
			headerIndex++;
		}
		
		Map<String, Object> data = null;
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < datas.get(0).length; i++){
			data = new HashMap<>();
			int dn = 1;
			for(String[] temp : datas){
				data.put("waveformName" + dn, temp[i]);
				dn++;
			}
			dataList.add(data);
		}
		
		String filename = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".xls";
		HSSFWorkbook workbook;
		try {
			workbook = ExcelUtils.exportExcelFilePath("data", headInfoList, dataList);
			// 处理中文文件名
			filename = ExcelUtils.encodeFilename(filename, request);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			OutputStream ouputStream = response.getOutputStream();
			workbook.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
    //查看采集有没有数据
    @RequestMapping("/SelectDspDcq")
    @ResponseBody
    public BaseResponseData SelectDspDcq(HttpServletRequest request,
			HttpServletResponse response){
		BaseResponseData data=new BaseResponseData();
		try {
			HttpSession session = request.getSession();
			String userId = (String) session.getAttribute("employeeId");
			Datadspacp datadspacp = new Datadspacp();
			datadspacp.setEmployeeid(userId);
			List<Datadspacp> datadspacpList = datadspacqService.selectDatadspacpGropby(datadspacp);
			data.setMessage("获取成功");
			data.setCode(WebResponseCode.SUCCESS);
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("dspNum", datadspacpList.size());
			data.setResponseData(resData);
		} catch (Exception e) {
			data.setCode(WebResponseCode.ERROR);
			data.setMessage("获取失败,服务器异常，请稍后重试！");
			HashMap<String, Object> resData = new HashMap<String, Object>();
			resData.put("errorcode", WebResponseCode.EXECUTIONERROR);
			resData.put("errormessage", WebResponseCode.EXECUTIONERRORMESSAGE);
			data.setResponseData(resData);
		}
		return data;
	}

	// 下载示波器波形文件
	@RequestMapping("/downFile")
	public void downFile(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		String userId = (String) session.getAttribute("employeeId");
		List<Datadspacp> datadspacpList = new ArrayList<Datadspacp>();
		Datadspacp datadspacp = new Datadspacp();
		datadspacp.setEmployeeid(userId);
		// datadspacpList = datadspacqService.selectDatadspacpList(datadspacp);
		List<Datadspacp> datadspacpGropby = datadspacqService
				.selectDatadspacpGropby(datadspacp);
		String xmlpath = "";
		for (int i = 0; i < datadspacpGropby.size(); i++) {
			datadspacp.setXh(datadspacpGropby.get(i).getXh());
			datadspacpList = datadspacqService.selectDatadspacpList(datadspacp);
			xmlpath+= CreateExcel(datadspacpList,datadspacpGropby.get(i).getCreate_time())+",";
		}
		//打包路径
		String collectFileUrl =this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/boxing/示波器波形.zip";
		//String collectFileUrl="E:/boxing/示波器波形.zip";
		//文件夹路径
		String folderUrl=xmlpath;
		String downFiles=CollectFiles.collectfile(collectFileUrl,folderUrl);
		
		String device_name = "";
		if (downFiles != null && downFiles != "") {
			device_name = URLDecoder.decode("示波器波形", "UTF-8");
			downFiles = URLDecoder.decode(downFiles, "UTF-8");
		}
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String downloadName = device_name + "_" + df.format(new Date());
		FileDownload.fileDownload(response, request, downFiles, downloadName);
		File dir = new File(this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/boxing/");
		FileUtils.deleteQuietly(dir);
	}

	protected String CreateExcel(List<Datadspacp> datadspacpList,String createTime) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("波形");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式
		HSSFCell cell = row.createCell((short) 0);
		for (int i = 0; i < datadspacpList.size(); i++) {
			cell.setCellValue(datadspacpList.get(i).getData_type());
			cell = row.createCell((short) i + 1);
			sheet.setColumnWidth(i, 256 * 15); 
		}
		// 第五步，写入实体数据 实际应用中这些数据从数据库得到，
		Map <Integer,String[]>  datamap=new Hashtable<Integer,String[]>();
		for(int k=0;k<datadspacpList.size();k++){
			datamap.put(k,datadspacpList.get(k).getDatastr().split(","));
		}
		String dataStr[] = datadspacpList.get(0).getDatastr().split(",");
		int str = dataStr.length;
		for (int i = 0; i < str; i++) {
			row = sheet.createRow((int) i + 1);
			// 第四步，创建单元格，并设置值
			for (int j = 0; j < datadspacpList.size(); j++) {
				row.createCell((short) j).setCellValue((String) datamap.get(j)[i]);
				sheet.setColumnWidth(j, 256 * 15); 
			}

		}
		// 第六步，将文件存到指定位置
		String collectFileUrl =this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/boxing";
		File file = new File(collectFileUrl);
		file.mkdirs();
		createTime=createTime.substring(0,10)+"_"+createTime.substring(11,13)+"-"+createTime.substring(14,16)+"-"+createTime.substring(17,19);
		String xmlpath=collectFileUrl+"/示波器波形_"+createTime+".xls";
		try {
			FileOutputStream fout = new FileOutputStream(xmlpath);
			wb.write(fout);
			fout.close();
			wb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xmlpath;
	}
}
