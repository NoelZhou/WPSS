package com.cn.hy.controller.windFieldPreview;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import com.cn.hy.bean.JDBConnection;
import com.cn.hy.service.windFieldPreview.OscilloscopeService;

/**
 * 示波器WebSocket
 * @author 李皓
 *
 */
@ServerEndpoint("/oscilloscopeWebSocket")
public class OscilloscopeWebSocket {
	
	private static final Log LOGGER = LogFactory.getLog(OscilloscopeWebSocket.class);
	
	@Resource
	private OscilloscopeService oscilloscopeService;

	private static Socket socketA;
	private static Socket socketB;
	
	private static InputStream inA;
	private static InputStream inB;
	
	/**
	 * 指令编号
	 */
	private static String cmdCode = "00";
	
	/**
	 * DSP返回数据长度
	 */
	private static int resDataLength = 66;
	
	/**
	 * 设备ID
	 */
	private static String deviceId;
	
	/**
	 * DSP信息
	 */
	private static Map<String, Object> dspMap;
	
	/**
	 * 模式，A8：示波器；A9：故障录波
	 */
	private static String modelType;
	
	/**
	 * 波形，用于故障录波，1：网侧；2：机侧
	 */
	private static String waveformType;
	
	/**
	 * 录波编号
	 */
	private static String defaultWaveformCode;
	
	/**
	 * 采样频率
	 */
	@SuppressWarnings("unused")
	private static double samplingFrequency;
	
	/**
	 * 返回数据集数量，DSP：= 采样频率 * 8 * 30
	 */
	private static int returnsDataNum;
	
	/**
	 * DSP1波形编号集合
	 */
	private static String[] dsp1MoveformCodes;
	
	/**
	 * DSP1波形编号集合
	 */
	private static String[] dsp2MoveformCodes;
	
	/**
	 * 是否发送数据到前台
	 * 0：发送；9：不发送
	 */
	private static String isSend;
	
	/**
	 * 显示百分比
	 */
	private int silderNum;
	
	/**
	 * dsp1大数组
	 */
	private  Map<Integer,String> dspDataA;
	
	/**
	 * dsp2大数组
	 */
	private  Map<Integer,String>  dspDataB;
	/**
	 * 采集对象
	 */
	private boolean wcbx=false;
	private boolean jcbx=false;
	private static  Map<Integer,String[]>  dspDataAcq= new Hashtable<Integer, String[]>();
	private int ct=0;
	private static int acqid=0;
	private int act=0;
	private int bct=0;
	private boolean isjs=true;
	private  int xfct=0;
	 
	 static boolean acqrun=false;
	 static int acqct=0;
	
	/**
	 * 上次集合长度
	 */
	@SuppressWarnings("unused")
	private int lastListsize = 0;
	
	/**
	 * 连续增长次数，连续增持10次，下发数据增加+1
	 */
	@SuppressWarnings("unused")
	private int addIndex = 0;
		
	@OnMessage
	public void onMessage(String message, Session session) throws IOException, InterruptedException {
		String[] params = message.split("#");
		initParams(params,session);
		if(isSend.equals("8")){
			return;
		}
		this.onClose();
		
		isSend = "0";
		dspDataA = new Hashtable<Integer, String>();
		dspDataB = new Hashtable<Integer, String>();
		//
		ct = 0;
		act = 0;
		bct = 0;
		xfct=0;
		acqid=0;
		isjs=false;
		if ("A8".equals(modelType)) {
			dsp1MoveformCodes = params[3].split(",");
			dsp2MoveformCodes = params[4].split(",");
			for(int i=0;i<dsp1MoveformCodes.length;i++){
				if(!dsp1MoveformCodes[i].equals("00")){
					wcbx=true;
					break;
				};
			}
			for(int i=0;i<dsp2MoveformCodes.length;i++){
				if(!dsp2MoveformCodes[i].equals("00")){
					jcbx=true;
					break;
				};
			}
			this.getDspDatas(session);
			
		} else if ("A9".equals(modelType)) {
			waveformType = params[3];
			defaultWaveformCode = params[4];
			this.getDspDefaultDatas(session);
		
		}else{
			acqrun=false;
			acqct=0;
		}
		
	}
	
