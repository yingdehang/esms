package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.systemManage.VersionDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class VersionService {
	@Autowired
	private VersionDao versionDao;

	/**
	 * 未分页的数据List
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVersionList(Map<String, Object> parameters) {
		return versionDao.getVersionList(parameters);
	}

	/**
	 * 分页的数据List
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVersionListPage(Map<String, Object> parameters) {
		List<Map<String,Object>> versionListPage = versionDao.getVersionListPage(parameters);
		versionListPage = DateFormatUtils.timeforDateType(versionListPage, "ADDTIME");
		return versionListPage;
	}

	/**
	 * 修改版本数据
	 * @param parameters
	 */
	public void modify(Map<String, Object> parameters) {
		versionDao.modify(parameters);
	}

	/**
	 * 添加版本
	 * @param parameters
	 */
	public void addVersion(Map<String, Object> parameters) {
		versionDao.addVersion(parameters);
	}
	
}
