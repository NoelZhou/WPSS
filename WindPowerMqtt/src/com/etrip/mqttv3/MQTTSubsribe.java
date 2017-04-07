package com.etrip.mqttv3;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttConnectOptions;

public class MQTTSubsribe {
	public static String doTest() { 
		try { 
			//创建MqttClient
			MqttClient client = new MqttClient("tcp://192.168.69.68:1883", "A140506988"); 
			//回调处理类
			CallBack callback = new CallBack(); 
			client.setCallback(callback); 
			//创建连接可选项信息
			MqttConnectOptions conOptions = new MqttConnectOptions(); 
			//
			conOptions.setCleanSession(false); 
			//连接broker
			client.connect(conOptions); 
			//发布相关的订阅
			client.subscribe("PC/Password", 1); 
			client.subscribe("/Time", 1); 
			client.disconnect(); 
		} catch (Exception e) { 
			e.printStackTrace(); 
			return "failed"; 
		} 
		return "success"; 
	} 
} 
