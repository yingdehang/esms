package com.example.elephantshopping.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VersionUpdateDao {

	/**
	 * 下载记录
	 * @param parameters
	 */
	void record(Map<String, Object> parameters);

}
