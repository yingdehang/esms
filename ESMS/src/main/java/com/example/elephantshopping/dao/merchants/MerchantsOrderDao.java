package com.example.elephantshopping.dao.merchants;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商家的订单管理Dao
 * @author XB
 *
 */
@Mapper
public interface MerchantsOrderDao 
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
	 * 获取物流公司
	 * @return
	 */
	List<Map<String, Object>> getLogistics();

	/**
	 * 修改订单状态为发货
	 * @param map
	 */
	void sendGoods(Map<String, Object> map);

	/**
	 * 取消订单
	 * @param orderId
	 */
	void cancelOrder(String orderId);

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
	 * 发货时查询订单
	 * @param orderId
	 * @return
	 */
	List<Map<String, Object>> getOrders(String orderId);

	/**
	 * 通过商品Id得到店铺Id
	 * @param goodsId
	 * @return
	 */
	String getStoreIdByGoodsId(String goodsId);

	/**
	 * 减少商品库存
	 * @param goodsId
	 * @param number
	 */
	void changeGoodsINVENTORY(Map<String,Object> parameters);

	/**
	 * 增加商品购买数量
	 * @param goodsId
	 * @param number
	 */
	void addGoodsPURCHASE_QUANTITY(Map<String,Object> parameters);

	/**
	 * 增加店铺月销量
	 * @param storeId
	 * @param number
	 */
	void addStoreMONTH_SALES(Map<String,Object> parameters);

	/**
	 * 店铺今日订单数量加1
	 * @param storeId
	 */
	void addStoreSUM_BUY_NUMBER(String storeId);

	/**
	 * 取出此订单的交易凭证字段
	 * @param orderNumber
	 * @return
	 */
	String getTradingProof(String orderNumber);

	/**
	 * 将新的交易凭证信息保存到数据库，并修改审核状态为通过
	 * @param parameters
	 */
	void saveProofUrl(Map<String, Object> parameters);

	/**
	 * 得到订单状态
	 * @param orderId
	 * @return
	 */
	String getOrderState(String orderId);

	/**
	 * 得到某个订单的物流信息
	 * @param orderId
	 * @return
	 */
	Map<String, Object> gerOrderLogistics(String orderId);

	/**
	 * 修改订单物流信息
	 * @param parameters
	 */
	void changeOrderLogistics(Map<String, Object> parameters);

	/**
	 * 得到订单状态
	 * @param orderNumber
	 * @return
	 */
	String getOrderStateByNumber(String orderNumber);
}
