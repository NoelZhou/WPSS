package com.etrip.mqttv3;

import com.ibm.micro.client.mqttv3.MqttClient;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

public class MQTTPub {
	public static void doTest(String ztstr,byte[] strcode){ 
		try { 
			MqttClient client = new MqttClient("tcp://192.168.69.68:1883","Administrator:Subscribe"); 
			MqttTopic topic = client.getTopic(ztstr); 
			MqttMessage message = new MqttMessage(strcode); 
			message.setQos(1); 
			client.connect();
			while(true){
				MqttDeliveryToken token = topic.publish(message); 
				while (!token.isComplete()){ 
					token.waitForCompletion(5000); 
				} 
			}
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 
} 

