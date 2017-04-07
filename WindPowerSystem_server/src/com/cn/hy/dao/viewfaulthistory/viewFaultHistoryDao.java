package com.cn.hy.dao.viewfaulthistory;


import java.util.List;

import com.cn.hy.pojo.viewFaultHistory.ViewFaultHistory;
/**
 * 事件记录中查看故障历史--弃用
 * @author Administrator
 *
 */
public interface viewFaultHistoryDao {

	public List<ViewFaultHistory> viewFaultHistoryListAll(int id);
}
