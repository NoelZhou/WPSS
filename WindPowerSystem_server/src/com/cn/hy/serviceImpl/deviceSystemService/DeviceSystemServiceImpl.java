package com.cn.hy.serviceImpl.deviceSystemService;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cn.hy.dao.devicesystem.DeviceSystemDao;
import com.cn.hy.pojo.modbustcp.ModBusErrorCode;
import com.cn.hy.service.deviceSystemService.DeviceSystemService;

@Service("DeviceSystemService")
public class DeviceSystemServiceImpl implements DeviceSystemService {
	@Resource
	private DeviceSystemDao devicesystemdao;
	/**
	 * 获取故障协议地址及bit位
	 */
	@Override
	public List<ModBusErrorCode> geterrorcode(int  type) {
		return devicesystemdao.geterrorcode(type);
	}
	/**
	 * 根据故障地址获取故障bit位
	 */
	@Override
	public String geterrorcodebyaddr(int addr,int modbustcp_type) {
		return  devicesystemdao.geterrorcodebyaddr(addr,modbustcp_type);
	}

}
