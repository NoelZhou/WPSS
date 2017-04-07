package com.zhta.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhta.bean.JDBConnection;
import com.zhta.bean.ParseModBusTcp;
import com.zhta.bean.SunModbusTcpbyIp;
import com.zhta.historydata.CreateExcel;
import com.zhta.historydata.CreateExcelByHashMap;
import com.zhta.historydata.HistoryDataAcqAction;
import com.zhta.pojo.AcqData;
import com.zhta.pojo.Device;
import com.zhta.pojo.Errordataobj;
import com.zhta.pojo.ModbusBit;
import com.zhta.util.mail.SimpleMailSender;

public class DataAcqAction {
	private static final Log LOGGER = LogFactory.getLog(DataAcqAction.class);
	// 查询实时入库时间
	private static int acq_real_time;
	public static String rw_role_req = "";
	public static String rw_role_res = "";

	/**
	 * 获取采集频率
	 */
	public static int getacq_real_time() {
		try {

			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  real_time  from  windpower_dataacqtime ";
			ResultSet rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				acq_real_time = rs_trd.getInt("real_time");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			return acq_real_time;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return acq_real_time;
	}

	static boolean errorstate = false;

	/**
	 * 开始设备数据实时采集
	 * 
	 * @param dev
	 */
	public static void startthreadother(Device dev) {
		// 读取数据中设备的可读写状态
		getrw_role(dev.getDevice_id());
		// 写指令关闭
		if ((rw_role_req.equals("R") && rw_role_res.equals("OK"))
				|| (rw_role_req.equals("SJJS") && rw_role_res.equals("OK"))) {
			errorstate = getmodbustcpstrbyip(dev.getDevice_id(), dev.getIp(), dev.getPort(), dev.getD_type(),
					dev.getStartaddr(), dev.getLength(), dev.getModbuslength(), errorstate);
		} else if (rw_role_req.equals("SJ") && rw_role_res.equals("OK")) {
			System.out.println(dev.getDevice_id() + "-" + dev.getIp() + "固件正在升级中,无法采集.....");
		} else {
			System.out.println(dev.getDevice_id() + "-" + dev.getIp() + "有写指令，等待写指令结束.....");

		}
	}

	/**
	 * 初始化采集设备的读写权限
	 * 
	 * @param device_id
	 */
	public static void updatere_roleall() {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			String sql = "update   windpower_device  set rw_role_req='R' , rw_role_res='OK' ";
			PreparedStatement ps = conn_trd.prepareStatement(sql);
			ps.execute();

			ps.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 更新设备的最后一次采集时间，判断是删除还是通讯故障导致无法采集
	 * 
	 * @param device_id
	 */
	public static void updatedevicestate(int devid, String acqstate) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "update   windpower_device  set last_acqtime=NOW() , acq_state='" + acqstate + "' where id="
					+ devid;
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 查询设备的读权限，判断是否可以采集
	 * 
	 * @param totcal_device_id
	 */
	public static void getrw_role(int totcal_device_id) {

		try {
			Connection conn_trd = JDBConnection.getConnect();
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select  * from   windpower_device " + " where   id=" + totcal_device_id;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				rw_role_req = rs_trd.getString("rw_role_req");
				rw_role_res = rs_trd.getString("rw_role_res");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static Map<Integer, String> wc_jc_map = new Hashtable<Integer, String>();
	// 出故障标志，停止更新wc_jc_map对象，保持出现故障时改对象为上次的WC_JC 采集值,用于判断是否进行DSP录波管理插入
	static Map<Integer, Boolean> errordevicestrmap = new Hashtable<Integer, Boolean>();
	static Map<Integer, String> errordatastrmap = new Hashtable<Integer, String>();
	static Map<Integer, Integer> errortimemap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> dsperrortimemap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> allidmap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> gzztidmap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> bwztidmap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> gzztmap = new Hashtable<Integer, Integer>();
	static Map<Integer, Integer> bwztmap = new Hashtable<Integer, Integer>();
	static Map<Integer, Boolean> errorstatemap = new Hashtable<Integer, Boolean>();
	static Map<Integer, Boolean> runstatemap = new Hashtable<Integer, Boolean>();
	static Map<Integer, Boolean> timestatemap = new Hashtable<Integer, Boolean>();
	static Map<Integer, Boolean> deviceSatrtstatemap = new Hashtable<Integer, Boolean>();

	protected static boolean getmodbustcpstrbyip(int device_id, String ip, int port, int d_type, int startaddr,
			int length, int modbuslength, boolean iserror) {

		// 将实时库30分钟外的数据移到历史库中、复制数据到历史库,删除实时库数据
		HistoryDataAcqAction.copynowacqdata(device_id);

		boolean errorstate = iserror;
		ParseModBusTcp parsetcp = new ParseModBusTcp();
		SunModbusTcpbyIp smi = new SunModbusTcpbyIp();
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String modbustcpstr = smi.ReadSunModbusTcpStrAll(ip, port, d_type, startaddr, length, modbuslength);

			// 第一次进来，恢复设备正常状态
			if ((deviceSatrtstatemap.get(device_id) == null) || deviceSatrtstatemap.get(device_id) == true) {
				updatedeviceerrorstate(device_id, "OK");
				deviceSatrtstatemap.put(device_id, false);
			}

			if (modbustcpstr == "" || modbustcpstr == null || modbustcpstr == "-1" || modbustcpstr.equals("connect")
					|| modbustcpstr.equals("sendcodeerror")) {
				System.out.println("与设备通讯失败ip" + ip + "错误信息：" + modbustcpstr);
				// LOGGER.info("与设备通讯失败ip"+ip+"错误信息："+modbustcpstr);
				// 插入错误信息
				updatedevicestate(device_id, modbustcpstr);
			} else {
				int tcplenght = modbustcpstr.split(",").length;
				// 返回的字长度和协议中长度是一直的，插入数据库，不一致不处理改数据， 当采集海上风电是，中间某个指令中断导致数据没有采集完成
				if ((tcplenght == modbuslength && d_type == 2) || (tcplenght == (modbuslength - 5) && d_type != 2)) {
					String sql = "insert into windpower_dataacq_tab (device_id,data)value( " + device_id + ",'"
							+ modbustcpstr + "')";
					st_trd.execute(sql);
					int value38 = Integer.parseInt(modbustcpstr.split(",")[38]);
					if (d_type == 2) {
						value38 = Integer.parseInt(modbustcpstr.split(",")[12836 - 12788]);
					} else {
						value38 = Integer.parseInt(modbustcpstr.split(",")[38]);
					}
					System.out.println(device_id + "=" + ip + "中38位的值位" + value38 + "   errorstate值位"+ errorstatemap.get(device_id));
					if ((value38 != 0 && errorstatemap.get(device_id) == null) || (value38 != 0 && errorstatemap.get(device_id) == false)) {
						errortimemap.put(device_id, 0);
						dsperrortimemap.put(device_id, 0);
						errorstatemap.put(device_id, true);
						errordevicestrmap.put(device_id, true);
						String idsql = "SELECT @@IDENTITY id";
						rs_trd = st_trd.executeQuery(idsql);
						int id = 0;
						while (rs_trd.next()) {
							id = rs_trd.getInt("id");
						}
						allidmap.put(device_id, id);
						LOGGER.info("发现故障==============================" + device_id + "=" + ip + "=="+ allidmap.get(device_id) + "故障时刻的数据：" + modbustcpstr);
					} else if (value38 != 0 && errorstatemap.get(device_id) == true) {
						// 排除重复插入故障数据
						errorstatemap.put(device_id, true);
					} else {
						// 复位 如果出现故障后立马复位，该故障无法捕捉到，3秒延迟故障插入，要求。
						errorstatemap.put(device_id, false);
						errortimemap.put(device_id, -1);
						dsperrortimemap.put(device_id, -1);
					}
					// 延迟2秒后插入 详情错误数据
					/*
					 * if(device_id==16){
					 * LOGGER.info("事件记录标志=============="+device_id+"="+ip+"=="+
					 * errortimemap.get(device_id)); }
					 */
					if (errortimemap.get(device_id) != null && errortimemap.get(device_id) == 10) {
						// System.out.println("开始插入事件记录=============="+device_id+"="+ip+"=="+allidmap.get(device_id));
						LOGGER.info("开始插入事件记录==============" + device_id + "=" + ip + "==" + allidmap.get(device_id)
								+ "5秒后的故障数据" + errordatastrmap.get(device_id));
						// 标志设备出现故障，停止30秒网站故障的判断
						updatedeviceerrorstate(device_id, "ERROR");
						inserterrordevice(device_id, allidmap.get(device_id), errordatastrmap.get(device_id));
						errortimemap.put(device_id, -1);
						allidmap.put(device_id, -1);
						// errordatastrmap.put(device_id, modbustcpstr);
						// 标志设备故障结束，恢复止30秒网站故障的判断
						updatedeviceerrorstate(device_id, "OK");
						LOGGER.info("结束插入事件记录==============" + device_id + "=" + ip + "==" + allidmap.get(device_id));
						// System.out.println("结束插入事件记录=============="+device_id+"="+ip+"=="+allidmap.get(device_id));

					}

					// 延迟10秒读取DSP波录管理
					/*
					 * if(device_id==16){
					 * LOGGER.info("DSP录波管理=============="+device_id+"="+ip+"=="
					 * +dsperrortimemap.get(device_id)); }
					 */

					if (dsperrortimemap.get(device_id) != null && d_type != 2 && dsperrortimemap.get(device_id) == 5) {
						// System.out.println("开始插入DSP录波管理=============="+device_id+"="+ip+"=="+allidmap.get(device_id));
						LOGGER.info("开始插入DSP录波管理==============" + device_id + "=" + ip + "==" + allidmap.get(device_id)
								+ "事件记录时刻的数据：" + modbustcpstr);
						updatedeviceerrorstate(device_id, "ERROR");
						inserterDspErroecode(device_id, d_type, modbustcpstr);
						errordatastrmap.put(device_id, modbustcpstr);
						dsperrortimemap.put(device_id, -1);
						updatedeviceerrorstate(device_id, "OK");
						// System.out.println("结束插入DSP录波管理=============="+device_id+"="+ip+"=="+allidmap.get(device_id));
						LOGGER.info(
								"结束插入DSP录波管理==============" + device_id + "=" + ip + "==" + allidmap.get(device_id));

					} else {

					}

					if (errortimemap.get(device_id) != null && errortimemap.get(device_id) > 10) {
						errortimemap.put(device_id, -1);
						allidmap.put(device_id, -1);
					}
					if (dsperrortimemap.get(device_id) != null && dsperrortimemap.get(device_id) > 5) {
						dsperrortimemap.put(device_id, -1);
					}
					Date day = new Date();
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String strtime = df.format(day);
					// 双馈，全功率
					if (d_type != 2) {
						int int38 = Integer.parseInt(modbustcpstr.split(",")[38]);
						int int20 = Integer.parseInt(modbustcpstr.split(",")[20]);
						List<String> liststr = parsetcp.parseModBusTcp(20, int20, d_type, modbustcpstr);
						if (liststr != null && !liststr.get(1).equals("0")) {
							String bit20_2 = liststr.get(1).split("[|]")[2].split(",")[2];
							String bit20_4 = liststr.get(1).split("[|]")[4].split(",")[2];
							// 出现故障 不更新wc_jc状态位
							// LOGGER.info(device_id+"设备并网标志位前："+errordevicestrmap.get(device_id)+"值："+wc_jc_map.get(device_id));
							if (errordevicestrmap.get(device_id) == null || errordevicestrmap.get(device_id) != true) {
								String bzstr = bit20_2 + "," + bit20_4;
								wc_jc_map.put(device_id, bzstr);
								// LOGGER.info(device_id+"设备并网标志位后："+errordevicestrmap.get(device_id)+"值："+wc_jc_map.get(device_id));
							} else {
								String wc_jc_mapstr = wc_jc_map.get(device_id);
								if (wc_jc_mapstr == null) {
									String bzstr = bit20_2 + "," + bit20_4;
									wc_jc_map.put(device_id, bzstr);
								}
							}

							// System.out.println(device_id+"="+ip+"中20_2位的值位" +
							// bit20_2 + " 20_4值位" + bit20_4);
							if (bit20_2.equals("1") && bit20_4.equals("1")) {
								// bwzt=1;如果状态之前不是1 ，表示 开始状态开始记录时间
								if (bwztmap.get(device_id) == null || bwztmap.get(device_id) == 0) {
									// 插入并网开始时间
									bwztmap.put(device_id, 1);
									int bwztidtmp = insertrunstatetime(device_id, "bwzt", strtime);
									bwztidmap.put(device_id, bwztidtmp);
								}
							} else {
								if (bwztmap.get(device_id) != null && bwztmap.get(device_id) == 1) {
									// 插入并网结束时间
									updaterunstatetime(bwztidmap.get(device_id), strtime);
								}
								bwztmap.put(device_id, 0);
							}
							if (int38 != 0) {
								// bwzt=1;如果状态之前不是1 ，表示 开始状态开始记录时间
								if (gzztmap.get(device_id) == null
										|| (gzztmap.get(device_id) != null && gzztmap.get(device_id) != 1)) {
									// 插入并网开始时间
									gzztmap.put(device_id, 1);
									gzztidmap.put(device_id, insertrunstatetime(device_id, "gzzt", strtime));
								}
							} else {
								if (gzztmap.get(device_id) != null && gzztmap.get(device_id) == 1) {
									updaterunstatetime(gzztidmap.get(device_id), strtime);
								}
								gzztmap.put(device_id, 0);
							}
						}
					} else {
						int int12837 = Integer.parseInt(modbustcpstr.split(",")[12836 - 12788]);
						int int12823 = Integer.parseInt(modbustcpstr.split(",")[12822 - 12788]);
						List<String> liststr = parsetcp.parseModBusTcp(12822, int12823, d_type, modbustcpstr);
						if (liststr != null) {
							String int12823_3 = liststr.get(1).split("[|]")[3].split(",")[2];
							String int12823_4 = liststr.get(1).split("[|]")[4].split(",")[2];
							// 出现故障 不更新wc_jc状态位
							if (errordevicestrmap.get(device_id) == null || errordevicestrmap.get(device_id) != true) {
								String bzstr = int12823_3 + "," + int12823_4;
								wc_jc_map.put(device_id, bzstr);
							} else {
								String wc_jc_mapstr = wc_jc_map.get(device_id);
								if (wc_jc_mapstr == null) {
									String bzstr = int12823_3 + "," + int12823_4;
									wc_jc_map.put(device_id, bzstr);
								}
							}
							if (int12823_3.equals("1") && int12823_4.equals("1")) {
								// bwzt=1;如果状态之前不是1 ，表示 开始状态开始记录时间
								if (bwztmap.get(device_id) == null || bwztmap.get(device_id) == 0) {
									// 插入并网开始时间
									bwztmap.put(device_id, 1);
									int bwztidtmp = insertrunstatetime(device_id, "bwzt", strtime);
									bwztidmap.put(device_id, bwztidtmp);
								}
							} else {
								if (bwztmap.get(device_id) != null && bwztmap.get(device_id) == 1) {
									// 插入并网结束时间
									updaterunstatetime(bwztidmap.get(device_id), strtime);
								}
								bwztmap.put(device_id, 0);
							}
							if (int12837 != 0) {
								// bwzt=1;如果状态之前不是1 ，表示 开始状态开始记录时间
								if (gzztmap.get(device_id) == null
										|| (gzztmap.get(device_id) != null && gzztmap.get(device_id) != 1)) {
									// 插入并网开始时间
									gzztmap.put(device_id, 1);
									gzztidmap.put(device_id, insertrunstatetime(device_id, "gzzt", strtime));
								}
							} else {
								if (gzztmap.get(device_id) != null && gzztmap.get(device_id) == 1) {
									updaterunstatetime(gzztidmap.get(device_id), strtime);
								}
								gzztmap.put(device_id, 0);
							}
						}

					}

					Date d = new Date();
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(d);
					// 整点插入运行状态
					if ((gc.get(GregorianCalendar.MINUTE) == 0)) {
						if (runstatemap == null || runstatemap.get(device_id) == true) {
							insertrundata(device_id, d_type, modbustcpstr);
							runstatemap.put(device_id, false);
						}
					} else {
						runstatemap.put(device_id, true);
					}
					// 每天0点兑时
					if ((gc.get(GregorianCalendar.HOUR_OF_DAY) == 0)) {
						if (timestatemap == null || timestatemap.get(device_id) == true) {
							starttimeupdate(device_id, d_type, ip, port);
							timestatemap.put(device_id, false);
						}
					} else {
						timestatemap.put(device_id, true);
					}
				}
				updatedevicestate(device_id, "采集数据正常");
			}

			if (errortimemap.get(device_id) != null && errortimemap.get(device_id) != -1) {
				errortimemap.put(device_id, errortimemap.get(device_id) + 1);
			}
			if (dsperrortimemap.get(device_id) != null && dsperrortimemap.get(device_id) != -1) {
				dsperrortimemap.put(device_id, dsperrortimemap.get(device_id) + 1);
			}
			if (rs_trd != null) {
				rs_trd.close();
			}
			if (st_trd != null) {
				st_trd.close();
			}
			if (conn_trd != null) {
				conn_trd.close();
			}
		} catch (SQLException e) {
			if (errortimemap.get(device_id) > 10) {
				errortimemap.put(device_id, -1);
				allidmap.put(device_id, -1);
			}
			if (dsperrortimemap.get(device_id) > 5) {
				dsperrortimemap.put(device_id, -1);
			}
			e.printStackTrace();
		}
		return errorstate;
	}

	private static void updatedeviceerrorstate(int device_id, String string) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = " UPDATE   windpower_device set  errorstate='" + string + "'  where id=" + device_id;
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 兑时操作
	 * 
	 * @param device_id
	 * @param d_type
	 * @param ip
	 * @param port
	 */
	private static void starttimeupdate(int device_id, int d_type, String ip, int port) {
		String modbusstr = "";
		String timestr = "";
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		int mm = gc.get(GregorianCalendar.MONTH) + 1;
		timestr = gc.get(GregorianCalendar.YEAR) + "," + mm + "," + gc.get(GregorianCalendar.DATE) + ","
				+ gc.get(GregorianCalendar.HOUR_OF_DAY) + "," + gc.get(GregorianCalendar.MINUTE) + ","
				+ gc.get(GregorianCalendar.SECOND);
		timestr = timestr.substring(2, timestr.length());
		if (d_type == 2) {
			modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(ip, port, d_type, "31f4", timestr, "0");
		} else {
			// 672-677 /13179-13184
			modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(ip, port, d_type, "02A0", timestr, "0");
		}
		// 确认指令,第一条指令有正确的返回时间时才去发送确认指令
		if (modbusstr.contains(",")) {
			if (d_type == 2) {
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(ip, port, d_type, "31f4", "8AH", "0");
			} else {
				// 672-677 /13179-13184
				modbusstr = SunModbusTcpbyIp.WriteSunModbusTcpStrAll(ip, port, d_type, "0000", "8DH", "0");
			}
		}
		String databz = "";
		if (modbusstr.contains(",")) {
			databz = "兑时成功";
		} else {
			databz = "兑时失败";
		}
		inserttimecode(device_id, timestr, databz);
	}

	/**
	 * 插入兑时日志
	 * 
	 * @param device_id
	 * @param timestr
	 */

	private static void inserttimecode(int device_id, String timestr, String databz) {
		try {

			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "  insert into  windpower_duitime (device_id,timestr,dsstate)values(" + device_id + ",'"
					+ timestr + "','" + databz + "')";
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入运行状态 开始时间
	 * 
	 * @param string
	 * @param strtime
	 * @return
	 */
	private static int insertrunstatetime(int device_id, String string, String strtime) {
		int colid = 0;
		try {

			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "  insert into  windpower_runstatect (device_id,run_type,starttime)values(" + device_id + ",'"
					+ string + "','" + strtime + "')";
			st_trd.execute(sql);
			String idsql = "SELECT @@IDENTITY id";
			rs_trd = st_trd.executeQuery(idsql);
			int id = 0;
			while (rs_trd.next()) {
				id = rs_trd.getInt("id");
			}
			colid = id;
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return colid;
	}

	/**
	 * 设置结束时间
	 * 
	 * @param colid
	 * @param strtime
	 */
	private static void updaterunstatetime(int colid, String strtime) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "  update   windpower_runstatect set endtime='" + strtime + "' where id=" + colid + "";
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void insertrundata(int device_id, int d_type, String modbustcpstr) {
		/*
		 * 运行日志报表展示数据：
		 * 电网电压/电流/频率/并网有功功率/并网无功功率,发电量,待机时间,并网时间,故障时间，平均利用小时数，平均发电功率,变流器无故障利用率
		 * 
		 * 双馈： 电网电压:指电网线电压 协议地址501 电流:指机组输出总有功电流 协议地址503 频率:指电网频率 协议地址85
		 * 并网有功功率:指机组输出有功功率 协议地址490 并网无功功率:指机组输出无功功率 协议地址491
		 * 发电量:发电量累计由变流器完成,不同的报表根据相应时刻的发电量数据进行减法操作即可;例如日报,
		 * 将当日24时的发电量数据减去0时的数据即是当日的机组发电量; 发电量计算:协议地址515和516的数据合并为32位整形数即是发电量.
		 * 待机时间:运行报表统计时间-并网时间-故障时间; 并网时间:参见风场概览的运行状态条件 故障时间:参见风场概览的故障判断条件
		 * 平均利用小时数:(统计时间-故障时间)/统计天数 平均发电功率:单位为kw,发电量/统计时间(h)
		 * 变流器无故障利用率:(统计时间-故障时间)/统计时间,百分数标量.
		 */

		String[] tcpstr = modbustcpstr.split(",");
		int dwdy = Integer.parseInt(tcpstr[501]);
		int dl = Integer.parseInt(tcpstr[503]);
		double pl = (double) Integer.parseInt(tcpstr[85]) / 10;
		int bwyggl = Integer.parseInt(tcpstr[490]);
		int bwwggl = Integer.parseInt(tcpstr[491]);
		int fdl = getfdl(modbustcpstr);
		long bwsj_long = getdeviceruntime("bwzt", device_id);
		long gzsj_long = getdeviceruntime("gzzt", device_id);
		String bwsj = bwsj_long + "";
		String gzsj = gzsj_long + "";
		String djsj = 60 * 60 - bwsj_long - gzsj_long + "";
		// 平均利用小时数
		long pjlyxss = (60 * 60 - gzsj_long) / 1;
		// 平均发电功率
		long pjfdgl = fdl / 1;
		DecimalFormat df00 = new DecimalFormat("###0.00");
		// 变流器无故障利用率
		double aa = 60 * 60 - gzsj_long;
		double bb = 3600;
		double ss = aa / bb;
		String blqwgzlyl = df00.format(ss);
		boolean stateerror = getdevicerunstate(device_id);
		String name = getdeviename(device_id);
		// 如果已经插入了就返回
		if (stateerror == false) {
			return;
		}
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "  insert into  windpower_formrundata (dwdy,dl,pl,bwyggl,bwwggl,fdl,djsj,bwsj,gzsj,pjlyxss,pjfdgl,blqwgzlyl,device_id,device_name)values("
					+ dwdy + "," + dl + "," + pl + "," + bwyggl + "," + bwwggl + "," + fdl + ",'" + djsj + "','" + bwsj
					+ "','" + gzsj + "'," + pjlyxss + "," + pjfdgl + ",'" + blqwgzlyl + "'," + device_id + ",'" + name
					+ "')";
			System.out.println("查询运行报表数据：" + sql);
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static boolean getdevicerunstate(int device_id) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "  select *  from windpower_formrundata where  createtime >=CURRENT_TIMESTAMP - INTERVAL 60 second and device_id="
					+ device_id;
			rs_trd = st_trd.executeQuery(sql);
			boolean isct = true;
			while (rs_trd.next()) {
				// return false;
				isct = false;
			}
			st_trd.close();
			conn_trd.close();
			return isct;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static long getdeviceruntime(String string, int device_id) {

		long run_time = 0;
		// 统计结束时间的时间
		Date day = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String endtimestr = df.format(day);

		String starttimestr = "";
		String endtimestrtmp = "";
		Calendar cal = Calendar.getInstance();
		endtimestrtmp = df.format(cal.getTime());
		int hh = cal.get(Calendar.HOUR) - 1;
		cal.set(Calendar.HOUR, hh);
		starttimestr = df.format(cal.getTime());
		try {
			long starttime = df.parse(starttimestr).getTime();
			long endtime = df.parse(endtimestrtmp).getTime();

			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  *  from  windpower_runstatect  where device_id=" + device_id + "  and run_type='"
					+ string + "'  and  DATE_FORMAT( starttime, '%Y-%c-%d %H:%i:%s')>= DATE_FORMAT('" + starttimestr
					+ "' ,'%Y-%c-%d %H:%i:%s') and  (DATE_FORMAT( endtime, '%Y-%c-%d %H:%i:%s')>= DATE_FORMAT('"
					+ endtimestr + "' ,'%Y-%c-%d %H:%i:%s') OR endtime is null) "
					+ " AND DATE_FORMAT( starttime, '%Y-%c-%d %H:%i:%s')<=DATE_FORMAT('" + endtimestr
					+ "' ,'%Y-%c-%d %H:%i:%s')";
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				String tmp_str = rs_trd.getString("starttime");
				long tmp_time = df.parse(tmp_str).getTime();

				// String tmp_str_end=rs_trd.getString("endtime");
				// long tmp_time_end=df.parse(tmp_str_end).getTime();
				// 开始时间在小时区间内，结束不在
				long i = (endtime - tmp_time) / 1000;
				run_time = run_time + i;
			}
			String sql01 = "select  *  from  windpower_runstatect  where device_id=" + device_id + " and run_type='"
					+ string + "' and  DATE_FORMAT( starttime, '%Y-%c-%d %H:%i:%s')<= DATE_FORMAT('" + starttimestr
					+ "' ,'%Y-%c-%d %H:%i:%s')  and  (DATE_FORMAT( endtime, '%Y-%c-%d %H:%i:%s')>=DATE_FORMAT('"
					+ endtimestr + "' ,'%Y-%c-%d %H:%i:%s')   OR endtime is null)";
			rs_trd = st_trd.executeQuery(sql01);
			while (rs_trd.next()) {
				// 开始结束时间 大于1小时。
				int i = 60 * 60;
				run_time = run_time + i;
			}
			String sql02 = "select  *  from  windpower_runstatect  where device_id=" + device_id + " and run_type='"
					+ string + "' and  DATE_FORMAT( starttime, '%Y-%c-%d %H:%i:%s')<= DATE_FORMAT('" + starttimestr
					+ "' ,'%Y-%c-%d %H:%i:%s') and  DATE_FORMAT( endtime, '%Y-%c-%d %H:%i:%s')<=DATE_FORMAT('"
					+ endtimestr
					+ "' ,'%Y-%c-%d %H:%i:%s') and DATE_FORMAT( endtime, '%Y-%c-%d %H:%i:%s')>=DATE_FORMAT('"
					+ starttimestr + "' ,'%Y-%c-%d %H:%i:%s') ";
			rs_trd = st_trd.executeQuery(sql02);
			while (rs_trd.next()) {
				// String tmp_str = rs_trd.getString("starttime");
				// long tmp_time = df.parse(tmp_str).getTime();

				String tmp_str_end = rs_trd.getString("endtime");
				long tmp_time_end = df.parse(tmp_str_end).getTime();
				// 开始在外面，结束在里面
				long i = (tmp_time_end - starttime) / 1000;
				run_time = run_time + i;
			}
			String sql03 = "select  *  from  windpower_runstatect  where device_id=" + device_id + " and run_type='"
					+ string + "' and  DATE_FORMAT( starttime, '%Y-%c-%d %H:%i:%s')>=DATE_FORMAT('" + starttimestr
					+ "' ,'%Y-%c-%d %H:%i:%s') and  DATE_FORMAT( endtime, '%Y-%c-%d %H:%i:%s')<=DATE_FORMAT('"
					+ endtimestr + "' ,'%Y-%c-%d %H:%i:%s') ";
			rs_trd = st_trd.executeQuery(sql03);
			while (rs_trd.next()) {
				String tmp_str = rs_trd.getString("starttime");
				long tmp_time = df.parse(tmp_str).getTime();

				String tmp_str_end = rs_trd.getString("endtime");
				long tmp_time_end = df.parse(tmp_str_end).getTime();
				// 开始结束在小时内
				long i = (tmp_time_end - tmp_time) / 1000;
				run_time = run_time + i;
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		return run_time;
	}

	private static String getdeviename(int device_id) {
		String name = "";
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  name  from  windpower_device  where id=" + device_id;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				name = rs_trd.getString("name");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取发电量
	 * 
	 * @param modbustsr
	 * @return
	 */
	private static int getfdl(String modbustsr) {
		// 电机参数 和发电总量
		short[] arraytmp = new short[2];
		arraytmp[0] = (short) Integer.parseInt(modbustsr.split(",")[515]);
		arraytmp[1] = (short) Integer.parseInt(modbustsr.split(",")[516]);
		int unshort = unshortToInt(arraytmp, 0);
		/*
		 * int num = (int) Math.ceil(unshort / 10); int numy = (int)
		 * Math.ceil(unshort % 10); if (numy != 0) { num++; } unshort=num;
		 */
		return unshort;
	}

	public static int unshortToInt(short[] unshort, int off) {
		int b0 = unshort[off] & 0xFFFF;
		int b1 = unshort[off + 1] & 0xFFFF;
		return (b1 << 16) | b0;
	}

	
	static int numct = 0;
	static String[] codestr = { "01", "02", "03" };
	/**
	 * 插入dsp故障数据
	 * 
	 * @param ip
	 * @param i
	 * @param d_type
	 * @param modbustcpstr
	 */
	private static void inserterDspErroecode(int device_id, int d_type, String modbustcpstr) {
		// 复位DSP故障标志位
		errordevicestrmap.put(device_id, false);
		// LOGGER.info(device_id+"赋值："+errordevicestrmap.get(device_id));
		// 查询是否存在机测网侧故障
		String errorcode = geterrortype(d_type, modbustcpstr);
		if (errorcode.equals("00")) {
			// System.out.print("没有故障信息，不采集DSP故障录波");
			LOGGER.info("没有故障信息，不采集DSP故障录波");
			return;
		}
		LOGGER.info("发现故障录波，网侧，机侧出现标志：" + errorcode);
		// 获取发生故障前一个数据的wc_jc
		String wc_jc_str = wc_jc_map.get(device_id);
		LOGGER.info(device_id + "发现故障录波，网侧，机侧并网标志：" + wc_jc_str);
		String wc_str = wc_jc_str.split(",")[0];
		String jc_str = wc_jc_str.split(",")[1];
		String[] waveformCodes = new String[2];
		String[] tcpst = modbustcpstr.split(",");
		String dsp_detail = getDsp_IP(device_id);
		if (dsp_detail == null || dsp_detail == "" || dsp_detail.indexOf(",") < 0) {
			LOGGER.info("不存在改设备IP");
			return;
		} else {
			// 网侧机测设备名称。
			if (dsp_detail.split(",").length != 3) {
				LOGGER.info("不存在改设备IP");
				return;
			}
		}
		String erroetimestr = geterrortime(modbustcpstr, d_type);
		String dsp_wc_ip = dsp_detail.split(",")[0].split(":")[0];
		String dsp_wc_port = dsp_detail.split(",")[0].split(":")[1];
		String dsp_jc_ip = dsp_detail.split(",")[1].split(":")[0];
		String dsp_jc_port = dsp_detail.split(",")[1].split(":")[1];
		String devide_name = dsp_detail.split(",")[2];
		if (d_type == 0) {
			String wc_91 = tcpst[91];
			String jc_256 = tcpst[256];
			if (errorcode.substring(0, 1).equals("1") && wc_str.equals("1")) {
				waveformCodes[0] = get4HexString(wc_91).split(",")[0];
				waveformCodes[1] = get4HexString(wc_91).split(",")[1];
				LOGGER.info("网侧编号" + waveformCodes[1]);
				List<Map<String, String>> list = DSPTcpUtils.getFaultWaveformDataList(dsp_wc_ip,
						Integer.parseInt(dsp_wc_port), 66, waveformCodes);
				if (list != null) {
					String excelurl = new CreateExcelByHashMap().createexcl(list, "网侧故障" + waveformCodes[1],
							devide_name);
					insertdsperror(device_id, 1, excelurl.replace("\\", "/"), erroetimestr);
				}
			}
			if (errorcode.substring(1, 2).equals("1") && jc_str.equals("1")) {
				waveformCodes[0] = get4HexString(jc_256).split(",")[0];
				waveformCodes[1] = get4HexString(jc_256).split(",")[1];
				LOGGER.info("机侧编号" + waveformCodes[1]);
				List<Map<String, String>> list2 = DSPTcpUtils.getFaultWaveformDataList(dsp_jc_ip,
						Integer.parseInt(dsp_jc_port), 66, waveformCodes);
				if (list2 != null) {
					String excelurl2 = new CreateExcelByHashMap().createexcl(list2, "机侧故障" + waveformCodes[1],
							devide_name);
					insertdsperror(device_id, 2, excelurl2.replace("\\", "/"), erroetimestr);
				}
			}
		} else {
			String wc_89 = tcpst[89];
			String jc_261 = tcpst[261];
			if (errorcode.substring(0, 1).equals("1") && wc_str.equals("1")) {
				waveformCodes[0] = get4HexString(wc_89).split(",")[0];
				waveformCodes[1] = get4HexString(wc_89).split(",")[1];
				LOGGER.info("网侧编号" + waveformCodes[1]);
				List<Map<String, String>> list = DSPTcpUtils.getFaultWaveformDataList(dsp_wc_ip,
						Integer.parseInt(dsp_wc_port), 66, waveformCodes);
				if (list != null) {
					String excelurl = new CreateExcelByHashMap().createexcl(list, "网侧故障", devide_name);
					insertdsperror(device_id, 1, excelurl.replace("\\", "/"), erroetimestr);
				}

			}
			if (errorcode.substring(1, 2).equals("1") && jc_str.equals("1")) {
				waveformCodes[0] = get4HexString(jc_261).split(",")[0];
				waveformCodes[1] = get4HexString(jc_261).split(",")[1];
				LOGGER.info("机侧编号" + waveformCodes[1]);
				List<Map<String, String>> list = DSPTcpUtils.getFaultWaveformDataList(dsp_jc_ip,
						Integer.parseInt(dsp_wc_port), 66, waveformCodes);
				if (list != null) {
					String excelurl = new CreateExcelByHashMap().createexcl(list, "机侧故障", devide_name);
					insertdsperror(device_id, 2, excelurl.replace("\\", "/"), erroetimestr);
				}

			}
		}
	}

	/**
	 * c查询 设备的dsp的 IP信息
	 * 
	 * @param device_id
	 * @return
	 */
	private static String getDsp_IP(int device_id) {
		String devicedetail = "";
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  t.ip,t.port,t1.name from  windpower_deviceinfo t ,windpower_device t1   where    t.device_id=t1.id and t.device_id="
					+ device_id + "  and  t.d_type='dsp' order by t.dsp_type asc";
			rs_trd = st_trd.executeQuery(sql);
			String name = "";
			while (rs_trd.next()) {
				String ip = rs_trd.getString("ip");
				int port = rs_trd.getInt("port");
				name = rs_trd.getString("name");
				String ip_port = ip + ":" + port;
				devicedetail += ip_port + ",";
			}
			devicedetail += name;
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return devicedetail;

	}

	/**
	 * 关闭运行状态中上次异常的数据
	 */
	public static void closeRundata() {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			PreparedStatement st_trd = conn_trd
					.prepareStatement("update  windpower_runstatect  set endtime=? where  endtime is null");
			Date day = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String endtimestr = df.format(day);
			st_trd.setString(1, endtimestr);
			st_trd.execute();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 插入 DSP故障记录
	private static void insertdsperror(int device_id, int type, String excelurl, String erroetimestr) {
		String device_name = "";
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  name from  windpower_device et where  id=" + device_id;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				device_name = rs_trd.getString("name");
			}
			String sqlinert = "insert into windpower_errordataparse_dsp (device_id,device_name,error_type,error_excel,error_time)values("
					+ device_id + ",'" + device_name + "'," + type + ",'" + excelurl + "','" + erroetimestr + "')";
			st_trd.execute(sqlinert);
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static String geterrortype(int d_type, String modbustcpstr) {
		String wc_jc = "";
		String[] strtcp = modbustcpstr.split(",");
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  et.id,et.errorname,ed.addr,ed.bitid,ap.name,ap.category from  windpower_errortype et,"
					+ "windpower_errortypdetail  ed ,modbustcp_sk_app ap where et.id=ed.error_type   and ap.addr=ed.addr and ed.modbus_type=ap.modbus_type  and  ed.modbus_type="
					+ d_type + "  and  (errorname like'%网侧%'  or errorname like'%机侧%')";
			rs_trd = st_trd.executeQuery(sql);
			String wc_error = "0";
			String jc_error = "0";
			while (rs_trd.next()) {
				int addr = rs_trd.getInt("addr");
				String name = rs_trd.getString("errorname");
				String addbalue = strtcp[addr];
				if (!addbalue.equals("0")) {
					if (name.contains("网侧")) {
						wc_error = "1";
					} else if (name.contains("机侧")) {
						jc_error = "1";
					}
				}

			}
			wc_jc = wc_error + jc_error;
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return wc_jc;
	}

	/**
	 * 插入故障记录、复位后不插入
	 * 
	 * @param device_id
	 * @param data_id
	 * @param modbustcpstr
	 */
	public static void inserterrordevice(int device_id, int data_id, String modbustcpstr) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "insert into windpower_historyerrordata (device_id,data,data_id )values(" + device_id + ",'"
					+ modbustcpstr + "'," + data_id + ")";
			LOGGER.info("插入错误历史数据：" + sql);
			st_trd.execute(sql);
			String idsql = "SELECT @@IDENTITY id";
			rs_trd = st_trd.executeQuery(idsql);
			int id = 0;
			while (rs_trd.next()) {
				id = rs_trd.getInt("id");
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			// 解析错误数据
			parseeerrorcode(id);
		} catch (SQLException e) {
			LOGGER.info(" 插入故障记录失败" + "设备编号为：" + device_id);
			e.printStackTrace();
		}
	}

	/**
	 * 获取估计数据的前100天，变现成EXcel
	 * 
	 * @param device_id
	 * @param i
	 * @return
	 */
	private static List<AcqData> geterrordata100(int device_id, int i) {
		List<AcqData> datalist = new ArrayList<AcqData>();
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select hd.id, hd.data,hd.create_time,d.name,d.id device_id from   windpower_dataacq_tab hd,windpower_device d where hd.device_id=d.id and hd.device_id="
					+ device_id + " and  hd.id<=" + i + "  order  by  create_time desc limit 0,101";
			ResultSet rs_trd = null;
			LOGGER.info("查询错误设备前100数据：" + sql);
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				AcqData data = new AcqData();
				data.setCreate_time(rs_trd.getTimestamp("create_time"));
				data.setData(rs_trd.getString("data"));
				data.setDevice_id(rs_trd.getInt("device_id"));
				data.setName(rs_trd.getString("name"));
				datalist.add(data);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			LOGGER.info(" 插入故障记录失败" + "设备编号为：" + device_id);
			e.printStackTrace();
		}
		return datalist;
	}

	/**
	 * 开始解析错误数据
	 * 
	 * @param id
	 */
	public static void parseeerrorcode(int id) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  h.*,d.name,mt.id modbus_type from  windpower_historyerrordata h,windpower_device d ,windpower_devicetype dt,modbustcp_type mt where   h.device_id=d.id and d.device_type_id=dt.id and dt.modbus_type=mt.id"
					+ "   and h.id=" + id;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				String data = rs_trd.getString("data");
				String name = rs_trd.getString("name");
				int modbus_type = rs_trd.getInt("modbus_type");
				// 获取excel
				String excelurl = new CreateExcel().createexc(geterrordata100(rs_trd.getInt("device_id"), rs_trd.getInt("data_id")), modbus_type);
				// 获取错误数据对象list
				List<Errordataobj> errdatalist = geterrordatalist(id, data, name, modbus_type,excelurl.replace("\\", "/"));
				// 插入时间记录，错误数据精确到每隔bit位，1个位错误显示一条数据
				inerterrordatalist(errdatalist);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入时间记录错误数据表
	 * 
	 * @param errdatalist
	 */
	private static void inerterrordatalist(List<Errordataobj> errdatalist) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (int i = 0; i < errdatalist.size(); i++) {
				String sql = "insert into  windpower_errordataparse (" + "device_name," + "error_type,"
						+ "error_column," + "error_name," + "error_excel," + "errordata_id," + "cometime" + ")value"
						+ "('" + errdatalist.get(i).getDevice_name() + "'," + errdatalist.get(i).getError_type() + ",'"
						+ errdatalist.get(i).getError_column() + "','" + errdatalist.get(i).getError_name() + "','"
						+ errdatalist.get(i).getError_excel() + "'," + errdatalist.get(i).getErrordata_id() + ",'"
						+ errdatalist.get(i).getCometime() + "')";
				LOGGER.info("插入时间记录错误解析库：" + sql);
				st_trd.execute(sql);
			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取错误数据ERRordata对象
	 * 
	 * @param data_id
	 * @param data
	 * @param device_name
	 * @param modbus_type
	 * @param excelurl
	 * @return
	 */
	public static List<Errordataobj> geterrordatalist(int data_id, String data, String device_name, int modbus_type,
			String excelurl) {
		List<Errordataobj> errdatalist = new ArrayList<Errordataobj>();
		List<Errordataobj> errdatarepairlist = new ArrayList<Errordataobj>();
		try {
			// 获取错误发生时间
			String erroetimestr = geterrortime(data, modbus_type);
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			// 查询故障类型和故障详情和对应故障的地址-在取地址的相应地址值-解析
			String sql = "select  et.id,et.errorname,ed.addr,ed.bitid,ap.name,ap.category from  windpower_errortype et,"
					+ "	windpower_errortypdetail  ed ,modbustcp_sk_app ap where et.id=ed.error_type   and ap.addr=ed.addr and ed.modbus_type=ap.modbus_type  and  ed.modbus_type="
					+ modbus_type;
			rs_trd = st_trd.executeQuery(sql);
			String[] modbusstr = data.split(",");

			while (rs_trd.next()) {
				int error_type = rs_trd.getInt("id");
				int addr = rs_trd.getInt("addr");
				String bitid = rs_trd.getString("bitid");
				String aadrname = rs_trd.getString("name");
				String errorname = rs_trd.getString("errorname");

				int addrnew = addr;
				if (modbus_type == 2) {
					addrnew = addr - 12788;
				}
				if (Integer.parseInt(modbusstr[addrnew]) > 0) {
					List<ModbusBit> li = getbittrue(addr, modbus_type);
					// bit位没有值，xml提供的地址没有bit为信息
					if (li.size() > 0) {
						String bitstring = setbittrue(li, Integer.parseInt(data.split(",")[addrnew]));
						// bit位空，不存在特殊bit位
						// 地址值大于0
						// 所有位都是故障信息
						if (bitid == null || bitid == "") {
							int iscz = 0;
							//时间记录数据，以bit为位最小记录单位
							for (int i = 0; i < bitstring.split("\\|").length; i++) {
								Errordataobj ED = new Errordataobj();
								if (Integer.parseInt(bitstring.split("\\|")[i].split(",")[2]) == 1) {
									ED.setError_name(bitstring.split("\\|")[i].split(",")[1]);
									ED.setError_column(aadrname + ":bit" + i);
									ED.setDevice_name((device_name));
									ED.setError_type(error_type);
									ED.setErrordata_id(data_id);
									ED.setError_excel(excelurl);
									ED.setCometime(erroetimestr);
									errdatalist.add(ED);
									iscz = 1;
								}
							}
							// 派单数据，一个故障作为一个派单 以地址为最新单位记录
							if (iscz == 1) {
								Errordataobj ED = new Errordataobj();
								ED.setError_name(errorname);
								ED.setError_column(aadrname);
								ED.setDevice_name((device_name));
								ED.setError_type(error_type);
								ED.setErrordata_id(data_id);
								ED.setError_excel(excelurl);
								ED.setCometime(erroetimestr);
								errdatarepairlist.add(ED);
							}
						} else {
							int iscz2 = 0;
							for (int i = 0; i < bitstring.split("\\|").length; i++) {
								Errordataobj ED = new Errordataobj();
								if (Integer.parseInt(bitstring.split("\\|")[i].split(",")[2]) == 1 && (Arrays
										.asList(bitid.split(",")).contains(bitstring.split("\\|")[i].split(",")[0]))) {
									ED.setError_name(bitstring.split("\\|")[i].split(",")[1]);
									ED.setError_column(aadrname + ":bit" + i);
									ED.setDevice_name((device_name));
									ED.setError_type(error_type);
									ED.setErrordata_id(data_id);
									ED.setError_excel(excelurl);
									ED.setCometime(erroetimestr);
									errdatalist.add(ED);
									iscz2 = 1;
								}
							}
							if (iscz2 == 1) {
								Errordataobj ED = new Errordataobj();
								ED.setError_name(errorname);
								ED.setError_column(aadrname);
								ED.setDevice_name((device_name));
								ED.setError_type(error_type);
								ED.setErrordata_id(data_id);
								ED.setError_excel(excelurl);
								ED.setCometime(erroetimestr);
								errdatarepairlist.add(ED);
							}
						}
					}
				}
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
			// 插入派单数据
			inserterrordatarepair(errdatarepairlist);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return errdatalist;
	}

	/**
	 * 获取时间记录的时间
	 * 
	 * @param data
	 * @param modbus_type
	 * @return
	 */
	private static String geterrortime(String data, int modbus_type) {
		String errortimestr = "";
		String datastr[] = data.split(",");
		try {
			Connection conn_trd = JDBConnection.getConnect();
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			int array_type = 4;
			if (modbus_type == 2) {
				array_type = 120;
			}
			String sql = "select  *  from modbustcp_sk_app where  array_type=" + array_type + " and  modbus_type="
					+ modbus_type;
			rs_trd = st_trd.executeQuery(sql);
			int i = 0;
			while (rs_trd.next()) {
				int addr = Integer.parseInt(rs_trd.getString("addr"));
				if (modbus_type == 2) {
					addr = addr - 12788;
				}
				if (i == 6) {
					return errortimestr;
				} else if (i < 2) {
					errortimestr += datastr[addr] + "-";
				} else if (i == 2) {
					errortimestr += datastr[addr] + "  ";
				} else if (i < 5) {
					errortimestr += datastr[addr] + ":";
				} else if (i == 5) {
					errortimestr += datastr[addr];
				}
				i++;
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errortimestr;
	}
	/**
	 * 插入派单数据 最小单位是地址 一条错误可能 有很多个派单
	 * @param errdatarepairlist
	 */
	private static void inserterrordatarepair(List<Errordataobj> errdatarepairlist) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			for (int i = 0; i < errdatarepairlist.size(); i++) {
				String sql = "insert into  windpower_errordatarepair (" + "device_name," + "error_type,"
						+ "error_column," + "error_name," + "error_excel," + "errordata_id," + "cometime" + ")value"
						+ "('" + errdatarepairlist.get(i).getDevice_name() + "',"
						+ errdatarepairlist.get(i).getError_type() + ",'" + errdatarepairlist.get(i).getError_column()
						+ "','" + errdatarepairlist.get(i).getError_name() + "','"
						+ errdatarepairlist.get(i).getError_excel() + "'," + errdatarepairlist.get(i).getErrordata_id()
						+ ",'" + errdatarepairlist.get(i).getCometime() + "')";
				LOGGER.info("插入派单解析库：" + sql);
				st_trd.execute(sql);
				// 派发邮件
				SimpleMailSender smail = new SimpleMailSender();
				smail.mailSend(
						errdatarepairlist.get(i).getDevice_name() + "=" + errdatarepairlist.get(i).getError_name() + ":"
								+ errdatarepairlist.get(i).getError_column(),
						errdatarepairlist.get(i).getError_excel());
			}
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 查询地址下的所有bit
	 * @param addr
	 * @param modbus_type
	 */
	private static List<ModbusBit> getbittrue(int addr, int modbus_type) {
		List<ModbusBit> bitlist = new ArrayList<ModbusBit>();
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs_trd = null;
			String sql = "select  * from modbustcp_sk_appparame where  modbus_type=" + modbus_type + " and  addr=" + addr;
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				ModbusBit mbit = new ModbusBit();
				mbit.setAddr(rs_trd.getInt("addr"));
				mbit.setBit_id(rs_trd.getInt("bit_id"));
				mbit.setName(rs_trd.getString("var1"));
				bitlist.add(mbit);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitlist;
	}

	/**
	 * 值和Bit位的片接
	 * @param bitlist
	 * @param shortvalue
	 */
	public static String setbittrue(List<ModbusBit> bitlist, int shortvalue) {
		String bstr = Integer.toBinaryString(shortvalue);
		String bitstr = "";
		if (shortvalue < 0) {
			bstr = bstr.substring(16, 32);
		}
		for (int i = bstr.length() - 1; i >= 0; i--) {
			int state = Integer.parseInt((bstr.substring(i, i + 1)));
			ModbusBit bitdemo = bitlist.get(bstr.length() - i - 1);
			bitdemo.setShowstate(state);
			bitstr += bitdemo.toString() + "|";
		}
		for (int j = bstr.length(); j < bitlist.size(); j++) {
			ModbusBit bitdemonew = bitlist.get(j);
			bitdemonew.setShowstate(0);
			if (j == bitlist.size() - 1) {
				bitstr += bitdemonew.toString();
			} else {
				bitstr += bitdemonew.toString() + "|";
			}
		}
		return bitstr;

	}

	/**
	 * 字符组成16进制的字符
	 * @param HexString
	 */
	public static String get4HexString(String HexString) {
		String hx = "";
		if (HexString.length() < 2) {
			hx = "00,0" + HexString;
		} else if (HexString.length() < 3) {
			hx = "00," + HexString;
		} else if (HexString.length() < 4) {
			hx = "0" + HexString.substring(0, 1) + "," + HexString.substring(1, 3);
		} else {
			hx = HexString.substring(0, 2) + "," + HexString.substring(2, 4);
		}
		return hx;
	}

	/**
	 * 删除实时数据前吧设备状态修改为null
	 * @param state
	 */
	public static void updateRunState(String state) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "update  windpower_device  set  errorstate='" + state + "'";
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除实时库中的数据
	 */
	public static void deleteRealData() {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "delete  from  windpower_dataacq_tab ";
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
