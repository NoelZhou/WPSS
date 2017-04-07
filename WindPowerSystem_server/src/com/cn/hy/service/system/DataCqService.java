package com.cn.hy.service.system;

import java.util.List;

import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;

public interface DataCqService {
	/**
	 * 获取所有设备最新采集的数据
	 * @return
	 */
	public List<DataCq> selectDataCq();
	/**
	 * 获取所有设备最新采集的数据(30s)
	 * @return
	 */
	public List<DataCq> selectDataCqEmail();
	/**
	 * 获取一条data数据
	 * @param id
	 * @return
	 */
	public DataCq getDataCq(int id);
	/**
	 * 通过设备id获取最新的一条采集的数据
	 * @param device_id
	 * @return
	 */
	public DataCq getDataCqBydeviceid(int device_id);
	/**
	 * 获取所有设备
	 * @return
	 */
	public List<Device> geAllDevice();
	/**
	 * 更新设备的读写权限
	 * @param string
	 * @param string2
	 */
	public void updateDevice_rwrole(String string, String string2);
	/**
	 * 查询故障风机的采集信息
	 * @return
	 */
	public List<DataCq> selectErrorDataCq();
	public List<DataCq> selectDataCqStart();
}
