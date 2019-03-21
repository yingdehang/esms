package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 订单管理Dao
 * @author XB
 *
 */
@Mapper
public interface OrderDao 
{
	/**
	 * 获取/查询未分页订单列表
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOrderList(Map<String, Object> parameters);

	/**
	 * 获取/查询已分页订单列表
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOrderListPage(Map<String, Object> parameters);

	/**
	 * 根据订单编号取出每个订单包含的商品List
	 * @param object
	 * @return
	 */
	List<Map<String, Object>> getOrderGoods(String orderNumber);

	/**
	 * 根据订单编号取出每个订单包含的商品List，用于订单导出
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOrderGoods2(Map<String, Object> parameters);
	
	/**
	 * 获取物流公司
	 * @return
	 */
	List<Map<String, Object>> getLogistics();
	
	/**
	 * 查询订单的物流信息
	 * @param orderNumber
	 * @return
	 */
	List<Map<String, Object>> getOrderLogisticsList(String orderNumber);

	/**
	 * 根据运单号取出订单包含的商品List
	 * @param awb
	 * @return
	 */
	List<Map<String, Object>> getOrderByAWB(String awb);

	/**
	 * 通过Id获取订单信息
	 * @param orderId
	 * @return
	 */
	Map<String, Object> getOrderById(String orderId);

	/**
	 * 通过订单编号获取订单列表
	 * @param orderNumber
	 * @return
	 */
	List<Map<String, Object>> getOrderByOrderNumber(String orderNumber);

	/**
	 * 根据店铺id获取店铺信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getStoreInfoById(String storeId);

	/**
	 * 获取卖家信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getStoreUser(String storeId);

	/**
	 * 未分页的所有订单
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getAllOrderList(Map<String, Object> parameters);

	/**
	 * 分页的所有订单
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getAllOrderListPage(Map<String, Object> parameters);

	/**
	 * 仲裁
	 * @param parameters
	 */
	void arbitration(Map<String, Object> parameters);

	/**
	 * 通过id拿到用户账号的状态（正常/冻结）
	 * @param userId
	 * @return
	 */
	String getUserState(String userId);

	/**
	 * 用户余额
	 * @param userId
	 * @return
	 */
	double getUserMoneys(String userId);

	/**
	 * 用户零花钱
	 * @param userId
	 * @return
	 */
	double getUserPocketMoney(String userId);

	/**
	 * 改变用户的余额
	 * @param parameters
	 */
	void changeUserMoneys(Map<String, Object> parameters);

	/**
	 * 改变用户的零花钱
	 * @param parameters
	 */
	void changeUserPocketMoney(Map<String, Object> parameters);

	/**
	 * 改变用户的消费券
	 * @param parameters
	 */
	void changeUserConsumption(Map<String, Object> parameters);

	/**
	 * 添加余额流水
	 * @param parameters
	 */
	void addMoneyWater(Map<String, Object> parameters);

	/**
	 * 添加零花钱流水
	 * @param parameters
	 */
	void addPocketMoneyWater(Map<String, Object> parameters);

	/**
	 * 添加收益流水
	 * @param parameters
	 */
	void addRunningWater(Map<String, Object> parameters);

	/**
	 * 添加消费券流水
	 * @param parameters
	 */
	void addConsumpationWater(Map<String, Object> parameters);

	/**
	 * 添加积分流水
	 * @param parameters
	 */
	void addIntegralWater(Map<String, Object> parameters);

	/**
	 * 改变用户积分
	 * @param parameters
	 */
	void changeUserIntegral(Map<String, Object> parameters);

	/**
	 * 添加分享奖励流水
	 * @param parameters
	 */
	void addShareAwardWater(Map<String, Object> parameters);

	/**
	 * 添加站长收支流水
	 * @param parameters
	 */
	void addWebMasterWater(Map<String, Object> parameters);

	/**
	 * 改变站长金额
	 * @param parameters
	 */
	void changeWebMasterMoney(Map<String, Object> parameters);

	/**
	 * 改变用户的分享奖励金额/积分
	 * @param parameters
	 */
	void changeShareReward(Map<String, Object> parameters);

	/**
	 * 查询订单的所有状态
	 * @return
	 */
	List<Map<String, Object>> getOrderState();

	/**
	 * 得到所有的商品列表
	 * @return
	 */
	List<Map<String, Object>> getGoods();

	/**
	 * 得到本店所有的商品列表
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> getStoreGoods(String storeId);

}
