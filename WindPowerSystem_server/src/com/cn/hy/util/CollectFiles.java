package com.cn.hy.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * @author wuhw
 *多文件打包
 */
public class CollectFiles {
		public static String collectfile(String collectFileUrl,String folderUrl) throws Exception{
			byte[] buffer = new byte[1024];
			// 生成的ZIP文件名为Demo.zip
			String strZipName = collectFileUrl;
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					strZipName));
			// 需要同时下载的两个文件result.txt ，source.txt
			String folderNameUrl[]=folderUrl.split(",");
			
			/*File[] file1 = { new File("e:/a.txt"), new File("e:/b.txt"),
					new File("e:/aa.txt"), new File("e:/bb.txt") };*/
			File file2=null;
			for (int i = 0; i < folderNameUrl.length; i++) {
				file2=new File(folderNameUrl[i]);
				FileInputStream fis = new FileInputStream(file2);
				out.putNextEntry(new ZipEntry(file2.getName()));
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
			}
			
			/*for (int i = 0; i < file1.length; i++) {
				FileInputStream fis = new FileInputStream(file1[i]);
				out.putNextEntry(new ZipEntry(file1[i].getName()));
				int len;
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.closeEntry();
				fis.close();
			}*/
			out.close();
			return strZipName;
		}
}
