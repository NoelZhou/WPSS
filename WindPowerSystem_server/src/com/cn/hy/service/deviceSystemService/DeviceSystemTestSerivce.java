package com.cn.hy.service.deviceSystemService;

import java.util.List;
import com.cn.hy.pojo.modbustcp.DeviceControlParme;
import com.cn.hy.pojo.modbustcp.ModBusVar;


public interface DeviceSystemTestSerivce {
	/**
	 * 获取控制模式的指令和对应的Bit位
	 * @param controlParame
	 * @param modbustcp_type
	 * @return
	 */
	DeviceControlParme getControlModelTest( String controlParame, int modbustcp_type);
	/**
	 * 获取所有控制模式值
	 * @param modbustcp_type
	 * @param i
	 * @return
	 */
	List<DeviceControlParme> getControlModelTestList(int modbustcp_type, int i);
	/**
	 * 获取模式类型
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	List<ModBusVar> getparamevalue(int addr, int modbustcp_type);
	/**
	 * 获取解析方式
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewall(int addr, int modbustcp_type);
	/**
	 * 获取片接地址
	 * @param p
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewallzh(String p, int modbustcp_type);
	/**
	 * 获取复位指令地址
	 * @param ids
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewallzh_fw(String ids, int modbustcp_type);

}
