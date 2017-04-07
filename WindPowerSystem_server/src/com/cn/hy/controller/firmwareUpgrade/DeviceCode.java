package com.cn.hy.controller.firmwareUpgrade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cn.hy.bean.JDBConnection;
import com.cn.hy.pojo.firmwareUpgrade.DevAttr;
import com.cn.hy.pojo.firmwareUpgrade.Mcu;
import com.cn.hy.pojo.firmwareUpgrade.Module;
import com.cn.hy.pojo.firmwareUpgrade.Pcb;
import com.cn.hy.pojo.firmwareUpgrade.section;

public class DeviceCode {
	public static int datalength = 0;
	public static byte[] data = null;
	static String dcrcinfostr = "";
	public static String[] sTxtlines;
	public static List<section> ListSection = new ArrayList<section>(); // 获得所有的Section名称
	public static List<section> ListPcb = new ArrayList<section>(); // 获得所有的Module前面的PCB名称
	public static List<section> ListModule = new ArrayList<section>(); // 获得所有的Module名称
	public static List<section> ListModuPcb = new ArrayList<section>(); // 获得所有的Module后面的pcb名称

	static String getSguAppversion(String path) {
		String[] saryHead = GetFielBaseMess(path);
		String[] sappver = getSguAppVer(saryHead);
		return sappver[2];
	}

	private static String[] getSguAppVer(String[] saryHead) {
		// 根据SGU代码区的信息头，在SGU的信息体中获得MCU的APP版本号
		// 参数1:[MODU1] 若没有，为空 参数2：[PCB1] 参数3：[MCU1] 参数4：SGU信息体.txt
		// 返回 整机版本号,PCB板号，APP版本号
		String smoduname = saryHead[0];
		String spcbname = saryHead[1];
		String smcuname = saryHead[2];
		String[] sApp = new String[4]; // 2013.1.7 Add 型号MOD
		// string sPath0 = sPath + "\\information.txt";
		DevAttr dev = GetAttr();
		sApp[0] = dev.getDver(); // 整机版本号
		sApp[3] = dev.getMod(); // 整机型号
		if (smoduname.equals("")) // 与模组同级的PCB 两层
		{
			for (Pcb pcbx : dev.getPcb()) {
				if (pcbx.getPcbname().equals(spcbname)) {
					sApp[1] = pcbx.getPbno();
					for (Mcu mcux : pcbx.getMcu()) {
						if (mcux.getMcuname().equals(smcuname)) {
							sApp[2] = mcux.getApp();
							break;
						}
					}
				}
			}
		} else // 模组 三层
		{
			for (Module modux : dev.getModule()) {
				if (modux.getModuname().equals(smoduname)) {
					for (Pcb pcbx : modux.getPcb()) {
						if (pcbx.getPcbname().equals(spcbname)) {
							sApp[1] = pcbx.getPbno();
							for (Mcu mcux : pcbx.getMcu()) {
								if (mcux.getMcuname().equals(smcuname)) {
									sApp[2] = mcux.getApp();
									break;
								}
							}
						}
					}
				}
			}
		}

		return sApp;
	}

