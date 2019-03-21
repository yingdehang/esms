package com.example.elephantshopping.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.VersionUpdateDao;

@Service
public class VersionUpdateService {
	@Autowired
	private VersionUpdateDao versionUpdateDao;

	/**
	 * 下载记录
	 * @param dId
	 * @param dType
	 */
	public void record(String dId, String dType) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("dId", dId);
		parameters.put("dType", dType);
		versionUpdateDao.record(parameters);
	}
}
