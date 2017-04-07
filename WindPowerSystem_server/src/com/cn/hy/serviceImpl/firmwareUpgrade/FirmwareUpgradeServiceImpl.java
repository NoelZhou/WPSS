package com.cn.hy.serviceImpl.firmwareUpgrade;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.firmwareUpgrade.FirmwareUpgradeDao;
import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgrade;
import com.cn.hy.service.firmwareUpgrade.FirmwareUpgradeService;

/**
 * 
 * 固件升级 ServiceImpl
 *
 */
@Service("firmwareUpgradeService")
public class FirmwareUpgradeServiceImpl implements FirmwareUpgradeService {
	
	@Resource
	private FirmwareUpgradeDao firmwareUpgradeDao;
	/**
	 * 根据协议类型、波形类型查看波形列表。
	 */
	@Override
	public List<FirmwareUpgrade> getDeviceinfoList(FirmwareUpgrade firmwareUpgrade) {
		return firmwareUpgradeDao.getDeviceinfoList(firmwareUpgrade);
	}
	/**
	 * 查询设备信息
	 */
	@Override
	public FirmwareUpgrade getDeviceinfo(FirmwareUpgrade firmwareUpgrade) {
		return firmwareUpgradeDao.getDeviceinfo(firmwareUpgrade);
	}
}
