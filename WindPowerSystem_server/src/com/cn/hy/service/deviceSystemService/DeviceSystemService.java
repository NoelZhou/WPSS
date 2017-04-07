package com.cn.hy.service.deviceSystemService;

import java.util.List;
import com.cn.hy.pojo.modbustcp.ModBusErrorCode;

public interface DeviceSystemService {
	/**
	 * 获取故障协议地址及bit位
	 * @param modbustcp_type
	 * @return
	 */
	List<ModBusErrorCode> geterrorcode(int modbustcp_type);
	/**
	 * 根据故障地址获取故障bit位
	 * @param addr
	 * @param modbustcp_type
	 * @return
	 */
	String geterrorcodebyaddr(int addr, int modbustcp_type);
     
}
