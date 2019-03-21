package com.example.elephantshopping.service.log;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.log.LogDao;
import com.example.elephantshopping.entity.Log;

/**
 * 操作日志
 * 
 * @author XB
 *
 */
@Service
public class LogService {
	@Autowired
	private LogDao logDao;

	/**
	 * 添加操作记录
	 * 
	 * @param log
	 */
	@Transactional
	public void insertLog(Log log) {
		logDao.insertLog(log);
	}

	/**
	 * 查询该操作的用户信息
	 * 
	 * @param uSERS_ID
	 * @return
	 */
	public Map<String, Object> getNIKENAMEBYID(String uSERS_ID) {
		return logDao.getNIKENAMEBYID(uSERS_ID);
	}

	/**
	 * 查询线下店铺分类名
	 * 
	 * @param storeClassId
	 * @return
	 */
	public String queryXxStoreClassNameById(String storeClassId) {
		return logDao.queryXxStoreClassNameById(storeClassId);
	}

	/**
	 * 查询线上店铺分类名
	 * 
	 * @param cLASSIFICATION_ID
	 * @return
	 */
	public String queryXsStoreClassNameById(String cLASSIFICATION_ID) {
		// TODO Auto-generated method stub
		return logDao.queryXsStoreClassNameById(cLASSIFICATION_ID);
	}

}
