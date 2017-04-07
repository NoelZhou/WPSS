package com.hy.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.ServiceManager;
import com.hy.bean.MqttUtil;
public class TextThd {
	 static MqttUtil mqttut=new MqttUtil();
	public static void main(String[] args) {
/*		float ss=(float)1;
		String datatmp =Integer.toHexString(Float.floatToIntBits(ss));	
		String datatmp1 = Double.toHexString(1);	
		
		System.out.print(datatmp);
		*/
		startacq();
	}
	static int statect=0;
	//就一个采集系统，不需要更新采集信息
	public static void startacq(){
		//系统设备登录服务器，设置好相关信息，获取密匙等信息。
		System.out.println("开始连接mqtt服务器，并登录.....");
		Socket sck=initsystemdevice();//连接mqtt服务器，登陆
		//先处理历史库数据
		if(sck!=null){
			System.out.println("连接Mqtt服务器成功，开始上传历史库数据......");
			mqttut.startpushhistorydata(sck);
			//开始传送连接的变流器采集数据
			System.out.println("连接Mqtt服务器成功，开始上传运行数据......");
     		statrSerivceManager(sck);
    		new THD_TWO(sck).start();
		}
	}
	public static Socket initsystemdevice(){
		Socket socket = null;
		//获取MQTT服务地址
		socket = mqttut.getMqttServerAddr();
		if(socket==null){
			System.out.println("连接MQTT服务失败！");
			return null;
		}
		// 登陆
		String[] loadHF = mqttut.dlhf(socket);
		if(loadHF!=null){
			DY(socket);
		}else{
			THD_Three third = new THD_Three(socket);
			third.start();
			socket =null;
		}
		return socket;
	};
	
	public static void DY(Socket socket){
		// 订阅
		String[] dyzthf= mqttut.dyhf(socket);//发送订阅
	   // 长连接获取云端的主题 密钥主题/ 获取测试点参数ID 不必等待云端参数设定信息，可以直接开始发送运行数据。
	
		//订阅设备回复  可以不回
		mqttut.sbdyhf(dyzthf,socket);
		//解密订阅数据数据
		String[] csdsecs = mqttut.parseztstr(dyzthf);
		
		// 发送参数设定信息
		// 拿到最新的密匙和设置参数信息，终端设备发布消息， 主题为Logger/SetPara，表示对数据中心下发参数的回复确认
		String[]cssdhf=mqttut.cssd(csdsecs, socket);
		if(cssdhf.length>0){
			System.out.println("参数设定成功！");
		}else{
			System.out.println("参数设定失败！");
		}
	}
	
	public static void statrSerivceManager(Socket sck){
		    if(sck==null){
		    	System.out.print("连接MQTT服务器......");
		    	return;
		    }
	        List<Service> services = Lists.newArrayList();
	       	SungrowService serviceImp = new SungrowService(sck);
	         services.add(serviceImp);
	        System.out.println("*******构造多线程服务管理器*******");
	 
	        final ServiceManager serviceManager = new ServiceManager(services);
	        serviceManager. addListener(new ServiceManager.Listener() {
	            @Override
	            public void healthy() {
	                System.out.println("多线程服务运行健康！");
	            }
	            @Override
	            public void stopped() {
	                System.out.println("多线程服务运行结束！");
	            }
	            @Override
	            public void failure(Service service) {
	                System.out.println("多线程服务运行失败！");
	            }
	        }, MoreExecutors.sameThreadExecutor());
	 
	        System.out.println("********启动多线程所有任务********");
	        serviceManager.startAsync().awaitHealthy();
	    }

}
