package com.etrip.wsmqtt.client;

import com.ibm.micro.client.mqttv3.*;  
	/** 
4.	 *  
5.	 * 消息订阅相关的回调类使用 
6.	 *  
7.	 * 必须实现MqttCallback的接口并实现对应的相关接口方法 
8.	 *   
9.	 * @author longgangbai 
10.	 */  
	public class WSMQTTClientCallBack implements MqttCallback {  
     private String instanceData = "";  
      public WSMQTTClientCallBack(String instance) {  
       instanceData = instance;  
     }  
     public void messageArrived(MqttTopic topic, MqttMessage message) {  
	            try {  
	              System.out.println("Message arrived: \"" + message.toString()  
                  + "\" on topic \"" + topic.toString() + "\" for instance \""  
	                  + instanceData + "\"");  
            } catch (Exception e) {  
              e.printStackTrace();  
	            }  
	      }  
	      public void connectionLost(Throwable cause) {  
            System.out.println("Connection lost on instance \"" + instanceData  
                + "\" with cause \"" + cause.getMessage() + "\" Reason code "   
                + ((MqttException)cause).getReasonCode() + "\" Cause \""   
                + ((MqttException)cause).getCause() +  "\"");      
            cause.printStackTrace();  
      }  
	      public void deliveryComplete(MqttDeliveryToken token) {  
	            try {  
              System.out.println("Delivery token \"" + token.hashCode()  
	                  + "\" received by instance \"" + instanceData + "\"");  
	            } catch (Exception e) {  
	              e.printStackTrace();  
	            }  
	      }  
}  
