package com.cn.hy.serviceImpl.reportstatistics;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cn.hy.dao.reportstatistics.ReportStatDao;
import com.cn.hy.dao.serviceset.ReportSetDao;
import com.cn.hy.pojo.reportstatistics.ErrorDataRepair;
import com.cn.hy.pojo.reportstatistics.FormRunData;
import com.cn.hy.pojo.reportstatistics.RowDevice;
import com.cn.hy.pojo.reportstatistics.RowErrorData;
import com.cn.hy.service.reportstatistics.ReportStatService;

@Service("ReportStatServiceImpl")
public class ReportStatServiceImpl implements ReportStatService {
	@Resource
	private ReportStatDao reportStatDao;
	@Resource
	private ReportSetDao reportSetDao;

	/**
	 * 获取风机列
	 */
	@Override
	public List<RowDevice> listRowDevice(int form_type, String timename) {
		String error_types = "";
		if (form_type == 0) { // 故障告警
			error_types = "1,2,3,4";
		} else if (form_type == 1) { //
			error_types = "5";
		}
		String date_area_sql = getDateAreaSql(timename);
		RowDevice rowDevice = new RowDevice();
		rowDevice.setError_types(error_types);
		rowDevice.setDate_area_sql(date_area_sql);
		return reportStatDao.listRowDevice(rowDevice);
	}

