package com.cn.hy.service.modbustcp;

import java.util.List;

public interface ModBusTcpService {
	/**
	 * 协议解析
	 * @param addr
	 * @param shortvalue
	 * @param device_type
	 * @return
	 */
   public List<String>  parseModBusTcp(int addr,int shortvalue,int device_type,String modbustsr);
	
}
