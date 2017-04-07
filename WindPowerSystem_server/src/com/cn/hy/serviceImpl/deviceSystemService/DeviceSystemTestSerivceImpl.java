package com.cn.hy.serviceImpl.deviceSystemService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.devicesystem.DeviceSystemTestDao;
import com.cn.hy.pojo.modbustcp.DeviceControlParme;
import com.cn.hy.pojo.modbustcp.ModBusVar;
import com.cn.hy.service.deviceSystemService.DeviceSystemTestSerivce;
@Service("DeviceSystemTestSerivce")
public class DeviceSystemTestSerivceImpl implements DeviceSystemTestSerivce {
	@Resource
	private DeviceSystemTestDao devicesystemtestdao;
	/**
	 * 获取控制模式的指令和对应的Bit位
	 */
	@Override
	public DeviceControlParme getControlModelTest(String controlParame, int modbustcp_type) {
		return devicesystemtestdao.getControlModelTest(controlParame,modbustcp_type);
	}
	/**
	 * 获取所有控制模式值
	 */
	@Override
	public List<DeviceControlParme> getControlModelTestList(int modbustcp_type,int model_type) {
		return devicesystemtestdao.getControlModelTestList(modbustcp_type,model_type);
	}
	/**
	 * 获取模式类型
	 */
	@Override
	public List<ModBusVar> getparamevalue(int addr, int modbustcp_type) {
		return devicesystemtestdao.getparamevalue(addr,modbustcp_type);
	}
	/**
	 * 获取解析方式
	 */
	@Override
	public String getaddrparasewall(int addr, int modbustcp_type) {
		return devicesystemtestdao.getaddrparasewall(addr,modbustcp_type);
	}
	/**
	 * 获取片接地址
	 */
	@Override
	public String getaddrparasewallzh(String addr, int modbustcp_type) {
		return  devicesystemtestdao.getaddrparasewallzh(addr,modbustcp_type);
	}
	/**
	 * 获取复位指令地址
	 */
	@Override
	public String getaddrparasewallzh_fw(String ids, int modbustcp_type) {
		return  devicesystemtestdao.getaddrparasewallzh_fw(ids,modbustcp_type);
	}

}
