package com.cn.hy.dao.reportstatistics;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.reportstatistics.ErrorDataRepair;
import com.cn.hy.pojo.reportstatistics.FormRunData;
import com.cn.hy.pojo.reportstatistics.RowDevice;
import com.cn.hy.pojo.reportstatistics.RowErrorData;

/**
 * 报表统计
 * @author HCheng
 *
 */
public interface ReportStatDao {
	/**
	 * 获取风机列
	 * @param rowDevice
	 * @return
	 */
	public List<RowDevice> listRowDevice(RowDevice rowDevice);
	/**
	 * 获取故障栏
	 * @param rowErrorData
	 * @return
	 */
	public List<RowErrorData> listRowErrorData(RowErrorData rowErrorData);
	/**
	 * 获取故障、告警数量
	 * @param rowErrorData
	 * @return
	 */
	public int getErrorNum(RowErrorData rowErrorData);
	/**
	 * 获取故障派单列表
	 * @param repair_state
	 * @return
	 */
	public List<ErrorDataRepair> listErrorDataRepair(int repair_state);
	/**
	 * 获取派单
	 * @param id
	 * @return
	 */
	public ErrorDataRepair getErrorDataRepair(int id);
	/**
	 * 修改派单状态
	 * @param errorDataRepair
	 */
	public void updateErrorDataRepair(ErrorDataRepair errorDataRepair);
	/**
	 * 获取运行时数据
	 * @param formRunData
	 * @return
	 */
	public List<FormRunData> listFormRunData(FormRunData formRunData);
	/**
	 * 获取设备统计报表中非统计类最新数据
	 * @param device_name
	 * @return
	 */
	public FormRunData getdevicerundata(@Param(value="device_id")int device_id,@Param(value="time_sql")String time_sql);
}
