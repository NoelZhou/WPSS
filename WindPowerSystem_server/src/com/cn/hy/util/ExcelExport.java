package com.cn.hy.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cn.hy.pojo.reportstatistics.FormRunData;
import com.cn.hy.pojo.reportstatistics.RowDevice;
import com.cn.hy.pojo.serviceset.FormColnum;


public class ExcelExport {
	/**
	 * 故障 告警 报表
	 * @param objList
	 * @param map
	 * @param outputStream
	 */
    public static void ExportExcel(List<RowDevice> objList, Map<String, List<Integer>> map, ServletOutputStream outputStream) {
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
        cell.setCellValue("故障名称");
        for (int i = 0; i < objList.size(); i++) {
            cell = headRow.createCell(i+1);
            cell.setCellStyle(headStyle);
            cell.setCellValue(objList.get(i).getDevice_name());
        }
        int j = 0;
        for (String key : map.keySet()) {
        	XSSFRow bodyRow = sheet.createRow(j + 1);
        	
        	cell = bodyRow.createCell(0);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(key);
        	List<Integer> numList = map.get(key);
        	for (int k = 0; k < numList.size(); k++) {
        		cell = bodyRow.createCell(k+1);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(numList.get(k));
			}
        	j++;
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
    /*
    *下载波形文件 
    */
    public static void ExportExcelOsc(List<String> objList, Map<Integer, String> map, ServletOutputStream outputStream) {
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
        //cell.setCellValue("故障名称");
        for (int i = 0; i < objList.size(); i++) {
            cell = headRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(objList.get(i));
        }
        String numListtmp[] = map.get(0).split(",");
        int len=numListtmp.length;
        for(int i=0;i<len;i++){
        	//String str16="";
        	XSSFRow bodyRow = sheet.createRow(i + 1);
        	cell = bodyRow.createCell(0);
            cell.setCellStyle(bodyStyle);
            for(int j=0;j<map.keySet().size();j++){
            	String numList[] = map.get(j).split(",");
        		cell = bodyRow.createCell(j);
                cell.setCellStyle(bodyStyle);
                cell.setCellValue(numList[i]);
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
        	sheet.setColumnWidth(0, (formRunData.getMintime().length()+formRunData.getMaxtime().length())*256);
            cell.setCellStyle(bodyStyle);
            cell.setCellValue(formRunData.getMintime()+"~"+formRunData.getMaxtime());
            cell = bodyRow.createCell(1);
            sheet.setColumnWidth(1, (formRunData.getDevice_name().length())*2*256);
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
     * 通过属性获取属性值
     * @param fieldName
     * @param o
     * @return
     */
    private static Object getFieldValueByName(String fieldName, Object o)   
    {      
       try   
       {      
           String firstLetter = fieldName.substring(0, 1).toUpperCase();      
           String getter = "get" + firstLetter + fieldName.substring(1);      
           Method method = o.getClass().getMethod(getter, new Class[] {});      
           Object value = method.invoke(o, new Object[] {});      
           return value;      
       } catch (Exception e)   
       {      
           System.out.println("属性不存在");      
           return null;      
       }      
    } 
    
    public static void main(String[] args) {
    	FormRunData formRunData = new FormRunData();
    	String aaa =  getFieldValueByName("dwdy",formRunData)+"";
    	System.out.println(aaa);
	}
    
}
