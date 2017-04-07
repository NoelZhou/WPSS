package com.cn.hy.controller.firmwareUpgrade;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.cn.hy.common.WebResponseCode;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgrade;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgradeDeviceInfoDetail;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.firmwareUpgrade.FirmwareUpgradeService;
import com.cn.hy.service.system.DeviceService;

@SuppressWarnings({"static-access", "unused", "unchecked"})
public class FirmwareUpgradeUtils {
	/**
	 * 加密后文件数据
	 */
	public static byte[] aescodebyte;

	/**
	 * 加密前文件数据
	 */
	public static byte[] aescodebyteMW;

	/**
	 * 总包数
	 */
	public static int totalnum = 0;
	/**
	 * 发送数据包单位
	 */
	public static int sendPackageUnit = 208;

	/**
	 * 设备
	 */
	private static String deviceVersionNo;

	/**
	 * 设备名称
	 */
	private static String deviceName;

	private static HttpServletRequest request;

	private static HttpSession session;

	private static FirmwareUpgradeService firmwareUpgradeService;
	private static DeviceService deviceService;
	private static int upgradeStatus = 0;

	public static void main(String[] args) {
		// "ABC"
		// "ADC"

		// String t = "ARM_WG2000KDF_V21_C_M";
		// String t1 = "ARM_WG2000KDF_V22_C_M";

		String t = "ABC";
		String t1 = "ADC";
		System.out.println(t.compareTo(t1));
	}

	public static void setRequest(HttpServletRequest request1) {
		request = request1;
		session = request.getSession();
	}

