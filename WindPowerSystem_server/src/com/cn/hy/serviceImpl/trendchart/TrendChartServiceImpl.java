package com.cn.hy.serviceImpl.trendchart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.modbustcp.ModbusSkAppDao;
import com.cn.hy.dao.system.DataCqDao;
import com.cn.hy.dao.system.DeviceDao;
import com.cn.hy.pojo.modbustcp.ModbusSkApp;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.pojo.trendchart.EchartData;
import com.cn.hy.pojo.trendchart.Series;
import com.cn.hy.service.trendchart.TrendChartService;
@Service("TrendChartServiceImpl")
public class TrendChartServiceImpl implements TrendChartService {
	@Resource
	private DeviceDao deviceDao;
	@Resource
	private DataCqDao dataCqDao;
	@Resource
	private ModbusSkAppDao modbusSkAppDao;
	/**
	 * 获取同一协议下的设备id和name
	 */
	@Override
	public List<Device> listDeviceByModbustype(int modbustype_id) {
		return deviceDao.listDeviceByModbustype(modbustype_id);
	}
	/**
	 * 获取报表数据
	 */
	@Override
	public EchartData getEchartData(String deviceids, String addrs,int modbustype_id) {
		SimpleDateFormat sdf=new SimpleDateFormat("HH:mm:ss");
		String[] deviceArray = deviceids.split(",");
		String[] addrArray = addrs.split(",");
		DataCq dataCq = new DataCq();
		List<String> legend = new ArrayList<String>();//数据分组
		List<String> category = new ArrayList<String>();//横坐标
		List<Series> seriesList = new ArrayList<Series>();//纵坐标
		ModbusSkApp msa = new ModbusSkApp();
		Device device = new Device();
		Series serie = new Series();
		String device_name = "";
		String time_area = getTimeArea();
		int datasize=0;
		for (int i = 0; i < deviceArray.length; i++) {
			int device_id = Integer.parseInt(deviceArray[i]);
			dataCq.setDevice_id(device_id);
			dataCq.setTime_area(time_area);
			int datatmpsize=0;
			//获取当前设备前15分钟采集的数据
			List<DataCq> datacqList = dataCqDao.listDataCqByDevice(dataCq);
			datatmpsize=datacqList.size();
			if(i==0){
				datasize=datatmpsize;
			}
			//通过设备id获取当前设备信息
			device = deviceDao.selectDeviceById(device_id);
			device_name = device.getName();
			for (int j = 0; j < addrArray.length; j++) {
				serie = new Series();
				int addr = Integer.parseInt(addrArray[j]);
				msa = new ModbusSkApp();
				msa.setAddr(addr);
				msa.setModbus_type(modbustype_id);
				msa = modbusSkAppDao.getModbusSkApp(msa);
				String lngname = device_name+"-"+msa.getName();
				legend.add(lngname);
				List<Integer> ydata = new ArrayList<Integer>();
				int m=0;
				for (DataCq dq : datacqList) {
					//只加一次横坐标
					if(j == 0){
					  if(datasize>=datatmpsize){
						  if(m==0){
							category = new ArrayList<String>();
						   }
						  category.add(sdf.format(dq.getCreate_time()));
						
					   }
					}
					String dataStr = dq.getData();
					String [] dataArray =dataStr.split(",");
					if(modbustype_id==2){
						ydata.add(Integer.parseInt(dataArray[addr-12788]));
					}else{
						ydata.add(Integer.parseInt(dataArray[addr]));
					}
					m++;
				}
				serie.setName(lngname);
				serie.setType("line");
				serie.setData(ydata);
				seriesList.add(serie);
			}
		}
		EchartData echartData = new EchartData(legend, category, seriesList);
		return echartData;
	}
	/**
	 * 获取当前时间前15min
	 * @return
	 */
	public static String getTimeArea(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		String endtime = sdf.format(calendar.getTime());
		calendar.add (Calendar.SECOND, -900);
		String starttime = sdf.format(calendar.getTime());
		return "unix_timestamp(create_time) between unix_timestamp('"+starttime+"') and unix_timestamp('"+endtime+"')";
	}
}
