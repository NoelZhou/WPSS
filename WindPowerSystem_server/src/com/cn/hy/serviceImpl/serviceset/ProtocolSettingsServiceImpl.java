package com.cn.hy.serviceImpl.serviceset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.serviceset.Modbustcp_Sk_AppparameDao;
import com.cn.hy.dao.serviceset.Modbustcp_Sk_IacparameDao;
import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Appparame;
import com.cn.hy.pojo.serviceset.Modbustcp_Sk_Iacparame;
import com.cn.hy.service.serviceset.ProtocolSettingsService;
@Service("ProtocolSettingsServiceImpl")
public class ProtocolSettingsServiceImpl implements ProtocolSettingsService{
	@Resource
	private Modbustcp_Sk_AppparameDao modbustcp_Sk_AppparameDao;
	@Resource
	private Modbustcp_Sk_IacparameDao modbustcp_Sk_IacparameDao;
	/**
	 * 通过addr获取数据
	 */
	@Override
	public List<Modbustcp_Sk_Appparame> findByAddr(Modbustcp_Sk_Appparame modbustcp_Sk_Appparame) {
		return modbustcp_Sk_AppparameDao.findByAddr(modbustcp_Sk_Appparame);
	}
	/**
	 * 通过addr获取枚举表数据
	 */
	@Override
	public List<Modbustcp_Sk_Iacparame> findByMskIAppAddr(Modbustcp_Sk_Iacparame modbustcp_Sk_Iacparame) {
		return modbustcp_Sk_IacparameDao.findByMskIAppAddr(modbustcp_Sk_Iacparame);
	}

}
