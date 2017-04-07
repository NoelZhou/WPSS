package com.cn.hy.service.modbustcp;

import java.util.List;

import com.cn.hy.pojo.modbustcp.ModbusSkApp;

public interface ModbusSkAppService {
	/**
	 * 获取modbustcp_sk_app中指定参数地址信息
	 * @param addr
	 * @param modbus_type
	 * @return
	 */
	public ModbusSkApp getModbusSkApp(int addr,int modbus_type);
	/**
	 * 根据id查找modbustcp_sk_app中对应的参数地址
	 * @param id
	 * @return
	 */
	public ModbusSkApp getModbusSkAppById(int id);
	/**
	 * 获取同一协议类型下所有参数地址
	 * @param modbus_type
	 * @return
	 */
	public List<ModbusSkApp> listModbusSkApp(int modbus_type);
	/**
	 * 查找基本参数配置的参数地址
	 * @param modbus_type
	 * @return
	 */
	public List<ModbusSkApp> alistModbusSkApp(int modbus_type);
	/**
	 * 更新选中的基本参数
	 * @param modbusSkApp
	 */
	public void updateModbusSkApp(ModbusSkApp modbusSkApp);
}
