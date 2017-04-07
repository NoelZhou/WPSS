package com.cn.hy.serviceImpl.datadspacp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.datadspacp.DatadspacpDao;
import com.cn.hy.pojo.datadspacq.Datadspacp;
import com.cn.hy.service.datadspacq.DatadspacqService;

@Service("DatadspacqService")
public class DatadspacpServiceImpl implements DatadspacqService{
	@Resource 
	private  DatadspacpDao datadspacpDao;
	/**
	 * 获取dsp数据
	 */
	@Override
	public List<Datadspacp> selectDatadspacpList(Datadspacp datadspacp) {
		return  datadspacpDao.selectDatadspacpList(datadspacp);
	}
	/**
	 * 按序号获取dsp数据
	 */
	@Override
	public List<Datadspacp> selectDatadspacpGropby(Datadspacp datadspacp) {
		return datadspacpDao.selectDatadspacpGropby(datadspacp);
	}

}
