package com.cn.hy.controller.DevciceDetail;


import java.util.HashMap;
import javax.annotation.Resource;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cn.hy.bean.BaseResponseData;
import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.serviceset.CollectSetService;
import com.cn.hy.service.system.DeviceService;

@Controller
@RequestMapping("/ExpertDatabase")
public class ExpertDatabase {
	@Resource
	private DeviceService deviceService;
	@Resource
	private CollectSetService collectSetService;
	
	@RequestMapping("/lookExpertDatabase")
	@ResponseBody
	public BaseResponseData lookExpertDatabase(@RequestParam("device_id") int device_id,
								 @RequestParam("errormessage") String errormessage){
		BaseResponseData data = new BaseResponseData();
		try {
				DeviceType deviceType = new DeviceType();
				int modbustcp_type = 0;
				// 获取设备基本信息
				Device dev = deviceService.selectDeviceById(device_id);
				deviceType = collectSetService.getDeviceTypeById(dev.getDevice_type_id());
				modbustcp_type = deviceType.getModbus_type();
				String Expetrmessage="";
				String xmlpath =this.getClass().getClassLoader().getResource("").getPath()+ "xmlconfig/expertdatabase";
				if(modbustcp_type==0){  //双馈
					xmlpath+="/双馈变流器专家库.docx"; 
					OPCPackage opcPackage = POIXMLDocument.openPackage(xmlpath);
					POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
					String text2007 = extractor.getText();
					extractor.close();
					String str[] =text2007.split("####");
					for (int i = 0; i < str.length; i++) {
						String twoStr[]=str[i].split("##");
						for (int j = 0; j < twoStr.length; j++) {
							if(twoStr[j].contains(errormessage)){
								Expetrmessage=twoStr[j];
							}
						}
					}
				}
				if(modbustcp_type==1){  //全功率
					xmlpath+="/全功率变流器专家库.docx";
					OPCPackage opcPackage = POIXMLDocument.openPackage(xmlpath);
					POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
					String text2007 = extractor.getText();
					extractor.close();
					String str[] =text2007.split("####");
					for (int i = 0; i < str.length; i++) {
						String twoStr[]=str[i].split("##");
						for (int j = 0; j < twoStr.length; j++) {
							if(twoStr[j].contains(errormessage)){
								Expetrmessage=twoStr[j];
							}
						}
					 }
				}
				if(modbustcp_type==2){ //海上风电
					xmlpath+="/海上风电变流器专家库.docx";
					OPCPackage opcPackage = POIXMLDocument.openPackage(xmlpath);
					POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
					String text2007 = extractor.getText();
					extractor.close();
					String str[] =text2007.split("####");
					for (int i = 0; i < str.length; i++) {
						String twoStr[]=str[i].split("##");
						for (int j = 0; j < twoStr.length; j++) {
							if(twoStr[j].contains(errormessage)){
								Expetrmessage=twoStr[j];
							}
						}
					}
				}
				String okContent="";
				String[] content =Expetrmessage.split("\n");
				for (int i = 0; i < content.length; i++) {
					if(i==0){
						okContent+=content[i]+"\n";
					}else{
						/*if(content[0].contains(content[i])){
							content[i]="";
						}*/
						if(content[i]==content[content.length-1]){
							content[i]="";
						}
						okContent+="\t"+content[i]+"\n";
					}
				}
				data.setMessage("获取成功");
				data.setCode(WebResponseCode.SUCCESS);
				HashMap<String, Object> resData = new HashMap<String, Object>();
				resData.put("Expetrmessage",okContent);
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
