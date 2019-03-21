package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VersionDao {

	/**
	 * 未分页的数据List
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVersionList(Map<String, Object> parameters);

	/**
	 * 分页的数据List
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVersionListPage(Map<String, Object> parameters);

	/**
	 * 修改版本数据
	 * @param parameters
	 */
	void modify(Map<String, Object> parameters);

	/**
	 * 添加版本
	 * @param parameters
	 */
	void addVersion(Map<String, Object> parameters);

}
