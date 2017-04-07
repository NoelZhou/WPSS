package com.cn.hy.controller.firmwareUpgrade;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgradeDeviceInfoDetail;

/**
 * 获取设备信息
 * @author Administrator
 * 
 */
public class FirmwareUpgradeDeviceUtils {
	public static void main(String[] args) throws Exception {
		getDeviceInfo("192.168.68.233", 502);
	}

/**
 * 获取设备详细信息
 * @param ip	IP地址
 * @param port	端口号
 * @return
 * @throws Exception
 */
public static FirmwareUpgradeDeviceInfoDetail getDeviceInfo(String ip, int port) throws Exception{
		FirmwareUpgradeDeviceInfoDetail deviceInfoDetail = new FirmwareUpgradeDeviceInfoDetail();
		String[] strox1 = { "00", "00", "00", "00", "00", "00", "01", "04", "09", "C3", "00", "65" };
		String[] strox2 = { "00", "00", "00", "00", "00", "00", "01", "04", "0A", "28", "00", "65" };
		//获取设备Byte返回数据
		byte[] recData1 = getDeviceInfoBytes(ip, port, strox1);
		//获取设备Byte返回数据
		byte[] recData2 = getDeviceInfoBytes(ip, port, strox2);
		readDeviceInfoBytesFirst(deviceInfoDetail, recData1);
		readDeviceInfoBytesSecond(deviceInfoDetail, recData2);
		return deviceInfoDetail;
	}

	/**
	 * 获取设备Byte返回数据
	 * @param strox
	 */
	private static byte[] getDeviceInfoBytes(String ip, int port, String[] strox) throws Exception{
		byte[] recData = new byte[211];
		byte[] cmds = new byte[12];
		for (int i = 0; i < strox.length; i++) {
			byte b = Integer.valueOf(strox[i], 16).byteValue();
			cmds[i] = b;
		}
		
		Socket socket = new Socket(ip, port);
		OutputStream out = socket.getOutputStream();
		out.write(cmds, 0, 12);
		InputStream in = socket.getInputStream();
		int nRet = 0;
		nRet = in.read(recData);
		System.out.println("返回数据长度"+nRet);
		
		out.close();
		in.close();
		socket.close();
		return recData;
	}
	
