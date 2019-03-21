package com.example.elephantshopping.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ActivityClickDao {

	/**
	 * 查询所有参赛人的list
	 * 
	 * @param endTime
	 * @return
	 */
	List<Map<String, Object>> queryRenqidasaiList(String endTime);

	/**
	 * 查询邀请的用户实名注册人数
	 * 
	 * @param map
	 * @return
	 */
	int queryInvitedUsersNumber(Map<String, Object> map);

	/**
	 * 查询邀请的线上店铺数量
	 * 
	 * @param map
	 * @return
	 */
	int queryInvitedStoreNumber(Map<String, Object> map);

	/**
	 * 查询用户是否已经参加过游戏
	 * 
	 * @param userId
	 * @return
	 */
	String queryIsGamed(String userId);

	/**
	 * 查询被抽取礼品数量
	 * 
	 * @param prizeName
	 * @return
	 */
	int queryPrizeNumber(String prizeName);

	/**
	 * 记录用户抽奖结果
	 * 
	 * @param map
	 */
	void updateLottery(Map<String, Object> map);

	/**
	 * 查询抽奖资格
	 * 
	 * @param userId
	 * @return
	 */
	int queryRaffle(String userId);

	/**
	 * 查询未抽奖人数
	 * 
	 * @return
	 */
	int queryUserNumber();

	/**
	 * 查询获奖名单
	 * 
	 * @return
	 */
	List<Map<String, Object>> queryWinList();

	/**
	 * 符合抽奖list的，插入抽奖list
	 * 
	 * @param user
	 */
	void insertLottery(Map<String, Object> user);

}
