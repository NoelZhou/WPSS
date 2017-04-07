package com.cn.hy.dao.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Iacparame;



public interface Modbustcp_Sk_IacparameDao {
	/**
	 * 通过addr获取数据
	 * @param modbustcp_Sk_Iacparame
	 * @return
	 */
	List<Modbustcp_Sk_Iacparame> findByMskIAppAddr(Modbustcp_Sk_Iacparame modbustcp_Sk_Iacparame);
}
