package com.cn.hy.dao.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Appparame;

public interface Modbustcp_Sk_AppparameDao {
	/**
	 * 通过addr获取数据
	 * @param modbustcp_Sk_Appparame
	 * @return
	 */
	public List<Modbustcp_Sk_Appparame> findByAddr(Modbustcp_Sk_Appparame modbustcp_Sk_Appparame);
}
