package com.zhta.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.zhta.bean.ExcelExport;
import com.zhta.bean.JDBConnectionNoJNDI;
import com.zhta.pojo.FormColnum;
import com.zhta.pojo.FormRunData;

public class testRunData {
	private boolean daystate=false;
	private boolean weekstate=false;
	private boolean monthstate=false;
	private boolean offmonthstate=false;
	private boolean quarterstate=false;
	private boolean offyearstate=false;
	private boolean yearstate=false;
	private static boolean isrun = true;
	public static void stopRunData(){
		isrun=false;
		System.out.println("关闭导出报表线程!");
	}
	public static void main(String[] args) {
		new testRunData().runData();
	}
	
	public void runData() {
		System.out.println("开启自动导出统计报表excel!");
		isrun = true;
		while (isrun) {
			Date d = new Date();
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(d);
			//每月最后的一天
			int maxDayOfMonth = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			//每年最后的一天
			int maxDayOfYear = gc.getActualMaximum(GregorianCalendar.DAY_OF_YEAR);
			//当前月份
			int month = gc.get(Calendar.MONTH)+1;
			//返回一周的第几天，从周一开始计算
			int dayOfWeek = getDayOfWeek(gc);
			//日报表
			if ((gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&daystate==false) {
				readyRunData(d, "日");
				daystate=true;
			}else if(gc.get(GregorianCalendar.HOUR_OF_DAY) != 23){
				daystate=false;
			}
			//周报表
			if ((dayOfWeek == 7)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&weekstate==false) {
				readyRunData(d, "周");
				weekstate=true;
			}else if(gc.get(GregorianCalendar.DAY_OF_WEEK)!=7){
				weekstate=false;
			}
			//月报表
			if ((gc.get(GregorianCalendar.DAY_OF_MONTH) == maxDayOfMonth)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&monthstate==false) {
				readyRunData(d, "月");
				monthstate=true;
			}else if(gc.get(GregorianCalendar.DAY_OF_MONTH) != maxDayOfMonth){
				monthstate=false;
			}
			//年报表
			if ((gc.get(GregorianCalendar.DAY_OF_YEAR) == maxDayOfYear)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&yearstate==false) {
				readyRunData(d, "年");
				yearstate=true;
			}else if(gc.get(GregorianCalendar.DAY_OF_YEAR) != maxDayOfYear){
				yearstate=false;
			}
			//半月报表
			if (maxDayOfMonth == 30 || maxDayOfMonth == 31) {
				if ((gc.get(GregorianCalendar.DAY_OF_MONTH) == 15)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&offmonthstate==false) {
					readyRunData(d, "半月");
					offmonthstate=true;
				}else if((gc.get(GregorianCalendar.DAY_OF_MONTH) == maxDayOfMonth)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&offmonthstate==false){
					readyRunData(d, "半月");
					offmonthstate=true;
				}else if((gc.get(GregorianCalendar.DAY_OF_MONTH) != 15)||(gc.get(GregorianCalendar.DAY_OF_MONTH) != maxDayOfMonth)){
					offmonthstate=false;
				}
			} else if (maxDayOfMonth == 28 || maxDayOfMonth == 29) {
				if ((gc.get(GregorianCalendar.DAY_OF_MONTH) == 14)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&offmonthstate==false) {
					readyRunData(d, "半月");
					offmonthstate=true;
				}else if((gc.get(GregorianCalendar.DAY_OF_MONTH) == maxDayOfMonth)&&(gc.get(GregorianCalendar.HOUR_OF_DAY) == 23)&&offmonthstate==false){
					readyRunData(d, "半月");
					offmonthstate=true;
				}else if((gc.get(GregorianCalendar.DAY_OF_MONTH)!=14)||(gc.get(GregorianCalendar.DAY_OF_MONTH) != maxDayOfMonth)){
					offmonthstate=false;
				}
			}
			// 季度报表
			if(month==3||month==6||month==9||month==12){
				if((gc.get(GregorianCalendar.DAY_OF_MONTH)==maxDayOfMonth)&&(gc.get(GregorianCalendar.HOUR_OF_DAY)==23)&&quarterstate==false){
					readyRunData(d, "季度");
					quarterstate=true;
				}
			}else{
				quarterstate=false;
			}
			// 半年报表
			if(month== 6 || month ==12){
				if((gc.get(GregorianCalendar.DAY_OF_MONTH)==maxDayOfMonth)&&(gc.get(GregorianCalendar.HOUR_OF_DAY)==23)&&offyearstate==false){
					readyRunData(d, "半年");
					offyearstate=true;
				}
			}else{
				offyearstate=false;
			}
		}
	}
	
	public void readyRunData(Date d,String timeStr){
		List<FormColnum> colnumList = listFormColnum();
		List<FormRunData> runList = listFormRunData(timeStr,d);
		new ExcelExport().createexcl(colnumList, runList,timeStr);
	}
	
	//设置周一为一周的开始，第一天，周日为最后一天
	public static int getDayOfWeek(GregorianCalendar gCal){
		gCal.setFirstDayOfWeek(Calendar.MONDAY);
		int tmp = gCal.get(Calendar.DAY_OF_WEEK)-1;
		if(tmp==0){
			tmp=7;
		}
		return tmp;
	}

	public List<FormColnum> listFormColnum() {
		List<FormColnum> formcolnumlist = new ArrayList<FormColnum>();
		try {
			JDBConnectionNoJNDI jdbc = new JDBConnectionNoJNDI();
			Connection conn_trd = jdbc.connection;
			ResultSet rs_trd = null;
			String sql = "select id,errorcode,codesx,form_type,showin from windpower_formcolnumset";
			PreparedStatement pStatement = conn_trd.prepareStatement(sql);
			rs_trd = pStatement.executeQuery();
			
			while (rs_trd.next()) {
				FormColnum fromone = new FormColnum();
				fromone.setId(rs_trd.getInt("id"));
				fromone.setErrorcode(rs_trd.getString("errorcode"));
				fromone.setCodesx(rs_trd.getString("codesx"));
				fromone.setShowin(rs_trd.getInt("showin"));
				formcolnumlist.add(fromone);
			}
			rs_trd.close();
			pStatement.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return formcolnumlist;
	}

	public List<FormRunData> listFormRunData(String timename,Date d) {
		String time_sql = getDateAreaSql(timename,d);
		return updaterundataXiugai(listFormRunDataDao(time_sql), time_sql,d);
	}

	public List<FormRunData> listFormRunDataDao(String time_sql) {
		List<FormRunData> FormRunDatalist = new ArrayList<FormRunData>();
		try {
			JDBConnectionNoJNDI jdbc = new JDBConnectionNoJNDI();
			Connection conn_trd = jdbc.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "	select t1.name device_name,t.device_id,t.id,dwdy,dl,pl,bwyggl,bwwggl,"
					+ " max(fdl)-min(fdl) fdl,sum(djsj) djsj,sum(bwsj) bwsj,sum(gzsj) gzsj,sum(pjlyxss) pjlyxss,sum(blqwgzlyl) blqwgzlyl,max(createtime) createtime,max(createtime) maxtime,min(createtime) mintime,count(1) ct"
					+ " from windpower_formrundata t" + ",windpower_device t1 " + "WHERE t.device_id=t1.id  and "
					+ time_sql + "   group  by device_id ";
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				FormRunData fromone = new FormRunData();
				fromone.setDevice_name(rs_trd.getString("device_name"));
				fromone.setDevice_id(rs_trd.getInt("device_id"));
				fromone.setId(rs_trd.getInt("id"));
				fromone.setDwdy(rs_trd.getInt("dwdy"));
				fromone.setDl(rs_trd.getInt("dl"));
				fromone.setPl(rs_trd.getDouble("pl"));
				fromone.setBwyggl(rs_trd.getInt("bwyggl"));
				fromone.setBwwggl(rs_trd.getInt("bwwggl"));
				fromone.setFdl(rs_trd.getInt("fdl"));
				fromone.setDjsj(rs_trd.getString("djsj"));
				fromone.setBwsj(rs_trd.getString("bwsj"));
				fromone.setGzsj(rs_trd.getString("gzsj"));
				fromone.setPjlyxss(rs_trd.getFloat("pjlyxss"));
				fromone.setBlqwgzlyl(rs_trd.getString("blqwgzlyl"));
				fromone.setMaxtime(rs_trd.getString("maxtime"));
				fromone.setMintime(rs_trd.getString("mintime"));
				FormRunDatalist.add(fromone);
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return FormRunDatalist;
	}

	public FormRunData getdevicerundata(int device_id, String time_sql) {
		FormRunData fromone = new FormRunData();
		try {
			JDBConnectionNoJNDI jdbc = new JDBConnectionNoJNDI();
			Connection conn_trd = jdbc.connection;
			ResultSet rs_trd = null;
			Statement st_trd = conn_trd.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String sql = "select *,1 ct from windpower_formrundata	where " + time_sql + " and device_id =" + device_id
					+ "	order by createtime desc limit 0,1 ";
			rs_trd = st_trd.executeQuery(sql);
			while (rs_trd.next()) {
				fromone.setDevice_name(rs_trd.getString("device_name"));
				fromone.setDevice_id(rs_trd.getInt("device_id"));
				fromone.setId(rs_trd.getInt("id"));
				fromone.setDwdy(rs_trd.getInt("dwdy"));
				fromone.setDl(rs_trd.getInt("dl"));
				fromone.setPl(rs_trd.getDouble("pl"));
				fromone.setBwyggl(rs_trd.getInt("bwyggl"));
				fromone.setBwwggl(rs_trd.getInt("bwwggl"));
				fromone.setCreatetime(rs_trd.getString("createtime"));
			}
			rs_trd.close();
			st_trd.close();
			conn_trd.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return fromone;
	}

	/**
	 * 获取日期的SQL语句
	 */
	public String getDateAreaSql(String timename,Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date_area_sql = "";
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		if ("日".equals(timename)) {// 天
			date_area_sql = "to_days(createtime) = to_days(now())";
		} else if ("周".equals(timename)) {// 周(从周一开始，周日结束)
			date_area_sql = "YEARWEEK(date_format(createtime,'%Y-%m-%d'),1) = YEARWEEK(now(),1)";
		} else if ("半月".equals(timename)) {// 半月
			gc.set(GregorianCalendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			String firstday = format.format(gc.getTime());//本月第一天
			int maxDay = gc.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			gc.set(GregorianCalendar.DAY_OF_MONTH, maxDay);
			String lastday = format.format(gc.getTime());
			String midTimeStr="";
			if(maxDay == 30 || maxDay == 31){
				gc.set(GregorianCalendar.DAY_OF_MONTH, 15);
				midTimeStr = format.format(gc.getTime());
			} else if(maxDay == 28 || maxDay == 29){
				gc.set(GregorianCalendar.DAY_OF_MONTH, 14);
				midTimeStr = format.format(gc.getTime());
			}
			Calendar cal = Calendar.getInstance();
			int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
			if (dayOfMonth == 15||dayOfMonth == 14) {
				date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + firstday + "') and TO_DAYS('"+midTimeStr+"')";
			}else{
				date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + midTimeStr + "') and TO_DAYS('"+lastday+"')";
			}
		} else if ("月".equals(timename)) {// 月
			date_area_sql = "DATE_FORMAT(createtime,'%Y%m') = DATE_FORMAT(CURDATE( ) ,'%Y%m')";
		} else if ("季度".equals(timename)) {// 季度
			date_area_sql = "QUARTER(createtime)=QUARTER(now())";
		} else if ("半年".equals(timename)) {// 半年
			int year = gc.get(GregorianCalendar.YEAR);
			int  month = gc.get(GregorianCalendar.MONTH)+1;
			String firstdayStr = year + "-01-01";
			String midyeardayStr = year+"-06-30";
			String enddayStr = year + "-12-31";
			if (month==6) {
				date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + firstdayStr + "') and TO_DAYS('"+ midyeardayStr+ "')";
			}else if(month == 12){
				date_area_sql = "TO_DAYS(createtime) BETWEEN TO_DAYS('" + midyeardayStr + "') and TO_DAYS('"+ enddayStr + "')";
			}
		} else if ("年".equals(timename)) {// 年
			date_area_sql = "YEAR(createtime)=YEAR(NOW())";
		}
		return date_area_sql;
	}

	private List<FormRunData> updaterundataXiugai(List<FormRunData> listFormRunData,String time_sql,Date date) {
		List<FormRunData> runlist=new ArrayList<FormRunData>();

		DecimalFormat df00  = new DecimalFormat("###0.00");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		int DATE=gc.get(GregorianCalendar.DATE);
		int HOUR_OF_DAY=gc.get(GregorianCalendar.HOUR_OF_DAY);
		int hours=HOUR_OF_DAY;//运行小时数
		int days=DATE;//运行天数
		for(int i=0;i<listFormRunData.size();i++){
			 int  device_id=0;
		    FormRunData formrundataone=listFormRunData.get(i);
		    device_id=formrundataone.getDevice_id();
		    FormRunData rundataone=getdevicerundata(device_id, time_sql);
		    
			//指定时间内采集了多少数据
		    formrundataone.setDl(rundataone.getDl());
		    formrundataone.setDwdy(rundataone.getDwdy());
		    formrundataone.setPl(rundataone.getPl());
		    formrundataone.setBwyggl(rundataone.getBwyggl());
		    formrundataone.setBwwggl(rundataone.getBwwggl());
		    //并网时间
			double bwsjtmp=Double.parseDouble(formrundataone.getBwsj())/3600;
			String bwsjstr=df00.format(bwsjtmp);
			formrundataone.setBwsj(bwsjstr);
			//待机时间
			double djsjtmp=Double.parseDouble(formrundataone.getDjsj())/3600;
			String djsjstr=df00.format(djsjtmp);
			formrundataone.setDjsj(djsjstr);
			//故障时间
			double gzsjtmp=Double.parseDouble(formrundataone.getGzsj())/3600;
			String gzsjstr=df00.format(gzsjtmp);
			formrundataone.setGzsj(gzsjstr);
			
			/**
			 * 计算运行小时数和天数
			 */
			String starttimestr=formrundataone.getMintime();
			String endtimestr=formrundataone.getMaxtime();
		    try {
				long starttime=df.parse(starttimestr).getTime();
				long endtime=df.parse(endtimestr).getTime();
				long sjtime=endtime-starttime;
				int length=3600000;
			    hours = (int) Math.ceil(sjtime / length)+1;
				int numy = (int) Math.ceil(sjtime % length);
				if(numy>1800000){
					hours++; 
				}
				
				Date startDate = dateFormat.parse(starttimestr);
				Date endDate= dateFormat.parse(endtimestr);
				days = (int)((endDate.getTime()-startDate.getTime())/86400000)+1;
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//变流器无故障利用率:(统计时间-故障时间)/统计时间,百分数标量.
			double blqwgzlyltmp=Double.parseDouble(formrundataone.getBlqwgzlyl())/hours;
			String blqwgzlylstr=df00.format(blqwgzlyltmp*100);
			formrundataone.setBlqwgzlyl(blqwgzlylstr+"%");
			
			//平均发电功率:单位为kw,发电量/统计时间(h)
			double pjfdgltmp=(double)formrundataone.getFdl()/hours;
			String pjfdglstr=df00.format(pjfdgltmp);
			formrundataone.setPjfdgl(Float.parseFloat(pjfdglstr));
			
			//平均利用小时数:(统计时间-故障时间)/统计天数
			double pjlyxsstmp=(double)formrundataone.getPjlyxss()/3600;//转换成小时对象
			
			String pjlyxssstr=df00.format(pjlyxsstmp/days);//这周的第几天
			formrundataone.setPjlyxss(Float.parseFloat(pjlyxssstr));
			runlist.add(i, formrundataone);
		}
	
		return runlist;
	}
}
