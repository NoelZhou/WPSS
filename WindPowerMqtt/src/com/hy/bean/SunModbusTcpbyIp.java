package com.hy.bean;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
/**
* @author 关龙锋 
* @date : 2016年7月11日 下午3:42:57
*/
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.util.concurrent.Uninterruptibles;

public class SunModbusTcpbyIp {

	private static final Log LOGGER = LogFactory.getLog(SunModbusTcpbyIp.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// 测试地址,0,1表示非标，2表示海上风电，modbuslength为读取字节长度
		String ip = "192.168.68.238";
		int modbuslength = 3000;
		// 起始和场长度都是字的长度 22个开始 就是 查到23-， 长度不包括 报文 解析说明字。
		// getSunModbusTcpStr(ip,502,0,22, 686);
		// getSunModbusTcpStr_BZ(ip, modbuslength);
	//	ReadSunModbusTcpStrAll(ip, 502, 2, 12788,100, 100);
	}
	public String[] sendonload_zthf(String [] code) {
		Socket socket = null;
		String strj[] = new String [1024];
		String strjnew []=new String [1024];
		byte[] recData = new byte[1024];
		try {
			socket = new Socket("192.168.69.75", 1883);
		/*	String[] strox = { "10", "18", "00", "06", "4D", "51", "49", "73", "64", "70", "03", "02", "00", "78", "00",
					"0A", "41", "31", "34", "30", "35", "30", "36", "39", "38", "37" };*/
			
		 //String [] codetmp={"82","24","00","FF","00","11","53","4E","41","31","34","30","35","30","36","39","38","37","2F","54","69","6D","65","01","00","0B","50","43","2f","50","61","73","73","77","6f","72","64","01"};
			byte data[] = new byte[code.length];
			for (int i = 0; i < code.length; i++) {
				byte b = Integer.valueOf(code[i], 16).byteValue();
				data[i] = b;
			}	
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(data); // 发送
			socket.setSoTimeout(2000);
			int totalBytesRcvd= in.read(recData);// 接收
			for (int i = 0; i < totalBytesRcvd; i++) {
				int str = recData[i];
				strj[i]=Integer.toHexString(str);
			}
			if(totalBytesRcvd!=-1){
			String strjnewtmp []=new String [totalBytesRcvd];
			System.arraycopy(strj, 0, strjnewtmp, 0, totalBytesRcvd);  	
			strjnew=strjnewtmp;
			}
			
			while(true){
				socket.setSoTimeout(2000);
				int totalBytesRcvdnew= in.read(recData);// 接收
				for (int i = 0; i < totalBytesRcvdnew; i++) {
					int str = recData[i];
					strj[i]=Integer.toHexString(str);
				}
				if(totalBytesRcvdnew!=-1){
				String strjnewtmp []=new String [totalBytesRcvdnew];
				System.arraycopy(strj, 0, strjnewtmp, 0, totalBytesRcvdnew);  	
				strjnew=strjnewtmp;
				}
				System.out.println(strjnew);
			}
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strjnew;
	} 
	/**
	 * 发送登陆指令
	 * @param code
	 * @param socket
	 * @return
	 */
	public void sendack(String [] code,Socket socket) {
		System.out.println("发送订阅回执："+MqttUtil.sztostr(code));
		try {
			byte data[] = new byte[code.length];
			for (int i = 0; i < code.length; i++) {
				byte b = Integer.valueOf(code[i], 16).byteValue();//16进制字符转10进制字节数
				data[i] = b;
			}	
			OutputStream out = socket.getOutputStream();
			out.write(data); // 发送
		} catch (IOException e) {
		}
	} 

	/**
	 * 发送登陆指令
	 * @param code
	 * @param socket
	 * @return
	 */
	public String[] sendonload(String [] code,Socket socket) {
		System.out.println("指令长度为"+(code.length-2));
		int codeleg=code.length-2;
		//长度是2个字节
	if(codeleg!=0){
		if(codeleg>127&&codeleg<16383){
		   //先保存数据
			String[] tmp=new String[code.length+1];
			System.arraycopy(code,2, tmp, 3, codeleg);
		   int code_h=codeleg/128;
		   int code_l=codeleg%128;
		   tmp[0]=code[0];
		   String tmp_l=MqttUtil.get2HexString(Integer.toHexString(code_l+128));
		   tmp[1]=tmp_l;
		   tmp[2]=MqttUtil.get2HexString(Integer.toHexString(code_h));
		   code=tmp;
		}//长度为3个字节//一般长度不会用到
		else if(codeleg>16383&&codeleg<2097151){
			
		}//长度为4个字节//一般长度不会用到
		else  if(codeleg>2097151&&codeleg<268435455){
			
		}//长度为1个字节
		else {
			String ll=Integer.toHexString(codeleg);
			code[1]=MqttUtil.get2HexString(ll);
		}
	   }
		System.out.println("发送运行参数："+MqttUtil.sztostr(code));
		String strj[] = new String [1024];
		String strjnew []=null;
		byte[] recData = new byte[1024];
		try {
			byte data[] = new byte[code.length];
			for (int i = 0; i < code.length; i++) {
				byte b = Integer.valueOf(code[i], 16).byteValue();//16进制字符转10进制字节数
				data[i] = b;
			}	
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(data); // 发送
			int totalBytesRcvd= in.read(recData);// 接收
			if(totalBytesRcvd==-1){
//				Socket sk;
//				try {
//					sk = new Socket("192.168.68.0", 502);
//					sk.setSoTimeout(3000);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				Uninterruptibles.sleepUninterruptibly(3000, TimeUnit.MILLISECONDS);
				totalBytesRcvd= in.read(recData);// 接收
			}
			
			
			for (int i = 0; i < totalBytesRcvd; i++) {
				int str = recData[i];
				  strj[i]=Integer.toHexString(str);
					String ss=Integer.toHexString(str);
					if(ss.length()>=2){
						strj[i] = ss.substring(ss.length()-2, ss.length());	
					}else{
						strj[i] =  "0" + ss;
					}
			}
			if(totalBytesRcvd!=-1){
			String strjnewtmp []=new String [totalBytesRcvd];
				if (strj[1].equals("04")) {
					int totalBytesRcvdtmp = in.read(recData);// 接收
					strjnewtmp = new String[totalBytesRcvdtmp];
					for (int i = 0; i < totalBytesRcvdtmp; i++) {
						int str = recData[i];
						String ss = Integer.toHexString(str);
						if(ss.length()>=2){
							strj[i] = ss.substring(ss.length()-2, ss.length());	
						}else{
							strj[i] =  "0" + ss;
						}
					}
					System.arraycopy(strj, 0, strjnewtmp, 0, totalBytesRcvdtmp);
					String strfh = sztostr(strj);
					System.out.println("设备订阅成功返回：" + strfh);
				} else {
					System.arraycopy(strj, 0, strjnewtmp, 0, totalBytesRcvd);
				}
				strjnew = strjnewtmp;
			}
		} catch (IOException e) {
			return  null;
		}
		return strjnew;
	} 
	
	/**
	 * 发送二次订阅指令
	 * @param code
	 * @param socket
	 * @return
	 */
	public String[] sendonloaddy(String [] code,Socket socket) {
		System.out.println("指令长度为"+(code.length-2));
		int codeleg=code.length-2;
		//长度是2个字节
		if(codeleg!=0){
			if(codeleg>127&&codeleg<16383){
			   //先保存数据
				String[] tmp=new String[code.length+1];
				System.arraycopy(code,2, tmp, 3, codeleg);
			   int code_h=codeleg/128;
			   int code_l=codeleg%128;
			   tmp[0]=code[0];
			   String tmp_l=MqttUtil.get2HexString(Integer.toHexString(code_l+128));
			   tmp[1]=tmp_l;
			   tmp[2]=MqttUtil.get2HexString(Integer.toHexString(code_h));
			   code=tmp;
			}//长度为3个字节//一般长度不会用到
			else if(codeleg>16383&&codeleg<2097151){
			
			}//长度为4个字节//一般长度不会用到
			else  if(codeleg>2097151&&codeleg<268435455){
			
			}//长度为1个字节
			else {
				String ll=Integer.toHexString(codeleg);
				code[1]=MqttUtil.get2HexString(ll);
			}
	   }
		System.out.println("发送运行参数："+MqttUtil.sztostr(code));
		String strj[] = new String [1024];
		String strjnew []=null;
		byte[] recData = new byte[1024];
		try {
			byte data[] = new byte[code.length];
			for (int i = 0; i < code.length; i++) {
				byte b = Integer.valueOf(code[i], 16).byteValue();//16进制字符转10进制字节数
				data[i] = b;
			}	
			
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			out.write(data); // 发送
			int totalBytesRcvd= in.read(recData);// 接收
			if(totalBytesRcvd==-1){
				Uninterruptibles.sleepUninterruptibly(3000, TimeUnit.MILLISECONDS);
				totalBytesRcvd= in.read(recData);// 接收
			}
			for (int i = 0; i < totalBytesRcvd; i++) {
				int str = recData[i];
				  strj[i]=Integer.toHexString(str);
					String ss=Integer.toHexString(str);
					if(ss.length()>=2){
						strj[i] = ss.substring(ss.length()-2, ss.length());	
					}else{
						strj[i] =  "0" + ss;
					}
			}
			if(totalBytesRcvd!=-1){
			String strjnewtmp []=new String [totalBytesRcvd];
			System.arraycopy(strj, 0, strjnewtmp, 0, totalBytesRcvd);
			strjnew = strjnewtmp;
			}
		} catch (IOException e) {
			return  null;
		}
		return strjnew;
	} 

public String ReadSunModbusTcpStrAll(String ip, int port, int d_type, int startaddr, int length,
			int modbuslength) {
	 	long aa = System.currentTimeMillis();
		String bString = "";
		String shortstr = "";
		int num = (int) Math.ceil(modbuslength / length);
		int numy = (int) Math.ceil(modbuslength % length);
		if (numy != 0) {
			num++;
		}
		try {
			String server = ip; // 服务器IP
			int servPort = port;// 端口
			// 发送的数据
			// 参数说明：前6个00是标准modbus协议，简称标准位，
			// 01：为tcp协议的标示，在串口中起作用
			// 04标示0x03/04指令，可以读
			// 00 00读取的起始位置
			// 02AE读取的长度 686 双馈：684,全功率：682
			String[] strox = new String[12];
			// 97 79 00 00 00 06 04 03 31 F5 00 64每次读取最多读取256个字节
			// 标准的modbus,为了方便我们每次读取100个字即h64
			if (d_type == 0 || d_type == 1) {
				String[] strox01 = { "00", "00", "00", "00", "00", "00", "01", "04", "00", "00", "02", "AE" };
				strox = strox01;
			} else if (d_type == 2) {
				String[] strox02 = { "97", "79", "00", "00", "00", "06", "04", "03", "31", "F5", "00", "64" };
				strox = strox02;
			} else {
				return "没有该协议解析方式。";
			}
			// 初始化协议起始地址
			String startaddrstr = Integer.toHexString(startaddr) + "";
			strox[8] = get4HexString(startaddrstr).split(",")[0];
			strox[9] = get4HexString(startaddrstr).split(",")[1];
			// 初始化长度赋值
			String lengthstr = Integer.toHexString(length) + "";
			strox[10] = get4HexString(lengthstr).split(",")[0];
			strox[11] = get4HexString(lengthstr).split(",")[1];
			int reclength = length * 2;
			if(d_type==2){
				reclength = length * 2+9;
			}else{
				reclength = length * 2;
			}
			for (int j = 0; j < num; j++) {
				Socket socket = new Socket(ip, servPort);
				// 更新起始地址信息
				int newstart = startaddr + j * length;
				String newstarthall = Integer.toHexString(newstart);
				strox[8] = get4HexString(newstarthall).split(",")[0];
				strox[9] = get4HexString(newstarthall).split(",")[1];
				// 更新读取长度
				if (j == num - 1) {
					int newlength = modbuslength - length * j;
					String newlengthstr = Integer.toHexString(newlength) + "";
					strox[10] = get4HexString(newlengthstr).split(",")[0];
					strox[11] = get4HexString(newlengthstr).split(",")[1];
					if(d_type==2){
						reclength = newlength * 2+9;
					}else{
						reclength = newlength * 2;
					}
					
				}
				String strj = "";
				byte data[] = new byte[12];

				for (int i = 0; i < strox.length; i++) {
					byte b = Integer.valueOf(strox[i], 16).byteValue();
					data[i] = b;
				}
				// byte的大小为8bits而int的大小为32bits 协议接收长度为查询长度的2倍
	         	byte[] recData = new byte[reclength];// 接收数据缓冲 海上风电1191个字
				InputStream in = socket.getInputStream();
				OutputStream out = socket.getOutputStream();
			   out.write(data); // 发送
				// 接收数据 初始化 数据接收长度
				int totalBytesRcvd = reclength; // Total bytes received so far
			   totalBytesRcvd = in.read(recData);// 接收
			  //  LOGGER.info("第" + j + "次返回字节长度为：" + totalBytesRcvd);
				//System.out.println("返回长度："+totalBytesRcvd);
				if(totalBytesRcvd==-1){
					return totalBytesRcvd+"";
				}
				for (int i = 0; i < totalBytesRcvd; i++) {
					byte str = recData[i];
					if (i == totalBytesRcvd - 1) {
						strj += str;
					} else {
						strj += str + ",";
					}

				}
				 //LOGGER.info("开始合并字节处理......");
				String shortstrone = getshortstr(recData,d_type);
				// LOGGER.info("返回合并字为：" + shortstrone);
                 //  System.out.println(shortstrone.split(",").length);
				if (j == num - 1) {
					bString += strj;
					shortstr += shortstrone;
				} else {
					bString += strj + ",";
					shortstr += shortstrone + ",";
				}
			
				socket.close();
			}	
		} catch (Exception e) {
			System.out.println("与ip：" + ip + "通讯失败！");
		}

		return shortstr;
	}

    public static String WriteSunModbusTcpStrAll(String ip, int port, int d_type,int addr,String paramestr) {
			String strj = "";
			try {
				//int d_type=0;
				String server = ip; // 服务器IP
				int servPort = port;// 端口
				// 发送的数据
				// 参数说明：前6个00是标准modbus协议，简称标准位，
				// 01：为tcp协议的标示，在串口中起作用
				// 04标示0x03/04指令，可以读
				// 00 00读取的起始位置
				// 02AE读取的长度 686 双馈：684,全功率：682
				String  parame[] =paramestr.split(",");
				int paramesize=parame.length;
				int lent=12+paramesize*2;
				if(d_type==2){
					 lent=13+paramesize*2;
				}
				String[] strox = new String[lent];
				if (d_type == 0 || d_type == 1) {
					//使用默认长度0001
					String[] strox01 = { "00", "00", "00", "00", "00", "00", "01", "10", "00", "00", "00", "01" };
					strox01[12]=Integer.toHexString(paramesize);
					for(int i=0;i<parame.length;i++){
						int index=12+i*2+1;
						strox01[index]=parame[i].substring(0, 2);
						strox01[index+1]=parame[i].substring(2, 4);
					}
					
					strox = strox01;
				} else if (d_type == 2) {
					String[] strox02 = { "97", "79", "00", "00", "00", "09", "04", "10", "31", "F4", "00", "01","02" };
					//参数的字节长度
					strox02[13]=Integer.toHexString(paramesize*2);
					//本地到最后一个字节数
					strox02[5]=Integer.toHexString(7+paramesize*2);
					
					for(int i=0;i<parame.length;i++){
						int index=13+i*2+1;
						strox02[index]=parame[i].substring(0, 2);
						strox02[index+1]=parame[i].substring(2, 4);
					}
					strox = strox02;
				} else {
					return "没有该协议解析方式。";
				}


				// 设置tcp接收长度

				LOGGER.info("连接IP为：" + ip + "上位机中,请等待......！");
			
				LOGGER.info("连接IP为：" + ip + " 上位机成功！");
					Socket socket = new Socket(server, servPort);
					byte data[] = new byte[lent];

					for (int i = 0; i < strox.length; i++) {
						byte b = Integer.valueOf(strox[i], 16).byteValue();
						data[i] = b;
					}
					// byte的大小为8bits而int的大小为32bits 协议接收长度为查询长度的2倍
					byte[] recData = new byte[3000];// 接收数据缓冲 海上风电1191个字
					InputStream in = socket.getInputStream();
					OutputStream out = socket.getOutputStream();
					LOGGER.info("发送修改指令！");
					out.write(data); // 发送
					// 接收数据 初始化 数据接收长度
					int totalBytesRcvd = 3000; // Total bytes received so far
					totalBytesRcvd = in.read(recData);// 接收
					//LOGGER.info("第" + j + "次返回字节长度为：" + totalBytesRcvd);
					for (int i = 0; i < totalBytesRcvd; i++) {
						byte str = recData[i];
						if (i == totalBytesRcvd - 1) {
							strj += str;
						} else {
							strj += str + ",";
						}

					}
					LOGGER.info("返回字节为：" + strj);
					LOGGER.info("开始合并字节处理......");
					String shortstrone = getshortstr(recData,d_type);
					LOGGER.info(ip+"返回合并字为：" + shortstrone);

					socket.close();
					//sleep()
				
			} catch (Exception e) {
				LOGGER.info("与ip：" + ip + "通讯失败！");
				return "error";
				//
			}

			
		
			
			return strj;
		}	

	/**
	 * 字符组成16进制的字符
	 * 
	 * @param HexString
	 * @return
	 */
	public static String get4HexString(String HexString) {
		String hx = "";
		if (HexString.length() < 2) {
			hx = "00,0" + HexString;
		} else if (HexString.length() < 3) {
			hx = "00," + HexString;
		} else if (HexString.length() < 4) {
			hx = "0" + HexString.substring(0, 1) + "," + HexString.substring(1, 3);
			;
		} else {
			hx = HexString.substring(0, 2) + "," + HexString.substring(2, 4);
		}
		return hx;
	}

	public static String getshortstr(byte[] newmodbusstr, int d_type) {
		byte[] array =null;
		if(d_type==2){
			 array = Arrays.copyOfRange(newmodbusstr, 9, newmodbusstr.length);
		}else{
			 array = Arrays.copyOfRange(newmodbusstr, 10, newmodbusstr.length);
		}
		//byte[] array = Arrays.copyOfRange(newmodbusstr, 10, newmodbusstr.length);
		//byte[] array = newmodbusstr;
		// LOGGER.info("0位"+array[0] );
		int len = array.length / 2;
		// LOGGER.info("len="+len);
		short[] ArrayData = new short[len];
		byte[] arraytmp = new byte[2];
		for (short i = 0; i <= len - 1; i++) {
			arraytmp[0] = array[2 * i];
			arraytmp[1] = array[2 * i + 1];
			ArrayData[i] = byte4ToInt(arraytmp, 0);
		}
		String bString = "";
		for (int i = 0; i < ArrayData.length; i++) {
			short str = ArrayData[i];
			if (i == ArrayData.length - 1) {
				bString += str;
			} else {
				bString += str + ",";
			}
		}

		return bString;
	}

	/**
	 * //合并字节位换成一个16位的整形数,short类
	 * 
	 * @param bytes
	 * @param off
	 * @return
	 */
	public static short byte4ToInt(byte[] bytes, int off) {
		short b0 = (short) ((short)bytes[off] & 0xFF);
		short b1 = (short) ((short)bytes[off + 1] & 0xFF);
		short ii = (short) ((b0 << 8) | b1);
		return ii;
	}
	/**
	 * 字符数组转换成字符串
	 * 
	 * @param ss
	 * @return
	 */
	public static String sztostr(String[] ss) {
		String strs = "";
		for (int i = 0; i < ss.length; i++) {
			if (i == ss.length - 1) {
				strs += ss[i];
			} else {
				strs += ss[i] + " ";
			}
		}
		return strs;
	}
}
