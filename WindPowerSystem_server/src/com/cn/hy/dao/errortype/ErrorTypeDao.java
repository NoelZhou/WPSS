package com.cn.hy.dao.errortype;

import java.util.List;

import com.cn.hy.pojo.errortype.ErrorType;

public interface ErrorTypeDao {
	/**
	 * 查询所有
	 * @return
	 */
	public List<ErrorType> selectErrorType();
}
