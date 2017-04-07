package com.hy.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hy.bean.MqttUtil;
import com.hy.pojo.Device;

public class THD_ONE  extends Thread {
    static MqttUtil mqttut=new MqttUtil();
	public void run() {
		boolean isrun=true;
		System.out.println("开启自动连接Mqtt服务器线程......");
		while(isrun){
			try {
				List<Device> devicelist = new ArrayList<Device>();
				devicelist=mqttut.startDataAcq();
					Socket socket = null;
					socket = MqttUtil.getMqttServerAddr();
					if(socket==null){
						System.out.println("连接MQTT服务失败,数据插入历史库！");
						for (int num = 0; num < devicelist.size(); num++) {
							Device devone = devicelist.get(num);
							mqttut.insertHistorydata(devone);
						};
					}else {
						System.out.println("连接MQTT服务成功，开始关闭连接服务线程，启动登录mqtt服务器线程......");
						socket.close();
						isrun=false;
						TextThd.startacq();
					}
					THD_ONE.sleep(5000*60);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
}
