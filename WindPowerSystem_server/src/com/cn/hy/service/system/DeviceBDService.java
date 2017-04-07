package com.cn.hy.service.system;

import java.util.List;

import com.cn.hy.pojo.system.DeviceBaseData;

public interface DeviceBDService {
	/**
	 * 获取风场预览需要显示的参数地址
	 * @param showtype
	 * @return
	 */
	public List<DeviceBaseData> selectDeviceBaseData(int showtype);
	/**
	 * 插入风场预览显示参数地址
	 * @param deviceBaseData
	 */
	public void saveDeviceBaseData(DeviceBaseData deviceBaseData);
	/**
	 * 删除风场预览显示参数地址
	 * @param paramter_id
	 */
	public void deleteDeviceBaseData(DeviceBaseData deviceBaseData);
	/**
	 * 获取基本参数配置的数组
	 * @param modbustype
	 * @return
	 */
	public List<DeviceBaseData> listDeviceBaseData(int modbustype);
	/**
	 * 修改风机显示参数
	 * @param deviceBaseData
	 */
	public void updateDeviceBaseData(DeviceBaseData deviceBaseData);
}
