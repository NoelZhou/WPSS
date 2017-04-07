package com.cn.hy.dao.modbustcp;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.cn.hy.pojo.modbustcp.ModBusParame;
import com.cn.hy.pojo.modbustcp.ModbusBit;

public interface ModBusTcpDao {

	/**
	 * 查找协议地址的bit位数
	 * @param mbparame
	 * @return
	 */
	public int getaddrbit(ModBusParame mbparame);
	/**
	 * 获取协议地址的var位数
	 * @param mbparame
	 * @return
	 */
	public int getaddrvar(ModBusParame mbparame);
	/**
	 * 获取指定地址的所有bit信息
	 * @param mbparame
	 * @return
	 */
	public List<ModbusBit> getaddrbitlist(ModBusParame mbparame);
	/**
	 * 获取指定地址的var的name
	 * @param mbparame
	 * @return
	 */
	public String getaddvarvalue(ModBusParame mbparame);
	/**
	 * 根据id和设备类型获取bit位列表
	 * @param id
	 * @param device_type
	 * @return
	 */
	public List<ModbusBit> getaddrbitlistbyid(@Param(value="id")int id, @Param(value="device_type")int device_type);

	/**
	 * 根据id获取var
	 * @param mbparameone
	 * @return
	 */
	public String getvarbyid(ModBusParame mbparameone);
	/**
	 * 获取指定地址的协议类型
	 * @param addr
	 * @param device_type
	 * @return
	 */
	public List<Integer> getaddrarrarytype(@Param(value="addr")int addr, @Param(value="device_type")int device_type);
	/**
	 * 获取指定地址的cof系数
	 * @param mbparame
	 * @return
	 */
	public Integer getaddrcof(ModBusParame mbparame);
    

}