	/**
	 * 单个升级
	 * 
	 * @param deviceId
	 * @param filePath
	 */
	public static String upgrade(String deviceId, String filePath) {
		if(upgradeStatus==110){
			upgradeStatus=0;
		}
		String returnMsg = "";
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		firmwareUpgradeService = (FirmwareUpgradeService) wac.getBean("firmwareUpgradeService");
		FirmwareUpgrade firmwareUpgrade = new FirmwareUpgrade();
		firmwareUpgrade.setId(Long.parseLong(deviceId));
		firmwareUpgrade = firmwareUpgradeService.getDeviceinfo(firmwareUpgrade);
		returnMsg = firmwareUpgrade.getName();
		deviceName = firmwareUpgrade.getName();
		//设置升级设备的名称
		updateProgressDeviceName(deviceId, firmwareUpgrade.getName());

		String ip = firmwareUpgrade.getIp();
		int port = firmwareUpgrade.getPort();
		// 查询升级状态
		returnMsg = getUpgradeStatus(ip, port, upgradeStatus,deviceId);
		if (returnMsg == "1") {
			returnMsg = "通讯故障,关闭窗口";
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
			return returnMsg;
		}
		if (upgradeStatus == 2 || upgradeStatus == 4|| upgradeStatus == 6|| upgradeStatus == 7) {
			returnMsg = quit(ip, port);
			returnMsg = getUpgradeStatus(ip, port, upgradeStatus,deviceId);
		}
		if (upgradeStatus != 1) {
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
		}
		// 登陆
		returnMsg = login(ip, port);
		// 设置目标级数.目标地址
		returnMsg = setSendData(ip, port);
		// 获取设备版本号主要为了断点续传
		returnMsg = getDeviceVersionNo(ip, port);

		// 读取文件
		returnMsg = getUpdateFile(filePath);
         
		if(!returnMsg.equals("success")){
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
			return returnMsg;
		}
		
		/* 设置升级数据 */
		returnMsg = setupdatedata(ip, port, filePath);

		if (returnMsg == "2") {
			returnMsg = "当前版本号不支持升级";
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
			return returnMsg;
		}

		returnMsg = getUpgradeStatus(ip, port, upgradeStatus,deviceId);
		if (returnMsg == "1") {
			returnMsg = "通讯故障,关闭窗口";
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
			return returnMsg;
		}
		// 发送数据
		returnMsg=sendData(ip, port,deviceId);

		if (returnMsg == "1") {
			returnMsg = "通讯故障,关闭窗口";
			updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, false,
					returnMsg);
			return returnMsg;
		}
		updateProgressOver(String.valueOf(firmwareUpgrade.getId()), firmwareUpgrade.getName(), ip, port, true, null);
		// quit(ip, port);
		return returnMsg;
	}

	/**
	 * 设置升级参数
	 * 
	 * @param ip
	 * @param port
	 * @param filePath
	 * @return
	 */
	private static String setupdatedata(String ip, int port, String filePath) {
		String sire = "0";
		int sendNum = aescodebyteMW.length / sendPackageUnit;
		if ((aescodebyteMW.length % sendPackageUnit) > 0) {
			sendNum++;
		}
		totalnum = sendNum;
		String[] cmdtmp = new String[1024];
		String appversion = new DeviceCode().getSguAppversion(filePath);
		FirmwareUpgradeDeviceInfoDetail firmwareUpgradeDeviceInfoDetail = null;
		try {
			firmwareUpgradeDeviceInfoDetail = FirmwareUpgradeDeviceUtils.getDeviceInfo(ip,
					port);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// if
		// (appversion.compareTo(firmwareUpgradeDeviceInfoDetail.getMcuFirmwareVersionNumber1())
		// < 0) {
		if (!compareVersion(appversion, firmwareUpgradeDeviceInfoDetail.getMcuFirmwareVersionNumber1(),
				firmwareUpgradeDeviceInfoDetail.getMcuFirmwareVersionNumber2(),
				firmwareUpgradeDeviceInfoDetail.getMcuFirmwareVersionNumber3())) {
			sire = "2";
			return sire;
		}
		String[] cmd = { "00", "00", "00", "00", "00", "35", "01", "10", "4e", "35", "00", "17", "2e" };
		String[] appstr = new DeviceCode().strtoHexString(appversion);
		String[] app31 = new String[31];
		System.arraycopy(appstr, 0, app31, 0, appstr.length);
		for (int i = appstr.length; i < app31.length; i++) {
			app31[i] = "00";
		}

		System.arraycopy(cmd, 0, cmdtmp, 0, cmd.length);
		System.arraycopy(app31, 0, cmdtmp, cmd.length, app31.length);
		// 强制升级00 01 保留
		cmdtmp[44] = "AA";
		cmdtmp[45] = "00";
		cmdtmp[46] = "01";

		// long ii=new TypeUtils().unsigned32ByBytes(aescodebyteMW);
		long[] bytetoint32 = new TypeUtils().byte2Intstr(aescodebyteMW);
		long getcrc32 = CRC32bylong(bytetoint32, bytetoint32.length);
		String intstr4 = Long.toHexString(getcrc32);// a4fe2ecc
		String intstr1 = new TypeUtils().gotolonghex4(intstr4);
		cmdtmp[47] = intstr1.substring(4, 6);
		cmdtmp[48] = intstr1.substring(6, 8);
		cmdtmp[49] = intstr1.substring(0, 2);
		cmdtmp[50] = intstr1.substring(2, 4);
		// cmdtmp[47]="2e";cmdtmp[48]="cc"; cmdtmp[49]="a4";cmdtmp[50]="fe";
		// 总长度2个字 高低字节 总长度143920=12848,2
		String[] intstr = new DeviceCode().IntTounshort(aescodebyte.length).split(",");
		int ing = Integer.parseInt(intstr[0]);
		int ind = Integer.parseInt(intstr[1]);

		String alllg = Integer.toHexString(ing);
		String[] alllhexg = new DeviceCode().get4HexString(alllg).split(",");
		cmdtmp[51] = alllhexg[0];
		cmdtmp[52] = alllhexg[1];

		String allld = Integer.toHexString(ind);
		String[] alllhexd = new DeviceCode().get4HexString(allld).split(",");
		cmdtmp[53] = alllhexd[0];
		cmdtmp[54] = alllhexd[1];

		// 00 d0 没报长度sendPackageUnit
		String put = Integer.toHexString(sendPackageUnit);
		String[] ohex = new DeviceCode().get4HexString(put).split(",");
		cmdtmp[55] = ohex[0];
		cmdtmp[56] = ohex[1];
		// 02 b4 692 总报数

		String allput = Integer.toHexString(totalnum);
		String[] allphex = new DeviceCode().get4HexString(allput).split(",");
		cmdtmp[57] = allphex[0];
		cmdtmp[58] = allphex[1];

		byte[] recData = null;
		String[] cmdtmpone = new String[cmd.length + app31.length + 15];
		System.arraycopy(cmdtmp, 0, cmdtmpone, 0, cmdtmpone.length);
		String sendstr = "设置升级参数发送指令为:";
		for (int i = 0; i < cmdtmpone.length; i++) {
			sendstr += cmdtmpone[i] + ",";
		}
		System.out.println(sendstr);
		recData = sendCmdGetRecInfo(ip, port, cmdtmpone, 12);

		for (int i = 0; i < 10; i++) {
			// 测试连接用来延迟时间
			if (recData.length != 1) {
				sire = "0";
				break;
			} else {
				Socket sk = null;
				try {
					sk = new Socket("192.168.0.0", 502);
					sk.setSoTimeout(3000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("设置升级数据，重新发送指令.....");
				sire = "1";
				recData = sendCmdGetRecInfo(ip, port, cmdtmpone, 12);
			}
		}
		if (sire.equals("1")) {
			return sire;
		}

		byte[] recvBuf;
		String requestr = "设置升级参数回复指令为:";
		for (int i = 0; i < recData.length; i++) {
			requestr += recData[i] + ",";
		}
		System.out.println(requestr);
		recvBuf = new byte[2];

		System.arraycopy(recData, 8, recvBuf, 0, 2);
		int startAddr = TypeUtils.byte4ToInt(recvBuf, 0);
		System.out.println("起始地址：" + startAddr);
		return "设置升级数据成功！";
	}

	/**
	 * 批量升级
	 * 
	 * @param deviceIds
	 * @param filePath
	 */
	public static String upgradeList(String[] deviceIds, String filePath) {
		updateProgressSessionMsgClear();
		String returnMsg = "";
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(deviceIds.length);
		for (String deviceId : deviceIds) {
			final String deviceIdt = deviceId;
			final String filePatht = filePath;
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					// 修改设备的升级状态
					new DeviceCode().updatestatebyid(deviceIdt, 1);
					System.out.println(deviceIdt + "开始执行！！！");
					//单个升级
					upgrade(deviceIdt, filePatht);
					System.out.println(deviceIdt + "结束执行！！！");
					// 升级完成，更改设备的升级状态
					new DeviceCode().updatestatebyid(deviceIdt, 2);
				}
			});
		}
		return returnMsg;
	}

	/**
	 * 获取设备的升级状态
	 * @param ip
	 * @param port
	 * @param status
	 * @throws Exception
	 */
	public static String getUpgradeStatus(String ip, int port, int status,String deviceId) {
		if(upgradeStatus==110){
			upgradeStatus=110;
			//固件升级中断，将退出升级
			return "1";
		}
		String sire = "0";
		String[] cmd = { "00", "00", "00", "00", "00", "06", "01", "03", "4E", "24", "00", "03" };

		String sendstr = "获取设备升级状态发送指令为:";
		for (int i = 0; i < cmd.length; i++) {
			sendstr += cmd[i] + ",";
		}
		System.out.println(sendstr);
		//发送cmd命令，获取返回数据Byte返回数据
		byte[] recData = sendCmdGetRecInfo(ip, port, cmd, 15);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		deviceService = (DeviceService) wac.getBean("DeviceServiceImpl");
		for (int i = 0; i < 3; i++) {
			Device d = deviceService.selectDeviceById(Integer.parseInt(deviceId));
			if(d.getRw_role_req().equals("SJJS")&&d.getRw_role_res().equals("OK")){
				System.out.println("固件升级中断，将退出升级！");
				String isfz=quit(ip, port);
				Device dd = new Device();
				dd.setId(Integer.parseInt(deviceId));
				dd.setRw_role_req("R");
				dd.setRw_role_res("OK");
				deviceService.updateDevice(dd);
				upgradeStatus=110;
				return "1";
				
			}
			if (recData.length != 1) {
				sire = "0";
				break;
			} else {

				// 测试连接用来延迟时间
				Socket sk = null;
				try {
					sk = new Socket("192.168.0.0", 502);
					sk.setSoTimeout(3000);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("查询升级状态失败，重新发送指令.....");
				sire = "1";
				recData = sendCmdGetRecInfo(ip, port, cmd, 15);
			}
		}
		String requestr1 = "sire值："+sire;
		if (sire.equals("1")) {
			return sire;
		}
		byte[] recvBuf = new byte[2];
		System.arraycopy(recData, 4, recvBuf, 0, 2);
		int returnByteLength = TypeUtils.byte4ToInt(recvBuf, 0);
		recvBuf = new byte[2];
		recvBuf[0] = 0;
		System.arraycopy(recData, 8, recvBuf, 1, 1);
		int dataLength = TypeUtils.byte4ToInt(recvBuf, 0);
		recvBuf = new byte[dataLength];
		System.arraycopy(recData, 9, recvBuf, 0, 2);
		status = TypeUtils.byte4ToInt(recvBuf, 0);
		// System.out.println("返回数据："+status);i
		upgradeStatus = status;
		String requestr = "指令长度为："+recData.length+"获取设备升级状态回复指令为:";
		for (int i = 0; i < recData.length; i++) {
			requestr += recData[i] + ",";
		}
		System.out.println(requestr);
		/*
		 * 1、未升级 2、正在升级 3、故障 4、升级成功 5、正在向下级升级（Logger等中间设备用）
		 * 6、向下升级结束（Logger等中间设备用） 7、本级升级结束 （本级非自身升级，如DSP）
		 */
		String mess = "";
		if (status == 1) {
			mess = "未升级";
		} else if (status == 2) {
			mess = "正在升级";
		} else if (status == 3) {
			mess = "故障";
		} else if (status == 4) {
			mess = "升级成功";
		} else if (status == 5) {
			mess = "正在向下级升级";
		} else if (status == 6) {
			mess = "向下升级结束";
		} else if (status == 7) {
			mess = "本级升级结束(DSP升级成功！)";
		}
		return mess;
	}

	/**
	 * 登录
	 * 
	 * @throws Exception
	 */
	private static String login(String ip, int port) {
		String[] cmd = { "00", "00", "00", "00", "00", "19", "01", "10", "4E", "29", "00", "09", "12", "00", "AA", "A1",
				"90", "75", "F7", "09", "A2", "C7", "43", "00", "05", "00", "00", "00", "00", "00", "00" };
		String sendstr = "登陆发送指令为:";
		for (int i = 0; i < cmd.length; i++) {
			sendstr += cmd[i] + ",";
		}
		System.out.println(sendstr);
		byte[] recData = null;
		try {
			recData = sendCmdGetRecInfo(ip, port, cmd, 12);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "登陆失败！";
		}
		String requestr = "登陆回复指令为:";
		for (int i = 0; i < recData.length; i++) {
			requestr += recData[i] + ",";
		}
		System.out.println(requestr);
		byte[] recvBuf;
		for (int i = 0; i < 10; i++) {
			if (recData.length == 1) {
				System.out.println("登陆失败，重新发送指令.....");
				try {
					recData = sendCmdGetRecInfo(ip, port, cmd, 12);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		recvBuf = new byte[2];
		System.arraycopy(recData, 8, recvBuf, 0, 2);
		int startAddr = TypeUtils.byte4ToInt(recvBuf, 0);
		System.out.println("起始地址：" + startAddr);
		return "登陆成功！";
	}

	/**
	 * 定义发送数据 4e 32 = 20018 发送起始地址
	 * 
	 * @throws Exception
	 */
	public static String setSendData(String ip, int port) {
		String[] cmd = { "00", "00", "00", "00", "00", "0D", "01", "10", "4E", "32", "00", "03", "06", "00", "00", "FF",
				"FF", "00", "01" };
		byte[] recData = null;
		try {
			recData = sendCmdGetRecInfo(ip, port, cmd, 12);
			for (int i = 0; i < 10; i++) {
				if (recData.length == 1) {
					System.out.println("设置目录级失败，重新发送指令.....");
					recData = sendCmdGetRecInfo(ip, port, cmd, 12);
				}
			}
		} catch (Exception e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return "设置目标级数.目标地址失败";
		}
		byte[] recvBuf;

		recvBuf = new byte[2];
		System.arraycopy(recData, 8, recvBuf, 0, 2);
		int startAddr = TypeUtils.byte4ToInt(recvBuf, 0);
		System.out.println("发送起始地址：" + startAddr);
		return "设置目标级数.目标地址成功！";
		// 返回0,0,0,0,0,6，01 10 4e 32 00 03
	}

	/**
	 * 获取设备的软件版本号
	 * 
	 * @throws Exception
	 */
	private static String getDeviceVersionNo(String ip, int port) {
		String iscz = "0";
		String[] cmd = { "00", "00", "00", "00", "00", "06", "01", "03", "4E", "35", "00", "18" };
		byte[] recData = sendCmdGetRecInfo(ip, port, cmd, 57);
		byte[] recvBuf;
		if (recData.length == -1) {
			iscz = "1";
		}
		recvBuf = new byte[2];
		recvBuf[0] = 0;
		System.arraycopy(recData, 8, recvBuf, 1, 1);
		int dataLength = TypeUtils.byte4ToInt(recvBuf, 0);
		System.out.println("数据长度：" + dataLength);

		recvBuf = new byte[21];
		recvBuf[0] = 0;
		System.arraycopy(recData, 9, recvBuf, 0, 21);
		try {
			String deVersionNo = new String(recvBuf, "UTF-8");
			deviceVersionNo = deVersionNo;
			System.out.println("设备软件版本号：" + deviceVersionNo);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return iscz;
	}

	/**
	 * 退出
	 * 
	 * @param ip
	 * @param port
	 * @throws Exception
	 */
	public static String quit(String ip, int port) {
		String isfz = "0";
		String[] cmd = { "00", "00", "00", "00", "00", "19", "01", "10", "4e", "29", "00", "09", "12", "00", "55", "a1",
				"90", "75", "f7", "09", "a2", "c7", "43", "00", "05", "00", "00", "00", "00", "00", "00" };
		byte[] recData = sendCmdGetRecInfo(ip, port, cmd, 12);
		byte[] recvBuf;
		if (recData.length == 1) {
			isfz = "1";
			return isfz;
		}
		recvBuf = new byte[2];
		System.arraycopy(recData, 8, recvBuf, 0, 2);
		int dataLength = TypeUtils.byte4ToInt(recvBuf, 0);
		System.out.println("返回地址位：" + dataLength);
		return isfz;
	}

	/**
	 * 发送升级数据包
	 * 
	 * @param ip
	 * @param port
	 * @param sendData
	 * @throws Exception
	 *//*
		 * public static void sendUpgradeData(String ip, int port, byte[]
		 * sendData) throws Exception{ int i = 0; while(i<20){ String[] cmds = {
		 * "00", "00", "00", "00", "00", "DF", "01", "10", "4E", "4C", "00",
		 * "6C", "D8" }; byte[] recData = sendCmdGetRecInfo(ip, port, cmds,
		 * sendData, 12); if (recData[1] == 0x90){ break; } i++; } }
		 */
	/**
	 * 发送cmd命令，获取返回数据Byte返回数据
	 * 
	 * @param strox
	 * @throws Exception
	 */
	private static byte[] sendCmdGetRecInfo(String ip, int port, String[] strox, int dataLength) {
		byte[] recData = new byte[dataLength];
		byte[] cmds = new byte[strox.length];
		for (int i = 0; i < strox.length; i++) {
			byte b = Integer.valueOf(strox[i], 16).byteValue();
			cmds[i] = b;
		}
		try {
			Socket socket = new Socket(ip, port);
			OutputStream out = socket.getOutputStream();
			out.write(cmds, 0, strox.length);
			InputStream in = socket.getInputStream();
			int nRet = 0;
			nRet = in.read(recData);
			System.out.println("返回数据长度:::" + nRet);

			out.close();
			in.close();
			socket.close();
			if (nRet == -1) {
				byte[] by = new byte[1];
				{
					by[0] = -1;
					return by;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			byte[] by = new byte[1];
			{
				by[0] = -1;
				return by;
			}
		}
		return recData;
	}

	/**
	 * 循环发送数据包
	 * @throws Exception
	 */
	public static String sendData(String ip, int port,String device_id) {
		// 指令头，读写指令写的长度，地址的定义
		String[] startcode = { "00", "00", "00", "00", "00", "DF", "01", "10", "4E", "4C", "00", "6C", "D8" };
		String[] xhcode = new String[8];
		String xhlenstr = new DeviceCode().get4HexString(Integer.toHexString(sendPackageUnit));
		xhcode[6] = xhlenstr.split(",")[0];
		xhcode[7] = xhlenstr.split(",")[1];
		int sendNum = aescodebyte.length / sendPackageUnit;
		if ((aescodebyte.length % sendPackageUnit) > 0) {
			sendNum++;
		}
		totalnum = sendNum;
		byte[] sendData;
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		deviceService = (DeviceService) wac.getBean("DeviceServiceImpl");
		for (int i = 1; i < sendNum + 1; i++) {
			Device d = deviceService.selectDeviceById(Integer.parseInt(device_id));
			if(d.getRw_role_req().equals("SJJS")&&d.getRw_role_res().equals("OK")){
				System.out.println("固件升级中断，将退出升级！");
				String isfz=quit(ip, port);
				//xiug
				Device dd = new Device();
				dd.setId(Integer.parseInt(device_id));
				dd.setRw_role_req("R");
				dd.setRw_role_res("OK");
				deviceService.updateDevice(dd);
				upgradeStatus=110;
				return "固件升级停止，退出程序！";
				
			}
			String xhstr = new DeviceCode().get4HexString(Integer.toHexString(i));
			xhcode[0] = xhstr.split(",")[0];
			xhcode[1] = xhstr.split(",")[1];
			int strartlen = (i - 1) * sendPackageUnit;
			String[] intstr = new DeviceCode().IntTounshort(strartlen).split(",");
			int ing = Integer.parseInt(intstr[0]);
			int ind = Integer.parseInt(intstr[1]);

			String alllg = Integer.toHexString(ing);
			if (ing < 0) {
				alllg = alllg.substring(4, 8);
			}
			String[] alllhexg = new DeviceCode().get4HexString(alllg).split(",");
			xhcode[2] = alllhexg[0];
			xhcode[3] = alllhexg[1];
			String allld = Integer.toHexString(ind);
			if (ind < 0) {
				allld = allld.substring(4, 8);
			}
			String[] alllhexd = new DeviceCode().get4HexString(allld).split(",");
			xhcode[4] = alllhexd[0];
			xhcode[5] = alllhexd[1];

			String startlenstr = new DeviceCode().get4HexString(Integer.toHexString(strartlen));
			// sendData = new byte[sendPackageUnit];
			String[] sendDatanew = new String[sendPackageUnit + startcode.length + xhcode.length];
			System.arraycopy(startcode, 0, sendDatanew, 0, startcode.length);
			System.arraycopy(xhcode, 0, sendDatanew, startcode.length, xhcode.length);
			int dataLength = aescodebyte.length - ((i - 1) * sendPackageUnit);
			if (dataLength >= sendPackageUnit) {
				byte[] sendone = new byte[sendPackageUnit];
				System.arraycopy(aescodebyte, ((i - 1) * sendPackageUnit), sendone, 0, sendPackageUnit);
				String[] sendonestr = new String[sendPackageUnit];
				for (int j = 0; j < sendone.length; j++) {
					short tmpshort = (short) (sendone[j] & 0x00ff);
					sendonestr[j] = Integer.toHexString(tmpshort);
				}
				System.arraycopy(sendonestr, 0, sendDatanew, startcode.length + xhcode.length, sendPackageUnit);
			} else {
				String startcodeone = Integer.toHexString(dataLength + xhcode.length);
				startcode[12] = startcodeone;
				String ss = new DeviceCode().get4HexString(Integer.toHexString((dataLength + xhcode.length) / 2));
				startcode[12] = startcodeone;
				startcode[11] = ss.split(",")[1];
				startcode[10] = ss.split(",")[0];
				String startcodeone7 = new DeviceCode()
						.get4HexString(Integer.toHexString(dataLength + xhcode.length + 7));
				startcode[5] = startcodeone7.split(",")[1];
				startcode[4] = startcodeone7.split(",")[0];
				System.arraycopy(startcode, 0, sendDatanew, 0, startcode.length);

				String xhlenstrone = new DeviceCode().get4HexString(Integer.toHexString(dataLength));
				xhcode[6] = xhlenstrone.split(",")[0];
				xhcode[7] = xhlenstrone.split(",")[1];
				System.arraycopy(xhcode, 0, sendDatanew, startcode.length, xhcode.length);
				byte[] sendone = new byte[sendPackageUnit];
				System.arraycopy(aescodebyte, ((i - 1) * sendPackageUnit), sendone, 0, dataLength);
				String[] sendonestr = new String[sendPackageUnit];
				for (int j = 0; j < sendPackageUnit; j++) {
					short tmpshort = (short) (sendone[j] & 0x00ff);
					sendonestr[j] = Integer.toHexString(tmpshort);
				}

				System.arraycopy(sendonestr, 0, sendDatanew, startcode.length + xhcode.length, sendPackageUnit);

			}
			String strxx = "";
			for (int k = 0; k < sendDatanew.length; k++) {
				String xx = sendDatanew[k];
				strxx += xx + ",";
			}
			System.out.println("第" + i + "次数据包为：" + strxx);
			byte[] recData = sendCmdGetRecInfo(ip, port, sendDatanew, sendDatanew.length);
			String sire = "0";
			for (int k = 0; k <5; k++) {
				// 测试连接用来延迟时间
				if (recData.length != 1) {
					sire = "0";
					break;
				} else {
					Socket sk;
					try {
						sk = new Socket("192.168.68.0", 502);
						sk.setSoTimeout(3000);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("第" + k + "次发送数据包" + i);
					sire = "1";
					recData = sendCmdGetRecInfo(ip, port, sendDatanew, sendDatanew.length);
				}
			}
			if (sire.equals("1")) {
				return sire;
			}
		}
		return "发送数据包完成";
	}

	private static String getDvcieSJState(String device_id) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String getUpdateFile(String sgufile) {
		// 解析信息包len,crc32,代码包CRC32
		byte[] byteArr2 = null;
		try {
			// byte[] byteArr21 = FileReadUtils.readFile("E:\\sumgrow\\CC.sgu");
			// byte[] byteArr21 = FileReadUtils.readFile(sgufile);
			// byte[] byteArr22 =
			// FileReadUtils.readFile("F:\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp2\\wtpwebapps\\WinPowerSystem_server\\upload/1473765257312123.xml");
			byteArr2 = FileReadUtils.readFile(sgufile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "读取文件失败！";
		}

		int infolen = TypeUtils.byte2Int(byteArr2, 26 + 0);
		int infocrc = TypeUtils.byte2Int(byteArr2, 26 + 6);
		int codecrc = TypeUtils.byte2Int(byteArr2, 26 + 12);

		// 解密信息体
		byte[] beforedes=null;
		try {
			beforedes = new byte[infolen];
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "该文件已加密，无法解析！";
		}
		System.arraycopy(byteArr2, 64, beforedes, 0, infolen);
		//解密
		byte[] dcrcinfo = AESUtils.decrypt(beforedes, "sdkjsdkj12987391");
		byte[] icrc32byte = CRC32Ret4(dcrcinfo);
		if (infocrc != TypeUtils.byte2Int(icrc32byte, 0)) {
			System.out.println("信息区CRC校验错误！");
			return "信息区CRC校验错误;";
		}

		// 解密代码体
		byte[] beforecodedes = new byte[byteArr2.length - 64 - infolen];
		System.arraycopy(byteArr2, (64 + infolen), beforecodedes, 0, beforecodedes.length);
		//解密
		byte[] dcrccode = AESUtils.decrypt(beforecodedes, "sdkjsdkj12987391"); // UTF8Encoding.UTF8.GetBytes(dinfotext);
		byte[] dcrc32byte = CRC32Ret4(dcrccode);

		if (codecrc != TypeUtils.byte2Int(dcrc32byte, 0)) {
			System.out.println("代码区CRC校验错误！");
			return "代码区CRC校验错误";
		}
		String checkcodestr = new String(dcrccode);
		String[] apps = checkcodestr.split("APP\r\nCRC=");
		String getcodecrc = apps[1].substring(0, 11);
		byte[] codebyte = TypeUtils.hexStringToByte(getcodecrc.replace(" ", ""));
		int dcodecrc = TypeUtils.byte2Int(codebyte, 0);// APP CRC=

		// APP LEN=
		int fromIndex = apps[0].length() + 9 + 18;
		int codeindex = checkcodestr.indexOf("CODE=", fromIndex);
		int dcodelenindex = codeindex - fromIndex;
		int dappcodelen = Integer.parseInt(checkcodestr.substring(fromIndex, (dcodelenindex + fromIndex - 2)));

		// 得到bin文件
		// String binfile = checkcodestr.substring(codeindex +
		// 5);//从头截取到尾，如果是多个PCB,则要修改

		// 转回byte数组
		byte[] byteArr3 = new byte[dcrccode.length - codeindex - 5]; // =
																		// ConvertBinByte(binfile);
		System.arraycopy(dcrccode, codeindex + 5, byteArr3, 0, byteArr3.length);

		// 补FF的个数
		// int buchongff = byteArr3.length - dappcodelen;

		// 获取CODE的纯BIN文件准备加密
		byte[] getCodebyte = new byte[dappcodelen];
		System.arraycopy(byteArr3, 0, getCodebyte, 0, dappcodelen);

		if (byteArr3.length - dappcodelen >= 16) {
			System.out.println("bin文件长度不正确！");
			// return;
		}

		byte[] bincrc = CRC32Ret4(getCodebyte);
		if (TypeUtils.byte2Int(bincrc, 0) != dcodecrc) {
			System.out.println("bin文件CRC校验不正确！");
			// return;
		}
		// 补FF,加密，
		byte[] secondcrcArray = beforeAesByte(getCodebyte, (byte) 0xff);

		// 代码区加密
		// byte[]
		aescodebyteMW = secondcrcArray;
		try {
			aescodebyte = AESUtils.encrypt(secondcrcArray, "sdkjsdkj12987391");
		} catch (Exception e) {
			e.printStackTrace();
			return "读取文件失败！";
		}
		return "success";
	}

	public static byte[] CRC32Ret4(byte[] data) {
		int[] CrcTable = new int[256];
		int Crc;
		int i, j;
		for (i = 0; i < 256; i++) {
			Crc = i;
			for (j = 8; j > 0; j--) {
				if ((Crc & 1) == 1) {
					Crc = (Crc >>> 1) ^ 0XEDB88320;
				} else {
					Crc >>>= 1;
				}
			}
			CrcTable[i] = Crc;
		}
		byte[] temdata = new byte[4];
		int iCount = data.length;
		int crc = (int) 0xFFFFFFFF;
		for (i = 0; i < iCount; i++) {
			crc = ((crc >>> 8) & 0x00FFFFFF) ^ CrcTable[(crc ^ data[i]) & 0xFF];
		}
		crc = crc ^ 0xFFFFFFFF;
		temdata[3] = (byte) (crc & 0xFF);
		temdata[2] = (byte) (crc >>> 8);
		temdata[1] = (byte) (crc >>> 16);
		temdata[0] = (byte) (crc >>> 24);

		return temdata;
	}

	private static byte[] beforeAesByte(byte[] firstinfo, byte addbyte) {
		byte[] secondinfo;
		if (firstinfo.length % 16 != 0) {
			secondinfo = new byte[firstinfo.length / 16 * 16 + 16];
			System.arraycopy(firstinfo, 0, secondinfo, 0, firstinfo.length);
			for (int i = 0; i < 16 - (firstinfo.length % 16); i++) {
				secondinfo[firstinfo.length + i] = addbyte;
			}
		} else {
			secondinfo = firstinfo;
		}
		return secondinfo;
	}

	/**
	 * 设置升级设备更新进度
	 * 
	 * @param countNum
	 * @param currentNum
	 */
	private static void updateProgress(String deviceId, int countNum, int currentNum) {
		int progressValue = currentNum / countNum * 100;
		Map<String, String> upgradeDeviceProgress = ((Map<String, String>) session.getAttribute("upgradeDeviceProgress"));
		if (upgradeDeviceProgress == null) {
			upgradeDeviceProgress = new HashMap<String, String>();
		}
		upgradeDeviceProgress.put(deviceId, "已完成" + progressValue + "%。");
		session.setAttribute("upgradeDeviceProgress", upgradeDeviceProgress);
	}

	/**
	 * 设置升级设备名称
	 */
	private static void updateProgressDeviceName(String deviceId, String deviceName) {
		Map<String, String> upgradeDeviceName = (Map<String, String>) session.getAttribute("upgradeDeviceName");
		if (upgradeDeviceName == null) {
			upgradeDeviceName = new HashMap<String, String>();
		}
		upgradeDeviceName.put(deviceId, "“" + deviceName + "”正在升级...");
		session.setAttribute("upgradeDeviceName", upgradeDeviceName);
	}

	/**
	 * 清除固件升级session信息
	 */
	public static void updateProgressSessionMsgClear() {
		session.setAttribute("upgradeDeviceOver", null);
		session.setAttribute("upgradeDeviceName", null);
		session.setAttribute("upgradeDeviceProgress", null);
		session.setAttribute("upgradeDeviceWaitStatus", null);
	}

	/**
	 * 设置已完成数据发送的设备信息
	 */
	private static void updateProgressOver(String deviceId, String deviceName, String ip, int port, boolean isFlag,
			String msg) {
		String overStr = "“" + deviceName + "”结束升级，" + (isFlag ? "等待反馈！" : "升级失败！");
		if (!StringUtils.isEmpty(msg)) {
			overStr += msg;
		}

		Map<String, String> upgradeDeviceOver = (Map<String, String>) session.getAttribute("upgradeDeviceOver");
		if (upgradeDeviceOver == null) {
			upgradeDeviceOver = new HashMap<String, String>();
		}
		upgradeDeviceOver.put(deviceId, overStr);
		session.setAttribute("upgradeDeviceOver", upgradeDeviceOver);

		Map<String, String> upgradeDeviceName = (Map<String, String>) session.getAttribute("upgradeDeviceName");
		Map<String, String> upgradeDeviceProgress = (Map<String, String>) session.getAttribute("upgradeDeviceProgress");
		if (upgradeDeviceName != null) {
			upgradeDeviceName.remove(deviceId);
		}
		if (upgradeDeviceProgress != null) {
			upgradeDeviceProgress.remove(deviceId);
		}

		if (isFlag) {
			updateProgressSessionWaitResult(deviceId, ip, port);
		}
	}

	/**
	 * 设置等待查询反馈状态设备
	 * 
	 * @param deviceId
	 * @param ip
	 * @param port
	 */
	private static void updateProgressSessionWaitResult(String deviceId, String ip, int port) {
		Map<String, String> upgradeDeviceWaitStatus = (Map<String, String>) session
				.getAttribute("upgradeDeviceWaitStatus");
		String waitStr = ip + "&" + port;
		if (upgradeDeviceWaitStatus == null) {
			upgradeDeviceWaitStatus = new HashMap<String, String>();
		}
		upgradeDeviceWaitStatus.put(deviceId, waitStr);
		session.setAttribute("upgradeDeviceWaitStatus", upgradeDeviceWaitStatus);
	}

	/************************************************
	 * 函数功能：网卡CRC32效验 输入：需要做计算的UInt32数组 输出：返回的4字节的数组 作者：周辉 日期：2012-10-19
	 * 最近修改日期：无 最近修改内容：无
	 ***********************************************/
	public static int CRC32(int[] InputData, int len) {
		int dwPolynomial = 0x04c11db7;

		int xbit;
		int data;
		int crc_cnt = (int) 0xFFFFFFFF; // init
		for (int i = 0; i < len; i++) {
			xbit = (int) 0x80000000;
			data = InputData[i];
			for (int bits = 0; bits < 32; bits++) {
				if ((crc_cnt & 0x80000000) > 0) {
					crc_cnt <<= 1;
					crc_cnt ^= dwPolynomial;
				} else {
					crc_cnt <<= 1;
				}
				if ((data & xbit) > 0) {
					crc_cnt ^= dwPolynomial;
				}
				xbit >>>= 1;
			}
		}
		return crc_cnt;
	}

	/************************************************
	 * 函数功能：网卡CRC32效验 输入：需要做计算的UInt32数组 输出：返回的4字节的数组 作者：周辉 日期：2012-10-19
	 * 最近修改日期：无 最近修改内容：无
	 ***********************************************/
	public static long CRC32bylong(long[] InputData, int len) {
		int dwPolynomial = 0x04c11db7;
		long xbit;
		long data;
		long crc_cnt = 0x00000000FFFFFFFFl; // 4294967295
		for (int i = 0; i < len; i++) {
			xbit = (long) 0x0000000080000000l;// 2147483648
			data = InputData[i];
			for (int bits = 0; bits < 32; bits++) {
				if ((crc_cnt & 0x80000000) > 0) {
					crc_cnt <<= 1; // 8430404754
					crc_cnt = crc_cnt & 0x00000000FFFFFFFFl;
					crc_cnt ^= dwPolynomial;// 4215202377 4072462629 1073741824
				} else {
					crc_cnt <<= 1;
					crc_cnt = crc_cnt & 0x00000000FFFFFFFFl;
				}
				if ((data & xbit) > 0) {
					crc_cnt ^= dwPolynomial;// 4215202377 4294967294
				}
				xbit >>= 1;// 1073741824 536870912 1073741824
			}
		}
		return crc_cnt;
	}

	public static int getUpgradeStatus() {
		return upgradeStatus;
	}

	/**
	 * 版本号比较
	 * 
	 * @param versionNo
	 * @param armVersionNo
	 * @param ndspVersionNo
	 * @return
	 */
	private static boolean compareVersion(String versionNo, String armVersionNo, String ndspVersionNo,
			String rdspVersionNo) {
		String[] versionNos = versionNo.split("_");
		String[] armVersionNos = armVersionNo.split("_");
		String[] ndspVersionNos = ndspVersionNo.split("_");
		String[] rdspVersionNos = rdspVersionNo.split("_");

		/**
		 * arm
		 */
		if (versionNos[0].equals(armVersionNos[0])) {
			if (!versionNos[1].equals(armVersionNos[1])) {
				return false;
			}

			if (versionNos[2].compareTo(armVersionNos[2]) < 0) {
				return false;
			}
		}

		/**
		 * ndsp
		 */
		if (versionNos[0].equals(ndspVersionNos[0])) {
			if (!versionNos[1].equals(ndspVersionNos[1])) {
				return false;
			}

			if (versionNos[2].compareTo(ndspVersionNos[2]) < 0) {
				return false;
			}
		}

		/**
		 * rdsp
		 */
		if (versionNos[0].equals(rdspVersionNos[0])) {
			if (!versionNos[1].equals(rdspVersionNos[1])) {
				return false;
			}

			if (versionNos[2].compareTo(rdspVersionNos[2]) < 0) {
				return false;
			}
		}
		return true;
	}
}
