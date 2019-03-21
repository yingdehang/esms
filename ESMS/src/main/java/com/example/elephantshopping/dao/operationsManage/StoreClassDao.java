package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺分类管理Dao
 * @author XB
 *
 */
@Mapper
public interface StoreClassDao 
{
	/**
	 * 未分页的店铺分类数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreClassList(Map<String, Object> parameters);

	/**
	 * 分页的店铺分类数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreClassListPage(Map<String, Object> parameters);

	/**
	 * 修改店铺分类名称
	 * @param parameters
	 */
	void edit(Map<String, Object> parameters);

	/**
	 * 添加店铺分类
	 * @param className
	 */
	void addClass(Map<String, Object> parameters);

	/**
	 * 删除店铺分类
	 * @param storeClassId
	 */
	void delete(String storeClassId);

	/**
	 * 查询此店铺分类是否关联店铺
	 * @param storeClassId
	 * @return
	 */
	List<Map<String, Object>> getStoreByClass(String storeClassId);

}
