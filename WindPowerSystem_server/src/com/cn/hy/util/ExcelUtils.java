package com.cn.hy.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.util.StringUtils;
//import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;

/**
 * poi 导出excel 工具类
 */
@SuppressWarnings({ "static-access", "deprecation" })
public class ExcelUtils {

	/**
	 * 1.创建 workbook
	 * 
	 * @return
	 */
	public static HSSFWorkbook getHSSFWorkbook() {
		return new HSSFWorkbook();
	}

	/**
	 * 2.创建 sheet
	 * 
	 * @param hssfWorkbook
	 * @param sheetName
	 *            sheet 名称
	 * @return
	 */
	public static HSSFSheet getHSSFSheet(HSSFWorkbook hssfWorkbook, String sheetName) {
		return hssfWorkbook.createSheet(sheetName);
	}

	/**
	 * 3.写入表头信息
	 * 
	 * @param hssfWorkbook
	 * @param hssfSheet
	 * @param headInfoList
	 *            List<Map<String, Object>> key: title 列标题 columnWidth 列宽
	 *            dataKey 列对应的 dataList item key
	 */
	public static void writeHeader(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, List<Map<String, Object>> headInfoList) {
		HSSFRow row = hssfSheet.createRow(0);
		HSSFCell cell = null;
		Map<String, Object> headInfo = null;
		
		/**
		 * 添加表头
		 */
		for (int i = 0, len = headInfoList.size(); i < len; i++) {
			headInfo = headInfoList.get(i);
			cell = row.createCell(i);
			cell.setCellValue(headInfo.get("title").toString());
			cell.setCellStyle(ExcelUtils.titleCellStyle(hssfWorkbook, (short) 0, (short) 0, (short) 0));
			if (headInfo.containsKey("columnWidth")) {
				hssfSheet.setColumnWidth(i, (short) ((Integer) headInfo.get("columnWidth") * 512));//汉字是512，数字是256.
			}
		}
	}

	/**
	 * 4.写入内容部分
	 * 
	 * @param hssfWorkbook
	 * @param hssfSheet
	 * @param startIndex
	 *            从1开始，多次调用需要加上前一次的dataList.size()
	 * @param headInfoList
	 *            List<Map<String, Object>> key: title 列标题 columnWidth 列宽
	 *            dataKey 列对应的 dataList item key
	 * @param dataList
	 */
	public static void writeContent(HSSFWorkbook hssfWorkbook, HSSFSheet hssfSheet, int startIndex,
			List<Map<String, Object>> headInfoList, List<Map<String, Object>> dataList) {
		Map<String, Object> headInfo = null;
		HSSFRow row = null;
		HSSFCell cell = null;
		// 处理数据
		Map<String, Object> dataItem = null;
		Object v = null;
		HSSFCellStyle contentCellStyle = ExcelUtils.contentCellStyle(hssfWorkbook, (short)0, (short)0, (short)0);
		for (int i = 0, rownum = startIndex, len = (startIndex + dataList.size()); rownum < len; i++, rownum++) {
			row = hssfSheet.createRow(rownum);
			row.setHeightInPoints(16);
			
			
			
			dataItem = dataList.get(i);
			for (int j = 0, jlen = headInfoList.size(); j < jlen; j++) {
				headInfo = headInfoList.get(j);
				cell = row.createCell(j);
				v = dataItem.get(headInfo.get("dataKey").toString());

				if (v instanceof String) {
					cell.setCellValue((String) v);
				} else if (v instanceof Boolean) {
					cell.setCellValue((Boolean) v);
				} else if (v instanceof Calendar) {
					cell.setCellValue((Calendar) v);
				} else if (v instanceof Double) {
					cell.setCellValue((Double) v);
				} else if (v instanceof Integer || v instanceof Long || v instanceof Short || v instanceof Float) {
					cell.setCellValue(Double.parseDouble(v.toString()));
				} else if (v instanceof HSSFRichTextString) {
					cell.setCellValue((HSSFRichTextString) v);
				} else {
					cell.setCellValue(v.toString());
				}
				cell.setCellStyle(contentCellStyle);
			}
		}
	}

