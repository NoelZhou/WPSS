package com.hy.bean;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.google.common.util.concurrent.Uninterruptibles;
import com.hy.data.SungrowService;
import com.hy.pojo.CdhData;
import com.hy.pojo.Data_Acq;
import com.hy.pojo.Device;

public class MqttUtil {
	private static  Map<Integer,Boolean>  faultstate= new Hashtable<Integer, Boolean>();
	public static void main(String[] args) {
		String ss="PC/Password";
		String[] st=toHexString(ss);
		
/*		   int code_h=codeleg/255;
		   int code_l=codeleg%255;*/
		short[] code11=new short[2];
		    code11[0]=2;
		    code11[1]=175;
			short[] code12=new short[2];
		    code12[1]=2;
		    code12[0]=175;
		  int lk= unshortToInt(code11,0);
		  int ls= unshortToInt(code12,0);
		Socket sck = null;
		try {
			sck = new Socket("192.168.69.68", 1883);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Device> devicelist = new ArrayList<Device>();
		devicelist = startDataAcq();
		for (int num = 0; num < devicelist.size(); num++) {
			Device devone = devicelist.get(num);
			startthreadother(devone, sck);
		}
		;

	}
	/**
	 * 发送登陆
	 * 
	 * @return
	 */
	public String[] dlhf(Socket socket) {
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		// 第一步 登陆 服务器，登陆mqtt服务器A140506987
		 String svstr=getSystemServer("sn");//获取系统服务信息
		 //字符串转成16进制
		String[] svhex = toHexString(svstr);//sn19891009
		String[] loadcode = { "10", "18", "00", "06", "4D", "51", "49", "73", "64", "70", "03", "02", "00", "3C", "00",
				"0A" };
		//playload长度
		int svlent=svhex.length;
		String svlenghex=Integer.toHexString(svlent);
		//因为设备长度不会超过125，
		loadcode[15]=get4HexString(svlenghex).split(",")[1];
		//协议长度 在最后发送的时候处理
/*		int mqttlent=svhex.length+16;
		String mqttlenghex=Integer.toHexString(mqttlent-2);
		loadcode[1]=mqttlenghex;*/
		String[] loadallcode = new String[svhex.length + loadcode.length];
		// 合并数组
		System.arraycopy(loadcode, 0, loadallcode, 0, loadcode.length);
		System.arraycopy(svhex, 0, loadallcode, loadcode.length, svhex.length);
		//字符数组转字符串
		String strscpde = sztostr(loadallcode);//带空格的字符串
		System.out.println(svstr + "设备登录指令发送：" + strscpde);
		//发送登陆指令
		String[] str = stp.sendonload(loadallcode, socket);
		if (str!= null) {
			String strs = sztostr(str);
			System.out.println(svstr + "设备登陆云端服务器成功！返回状态码：" + strs);
		} else {
			System.out.println(svstr + "设备登陆云端服务器失败！真正重新发送");
			for (int i = 0; i < 5; i++) {
				String[] str1 = stp.sendonload(loadallcode, socket);
				if(str1!=null){
					if (str1.length > 0) {
						System.out.println(svstr + "设备登陆云端服务器成功！返回状态码：" + str1.toString());
						break;
					}
				}
			}
		}
		return str;
	}
	
	/**
	 * 发送订阅
	 * 
	 * @return
	 */
	public String[] dyhf(Socket socket) {
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		String[] dyzt=getZT("DY");
		String strs = sztostr(dyzt);//字符数组转换成字符串
		System.out.println("设备发送订阅指令：" + strs);
		String[] strdy = stp.sendonload(dyzt, socket);//发送登陆指令
		if (strdy.length > 0) {
			String strfh = sztostr(strdy);
			System.out.println("设备订阅成功返回：" + strfh);
		} else {
			System.out.println("设备订阅失败！真正从新发送");
			for (int i = 0; i < 5; i++) {
				String[] str1 = stp.sendonload(dyzt, socket);
				if (str1.length > 0) {
					System.out.println("设备订阅成功！");
					break;
				}
			}
		}
		return strdy;
	}
	/**
	 * 发送二次订阅
	 * 
	 * @return
	 */
	public String[] seconddyhf(Socket socket) {
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		String[] dyzt=getZT("DY");
		String strs = sztostr(dyzt);//字符数组转换成字符串
		System.out.println("设备发送订阅指令：" + strs);
		String[] strdy = stp.sendonloaddy(dyzt, socket);//发送登陆指令
		if (strdy.length > 0) {
			String strfh = sztostr(strdy);
			System.out.println("设备订阅成功返回：" + strfh);
		} else {
			System.out.println("设备订阅失败！真正从新发送");
			for (int i = 0; i < 5; i++) {
				String[] str1 = stp.sendonload(dyzt, socket);
				if (str1.length > 0) {
					System.out.println("设备订阅成功！");
					break;
				}
			}
		}
		return strdy;
	}
	/**
	 * 发送订阅确认回复
	 * 
	 * @return
	 */
	public void sbdyhf(String [] dystr,Socket socket) {
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		int ztid=Integer.parseInt(dystr[3],16)+3;
		String[] hfcode=new String[4];
		hfcode[0]="40";
		hfcode[1]="02";
		hfcode[2]=dystr[ztid+1];
		hfcode[3]=dystr[ztid+2];
		String strs = sztostr(hfcode);
		System.out.println("设备发送订阅确认指令：" + strs);
		stp.sendack(hfcode, socket);
	}
	
	/**
	 * 发送心跳
	 * @param sck
	 */
	public String[] sendxtcode(Socket sck) {
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		// TODO Auto-generated method stub
		String[] xtcode={"C0","00"};
		String[] cssdallcodeonehf = stp.sendonload(xtcode,sck);
		if(cssdallcodeonehf==null){
			System.out.println("心跳返回结果："+(cssdallcodeonehf));
		}else{
		System.out.println("心跳返回结果："+sztostr(cssdallcodeonehf));}
		return cssdallcodeonehf;
	}
	/**
	 * 参数设定信息
	 */
	public String[] cssd(String[] csdsecs, Socket socket) {
		//参数设定主题+设备序号明文+加密：设备名称+设备序号+服务器接收过来的测定数据的成功和失败设置
		SunModbusTcpbyIp stp = new SunModbusTcpbyIp();
		//主题 cssdztcode
		String[]cssdztcode=getZT("CSSD");
		
		// 明文的设备序号    前24个序号
		String svstr1 = getSystemServer("sn");
		if (svstr1 == null) {
			return null;
		} 
		String[] svhex1 = toHexString(svstr1);
		String[] svstrmw = new String[24];
		System.arraycopy(svhex1, 0, svstrmw, 0, svhex1.length);
		for (int j = svhex1.length; j < 24; j++) {
			svstrmw[j] = "00";
		}
		
        //加密数据：设备名称，设备序号，测定结果设置
		String[] devname = getcsdcode(50000, null,null);
		String[] devsn = getcsdcode(50001, null,null);
		String[] devcsjg = getcsdcode(50006, csdsecs,null);
		String[] allcodeone = new String[devname.length + devsn.length + devcsjg.length];
		System.arraycopy(devname, 0, allcodeone, 0, devname.length);
		System.arraycopy(devsn, 0, allcodeone, devname.length, devsn.length);
		System.arraycopy(devcsjg, 0, allcodeone, devname.length+devsn.length, devcsjg.length);
		int zs=allcodeone.length/16;
		int ys=allcodeone.length%16;
		if(ys>0){
			zs++;
		}
		String[] allcode=new String[16*zs];
		System.arraycopy(allcodeone, 0, allcode, 0, allcodeone.length);

		for(int i=allcodeone.length;i<allcode.length;i++){
			allcode[i]="FF";
		}
		
		String sykey = getKey("private_key");
		String yxztstrtmp="";
		for (int i = 0; i < allcode.length; i++) {
			if(i==allcode.length-1){
				yxztstrtmp+=allcode[i];
			}else{
			   yxztstrtmp+=allcode[i]+" ";
			}
		}
		// 对数据进行加密
		String ymjzm="";
		try {
			 ymjzm = new AESUtils().encrypt(yxztstrtmp, sykey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] ymjzmstr=ymjzm.split(" ");
		
		//其中 01 表示AES-128加密类型 50 00 表示有效数据长度为80 06 36 表示数据的CRC16校验值
		String[] basecocde=new String[5];
		String dataleng=Integer.toHexString(allcodeone.length);
		String name = get4HexString(dataleng);
		//校验
		String crc21 = CRCUtils.checkCRC16(yxztstrtmp);
		basecocde[0]="01";
		basecocde[1]=name.split(",")[1];
		basecocde[2]=name.split(",")[0];
		basecocde[4]=crc21.substring(2, 4);
		basecocde[3]=crc21.substring(0, 2);
		String[] cssdallcodeone = new String[cssdztcode.length + svstrmw.length + ymjzmstr.length+basecocde.length];
		//加载主题
		System.arraycopy(cssdztcode, 0, cssdallcodeone, 0, cssdztcode.length);
		//加载明白设备名称
		System.arraycopy(svstrmw, 0, cssdallcodeone, cssdztcode.length, svstrmw.length);
		//加载校验基本信息
		System.arraycopy(basecocde, 0, cssdallcodeone, cssdztcode.length + svstrmw.length, basecocde.length);
		//加载加密数据
		System.arraycopy(ymjzmstr, 0, cssdallcodeone, cssdztcode.length + svstrmw.length+basecocde.length, ymjzmstr.length);

		System.out.println("发送设定参数"+sztostr(cssdallcodeone));
		String[] cssdallcodeonehf = stp.sendonload(cssdallcodeone, socket);
		System.out.println("发送设定参数返回结果"+sztostr(cssdallcodeonehf));
		return cssdallcodeonehf;
	}
/**
 * 解析订阅主题参数信息，更新密匙等信息
 * @param getztstr
 * @return
 */
	@SuppressWarnings("unused")
	public String[] parseztstr(String[] getztstr) {
		int ztid=Integer.parseInt(getztstr[3],16)+6;
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
		String gykey = getKey("public_key");
		if(gykey==""||gykey==null){
			System.out.println("获取公钥失败！");
			return null;
		}
		// 获取有效数据
		String[] yxztstr = new String[getztstr.length-22];
		System.arraycopy(getztstr, 22, yxztstr, 0, getztstr.length-22);
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
		insertkey(mykey);
		
		
		byte[] cdhbyte=new byte[cdhdata.length];
		for(int i=0;i<cdhbyte.length;i++){
			cdhbyte[i]=(byte) Integer.parseInt(cdhdata[i],16);
		}
		String cdhdatastr = new String(cdhbyte);
		return cdhdata;
	}
	
	/**
	 * 获取测点的数据信息
	 * @param chd
	 * @return
	 */
	public static String[] getcddatacode(CdhData chd) {
		// 设备名称
		int cdhid = chd.getCdid();
		int cdhlx = chd.getLx();
		int cdhlxlen = chd.getLxlen();
		float addrdata = chd.getValue();
		String addrstr = chd.getStrvalue();
		//非字符串，取类型的长度，不足补00
		int cdhlxlen_ys = chd.getLxlen();

		if (addrstr != null && addrstr != "") {
			String[] addrjexstr = toHexString(addrstr);
			cdhlxlen = addrjexstr.length;
		}
		// 总长度
		String[] tmp = new String[cdhlxlen + 4];
		if(cdhlx==3){
			tmp = new String[cdhlxlen_ys + 4];
		}
		
		// 测试点号转换
		String idhex = Integer.toHexString(cdhid);
		String idhex4 = get4HexString(idhex);
		tmp[0] = idhex4.split(",")[1];
		tmp[1] = idhex4.split(",")[0];

		// 数据类型和长度的转换 前6位位数据类型;后10位数据长度
		
		if(cdhlx!=12){
			//测试类型不为12时，后10位表示测点长度，即有几个测点数据。
			cdhlxlen=1;
		}
		String namelen = Integer.toBinaryString(cdhlxlen);
		// 没有10位补充0(补零操作)
		String namelen10 = getbit10(namelen);
		// 数据类型
		String sulx = "";
		sulx = Integer.toBinaryString(cdhlx);
		String namehb = sulx + namelen10;
		String namehexstr = Integer.toHexString(Integer.parseInt(namehb, 2));
		String name = get4HexString(namehexstr);
		tmp[2] = name.split(",")[1];
		tmp[3] = name.split(",")[0];
		String[] nametmp = null;
		if (addrstr != null && addrstr != "") {
			nametmp = toHexString(addrstr);
			if(cdhlx!=12){
				String[] nametmpnew = null;
				nametmpnew = new String[cdhlxlen_ys];
				System.arraycopy(nametmp, 0, nametmpnew, cdhlxlen_ys - nametmp.length, nametmp.length);
				// 长度不足，补0
				for (int i = 0; i < cdhlxlen_ys - nametmp.length; i++) {
					nametmpnew[i] = "00";
				}
				nametmp=nametmpnew;
			}
			
		} else {
			// 对数据值处理
			String datatmp = null;
			String[] namestr=null;
			if(cdhlx==9){
				 datatmp=Integer.toHexString(Float.floatToIntBits(addrdata));
				 namestr = getFloat_4HexString(datatmp).split(",");
			}else{
				 int  addint=(int)addrdata;
				 datatmp = Integer.toHexString(addint);
				 namestr = get4HexString(datatmp).split(",");
			}
			
			if (cdhlxlen_ys == 1) {
				String tmpstr = get4HexString(datatmp).split(",")[1];
				namestr = new String[1];
				namestr[0] = tmpstr;
			}
			nametmp = new String[cdhlxlen_ys];
			System.arraycopy(namestr, 0, nametmp, cdhlxlen_ys - namestr.length, namestr.length);
			// 长度不足，补0
			for (int i = 0; i < cdhlxlen_ys - namestr.length; i++) {
				nametmp[i] = "00";
			}
		}
		System.arraycopy(nametmp, 0, tmp, 4, nametmp.length);
		return tmp;
	}
/**
 * 设置参数设定信息
 * @param id
 * @param dev
 * @param csdcgbz
 * @return
 */
	public static String[] getcsdcode(int id, String[] csdcgbz,Device dev) {
		// 设备名称
		String tmpstr = "";
		String devip="";
		String[]   namehex=null;
		if (id == 50000) {
			tmpstr = getSystemServer("name");
		} else if(id==50001){
			tmpstr =getSystemServer("sn");
		}else if(id==50005){
			tmpstr = dev.getName();
		}else if(id==50003){
			tmpstr=dev.getLx()+"";
			devip=dev.getDevice_addrid()+"";
		}else if(id==50002){
	    	Date d = new Date();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d);
			//每天0点兑时
			int YEAR=gc.get(GregorianCalendar.YEAR);
			int MONTH=gc.get(GregorianCalendar.MONTH)+1;
			int DATE=gc.get(GregorianCalendar.DATE);
			int HOUR=gc.get(GregorianCalendar.HOUR_OF_DAY);
			int MINUTE=gc.get(GregorianCalendar.MINUTE);
			int SECOND=gc.get(GregorianCalendar.SECOND);
			
			String yearste=get4HexString(Integer.toHexString(YEAR));
			String MONTHste=get4HexString(Integer.toHexString(MONTH));
			String DATEste=get4HexString(Integer.toHexString(DATE));
			String HOURste=get4HexString(Integer.toHexString(HOUR));
			String MINUTEste=get4HexString(Integer.toHexString(MINUTE));
			String SECONDste=get4HexString(Integer.toHexString(SECOND));
			int yearlea=yearste.split(",").length;
			int MONTHlea=MONTHste.split(",").length;
			int DATElea=DATEste.split(",").length;
			int HOURlea=HOURste.split(",").length;
			int MINUTElea=MINUTEste.split(",").length;
			int SECONDlea=SECONDste.split(",").length;
			namehex=new String[yearlea+5];
			int i=0;
			if(yearlea>=2){
				namehex[i]=	yearste.split(",")[0];
				namehex[i+1]=	yearste.split(",")[1];
				i=i+2;
			}else {
				namehex[i]=	yearste.split(",")[0];
				i++;
			}
				namehex[i]=	MONTHste.split(",")[1];
				i++;
				namehex[i]=	DATEste.split(",")[1];
				i++;
				namehex[i]=	HOURste.split(",")[1];
				i++;
				namehex[i]=	MINUTEste.split(",")[1];
				i++;
				namehex[i]=	SECONDste.split(",")[1];
				i++;
		}else{ 
			// 查询数据库中最新的采集的测试点值 并且获取测试点得数据类型
			namehex=new String[csdcgbz.length+1];
			//设定31为恢复成功
			namehex[0]="31";
			//设定30为回复失败
			System.arraycopy(csdcgbz, 0, namehex, 1, csdcgbz.length);
			
			byte[] cdhbyte=new byte[namehex.length];
			for(int i=0;i<cdhbyte.length;i++){
				cdhbyte[i]=(byte) Integer.parseInt(namehex[i],16);
			}
			String cdhdatastr = new String(cdhbyte);
			System.out.println(cdhdatastr);
		}
		// 测试点值转换
         if(id==50003){
			String ss=get4HexString(Integer.toHexString(Integer.parseInt(tmpstr))).split(",")[1];
			String[] str=new String[2];
			str[0]=ss;
			str[1]=get4HexString(Integer.toHexString(Integer.parseInt(devip))).split(",")[1];
		    namehex=str;
		}else if(csdcgbz==null&&id!=50002){
			 namehex = toHexString(tmpstr);
		}
		
		int nameint = namehex.length;
		// 测试点号转换
		String[] tmp = new String[nameint+4];
		String idhex = Integer.toHexString(id);
		String idhex4 = get4HexString(idhex);
		tmp[0] = idhex4.split(",")[1];
		tmp[1] = idhex4.split(",")[0];
		
		// 前6位位数据类型;后10位数据长度
		String namelen = Integer.toBinaryString(nameint);
		// 没有10位补充0
		String namelen10 = getbit10(namelen);
		//数据类型
		String sulx = "";
		int chdjlx=getCdhsjlx(id);
	    sulx=Integer.toBinaryString(chdjlx);
		String namehb = sulx + namelen10;
		String namehexstr = Integer.toHexString(Integer.parseInt(namehb, 2));
		String name = get4HexString(namehexstr);
		tmp[2] = name.split(",")[1];
		tmp[3] = name.split(",")[0];
		if(id==50002){
			//位时间是表示的是测试点数，不是数据长度
			tmp[2] = "01";
			tmp[3] = "3C";
		}
		System.arraycopy(namehex, 0, tmp, 4, namehex.length);
		return tmp;
	}
    
	/**
	 * 开始上传云服务器
	 * @param dev
	 */
	public static String startthreadother(Device dev,Socket sck){
		//0表示上传运行数据，1表示上传历史数据,2表示设备基本信息及统计信息数据，无论有无故障都发送
	   // sendrundatacode(dev,2,sck,null,null);
		//0表示上传运行数据，1表示上传历史数据
	   String str =  sendrundatacode(dev,0,sck,null,null);
		return str;
		
	}
	public static void startpushhistorydata(Socket sck){
	    //获取历史库数据
		gethistorydata(sck);
	}
	/**
	 * 获取历史库数据
	 * @return
	 */
	private static void gethistorydata(Socket sck) {
			List<CdhData> cdall = new ArrayList<CdhData>();
			try {
				JDBConnection jdbc_trd = new JDBConnection();
				Connection conn_trd = jdbc_trd.connection;
				ResultSet rs_trd = null;
				Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "select * from windpower_db.mqtt_dataacq_history   order  by  id desc  ";
				rs_trd = st_trd.executeQuery(sql);
				String create_time = "";
				String data = "";
				int id=0;
				int did=0;
				while (rs_trd.next()) {
					id=rs_trd.getInt("id");
					data = rs_trd.getString("data");
					create_time = rs_trd.getString("create_time");
					did=rs_trd.getInt("device_id");
					//根据设备ID获取设备的基本信息
					Device dev=getDevicedata(did);
					if(dev==null||dev.equals("")){
						System.out.println("该设备已经删除.....");
					}else{
						//获取测试点
					cdall = getnewChdData(data, dev);
					//加入设备基本信息
					List<CdhData> cdalltmp = getDeviceBase(dev);
					cdall.addAll(cdalltmp);	
					sendrundatacode(dev,1, sck,cdall,null);
					}
					//插入历史库数据记录，并删除历史库已经上传的数据
					InsertHistory_jl(id,data,create_time,dev);
				}
				st_trd.close();
				conn_trd.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*Device dev=getDevicedata(4);
			//加入设备基本信息
			List<CdhData> cdalltmp = getDeviceBase(dev);
			cdall.addAll(cdalltmp);	
			sendrundatacode(dev,1, sck,cdall,null);*/
	}
	/**
	 * 根据设备获取设备的基本信息
	 * @param did
	 * @return
	 */
	private static Device getDevicedata(int did) {
		Device dev=new Device();
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select 	t.*,t1.modbus_type,t2.errorstate from windpower_db.mqtt_device t  ,windpower_devicetype t1 ,windpower_device t2 where t2.device_type_id=t1.id and t.device_id=t2.id and  t2.run_state=0  and t.run_state=0 and t2.id="+did ;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				dev.setDevice_id(rs_trd.getInt("device_id"));;
				dev.setD_type(rs_trd.getInt("modbus_type"));
				dev.setName(rs_trd.getString("name"));
				dev.setLx(rs_trd.getInt("device_yd_lx"));
				dev.setSn(rs_trd.getString("sn"));
				dev.setDevice_lx_mh(rs_trd.getFloat("device_lx_MH"));
				dev.setDevice_xh(rs_trd.getString("device_xh"));
				dev.setDevice_CJ(rs_trd.getString("device_CJ"));
				dev.setErrorstate(rs_trd.getString("errorstate"));
			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dev;
	}
	/**
	 * 发送运行数据
	 * @param dev
	 */
public static String sendrundatacode(Device dev,int typezt,Socket sck,List<CdhData> cdhlistone,Data_Acq data){
		 SunModbusTcpbyIp stp=new SunModbusTcpbyIp();
		//主题+设备明文+加密：上次设备名称，设备序列号，设备参数值
		List<CdhData> cdhlist=null;
		String[] ztstr=null;
		if(typezt==0){
			ztstr=getZT("YXSJ");
			cdhlist=getRundata(dev);	
		}else if(typezt==1) {
		    ztstr=getZT("HistorySJ");
			cdhlist=cdhlistone;
		}else{
			ztstr=getZT("DeviceBaseSJ");
			//获取设备基本信息
			cdhlist=getBasedata(dev,data);
		}
		
		if(cdhlist==null||cdhlist.size()<=0){
            return "无数据";
		}
		//设备序号24位明文
		 String svstr=getSystemServer("sn");
		 String [] svhex=toHexString(svstr);
		 String []svstrmw=new String[24];
		 System.arraycopy(svhex, 0, svstrmw,0, svhex.length);
		 for(int j=svhex.length;j<24;j++){
			 svstrmw[j]="00";
	     }
		 //获取连接设备的名称，序列号
		 String[] systemdevicename=getcsdcode(50000,null,null);
		 String[] systemdevicensn=getcsdcode(50001,null,null);
		 String[] systemdevicentime=getcsdcode(50002,null,null);
		 String[] devicenname=getcsdcode(50005,null,dev);
		 String[] devicenlx=getcsdcode(50003,null,dev);
		//获取所有运行状态下的测试点数据
		int leng=0;
		List<String[]> listchd=new ArrayList();
		for(int i=0;i<cdhlist.size();i++){
			//获取测点的数据信息
			String[] str=getcddatacode(cdhlist.get(i));
			listchd.add(str);
			leng+=str.length;
		}
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
		System.arraycopy(rundata, 0, jmrundataall, systemdevicename.length+systemdevicensn.length+systemdevicentime.length+devicenname.length+devicenlx.length, rundata.length);
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
			String key=getKey("private_key");
			String jmrundatatmp=new AESUtils().encrypt(yxztstrtmp, key);
			String[] ymjzmstr=jmrundatatmp.split(" ");
			//其中 01 表示AES-128加密类型 50 00 表示有效数据长度为80 06 36 表示数据的CRC16校验值
			String[] basecocde=new String[5];
			String dataleng=Integer.toHexString(jmrundataall.length);
			String name = get4HexString(dataleng);
			//校验
			String crc21 = CRCUtils.checkCRC16(yxztstrtmp);
			basecocde[0]="01";
			basecocde[1]=name.split(",")[1];
			basecocde[2]=name.split(",")[0];
			basecocde[4]=crc21.substring(2, 4);
			basecocde[3]=crc21.substring(0, 2);
			
			String[] tallcode=new String[ztstr.length+svstrmw.length+ymjzmstr.length+basecocde.length];
		   //加载主题
			System.arraycopy(ztstr, 0, tallcode, 0, ztstr.length);
			//明文设备名称
			System.arraycopy(svstrmw, 0, tallcode, ztstr.length, svstrmw.length);
			//校验码
			System.arraycopy(basecocde, 0, tallcode, ztstr.length+svstrmw.length, basecocde.length);
			//加密设备基本信息和运行数据
			System.arraycopy(ymjzmstr, 0, tallcode, ztstr.length+svstrmw.length+basecocde.length, ymjzmstr.length);
			String strfh1 = sztostr(ztstr);
			System.out.println("发主题送运行数据指令：" + strfh1);
			String strfh2 = sztostr(svstrmw);
			System.out.println("发送设备明文指令：" + strfh2);
			String strfh3 = sztostr(basecocde);
			System.out.println("发送校验码指令：" + strfh3);
			String strfh4 = sztostr(ymjzmstr);
			System.out.println("发送加密设备基本信息和运行数据指令：" + strfh4);
			String[] strdy=stp.sendonload(tallcode,sck);
			if(strdy==null){
				System.out.println("服务出现故障，返回结果失败！");
				return "error";
			}else{
		    	System.out.println("发送运行参数返回结果："+sztostr(strdy));
		    	return sztostr(strdy);
			}
		   }
		   catch(Exception e){
			  e.printStackTrace();
			  return "error";
		 }
	}

static Map<Integer,Integer> gzztmap=new Hashtable<Integer,Integer>();
static Map<Integer,Integer> bwztmap=new Hashtable<Integer,Integer>();
static Map<Integer,Integer> txztmap=new Hashtable<Integer,Integer>();
/**
 * 获取设备基本信息
 * @param dev
 * @param datatmp
 * @return
 */
public static List<CdhData> getBasedata(Device dev,Data_Acq datatmp){
	String data=datatmp.getDatastr();
	int dataid=datatmp.getId();
	boolean issend=false;
	List<CdhData> cdall = new ArrayList<CdhData>();
		if(data!=""&&data!=null){
				int value38 = Integer.parseInt(data.split(",")[38]);
				if(dev.getD_type()==2){
					 value38 = Integer.parseInt(data.split(",")[12836-12788]);
				}else{
					value38 = Integer.parseInt(data.split(",")[38]);
				}
				if(value38==0){
					gzztmap.put(dev.getDevice_id(), 1);
				}
			 if((value38>0&&gzztmap.get(dev.getDevice_id())==null)||(value38>0&&gzztmap.get(dev.getDevice_id())==1)){
					  CdhData cdhone1=new CdhData();
					  cdhone1.setCdid(28702);
					  cdhone1.setName("故障状态");
					  cdhone1.setLx(2);
					  cdhone1.setLxlen(1);
					  cdhone1.setValue(1); 
					  cdall.add(cdhone1);
					  //获取故障地址数据
					  List<CdhData> falseChddata=getfalseChdData(data,dev); 
					  cdall.addAll(falseChddata);
					  System.out.println("发送变流器状态变化数据：设备发生故障，发送故障数据......");
//					  插入故障数据记录数据，作为备份记录
					  insert_faultdata_jl(dataid,data,dev);
					  issend=true;
					  gzztmap.put(dev.getDevice_id(), 0);
			  }
				
			// 并离状态
			int modbus_type = dev.getD_type();
			// 并网风机 条件（20.2==1 & 20.4==1）
			// 协议解析
			List<String> bwList = null;
			if (modbus_type != 2) {
				//解析
				bwList = new ParseModBusTcp().parseModBusTcp(20, Integer.parseInt(data.split(",")[20]), modbus_type,
						data);
			} else {
				bwList = new ParseModBusTcp().parseModBusTcp(12822, Integer.parseInt(data.split(",")[12822 - 12788]),
						modbus_type, data);
			}
			String bwStr = bwList.get(1);
			String bwStrVal1 = "";
			String bwStrVal2 = "";
			if (bwStr.split("[|]").length > 0 && bwStr.contains("|")) {
				if (modbus_type != 2) {
					bwStrVal1 = bwStr.split("[|]")[2].split(",")[2];
					bwStrVal2 = bwStr.split("[|]")[4].split(",")[2];
				} else {
					bwStrVal1 = bwStr.split("[|]")[3].split(",")[2];
					bwStrVal2 = bwStr.split("[|]")[4].split(",")[2];
				}

				if ("1".equals(bwStrVal1) && "1".equals(bwStrVal2)) {
					// 并网状态,1 表示离网，0表示并网
					// 当前并网，之前的状态为离网。
					if (bwztmap.get(dev.getDevice_id()) == null || bwztmap.get(dev.getDevice_id()) == 1) {
						CdhData cdh_bw = new CdhData();
						cdh_bw.setCdid(28701);
						cdh_bw.setName("并网状态");
						cdh_bw.setLx(2);
						cdh_bw.setLxlen(1);
						cdh_bw.setValue(0);
						cdall.add(cdh_bw);
						bwztmap.put(dev.getDevice_id(), 0);
						//获取并网时间
						List<CdhData> stateupdatetime=getStateipdatetime(dev, 0);
						cdall.addAll(stateupdatetime);
						System.out.println("发送变流器状态变化数据：离网转并网......");
						 issend=true;
					}
				} else {
					if (bwztmap.get(dev.getDevice_id()) == null) {
						CdhData cdh_bw = new CdhData();
						cdh_bw.setCdid(28701);
						cdh_bw.setName("离网状态");
						cdh_bw.setLx(2);
						cdh_bw.setLxlen(1);
						cdh_bw.setValue(1);
						cdall.add(cdh_bw);
						bwztmap.put(dev.getDevice_id(), 1);
						//获取离网时间
						List<CdhData> stateupdatetime=getStateipdatetime(dev, 1);
						cdall.addAll(stateupdatetime);
						System.out.println("发送变流器状态变化数据：并网转离网......");
						 issend=true;
					} else {
						// 当前离网，之前的状态为并网。
						if (bwztmap.get(dev.getDevice_id()) == 0) {
							CdhData cdh_bw = new CdhData();
							cdh_bw.setCdid(28701);
							cdh_bw.setName("并网状态");
							cdh_bw.setLx(2);
							cdh_bw.setLxlen(1);
							cdh_bw.setValue(1);
							cdall.add(cdh_bw);
							bwztmap.put(dev.getDevice_id(), 1);
							//获取离网时间
							List<CdhData> stateupdatetime=getStateipdatetime(dev, 1);
							cdall.addAll(stateupdatetime);
							System.out.println("发送变流器状态变化数据：并网转离网......");
							 issend=true;
						}
					}
				}
			}
			//通讯状态
			//1表示故障，0表示正常
			if(txztmap.get(dev.getDevice_id())==null||txztmap.get(dev.getDevice_id())==1){
				System.out.println("发送变流器状态变化数据：变流器与系统通讯正常......");
				CdhData cdhone=new CdhData();
				  cdhone.setCdid(28700);
				  cdhone.setName("通信状态:正常");
				  cdhone.setLx(2);
				  cdhone.setLxlen(1);
				  cdhone.setValue(0);
				  cdall.add(cdhone);
				  txztmap.put(dev.getDevice_id(), 0);
				  issend=true;
			}
		}else{
			//没有采集数据
			//1表示故障，0表示正常
			if(txztmap.get(dev.getDevice_id())==null||txztmap.get(dev.getDevice_id())==0){
				System.out.println("发送变流器状态变化数据：变流器与系统通讯故障......");
				CdhData cdhone=new CdhData();
				  cdhone.setCdid(28700);
				  cdhone.setName("通信状态:故障");
				  cdhone.setLx(2);
				  cdhone.setLxlen(1);
				  cdhone.setValue(1);
				  cdall.add(cdhone);
				  txztmap.put(dev.getDevice_id(), 1);
				  issend=true;
			}
	  }
		//无论什么状态的发送都需要加入设备的基本信息
		if(issend==true){
			//设备基本信息
			List<CdhData> cdalltmp = getDeviceBase(dev);
			cdall.addAll(cdalltmp);	
		}
	
	return cdall;	
}
/**
 * 获取并网/离网时间
 * @param dev
 * @param bwstate
 * @return
 */
private static List<CdhData> getStateipdatetime(Device dev,int bwstate){
	//并网时间
	List<CdhData> cdall = new ArrayList<CdhData>();
	Date d = new Date();
	GregorianCalendar gc = new GregorianCalendar();
	gc.setTime(d);
	int YEAR=gc.get(GregorianCalendar.YEAR);
	int MONTH=gc.get(GregorianCalendar.MONTH)+1;
	int DATE=gc.get(GregorianCalendar.DATE);
	int HOUR=gc.get(GregorianCalendar.HOUR_OF_DAY);
	int MINUTE=gc.get(GregorianCalendar.MINUTE);
	int SECOND=gc.get(GregorianCalendar.SECOND);
	if(bwstate==0){
		CdhData cdh_bwtime_year = new CdhData();
		cdh_bwtime_year.setCdid(28582);
		cdh_bwtime_year.setLx(9);
		cdh_bwtime_year.setLxlen(4);
		cdh_bwtime_year.setValue(YEAR);
		cdall.add(cdh_bwtime_year);
		
		
		CdhData cdh_bwtime_MONTH = new CdhData();
		cdh_bwtime_MONTH.setCdid(28583);
		cdh_bwtime_MONTH.setLx(9);
		cdh_bwtime_MONTH.setLxlen(4);
		cdh_bwtime_MONTH.setValue(MONTH);
		cdall.add(cdh_bwtime_MONTH);
		
		CdhData cdh_bwtime_DATE = new CdhData();
		cdh_bwtime_DATE.setCdid(28584);
		cdh_bwtime_DATE.setLx(9);
		cdh_bwtime_DATE.setLxlen(4);
		cdh_bwtime_DATE.setValue(DATE);
		cdall.add(cdh_bwtime_DATE);
		
		CdhData cdh_bwtime_HOUR = new CdhData();
		cdh_bwtime_HOUR.setCdid(28585);
		cdh_bwtime_HOUR.setLx(9);
		cdh_bwtime_HOUR.setLxlen(4);
		cdh_bwtime_HOUR.setValue(HOUR);
		cdall.add(cdh_bwtime_HOUR);
		
		CdhData cdh_bwtime_MINUTE = new CdhData();
		cdh_bwtime_MINUTE.setCdid(28586);
		cdh_bwtime_MINUTE.setLx(9);
		cdh_bwtime_MINUTE.setLxlen(4);
		cdh_bwtime_MINUTE.setValue(MINUTE);
		cdall.add(cdh_bwtime_MINUTE);
		
		CdhData cdh_bwtime_SECOND = new CdhData();
		cdh_bwtime_SECOND.setCdid(28587);
		cdh_bwtime_SECOND.setLx(9);
		cdh_bwtime_SECOND.setLxlen(4);
		cdh_bwtime_SECOND.setValue(SECOND);
		cdall.add(cdh_bwtime_SECOND);
	}else{
		//离网状态默认为1
		CdhData cdh_lwbwtime_year = new CdhData();
		cdh_lwbwtime_year.setCdid(28588);
		cdh_lwbwtime_year.setLx(9);
		cdh_lwbwtime_year.setLxlen(4);
		cdh_lwbwtime_year.setValue(YEAR);
		cdall.add(cdh_lwbwtime_year);
		
		
		CdhData cdh_lwbwtime_MONTH = new CdhData();
		cdh_lwbwtime_MONTH.setCdid(28589);
		cdh_lwbwtime_MONTH.setLx(9);
		cdh_lwbwtime_MONTH.setLxlen(4);
		cdh_lwbwtime_MONTH.setValue(MONTH);
		cdall.add(cdh_lwbwtime_MONTH);
		
		CdhData cdh_lwtime_DATE = new CdhData();
		cdh_lwtime_DATE.setCdid(28590);
		cdh_lwtime_DATE.setLx(9);
		cdh_lwtime_DATE.setLxlen(4);
		cdh_lwtime_DATE.setValue(DATE);
		cdall.add(cdh_lwtime_DATE);
		
		CdhData cdh_lwtime_HOUR = new CdhData();
		cdh_lwtime_HOUR.setCdid(28591);
		cdh_lwtime_HOUR.setLx(9);
		cdh_lwtime_HOUR.setLxlen(4);
		cdh_lwtime_HOUR.setValue(HOUR);
		cdall.add(cdh_lwtime_HOUR);
		
		CdhData cdh_lwtime_MINUTE = new CdhData();
		cdh_lwtime_MINUTE.setCdid(28592);
		cdh_lwtime_MINUTE.setLx(9);
		cdh_lwtime_MINUTE.setLxlen(4);
		cdh_lwtime_MINUTE.setValue(MINUTE);
		cdall.add(cdh_lwtime_MINUTE);
		
		CdhData cdh_lwtime_SECOND = new CdhData();
		cdh_lwtime_SECOND.setCdid(28593);
		cdh_lwtime_SECOND.setLx(9);
		cdh_lwtime_SECOND.setLxlen(4);
		cdh_lwtime_SECOND.setValue(SECOND);
		cdall.add(cdh_lwtime_SECOND);
	}
	return cdall;
}
/**
 * 设备基本信息
 * @param dev
 * @return
 */
private static List<CdhData> getDeviceBase(Device dev) {
	List<CdhData> cdall = new ArrayList<CdhData>();
	try {
		JDBConnection jdbc_trd = new JDBConnection();
		Connection conn_trd = jdbc_trd.connection;
		ResultSet rs_trd = null;
		Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql = "SELECT  t.*,t1.len FROM windpower_db.mqtt_cddata_information t,mqtt_datatype t1 WHERE  t.datalx=t1.type_id   AND addr='base' ";
		rs_trd = st_trd.executeQuery(sql);
		while (rs_trd.next()) {
			CdhData cdone=new CdhData();
			cdone.setCdid(rs_trd.getInt("cdid"));
			cdone.setLx(rs_trd.getInt("datalx"));
			cdone.setLxlen(rs_trd.getInt("len"));
			int chd=cdone.getCdid();
			if(chd==28500){
				//设备类型编码
				cdone.setName("设备类型编码");
				cdone.setValue(dev.getDevice_lx_mh());
			}else if(chd==28503){
				cdone.setName("设备ID");
				cdone.setValue(dev.getDevice_id());
			}else if(chd==28507){
				//设备型号
				cdone.setName("设备型号");
				cdone.setStrvalue(dev.getDevice_xh());
			}else if(chd==28504){
				cdone.setName("设备SN");
				cdone.setStrvalue(dev.getSn());
			}else if(chd==28505){
				//设备名称
				cdone.setName("设备名称");
				cdone.setStrvalue(dev.getName());
			}else if(chd==28506){
				//厂家
				cdone.setName("设备厂家");
				cdone.setStrvalue(dev.getDevice_CJ());
			}
			cdall.add(cdone);
		}
		st_trd.close();
		conn_trd.close();
		
	
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return cdall;
}

	/**
	 * 获取运行数据
	 * 
	 * @return
	 */
	private static List<CdhData> getRundata(Device dev) {
		List<CdhData> cdall = new ArrayList<CdhData>();
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select * from windpower_db.windpower_dataacq_tab  where device_id=" + dev.getDevice_id()
					+ " order  by  id desc  limit 0,1 ";
			rs_trd = st_trd.executeQuery(sql);
			String create_time = "";
			String data = "";
			int id=0;
			while (rs_trd.next()) {
				id=rs_trd.getInt("id");
				data = rs_trd.getString("data");
				create_time = rs_trd.getString("create_time");
			}
			st_trd.close();
			conn_trd.close();
			if(create_time!=""){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date day = new Date();
				long endtime = day.getTime();
				Date date1 = df.parse(create_time);
				long starttime = date1.getTime();
				long send = (endtime - starttime) / (1000 * 60);
				// 数据在10分钟中只能为最新数据。
				if (send <= 10) {
					//运行周期不发生运行故障
					cdall = getnewChdData(data, dev);//获取测试点
					insert_rundata_jl(id,data,dev);
					//加入设备基本信息
					List<CdhData> cdalltmp = getDeviceBase(dev);
					cdall.addAll(cdalltmp);	
				}
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cdall;
	}
/**
 * 插入运行数据记录数据，作为备份记录
 * @param dev 
 * @param data 
 * @param id 
 */
	private static void insert_rundata_jl(int id, String data, Device dev) {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql_tmp = "insert into mqtt_run_data_jl(id,device_id,data)values(" + id + "," + dev.getDevice_id()
					+ ",'" + data + "')";
			st_trd.execute(sql_tmp);
			System.out.println("设备" + dev.getDevice_id() + "运行数据备份入库......");
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/**
 * 插入故障数据记录数据，作为备份记录
 * @param dev 
 * @param data 
 * @param id 
 */
	private static void insert_faultdata_jl(int id, String data, Device dev) {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql_tmp = "insert into mqtt_fault_data_jl(id,device_id,data)values(" + id + "," + dev.getDevice_id()
					+ ",'" + data + "')";
			st_trd.execute(sql_tmp);
			System.out.println("设备" + dev.getDevice_id() + "故障时间备份入库......");
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void InsertHistory_jl(int id, String data, String time,Device dev) {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql_tmp = "insert into mqtt_history_data_jl(id,device_id,data,update_time)values(" + id + "," + dev.getDevice_id()
					+ ",'" + data + "','"+time+"')";
			st_trd.execute(sql_tmp);
			System.out.println("设备" + dev.getDevice_id() + "历史库数据备份入库......");
			String sqlome="delete from mqtt_dataacq_history  where id="+id;
			st_trd.execute(sqlome);
			System.out.println("删除历史数据.....id为："+id);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
/**
 * 获取测试点
 * @param data
 * @param dev
 * @return
 */
private static List<CdhData> getnewChdData(String data,Device dev) {
	 List<CdhData> cdall=new ArrayList<CdhData>();
	try {
		String[] datastr=data.split(",");
		JDBConnection jdbc_trd = new JDBConnection();
		Connection conn_trd = jdbc_trd.connection;
		ResultSet rs_trd=null;
		Statement st_trd  = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql = "select  t.*,t1.len from windpower_db.mqtt_cddata t,mqtt_datatype t1 where t.modbus_type="+dev.getD_type() +"   and t.datalx=t1.type_id  and  addr  is not null and addr!='' order  by  t.id asc  ";
		rs_trd=st_trd.executeQuery(sql);
		while(rs_trd.next()){
			CdhData cdhone=new CdhData();
			  cdhone.setCdid(rs_trd.getInt("cdid"));
			  cdhone.setName(rs_trd.getString("name"));
			  cdhone.setLx(rs_trd.getInt("datalx"));
			  cdhone.setLxlen(rs_trd.getInt("len"));
			  cdhone.setAddr(rs_trd.getString("addr"));
			  cdhone.setModbus_type(rs_trd.getInt("modbus_type"));
			  //海上风电
			  String addrtmp=cdhone.getAddr();
			  if(cdhone.getModbus_type()==2){
				  if(cdhone.getAddr().split("/").length>1){
					  int value_h=Integer.parseInt(datastr[Integer.parseInt(cdhone.getAddr().split("/")[0])-12788]);
					  int value_l=Integer.parseInt(datastr[Integer.parseInt(cdhone.getAddr().split("/")[1])-12788]);
					  short[] arraytmp=new short[2];
					  arraytmp[0]=(short)value_h;
					  arraytmp[1]=(short)value_l;
					  int unshort= unshortToInt(arraytmp,0);
					  cdhone.setValue(unshort);
				  }else{
					  float valuetmp=0;
					  String[] addrone = addrtmp.split("[.]");
						if (addrone.length > 1) {
							int addr = Integer.parseInt(addrone[0]);
							if (cdhone.getModbus_type() == 2) {
								addr = addr - 12788;
							}
							int bit = Integer.parseInt(addrone[1]);
							int addrvallue = Integer.parseInt(datastr[addr]);
							valuetmp = getbitvalue(bit, addrvallue);
							cdhone.setValue(valuetmp);
						} else {
							valuetmp = Float.parseFloat(datastr[Integer.parseInt(addrtmp)-12788]);
							cdhone.setValue(valuetmp);
						}
				 }
			  }else{
				  if(cdhone.getAddr().split("/").length>1){
					  int value_h=Integer.parseInt(datastr[Integer.parseInt(cdhone.getAddr().split("/")[0])]);
					  int value_l=Integer.parseInt(datastr[Integer.parseInt(cdhone.getAddr().split("/")[1])]);
					  short[] arraytmp=new short[2];
					  arraytmp[0]=(short)value_h;
					  arraytmp[1]=(short)value_l;
					  int unshort= unshortToInt(arraytmp,0);
					  cdhone.setValue(unshort);
				  }else{
					  float valuetmp=0;
					  String[] addrone = addrtmp.split("[.]");
						if (addrone.length > 1) {
							int addr = Integer.parseInt(addrone[0]);
							int bit = Integer.parseInt(addrone[1]);
							int addrvallue = Integer.parseInt(datastr[addr]);
							valuetmp = getbitvalue(bit, addrvallue);
							cdhone.setValue(valuetmp);
						} else {
							//String ss="18.5";
//							valuetmp = Float.parseFloat(ss);
							valuetmp = Float.parseFloat(datastr[Integer.parseInt(addrtmp)]);
							cdhone.setValue(valuetmp);
						}
				  }
			  }
			  cdall.add(cdhone);
		}
		st_trd.close();
		conn_trd.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return  cdall;
	}

/**
 * 获取故障地址数据
 * @param data
 * @param dev
 * @return
 */
	private static List<CdhData> getfalseChdData(String data, Device dev) {
		List<CdhData> cdall = new ArrayList<CdhData>();
		try {
			String[] datastr = data.split(",");
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  t.*,t1.len from windpower_db.mqtt_cddata_fault t,mqtt_datatype t1 where t.modbus_type="
					+ dev.getD_type()
					+ "   and t.datalx=t1.type_id  and  addr  is not null and addr!='' order  by  t.id desc  ";
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				CdhData cdhone = new CdhData();
				cdhone.setCdid(rs_trd.getInt("cdid"));
				cdhone.setName(rs_trd.getString("name"));
				cdhone.setLx(rs_trd.getInt("datalx"));
				cdhone.setLxlen(rs_trd.getInt("len"));
				cdhone.setAddr(rs_trd.getString("addr"));
				cdhone.setModbus_type(rs_trd.getInt("modbus_type"));
				int valuetmp = 0;
				String addrtmp = cdhone.getAddr();
				String[] addrone = addrtmp.split("[.]");
				if (addrone.length > 1) {
					int addr = Integer.parseInt(addrone[0]);
					if (cdhone.getModbus_type() == 2) {
						addr = addr - 12788;
					}
					int bit = Integer.parseInt(addrone[1]);
					int addrvallue = Integer.parseInt(datastr[addr]);
					valuetmp = getbitvalue(bit, addrvallue);
					cdhone.setValue(valuetmp);
				} else {
					int addr=Integer.parseInt(addrtmp);
					if (cdhone.getModbus_type() == 2) {
						addr = addr - 12788;
					}
					int addrvallue = Integer.parseInt(datastr[addr]);
					valuetmp = getbitvalue(0, addrvallue);
					cdhone.setValue(valuetmp);
				}
				// 出故障大于0的地址才需要发送
				if (valuetmp > 0) {
					cdall.add(cdhone);
				}

			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cdall;
	}

/**
 * bit位数组
 * @param bitlist
 * @param shortvalue
 * @return
 */
public static int getbitvalue(int bit, int shortvalue) {
	int bitvalue=0;
	short sv=(short)shortvalue;
	String bstr = Integer.toBinaryString(sv);
	String bitstr = "";
	int lgth=bstr.length();
	if(lgth>16){
		bstr=bstr.substring(bstr.length()-16, bstr.length());
		lgth=bstr.length();
	}
	for(int i=bstr.length();i<16;i++){
		bstr="0"+bstr;
	}
	String btmp=bstr.charAt(15-bit)+"";
	bitvalue=Integer.parseInt(btmp);

	/*//数值的进制长度，没有bit值大。
	int startle=16-lgth;
		if (startle <= bit && bit < 16) {
			int bittmp = bit - startle;
			String btmp=bstr.charAt(bittmp)+"";
			bitvalue=Integer.parseInt(btmp);
		} else {
			bitvalue = 0;
		}*/
	return bitvalue;

}
	/**
	 * 补0操作
	 * @param namelen
	 * @return
	 */
	private static String getbit10(String namelen) {
		String hx = "";
		if (namelen.length() > 10) {
			hx = namelen.substring(namelen.length() - 10, namelen.length());
		} else {
			for (int i = namelen.length(); i < 10; i++) {
				hx += "0";
			}
			hx += namelen;

		}
		;
		return hx;
	}

	public static String get4HexString(String HexString) {
		String hx = "";
		if (HexString.length() < 2) {
			hx = "00,0" + HexString;
		} else if (HexString.length() < 3) {
			hx = "00," + HexString;
		} else if (HexString.length() < 4) {
			hx = "0" + HexString.substring(0, 1) + "," + HexString.substring(1, 3);
			;
		} else {
			hx = HexString.substring(HexString.length()-4, HexString.length()-2) + "," + HexString.substring(HexString.length()-2, HexString.length()-0);
			//hx = HexString.substring(0, 2) + "," + HexString.substring(2, 4);
		}
		return hx;
	}
	public static String getFloat_4HexString(String HexString) {
		String hx = "";
		if(HexString.equals("0")){
			hx ="00,00,00,00";
		}else{
		  hx = HexString.substring(6, 8) + "," + HexString.substring(4, 6)+"," + HexString.substring(2, 4)+"," + HexString.substring(0, 2);
		}
		return hx;
	}
	
	public static String get2HexString(String HexString) {
		String hx = "";
        if (HexString.length() < 2) {
			hx = "0" + HexString;
		} else {
			hx = HexString;
		}
		return hx;
	}

	public static int unshortToInt(short[] unshort, int off) {
		int b0 = unshort[off] & 0xFFFF;
		int b1 = unshort[off + 1] & 0xFFFF;
		return (b1 << 16) | b0;
	}

	/**
	 * 字符串转成16进制
	 * 
	 * @param s
	 * @return
	 */
	public static String[] toHexString(String s) {
		String str[] = new String[s.length()];
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);//字符转int整型
			String s4 = Integer.toHexString(ch);
			str[i] = s4;
		}
		return str;
	}

	/**
	 * 字符数组转换成字符串
	 * 
	 * @param ss
	 * @return
	 */
	public static String sztostr(String[] ss) {
		String strs = "";
		for (int i = 0; i < ss.length; i++) {
			if (i == ss.length - 1) {
				strs += ss[i];
			} else {
				strs += ss[i] + " ";
			}
		}
		return strs;
	}

	/**
	 * 获取MQTT服务地址
	 * @return
	 */
	public static Socket getMqttServerAddr() {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  * from  mqtt_server_addr limit 0,1";
			rs_trd = st_trd.executeQuery(sql);
			String ip="";
			int port=0;
			while (rs_trd.next()) {
				ip = rs_trd.getString("service_ip");//MQTT服务地址
				port = rs_trd.getInt("service_port");//MQTT服务端口
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			if(ip!=null&&port!=0){
				try {
					Socket sk=new Socket(ip,port);//连接MQTT服务
					return sk;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
	}
/**
 * 获取主题信息
 * @param zt
 * @return
 */
	public static String[] getZT(String zt) {
		String[] ztstrall=null;
		String ztid="0";
		if(zt.equals("DY")){
			ztid="6,2";
		}else if(zt.equals("CSSD")){
			ztid="8";
		}else if(zt.equals("YXSJ")){
			ztid="3";
		}else if(zt.equals("HistorySJ")){
			ztid="5";
		}else if(zt.equals("FaultSJ")){
			ztid="4";
		}else if(zt.equals("DeviceBaseSJ")){
			ztid="3";
		}
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  * from  mqtt_zttype where id in("+ztid+") order by  id desc ";
			rs_trd = st_trd.executeQuery(sql);
			String ztstr="";
			while (rs_trd.next()) {
				ztstr += rs_trd.getString("ztstr")+",";
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			//订阅主体为合并主题SNA140506987/Time，PC/Password
			//82 24 00 FF 00 11基本指令
			if(zt.equals("DY")){
				//订阅主题设置为01
				String[] basecode={"82","24","00","FF","00","11"};
				String svn="SN"+getSystemServer("sn")+ztstr.split(",")[0];
				String svn2=ztstr.split(",")[1];
				String[] svnstr=toHexString(svn);
				String[] svn2str=toHexString(svn2);
				
				//第一个主题长度
				int ztonelen=svnstr.length;
				String ztonehex=Integer.toHexString(ztonelen);
				basecode[5]=get2HexString(ztonehex);
				
				//第二个主题长度
				int zttwoonelen=svn2str.length;
				String zttwohex=Integer.toHexString(zttwoonelen);
				
				//6个基本指令，3个符号位，1个结尾
				ztstrall=new String[svnstr.length+svn2str.length+3+1+6];
				System.arraycopy(basecode, 0, ztstrall, 0, basecode.length);
				
                System.arraycopy(svnstr, 0, ztstrall,basecode.length, svnstr.length);
                ztstrall[svnstr.length+basecode.length]="01";//第一个主题请求级别，只有主题基本为1，2是才会有主题消息标准ID
                ztstrall[svnstr.length+basecode.length+1]="00";
                ztstrall[svnstr.length+basecode.length+2]=get2HexString(zttwohex);//第二个主题长度
                
                System.arraycopy(svn2str, 0, ztstrall, svnstr.length+3+basecode.length, svn2str.length);
                ztstrall[svnstr.length+svn2str.length+3+basecode.length]="01";//第二个主题请求级别
                  //固定报头剩余长度设置
                String dybt=Integer.toHexString(ztstrall.length-1);
                ztstrall[1]=get2HexString(dybt);
                
			}else if(zt.equals("CSSD")){
				String[] zt1str=null;
				String[] startbasecode={"32" ,"7F", "00" ,"0E"};
				String svn=ztstr.split(",")[0];
				zt1str=toHexString(svn);
				String[] endbasecode={ "00" ,"03"};
				ztstrall=new String[startbasecode.length+zt1str.length+endbasecode.length];
				System.arraycopy(startbasecode, 0, ztstrall, 0, startbasecode.length);
				System.arraycopy(zt1str, 0, ztstrall, startbasecode.length, zt1str.length);
				System.arraycopy(endbasecode, 0, ztstrall, startbasecode.length+zt1str.length, endbasecode.length);
				//设置主题长度
				String zthex=Integer.toHexString(zt1str.length);
				ztstrall[3]=get2HexString(zthex);
				
			}else if(zt.equals("YXSJ")){
				String[] zt1str=null;
				String[] startbasecode={"32","AF" ,"00", "0E"};
				String svn=ztstr.split(",")[0];
				zt1str=toHexString(svn);
				//主题编号
				String[] endbasecode={ "00" ,"04"};
				ztstrall=new String[startbasecode.length+zt1str.length+endbasecode.length];
				System.arraycopy(startbasecode, 0, ztstrall, 0, startbasecode.length);
				System.arraycopy(zt1str, 0, ztstrall, startbasecode.length, zt1str.length);
				System.arraycopy(endbasecode, 0, ztstrall, startbasecode.length+zt1str.length, endbasecode.length);
				//设置主题长度
				String zthex=Integer.toHexString(zt1str.length);
				ztstrall[3]=get2HexString(zthex);
			  }
			else if(zt.equals("HistorySJ")){
				String[] zt1str=null;
				String[] startbasecode={"32","AF" ,"00", "0E"};
				String svn=ztstr.split(",")[0];
				zt1str=toHexString(svn);
				//主题编号
				String[] endbasecode={ "00" ,"05"};
				ztstrall=new String[startbasecode.length+zt1str.length+endbasecode.length];
				System.arraycopy(startbasecode, 0, ztstrall, 0, startbasecode.length);
				System.arraycopy(zt1str, 0, ztstrall, startbasecode.length, zt1str.length);
				System.arraycopy(endbasecode, 0, ztstrall, startbasecode.length+zt1str.length, endbasecode.length);
				//设置主题长度
				String zthex=Integer.toHexString(zt1str.length);
				ztstrall[3]=get2HexString(zthex);
			  }else if(zt.equals("FaultSJ")){
					String[] zt1str=null;
					String[] startbasecode={"32","AF" ,"00", "0E"};
					String svn=ztstr.split(",")[0];
					zt1str=toHexString(svn);
					//主题编号
					String[] endbasecode={ "00" ,"06"};
					ztstrall=new String[startbasecode.length+zt1str.length+endbasecode.length];
					System.arraycopy(startbasecode, 0, ztstrall, 0, startbasecode.length);
					System.arraycopy(zt1str, 0, ztstrall, startbasecode.length, zt1str.length);
					System.arraycopy(endbasecode, 0, ztstrall, startbasecode.length+zt1str.length, endbasecode.length);
					//设置主题长度
					String zthex=Integer.toHexString(zt1str.length);
					ztstrall[3]=get2HexString(zthex);
				  }
			  else if(zt.equals("DeviceBaseSJ")){
					String[] zt1str=null;
					String[] startbasecode={"32","AF" ,"00", "0E"};
					String svn=ztstr.split(",")[0];
					zt1str=toHexString(svn);
					//主题编号
					String[] endbasecode={ "00" ,"07"};
					ztstrall=new String[startbasecode.length+zt1str.length+endbasecode.length];
					System.arraycopy(startbasecode, 0, ztstrall, 0, startbasecode.length);
					System.arraycopy(zt1str, 0, ztstrall, startbasecode.length, zt1str.length);
					System.arraycopy(endbasecode, 0, ztstrall, startbasecode.length+zt1str.length, endbasecode.length);
					//设置主题长度
					String zthex=Integer.toHexString(zt1str.length);
					ztstrall[3]=get2HexString(zthex);
				  }
			return ztstrall;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	
/**
 * 获取公钥，私钥
 * @param key
 * @return
 */
	public static String getKey(String key) {
		String keystr="";
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  * from  mqtt_key limit 0,1 ";
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				keystr = rs_trd.getString(key);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			return keystr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return keystr;
		}
	}
	/**
	 * 获取采集点得数据类型
	 * @param id
	 * @return
	 */
	public static int getCdhsjlx(int  id) {
		int sjlxstr=0;
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  * from  mqtt_device_sx_cddata where device_sx_chd ="+id;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				sjlxstr = rs_trd.getInt("sjlx");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			return sjlxstr;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return sjlxstr;
	}
}
	
	/**
	 * 获取系统服务信息
	 * @param str
	 * @return
	 */
	public static String getSystemServer(String  str) {
			String sjlxstr="";
			try {
				JDBConnection jdbc_trd = new JDBConnection();
				Connection conn_trd = jdbc_trd.connection;
				ResultSet rs_trd = null;
				Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "select  * from  mqtt_system_server ";
				rs_trd = st_trd.executeQuery(sql);
				while (rs_trd.next()) {
					sjlxstr = rs_trd.getString(str);//获取数据库mqtt_system_server中sn的内容
				}
				rs_trd.close();
				st_trd.close();
				conn_trd.close();
				return sjlxstr;
			} catch (SQLException e) {
				e.printStackTrace();
				return sjlxstr;
			}
		
	}

	/**
	 * 获取变流器列表信息
	 */
    public static List<Device> startDataAcq() {
    	    List<Device> newdevicelist=new ArrayList<Device>();
			int device_id = 0;
			String name = "";
		     String sn="";
		     int  lx=0;
		     int d_type=0;
			try {
				 JDBConnection jdbc_trd = new JDBConnection();
					Connection conn_trd = jdbc_trd.connection;
					ResultSet rs_trd=null;
					Statement st_trd  = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "select 	t.*,t1.modbus_type,t2.errorstate from windpower_db.mqtt_device t  ,windpower_devicetype t1,windpower_device t2  where t2.device_type_id=t1.id  and  t.run_state=0 and t2.id=t.device_id and t2.run_state=0  ";
				rs_trd = st_trd.executeQuery(sql);
				while (rs_trd.next()) {
					Device dev = new Device();
					device_id = rs_trd.getInt("device_id");
					name = rs_trd.getString("name");
					sn = rs_trd.getString("sn");
					lx=rs_trd.getInt("device_yd_lx");
					d_type=rs_trd.getInt("modbus_type");
					dev.setDevice_id(device_id);
					dev.setName(name);
					dev.setSn(sn);
					dev.setLx(lx);
					dev.setD_type(d_type);
					dev.setDevice_lx_mh(rs_trd.getInt("device_lx_MH"));
					dev.setDevice_xh(rs_trd.getString("device_xh"));
					dev.setDevice_CJ(rs_trd.getString("device_CJ"));
					dev.setErrorstate(rs_trd.getString("errorstate"));
					newdevicelist.add(dev);
				}
				rs_trd.close();
				st_trd.close();
				conn_trd.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return newdevicelist;
		}
	/**
	 * 插入设备历史数据
	 * @param dev
	 */
    public static void insertHistorydata(Device dev) {
		try {
			 JDBConnection jdbc_trd = new JDBConnection();
				Connection conn_trd = jdbc_trd.connection;
				ResultSet rs_trd=null;
				Statement st_trd  = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "select * from windpower_db.windpower_dataacq_tab  where device_id="+dev.getDevice_id() +" order  by  id desc  limit 0,1 ";
				rs_trd=st_trd.executeQuery(sql);
				String create_time="";
				String data="";
				int did=0;
				int id=0;
				while(rs_trd.next()){
					 data=rs_trd.getString("data");
					 create_time=rs_trd.getString("create_time");
					 did=rs_trd.getInt("device_id");
					 id=rs_trd.getInt("id");
				}
				if(create_time!=""){
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			     	Date day = new Date();
			     	long endtime= day.getTime();
			     	Date date1=df.parse(create_time);
			     	long starttime=date1.getTime();
			     	long send= (endtime-starttime)/(1000*60);
			     	//数据在10分钟中只能为最新数据。
			     	if(send<=10){
						String sql_tmp = "insert into mqtt_dataacq_history(id,device_id,data)values("+id+","+did+",'"+data+"')";
						st_trd.execute(sql_tmp);
						System.out.println("设备："+dev.getDevice_id()+"数据插入历史库......");
			     	}else{
			     		System.out.println("设备："+dev.getDevice_id()+"没有最新数据入库，不插入历史库......");
			     	}
				}else{
					System.out.println("设备"+dev.getDevice_id()+"没有最新数据入库，不插入历史库......");
				}
				
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
   /**
    * 获取设备采集信息 
    * @param dev 设备信息
    * @return
    */
	@SuppressWarnings("null")
	public Data_Acq getDevice_data(Device dev) {
		Data_Acq dataObj = new Data_Acq();
		dataObj.setDatastr(null);
		dataObj.setId(0);
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql ="";
			if(dev.getErrorstate().equals("OK")){
				sql = "select t1.create_time,t.*,t1.data,t1.id dataid,t2.errorstate from mqtt_device t ,  windpower_dataacq_tab t1 ,windpower_device t2  where   t.device_id=t1.device_id  and t2.id=t.device_id  and  t.device_id="+dev.getDevice_id()+"  and   t1.create_time >=CURRENT_TIMESTAMP - INTERVAL 30  second  and t2.errorstate='OK'  order  by  t1.id desc  limit 0,1  ";
			}else{
				sql = "select t1.create_time,t.*,t1.data,t1.id dataid,t2.errorstate from mqtt_device t ,  windpower_dataacq_tab t1,windpower_device t2  where   t.device_id=t1.device_id  and t2.id=t.device_id  and  t.device_id="+dev.getDevice_id()+"  and t2.errorstate='ERROR'  order  by  t1.id desc  limit 0,1  ";
			}
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				dataObj.setDatastr(rs_trd.getString("data"));
				dataObj.setId(rs_trd.getInt("dataid"));
			}
			st_trd.close();
			conn_trd.close();
			return dataObj;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 更新私钥
	 * @param mykey
	 */
		public void insertkey(String mykey) {
			try {
				JDBConnection jdbc_trd = new JDBConnection();
				Connection conn_trd = jdbc_trd.connection;
				Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String sql = "update  mqtt_key set private_key='"+mykey+"'";
				st_trd.execute(sql);
				st_trd.close();
				conn_trd.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}

