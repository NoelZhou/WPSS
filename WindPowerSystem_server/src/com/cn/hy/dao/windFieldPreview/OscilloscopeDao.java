package com.cn.hy.dao.windFieldPreview;


import java.util.List;

import com.cn.hy.pojo.windFieldPreview.DeviceInfo1;
import com.cn.hy.pojo.windFieldPreview.DeviceInfoDspWaveform;

/**
 * 示波器Dao
 */
public interface OscilloscopeDao {

	/**
	 * 根据协议类型、波形类型查看波形列表。
	 * @param waveform 波形
	 * @return
	 */
	public List<DeviceInfoDspWaveform> getDeviceinfoDspWaveformList(DeviceInfoDspWaveform waveform);
	
	/**
	 * 获取变流器DSP列表
	 * @param deviceInfo 变流器明细
	 * @return
	 */
	public List<DeviceInfo1> getDeviceInfoDspList(DeviceInfo1 deviceInfo1);
}
