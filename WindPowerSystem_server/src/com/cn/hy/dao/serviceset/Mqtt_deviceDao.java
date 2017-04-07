package com.cn.hy.dao.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.Mqtt_device;

public interface Mqtt_deviceDao {

	public List<Mqtt_device> selectMqtt_device();

	public void insertMqtt_device(Mqtt_device mqtt_device);
	
	public Mqtt_device findById(int id);
	
	public void editMqtt_device(Mqtt_device mqtt_device);
	
	public void deleteMqtt_device(int id);
}
