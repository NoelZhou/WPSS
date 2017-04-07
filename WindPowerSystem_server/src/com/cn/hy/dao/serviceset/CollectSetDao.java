package com.cn.hy.dao.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.CollectTimes;
import com.cn.hy.pojo.serviceset.DeviceInfo;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.serviceset.ModbustcpType;
import com.cn.hy.pojo.system.Device;
/**
 * 采集设置
 * @author HCheng
 *
 */
public interface CollectSetDao {
	/**
	 * 获取实时，历史数据采集频率
	 * @return
	 */
	public CollectTimes getCollectTimes();
	/**
	 * 插入采集频率
	 * @param collectTimes
	 */
	public void saveCollectTimes(CollectTimes collectTimes);
	/**
	 * 设置更新采集频率
	 * @param collectTimes
	 */
	public void updateCollectTimes(CollectTimes collectTimes);
	/**
	 * 获取设备类型
	 * @return
	 */
	public List<DeviceType> listDeviceType();
	/**
	 * 插入设备类型
	 * @param deviceType
	 */
	public void saveDeviceType(DeviceType deviceType);
	/**
	 * 更新设备类型
	 * @param deviceType
	 */
	public void updateDeviceType(DeviceType deviceType);
	/**
	 * 删除设备类型
	 * @param id
	 */
	public void deleteDeviceType(int id);
	/**
	 * 通过id获取设备类型
	 * @param id
	 * @return
	 */
	public DeviceType getDeviceTypeById(int id);
	/**
	 * 设备管理修改之前，获取设备信息
	 * @param id
	 * @return
	 */
	public Device getDeviceById(int id);
	/**
	 * 获取协议类型
	 * @return
	 */
	public List<ModbustcpType> listModbustcpType();
	/**
	 * 获取所有设备ip信息
	 * @return
	 */
	public List<DeviceInfo> listDeviceInfo();
	/**
	 * 根据id查找设备ip信息
	 * @param id
	 * @return
	 */
	public DeviceInfo getDeviceInfo(int id);
	/**
	 * 新增设备信息
	 * @param deviceInfo
	 */
	public void saveDeviceInfo(DeviceInfo deviceInfo);
	/**
	 * 编辑更新设备信息
	 * @param deviceInfo
	 */
	public void updateDeviceInfo(DeviceInfo deviceInfo);
	/**
	 * 删除设备ip信息
	 * @param id
	 */
	public void deleteDeviceInfo(int id);
	/**
	 * 根据目标设备id查找设备arm板的ip信息
	 * @param device_id
	 * @return
	 */
	public DeviceInfo getDeviceInfoBydid(int device_id);
	/**
	 * 根据目标设备id查找设备arm和dsp的ip信息
	 * @param device_id
	 * @return
	 */
	public List<DeviceInfo> getDeviceInfoByDevice_id(int device_id);
	/**
	 * 根据ip地址查找设备信息
	 * @param ip
	 * @return
	 */
	public List<DeviceInfo> findByIp(DeviceInfo ip);
	/**
	 * 根据id和ip查找设备信息
	 * @param deviceInfo
	 * @return
	 */
	public List<DeviceInfo> findByIdAndIp(DeviceInfo deviceInfo);
	
}
