package com.hy.data;

import static com.google.common.util.concurrent.Service.State.NEW;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.Service;
import com.google.common.util.concurrent.Uninterruptibles;
import com.hy.bean.AESUtils;
import com.hy.bean.CRCUtils;
import com.hy.bean.JDBConnection;
import com.hy.bean.MqttUtil;
import com.hy.bean.SunModbusTcpbyIp;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class SungrowService extends AbstractService implements Service {
	private State state = NEW;
	boolean  isrun = true;
	private Socket sck;
	private Listener listener;
	 static MqttUtil mqttut=new MqttUtil();
	SungrowService(Socket sck) {
		this.sck = sck;
	}

	public State stopAndWait() {
		return state;
	}

	@Override
	protected void doStart() {
		new Thread() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				notifyStarted();
				int numct=0;
				while (isrun) {
					List<Device> devicelist = new ArrayList<Device>();
					devicelist=mqttut.startDataAcq();
					//每一分钟发送一次心跳。每5分钟发送一次运行数据
					Uninterruptibles.sleepUninterruptibly(60000, TimeUnit.MILLISECONDS);
					String strMessage="";
					int flag = 0;
					if(numct==5){
						for (int num = 0; num < devicelist.size(); num++) {
							Device devone = devicelist.get(num);
							 strMessage = mqttut.startthreadother(devone,sck);
							 if(!strMessage.equals("无数据")){
								 flag = 1;
							 }
						};
						if(flag != 1){
							//无数据 发送心跳，防止连接断开
							sendHeartBit(sck);
						}
						numct=0;
					}else{
//						SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//						System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
						//发送心跳
						sendHeartBit(sck);
						numct++;
					}
				}
			}

			

		}.start();
	}
	@Override
	protected void doStop() {
		notifyStopped();
		new Thread() {
			@Override
			public void run() {
				Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
				notifyStopped();
			}
		}.start();
	}
	
	public void sendHeartBit(Socket sck){
		String[] xthfcode=mqttut.sendxtcode(sck);
		if(xthfcode==null){	
			new THD_Four(sck).start();
			isrun=false;
		}
	}
}