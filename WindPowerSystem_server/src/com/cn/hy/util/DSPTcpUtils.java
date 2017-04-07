package com.cn.hy.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;


/**
 * 
 * @author wuhw
 *
 */
public class DSPTcpUtils {
	private static final Log LOGGER = LogFactory.getLog(DSPTcpUtils.class);

	private static Socket socket;
	
	private static java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

	public static void main(String[] args) {
//		String[] waveformCodes = new String[34];
//		for(int i = 0; i < 34; i++){
//			if(i < 10){
//				waveformCodes[i] = "0" + i;
//			}else{
//				waveformCodes[i] = String.valueOf(i);
//			}
//		}

		String[] waveformCodes = new String[2];
		waveformCodes[0] = "00";
		waveformCodes[1] = "02";
     //		DSPTcpUtils.getData("10.128.5.200", 503, 66, "A9", "00", waveformCodes);
	List<Map<String, String>> list = DSPTcpUtils.getFaultWaveformDataList("192.168.68.237", 503, 66, waveformCodes);
	    for(int i=0;i<list.size();i++){
	    	System.out.println(list.get(i).values());
	    }
	}

	/**
	 * 获取DSP数据
	 * 
	 * @param ip
	 *            IP地址
	 * @param port
	 *            端口
	 * @param cmdType 指令类型
	 * @param length
	 *            接收数据长度
	 * @param waveformCodes
	 *            波形集合
	 * @return
	 */
	public static void getData(String ip, int port, int length, String cmdType, String cmdCode, String[] waveformCodes) {
		DSPTcpUtils.connect(ip, port);
		DSPTcpUtils.sendCmd(cmdType, cmdCode, waveformCodes);
		DSPTcpUtils.deleteHisData(ip, port, cmdType);
		if("A8".equals(cmdType)){
			DSPTcpUtils.getRecData(ip, port, length, cmdType, waveformCodes);
		}
		if("A9".equals(cmdType)){
			DSPTcpUtils.getDefaultRecData(ip, port, length, cmdType, waveformCodes);
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
	public static void connect(String ip, int port) {
		LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），正在链接，请等待......！");
		try {
			socket = new Socket(ip, port);
			LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），链接成功！");
		} catch (Exception e) {
			LOGGER.info("变流器DSP，IP（" + ip + "）端口（" + port + "），链接失败！");
			e.printStackTrace();
		}
	}

	/**
	 * 发送指令
	 * @param cmdType 指令类型（A8：示波器；A9：故障录波）
	 * @param cmdCode 指令编号
	 * @param waveformCodes
	 *            波形编号集合
	 */
	public static void sendCmd(String cmdType, String cmdCode, String[] waveformCodes) {
		byte cmd[] = new byte[waveformCodes.length + 2];
		cmd[0] = Integer.valueOf(cmdType, 16).byteValue();
		cmd[1] = Integer.valueOf(cmdCode, 16).byteValue();

		for (int i = 0; i < waveformCodes.length; i++) {
			cmd[i + 2] = Integer.valueOf(waveformCodes[i], 16).byteValue();
		}

		OutputStream out;
		try {
			out = socket.getOutputStream();
			// 发送指令
			out.write(cmd);
			LOGGER.info("发送指令成功！");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("发送指令失败！");
		}
	}

	/**
	 * 存储DSP回传数据（示波器）
	 * @param ip DSP-IP地址
	 * @param port DSP-端口
	 * @param length 数据长度
	 * @param cmdType 指令类型（A8：示波器；A9：故障录波）
	 * @param waveformCodes 波形集合
	 */
	public static void getRecData(String ip, int port, int length, String cmdType, String[] waveformCodes) {
		try {
			InputStream in = socket.getInputStream();
			byte[] recData = new byte[length];

			StringBuffer recDataString = new StringBuffer();
			String sql = "insert into windpower_deviceinfo_dsp_record(ip, port, type, cmd, value, now_time) values (?, ?, ?, ?, ?, ?)";// SQL语句

			Date nowDate = null;
			boolean isGo = true;
			while (1 > 0) {
				if(!DSPTcpUtils.getIsSync(ip, port)){
					break;
				}
				if (!isGo) {
					break;
				}
				nowDate = new Date();
				recData = new byte[length];
				in.read(recData);
				String shortstrone = getshortstr(recData);
				String[] values = shortstrone.split(",");

				int cInd = 0;
				int arrInd = 0;
				String[] datas = new String[4];

				for (int i = 0; i < values.length; i++) {
					if (i == 0 && recData[i] != -88 && recData[i] != -87) {
						isGo = false;
						LOGGER.info("数据错误");
						break;
					}

					/**
					 * psd板子反馈结果拼接
					 */
					if (cInd == 0) {
						recDataString.append(values[i]);
					} else {
						recDataString.append(",").append(values[i]);
					}
					cInd++;

					/**
					 * 8个字符为一组数据结果集
					 */
					if (cInd == 8) {
						datas[arrInd] = recDataString.toString();
						arrInd++;
						cInd = 0;
						recDataString.delete(0, recDataString.length());
					}
				}

				/**
				 * 保存记录
				 */
				DBUtils db1 = new DBUtils();// 创建DBHelper对象
				try {
					for (String res : datas) {
						db1.runSql(sql);
						db1.pst.setString(1, ip);
						db1.pst.setInt(2, port);
						db1.pst.setString(3, cmdType);
						db1.pst.setString(4, DSPTcpUtils.objsToStr(waveformCodes));
						db1.pst.setString(5, res);
						db1.pst.setString(6, format.format(nowDate));
						int result = db1.pst.executeUpdate();
						if (result == 0) {
							LOGGER.info("记录存储出现错误：IP：" + ip + "；port：" + port + "；value：" + res + "；");
						}
					}
					db1.close();// 关闭连接
				} catch (SQLException e) {
					e.printStackTrace();
					db1.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送指令
	 * 
	 * @param waveformCodes
	 *            波形编号集合
	 */
	public static void sendFaultCmd(String[] waveformCodes, String faultCode) {
		byte cmd[] = new byte[waveformCodes.length + 2];
		cmd[0] = Integer.valueOf("A9", 16).byteValue();
		cmd[1] = Integer.valueOf(faultCode, 16).byteValue();

		for (int i = 0; i < waveformCodes.length; i++) {
			cmd[i + 2] = Integer.valueOf(waveformCodes[i], 16).byteValue();
		}

		OutputStream out;
		try {
			out = socket.getOutputStream();
			// 发送指令
			out.write(cmd);
			LOGGER.info("发送指令成功！");
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.info("发送指令失败！");
		}
	}
	
	/**
	 * 存储DSP回传数据（故障录波）
	 * @param ip DSP-IP地址
	 * @param port DSP-端口
	 * @param length 数据长度
	 * @param cmdType 指令类型（A8：示波器；A9：故障录波）
	 * @param waveformCodes 波形集合
	 */
	public static void getDefaultRecData(String ip, int port, int length, String cmdType, String[] waveformCodes) {
		try {
			InputStream in = socket.getInputStream();
			byte[] recData = new byte[length];

			StringBuffer recDataString = new StringBuffer();
			String sql = "insert into windpower_deviceinfo_dsp_record(ip, port, type, cmd, value, now_time) values (?, ?, ?, ?, ?, ?)";// SQL语句

			Date nowDate = null;
			while (1 > 0) {
				socket.setSoTimeout(2000);
				nowDate = new Date();
				recData = new byte[length];
				in.read(recData);
				String shortstrone = getshortstr(recData);
				String[] values = shortstrone.split(",");

				for (int i = 0; i < values.length; i++) {
					if (i == 0 && recData[i] != -88 && recData[i] != -87) {
						LOGGER.info("数据错误");
						break;
					}

					/**
					 * psd板子反馈结果拼接
					 */
					if (i == 0) {
						recDataString.append(values[i]);
					} else {
						recDataString.append(",").append(values[i]);
					}
				}

				/**
				 * 保存记录
				 */
				DBUtils db1 = new DBUtils();// 创建DBHelper对象
				db1.runSql(sql);
				db1.pst.setString(1, ip);
				db1.pst.setInt(2, port);
				db1.pst.setString(3, cmdType);
				db1.pst.setString(4, DSPTcpUtils.objsToStr(waveformCodes));
				db1.pst.setString(5, recDataString.toString());
				db1.pst.setString(6, format.format(nowDate));
				int result = db1.pst.executeUpdate();
				if (result == 0) {
					LOGGER.info("记录存储出现错误：IP：" + ip + "；port：" + port + "；value：" + recDataString + "；");
				}
				db1.close();// 关闭连接
				
				recDataString.delete(0, recDataString.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取故障录波数据列表
	 * @param ip DSPIP地址
	 * @param port DSP端口
	 * @param length 数据长度 故障录波默认长度66，为一条数据
	 * @param waveformCodes 波形编号集合
	 * @return
	 */
	public static List<Map<String, String>> getFaultWaveformDataList(String ip, int port, int length, String[] waveformCodes){
		DSPTcpUtils.connect(ip, port);
		DSPTcpUtils.sendCmd("A9", "00", waveformCodes);
		return DSPTcpUtils.readFaultWaveformData(ip, port, length, waveformCodes);
	}
	
	/**
	 * 获取故障录波数据列表
	 * @param ip DSPIP地址
	 * @param port DSP端口
	 * @param length 数据长度
	 * @param waveformCodes 波形编号集合
	 * @return
	 */
	public static List<Map<String, String>> readFaultWaveformData(String ip, int port, int length, String[] waveformCodes){
		List<Map<String, String>> resultDatas = new ArrayList<Map<String, String>>(); 
		try {
			InputStream in = socket.getInputStream();
			byte[] recData = new byte[length];
			StringBuffer recDataString = new StringBuffer();
			
			Map<String, String> data = null;
			Date nowDate = null;
			while(0<1){
				socket.setSoTimeout(2000);
				nowDate = new Date();
				recData = new byte[length];
				in.read(recData);
				String shortstrone = getshortstr(recData);
				String[] values = shortstrone.split(",");
				
				if(shortstrone.length() <= 31){
					return resultDatas;
				}
				
				for (int i = 0; i < values.length; i++) {
					if (i == 0 && recData[i] != -88 && recData[i] != -87) {
						LOGGER.info("数据错误");
						break;
					}

					/**
					 * psd板子反馈结果拼接
					 */
					if (i == 0) {
						recDataString.append(values[i]);
					} else {
						recDataString.append(",").append(values[i]);
					}
				}
				data = new HashMap<String, String>();
				data.put("nowTime", format.format(nowDate));
				data.put("value", recDataString.toString());
				resultDatas.add(data);
				
				System.out.println(resultDatas.size() +"==="+ format.format(nowDate) +"::::"+recDataString);
				recDataString.delete(0, recDataString.length());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return resultDatas;
		}
	}

	/**
	 * 关闭socket
	 */
	public static void closeConnect() {
		try {
			socket.close();
		} catch (IOException e) {
			LOGGER.info("断开连接失败！");
			e.printStackTrace();
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
	 * 数组转字符串方法
	 * 
	 * @param obj
	 * @return
	 */
	public static String objsToStr(Object[] objs) {
		StringBuffer result = new StringBuffer();
		int i = 0;
		for (Object obj : objs) {
			if (i > 0) {
				result.append(",").append(obj);
			} else {
				result.append(obj);
			}
			i++;
		}
		return result.toString();
	}
	
	/**
	 * 根据变流器ID修改DSP板子是否同步
	 * @param deviceId 变流器ID
	 * @param isSync 是否同步，0：不同步；1：同步
	 */
	public static void updateDeviceSyncv(int deviceId, String isSync){
		String sql = "update windpower_deviceinfo set is_sync = ? WHERE device_id = ? AND d_type = 'dsp'";
		DBUtils db1 = new DBUtils();
		db1.runSql(sql);
		try {
			db1.pst.setString(1, isSync);
			db1.pst.setInt(2, deviceId);
			db1.pst.executeUpdate();
			db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断当前DSP是否需要同步数据
	 * @param ip IP地址
	 * @param port 端口
	 * @return
	 */
	public static boolean getIsSync(String ip, int port){
		String sql = "select is_sync from windpower_deviceinfo where ip = ? and port = ?";
		DBUtils db1 = new DBUtils();
		db1.runSql(sql);
		
		boolean res = false;
    	try {
			db1.pst.setString(1, ip);
			db1.pst.setInt(2, port);
			ResultSet rs = db1.pst.executeQuery();
        	while (rs.next()) {
        		if(!StringUtils.isEmpty(rs.getString(1)) && "1".equals(rs.getString(1))){
        			res =  true;
        		}
        	}
        	rs.close();
        	db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	/**
	 * 删除历史数据
	 * @param ip IP地址
	 * @param port 端口
	 */
	public static void deleteHisData(String ip, int port, String cmdType){
		String sql = "delete from windpower_deviceinfo_dsp_record where ip = ? and port = ? and type = ?";
		DBUtils db1 = new DBUtils();
		db1.runSql(sql);
		try {
			db1.pst.setString(1, ip);
			db1.pst.setInt(2, port);
			db1.pst.setString(3, cmdType);
			db1.pst.executeUpdate();
			db1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
}
