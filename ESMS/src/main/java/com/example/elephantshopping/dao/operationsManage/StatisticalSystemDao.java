package com.example.elephantshopping.dao.operationsManage;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StatisticalSystemDao {
	/**
	 * 查询用户总数
	 * 
	 * @param map
	 * @return
	 */
	int queryUserCount(Map<String, Object> map);

	/**
	 * 查询实名认证用户数
	 * 
	 * @param map
	 * @return
	 */
	int queryUserPassCount(Map<String, Object> map);

	/**
	 * 当前总积分
	 * 
	 * @param map
	 * @return
	 */
	Double queryTotalScore(Map<String, Object> map);

	/**
	 * 查询线上店铺总数
	 * 
	 * @param querymap
	 * @return
	 */
	int getXsStoreCount(Map<String, Object> querymap);

	/**
	 * 查询线下店铺总数
	 * 
	 * @param map
	 * @return
	 */
	int getXxStoreCount(Map<String, Object> map);

	/**
	 * 昨日线上店铺产生的佣金
	 * 
	 * @param yesterday
	 * @return
	 */
	Double queryYesterdayXsCommission(Map<String, Object> yesterday);

	/**
	 * 线下店铺扫码支付产生的佣金
	 * 
	 * @param yesterday
	 * @return
	 */
	Double queryYesterdayXxSmCommission(Map<String, Object> yesterday);

	/**
	 * 线下店铺报单产生的佣金
	 * 
	 * @param yesterday
	 * @return
	 */
	Double queryYesterdayXxBdCommission(Map<String, Object> yesterday);

	/**
	 * 奖励金支出
	 * 
	 * @param yesterday
	 * @return
	 */
	double queryYesterdayIncentivePayments(Map<String, Object> yesterday);

	/**
	 * 积分赠送
	 * 
	 * @param yesterday
	 * @return
	 */
	double queryYesterdayIntegralPresent(Map<String, Object> yesterday);

	/**
	 * 积分变化
	 * 
	 * @param yesterday
	 * @return
	 */
	double queryYesterdayIntegralChange(Map<String, Object> yesterday);

	/**
	 * 充值金额
	 * 
	 * @param yesterday
	 * @return
	 */
	double queryYesterdayRechargeAmount(Map<String, Object> yesterday);

	/**
	 * 店铺等级分布
	 * 
	 * @param map
	 * @return
	 */
	Integer getShopGradeDistributionData(Map<String, Object> map);

	/**
	 * 会员充值金额占比
	 * 
	 * @param map
	 * @return
	 */
	Double getmembershipRechargeAmountData(Map<String, Object> map);

	/**
	 * 会员等级分布
	 * 
	 * @param map
	 * @return
	 */
	int membershipistributionData(Map<String, Object> map);

	/**
	 * 店铺流水
	 * 
	 * @param query
	 * @return
	 */
	Double queryToreEvenue(Map<String, Object> query);

	/**
	 * 插入数据记录表
	 * 
	 * @param map
	 */
	void insertDatatable(Map<String, Object> map);

	/**
	 * 查询平台总积分记录表
	 * 
	 * @param query
	 * @return
	 */
	double queryDataTable(Map<String, Object> query);

	/**
	 * 查询账户总积分，余额，消费券，零花钱
	 * 
	 * @return
	 */
	Map<String, Object> queryMoneyData();

	/**
	 * 查询订单数量
	 * 
	 * @param map
	 * 
	 * @return
	 */
	int queryOrderMumber(Map<String, Object> map);

}
