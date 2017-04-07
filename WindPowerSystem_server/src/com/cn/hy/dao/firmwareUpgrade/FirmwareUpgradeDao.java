package com.cn.hy.dao.firmwareUpgrade;


import java.util.List;

import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgrade;

/**
 * 固件升级 Dao
 * @author Administrator
 *
 */
public interface FirmwareUpgradeDao {

	/**
	 * 查询设备信息
	 * @param firmwareUpgrade
	 * @return
	 */
	public List<FirmwareUpgrade> getDeviceinfoList(FirmwareUpgrade firmwareUpgrade);
	
	/**
	 * 查询设备信息
	 * @param firmwareUpgrade
	 * @return
	 */
	public FirmwareUpgrade getDeviceinfo(FirmwareUpgrade firmwareUpgrade);
}
