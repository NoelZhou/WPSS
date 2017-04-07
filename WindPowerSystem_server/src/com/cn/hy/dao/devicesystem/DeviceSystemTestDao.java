package com.cn.hy.dao.devicesystem;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.modbustcp.DeviceControlParme;
import com.cn.hy.pojo.modbustcp.ModBusVar;

public interface DeviceSystemTestDao {
	/**
	 * 获取控制模式的指令和对应的Bit位
	 * @param controlParame
	 * @param modbustcp_type
	 * @return
	 */
	DeviceControlParme getControlModelTest(@Param(value="controlParame") String controlParame,@Param(value="modbustcp_type") int modbustcp_type);
	/**
	 * 获取所有控制模式值
	 * @param modbustcp_type
	 * @param model_type 
	 * @return
	 */
	List<DeviceControlParme> getControlModelTestList(@Param(value="modbustcp_type")int modbustcp_type,@Param(value="model_type") int model_type);
	/**
	 * 获取模式类型
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	List<ModBusVar> getparamevalue(@Param(value="addr")int addr,@Param(value="modbustcp_type") int modbustcp_type);
	/**
	 * 获取解析方式
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewall(@Param(value="addr")int addr,@Param(value="modbustcp_type") int modbustcp_type);
	/**
	 * 获取片接地址
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewallzh(@Param(value="addr") String addr, @Param(value="modbustcp_type") int modbustcp_type);
	/**
	 * 获取复位指令地址
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String getaddrparasewallzh_fw(@Param(value="addr") String addr, @Param(value="modbustcp_type") int modbustcp_type);

}
