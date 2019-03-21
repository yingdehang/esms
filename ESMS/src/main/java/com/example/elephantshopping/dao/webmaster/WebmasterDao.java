package com.example.elephantshopping.dao.webmaster;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WebmasterDao {
	/**
	 * 查询站长信息
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> getWebmasterInfo(String userId);

	/**
	 * 查询该地区用户总数
	 * 
	 * @param areaId
	 * @return
	 */
	int getuserCount(String areaId);

	/**
	 * 查询该地区线上店铺数
	 * 
	 * @param areaId
	 * @return
	 */
	int getXsStoreCount(String areaId);

	/**
	 * 查询该地区线下店铺数
	 * 
	 * @param areaId
	 * @return
	 */
	int getXxStoreCount(String areaId);

	/**
	 * 七天新增用户数
	 * 
	 * @param querymap
	 * @return
	 */
	int getnewUserCount(Map<String, Object> querymap);

	/**
	 * 七天新增线上店铺
	 * 
	 * @param querymap
	 * @return
	 */
	int getnewXsStoreCount(Map<String, Object> querymap);

	/**
	 * 七天新增线下店铺
	 * 
	 * @param querymap
	 * @return
	 */
	int getnewXxStoreCount(Map<String, Object> querymap);

	/**
	 * 通过id查询地区
	 * 
	 * @param areaId
	 * @return
	 */
	Map<String, Object> queryAreaName(String areaId);

	/**
	 * 查询站长收益
	 * 
	 * @param map
	 * @return
	 */
	Double queryTotalRevenue(Map<String, Object> map);

	/**
	 * 查询上月收益
	 * 
	 * @param queryMonth
	 * @return
	 */
	Double queryLastmonthEarnings(Map<String, Object> queryMonth);

	/**
	 * 查詢地區id
	 * 
	 * @param areaId
	 * @return
	 */
	String getAreaId(String areaId);

	/**
	 * 查询店铺收益
	 * 
	 * @param duringday
	 * @return
	 */
	Double queryToreEvenue(Map<String, Object> duringday);

	/**
	 * 查询店铺等级分布
	 * 
	 * @param map
	 * @return
	 */
	Integer getShopGradeDistributionData(Map<String, Object> map);

	/**
	 * 查询会员等级分布
	 * 
	 * @param map
	 * @return
	 */
	Integer membershipistributionData(Map<String, Object> map);

	/**
	 * 查询店铺总数
	 * 
	 * @param map
	 * @return
	 */
	int getStoreCount(Map<String, Object> map);

	/**
	 * 查询收支明细总条数
	 * 
	 * @param map
	 * @return
	 */
	int queryPaymentListCount(Map<String, Object> map);

	/**
	 * 查询收支明细
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryPaymentList(Map<String, Object> map);

	/**
	 * 查询收支明细打印list
	 * 
	 * @param ezpids
	 * @return
	 */
	Map<String, Object> getPaymentList(String s);

	/**
	 * 会员充值占比
	 * 
	 * @param map
	 * @return
	 */
	Double getmembershipRechargeAmountData(Map<String, Object> map);

}
