package com.cn.hy.service.windFieldPreview;
import java.util.List;
import java.util.Map;

import com.cn.hy.pojo.windFieldPreview.DeviceInfoDspWaveform;

/**
 * 波形Servier
 *
 */
public interface OscilloscopeService {
	/**
	 * 根据协议类型、波形类型查看波形列表。
	 * @param waveform 波形
	 * @return
	 */
	public List<DeviceInfoDspWaveform> getDeviceinfoDspWaveformList(DeviceInfoDspWaveform waveform);
	
	/**
	 * 根据设备ID获取机侧、网侧波形列表
	 * @param deviceId
	 * @return
	 */
	public Map<String, List<DeviceInfoDspWaveform>> getDeviceinfoDspWaveformList(String deviceId);
	
	/**
	 * 获取变流器DSP信息
	 * @param deviceId 变流器ID
	 * @return
	 */
	public Map<String, Object> getDeviceInfoDspMap(String deviceId);
	
	/**
	 * 开始获取变流器数据
	 * @param deviceId 变流器ID
	 * @param waveformCodes 命令集合
	 */
	public void start(String deviceId, String[] waveformCodes);
	
	/**
	 * 停止获取变流器数据
	 * @param deviceId 变流器ID
	 */
	public void stop(String deviceId);
 
}
