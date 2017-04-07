package com.cn.hy.serviceImpl.serviceset;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.serviceset.ErrorWarnSetDao;
import com.cn.hy.pojo.serviceset.EmailServer;
import com.cn.hy.pojo.serviceset.UserEmail;
import com.cn.hy.service.serviceset.ErrorWarnSetService;
@Service("ErrorWarnSetServiceImpl")
public class ErrorWarnSetServiceImpl implements ErrorWarnSetService {
	@Resource
	private ErrorWarnSetDao errorWarnSetDao;
	/**
	 * 接受邮件管理
	 */
	@Override
	public List<UserEmail> listUserEmail() {
		return errorWarnSetDao.listUserEmail();
	}
	/**
	 * 根据id获取用户邮箱
	 */
	@Override
	public UserEmail getUserEmailById(int id) {
		return errorWarnSetDao.getUserEmailById(id);
	}
	/**
	 * 新增用户邮箱
	 */
	@Override
	public void saveUserEmail(UserEmail userEmail) {
		errorWarnSetDao.saveUserEmail(userEmail);
	}
	/**
	 * 修改用户邮箱
	 */
	@Override
	public void updateUserEmail(UserEmail userEmail) {
		errorWarnSetDao.updateUserEmail(userEmail);
	}
	/**
	 * 删除用户邮箱
	 */
	@Override
	public void deleteUserEmail(int id) {
		errorWarnSetDao.deleteUserEmail(id);
	}
	/**
	 * 邮箱服务设置
	 */
	@Override
	public EmailServer getEmailServer() {
		return errorWarnSetDao.getEmailServer();
	}
	/**
	 * 修改邮箱服务设置
	 */
	@Override
	public void updateEmailServer(EmailServer emailServer) {
		errorWarnSetDao.updateEmailServer(emailServer);
	}
	/**
	 * 新增邮箱服务设置
	 */
	@Override
	public void saveEmailServer(EmailServer emailServer) {
		errorWarnSetDao.saveEmailServer(emailServer);
	}

}
