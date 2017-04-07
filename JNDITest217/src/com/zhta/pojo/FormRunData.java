package com.zhta.pojo;
/**
 * 运行时数据
 * @author HCheng
 *
 */
public class FormRunData {
	/*运行日志报表展示数据： 
	电网电压/电流/频率/并网有功功率/并网无功功率,发电量,待机时间,并网时间,故障时间，平均利用小时数，平均发电功率,变流器无故障利用率 
	双馈：
	电网电压:指电网线电压  协议地址501
	电流:指机组输出总有功电流  协议地址503
	频率:指电网频率  协议地址85
	并网有功功率:指机组输出有功功率 协议地址490
	并网无功功率:指机组输出无功功率 协议地址491
	发电量:发电量累计由变流器完成,不同的报表根据相应时刻的发电量数据进行减法操作即可;例如日报,将当日24时的发电量数据减去0时的数据即是当日的机组发电量;
	发电量计算:协议地址515和516的数据合并为32位整形数即是发电量.
	待机时间:运行报表统计时间-并网时间-故障时间;
	并网时间:参见风场概览的运行状态条件
	故障时间:参见风场概览的故障判断条件
	平均利用小时数:(统计时间-故障时间)/统计天数
	平均发电功率:单位为kw,发电量/统计时间(h)
	变流器无故障利用率:(统计时间-故障时间)/统计时间,百分数标量.*/
	private int id;
	private int dwdy;//电网电压
	private int dl;//电流
	private double pl;//频率
	private int bwyggl;//并网有功功率
	private int bwwggl;//并网无功功率
	private int fdl;//发电量
	private String djsj;//待机时间
	private String bwsj;//并网时间
	private String gzsj;//故障时间
	private Float pjlyxss;//平均利用小时数
	private Float pjfdgl;//平均发电功率
	private String blqwgzlyl;//变流器无故障利用率
	private String createtime;//创建时间
	private String device_name;//设备名字
	private String time_sql;//时间段
	private int device_id;
	private int ct;
	private String maxtime;
	private String mintime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDwdy() {
		return dwdy;
	}
	public void setDwdy(int dwdy) {
		this.dwdy = dwdy;
	}
	public int getDl() {
		return dl;
	}
	public void setDl(int dl) {
		this.dl = dl;
	}
	public double getPl() {
		return pl;
	}
	public void setPl(double pl) {
		this.pl = pl;
	}
	public int getBwyggl() {
		return bwyggl;
	}
	public void setBwyggl(int bwyggl) {
		this.bwyggl = bwyggl;
	}
	public int getBwwggl() {
		return bwwggl;
	}
	public void setBwwggl(int bwwggl) {
		this.bwwggl = bwwggl;
	}
	public int getFdl() {
		return fdl;
	}
	public void setFdl(int fdl) {
		this.fdl = fdl;
	}
	public String getDjsj() {
		return djsj;
	}
	public void setDjsj(String djsj) {
		this.djsj = djsj;
	}
	public void fdl(String djsj) {
		this.djsj = djsj;
	}
	public String getBwsj() {
		return bwsj;
	}
	public void setBwsj(String bwsj) {
		this.bwsj = bwsj;
	}
	public String getGzsj() {
		return gzsj;
	}
	public void setGzsj(String gzsj) {
		this.gzsj = gzsj;
	}
	public Float getPjlyxss() {
		return pjlyxss;
	}
	public void setPjlyxss(Float pjlyxss) {
		this.pjlyxss = pjlyxss;
	}
	public Float getPjfdgl() {
		return pjfdgl;
	}
	public void setPjfdgl(Float pjfdgl) {
		this.pjfdgl = pjfdgl;
	}
	public String getBlqwgzlyl() {
		return blqwgzlyl;
	}
	public void setBlqwgzlyl(String blqwgzlyl) {
		this.blqwgzlyl = blqwgzlyl;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getDevice_name() {
		return device_name;
	}
	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}
	public String getTime_sql() {
		return time_sql;
	}
	public void setTime_sql(String time_sql) {
		this.time_sql = time_sql;
	}
	public int getCt() {
		return ct;
	}
	public void setCt(int ct) {
		this.ct = ct;
	}
	public int getDevice_id() {
		return device_id;
	}
	public void setDevice_id(int device_id) {
		this.device_id = device_id;
	}
	public String getMaxtime() {
		return maxtime;
	}
	public void setMaxtime(String maxtime) {
		this.maxtime = maxtime;
	}
	public String getMintime() {
		return mintime;
	}
	public void setMintime(String mintime) {
		this.mintime = mintime;
	}
	
	
}
