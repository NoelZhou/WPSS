package com.hy.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hy.bean.MqttUtil;
import com.hy.pojo.CdhData;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class THD_Four extends Thread {
	static MqttUtil mqttut = new MqttUtil();
	private Socket sck;
	THD_Four(Socket sck) {
		this.sck = sck;
	}
	public void run() {
		boolean isRun =true;
		int num = 0;
		System.out.println("启动发送心跳线程");
		while (isRun) {
			try {
				THD_Four.sleep(60000);
				if(num<10){
					String[] xthfcode=mqttut.sendxtcode(sck);
					if(xthfcode!=null){
						isRun=false;
						TextThd.statrSerivceManager(sck);
					}else{
						System.out.println("第"+(num+1)+"次发送心跳失败！");
					}
					num++;
				}else{
					isRun=false;
					//关闭线程
					System.out.println("心跳MQTT服务器出现故障，断开连接......");
					new THD_TWO(sck).stop();
					new THD_ONE().start();
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
