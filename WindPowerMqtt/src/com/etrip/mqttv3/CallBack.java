package com.etrip.mqttv3;

import com.hy.bean.AESUtils;
import com.hy.bean.CRCUtils;
import com.hy.bean.MqttUtil;
import com.hy.bean.SunModbusTcpbyIp;
import com.ibm.micro.client.mqttv3.MqttCallback;
import com.ibm.micro.client.mqttv3.MqttDeliveryToken;
import com.ibm.micro.client.mqttv3.MqttMessage;
import com.ibm.micro.client.mqttv3.MqttTopic;

public class CallBack implements MqttCallback {
	static MqttUtil mqttut = new MqttUtil();

	public void messageArrived(MqttTopic topic, MqttMessage message) {
		try {
			System.out.println(" MQTTSubsribe  topic" + topic.toString());
			//Thread.sleep(2000);
	/*		if(topic.toString().equals("PC/Password")){
				getcssd(message.getPayload());	
			}*/
			// message.ack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectionLost(Throwable cause) {

	}

	public void deliveryComplete(MqttDeliveryToken token) {

	}

	public void getcssd(byte[] codebyte) {
		String strj[] = new String[codebyte.length];
		for (int i = 0; i < codebyte.length; i++) {
			int str = codebyte[i];
			strj[i] = Integer.toHexString(str);
			String ss = Integer.toHexString(str);
			if (ss.length() >= 2) {
				strj[i] = ss.substring(ss.length() - 2, ss.length());
			} else {
				strj[i] = "0" + ss;
			}
		}
		String[] csdsecs =parseztstr(strj);
		getcssdtobyte(csdsecs);
	}

	@SuppressWarnings("static-access")
	public void getcssdtobyte(String[] csdsecs) {
		MqttUtil mq = new MqttUtil();
		// 参数设定主题+设备序号明文+加密：设备名称+设备序号+服务器接收过来的测定数据的成功和失败设置
		/*
		 * //主题 cssdztcode String[]cssdztcode=getZT("CSSD");
		 * 
		 * // 明文的设备序号 前24个序号 String svstr1 = getSystemServer("sn"); if (svstr1
		 * == null) { return null; } String[] svhex1 = toHexString(svstr1);
		 * String[] svstrmw = new String[24]; System.arraycopy(svhex1, 0,
		 * svstrmw, 0, svhex1.length); for (int j = svhex1.length; j < 24; j++)
		 * { svstrmw[j] = "00"; }
		 */
		// 加密数据：设备名称，设备序号，测定结果设置
		String[] devname = mq.getcsdcode(50000, null, null);
		String[] devsn = mq.getcsdcode(50001, null, null);
		String[] devcsjg = mq.getcsdcode(50006, csdsecs, null);
		String[] allcodeone = new String[devname.length + devsn.length + devcsjg.length];
		System.arraycopy(devname, 0, allcodeone, 0, devname.length);
		System.arraycopy(devsn, 0, allcodeone, devname.length, devsn.length);
		System.arraycopy(devcsjg, 0, allcodeone, devname.length + devsn.length, devcsjg.length);
		int zs = allcodeone.length / 16;
		int ys = allcodeone.length % 16;
		if (ys > 0) {
			zs++;
		}
		String[] allcode = new String[16 * zs];
		System.arraycopy(allcodeone, 0, allcode, 0, allcodeone.length);

		for (int i = allcodeone.length; i < allcode.length; i++) {
			allcode[i] = "FF";
		}

		String sykey = mq.getKey("private_key");
		String yxztstrtmp = "";
		for (int i = 0; i < allcode.length; i++) {
			if (i == allcode.length - 1) {
				yxztstrtmp += allcode[i];
			} else {
				yxztstrtmp += allcode[i] + " ";
			}
		}
		// 对数据进行加密
		String ymjzm = "";
		try {
			ymjzm = new AESUtils().encrypt(yxztstrtmp, sykey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] ymjzmstr = ymjzm.split(" ");

		// 其中 01 表示AES-128加密类型 50 00 表示有效数据长度为80 06 36 表示数据的CRC16校验值
		String[] basecocde = new String[5];
		String dataleng = Integer.toHexString(allcodeone.length);
		String name = mq.get4HexString(dataleng);
		// 校验
		String crc21 = CRCUtils.checkCRC16(ymjzm);
		basecocde[0] = "01";
		basecocde[1] = name.split(",")[1];
		basecocde[2] = name.split(",")[0];
		basecocde[3] = crc21.substring(2, 4);
		basecocde[4] = crc21.substring(0, 2);
		String[] cssdallcodeone = new String[ymjzmstr.length + basecocde.length];
		// 加载校验基本信息
		System.arraycopy(basecocde, 0, cssdallcodeone, 0, basecocde.length);
		// 加载加密数据
		System.arraycopy(ymjzmstr, 0, cssdallcodeone, basecocde.length, ymjzmstr.length);
		System.out.println("发送设定参数" + mq.sztostr(cssdallcodeone));
		byte data[] = new byte[cssdallcodeone.length];
		for (int i = 0; i < cssdallcodeone.length; i++) {
			byte b = Integer.valueOf(cssdallcodeone[i], 16).byteValue();// 16进制字符转10进制字节数
			data[i] = b;
		}
		MQTTPub.doTest("Wind/SetPara",data);
	}
	@SuppressWarnings("unused")
	public String[] parseztstr(String[] getztstr) {
		//int ztid=Integer.parseInt(getztstr[0],16)+6;
		int ztid=0;
		// 获取加密方式
		String aes = getztstr[ztid];
		// 数据长度
		String dataleng = getztstr[ztid+2];
		String datalend = getztstr[ztid+1];
		String datahall=dataleng+datalend;
		int dataalllength =Integer.parseInt(datahall, 16);
		// Crt16校验值
		String CRC16G = getztstr[ztid+4];
		String CRC16D = getztstr[ztid+3];
		String CRC16 = CRC16D + CRC16G;
		// 获取公钥 ，查询数据库获取
		String gykey = mqttut.getKey("public_key");
		if(gykey==""||gykey==null){
			System.out.println("获取公钥失败！");
			return null;
		}
		// 获取有效数据
		String[] yxztstr = new String[getztstr.length-5];
		System.arraycopy(getztstr, 5, yxztstr, 0, getztstr.length-5);
		String yxztstrtmp="";
		for (int i = 0; i < yxztstr.length; i++) {
			if(i==yxztstr.length-1){
				yxztstrtmp+=yxztstr[i];
			}else{
			   yxztstrtmp+=yxztstr[i]+" ";
			}
		}
		// 解密后的有效数组
		String ymjzm="";
		try {
			 ymjzm = new AESUtils().decrypt(yxztstrtmp, gykey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 进行CRT16校验
		String crc21 = CRCUtils.checkCRC16(ymjzm);
		if(!CRC16.equals(crc21)){
			System.out.println("CRC16校验失败！");
			return null;
		}
		String[] ymjzmstr=ymjzm.split(" ");
		
		// 获取测点号
		String cdstr=ymjzmstr[1]+""+ymjzmstr[0];
		int chdnew=Integer.parseInt(cdstr, 16);
		//获取测点号数据类型
		String datastr=ymjzmstr[3]+""+ymjzmstr[2];
		int data = Integer.parseInt(datastr,16);
		String bit = Integer.toBinaryString(data);
		// 测点号数据类型取前6位，数量取后10位
		String sl = bit.substring(bit.length() - 10, bit.length());
		String lx = bit.substring(0, bit.length() - 10);
		int lxint = Integer.parseInt(lx,2);//二进制转十进制整型
		int slint= Integer.parseInt(sl,2);
		// 测点号 获取设备测定号数据
		String[] cdhdata = new String[slint];
		System.arraycopy(ymjzmstr, 4, cdhdata, 0, slint);
		
		// 获取密匙=52003
		int strmy = slint + 4;
		String arraytmp1 = ymjzmstr[strmy + 1]+ymjzmstr[strmy];
		int myint = Integer.parseInt(arraytmp1,16);
		// 获取数据类型和数量
		String arraytmp2 = ymjzmstr[strmy + 3]+ymjzmstr[strmy+2];
		int mydata =Integer.parseInt(arraytmp2,16);
		String mybit = Integer.toBinaryString(mydata);
		// 数据类型取前6位，数量取后10位
		String mysl = mybit.substring(mybit.length() - 10, mybit.length());
		String mylx = mybit.substring(0, mybit.length() - 10);
		int mylxint = Integer.parseInt(mylx,2);
		int myslint = Integer.parseInt(mysl,2);
		// 获取密钥数组
		String mybyte[] = new String[myslint];
		System.arraycopy(ymjzmstr, 4 + slint + 4, mybyte,0 , myslint);
		byte[] mytmp=new byte[myslint];
		for(int i=0;i<mybyte.length;i++){
			mytmp[i]=(byte) Integer.parseInt(mybyte[i],16);
		}
		String mykey = new String(mytmp);
		// 存入数据库中
		//mqttutinsertkey(mykey);
		
		
		byte[] cdhbyte=new byte[cdhdata.length];
		for(int i=0;i<cdhbyte.length;i++){
			cdhbyte[i]=(byte) Integer.parseInt(cdhdata[i],16);
		}
		String cdhdatastr = new String(cdhbyte);
		return cdhdata;
	}
}