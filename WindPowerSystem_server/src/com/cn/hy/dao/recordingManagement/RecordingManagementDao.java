package com.cn.hy.dao.recordingManagement;

import java.util.List;

import com.cn.hy.pojo.recordingManagement.RecordingManagement;

public interface RecordingManagementDao {
	
	/**
	 * 查询所有录波管理数据
	 * @param recordingManagement
	 * @return
	 */
	List<RecordingManagement> list(RecordingManagement recordingManagement);
	
}
