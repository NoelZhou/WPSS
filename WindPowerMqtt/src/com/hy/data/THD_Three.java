package com.hy.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hy.bean.MqttUtil;
import com.hy.pojo.CdhData;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class THD_Three extends Thread {
	static MqttUtil mqttut = new MqttUtil();
	private Socket sck;
	THD_Three(Socket sck) {
		this.sck = sck;
	}

	public void run() {
		boolean isRun =true;
		int num = 0;
		System.out.println("启动自动登录线程");
		while (isRun) {
			try {
				THD_Three.sleep(60000);
				if(num<10){
					String[] loadHF=mqttut.dlhf(sck);
					if(loadHF!=null){
						TextThd.DY(sck);
						System.out.println("连接Mqtt服务器成功，开始上传历史库数据......");
						MqttUtil.startpushhistorydata(sck);
						//开始传送连接的变流器采集数据
						System.out.println("连接Mqtt服务器成功，开始上传运行数据......");
			     		TextThd.statrSerivceManager(sck);
			    		new THD_TWO(sck).start();
			    		
						isRun=false;
					}else{
						System.out.println("第"+(num+1)+"次登录失败！");
					}
					num++;
				}else{
					System.out.println("自动登录MQTT服务器出现故障，断开连接......");
					new THD_ONE().start();
					isRun=false;
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
