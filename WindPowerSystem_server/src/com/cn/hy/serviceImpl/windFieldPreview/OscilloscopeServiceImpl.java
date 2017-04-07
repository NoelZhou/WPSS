package com.cn.hy.serviceImpl.windFieldPreview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.cn.hy.dao.windFieldPreview.OscilloscopeDao;
import com.cn.hy.pojo.windFieldPreview.DeviceInfo1;
import com.cn.hy.pojo.windFieldPreview.DeviceInfoDspWaveform;
import com.cn.hy.service.windFieldPreview.OscilloscopeService;
import com.cn.hy.util.DSPTcpUtils;
import com.cn.hy.util.DspReadThread;

/**
 * 
 * 示波器ServiceImpl
 *
 */
@Service("oscilloscopeService")
public class OscilloscopeServiceImpl implements OscilloscopeService {
	
	@Resource
	private OscilloscopeDao oscilloscopeDao;

	@Override
	public List<DeviceInfoDspWaveform> getDeviceinfoDspWaveformList(DeviceInfoDspWaveform waveform) {
		return oscilloscopeDao.getDeviceinfoDspWaveformList(waveform);
	}

	@Override
	public Map<String, List<DeviceInfoDspWaveform>> getDeviceinfoDspWaveformList(String deviceId) {
		Map<String, List<DeviceInfoDspWaveform>> result = new HashMap<String, List<DeviceInfoDspWaveform>>();
		DeviceInfo1 deviceInfo = new DeviceInfo1();
		deviceInfo.setDeviceId(deviceId);
		List<DeviceInfo1> deviceInfoList = oscilloscopeDao.getDeviceInfoDspList(deviceInfo);
		
		DeviceInfoDspWaveform waveform = null;
		for(DeviceInfo1 info : deviceInfoList){
			waveform = new DeviceInfoDspWaveform();
			waveform.setDeviceId(deviceId);
			waveform.setDspType(info.getDspType());
			if(!StringUtils.isEmpty(info.getDspType()) && "1".equals(info.getDspType())){
				waveform.setModel("1");
				result.put("webList", this.getDeviceinfoDspWaveformList(waveform));
				waveform.setModel("2");
				result.put("webDefaultList", this.getDeviceinfoDspWaveformList(waveform));
			}else{
				waveform.setModel("1");
				result.put("machineList", this.getDeviceinfoDspWaveformList(waveform));
				waveform.setModel("2");
				result.put("machineDefaultList", this.getDeviceinfoDspWaveformList(waveform));
			}
		}
		return result;
	}

	@Override
	public void start(String deviceId, String[] waveformCodes) {
		DeviceInfo1 deviceInfo = new DeviceInfo1();
		deviceInfo.setDeviceId(deviceId);
		List<DeviceInfo1> deviceInfoList = oscilloscopeDao.getDeviceInfoDspList(deviceInfo);
		String[] ips = new String[deviceInfoList.size()];
		int[] ports = new int[deviceInfoList.size()];
		int i = 0;
		for(DeviceInfo1 info : deviceInfoList){
			ips[i] = info.getIp();
			ports[i] = info.getPort();
			i++;
		}
		int[] length = {66, 66};
		
		DSPTcpUtils.updateDeviceSyncv(Integer.parseInt(deviceId), "1");
		DspReadThread.start(ips, ports, length, waveformCodes);
	}

	@Override
	public void stop(String deviceId) {
		DSPTcpUtils.updateDeviceSyncv(Integer.parseInt(deviceId), "0");
	}

	@Override
	public Map<String, Object> getDeviceInfoDspMap(String deviceId) {
		Map<String, Object> result = new HashMap<String, Object>();
		DeviceInfo1 deviceInfo = new DeviceInfo1();
		deviceInfo.setDeviceId(deviceId);
		List<DeviceInfo1> deviceInfoList = oscilloscopeDao.getDeviceInfoDspList(deviceInfo);
		
		for(DeviceInfo1 info : deviceInfoList){
			if(!StringUtils.isEmpty(info.getDspType()) && "1".equals(info.getDspType())){
				result.put("dsp1Ip", info.getIp());
				result.put("dsp1Port", info.getPort());
			}else{
				result.put("dsp2Ip", info.getIp());
				result.put("dsp2Port", info.getPort());
			}
		}
		return result;
	}
}
