package com.cn.hy.serviceImpl.serviceset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;



import com.cn.hy.dao.serviceset.Mqtt_deviceDao;
import com.cn.hy.pojo.serviceset.Mqtt_device;
import com.cn.hy.service.serviceset.Mqtt_deviceService;

@Service("Mqtt_deviceServiceImpl")
public class Mqtt_deviceServiceImpl implements Mqtt_deviceService {

	@Resource
	private Mqtt_deviceDao mqtt_deviceDao;

	/*
	 * matt设备集合
	 */
	@Override
	public List<Mqtt_device> selectMqtt_device() {
		return mqtt_deviceDao.selectMqtt_device();
	}
	/*
	 * 新增
	 */
	@Override
	public void insertMqtt_device(Mqtt_device mqtt_device) {
		mqtt_deviceDao.insertMqtt_device(mqtt_device);
	}
	/*
	 * 通过id查询matt设备集
	 */
	@Override
	public Mqtt_device findById(int id) {
		return mqtt_deviceDao.findById(id);
	}
	/*
	 * 修改
	 */
	@Override
	public void editMqtt_device(Mqtt_device mqtt_device) {
		mqtt_deviceDao.editMqtt_device(mqtt_device);
	}
	/*
	 * 删除
	 */
	@Override
	public void deleteMqtt_device(int id) {
		mqtt_deviceDao.deleteMqtt_device(id);
	}

}
