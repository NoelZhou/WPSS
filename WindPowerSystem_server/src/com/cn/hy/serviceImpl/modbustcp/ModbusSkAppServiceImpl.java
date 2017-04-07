package com.cn.hy.serviceImpl.modbustcp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.modbustcp.ModbusSkAppDao;
import com.cn.hy.pojo.modbustcp.ModbusSkApp;
import com.cn.hy.service.modbustcp.ModbusSkAppService;

@Service("ModbusSkAppServiceImpl")
public class ModbusSkAppServiceImpl implements ModbusSkAppService {
	@Resource
	private ModbusSkAppDao modbusSkAppDao;
	/**
	 * 获取modbustcp_sk_app中指定参数地址信息
	 */
	@Override
	public ModbusSkApp getModbusSkApp(int addr,int modbus_type) {
		ModbusSkApp msa = new ModbusSkApp();
		msa.setAddr(addr);
		msa.setModbus_type(modbus_type);
		return modbusSkAppDao.getModbusSkApp(msa);
	}
	/**
	 * 根据id查找modbustcp_sk_app中对应的参数地址
	 */
	@Override
	public ModbusSkApp getModbusSkAppById(int id) {
		// TODO Auto-generated method stub
		return modbusSkAppDao.getModbusSkAppById(id);
	}
	/**
	 * 获取同一协议类型下所有参数地址
	 */
	@Override
	public List<ModbusSkApp> listModbusSkApp(int modbus_type) {
		return modbusSkAppDao.listModbusSkApp(modbus_type);
	}
	/**
	 * 查找基本参数配置的参数地址
	 */
	@Override
	public List<ModbusSkApp> alistModbusSkApp(int modbus_type) {
		return modbusSkAppDao.alistModbusSkApp(modbus_type);
	}
	/**
	 * 更新选中的基本参数
	 */
	@Override
	public void updateModbusSkApp(ModbusSkApp modbusSkApp) {
		modbusSkAppDao.updateModbusSkApp(modbusSkApp);
	}

}
