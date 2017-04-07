package com.cn.hy.dao.devicesystem;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.modbustcp.Parameter;

public interface ParameterServiceDao {
	/**
	 * 根据协议类型和父节点获取参数监控数组
	 * @param parent_type
	 * @param modbustcp_type
	 * @return
	 */
	public List<Parameter>  selectbytype(@Param(value="parent_type")int parent_type,@Param("modbustcp_type")int modbustcp_type);
	/**
	 * 获取同数组下所有协议地址
	 * @param id
	 * @param modbustcp_type
	 * @return
	 */
	public List<Parameter>  selectbychild(@Param(value="id")int id, @Param(value="modbustcp_type")int modbustcp_type);
	/**
	 * 获取设备基本信息
	 * @param modbus_type
	 * @return
	 */
	public List<Parameter>  selectdervicebasic(int modbus_type);
	/**
	 * 获取基本参数
	 * @param modbus_type
	 * @return
	 */
	public List<Parameter>  selectdervicename(Integer modbus_type);
} 
