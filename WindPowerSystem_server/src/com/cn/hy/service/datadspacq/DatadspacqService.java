package com.cn.hy.service.datadspacq;

import java.util.List;

import com.cn.hy.pojo.datadspacq.Datadspacp;

public interface DatadspacqService {
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