	public static DevAttr GetAttr() {
		// String[] sTxtlines=new String[1024];
		DevAttr devattr = new DevAttr();
		// String[] allstr=dcrcinfostr.split("/r");
		// 去掉空白字符
		String[] sAllLine = dcrcinfostr.split("\r\n"); // 读取.tet 所有的行
		sTxtlines = new String[sAllLine.length];

		for (int iline = 0; iline < sAllLine.length; iline++) {
			sTxtlines[iline] = sAllLine[iline].trim();
		}
		// sTxtlines = File.ReadAllLines(sPath, Encoding.Default); //读取.text
		// 所有的行

		ListSection = getAllSection(); // 获得所有的Section名称 和 模组名称

		// 整机
		DevAttr tempDevAttr = sDevAttrDate(); // 获得区间段内的 属性
		devattr.setMod(tempDevAttr.getMod());
		devattr.setDver(tempDevAttr.getDver());
		devattr.setHver(tempDevAttr.getHver());
		devattr.setSver(tempDevAttr.getSver());

		ListPcb = getPcbSetion();
		devattr.setPcbCnt(ListPcb.size());
		devattr.setPcb(new Pcb[devattr.getPcbCnt()]);
		int iPcbIdx = 0;
		// 整机下，与模组同级的PCB
		for (section pcbx : ListPcb) {
			Pcb pcb0 = new Pcb();
			Pcb temppcb = sPcbAttrDate(pcbx.getSsLocation()); // 获得pcb的区间信息
			pcb0.setPcbname(pcbx.getSsectionName());
			pcb0.setPn(temppcb.getPn());
			pcb0.setPbno(temppcb.getPbno());
			pcb0.setPbom(temppcb.getPbom());
			pcb0.setPbsn(temppcb.getPbsn());
			// MCU
			ArrayList<section> listmcu = getPcbMcu(pcbx); // 获得pcbx下面的mcu
			pcb0.setMcuCnt(listmcu.size());
			pcb0.setMcu(new Mcu[listmcu.size()]);
			int iPcbMcuIdx = 0;
			for (section mcux : listmcu) {
				Mcu mcu0 = new Mcu();
				Mcu tempmcu = sMcuAttrDate(mcux.getSsLocation());
				mcu0.setMcuname(mcux.getSsectionName());
				mcu0.setChip(tempmcu.getChip());
				mcu0.setOs(tempmcu.getOs());
				mcu0.setApp(tempmcu.getApp());
				mcu0.setBoot(tempmcu.getBoot());
				pcb0.getMcu()[iPcbMcuIdx++] = mcu0;
			}
			devattr.getPcb()[iPcbIdx++] = pcb0;
		}

		// MODUlE
		int moduIdx = 0; // 记录module 的个数
		devattr.setModuCnt(getNum(ListModule));
		devattr.setModule(new Module[devattr.getModuCnt()]);
		for (section modux : ListModule) // [MODU1,2,3] 和 [MODU4] 和 [MODU5]
		{
			String[] sModuName = modux.getSsectionName().trim().split("[,]", -1); // 3,1,1
			Module tempmodule = sModuAttrDate(modux.getSsLocation()); // get
																		// 模组的区间信息
			String smod = tempmodule.getMod();
			String sdver = tempmodule.getDver();
			String shver = tempmodule.getHver();
			String ssn = tempmodule.getSn();

			ArrayList<section> listModuPcb = getModuPcb(modux); // 搜索模组后面的pcb
																// [PCB1,1,1] 或
																// [PCB4]

			for (int im = 0; im < sModuName.length; im++) {
				Module module = new Module();
				module.setModuname(GsetionName(sModuName, im, "[MODU")); // "[MODU"
																			// +
																			// (im
																			// +
																			// 1).ToString()
																			// +
																			// "]";
				module.setMod(smod);
				module.setDver(sdver);
				module.setHver(shver);
				module.setSn(ssn);
				module.setPcbCnt(listModuPcb.size()); // getNum(listModuPcb);
														// //每个模组后面的pcb个数
				module.setPcb(new Pcb[module.getPcbCnt()]);
				int moduPcbIdx = 0; // 记录module下面的PCB的个数
				// ADD pcb [PCB1,1,1] 和 [PCB2,2,2] 或 [PCB4] 和 [PCB5]
				for (section ModuPcbx : listModuPcb) {
					Pcb pcb = new Pcb();
					Pcb temppcb = sPcbAttrDate(ModuPcbx.getSsLocation());
					pcb.setPcbname(ModuPcbx.getSsectionName().split("[,]", -1)[0] + "]"); // 都是模组下面的第一个PCB1
					pcb.setPn(temppcb.getPn());
					pcb.setPbno(temppcb.getPbno());
					pcb.setPbom(temppcb.getPbom());
					pcb.setPbsn(GsetionName(temppcb.getPbsn().split("[,]", -1), im, "")); // temppcb.pbsn;
																							// 类似这样的解析
																							// 2222,3333,3444

					ArrayList<section> listModuPcbMcu = getPcbMcu(ModuPcbx); // 搜索PCB后面的MCU
																				// [MCU1,2,3]
																				// 或
																				// [MCU4]
					pcb.setMcuCnt(listModuPcbMcu.size());
					pcb.setMcu(new Mcu[pcb.getMcuCnt()]);
					int iModuPcbMcuIdx = 0; // 记录module下面的PCB下面的MCU的个数

					// ADD MCU [MCU1,1,1] 或 [MCU4]
					for (section ModuPcbMcux : listModuPcbMcu) {
						Mcu mcu = new Mcu();
						Mcu tempmcu = sMcuAttrDate(ModuPcbMcux.getSsLocation());
						mcu.setMcuname(ModuPcbMcux.getSsectionName().split("[,]", -1)[0] + "]"); // 都是模组下的PCB1下的MCU1
						mcu.setOs(tempmcu.getOs());
						mcu.setBoot(tempmcu.getBoot());
						mcu.setChip(tempmcu.getChip());
						mcu.setApp(tempmcu.getApp());

						pcb.getMcu()[iModuPcbMcuIdx++] = mcu;
					}

					module.getPcb()[moduPcbIdx++] = pcb;

				}

				devattr.getModule()[moduIdx++] = module;
			}

		}

		return devattr;
	}

