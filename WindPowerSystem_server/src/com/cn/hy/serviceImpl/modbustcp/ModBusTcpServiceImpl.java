package com.cn.hy.serviceImpl.modbustcp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.modbustcp.ModBusTcpDao;
import com.cn.hy.pojo.modbustcp.ModBusParame;
import com.cn.hy.pojo.modbustcp.ModbusBit;
import com.cn.hy.service.modbustcp.ModBusTcpService;

@Service("ModBusTcpServiceImpl")
public class ModBusTcpServiceImpl implements ModBusTcpService {
	@Resource
	private ModBusTcpDao modbusdao;

	/**
	 * 协议解析
	 */
	@Override
	public List<String> parseModBusTcp(int addr, int shortvalue, int device_type, String modbustsr) {
		// TODO Auto-generated method stub
		ModBusParame mbparame = new ModBusParame();
		mbparame.setDevice_type(device_type);
		mbparame.setShortvalue(shortvalue);
		mbparame.setAddr(addr);
		// 查找协议地址的bit位数
		int ctbit = modbusdao.getaddrbit(mbparame);
		// 获取协议地址的var位数
		int ctvar = modbusdao.getaddrvar(mbparame);
		List<String> modbusvaluelist = new ArrayList<String>();
		// 获取指定地址的协议类型
		List<Integer> array_type = modbusdao.getaddrarrarytype(addr, device_type);

		// bit位解析
		// 特殊位置的解析
		// 双馈 使用字符比较 不能转换成int
		if (addr == 233 && device_type == 0) {
			if (modbustsr.split(",")[232].equals("51")) {
				// 获取84位的bit位置
				List<ModbusBit> bitlist = modbusdao.getaddrbitlistbyid(84, device_type);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else if (modbustsr.split(",")[232].equals("68")) {
				List<ModbusBit> bitlist = modbusdao.getaddrbitlist(mbparame);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else {
				modbusvaluelist.add("0");
				modbusvaluelist.add("不解析");
			}
		} else if (addr == 73 && device_type == 0) {
			if (modbustsr.split(",")[72].equals("51")) {
				// 获取84位的bit位置
				List<ModbusBit> bitlist = modbusdao.getaddrbitlistbyid(54, device_type);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else if (modbustsr.split(",")[72].equals("68")) {
				// 获取指定地址的所有bit信息
				List<ModbusBit> bitlist = modbusdao.getaddrbitlist(mbparame);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else {
				modbusvaluelist.add("0");
				modbusvaluelist.add("不解析");
			}
		} else if (addr == 233 && device_type == 1) {
			// 全功率
			if (modbustsr.split(",")[232].equals("4147")) {
				// 获取84位的bit位置
				ModBusParame mbparameone = new ModBusParame();
				mbparameone.setDevice_type(device_type);
				mbparameone.setShortvalue(shortvalue);
				mbparameone.setPoint_id(114);
				// 根据id获取var
				String getvar = modbusdao.getvarbyid(mbparameone);
				modbusvaluelist.add("0");
				modbusvaluelist.add(getvar);
			} else if (modbustsr.split(",")[232].equals("238")) {
				// 获取指定地址的所有bit信息
				List<ModbusBit> bitlist = modbusdao.getaddrbitlist(mbparame);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else {
				modbusvaluelist.add("0");
				modbusvaluelist.add("不解析");
			}
		} else if (addr == 73 && device_type == 1) {
			// 全功率
			if (modbustsr.split(",")[72].equals("4147")) {
				// 获取84位的bit位置 位4147是 解析到id位54位上
				ModBusParame mbparameone = new ModBusParame();
				mbparameone.setDevice_type(device_type);
				mbparameone.setShortvalue(shortvalue);
				mbparameone.setPoint_id(54);
				String getvar = modbusdao.getvarbyid(mbparameone);
				modbusvaluelist.add("0");
				modbusvaluelist.add(getvar);
			} else if (modbustsr.split(",")[72].equals("238")) {
				// 获取指定地址的所有bit信息
				List<ModbusBit> bitlist = modbusdao.getaddrbitlist(mbparame);
				modbusvaluelist = getbitlist(bitlist, shortvalue);
			} else {
				modbusvaluelist.add("0");
				modbusvaluelist.add("不解析");
			}
		} else if (ctbit != 0) {
			// bit值组合
			List<ModbusBit> bitlist = modbusdao.getaddrbitlist(mbparame);
			modbusvaluelist = getbitlist(bitlist, shortvalue);

		} else if (ctvar != 0) {
			// var值解析
			String str = modbusdao.getaddvarvalue(mbparame);
			if (str == null || str == "") {
				modbusvaluelist.add("4");
				modbusvaluelist.add("地址解析错误：" + addr + "位的值" + shortvalue + "，设备类型为：" + device_type);
			} else {
				modbusvaluelist.add("1");
				modbusvaluelist.add(str);
			}
		} else if (array_type.contains(8) || (addr == 30 && device_type != 2) || (addr == 12996 && device_type == 2)
				|| (addr == 515 && device_type != 2)) {
			// 电机参数 和发电总量
			short[] arraytmp = new short[2];
			if (addr >= 12788) {
				addr = addr - 12788;
			}
			arraytmp[0] = (short) Integer.parseInt(modbustsr.split(",")[addr]);
			arraytmp[1] = (short) Integer.parseInt(modbustsr.split(",")[addr + 1]);
			int unshort = unshortToInt(arraytmp, 0);
			/*
			 * if(addr==30||addr==12996){ int num = (int) Math.ceil(unshort /
			 * 10); int numy = (int) Math.ceil(unshort % 10); if (numy != 0) {
			 * num++; } unshort=num; }
			 */
			modbusvaluelist.add("2");
			modbusvaluelist.add(unshort + "");
			// System.out.println(unshort);
		} else {
			// 查询系数cof
			Integer cof = modbusdao.getaddrcof(mbparame);
			if (cof == null) {
				modbusvaluelist.add("2");
				modbusvaluelist.add(shortvalue + "");
			} else {
				if (cof == 1) {
					modbusvaluelist.add("2");
					modbusvaluelist.add(shortvalue + "");
				} else {
					float ab = shortvalue;
					modbusvaluelist.add("2");
					modbusvaluelist.add(ab / cof + "");
				}
			}
		}
		// 需要16进制显示的addr是哪些
		// return "地址解析错误";
		return modbusvaluelist;
	}

	public static int unshortToInt(short[] unshort, int off) {
		int b0 = unshort[off] & 0xFFFF;
		int b1 = unshort[off + 1] & 0xFFFF;
		return (b1 << 16) | b0;
	}

	/**
	 * bit位数组
	 * 
	 * @param bitlist
	 * @param shortvalue
	 * @return
	 */
	public List<String> getbitlist(List<ModbusBit> bitlist, int shortvalue) {
		List<String> modbusvaluelist = new ArrayList<String>();
		short sv = (short) shortvalue;
		String bstr = Integer.toBinaryString(sv);
		String bitstr = "";
		int lgth = bstr.length();
		if (lgth > 16) {
			bstr = bstr.substring(bstr.length() - 16, bstr.length());
			lgth = bstr.length();
		}
		// bit位数大于 真实值 43
		if (bitlist.size() < lgth) {
			lgth = bitlist.size();
		}
		for (int i = lgth - 1; i >= 0; i--) {

			int state = Integer.parseInt((bstr.substring(i, i + 1)));
			ModbusBit bitdemo = bitlist.get(lgth - i - 1);
			bitdemo.setShowstate(state);
			if (lgth > 15) {
				if (i == 0) {
					bitstr += bitdemo.toString();
				} else {
					bitstr += bitdemo.toString() + "|";
				}
			} else {
				bitstr += bitdemo.toString() + "|";
			}
		}
		for (int j = lgth; j < bitlist.size(); j++) {
			ModbusBit bitdemonew = bitlist.get(j);
			bitdemonew.setShowstate(0);
			if (j == bitlist.size() - 1) {
				bitstr += bitdemonew.toString();
			} else {
				bitstr += bitdemonew.toString() + "|";
			}
		}
		modbusvaluelist.add("0");
		modbusvaluelist.add(bitstr.replace(" ", ""));
		return modbusvaluelist;

	}

	/**
	 * 合并2个字
	 * 
	 * @param bytes
	 * @param off
	 * @return
	 */
	public static short byte4ToInt(byte[] bytes, int off) {
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		short ii = (short) ((b0 << 8) | b1);
		return ii;
	}

}
