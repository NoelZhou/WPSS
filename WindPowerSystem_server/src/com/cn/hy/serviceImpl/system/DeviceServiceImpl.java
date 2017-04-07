package com.cn.hy.serviceImpl.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.DeviceDao;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.system.DeviceService;
@Service("DeviceServiceImpl")
public class DeviceServiceImpl implements DeviceService {
	@Resource
	private DeviceDao deviceDao;
	/**
	 * 根据设备id获取数据
	 */
	@Override
	public Device selectDeviceById(int id) {
		return deviceDao.selectDeviceById(id);
	}
	/**
	 * 获取设备列表
	 */
	@Override
	public List<Device> listDevice() {
		return deviceDao.listDevice();
	}
	/**
	 * 保存设备
	 */
	@Override
	public int saveDevice(Device device) {
		deviceDao.saveDevice(device);
		return device.getId();
	}
	/**
	 * 修改设备
	 */
	@Override
	public void updateDevice(Device device) {
		deviceDao.updateDevice(device);
	}
	/**
	 * 删除设备
	 */
	@Override
	public void deleteDevice(int id) {
		deviceDao.deleteDevice(id);
	}
	/**
	 * 根据设备id获取协议类型
	 */
	@Override
	public Device findByModbus_type(int id) {
		return deviceDao.findByModbus_type(id);
	}

}
