package com.example.elephantshopping.dao.userManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏管理Dao
 * @author XB
 *
 */
@Mapper
public interface CollectionDao 
{
	/**
	 * 获取未分页的收藏数据List
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getCollectionList();
	
	/**
	 * 根据Id查询商品名
	 * @return
	 */
	String getGoodsName(String goodsId);
	
	/**
	 * 根据Id查询店铺名
	 * @return
	 */
	String getStoreName(String storeId);

	/**
	 * 获取收藏数据并分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getCollectionListPage(Map<String, Object> parameters);

	/**
	 * 查询收藏
	 * @param phoneNumber
	 * @return
	 */
	List<Map<String, Object>> searchCollection(String phoneNumber);

	/**
	 * 查询收藏并分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> searchCollectionPage(Map<String, Object> parameters);
}
