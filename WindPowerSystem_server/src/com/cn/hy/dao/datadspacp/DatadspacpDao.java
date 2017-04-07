package com.cn.hy.dao.datadspacp;

import java.util.List;

import com.cn.hy.pojo.datadspacq.Datadspacp;

public interface DatadspacpDao {
	/**
	 * 获取dsp数据
	 * @param datadspacp
	 * @return
	 */
	List<Datadspacp> selectDatadspacpList(Datadspacp datadspacp);
	/**
	 * 按序号获取dsp数据
	 * @param datadspacp
	 * @return
	 */
	List<Datadspacp> selectDatadspacpGropby(Datadspacp datadspacp);
}
