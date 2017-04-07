package com.cn.hy.serviceImpl.serviceset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.serviceset.ReportSetDao;
import com.cn.hy.pojo.serviceset.FormColnum;
import com.cn.hy.pojo.serviceset.FormTimeSet;
import com.cn.hy.service.serviceset.ReportSetService;
@Service("ReportSetServiceImpl")
public class ReportSetServiceImpl implements ReportSetService {
	@Resource
	private ReportSetDao reportSetDao;
	/**
	 * 获取报表时间
	 */
	@Override
	public List<FormTimeSet> listFormTimeSet(FormTimeSet formTimeSet) {
		return reportSetDao.listFormTimeSet(formTimeSet);
	}
	/**
	 * 新增报表时间
	 */
	@Override
	public void saveFormTimeSet(FormTimeSet formTimeSet) {
		reportSetDao.saveFormTimeSet(formTimeSet);
	}
	/**
	 * 修改报表时间显示与否
	 */
	@Override
	public void updateShowin(FormTimeSet formTimeSet) {
		reportSetDao.updateShowin(formTimeSet);
	}
	/**
	 * 获取字段信息
	 */
	@Override
	public List<FormColnum> listFormColnum(FormColnum formColnum) {
		return reportSetDao.listFormColnum(formColnum);
	}
	/**
	 * 修改字段显示与否
	 */
	@Override
	public void updateFormColnum(FormColnum formColnum) {
		reportSetDao.updateFormColnum(formColnum);
	}

}
