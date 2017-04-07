package com.cn.hy.serviceImpl.recordingManagement;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cn.hy.dao.recordingManagement.RecordingManagementDao;
import com.cn.hy.pojo.recordingManagement.RecordingManagement;
import com.cn.hy.service.recordingManagement.RecordingManagementService;

@Service("RecordingManagementServiceImpl")
public class RecordingManagementServiceImpl implements RecordingManagementService{
	
	@Resource
	private RecordingManagementDao recordingManagementDao;
	/**
	 * 查询所有录波管理数据
	 */
	@Override
	public List<RecordingManagement> list(RecordingManagement recordingManagement) {
		return recordingManagementDao.list(recordingManagement);
	}

}
