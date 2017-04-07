package com.cn.hy.controller.firmwareUpgrade;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileReadUtils {
	/**
	 * 读取源文件内容
	 * 
	 * @param filename
	 *            String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	 */
	public static byte[] readFile(String filename) throws IOException {

		File file = new File(filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		int  len = (int)file.length();
		short [] tempbyteu=new short[len];
		byte[] bytes = new byte[len];
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
		int read = bufferedInputStream.read(bytes, 0, bytes.length);
		for(int i=0;i<read;i++){
			if(bytes[i]<0){
				tempbyteu[i]=(short)(bytes[i]&0x00ff);
				bytes[i]=(byte) (bytes[i]&0x00ff);
			}else{
				tempbyteu[i]=bytes[i];
			}
		}
		bufferedInputStream.close();
		return bytes;
	}
	public static void readFile_str(String filename) throws IOException {
		StringBuffer txtContent=new StringBuffer();
		File file = new File(filename);
		if (filename == null || filename.equals("")) {
			throw new NullPointerException("无效的文件路径");
		}
		Reader r=new FileReader(file);
	      BufferedReader br=new BufferedReader(r);
	      String str=br.readLine();
	      while(str!=null){
	       txtContent.append(str+"\r\n");
	       str=br.readLine();
	       System.out.println(str);
	      }
	      br.close();
	      r.close();
	      System.out.println(txtContent);
	}

	/**
	 * 将数据写入文件
	 * 
	 * @param data
	 *            byte[]
	 * @throws IOException
	 */
	public static void writeFile(byte[] data, String filename) throws IOException {
		File file = new File(filename);
		file.getParentFile().mkdirs();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
		bufferedOutputStream.write(data);
		bufferedOutputStream.close();

	}

	/**
	 * 从jar文件里读取class
	 * 
	 * @param filename
	 *            String
	 * @throws IOException
	 * @return byte[]
	 */
	public byte[] readFileJar(String filename) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(
				getClass().getResource(filename).openStream());
		int len = bufferedInputStream.available();
		byte[] bytes = new byte[len];
		int r = bufferedInputStream.read(bytes);
		if (len != r) {
			bytes = null;
			throw new IOException("读取文件不正确");
		}
		bufferedInputStream.close();
		return bytes;
	}

	/**
	 * 读取网络流，为了防止中文的问题，在读取过程中没有进行编码转换，而且采取了动态的byte[]的方式获得所有的byte返回
	 * 
	 * @param bufferedInputStream
	 *            BufferedInputStream
	 * @throws IOException
	 * @return byte[]
	 */
	public byte[] readUrlStream(BufferedInputStream bufferedInputStream) throws IOException {
		byte[] bytes = new byte[100];
		byte[] bytecount = null;
		int n = 0;
		int ilength = 0;
		while ((n = bufferedInputStream.read(bytes)) >= 0) {
			if (bytecount != null){
				ilength = bytecount.length;
			}
			byte[] tempbyte = new byte[ilength + n];
			if (bytecount != null) {
				System.arraycopy(bytecount, 0, tempbyte, 0, ilength);
			}

			System.arraycopy(bytes, 0, tempbyte, ilength, n);
			bytecount = tempbyte;

			if (n < bytes.length){
				break;
			}
		}
		return bytecount;
	}
	
	public static int byte2int(byte[] res) {
		// 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000

		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00) // | 表示安位或
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	} 
}
