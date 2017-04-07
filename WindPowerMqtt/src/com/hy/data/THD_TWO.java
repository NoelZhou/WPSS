package com.hy.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.hy.bean.MqttUtil;
import com.hy.pojo.CdhData;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class THD_TWO extends Thread {
	static MqttUtil mqttut = new MqttUtil();
	private Socket sck;

	THD_TWO(Socket sck) {
		this.sck = sck;
	}

	public void run() {
		boolean isrun = true;
		System.out.println("开启监听设备状态线程线程......");
		int flag = 0;
		while (isrun) {
			List<Device> devicelist = new ArrayList<Device>();
			//获取变流器列表信息
			devicelist = mqttut.startDataAcq();
			try {
				THD_TWO.sleep(3000);
				sck.sendUrgentData(0xFF);
				for (int num = 0; num < devicelist.size(); num++) {
					Device devone = devicelist.get(num);
					//获取设备采集信息
					Data_Acq data = mqttut.getDevice_data(devone);
					System.out.println("监听设备状态中.....设备ID="+devone.getDevice_id());
					//发送运行数据
					String tmpreq=mqttut.sendrundatacode(devone, 2, sck, null, data);
					if(tmpreq.equals("error")){
						isrun=false;
						System.out.println("自动连接MQTT出现故障，停止监听设备状态进程......");
					}
				};
				if(flag==200){
					ReceiveScretKey(sck);
					flag=0;
				}
				flag++;
			} catch (Exception e) {
				isrun = false;
				System.out.println("MQTT出现故障，停止监听设备状态进程......");
//				e.printStackTrace();
//				new THD_TWO(sck).start();
			}
		}

	}
	
	/**
	 * 定时发送订阅获取密钥
	 * @param socket
	 */
	public void ReceiveScretKey(Socket socket){
		    String[] dyzthf= mqttut.seconddyhf(socket);//发送订阅
		   // 长连接获取云端的主题 密钥主题/ 获取测试点参数ID 不必等待云端参数设定信息，可以直接开始发送运行数据。
		     if(dyzthf.length!=6){
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
			
	}
	
}
