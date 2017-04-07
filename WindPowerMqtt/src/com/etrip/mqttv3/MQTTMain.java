package com.etrip.mqttv3;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.Uninterruptibles;
import com.hy.bean.AESUtils;
import com.hy.bean.CRCUtils;
import com.hy.bean.MqttUtil;
import com.hy.bean.SunModbusTcpbyIp;
import com.hy.pojo.CdhData;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class MQTTMain {
	 static MqttUtil mq=new MqttUtil();
	public static void main(String[] args) {
		//订阅消息的方法
		//MQTTSubsribe.doTest();
		//发布消息的类
		List<Device> devicelist = new ArrayList<Device>();
		devicelist=mq.startDataAcq();
		//每一分钟发送一次心跳。每5分钟发送一次运行数据
		byte [] bytecode=null;
	//	Uninterruptibles.sleepUninterruptibly(2000, TimeUnit.MILLISECONDS);
			for (int num = 0; num < devicelist.size(); num++) {
				Device devone = devicelist.get(num);
				bytecode=sedrunddate(devone,0,null,null,null);
			    MQTTPub.doTest("Wind/BaseData",bytecode);
			};
		//sedrunddate();
	     
		
	}

	private static byte[] sedrunddate(Device dev,int typezt,Socket sck,List<CdhData> cdhlistone,Data_Acq data) { 
		 
		// TODO Auto-generated method stub
		 SunModbusTcpbyIp stp=new SunModbusTcpbyIp();
			//主题+设备明文+加密：上次设备名称，设备序列号，设备参数值
			List<CdhData> cdhlist=null;
				//获取设备基本信息
			//cdhlist=mq.getBasedata(dev,data);
			//设备序号24位明文
/*			 String svstr=mq.getSystemServer("sn");
			 String [] svhex=mq.toHexString(svstr);
			 String []svstrmw=new String[24];
			 System.arraycopy(svhex, 0, svstrmw,0, svhex.length);
			 for(int j=svhex.length;j<24;j++){
				 svstrmw[j]="00";
		     }*/
			 //获取连接设备的名称，序列号
			 String[] systemdevicename=mq.getcsdcode(50000,null,null);
			 String[] systemdevicensn=mq.getcsdcode(50001,null,null);
			 String[] systemdevicentime=mq.getcsdcode(50002,null,null);
			 String[] devicenname=mq.getcsdcode(50005,null,dev);
			 String[] devicenlx=mq.getcsdcode(50003,null,dev);
			//获取所有运行状态下的测试点数据
			int leng=0;
			List<String[]> listchd=new ArrayList();
	/*		for(int i=0;i<cdhlist.size();i++){
				//获取测点的数据信息
				String[] str=mq.getcddatacode(cdhlist.get(i));
				listchd.add(str);
				leng+=str.length;
			}*/
			String[] rundata=new String[leng];
			int lengtmp=0;
			for(int k=0;k<listchd.size();k++){
				String[] str=listchd.get(k);
				System.arraycopy(str, 0, rundata, lengtmp, str.length);
				lengtmp+=str.length;
			}
			String[] jmrundataall=new String[systemdevicename.length+systemdevicensn.length+systemdevicentime.length+devicenname.length+devicenlx.length+rundata.length];
			
			System.arraycopy(systemdevicename, 0, jmrundataall, 0, systemdevicename.length);
			System.arraycopy(systemdevicensn, 0, jmrundataall, systemdevicename.length, systemdevicensn.length);
			System.arraycopy(systemdevicentime, 0, jmrundataall, systemdevicename.length+systemdevicensn.length, systemdevicentime.length);
			System.arraycopy(devicenname, 0, jmrundataall, systemdevicename.length+systemdevicensn.length+ systemdevicentime.length, devicenname.length);
			System.arraycopy(devicenlx, 0, jmrundataall, systemdevicename.length+systemdevicensn.length+ systemdevicentime.length+devicenname.length, devicenlx.length);
		//	System.arraycopy(rundata, 0, jmrundataall, systemdevicename.length+systemdevicensn.length+systemdevicentime.length+devicenname.length+devicenlx.length, rundata.length);
			String yxztstrtmp="";
			//长度不足16的倍数，补FF处理。
			int zs=jmrundataall.length/16;
			int ys=jmrundataall.length%16;
			if(ys>0){
				zs++;
			}
			String[] allcode=new String[16*zs];
			System.arraycopy(jmrundataall, 0, allcode, 0, jmrundataall.length);

			for(int i=jmrundataall.length;i<allcode.length;i++){
				allcode[i]="FF";
			}
			System.arraycopy(jmrundataall, 0, allcode, 0, jmrundataall.length);
			for (int i = 0; i < allcode.length; i++) {
				if(i==allcode.length-1){
					yxztstrtmp+=allcode[i];
				}else{
				   yxztstrtmp+=allcode[i]+" ";
				}
			}
			try {
				String key=mq.getKey("private_key");
				String jmrundatatmp=new AESUtils().encrypt(yxztstrtmp, key);
				String[] ymjzmstr=jmrundatatmp.split(" ");
				//其中 01 表示AES-128加密类型 50 00 表示有效数据长度为80 06 36 表示数据的CRC16校验值
				String[] basecocde=new String[5];
				String dataleng=Integer.toHexString(jmrundataall.length);
				String name = mq.get4HexString(dataleng);
				//校验
				String crc21 = CRCUtils.checkCRC16(jmrundatatmp);
				basecocde[0]="01";
				basecocde[1]=name.split(",")[1];
				basecocde[2]=name.split(",")[0];
				basecocde[3]=crc21.substring(2, 4);
				basecocde[4]=crc21.substring(0, 2);
				
				String[] tallcode=new String[ymjzmstr.length+basecocde.length];
			 /*  //加载主题
				System.arraycopy(ztstr, 0, tallcode, 0, ztstr.length);
				//明文设备名称
				System.arraycopy(svstrmw, 0, tallcode, ztstr.length, svstrmw.length);*/
				//校验码
				System.arraycopy(basecocde, 0, tallcode, 0, basecocde.length);
				//加密设备基本信息和运行数据
				System.arraycopy(ymjzmstr, 0, tallcode, basecocde.length, ymjzmstr.length);
				byte[] cdhbyte=new byte[tallcode.length];
				for(int i=0;i<cdhbyte.length;i++){
					cdhbyte[i]=(byte) Integer.parseInt(tallcode[i],16);
				}
				return cdhbyte;
				//String cdhdatastr = new String(cdhbyte);
			   }
			   catch(Exception e){
				  e.printStackTrace();
				 // return "error";
			 }
			return null;
	}
}
