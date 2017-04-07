package com.cn.hy.dao.errordataparse;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.errordataparse.ErrorDataParse;

public interface ErrorDataParseDao {
	
	/**
	 * 查询所有
	 * @param device_name
	 * @param errorname
	 * @param start_time
	 * @param end_time
	 * @return
	 */
	public List<ErrorDataParse> selectErrorDataParse(@Param(value="device_name") String device_name,@Param(value="errorname") String errorname,@Param(value="start_time") String start_time,@Param(value="end_time") String end_time);
/*	public List<ErrorDataParse> selectErrorDataParse(String device_name, String errorname, String start_time, String end_time);
*/}
