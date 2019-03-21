package com.example.elephantshopping.dao.merchants;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 我的店铺Dao
 * @author XB
 *
 */
@Mapper
public interface MyStoreDao
{
	/**
	 * 根据用户Id获得店铺信息
	 * @param userId
	 * @return
	 */
	Map<String, Object> getUserStore(String userId);

	/**
	 * 查询待处理的订单
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getPendingOrder(String userId);

	/**
	 * 查询所有的店铺状态
	 * @return
	 */
	List<Map<String, Object>> getStoreState();

	/**
	 * 查询店铺的所有消息
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getMessage(String userId);

	/**
	 * 查询店铺认证信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getStoreCertification(String storeId);

	/**
	 * 改变店铺营业状态
	 * @param parameters
	 */
	void changeState(Map<String, Object> parameters);

	/**
	 * 改变店铺客服电话
	 * @param parameters
	 */
	void changeServicePhone(Map<String, Object> parameters);

}
