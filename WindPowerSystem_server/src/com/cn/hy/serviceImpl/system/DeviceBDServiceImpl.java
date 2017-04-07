package com.cn.hy.serviceImpl.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.DeviceBDDao;
import com.cn.hy.pojo.system.DeviceBaseData;
import com.cn.hy.service.system.DeviceBDService;
@Service("DeviceBDServiceImpl")
public class DeviceBDServiceImpl implements DeviceBDService {
	@Resource
	private DeviceBDDao deviceBDDao;
	/**
	 * 获取风场预览需要显示的参数地址
	 */
	@Override
	public List<DeviceBaseData> selectDeviceBaseData(int showtype) {
		return deviceBDDao.selectDeviceBaseData(showtype);
	}
	/**
	 * 插入风场预览显示参数地址
	 */
	@Override
	public void saveDeviceBaseData(DeviceBaseData deviceBaseData) {
		deviceBDDao.saveDeviceBaseData(deviceBaseData);
	}
	/**
	 * 删除风场预览显示参数地址
	 */
	@Override
	public void deleteDeviceBaseData(DeviceBaseData deviceBaseData) {
		deviceBDDao.deleteDeviceBaseData(deviceBaseData);
	}
	/**
	 * 获取基本参数配置的数组
	 */
	@Override
	public List<DeviceBaseData> listDeviceBaseData(int modbustype) {
		return deviceBDDao.listDeviceBaseData(modbustype);
	}
	/**
	 * 修改风机显示参数
	 */
	@Override
	public void updateDeviceBaseData(DeviceBaseData deviceBaseData) {
		deviceBDDao.updateDeviceBaseData(deviceBaseData);
	}

}
