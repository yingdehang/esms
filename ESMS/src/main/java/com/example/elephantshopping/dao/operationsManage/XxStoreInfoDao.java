package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface XxStoreInfoDao {
	/**
	 * 查询线下店铺内容list的count
	 * 
	 * @param requestToMap
	 * @return
	 */
	int getXxStoreInfolistcount(Map<String, Object> requestToMap);

	/**
	 * 查询线下店铺内容list
	 * 
	 * @param requestToMap
	 * @return
	 */
	List<Map<String, Object>> getXxStoreInfolist(Map<String, Object> requestToMap);

	/**
	 * 查询地区名称
	 * 
	 * @param string
	 * @return
	 */
	String queryCityName(String string);

	/**
	 * 查询线下店铺内容详情
	 * 
	 * @param eSXID
	 * @return
	 */
	Map<String, Object> getXxStoreInfoById(String eSXID);

	/**
	 * 修改店铺内容状态
	 * 
	 * @param map
	 * @return
	 */
	int updateStoreInfoState(Map<String, Object> map);

}
