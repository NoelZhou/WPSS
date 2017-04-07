package com.cn.hy.util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载文件
 * 
 * @version
 */
public class FileDownload {

	/**
	 * 文件下载
	 * 
	 * @param response
	 * @param filePath
	 *            //文件完整路径(包括文件名和扩展名)
	 * @param fileName
	 *            //下载后看到的文件名
	 * @return 文件名
	 */
	public static void fileDownload(final HttpServletResponse response, final HttpServletRequest request,
			String filePath, String fileName) throws Exception {
		String str1 = filePath.substring(filePath.indexOf(".") + 1, filePath.length());
		String[] name = str1.split("\\.");
		String gsname = name[name.length - 1];
		String userAgent = request.getHeader("User-Agent");
		String filenewname = (fileName + "." + gsname).toString();
		byte[] data = FileUtil.toByteArray2(filePath);
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			filenewname = java.net.URLEncoder.encode(filenewname, "UTF-8");
		} else {
			// 非IE浏览器的处理：
			filenewname = new String(filenewname.getBytes("UTF-8"), "ISO-8859-1");
		}
		response.setContentType("bin");
		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filenewname + "\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream;charset=UTF-8");
		OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
		outputStream.write(data);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();

	}
}