	/**
	 * 读取发送第一次命令返回的数据
	 * @param buf
	 * @throws UnsupportedEncodingException
	 */
	private static void readDeviceInfoBytesFirst(FirmwareUpgradeDeviceInfoDetail deviceInfoDetail, byte[] buf) throws UnsupportedEncodingException {
		int i = 0;
		String strRet = null;
		
		int uitmp = 0;
		String hexstring = "";
		byte[] recvBuf;
		boolean isResult;
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[9 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 9] * 256 + buf[i + 10] + buf[i + 11] * 65536 * 256 + buf[i + 12] * 65536);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setPropertyAgreementNumber(hexstring.trim());
		}

		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[13 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 13] * 256 + buf[i + 14] + buf[i + 15] * 65536 * 256 + buf[i + 16] * 65536);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setPropertyAgreementVersion(hexstring.trim());
		}
		

		isResult = false;
		for(int t = 0; t < 20; t++){
			if(buf[19 + t] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 20; jtmp++) {
				if (buf[19 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}

			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				//拷贝字符串
				System.arraycopy(buf, 19 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "UTF-8");
				deviceInfoDetail.setSerialNumber(strRet);
			}
		}
		
		isResult = false;
		for(int t = 0; t < 2; t++){
			if(buf[39 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 39] * 256 + buf[i + 40]);
			deviceInfoDetail.setDeviceModule(String.valueOf(uitmp));
		}
		
		isResult = false;
		for(int t = 0; t < 16; t++){
			if(buf[41 + t] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 16; jtmp++) {
				if (buf[41 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}

			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				System.arraycopy(buf, 41 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "UTF-8");//ac9f00ac3a98
				deviceInfoDetail.setDeviceType(strRet);
			}
		}

		isResult = false;
		for(int t = 0; t < 20; t++){
			if(buf[57 + t] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 20; jtmp++) {
				if (buf[57 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}
			
			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				System.arraycopy(buf, 57 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "UTF-8");
				deviceInfoDetail.setDeviceVersion(strRet);
			}
		}
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[77 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 79] * 65536 * 256 + buf[i + 80] * 65536 + buf[77] * 256 + buf[i + 78]);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setApplicationProtocolNumber(hexstring.trim());
		}

		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[81 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 83] * 65536 * 256 + buf[i + 84] * 65536 + buf[i + 81] * 256 + buf[i + 82]);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setApplicationProtocolVersion(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 6; t++){
			if(buf[93 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			recvBuf = new byte[6];
			System.arraycopy(buf, 93 + i, recvBuf, 0, 6);
			strRet = "";
			for (int j = 0; j < 6; j++) {
				int v = recvBuf[j] & 0xFF;
				String strtmp = Integer.toHexString(v);
				if (strtmp.length() == 1) {
					strtmp = "0" + strtmp;
				}
				strRet += strtmp;
			}
			deviceInfoDetail.setMac1(strRet.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 6; t++){
			if(buf[99 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			recvBuf = new byte[6];
			System.arraycopy(buf, 99 + i, recvBuf, 0, 6);
			strRet = "";
			for (int j = 0; j < 6; j++) {
				int v = recvBuf[j] & 0xFF;
				String strtmp = Integer.toHexString(v);
				if (strtmp.length() == 1) {
					strtmp = "0" + strtmp;
				}
				strRet += strtmp;
			}
			deviceInfoDetail.setMac2(strRet.trim());
		}

		isResult = false;
		for(int t = 0; t < 2; t++){
			if(buf[105 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 105] * 256 + buf[i + 106]);
			deviceInfoDetail.setMaintenanceContractNumber(String.valueOf(uitmp));
		}

		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[107 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 109] * 65536 * 256 + buf[i + 110] * 65536 + buf[i + 107] * 256 + buf[i + 108]);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIapAgreementNumber(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[111 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = (int) (buf[i + 113] * 65536 * 256 + buf[i + 114] * 65536 + buf[i + 111] * 256 + buf[i + 112]);
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIapAgreementVersion(hexstring.trim());
		}

		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[115 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 117] * 65536 * 256 + buf[i + 118] * 65536 + buf[i + 115] * 256 + buf[i + 116]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIacAgreementNumber(hexstring.trim());
		}

		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[119 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 121] * 65536 * 256 + buf[i + 122] * 65536 + buf[i + 119] * 256 + buf[i + 120]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIacAgreementVersion(hexstring.trim());
		}

		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[123 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 125] * 65536 * 256 + buf[i + 126] * 65536 + buf[i + 123] * 256 + buf[i + 124]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIadAgreementNumber(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[127 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 129] * 65536 * 256 + buf[i + 130] * 65536 + buf[i + 127] * 256 + buf[i + 128]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIadAgreementVersion(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[131 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 133] * 65536 * 256 + buf[i + 134] * 65536 + buf[i + 131] * 256 + buf[i + 132]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIatAgreementNumber(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 4; t++){
			if(buf[135 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 137] * 65536 * 256 + buf[i + 138] * 65536 + buf[i + 135] * 256 + buf[i + 136]));
			hexstring = getHexString(uitmp, 16);
			deviceInfoDetail.setIatAgreementVersion(hexstring.trim());
		}
		
		isResult = false;
		for(int t = 0; t < 2; t++){
			if(buf[171 + t] != 0xff){
				isResult = true;
				break;
			}
		}
		if(isResult){
			uitmp = ((int) (buf[i + 171] * 256 + buf[i + 172]));
			deviceInfoDetail.setFirmwareVersionNumber(String.valueOf(uitmp));
		}

		isResult = false;
		for(int t = 0; t < 30; t++){
			if(buf[173 + t] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 30; jtmp++) {
				if (buf[173 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}

			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				System.arraycopy(buf, 173 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "utf-8");
				deviceInfoDetail.setMcuFirmwareVersionNumber1(strRet);
			}
		}
	}
	
	/**
	 * 读取发送第二次命令返回的数据
	 * @param buf
	 * @throws UnsupportedEncodingException
	 */
	private static void readDeviceInfoBytesSecond(FirmwareUpgradeDeviceInfoDetail deviceInfoDetail, byte[] buf) throws UnsupportedEncodingException {
		int i = 0;
		String strRet = null;
		
		byte[] recvBuf;
		boolean isResult = false;
		
		for(int t = 0; t < 22; t++){
			if(buf[9 + t] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 21; jtmp++) {
				if (buf[9 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}
			
			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				System.arraycopy(buf, 9 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "utf-8");
				deviceInfoDetail.setMcuFirmwareVersionNumber2(strRet);
			}
		}
		
		isResult = false;
		for(int t = 0; t < 30; t++){
			if(buf[t + 31] != 0x00){
				isResult = true;
				break;
			}
		}
		if(isResult){
			int ilastvar = 0;
			for (int jtmp = 0; jtmp < 30; jtmp++) {
				if (buf[31 + i + jtmp] != 0) {
					ilastvar = jtmp;
				}
			}
			
			if (ilastvar == 0) {
				strRet = "";
			} else {
				recvBuf = new byte[ilastvar + 1];
				System.arraycopy(buf, 31 + i, recvBuf, 0, ilastvar + 1);
				strRet = new String(recvBuf, "utf-8");
				deviceInfoDetail.setMcuFirmwareVersionNumber3(strRet);
				deviceInfoDetail.setAgainMcuFirmwareVersionNumber2();
			}
		}
	}
	
	
	/**
	 * 接收字节转 字符串
	 * @param uival
	 * @param len
	 * @return
	 */
	private static String getHexString(int uival, int len) {
		return Integer.toString(uival, len);
	}
}
