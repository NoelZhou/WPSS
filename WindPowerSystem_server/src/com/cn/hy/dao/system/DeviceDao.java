package com.cn.hy.dao.system;

import java.util.List;

import com.cn.hy.pojo.system.Device;

public interface DeviceDao {
	/**
	 * 通过设备id获取当前设备信息
	 * @param id
	 * @return
	 */
	public Device selectDeviceById(int id);
	/**
	 * 获取设备信息数组
	 * @return
	 */
	public List<Device> listDevice();
	/**
	 * 插入新的设备
	 * @param device
	 */
	public void saveDevice(Device device);
	/**
	 * 更新设备信息
	 * @param device
	 */
	public void updateDevice(Device device);
	/**
	 * 根据设备id删除当前设备
	 * @param id
	 */
	public void deleteDevice(int id);
	/**
	 * 获取同一协议下的设备id和name
	 * @param modbustype_id
	 * @return
	 */
	public List<Device> listDeviceByModbustype(int modbustype_id);
	/**
	 * 根据设备id获取协议类型
	 * @param id
	 * @return
	 */
	public Device findByModbus_type(int id);
}
