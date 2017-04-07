package com.cn.hy.dao.system;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;

public interface DataCqDao {
	/**
	 * 获取所有设备最新采集的数据
	 * @return
	 */
	public List<DataCq> selectDataCq();
	/**
	 * 获取所有设备状态是start的设备
	 */
	public List<DataCq> selectDataCqStart();
	
	/**
	 * 获取所有设备最新采集的数据(30s)
	 * @return
	 */
	public List<DataCq> selectDataCqEmail();
	/**
	 * 获取一条data数据
	 * @param datacq
	 * @return
	 */
	public DataCq getDataCq(DataCq datacq);
	/**
	 * 通过设备id获取最新的一条采集的数据
	 * @param datacq
	 * @return
	 */
	public DataCq getDataCqBydeviceid(DataCq datacq);
	/**
	 * 获取当前设备前15分钟采集的数据
	 * @param dataCq
	 * @return
	 */
	public List<DataCq> listDataCqByDevice(DataCq dataCq);
	/**
	 * 获取所有设备
	 * @return
	 */
	public List<Device> geAllDevice();
	/**
	 * 更新设备的读写权限
	 * @param s1
	 * @param s2
	 */
	public void updateDevice_rwrole(@Param(value="s1")String s1,@Param(value="s2")String s2);
	/**
	 * 获取故障风机的采集数据
	 * @return
	 */
	public List<DataCq> selectErrorDataCq();
}
