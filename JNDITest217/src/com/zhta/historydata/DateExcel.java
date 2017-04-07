package com.zhta.historydata;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DateExcel {
		public static String createexcl(List<String[]> datalist) {
			//System.out.println("创建XML"+);
			File directory = new File("");//参数为空
			String filename="";
			String str[]=null;
			try {
				String courseFile = directory.getCanonicalPath() ;
				String xmlpath = courseFile + "/WebContent/ExcelLog/";
				SimpleDateFormat dfe = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			    filename=xmlpath+""+"_"+ dfe.format(new Date())+".xls";
				System.out.println("创建XML"+filename);
				// 创建一个可写入的excel文件对象
				WritableWorkbook workbook = Workbook.createWorkbook(new File(filename));
				// 使用第一张工作表，将其命名为“午餐记录”
				WritableSheet sheet = workbook.createSheet("事件历史记录文件", 0);
				for(int i=0;i<datalist.size();i++){
					str=datalist.get(i);
					for(int j=0;j<str.length;j++){
						Label label = new Label(i, j,str[j]);
						sheet.addCell(label);
						System.out.print(str[j].length());						
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
