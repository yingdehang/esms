package com.example.elephantshopping.dao.webmaster;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ViceWebmasterDao {
	/**
	 * 查询副站长list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryvicemasterlist(Map<String, Object> map);

	/**
	 * 查询count
	 * 
	 * @param map
	 * @return
	 */
	int queryvicemasterlistCount(Map<String, Object> map);

	/**
	 * 验证手机号是否注册
	 * 
	 * @param masterPhone
	 * @return
	 */
	Map<String, Object> isregisterUser(String masterPhone);

	/**
	 * 验证推荐者手机号是否有效
	 * 
	 * @param upPhone
	 * @return
	 */
	Map<String, Object> presenterPhone(String upPhone);

	/**
	 * 设置副站长
	 * 
	 * @param map
	 * @return
	 */
	int updateUserIsFzz(Map<String, Object> map);

	/**
	 * 查询该推荐者推荐了多少人
	 * 
	 * @param upPhone
	 * @return
	 */
	int presenterNumber(String upPhone);

	/**
	 * 添加收益流水
	 * 
	 * @param map
	 */
	void insertRunningWater(Map<String, Object> map);

	/**
	 * 添加零花钱收支
	 * 
	 * @param map
	 */
	void insertPocketMoney(Map<String, Object> map);

	/**
	 * 添加推荐者零花钱
	 * 
	 * @param map
	 */
	int addPersenterPocketMoney(Map<String, Object> map);

	/**
	 * 撤销副站长
	 * 
	 * @param userId
	 * @return
	 */
	int cancelViceMaster(String userId);

}
