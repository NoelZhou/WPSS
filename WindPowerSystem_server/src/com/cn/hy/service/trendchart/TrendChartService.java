package com.cn.hy.service.trendchart;

import java.util.List;

import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.trendchart.EchartData;

public interface TrendChartService {
	public List<Device> listDeviceByModbustype(int modbustype_id);
	public EchartData getEchartData(String deviceids,String addrs,int modbustype_id);
}
