package com.cn.hy.service.system;

import java.util.List;

import com.cn.hy.pojo.system.Device;

public interface DeviceService {
	/**
	 * 根据设备id获取数据
	 * @param id
	 * @return
	 */
	public Device selectDeviceById(int id);
	/**
	 * 获取设备列表
	 * @return
	 */
	public List<Device> listDevice();
	/**
	 * 保存设备
	 * @param device
	 * @return
	 */
	public int saveDevice(Device device);
	/**
	 * 修改设备
	 * @param device
	 */
	public void updateDevice(Device device);
	/**
	 * 删除设备
	 * @param id
	 */
	public void deleteDevice(int id);
	/**
	 * 根据设备id获取协议类型
	 * @param id
	 * @return
	 */
	public Device findByModbus_type(int id);
}
