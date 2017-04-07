package com.cn.hy.service.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.Mqtt_device;

public interface Mqtt_deviceService {

	/*
	 * matt设备集合
	 */
	public List<Mqtt_device> selectMqtt_device();

	/*
	 * 新增
	 */
	public void insertMqtt_device(Mqtt_device mqtt_device);

	/*
	 * 通过id查询matt设备集
	 */
	public Mqtt_device findById(int id);

	/*
	 * 修改
	 */
	public void editMqtt_device(Mqtt_device mqtt_device);
	/*
	 * 删除
	 */
	public void deleteMqtt_device(int id);
}
