package com.cn.hy.service.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Appparame;
import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Iacparame;


public interface ProtocolSettingsService {
	/**
	 * 通过addr获取数据
	 * @param modbustcp_Sk_Appparame
	 * @return
	 */
	public List<Modbustcp_Sk_Appparame> findByAddr(Modbustcp_Sk_Appparame modbustcp_Sk_Appparame); 
	/**
	 * 通过addr获取枚举表数据
	 * @param modbustcp_Sk_Iacparame
	 * @return
	 */
	public List<Modbustcp_Sk_Iacparame> findByMskIAppAddr(Modbustcp_Sk_Iacparame modbustcp_Sk_Iacparame);
}
