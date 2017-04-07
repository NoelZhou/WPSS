package com.cn.hy.service.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.FormColnum;
import com.cn.hy.pojo.serviceset.FormTimeSet;
/**
 * 报表设置
 * @author HCheng
 *
 */
public interface ReportSetService {
	/**
	 * 获取报表时间
	 * @param formTimeSet
	 * @return
	 */
	public List<FormTimeSet> listFormTimeSet(FormTimeSet formTimeSet); 
	/**
	 * 新增报表时间
	 * @param formTimeSet
	 */
	public void saveFormTimeSet(FormTimeSet formTimeSet);
	/**
	 * 修改报表时间显示与否
	 * @param formTimeSet
	 */
	public void updateShowin(FormTimeSet formTimeSet);
	/**
	 * 获取字段信息
	 * @param formColnum
	 * @return
	 */
	public List<FormColnum> listFormColnum(FormColnum formColnum);
	/**
	 * 修改字段显示与否
	 * @param formColnum
	 */
	public void updateFormColnum(FormColnum formColnum);
}
