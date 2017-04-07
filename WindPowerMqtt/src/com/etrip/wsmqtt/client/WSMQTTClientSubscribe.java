/*package com.etrip.wsmqtt.client;
	import com.ibm.micro.client.mqttv3.MqttClient;  
		import com.ibm.micro.client.mqttv3.MqttConnectOptions;  
	*//** 
	 *  
	 * 使用 Java 为 MQ Telemetry Transport 创建订户 
		 * 在此任务中，您将遵循教程来创建订户应用程序。订户将针对主题创建预订并接收该预订的发布。 
	9.	 *  提供了一个示例订户应用程序 Subscribe。Subscribe 将创建预订主题 MQTT Examples，并等待获 
	10.	 *  得该预订的发布，等待时间为 30 秒。订户可以创建预订并等待获得发布。它还可以接收发送至先前 
	11.	 *  为同一客户机标识创建的预订的发布。 
	12.	 * @author longgangbai  
	13.	 *//*  
public class WSMQTTClientSubscribe {  
	      public static void main(String[] args) {  
	            try {  
                    
                //创建MQTT客户端对象  
                  MqttClient client = new MqttClient(WSMQTTClientConstants.TCPAddress, WSMQTTClientConstants.clientId);  
                   
                 //创建客户端MQTT回调类  
	                  WSMQTTClientCallBack callback = new WSMQTTClientCallBack(WSMQTTClientConstants.clientId);  
                  
                 //设置MQTT回调  
	                  client.setCallback(callback);  
		                    
                //创建一个连接对象  
                  MqttConnectOptions conOptions = new MqttConnectOptions();  
	                    
                  //设置清除会话信息  
                  conOptions.setCleanSession(WSMQTTClientConstants.cleanSession);  
                    
	                  //设置超时时间  
	                  conOptions.setConnectionTimeout(10000);  
                    
                  //设置会话心跳时间  
	                  conOptions.setKeepAliveInterval(20000);  
                    
                  //设置最终端口的通知消息  
		                  conOptions.setWill(client.getTopic("LastWillTopic"), "the client will stop !".getBytes(), 1, false);  
	                    
	                  //连接broker  
                  client.connect(conOptions);  
	                  System.out.println("Subscribing to topic \"" + WSMQTTClientConstants.topicString  
	                      + "\" for client instance \"" + client.getClientId()  
                      + "\" using QoS " + WSMQTTClientConstants.QoS + ". Clean session is "  
	                      + WSMQTTClientConstants.cleanSession);  
	                  //订阅相关的主题信息  
                  client.subscribe(WSMQTTClientConstants.topicString, WSMQTTClientConstants.QoS);  
	                  System.out.println("Going to sleep for " + WSMQTTClientConstants.sleepTimeout / 1000  
                      + " seconds");  
		                    
                     Thread.sleep(50000);  
	                  //关闭相关的MQTT连接  
	                  if(client.isConnected()){  
                      client.disconnect();  
		                  }  
	                  System.out.println("Finished");  
            } catch (Exception e) {  
              e.printStackTrace();  
            }  
	      }  
	}  
*/