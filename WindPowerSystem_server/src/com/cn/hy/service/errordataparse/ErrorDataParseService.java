package com.cn.hy.service.errordataparse;


import java.util.List;

import com.cn.hy.pojo.errordataparse.ErrorDataParse;
import com.cn.hy.pojo.errortype.ErrorType;
import com.cn.hy.pojo.viewFaultHistory.ViewFaultHistory;

public interface ErrorDataParseService {
	/**
	 * 查询所有
	 * @param device_name
	 * @param errorname
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<ErrorDataParse> selectErrorDataParse(String device_name,String errorname,String start_time,String end_time);
	
	/**
	 * 查询errortype表中的所有故障名称
	 * @return
	 */
	public List<ErrorType> selectErrorType();
	
	/**
	 * 查看故障历史
	 * @param id
	 * @return
	 */
	public List<ViewFaultHistory> viewFaultHistoryListAll(int id);
}