	/**
	 * 获取SQL语句
	 * @param timename
	 */
	public String getDateAreaSql(String timename) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date_area_sql = "";
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		// 每天0点兑时
		int YEAR = gc.get(GregorianCalendar.YEAR);
		int MONTH = gc.get(GregorianCalendar.MONTH) + 1;
		int DATE = gc.get(GregorianCalendar.DATE);
		String mintimestr = YEAR + "-" + MONTH + "-" + DATE + " 00";
		String minstr = " 00";
		if ("日".equals(timename)) {// 天
			date_area_sql = "to_days(createtime) = to_days(now()) and createtime NOT LIKE CONCAT('%" + mintimestr
					+ "%')";
		} else if ("周".equals(timename)) {// 周
			Calendar cal_1 = Calendar.getInstance();
			cal_1.add(Calendar.WEDNESDAY, 0);
			cal_1.set(Calendar.DAY_OF_WEEK, 1);// 设置为1号,当前日期既为本月第一天
			String firstday = format.format(cal_1.getTime());
			mintimestr = firstday + minstr;
			date_area_sql = "YEARWEEK(date_format(createtime,'%Y-%m-%d'),1) = YEARWEEK(now(),1) and createtime NOT LIKE CONCAT('%"
					+ mintimestr + "%')";
		} else if ("半月".equals(timename)) {// 半月
			try {
				Calendar cal_1 = Calendar.getInstance();
				cal_1.add(Calendar.MONTH, 0);
				cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
				String firstday = format.format(cal_1.getTime());
				int num = cal_1.getActualMaximum(Calendar.DAY_OF_MONTH);
				// 半个月的日期
				String midday = "";
				Date midDate = null;
				if (num == 30 || num == 31) {
					cal_1.add(Calendar.DAY_OF_YEAR, 14);
					midday = format.format(cal_1.getTime());
					midDate = format.parse(midday);
				} else if (num == 28 || num == 29) {
					cal_1.add(Calendar.DAY_OF_YEAR, 13);
					midday = format.format(cal_1.getTime());
					midDate = format.parse(midday);
				}
				// 本月最后一天
				cal_1.set(Calendar.DAY_OF_MONTH, cal_1.getActualMaximum(Calendar.DAY_OF_MONTH));
				String lastday = format.format(cal_1.getTime());
				Date nowDate = new Date();
				if (nowDate.before(midDate)) {
					mintimestr = firstday + minstr;
					date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + firstday + "') and TO_DAYS('" + midday
							+ "') and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
				} else if (nowDate.after(midDate)) {
					mintimestr = midday + minstr;
					date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + midday + "') and TO_DAYS('" + lastday
							+ "') and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("月".equals(timename)) {// 月
			Calendar cal_1 = Calendar.getInstance();
			cal_1.add(Calendar.MONTH, 0);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			String firstday = format.format(cal_1.getTime());
			mintimestr = firstday + minstr;
			date_area_sql = "DATE_FORMAT(createtime,'%Y%m') = DATE_FORMAT(CURDATE( ) ,'%Y%m') and createtime NOT LIKE CONCAT('%"
					+ mintimestr + "%')";
		} else if ("季度".equals(timename)) {// 季度
			Calendar cal_1 = Calendar.getInstance();
			cal_1.setTime(new Date());
			int month = getQuarterInMonth(cal_1.get(Calendar.MONTH), false);
			cal_1.set(Calendar.MONTH, month);
			cal_1.set(Calendar.DAY_OF_MONTH, 1);
			String firstday = format.format(cal_1.getTime());
			mintimestr = firstday + minstr;
			date_area_sql = "QUARTER(createtime)=QUARTER(now()) and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
		} else if ("半年".equals(timename)) {// 半年
			try {
				Calendar now = Calendar.getInstance();
				int year = now.get(Calendar.YEAR);
				String midyeardayStr = year + "-06-30";
				Date midyearday = format.parse(midyeardayStr);
				String nowdate = format.format(new Date());
				Date nowyearday = format.parse(nowdate);
				String firstdayStr = year + "-01-01";
				String enddayStr = year + "-12-31";

				if (nowyearday.before(midyearday)) {
					mintimestr = firstdayStr + minstr;
					date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + firstdayStr + "') and TO_DAYS('"
							+ midyeardayStr + "') and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
				} else if (nowyearday.after(midyearday)) {
					mintimestr = midyeardayStr + minstr;
					date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + midyeardayStr + "') and TO_DAYS('"
							+ enddayStr + "') and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("年".equals(timename)) {// 年
			Calendar cal_1 = Calendar.getInstance();
			cal_1.set(Calendar.DAY_OF_YEAR, 1);
			String firstday = format.format(cal_1.getTime());
			mintimestr = firstday + minstr;
			date_area_sql = "YEAR(createtime)=YEAR(NOW()) and createtime NOT LIKE CONCAT('%" + mintimestr + "%')";
		}
		return date_area_sql;
	}

	// 返回第几个月份，不是几月
	// 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
	private static int getQuarterInMonth(int month, boolean isQuarterStart) {
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart) {
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 2 && month <= 4)
			return months[0];
		else if (month >= 5 && month <= 7)
			return months[1];
		else if (month >= 8 && month <= 10)
			return months[2];
		else
			return months[3];
	}

	/**
	 * 获取列表主信息
	 */
	@Override
	public Map<String, List<Integer>> listMainReport(int form_type, String timename) {
		String error_types = "";
		if (form_type == 0) { // 故障
			error_types = "1,2,3,4";
		} else if (form_type == 1) { // 系统告警
			error_types = "5";
		}
		String date_area_sql = getDateAreaSql(timename);
		RowDevice rowDevice = new RowDevice();
		rowDevice.setError_types(error_types);
		rowDevice.setDate_area_sql(date_area_sql);
		RowErrorData rowErrorData = new RowErrorData();
		rowErrorData.setError_types(error_types);
		rowErrorData.setDate_area_sql(date_area_sql);
		// 获取风机列
		List<RowDevice> rowList = reportStatDao.listRowDevice(rowDevice);
		// 获取故障栏
		List<RowErrorData> rowErrorList = reportStatDao.listRowErrorData(rowErrorData);
		Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
		int errornum = 0;
		for (int i = 0; i < rowErrorList.size(); i++) {
			List<Integer> numlist = new ArrayList<Integer>();
			for (int j = 0; j < rowList.size(); j++) {
				rowErrorData.setDevice_name(rowList.get(j).getDevice_name());
				rowErrorData.setError_column(rowErrorList.get(i).getError_column());
				rowErrorData.setError_name(rowErrorList.get(i).getError_name());
				rowErrorData.setError_types(error_types);
				rowErrorData.setDate_area_sql(date_area_sql);
				// 获取故障、告警数量
				errornum = reportStatDao.getErrorNum(rowErrorData);
				numlist.add(errornum);
			}
			map.put(rowErrorList.get(i).getError_name(), numlist);
		}

		return map;
	}

	/**
	 * 获取故障派单列表
	 */
	@Override
	public List<ErrorDataRepair> listErrorDataRepair(int repair_state) {
		List<ErrorDataRepair> errorRepairList = reportStatDao.listErrorDataRepair(repair_state);
		for (int i = 0; i < errorRepairList.size(); i++) {
			errorRepairList.get(i).setEndtime(afterNHours(errorRepairList.get(i).getCometime(), 48));
		}
		return errorRepairList;
	}

	/**
	 * 获取派单
	 */
	@Override
	public ErrorDataRepair getErrorDataRepair(int id) {
		ErrorDataRepair errorDataRepair = reportStatDao.getErrorDataRepair(id);
		errorDataRepair.setEndtime(afterNHours(errorDataRepair.getCometime(), 48));
		return errorDataRepair;
	}

	/**
	 * 修改派单状态
	 */
	@Override
	public void updateErrorDataRepair(int id, String repair_user, String repair_result, int repair_state) {
		ErrorDataRepair errorDataRepair = new ErrorDataRepair();
		errorDataRepair.setId(id);
		errorDataRepair.setRepair_user(repair_user);
		errorDataRepair.setRepair_result(repair_result);
		errorDataRepair.setRepair_state(repair_state);
		reportStatDao.updateErrorDataRepair(errorDataRepair);
	}

	/**
	 * 时间加N小时后的时间
	 * 
	 * @param time
	 * @param hour
	 * @return
	 */
	public String afterNHours(String time, int hour) {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		String hourStr = "";
		try {
			Date date = sdf.parse(time);
			Calendar ca = Calendar.getInstance();
			ca.setTime(date);
			ca.add(Calendar.HOUR_OF_DAY, hour);
			hourStr = sdf.format(ca.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hourStr;
	}

	/**
	 * 获取运行时数据
	 */
	@Override
	public List<FormRunData> listFormRunData(String timename) {
		FormRunData formRunData = new FormRunData();
		String time_sql = getDateAreaSql(timename);
		formRunData.setTime_sql(time_sql);
		return updaterundataXiugai(reportStatDao.listFormRunData(formRunData), time_sql);
		// return reportStatDao.listFormRunData(formRunData);
	}

	/**
	 * 更新运行报表数据(12.2原版)
	 * 
	 * @param listFormRunData
	 * @param time_sql
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<FormRunData> updaterundata(List<FormRunData> listFormRunData, String time_sql) {
		List<FormRunData> runlist = new ArrayList<FormRunData>();

		DecimalFormat df00 = new DecimalFormat("###0.00");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		// 每天0点兑时
		int YEAR = gc.get(GregorianCalendar.YEAR);
		int MONTH = gc.get(GregorianCalendar.MONTH) + 1;
		int DATE = gc.get(GregorianCalendar.DATE);
		int MINUTE = gc.get(GregorianCalendar.MINUTE);
		int SECOND = gc.get(GregorianCalendar.SECOND);
		int HOUR_OF_DAY = gc.get(GregorianCalendar.HOUR_OF_DAY);

		int hour = HOUR_OF_DAY;
		for (int i = 0; i < listFormRunData.size(); i++) {
			int device_id = 0;
			FormRunData formrundataone = listFormRunData.get(i);
			String starttimestr = formrundataone.getMintime();
			String endtimestr = formrundataone.getMaxtime();
			try {
				long starttime = df.parse(starttimestr).getTime();
				long endtime = df.parse(endtimestr).getTime();
				long sjtime = endtime - starttime;
				int length = 3600000;
				hour = (int) Math.ceil(sjtime / length) + 1;
				int numy = (int) Math.ceil(sjtime % length);
				if (numy > 1800000) {
					hour++;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			device_id = formrundataone.getDevice_id();
			FormRunData rundataone = reportStatDao.getdevicerundata(device_id, time_sql);
			// 指定时间内采集了多少数据
			formrundataone.setDl(rundataone.getDl());
			formrundataone.setDwdy(rundataone.getDwdy());
			formrundataone.setPl(rundataone.getPl());
			formrundataone.setBwyggl(rundataone.getBwyggl());
			formrundataone.setBwwggl(rundataone.getBwwggl());
			// int hour=formrundataone.getCt();
			// int hour=HOUR_OF_DAY;
			double bwsjtmp = Double.parseDouble(formrundataone.getBwsj()) / 3600;
			String bwsjstr = df00.format(bwsjtmp);
			formrundataone.setBwsj(bwsjstr);

			double djsjtmp = Double.parseDouble(formrundataone.getDjsj()) / 3600;
			String djsjstr = df00.format(djsjtmp);
			formrundataone.setDjsj(djsjstr);

			double gzsjtmp = Double.parseDouble(formrundataone.getGzsj()) / 3600;
			String gzsjstr = df00.format(gzsjtmp);
			formrundataone.setGzsj(gzsjstr);
			// 变流器无故障利用率:(统计时间-故障时间)/统计时间,百分数标量.
			double blqwgzlyltmp = Double.parseDouble(formrundataone.getBlqwgzlyl()) / hour;
			String blqwgzlylstr = df00.format(blqwgzlyltmp * 100);
			formrundataone.setBlqwgzlyl(blqwgzlylstr + "%");
			// 平均发电功率:单位为kw,发电量/统计时间(h)
			double pjfdgltmp = (double) formrundataone.getFdl() / hour;
			String pjfdglstr = df00.format(pjfdgltmp);
			formrundataone.setPjfdgl(Float.parseFloat(pjfdglstr));
			int hit = hour - HOUR_OF_DAY;
			int day = hit / 24 + 1;
			int dayys = hit % 24;
			if (dayys > 0) {
				day++;
			}
			// 平均利用小时数:(统计时间-故障时间)/统计天数
			double pjlyxsstmp = (double) formrundataone.getPjlyxss() / 3600;// 转换成小时对象
			String pjlyxssstr = df00.format(pjlyxsstmp / day);// 这周的第几天
			formrundataone.setPjlyxss(Float.parseFloat(pjlyxssstr));
			runlist.add(i, formrundataone);
			;
		}

		return runlist;
	}

	/**
	 * 更新运行报表数据(平均利用小时修改版)
	 * 
	 * @param listFormRunData
	 * @param time_sql
	 * @return
	 */
	private List<FormRunData> updaterundataXiugai(List<FormRunData> listFormRunData, String time_sql) {
		List<FormRunData> runlist = new ArrayList<FormRunData>();
		DecimalFormat df00 = new DecimalFormat("###0.00");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d = new Date();
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(d);
		int DATE = gc.get(GregorianCalendar.DATE);
		int HOUR_OF_DAY = gc.get(GregorianCalendar.HOUR_OF_DAY);
		int hours = HOUR_OF_DAY;// 运行小时数
		int days = DATE;// 运行天数
		for (int i = 0; i < listFormRunData.size(); i++) {
			int device_id = 0;
			FormRunData formrundataone = listFormRunData.get(i);
			device_id = formrundataone.getDevice_id();
			FormRunData rundataone = reportStatDao.getdevicerundata(device_id, time_sql);
			// 指定时间内采集了多少数据
			formrundataone.setDl(rundataone.getDl());
			formrundataone.setDwdy(rundataone.getDwdy());
			formrundataone.setPl(rundataone.getPl());
			formrundataone.setBwyggl(rundataone.getBwyggl());
			formrundataone.setBwwggl(rundataone.getBwwggl());
			// 并网时间
			double bwsjtmp = Double.parseDouble(formrundataone.getBwsj()) / 3600;
			String bwsjstr = df00.format(bwsjtmp);
			formrundataone.setBwsj(bwsjstr);
			// 待机时间
			double djsjtmp = Double.parseDouble(formrundataone.getDjsj()) / 3600;
			String djsjstr = df00.format(djsjtmp);
			formrundataone.setDjsj(djsjstr);
			// 故障时间
			double gzsjtmp = Double.parseDouble(formrundataone.getGzsj()) / 3600;
			String gzsjstr = df00.format(gzsjtmp);
			formrundataone.setGzsj(gzsjstr);
			/**
			 * 计算运行小时数和天数
			 */
			String starttimestr = formrundataone.getMintime();
			String endtimestr = formrundataone.getMaxtime();
			try {
				long starttime = df.parse(starttimestr).getTime();
				long endtime = df.parse(endtimestr).getTime();
				long sjtime = endtime - starttime;
				int length = 3600000;
				hours = (int) Math.ceil(sjtime / length) + 1;
				int numy = (int) Math.ceil(sjtime % length);
				if (numy > 1800000) {
					hours++;
				}
				Date startDate = dateFormat.parse(starttimestr);
				Date endDate = dateFormat.parse(endtimestr);
				days = (int) ((endDate.getTime() - startDate.getTime()) / 86400000) + 1;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 变流器无故障利用率:(统计时间-故障时间)/统计时间,百分数标量.
			double blqwgzlyltmp = Double.parseDouble(formrundataone.getBlqwgzlyl()) / hours;
			String blqwgzlylstr = df00.format(blqwgzlyltmp * 100);
			formrundataone.setBlqwgzlyl(blqwgzlylstr + "%");
			// 平均发电功率:单位为kw,发电量/统计时间(h)
			double pjfdgltmp = (double) formrundataone.getFdl() / hours;
			String pjfdglstr = df00.format(pjfdgltmp);
			formrundataone.setPjfdgl(Float.parseFloat(pjfdglstr));
			// 平均利用小时数:(统计时间-故障时间)/统计天数
			double pjlyxsstmp = (double) formrundataone.getPjlyxss() / 3600;// 转换成小时对象
			String pjlyxssstr = df00.format(pjlyxsstmp / days);// 这周的第几天
			formrundataone.setPjlyxss(Float.parseFloat(pjlyxssstr));
			runlist.add(i, formrundataone);
		}

		return runlist;
	}

}
