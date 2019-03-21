package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺管理Dao
 * @author XB
 *
 */
@Mapper
public interface StoreDao
{
	/**
	 * 未分页的店铺数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreList(Map<String, Object> parameters);

	/**
	 * 已分页的店铺数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreListPage(Map<String, Object> parameters);

	/**
	 * 获取店铺状态列表
	 * @return
	 */
	List<Map<String, Object>> getStoreStatesList();

	/**
	 * 获取店铺分类
	 * @return
	 */
	List<Map<String, Object>> getStoreClassList();

	/**
	 * 判断店铺是否重名
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreByName(Map<String, Object> parameters);

	/**
	 * 保存店铺基本信息
	 * @param parameters
	 */
	void addStore(Map<String, Object> parameters);

	/**
	 * 保存图片信息
	 * @param map
	 */
	void addPhoto(Map<String, Object> map);

	/**
	 * 将图片Id保存到店铺表里面
	 * @param map
	 */
	void addIconId(Map<String, Object> map);

	/**
	 * 获取某个店铺的信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getStoreInfo(String storeId);

	/**
	 * 修改店铺基本信息
	 * @param parameters
	 */
	void editStore(Map<String, Object> parameters);

	/**
	 * 删除原来表中的图片信息
	 * @param iId
	 */
	void deletIcon(String iId);

	/**
	 * 修改店铺的图标Id
	 * @param map
	 */
	void updateIconId(Map<String, Object> map);

}