	// 搜索某个模组后的pcb
	private static ArrayList<section> getModuPcb(section sModu) {
		ArrayList<section> list1 = new ArrayList<section>();

		for (int i = sModu.getSsLocation() + 1; i < sTxtlines.length; i++) {

			if (sTxtlines[i].contains("[MODU")) {
				return list1; // 遇到 MODU结束
			} else if (sTxtlines[i].contains("[PCB")) {
				section sec1 = new section();
				sec1.setSsLocation(i);
				sec1.setSsectionName(sTxtlines[i]);
				list1.add(sec1);
			}
		}

		return list1;
	}

	// Module 区间段属性 参数1:起始行
	private static Module sModuAttrDate(int sline) {
		Module sret = new Module();

		for (int i = sline + 1; i < sTxtlines.length; i++) // [DEV] ~ [PCBX]
		{
			if (ReadLeft(sTxtlines[i]).equals("MOD")) {
				sret.setMod(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("DVER")) {
				sret.setDver(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("HVER")) {
				sret.setHver(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("SN")) {
				sret.setSn(ReadRight(sTxtlines[i]));
			} else if (sTxtlines[i].contains("[")) {
				break;
			}
		}
		return sret;
	}

	// 返回个数
	private static int getNum(List<section> list1) {
		int num = 0;
		for (section item : list1) {
			num += item.getSsectionName().split("[,]", -1).length;
		}
		return num;
	}

	// Mcu 区间段属性 参数1:起始行
	private static Mcu sMcuAttrDate(int sline) {
		Mcu sret = new Mcu();
		;

		for (int i = sline + 1; i < sTxtlines.length; i++) // [DEV] ~ [PCBX]
		{
			if (ReadLeft(sTxtlines[i]).equals("CHIP")) {
				sret.setChip(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("BOOT")) {
				sret.setBoot(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("OS")) {
				sret.setOs(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("APP")) {
				sret.setApp(ReadRight(sTxtlines[i]));
			} else if (sTxtlines[i].contains("[")) {
				break;
			}
		}
		return sret;
	}

	// 搜索某个PCB下面的MCU, 不区分是否在模组后
	private static ArrayList<section> getPcbMcu(section spcb) {

		ArrayList<section> listmcu = new ArrayList<section>(); // spcb下面的mcu

		//
		for (int i = spcb.getSsLocation() + 1; i < sTxtlines.length; i++) {
			if (sTxtlines[i].contains("[PCB")) {
				return listmcu; // 遇到 Pcb结束
			} else if (sTxtlines[i].contains("[MCU")) {
				section sec1 = new section();
				sec1.setSsLocation(i);
				sec1.setSsectionName(sTxtlines[i]);
				listmcu.add(sec1);
			}
		}
		return listmcu;
	}

	// PCB 区间段属性 参数1:起始行
	private static Pcb sPcbAttrDate(int sline) {
		Pcb sret = new Pcb();

		for (int i = sline + 1; i < sTxtlines.length; i++) // [DEV] ~ [PCBX]
		{
			if (ReadLeft(sTxtlines[i]).equals("PN")) {
				sret.setPn(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("PBNO")) {
				sret.setPbno(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("PBOM")) {
				sret.setPbom(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("PBSN")) {
				sret.setPbsn(ReadRight(sTxtlines[i]));
			} else if (sTxtlines[i].contains("[")) {
				break;
			}
		}
		return sret;
	}

	// 搜索在"MODU"字段之前所有的PCB
	private static ArrayList<section> getPcbSetion() {
		ArrayList<section> listDevPcb = new ArrayList<section>();
		// 在所有的字段中检索
		for (section item : ListSection) {
			if (item.getSsectionName().contains("MODU")) {
				return listDevPcb;
			} else if (item.getSsectionName().contains("[PCB")) {
				listDevPcb.add(item);
			}
		}
		return listDevPcb;

	}

	// DEV 区间段属性 参数1：起始行
	private static DevAttr sDevAttrDate() {
		DevAttr sret = new DevAttr();

		for (int i = 1; i < sTxtlines.length; i++) // [DEV] ~ [PCBX]
		{
			if (ReadLeft(sTxtlines[i]).equals("MOD")) {
				sret.setMod(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("DVER")) {
				sret.setDver(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("HVER")) {
				sret.setHver(ReadRight(sTxtlines[i]));
			} else if (ReadLeft(sTxtlines[i]).equals("SVER")) {
				sret.setSver(ReadRight(sTxtlines[i]));
			} else if (sTxtlines[i].contains("[")) {
				break; // 遇到下一个Section 返回
			}
		}
		return sret;
	}

	// Get = 左边
	private static String ReadLeft(String sline) {
		String sret = "";
		if (sline.contains("=")) {
			String[] sary = sline.split("[=]", -1);
			sret = sary[0];
		}
		return sret;
	}

	// Get = 右边
	private static String ReadRight(String sline) {
		String sret = "";
		if (sline.contains("=")) {
			String[] sary = sline.split("[=]", -1);
			sret = sary[1];
		}
		return sret;
	}

	// 参数：[MODU1,2,3] 参数2：0或1或2 参数3：[MODU 或[PCB 或[MCU 字段标识头
	// 返回: [MODU1] [MODU2] [MODU3] add pbsn=2222,2333,3444或pbsn=2222的解析
	private static String GsetionName(String[] sname, int idex, String slab) {
		String sr = "";
		if (idex + 1 > sname.length) {
			return "";
		}
		if (sname.length != 1) {
			if (!slab.equals("")) // [MODU1,2,3]的name解析
			{
				if (!sname[idex].contains(slab)) // 2 3]
				{
					if (sname[idex].contains("]")) {
						sr = (slab + sname[idex]).trim();
					} else {
						sr = (slab + sname[idex] + "]").trim();
					}
				} else {
					sr = (sname[idex] + "]").trim(); // [MODU1
				}
			} else // pbsn=2222,2333,3444
			{
				sr = sname[idex].trim();
			}
		} else {
			sr = sname[0].trim();
		}
		return sr;
	}

	// 记录下每个Section,及位置 获得所有的Module名称
	private static ArrayList<section> getAllSection() {
		ArrayList<section> list1 = new ArrayList<section>();
		ArrayList<section> listModu = new ArrayList<section>();
		int iidx = 0;
		for (String item : sTxtlines) {
			if (item.contains("[")) {
				if (item.contains("[MODU")) {
					section se10 = new section();
					se10.setSsectionName(item);
					se10.setSsLocation(iidx);
					listModu.add(se10);
				}
				section se1 = new section();
				se1.setSsectionName(item);
				se1.setSsLocation(iidx);
				list1.add(se1);
			}
			iidx++;
		}
		ListModule = listModu;
		return list1;
	}

	private static String[] GetFielBaseMess(String sgufile) {
		String[] retStr = new String[3];
		try { // TODO Auto-generated method stub

			// 解析信息包len,crc32,代码包CRC32
			byte[] byteArr2 = FileReadUtils.readFile(sgufile);

			int infolen = TypeUtils.byte2Int(byteArr2, 26 + 0);
			int infocrc = TypeUtils.byte2Int(byteArr2, 26 + 6);
			int codecrc = TypeUtils.byte2Int(byteArr2, 26 + 12);

			// 解密信息体
			byte[] beforedes = new byte[infolen];
			System.arraycopy(byteArr2, 64, beforedes, 0, infolen);

			byte[] dcrcinfo = AESUtils.decrypt(beforedes, "sdkjsdkj12987391");
			byte[] icrc32byte = CRC32Ret4(dcrcinfo);
			if (infocrc != TypeUtils.byte2Int(icrc32byte, 0)) {
				System.out.println("信息区CRC校验错误！");
				// return;
			}

			// 解密代码体
			byte[] beforecodedes = new byte[byteArr2.length - 64 - infolen];
			System.arraycopy(byteArr2, (64 + infolen), beforecodedes, 0, beforecodedes.length);
			byte[] dcrccode = AESUtils.decrypt(beforecodedes, "sdkjsdkj12987391"); // UTF8Encoding.UTF8.GetBytes(dinfotext);
			byte[] dcrc32byte = CRC32Ret4(dcrccode);

			if (codecrc != TypeUtils.byte2Int(dcrc32byte, 0)) {
				System.out.println("代码区CRC校验错误！");
				// return;
			}

			String checkcodestr = new String(dcrccode);
			String[] strtmp = checkcodestr.split("\r");
			if (strtmp[0].contains("[MODU")) {
				retStr[0] = strtmp[0];
				retStr[1] = strtmp[1].substring(1);
				retStr[2] = strtmp[2].substring(1);
			} else {
				retStr[0] = "";
				retStr[1] = strtmp[0];
				retStr[2] = strtmp[1].substring(1);
			}

			// 输出文件sgu
			// FileOperation.writeTxtFile1(dcrcinfo, new File(sgufile +
			// ".txt"));
			/// 输出文件sgu
			// FileOperation.writeTxtFile1(dcrccode, new File(sgufile +
			// ".bin"));
			// 检验code区
			// checkcodestr = FileOperation.readTxtFile(new File(sgufile +
			// ".bin"));
			dcrcinfostr = new String(dcrcinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retStr;
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

	@SuppressWarnings("unused")
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
	 * 字符串转成16进制
	 * 
	 * @param s
	 * @return
	 */
	public static String[] strtoHexString(String s) {
		String str[] = new String[s.length()];
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str[i] = s4;
		}
		return str;
	}

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

	public static String IntTounshort(int a) {
		short aa = (short) (a & 0xFFFF);
		short bb = (short) ((a >> 16) & 0xFFFF);
		return aa + "," + bb;
	}

	/*
	 * public static int[] ByteArrayToUInt32Array(byte[] bytes) { int getmodlen
	 * = bytes.length % 4 == 0 ? 0 : (4 - bytes.length % 4); byte[] newbytes =
	 * new byte[bytes.length + getmodlen]; for (int i = 0; i < bytes.length;
	 * i++) { newbytes[i] = bytes[i]; }
	 * 
	 * int[] u32 = new int[newbytes.length / 4]; for (int i = 0; i <
	 * newbytes.length / 4; i++) { u32[i] = Integer.parseInt(newbytes, i * 4); }
	 * return u32; }
	 */
	/**
	 * 修改设备的升级状态
	 * 
	 * @param deviceIdt
	 * @param type
	 */
	public static void updatestatebyid(String deviceIdt, int type) {
		// TODO Auto-generated method stub
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "";
			if (type == 1) {
				sql = "update windpower_device set rw_role_req='SJ' , rw_role_res='OK' where id=" + deviceIdt;
			} else {
				sql = "update windpower_device set rw_role_req='R' , rw_role_res='OK' where id=" + deviceIdt;
			}

			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