	/**
	 * 导出本地
	 * @param hssfWorkbook
	 * @param filePath
	 * @throws IOException
	 */
	public static void write2FilePath(HSSFWorkbook hssfWorkbook, String filePath) throws IOException {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(filePath);
			hssfWorkbook.write(fileOut);
		} finally {
			if (fileOut != null) {
				fileOut.close();
			}
		}
	}

	/**
	 * 导出excel code example:
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param filePath
	 *            文件存储路径， 如：f:/a.xls
	 * @param headInfoList
	 *            List<Map<String, Object>> key: title 列标题 columnWidth 列宽
	 *            dataKey 列对应的 dataList item key
	 * @param dataList
	 *            List<Map<String, Object>> 导出的数据
	 * @throws java.io.IOException
	 *
	 */
	public static void exportExcel2FilePath(String sheetName, String filePath, List<Map<String, Object>> headInfoList,
			List<Map<String, Object>> dataList) throws IOException {
		// 1.创建 Workbook
		HSSFWorkbook hssfWorkbook = ExcelUtils.getHSSFWorkbook();
		// 2.创建 Sheet
		HSSFSheet hssfSheet = ExcelUtils.getHSSFSheet(hssfWorkbook, sheetName);
		// 3.写入 head
		ExcelUtils.writeHeader(hssfWorkbook, hssfSheet, headInfoList);
		// 4.写入内容
		ExcelUtils.writeContent(hssfWorkbook, hssfSheet, 1, headInfoList, dataList);
		// 5.保存文件到filePath中
		ExcelUtils.write2FilePath(hssfWorkbook, filePath);
	}
	
	/**
	 * 导出excel code example:
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param filePath
	 *            文件存储路径， 如：f:/a.xls
	 * @param headInfoList
	 *            List<Map<String, Object>> key: title 列标题 columnWidth 列宽
	 *            dataKey 列对应的 dataList item key
	 * @param dataList
	 *            List<Map<String, Object>> 导出的数据
	 * @throws java.io.IOException
	 *
	 */
	public static HSSFWorkbook exportExcelFilePath(String sheetName, List<Map<String, Object>> headInfoList,
			List<Map<String, Object>> dataList) throws IOException {
		// 1.创建 Workbook
		HSSFWorkbook hssfWorkbook = ExcelUtils.getHSSFWorkbook();
		// 2.创建 Sheet
		HSSFSheet hssfSheet = ExcelUtils.getHSSFSheet(hssfWorkbook, sheetName);
		// 3.写入 head
		ExcelUtils.writeHeader(hssfWorkbook, hssfSheet, headInfoList);
		// 4.写入内容
		ExcelUtils.writeContent(hssfWorkbook, hssfSheet, 1, headInfoList, dataList);
		return hssfWorkbook;
	}
	
	/**
	 * 表头样式
	 * @param hssfWorkbook 工作簿
	 * @param alignment 单元格对齐样式，HSSFCellStyle.ALIGN_CENTER（居中）；HSSFCellStyle.ALIGN_LEFT（居左）；HSSFCellStyle.ALIGN_RIGHT（居右）
	 * @param fontSize 行高
	 * @param groundColor 背景色 IndexedColors.GREY_40_PERCENT.getIndex()（默认）
	 * @return
	 */
	public static HSSFCellStyle titleCellStyle(HSSFWorkbook hssfWorkbook, short fontSize, short alignment, short groundColor) {
		fontSize = fontSize != 0 ? fontSize : 20;
		alignment = alignment != 0 ? alignment : HSSFCellStyle.ALIGN_CENTER;
		groundColor = groundColor != 0 ? groundColor : IndexedColors.GREY_40_PERCENT.getIndex();
		
		HSSFFont font = hssfWorkbook.createFont();
		font.setFontHeightInPoints(fontSize); //行高
		font.setBoldweight(font.BOLDWEIGHT_BOLD); //加粗
		
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(alignment); //居中
		
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(groundColor);
		
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		return cellStyle;
	}
	
	/**
	 * 内容样式
	 * @param hssfWorkbook 工作簿
	 * @param alignment 单元格对齐样式，HSSFCellStyle.ALIGN_CENTER（居中）；HSSFCellStyle.ALIGN_LEFT（居左）；HSSFCellStyle.ALIGN_RIGHT（居右）
	 * @param fontSize 字体大小
	 * @param groundColor 背景色 IndexedColors.GREY_40_PERCENT.getIndex()（默认）
	 * @return
	 */
	public static HSSFCellStyle contentCellStyle(HSSFWorkbook hssfWorkbook, short alignment, short fontSize, short groundColor) {
		
		fontSize = fontSize != 0 ? fontSize : 14;
		alignment = alignment != 0 ? alignment : HSSFCellStyle.ALIGN_CENTER;
		
		HSSFFont font = hssfWorkbook.createFont();
		font.setFontHeightInPoints(fontSize);
		
		HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(alignment); //居中
		
		if(groundColor != 0){
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			cellStyle.setFillForegroundColor(groundColor);
		}
		
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
		return cellStyle;
	}
	
	/** 
     * 设置下载文件中文件的名称 
     *  
     * @param filename 
     * @param request 
     * @return 
     */  
	public static String encodeFilename(String filename, HttpServletRequest request) {
		/**
		 * 获取客户端浏览器和操作系统信息 在IE浏览器中得到的是：User-Agent=Mozilla/4.0 (compatible; MSIE
		 * 6.0; Windows NT 5.1; SV1; Maxthon; Alexa Toolbar)
		 * 在Firefox中得到的是：User-Agent=Mozilla/5.0 (Windows; U; Windows NT 5.1;
		 * zh-CN; rv:1.7.10) Gecko/20050717 Firefox/1.0.6
		 */
		String agent = request.getHeader("USER-AGENT");
		try {
			if ((agent != null) && (-1 != agent.indexOf("MSIE"))) {
				String newFileName = URLEncoder.encode(filename, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", "%20");
				if (newFileName.length() > 150) {
					newFileName = new String(filename.getBytes("GB2312"), "ISO8859-1");
					newFileName = StringUtils.replace(newFileName, " ", "%20");
				}
				return newFileName;
			}
//			if ((agent != null) && (-1 != agent.indexOf("Mozilla")))
//				return MimeUtility.encodeText(filename, "UTF-8", "B");

			return filename;
		} catch (Exception ex) {
			return filename;
		}
	}

	public static void main(String[] args) throws IOException {
		List<Map<String, Object>> headInfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		itemMap.put("title", "序号1");
		itemMap.put("columnWidth", 10);
		itemMap.put("dataKey", "XH1");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "序号2");
		itemMap.put("columnWidth", 20);
		itemMap.put("dataKey", "XH2");
		headInfoList.add(itemMap);

		itemMap = new HashMap<String, Object>();
		itemMap.put("title", "序号3");
		itemMap.put("columnWidth", 10);
		itemMap.put("dataKey", "XH3");
		headInfoList.add(itemMap);

		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<String, Object> dataItem = null;
		for (int i = 0; i < 100; i++) {
			dataItem = new HashMap<String, Object>();
			dataItem.put("XH1", "data" + i);
			dataItem.put("XH2", 88888888f);
			dataItem.put("XH3", "脉兜V5..");
			dataList.add(dataItem);
		}
		ExcelUtils.exportExcel2FilePath("test sheet 1", "D:\\customer2.xls", headInfoList, dataList);
	}
}