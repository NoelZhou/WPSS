package com.cn.hy.service.reportstatistics;

import java.util.List;
import java.util.Map;

import com.cn.hy.pojo.reportstatistics.ErrorDataRepair;
import com.cn.hy.pojo.reportstatistics.FormRunData;
import com.cn.hy.pojo.reportstatistics.RowDevice;

/**
 * 报表统计
 * @author HCheng
 *
 */
public interface ReportStatService {
	/**
	 * 获取风机列
	 * @param form_type
	 * @param timename
	 * @return
	 */
	public List<RowDevice> listRowDevice(int form_type,String timename);
	/**
	 * 获取列表主信息
	 */
	public Map<String,List<Integer>> listMainReport(int form_type,String timename);
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
	 * @param id
	 * @param repair_user
	 * @param repair_result
	 * @param repair_state
	 */
	public void updateErrorDataRepair(int id,String repair_user,String repair_result,int repair_state); 
	/**
	 * 获取运行时数据
	 * @param timename
	 * @return
	 */
	public List<FormRunData> listFormRunData(String timename);
}
