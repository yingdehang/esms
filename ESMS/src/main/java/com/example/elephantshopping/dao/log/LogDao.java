package com.example.elephantshopping.dao.log;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import com.example.elephantshopping.entity.Log;

@Mapper
public interface LogDao {
	/**
	 * 插入记录表
	 * 
	 * @param log
	 */
	void insertLog(Log log);

	/**
	 * 查询用户名
	 * 
	 * @param uSERS_ID
	 * @return
	 */
	Map<String, Object> getNIKENAMEBYID(String uSERS_ID);

	/**
	 * 查询线下店铺分类名
	 * 
	 * @param storeClassId
	 * @return
	 */
	String queryXxStoreClassNameById(String storeClassId);

	/**
	 * 查询线上店铺分类名
	 * 
	 * @param cLASSIFICATION_ID
	 * @return
	 */
	String queryXsStoreClassNameById(String cLASSIFICATION_ID);

}
