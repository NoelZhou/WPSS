package com.cn.hy.serviceImpl.serviceset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.serviceset.CollectSetDao;
import com.cn.hy.pojo.serviceset.CollectTimes;
import com.cn.hy.pojo.serviceset.DeviceInfo;
import com.cn.hy.pojo.serviceset.DeviceType;
import com.cn.hy.pojo.serviceset.ModbustcpType;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.serviceset.CollectSetService;
@Service("ServiceSetServiceImpl")
public class CollectSetServiceImpl implements CollectSetService {
	@Resource
	private CollectSetDao collectSetDao;
	/**
	 * 获取采集频率
	 */
	@Override
	public CollectTimes getCollectTimes() {
		return collectSetDao.getCollectTimes();
	}
	/**
	 * 新增采集频率
	 */
	@Override
	public void saveCollectTimes(CollectTimes collectTimes) {
		collectSetDao.saveCollectTimes(collectTimes);
	}
	/**
	 * 修改采集频率
	 */
	@Override
	public void updateCollectTimes(CollectTimes collectTimes) {
		collectSetDao.updateCollectTimes(collectTimes);
	}
	
	/**
	 * 获取设备类型
	 */
	@Override
	public List<DeviceType> listDeviceType() {
		return collectSetDao.listDeviceType();
	}
	
	/**
	 * 保存设备类型
	 */
	@Override
	public void saveDeviceType(DeviceType deviceType) {
		collectSetDao.saveDeviceType(deviceType);
	}
	
	/**
	 * 修改设备类型
	 */
	
	@Override
	public Device getDeviceById(int id) {
		return collectSetDao.getDeviceById(id);
	}
	/**
	 * 更新设备类型
	 */
	@Override
	public void updateDeviceType(DeviceType deviceType) {
		collectSetDao.updateDeviceType(deviceType);
	}

	/**
	 * 删除设备类型
	 */
	@Override
	public void deleteDeviceType(int id) {
		collectSetDao.deleteDeviceType(id);
	}
	
	/**
	 * 根据设备类型id获取数据
	 */
	@Override
	public DeviceType getDeviceTypeById(int id) {
		return collectSetDao.getDeviceTypeById(id);
	}
	
	/**
	 * 获取协议类型
	 */
	@Override
	public List<ModbustcpType> listModbustcpType() {
		return collectSetDao.listModbustcpType();
	}
	/**
	 * 获取通讯列表
	 */
	@Override
	public List<DeviceInfo> listDeviceInfo() {
		return collectSetDao.listDeviceInfo();
	}
	/**
	 * 根据id获取通讯
	 */
	@Override
	public DeviceInfo getDeviceInfo(int id) {
		return collectSetDao.getDeviceInfo(id);
	}
	/**
	 * 保存通讯
	 */
	@Override
	public void saveDeviceInfo(DeviceInfo deviceInfo) {
		collectSetDao.saveDeviceInfo(deviceInfo);
	}
	/**
	 * 修改通讯
	 */
	@Override
	public void updateDeviceInfo(DeviceInfo deviceInfo) {
		collectSetDao.updateDeviceInfo(deviceInfo);
	}
	/**
	 * 删除通讯
	 */
	@Override
	public void deleteDeviceInfo(int id) {
		collectSetDao.deleteDeviceInfo(id);
	}
	/**
	 * 根据目标设备id查找设备arm板的ip信息
	 */
	@Override
	public DeviceInfo getDeviceInfoBydid(int device_id) {
		// TODO Auto-generated method stub
		return collectSetDao.getDeviceInfoBydid(device_id);
	}
	/**
	 * 根据目标设备id查找设备arm和dsp的ip信息
	 */
	@Override
	public List<DeviceInfo> getDeviceInfoByDevice_id(int device_id) {
		return collectSetDao.getDeviceInfoByDevice_id(device_id);
	}
	
	/**
	 * 根据ip地址查找设备信息
	 */
	@Override
	public List<DeviceInfo> findByIp(DeviceInfo ip) {
		return collectSetDao.findByIp(ip);
	}
	/**
	 * 根据id和ip查找设备信息
	 */
	@Override
	public List<DeviceInfo> findByIdAndIp(DeviceInfo deviceInfo) {
		return collectSetDao.findByIdAndIp(deviceInfo);
	}
	

}
