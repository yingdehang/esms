package com.example.elephantshopping.dao.activityManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityDao {
	/**
	 * 查询活动list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getActiveList(Map<String, Object> map);

	/**
	 * 查询活动count
	 * 
	 * @param map
	 * @return
	 */
	int getActiveListCount(Map<String, Object> map);

	/**
	 * 修改活动状态
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateActivityState(Map requestToMap);

	/**
	 * 修改排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateActivitySort(Map requestToMap);

	/**
	 * 添加活动
	 * 
	 * @param map
	 */
	void addactivity(Map<String, Object> map);

	/**
	 * 查询活动信息
	 * 
	 * @param eSID
	 * @return
	 */
	Map<String, Object> queryActivityInfo(String eSID);

	/**
	 * 删除活动
	 * 
	 * @param string
	 */
	void deleteActivity(String ESID);

	/**
	 * 查询首页活动list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getNewActiveList(Map<String, Object> map);

	/**
	 * 查询首页活动总数
	 * 
	 * @param map
	 * @return
	 */
	int getNewActiveListCount(Map<String, Object> map);

	/**
	 * 添加活动
	 * 
	 * @param map
	 */
	void addNewActivity(Map<String, Object> map);

	/**
	 * 删除首页活动
	 * 
	 * @param nID
	 */
	void deleteNewActivity(String NID);

	/**
	 * 开启关闭首页活动
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateNewActivityState(Map<String, Object> requestToMap);

	/**
	 * 修改排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateNewActivitySort(Map<String, Object> requestToMap);

	/**
	 * 查询首页活动信息
	 * 
	 * @param nID
	 * @return
	 */
	Map<String, Object> queryNewActivityInfo(String nID);

	/**
	 * 查询首页活动类型一商品list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> querygoodslist(String NID);

	/**
	 * 查询店铺list
	 * 
	 * @return
	 */
	List<Map<String, Object>> querystorelist();

	/**
	 * 通过商品查询店铺Id
	 * 
	 * @param goodsId
	 * @return
	 */
	String queryStoreId(String goodsId);

	/**
	 * 
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> querygoodslistByStoreId(String storeId);

}
