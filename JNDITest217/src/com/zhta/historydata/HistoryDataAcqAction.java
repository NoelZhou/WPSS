package com.zhta.historydata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zhta.bean.JDBConnection;

public class HistoryDataAcqAction {

	private static final Log LOGGER = LogFactory.getLog(HistoryDataAcqAction.class);

	private static int save_time = 30;// 实时库保存时间周期
	private static boolean isrun = true;
	private static int acq_real_time;
	// 查询实时入库时间
	private static int acq_history_time;// 历史库保存周期

	public static void stopHistoryData() {
		isrun = false;
		System.out.println("关闭历史库采集线程!");
	}

	public static void HisTorystartDataAcq() {
		startthread();
	}

	public static void startthread() {
		System.out.println("开启历史库采集进程......");
		ExecutorService threadPool = Executors.newScheduledThreadPool(1);
		threadPool.execute(new Runnable() {
			public void run() {
				isrun = true;
				while (isrun) {
					try {
						getacq_history_time();
						Thread.sleep(acq_real_time * 1000);
						delhistorydata();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		});
	}

	/**
	 * 获取历史数据，实时数据，保存间隔时间
	 */
	public static void getacq_history_time() {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			ResultSet rs = null;
			String sql = "select  real_time,history,save_time from  windpower_dataacqtime ";
			PreparedStatement pStatement = conn_trd.prepareStatement(sql);
			rs = pStatement.executeQuery();
			while (rs.next()) {
				acq_history_time = rs.getInt("history");
				save_time = rs.getInt("save_time");
				acq_real_time = rs.getInt("real_time");
			}
			rs.close();
			pStatement.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除超过历史库数据保存时间的数据
	 */
	public static void delhistorydata() {
		try {
//			LOGGER.info(" 正在删除历史库超过"+acq_history_time+"天数据.......");
			Connection conn_trd = JDBConnection.getConnect();
			String sql = "delete from  windpower_dataacq_history WHERE create_time <=CURRENT_TIMESTAMP - INTERVAL ? Day";
			PreparedStatement pStatement = conn_trd.prepareStatement(sql);
			pStatement.setInt(1, acq_history_time);
			pStatement.execute();
			pStatement.close();
			conn_trd.close();
		} catch (SQLException e) {
			LOGGER.info(" 删除历史记录失败，数据库异常！");
			e.printStackTrace();
		}
	}
	

	/**
	 * 将制定时间之外的数据移到历史库中
	 */
	public static void copynowacqdata(int device_id) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = null;
			String sql = "SELECT * FROM windpower_dataacq_tab  WHERE  device_id=" + device_id
					+ " and create_time <=CURRENT_TIMESTAMP - INTERVAL " + save_time + " MINUTE";
			rs = st_trd.executeQuery(sql);
			while (rs.next()) {
				inserthistorydata(rs.getInt("id"), rs.getInt("device_id"), rs.getTimestamp("create_time"),
						rs.getString("data"));

			}
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			LOGGER.info(" 查询中采集库记录失败，数据库异常！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 插入历史库
	 * 
	 * @param id
	 * @param devcie_id
	 * @param acqtime
	 * @param data
	 */
	public static void inserthistorydata(int id, int devcie_id, Date acqtime, String data) {
		try {
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "insert into  windpower_dataacq_history  (device_id,data,acq_time,data_id) values(" + devcie_id
					+ ",'" + data + "','" + acqtime + "'," + id + ")";
			st_trd.execute(sql);
			deleteacqdata(id);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			LOGGER.info(" 插入历史记录失败，数据库异常！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 删除插入历史库的实时库数据
	 * 
	 * @param id
	 */
	public static void deleteacqdata(int id) {
		try {
			// LOGGER.info(" 正在删除实时库超过"+save_time+"分钟的数据.......");
			Connection conn_trd = JDBConnection.getConnect();
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "delete from  windpower_dataacq_tab WHERE id=" + id;
			st_trd.execute(sql);
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			LOGGER.info(" 删除历史记录失败，数据库异常！");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
