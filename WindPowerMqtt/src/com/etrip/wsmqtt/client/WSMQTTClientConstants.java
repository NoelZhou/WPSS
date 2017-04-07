package com.etrip.wsmqtt.client;

	/** 
4.	 *  
5.	 * 消息订阅消息的常量字段 
6.	 *  
7.	 * @author longgangbai 
8.	 */  
	public final class WSMQTTClientConstants {  
	      
	  public static final String TCPAddress = System.getProperty("TCPAddress", "tcp://192.168.69.68:1883");  
	  public static String  clientId = String.format("%-23.23s",(System.getProperty("user.name") + "_" + (System.getProperty("clientId", "Subscribe."))).trim()).replace('-', '_');  
	  public static final String  topicString = System.getProperty("topicString", "PC/Password");  
  public static final String  publication =System.getProperty("publication", "Hello World " + String.format("%tc", System.currentTimeMillis()));  
	  public static final int quiesceTimeout = Integer.parseInt(System.getProperty("timeout", "10000"));  
  public static final int sleepTimeout =Integer.parseInt(System.getProperty("timeout", "10000000"));  
	  public static final boolean cleanSession = Boolean.parseBoolean(System.getProperty("cleanSession", "false"));  
	  public static final int  QoS = Integer.parseInt(System.getProperty("QoS", "1"));  
 public static final boolean  retained = Boolean.parseBoolean(System.getProperty("retained", "false"));  
	}  
