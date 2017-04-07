package com.cn.hy.serviceImpl.system;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.system.DataCqDao;
import com.cn.hy.pojo.system.DataCq;
import com.cn.hy.pojo.system.Device;
import com.cn.hy.service.system.DataCqService;

@Service("DataCqServiceImpl")
public class DataCqServiceImpl implements DataCqService {
	@Resource
	private DataCqDao dataCqDao;
	
	/**
	 * 获取所有设备最新采集的数据
	 */
	@Override
	public List<DataCq> selectDataCq() {
		return dataCqDao.selectDataCq();
	}
	/**
	 * 获取所有设备最新采集的数据(30s)
	 */
	@Override
	public List<DataCq> selectDataCqEmail() {
		return dataCqDao.selectDataCqEmail();
	}
	/**
	 * 获取一条data数据
	 */
	@Override
	public DataCq getDataCq(int id) {
		DataCq datacq = new DataCq();
		datacq.setId(id);
		return dataCqDao.getDataCq(datacq);
	}
	/**
	 * 通过设备id获取最新的一条采集的数据
	 */
	@Override
	public DataCq getDataCqBydeviceid(int device_id) {
		DataCq datacq = new DataCq();
		datacq.setDevice_id(device_id);
		return dataCqDao.getDataCqBydeviceid(datacq);
	}
	/**
	 * 获取所有设备
	 */
	@Override
	public List<Device> geAllDevice() {
		// TODO Auto-generated method stub
		return dataCqDao.geAllDevice();
	}
	/**
	 * 更新设备的读写权限
	 */
	@Override
	public void updateDevice_rwrole(String string, String string2) {
		// TODO Auto-generated method stub
		 dataCqDao.updateDevice_rwrole(string,string2);
	}
	@Override
	public List<DataCq> selectErrorDataCq() {
		return dataCqDao.selectErrorDataCq();
	}
	@Override
	public List<DataCq> selectDataCqStart() {
		
		return dataCqDao.selectDataCqStart();
	}

}