	/**
	 * 获取Dsp数据（故障录波）
	 * @param session
	 */
	private void getDspDefaultDatas(Session session){
		StringBuffer sendDatas = new StringBuffer();
		try {
			this.connectSocket();
			int incode=Integer.parseInt(defaultWaveformCode);
			String[] hexstr={"00",incode+""};
			OscilloscopeWebSocket.sendCmd(socketA, hexstr);
			
			inA = socketA.getInputStream();
			byte[] recData;
			String recStrA = null;
			while(0<1){
				socketA.setSoTimeout(2000);
				recData = new byte[resDataLength];
				inA.read(recData);
		/*		String bytestr="";
				//String hexstr="";
				for(int i=0;i<recData.length;i++){
					bytestr+=recData[i]+" ";
					//hexstr+=Integer.toHexString(recData[i])+" ";
					
				}*/
			//	LOGGER.info("未合并故障录波数据接收："+bytestr);
				recStrA = getshortstr(recData);
				sendDatas.append(recStrA).append("=");
				//LOGGER.info("故障录波数据接收："+recStrA);
			}
		} catch (Exception e) {
			
			try {
				 if(socketA==null){
					   session.getBasicRemote().sendText("DSP连接异常！");
				   }
			e.printStackTrace();
			String ee=e.getMessage();
		   if(ee.equals("Read timed out")){
				if(sendDatas.length() > 0){
					session.getBasicRemote().sendText(sendDatas.substring(0, sendDatas.length() - 1));
					System.out.println(sendDatas.substring(0, sendDatas.length() - 1).split("=").length);
				}
		   }else{
				session.getBasicRemote().sendText("DSP连接异常！");
			}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			this.onClose();
		}
		//LOGGER.info("故障录波数据接收："+sendDatas);
	}

	/**
	 * 获取DSP数据
	 * @param session
	 */
	private void getDspDatas(Session session) {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
		for (int i=0; i<3; i++) {
			final int index = i;
			final Session ses = session;
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					if(index == 0){
						readData(ses);
					}
					if(index == 1){
						System.out.println("开启下发数据线程......");
						sendData(ses);;
					}
					if(index == 2){
						System.out.println("开启计数线程......");
						isjs=true;
					    //getnumct();
					}
				}
			});
		}
	}
	private static int sjct=0;
	private static boolean issj=false;
	private static void getsjct() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
    	System.out.println("开始DSP采集时间计时进程："+issj);
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					while(issj==true){try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					sjct++;
					if(issj==false){
						System.out.println("结束DSP采集数据计时进程");
					}
				}}
					
			});
		}
	
	
	
	/**
	 * 
	 */
	/**
	 * 读取DSP数据
	 */
	public void readData(Session session){
		try {
			this.connectSocket();
			
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
			for (int i=0; i<2; i++) {
				final int index = i;
				final Session ses = session;
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						if(index == 0){
							if(wcbx==true){
								System.out.println("开启A数据读取线程......");
								readData1(ses);	
							}
							
						}
						
						if(index == 1){
							if(jcbx==true){
								System.out.println("开启B数据读取线程......");
								readData2(ses);	
							}
							
							
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void readData1(Session session){

		try {
			if(isSend.equals("9")){
				return;
			}
			OscilloscopeWebSocket.sendCmd(socketA, dsp1MoveformCodes);
			
			inA = socketA.getInputStream();
			
			byte[] recDataA;
			String recStrA;
			int lastValue = 0;
			String sstra="";
			while(true){
				socketA.setSoTimeout(1000);
				
				recDataA = new byte[resDataLength];
				inA.read(recDataA);
				
				recStrA = getshortstr(recDataA);
				
				if(recStrA.equals(sstra)){
					System.out.println("相同数据A"+recStrA);
				}
				sstra=recStrA;
				String[] res1 = OscilloscopeWebSocket.getDataArray(recStrA);
				for(String res : res1){
					String[] recs = res.split(",");
					int nowValue = Integer.parseInt(recs[2]);
					if(lastValue != 0 && Math.abs(lastValue - nowValue) > 50){
					}
					lastValue = Integer.parseInt(recs[2]);
				}
				
				
				
				sstra=recStrA;
				dspDataA.put(act,recStrA);
				act++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSend = "9";
			try {
				session.getBasicRemote().sendText("DSP连接异常！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	
	}
	
	public void readData2(Session session){
		try {
			if(isSend.equals("9")){
				return;
			}
			OscilloscopeWebSocket.sendCmd(socketB, dsp2MoveformCodes);
			
			inB = socketB.getInputStream();
			byte[] recDataB;
			String recStrB;
			int lastValue = 0;
			String sstra="";
			while(true){
				socketB.setSoTimeout(1000);
				recDataB = new byte[resDataLength];
				inB.read(recDataB);
				recStrB = getshortstr(recDataB);
				if(recStrB.equals(sstra)){
					//System.out.println("相同数据B"+recStrB);
				}
				sstra=recStrB;
				String[] res1 = OscilloscopeWebSocket.getDataArray(recStrB);
				for(String res : res1){
					String[] recs = res.split(",");
					int nowValue = Integer.parseInt(recs[2]);
					if(lastValue != 0 && Math.abs(lastValue - nowValue) > 50){
						//System.out.println("B掉包数据：上一个值："+lastValue+"下一个值："+nowValue);
					}
					lastValue = Integer.parseInt(recs[2]);
				}
				dspDataB.put(bct, recStrB);
				bct++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSend = "9";
			try {
				session.getBasicRemote().sendText("DSP连接异常！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	
	/**
	 * 向前前台发送数据
	 */
	public void sendData(Session session){
		try {
			String[] recAllArr;
			String[] recAllArrnew;
			StringBuffer sendDatas = new StringBuffer();
			
			int lastValue = 0;
			int dataNum = 0;
			
			while(true){

				if(isSend.equals("9")){
					return;
				}
				String Ast="";
				String Bst="";
				//都为0时，么有点击任何波形
				if(wcbx==true&&jcbx==true){
					if(dspDataA.size() == 0||dspDataB.size() == 0){
						continue;
					}
				}else if(wcbx==true&&jcbx==false){
					if(dspDataA.size() == 0){
						continue;
					}
				}else if(wcbx==false&&jcbx==true){
					if(dspDataB.size() == 0){
						continue;
					}
				}
				
				if(wcbx==true){
					Ast=dspDataA.get(ct);
				}
				if(jcbx==true){
					Bst=dspDataB.get(ct);
				}
				if(StringUtils.isEmpty(Ast)&&StringUtils.isEmpty(Bst)){
					dspDataA.remove(ct);
					dspDataB.remove(ct);
					continue;
				}
				   recAllArr = OscilloscopeWebSocket.getAllDataArray(Ast, Bst);	
				
				//开启DSP采集模式
				if(acqrun==true){
					recAllArrnew=getAllDataArraynew(Ast,Bst);
					dspDataAcq.put(acqid, recAllArrnew);	
				}
				
				for(String rec : recAllArr){
					dataNum++;
					sendDatas.append(rec).append("=");
					if(!StringUtils.isEmpty(rec)){
						String[] tems = rec.split(",");
						int tem = Integer.parseInt(tems[2]);
						if(lastValue != 0 && Math.abs(lastValue - tem) > 50){
							//System.out.println(sendDatas);
							//System.out.println("上一个值："+lastValue+"下一个值："+tem);
						}
						lastValue = tem;
					}
				}

				if(dataNum >= returnsDataNum){
					xfct++;
					if(wcbx==true&&jcbx==false&&dspDataA.size()>100){
						returnsDataNum += 100;
					}else if(wcbx==false&&jcbx==true&&dspDataB.size()>100){
						returnsDataNum += 100;
					}else{
					if(dspDataA.size()>100&&dspDataB.size()>100){
						returnsDataNum += 100;
					  }
					}
			    	String substring = sendDatas.substring(0, sendDatas.length() - 1);
					session.getBasicRemote().sendText(substring);
			    	dataNum = 0;
			    	sendDatas.delete(0, sendDatas.length());
				}
				
				if(wcbx==true){
					String tem1 = dspDataA.get(ct);
					int a=0;
					
					while(true){
						dspDataA.remove(ct);
						if(dspDataA.size() == 0 || (dspDataA.size() > 0 && !tem1.equals(dspDataA.get(ct)))){
							break;
						}else{
							System.out.println("a次数"+a);
							a++;
						}
					}
						
				}
				if(jcbx==true){
					int b=0;
					String tem2 = dspDataB.get(ct);
					while(true){
						dspDataB.remove(ct);
						if(dspDataB.size() == 0 || (dspDataB.size() > 0 && !tem2.equals(dspDataB.get(ct)))){
							break;
						}else{
							System.out.println("b次数"+b);
							b++;
						}
					}
				
				}
				ct++;
			if(acqrun==true){
				acqid++;}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSend = "9";
			try {
				session.getBasicRemote().sendText("程序异常！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 初始化参数
	 * @param message
	 */
	private void initParams(String[] params,Session session) {
		deviceId = params[0];
		modelType = params[1];
		if(modelType.equals("A10")){
			modelType="A8";
		}
		samplingFrequency = Double.parseDouble(params[2]);
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();  
		oscilloscopeService = (OscilloscopeService)wac.getBean("oscilloscopeService");
		isSend = params[5];
		if ("9".equals(isSend)) {
			onClose();
		}
		silderNum = Integer.parseInt(params[6]);
		returnsDataNum = 500;
		if(silderNum > 4){
			returnsDataNum += silderNum * 15;
		}
		dspMap = oscilloscopeService.getDeviceInfoDspMap(deviceId);
	}

	@OnOpen
	public void onOpen() {
		onClose();
		System.out.println("Client connected");
	}

	/**
	 * 连接Socket
	 */
	private void connectSocket() {
		if("A8".equals(modelType)){
			if(wcbx==true){
				socketA = OscilloscopeWebSocket.connect(String.valueOf(dspMap.get("dsp1Ip")), (Integer)dspMap.get("dsp1Port"));
			}
			if(jcbx==true){
		    	socketB = OscilloscopeWebSocket.connect(String.valueOf(dspMap.get("dsp2Ip")), (Integer)dspMap.get("dsp2Port"));
			}
		}else if("A9".equals(modelType)){
			if("1".equals(waveformType)){
				socketA = OscilloscopeWebSocket.connect(String.valueOf(dspMap.get("dsp1Ip")), (Integer)dspMap.get("dsp1Port"));
			}else if("2".equals(waveformType)){
				socketA = OscilloscopeWebSocket.connect(String.valueOf(dspMap.get("dsp2Ip")), (Integer)dspMap.get("dsp2Port"));
			}
			
		}
	}

	@OnClose
	public void onClose() {
		System.out.println("Connection closed");
		dspDataA = new Hashtable<Integer, String>();
		dspDataB = new Hashtable<Integer, String>();

		ct = 0;
		act = 0;
		bct = 0;
		isjs=false;
		try {
			isSend = "9";
			if (inA != null) {
				inA.close();
			}
			if (inB != null) {
				inB.close();
			}
			if (socketA != null) {
				socketA.close();
			}
			if (socketB != null) {
				socketB.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 连接变流器（DSP）
	 * 
	 * @param ip
	 *            IP地址
	 * @param port
	 *            端口
	 * @return
	 */
	public static Socket connect(String ip, int port) {
		LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），正在链接，请等待......！");
		try {
			Socket socket = new Socket(ip, port);
			LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），链接成功！");
			return socket;
		} catch (Exception e) {
			LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），链接失败！");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 发送指令
	 * @param cmdType 指令类型（A8：示波器；A9：故障录波）
	 * @param cmdCode 指令编号
	 * @param waveformCodes
	 *            波形编号集合
	 */
	public static void sendCmd(Socket socket, String[] waveformCodes) {
		byte cmd[] = new byte[waveformCodes.length + 2];
		cmd[0] = Integer.valueOf(modelType, 16).byteValue();
		cmd[1] = Integer.valueOf(cmdCode, 16).byteValue();
		String codestr="";
		for (int i = 0; i < waveformCodes.length; i++) {
		    //	示波器的编号为十进制的，之前已经进行进制转换了
			cmd[i + 2] = Integer.valueOf(waveformCodes[i], 10).byteValue();
		//	cmd[i + 2] = Integer.valueOf(waveformCodes[i], 16).byteValue();
			codestr+=cmd[i+2]+",";
		}
		if(socket==null){
		System.out.println("socket对象为空");
		}else{
		System.out.println(socket.getInetAddress()+"DSP波形指令为（byte大小）："+codestr);
		}
		OutputStream out;
		try {
			out = socket.getOutputStream();
			// 发送指令
			out.write(cmd);
			LOGGER.info("xt发送指令成功！"+codestr);
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("发送指令失败！");
		}
	}
	
	/**
	 * 字节转换成字符
	 * @param newmodbusstr
	 * @return
	 */
	public static String getshortstr(byte[] newmodbusstr) {
		byte[] array = Arrays.copyOfRange(newmodbusstr, 2, newmodbusstr.length);
		int len = array.length / 2;
		short[] ArrayData = new short[len];
		byte[] arraytmp = new byte[2];
		for (short i = 0; i <= len - 1; i++) {
			arraytmp[0] = array[2 * i];
			arraytmp[1] = array[2 * i + 1];
			ArrayData[i] = byte4ToInt(arraytmp, 0);
		}
		String bString = "";
		for (int i = 0; i < ArrayData.length; i++) {
			short str = ArrayData[i];
			if (i == ArrayData.length - 1) {
				bString += str;
			} else {
				bString += str + ",";
			}
		}

		return bString;
	}
	
	/*
	 * 字符组成16进制的字符
	 * 
	 * @param HexString
	 * 
	 * @return
	 */
	public static String[] get4HexString(int it) {
		String HexString = Integer.toHexString(it);
		String hx = "";
		if (HexString.length() < 2) {
			hx = "00,0" + HexString;
		} else if (HexString.length() < 3) {
			hx = "00," + HexString;
		} else if (HexString.length() < 4) {
			hx = "0" + HexString.substring(0, 1) + "," + HexString.substring(1, 3);
			;
		} else {
			hx = HexString.substring(0, 2) + "," + HexString.substring(2, 4);
		}
		return hx.split(",");
	}
	
	/**
	 * //合并字节位换成一个16位的整形数,short类
	 * 
	 * @param bytes
	 * @param off
	 * @return
	 */
	public static short byte4ToInt(byte[] bytes, int off) {
		int b0 = bytes[off] & 0xFF;
		int b1 = bytes[off + 1] & 0xFF;
		short ii = (short) ((b0 << 8) | b1);
		return ii;
	}
	
	/**
	 * 字符串转换成接收数组（示波器-两个DSP-16个波形）
	 * @param str
	 * @return
	 */
	public static String[] getAllDataArray(String resStr1, String resStr2){
		String[] res1 = OscilloscopeWebSocket.getDataArray(resStr1);
		String[] res2 = OscilloscopeWebSocket.getDataArray(resStr2);
		String tmp="00,00,00,00,00,00,00,00";
		for(int i=0; i<4; i++){
            if(res1==null){
            	res2[i] =  tmp+"," + res2[i];
            }else if(res2==null){
            	res1[i] = res1[i] +"," +tmp;
            }else{
			   res1[i] = res1[i] + "," + res2[i];
		  }
		}
		if(res1==null){
			res1=res2;
		}
		return res1;
	}
	
	
	/**
	 * 字符串转换成接收数组（示波器-两个DSP-16个波形）
	 * @param str
	 * @return
	 */
	public static String[] getAllDataArraynew(String resStr1, String resStr2){
		//String[] tmp="00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00","00"};
		 String tmp="00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00,00";
		if(resStr1==null||resStr1.equals("")){
			resStr1=tmp;
		}
		if(resStr2==null||resStr2.equals("")){
			resStr2=tmp;
		}
		String[] res1 = OscilloscopeWebSocket.getDataArraynew(resStr1);
		String[] res2 = OscilloscopeWebSocket.getDataArraynew(resStr2);
		
		String[] newres=new String[res1.length+res2.length];
		System.arraycopy(res1, 0, newres, 0, res1.length);
		System.arraycopy(res2, 0, newres, res1.length, res2.length);
		return newres;
	}
	
	/**
	 * 字符串转换成接收数组（示波器-单个DSP-8个波形）
	 * @param str
	 * @return
	 */
	public static String[] getDataArraynew(String str){
		if(str.equals("")){
			return  null;
		}
		String[] allArrs = str.split(",");
		String[] resArrs = new String[8];
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < 8; i++){
			for(int j=0; j < 4; j++){
				sb.append(allArrs[j*8+i]).append(",");
			}
			resArrs[i] = sb.substring(0, sb.length()-1);
			sb.delete(0, sb.length());
		}
		return resArrs;
	}
	
	
	/**
	 * 字符串转换成接收数组（示波器-单个DSP-8个波形）
	 * @param str
	 * @return
	 */
	public static String[] getDataArray(String str){
		if(str.equals("")){
			return null;
		}
		String[] allArrs = str.split(",");
		String[] resArrs = new String[4];
		StringBuffer sb = new StringBuffer();
		for(int i=0; i < 4; i++){
			
			for(int j=0; j < 8; j++){
				sb.append(allArrs[i*8+j]).append(",");
			}
			resArrs[i] = sb.substring(0, sb.length()-1);
			sb.delete(0, sb.length());
		}
		return resArrs;
	}
	
	@SuppressWarnings("unused")
	private void getnumct() {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
	 
			fixedThreadPool.execute(new Runnable() {
				public void run() {
					while(isjs==true){try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("A接收大小："+act);
					System.out.println("B接收大小："+bct);
					System.out.println("A数组大小："+dspDataA.size());
					System.out.println("B数组大小："+dspDataB.size());
					System.out.println("组合数据长度："+ct);
					System.out.println("下发单位："+returnsDataNum);System.out.println("下发次数："+xfct);
					System.out.println("采集次数："+acqid);
					System.out.println("采集数组大小："+dspDataAcq.size());
					
				}}
					
			});
		}
	/**
	 * 开启采集DSP数据线程
	 * @param employeeId
	 */
	public static void StartDataAcq(final String employeeId,final String dspWaveformCodes,final String dspWaveformName) { 
		deleteDspDataAcq(employeeId);
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
				fixedThreadPool.execute(new Runnable() {
					public void run() {
						System.out.println("开启采集DSP数据线程.....");
						acqct=0;
						acqid=0;
						acqrun=true;
						sjct=0;
						 issj=true;
						  getsjct();
						while(acqrun==true){
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						if(sjct%10==0&&sjct!=0){
						//	System.out.println("开始插入时间库....."+sjct);
							//System.out.println("开始插入时间库数据大小....."+dspDataAcq.size());
							Map<Integer, String[]> dspDataAcqtmpallone=dspDataAcq;
							System.out.println("插入DPS采集数据10秒长度"+dspDataAcqtmpallone.size());
							dspDataAcq=new Hashtable<Integer, String[]>();
							acqid=0;
						    InsertDspDataAcq(employeeId,dspDataAcqtmpallone,dspWaveformCodes,dspWaveformName);
							dspDataAcqtmpallone=new Hashtable<Integer, String[]>();
							//System.out.println("结束插入数据....."+sjct);
						}
					}
						if(acqrun==false){
							System.out.println("停止运行DSP采集数据");
						}
						//点击接受的时候运行
					if(acqrun==false&&acqct!=1){
						System.out.println("结束DSP采集数据进程");
						acqrun=false;
						issj=false;
						Map<Integer, String[]> dspDataAcqtmpallone=dspDataAcq;
						System.out.println("插入DPS采集数据最后一次长度"+dspDataAcqtmpallone.size());
						dspDataAcq=new Hashtable<Integer, String[]>();
						acqid=0;
						//最后一秒的数据延迟插入
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						InsertDspDataAcq(employeeId,dspDataAcqtmpallone,dspWaveformCodes,dspWaveformName);
					;
					}	
				 }
				});
			
		
	}

	private static int xh = 0;

	/**
	 * 插入10内采集到的DSp组合数据
	 * 
	 * @param employeeId
	 * @param dspDataAcq
	 */
	private static void InsertDspDataAcq(String employeeId, Map<Integer, String[]> dspDataAcqtmpall,
			String dspWaveformCodes, String dspWaveformName) {
		Map<Integer, String> dspDataAcqbxname = new LinkedHashMap<Integer, String>();
		String dspcode = dspWaveformCodes;
		String[] dsp1 = dspcode.split(",");
		String[] dspname = dspWaveformName.split(",");
		// 获取波形名称
		for (int ii = 0; ii < dspname.length; ii++) {
			if (!dspname[ii].equals("")) {
				dspDataAcqbxname.put(ii, dspname[ii]);
			}
		}
		// 获取模式编号序号
		Map<Integer, String> dspDataAcqtm = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < dsp1.length; i++) {
			if (!dsp1[i].equals("00")) {
				dspDataAcqtm.put(i, "");
			}
			;
		}
		// 采集数据处理
		for (int i = 0; i < dspDataAcqtmpall.size(); i++) {
			String[] strtmp = dspDataAcqtmpall.get(i);
			if (strtmp != null) {
				Set<Integer> keyset = dspDataAcqtm.keySet();
				Iterator<Integer> it = keyset.iterator();
				while (it.hasNext()) {
					Object okey = it.next();
					Integer is = (Integer) okey;
					String s = (String) dspDataAcqtm.get(okey);
					s += strtmp[is] + ",";
					dspDataAcqtm.put(is, s);
				}
			}
		}
		try {
			/*
			 * if(dspDataAcqtmp.equals("")||dspDataAcqtmp.equals(null)||
			 * dspDataAcqtmp.size()==0){ return; }
			 */
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			// 遍历整合后的数组，入库
			/*
			 * Set set1 = dspDataAcqtmp.keySet(); for (Object obj : set1) {
			 * String sql =
			 * "insert into  windpower_data_dspacq   (employeeid,datastr,data_type,xh)value('"
			 * +employeeId+"','"+dspDataAcqtmp.get(obj)+"','"+obj+"',"+xh+")";
			 * st_trd.execute(sql); }
			 */

			// 遍历方法一：遍历哈希表中的键

			// 利用循环遍历出key和value
			Iterator<Integer> itr = dspDataAcqbxname.keySet().iterator();
			while (itr.hasNext()) {
				int str = (Integer) itr.next();
				String valuestr = (String) dspDataAcqtm.get(str);
				String name = (String) dspDataAcqbxname.get(str);
				if (valuestr.equals("") || valuestr == null) {
					return;
				}
				String sql = "insert into  windpower_data_dspacq   (employeeid,datastr,data_type,xh)value('"
						+ employeeId + "','" + valuestr + "','" + name + "'," + xh + ")";
				st_trd.execute(sql);
				// System.out.println(name+" "+valuestr);
			}
			/*
			 * for(int acqi=0;acqi<dspDataAcqbxname.size();acqi++){ String
			 * valuestr = (String) dspDataAcqtm.get(acqi); String name =
			 * (String) dspDataAcqbxname.get(acqi);
			 * if(valuestr.equals("")||valuestr==null){ return; }
			 * System.out.println(name+"  "+valuestr); String sql =
			 * "insert into  windpower_data_dspacq   (employeeid,datastr,data_type,xh)value('"
			 * +employeeId+"','"+valuestr+"','"+name+"',"+xh+")";
			 * st_trd.execute(sql); }
			 */
			xh++;
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteDspDataAcq(String employeeId) {
		try {
			JDBConnection jdbc_trd = new JDBConnection();
			Connection conn_trd = jdbc_trd.connection;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "delete from  windpower_data_dspacq   where employeeid='" + employeeId+"'" ;
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
