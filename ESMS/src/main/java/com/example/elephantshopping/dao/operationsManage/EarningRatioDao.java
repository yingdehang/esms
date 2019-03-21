package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 收益比例管理Dao
 * @author XB
 *
 */
@Mapper
public interface EarningRatioDao 
{
	/**
	 * 未分页的收益比例数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getEarningRatio(Map<String, Object> parameters);

	/**
	 * 已分页的收益比例数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getEarningRatioPage(Map<String, Object> parameters);

	/**
	 * 删除
	 * @param id
	 */
	void delete(String id);

	/**
	 * 修改
	 * @param parameters
	 */
	void edit(Map<String, Object> parameters);

	/**
	 * 添加
	 * @param parameters
	 */
	void add(Map<String, Object> parameters);
	
}
