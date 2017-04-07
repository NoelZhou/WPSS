package com.cn.hy.dao.serviceset;

import java.util.List;

import com.cn.hy.pojo.serviceset.EmailServer;
import com.cn.hy.pojo.serviceset.UserEmail;

/**
 * 故障告警设置
 * @author HCheng
 *
 */
public interface ErrorWarnSetDao {
	/**
	 * 接受邮件管理
	 * @return
	 */
	public List<UserEmail> listUserEmail();
	/**
	 * 根据id获取用户邮箱
	 * @param id
	 * @return
	 */
	public UserEmail getUserEmailById(int id);
	/**
	 * 新增用户邮箱
	 * @param userEmail
	 */
	public void saveUserEmail(UserEmail userEmail);
	/**
	 * 修改用户邮箱
	 * @param userEmail
	 */
	public void updateUserEmail(UserEmail userEmail);
	/**
	 * 删除用户邮箱
	 * @param id
	 */
	public void deleteUserEmail(int id);
	/**
	 * 邮箱服务设置
	 * @return
	 */
	public EmailServer getEmailServer();
	/**
	 * 新增邮箱服务设置
	 * @param emailServer
	 */
	public void saveEmailServer(EmailServer emailServer);
	/**
	 * 修改邮箱服务设置
	 * @param emailServer
	 */
	public void updateEmailServer(EmailServer emailServer);
}
