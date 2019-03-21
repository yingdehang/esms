package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 线下店铺Dao
 * @author XB
 *
 */
@Mapper
public interface OffLineStoreDao
{
	/**
	 * 未认证的线下店铺数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getNoVerifyStore(Map<String, Object> parameters);

	/**
	 * 未认证的线下店铺数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getNoVerifyStorePage(Map<String, Object> parameters);

	/**
	 * 获取店铺所有分类
	 * @return
	 */
	List<Map<String, Object>> getStoreClass();

	/**
	 * 获取店铺信息
	 * @param certificationId
	 * @return
	 */
	Map<String, Object> getStoreInfo(String certificationId);

	/**
	 * 进行认证
	 * @param parameters
	 */
	void verify(Map<String, Object> parameters);

	/**
	 * 获取未分页的认证记录
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVerifyRecord(Map<String, Object> parameters);

	/**
	 * 获取已分页的认证记录
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVerifyRecordPage(Map<String, Object> parameters);

	/**
	 * 通过认证的线下店铺数据未分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOffLineStoreList(Map<String, Object> parameters);

	/**
	 * 通过认证的线下店铺数据已分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOffLineStoreListPage(Map<String, Object> parameters);

	/**
	 * 获取店铺状态（正常/冻结）
	 * @return
	 */
	List<Map<String, Object>> getStoreState();

	/**
	 * 转换店铺状态（正常/冻结）
	 * @param parameters
	 */
	void changeState(Map<String, Object> parameters);

	/**
	 * 改变店铺名字
	 * @param parameters
	 */
	void changeName(Map<String, Object> parameters);

	/**
	 * 改变店铺详细地址
	 * @param parameters
	 */
	void changeAddr(Map<String, Object> parameters);

	/**
	 * 改变店铺分类
	 * @param parameters
	 */
	void changeClass(Map<String, Object> parameters);

	/**
	 * 改变店铺客服电话
	 * @param parameters
	 */
	void changeServicePhone(Map<String, Object> parameters);

	/**
	 * 取出所有的通过认证的店铺的认证信息
	 * @return
	 */
	List<Map<String, Object>> getStoreCertificationList();

	/**
	 * 将店铺等级替换为新的等级并删除WAITING_STATE字段的值
	 * @param parameters
	 */
	void changeLevel(Map<String, Object> parameters);

	/**
	 * 获取店铺所有等级
	 * @return
	 */
	List<Map<String, Object>> getStoreLevel();

	/**
	 * 为店铺添加一个待改变的等级
	 * @param parameters
	 */
	void addNewLevel(Map<String, Object> parameters);

	/**
	 * 获取店铺认证状态
	 * @return
	 */
	List<Map<String, Object>> getVerifyState();

	/**
	 * 改变店铺的状态为正常
	 * @param storeId
	 */
	void changeStoreStateNormal(String storeId);

	/**
	 * 查询线下店铺某月/日的大额报单流水
	 * @param parameters
	 * @return
	 */
	Map<String, Object> getDeclarationWater(Map<String, Object> parameters);

	/**
	 * 得到店铺的信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getOffLineStoreInfoById(String storeId);

	/**
	 * 取出不是当月注册的用户
	 * @param thisMonth
	 * @return
	 */
	List<Map<String, Object>> getUsersExceptThisMonth(String thisMonth);

	/**
	 * 查询用户本月订单总额
	 * @param parameters
	 * @return
	 */
	Double getOrderMoney(Map<String, Object> parameters);

	/**
	 * 查询用户本月线下店铺支付总额
	 * @param parameters
	 * @return
	 */
	Double getOffLinePayMoney(Map<String, Object> parameters);

	/**
	 * 查询用户本月小额报单总额
	 * @param parameters
	 * @return
	 */
	Double getLittleDeclarationMoney(Map<String, Object> parameters);

	/**
	 * 查询用户本月大额报单总额
	 * @param parameters
	 * @return
	 */
	Double getBigDeclarationMoney(Map<String, Object> parameters);

	/**
	 * 改变用户会员等级
	 * @param parameters
	 */
	void changeMembershipGrade(Map<String, Object> parameters);

	/**
	 * 得到已付款的订单
	 * @return
	 */
	List<Map<String, Object>> getPayOrder();

	/**
	 * 取消订单
	 * @param orderNumber
	 */
	void cancleOrder(String orderNumber);
	
}
