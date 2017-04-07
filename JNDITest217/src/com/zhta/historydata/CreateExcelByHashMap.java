package com.zhta.historydata;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;


import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class CreateExcelByHashMap {
	 /**
	  * 使用集合来遍历data
	  * @param args
	  */
	public String createexcl(List<Map<String, String>> datalist,String name,String device_name) {
		//System.out.println("创建XML"+);
		String rootPath=getClass().getResource("../../../../../").getFile().toString();  
		System.out.println("创建XML1:"+rootPath);
		File directory = new File(rootPath);//参数为空
		String filename="";
		String str[]=null;
		String strnew[]=null;
		Collection<String>  strtmp=null;
		try {
			String courseFile = directory.getAbsolutePath() ;//获取绝对路径
			String xmlpath = courseFile + "/ExcelLog/";
		  //File file = new File(xmlpath);
			//String path4  =CreateExcel.class.getResource("/").getPath()+"Excel/";
			SimpleDateFormat dfe = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		    filename=xmlpath+device_name+"_"+name+"_"+ dfe.format(new Date())+"_lbgl.xls";
			System.out.println("创建XML"+filename);
			// 创建一个可写入的excel文件对象
			WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
			// 使用第一张工作表，将其命名为“午餐记录”
			WritableSheet sheet = workbook.createSheet("事件历史记录文件", 0);
			for(int i=0;i<datalist.size();i++){
				strtmp=datalist.get(i).values();
				String tmp=strtmp.toString();
				str=tmp.substring(1,tmp.length()-1).split(",");
				strnew=Arrays.copyOfRange(str, 0, str.length-1);
				for(int j=0;j<strnew.length;j++){
					Label label = new Label(j, i,strnew[j]);
					sheet.addCell(label);
					System.out.print(strnew[j].length());					
				}
			}
			// 关闭对象，释放资源
			workbook.write();
			workbook.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		  return filename;
	}

}
