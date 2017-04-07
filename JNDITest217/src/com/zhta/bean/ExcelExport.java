package com.zhta.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.zhta.pojo.FormColnum;
import com.zhta.pojo.FormRunData;


public class ExcelExport {
	
	public   String createexcl(List<FormColnum> colnumList, List<FormRunData> runList,String timetype) {
		String rootPath=getClass().getResource("../../../../../").getFile().toString(); 
		File directory = new File(rootPath);//参数为空
		String filename="";
		try {
			String courseFile = directory.getAbsolutePath() ;//获取绝对路径
			String xmlpath = courseFile + "/RunLog/"+timetype;
			new File(xmlpath).mkdirs();//创建存放运行报表的文件夹路径(不存在新建)
			SimpleDateFormat dfe = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		    filename=xmlpath+ dfe.format(new Date())+"_"+timetype+"_运行报表.xlsx";
			System.out.println("创建XML"+filename);
			// 创建一个workbook 对应一个excel应用文件
	        XSSFWorkbook workBook = new XSSFWorkbook();
	        // 在workbook中添加一个sheet,对应Excel文件中的sheet
	        //Sheet名称，可以自定义中文名称
	        XSSFSheet sheet = workBook.createSheet("Sheet1");
	        ExportInternalUtil exportUtil = new ExportInternalUtil(workBook, sheet);
	        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
	        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
	        // 构建表头
	        XSSFRow headRow = sheet.createRow(0);
	        XSSFCell cell = null;
	        // 输出标题
	        cell = headRow.createCell(0);
	        cell.setCellStyle(headStyle);
	        cell.setCellValue("时间");
	        cell = headRow.createCell(1);
	        cell.setCellStyle(headStyle);
	        cell.setCellValue("风机号");
	        String codeStr = "";
	        for (int i = 0; i < colnumList.size(); i++) {
	            cell = headRow.createCell(i+2);
	            cell.setCellStyle(headStyle);
	            cell.setCellValue(colnumList.get(i).getErrorcode());
	            if(codeStr == ""){
			    	codeStr += colnumList.get(i).getCodesx();
			    }else{
			    	codeStr += ","+colnumList.get(i).getCodesx();
			    } 
	        }
	        String[] codeArray = codeStr.split(",");
	        FormRunData formRunData = new FormRunData();
	        for (int j = 0; j < runList.size(); j++) {
	        	XSSFRow bodyRow = sheet.createRow(j + 1);
	        	formRunData = runList.get(j);
	        	cell = bodyRow.createCell(0);
	            cell.setCellStyle(bodyStyle);
	            cell.setCellValue(formRunData.getMintime()+"~"+formRunData.getMaxtime());
	            cell = bodyRow.createCell(1);
	            cell.setCellStyle(bodyStyle);
	            cell.setCellValue(formRunData.getDevice_name());
	        	for (int k = 0; k < codeArray.length; k++) {
	        		cell = bodyRow.createCell(k+2);
	                cell.setCellStyle(bodyStyle);
	                cell.setCellValue(getFieldValueByName(codeArray[k],formRunData).toString());
				}
			}
			// 关闭对象，释放资源
	        FileOutputStream fout = new FileOutputStream(filename);  
	        workBook.write(fout);
            fout.flush();  
            fout.close();
	        workBook.close();
		} catch (Exception e) {
			System.out.println(e);

		}
		  return filename;
}
    /**
     * 运行报表
     * @param colnumList
     * @param runList
     * @param outputStream
     */
    public static void RunExportExcel(List<FormColnum> colnumList, List<FormRunData> runList, ServletOutputStream outputStream) {
    	// 创建一个workbook 对应一个excel应用文件
        XSSFWorkbook workBook = new XSSFWorkbook();
        // 在workbook中添加一个sheet,对应Excel文件中的sheet
        //Sheet名称，可以自定义中文名称
        XSSFSheet sheet = workBook.createSheet("Sheet1");
        ExportInternalUtil exportUtil = new ExportInternalUtil(workBook, sheet);
        XSSFCellStyle headStyle = exportUtil.getHeadStyle();
        XSSFCellStyle bodyStyle = exportUtil.getBodyStyle();
        // 构建表头
        XSSFRow headRow = sheet.createRow(0);
        XSSFCell cell = null;
        // 输出标题
        cell = headRow.createCell(0);
        cell.setCellStyle(headStyle);
        cell.setCellValue("时间");
        cell = headRow.createCell(1);
        cell.setCellStyle(headStyle);
        cell.setCellValue("风机号");
        String codeStr = "";
        for (int i = 0; i < colnumList.size(); i++) {
            cell = headRow.createCell(i+2);
            cell.setCellStyle(headStyle);
            cell.setCellValue(colnumList.get(i).getErrorcode());
            if(codeStr == ""){
		    	codeStr += colnumList.get(i).getCodesx();
		    }else{
		    	codeStr += ","+colnumList.get(i).getCodesx();
		    } 
        }
        
        String[] codeArray = codeStr.split(",");
        FormRunData formRunData = new FormRunData();
        for (int j = 0; j < runList.size(); j++) {
        	XSSFRow bodyRow = sheet.createRow(j + 1);
        	formRunData = runList.get(j);
        	cell = bodyRow.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(formRunData.getCreatetime());
            cell = bodyRow.createCell(1);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(formRunData.getDevice_name());
        	for (int k = 0; k < codeArray.length; k++) {
        		cell = bodyRow.createCell(k+2);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(getFieldValueByName(codeArray[k],formRunData).toString());
			}
		}
        try {
            workBook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
    /**
     * 通过属性获取属性值(利用反射机制)
     * @param fieldName
     * @param o
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object o){      
       try
       {   
           String firstLetter = fieldName.substring(0, 1).toUpperCase();      
           String getter = "get" + firstLetter + fieldName.substring(1);
           Method method = o.getClass().getMethod(getter, new Class[] {});      
           Object value = method.invoke(o, new Object[] {});      
           return value;
       } catch (Exception e){
           System.out.println("属性不存在");      
           return null;      
       }
    } 
    
}
