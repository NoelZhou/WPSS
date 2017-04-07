package com.cn.hy.service.firmwareUpgrade;
import java.util.List;

import com.cn.hy.pojo.firmwareUpgrade.FirmwareUpgrade;

/**
 * 固件升级 Service
 * @author Administrator
 *
 */
public interface FirmwareUpgradeService {
	/**
	 * 根据协议类型、波形类型查看波形列表。
	 * @param waveform 波形
	 * @return
	 */
	public List<FirmwareUpgrade> getDeviceinfoList(FirmwareUpgrade firmwareUpgrade);
	
	/**
	 * 根据协议类型、波形类型查看波形列表。
	 * @param waveform 波形
	 * @return
	 */
	public FirmwareUpgrade getDeviceinfo(FirmwareUpgrade firmwareUpgrade);
}
