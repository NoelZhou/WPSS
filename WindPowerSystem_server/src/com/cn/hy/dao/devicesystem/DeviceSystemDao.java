package com.cn.hy.dao.devicesystem;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cn.hy.pojo.modbustcp.ModBusErrorCode;

public interface DeviceSystemDao {

	/**
	 * 获取故障协议地址及bit位
	 * @param type
	 * @return
	 */
	List<ModBusErrorCode> geterrorcode(int type);
	/**
	 * 根据故障地址获取故障bit位
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String geterrorcodebyaddr(@Param(value="addr")int addr,@Param(value="modbus_type") int modbustcp_type);

}
