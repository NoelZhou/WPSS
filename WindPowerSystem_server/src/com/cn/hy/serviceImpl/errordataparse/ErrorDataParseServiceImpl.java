package com.cn.hy.serviceImpl.errordataparse;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cn.hy.dao.errordataparse.ErrorDataParseDao;
import com.cn.hy.dao.errortype.ErrorTypeDao;
import com.cn.hy.dao.viewfaulthistory.viewFaultHistoryDao;
import com.cn.hy.pojo.errordataparse.ErrorDataParse;
import com.cn.hy.pojo.errortype.ErrorType;
import com.cn.hy.pojo.viewFaultHistory.ViewFaultHistory;
import com.cn.hy.service.errordataparse.ErrorDataParseService;

@Service("ErrorDataParseServiceImpl")
public class ErrorDataParseServiceImpl implements ErrorDataParseService {

	@Resource
	private ErrorDataParseDao errorDataParseDao;
	@Resource
	private ErrorTypeDao errorTypeDao;
	@Resource
	private viewFaultHistoryDao viewfaulthistoryDao;
	/**
	 * 查询所有
	 */
	@Override
	public List<ErrorDataParse> selectErrorDataParse(String device_name, String errorname, String start_time,
			String end_time) {
		return errorDataParseDao.selectErrorDataParse(device_name, errorname, start_time, end_time);
	}
	/**
	 * 查询errortype表中的所有故障名称
	 */
	@Override
	public List<ErrorType> selectErrorType() {
		return errorTypeDao.selectErrorType();
	}
	/**
	 * 查看故障历史
	 */
	@Override
	public List<ViewFaultHistory> viewFaultHistoryListAll(int id) {
		return viewfaulthistoryDao.viewFaultHistoryListAll(id);
	}
}
