package com.cn.hy.serviceImpl.deviceSystemService;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.devicesystem.ParameterServiceDao;
import com.cn.hy.pojo.modbustcp.Parameter;
import com.cn.hy.service.deviceSystemService.ParameterService;
@Service("ParameterServiceImpl")
public class ParameterImpl implements ParameterService {
	@Resource
	private ParameterServiceDao parameterServiceDao;
	/**
	 * 根据协议类型和父节点获取参数监控数组
	 */
	@Override
	public List<Parameter> selectbytype(int parent_type,int modbustcp_type ) {
		//根据页面传递的type,来查询数据多条数据
		List<Parameter> paramete=parameterServiceDao.selectbytype(parent_type,modbustcp_type);
		return paramete;
	}
	/**
	 * 获取同数组下所有协议地址
	 */
	@Override
	public List<Parameter> selectbychild(Integer id,int modbustcp_type) {
		List<Parameter> paramete=parameterServiceDao.selectbychild(id,modbustcp_type);
		return paramete;
	}
	/**
	 * 获取设备基本信息
	 */
	@Override
	public List<Parameter> selectdervicebasic(int modbus_type) {
		List<Parameter> paramete=parameterServiceDao.selectdervicebasic(modbus_type);
		return  paramete;
	}
	/**
	 * 获取基本参数
	 */
	@Override
	public List<Parameter> selectdervicename(Integer modbus_type) {
		List<Parameter> paramete=parameterServiceDao.selectdervicename(modbus_type);
		return paramete;
	}
}
