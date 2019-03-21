package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 提现审核Dao
 * @author XB
 *
 */
@Mapper
public interface WithdrawalsDao 
{
	/**
	 * 提现申请数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getWithdrawals(Map<String, Object> parameters);

	/**
	 * 提现申请数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getWithdrawalsPage(Map<String, Object> parameters);

	/**
	 * 提现申请明细数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getWithdrawalsRecord(Map<String, Object> parameters);

	/**
	 * 提现申请明细数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getWithdrawalsRecordPage(Map<String, Object> parameters);

	/**
	 * 审核状态
	 * @return
	 */
	List<Map<String, Object>> getWithdrawalsStates();

	/**
	 * 提现审核通过
	 * @param parameters
	 */
	void verifyPass(String withdrawalsId);

	/**
	 * 提现审核失败
	 * @param parameters
	 */
	void verifyNo(Map<String, Object> parameters);

	/**
	 * 用户表添加金额
	 * @param parameters
	 */
	void addUserMoney(Map<String, Object> parameters);

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
	 * 得到用户的旗舰店
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserQJStore(String userId);

	/**
	 * 判断用的银行卡信息是否存在
	 * @param parameters
	 * @return
	 */
	int getUserBank(Map<String, Object> parameters);

	/**
	 * 添加一条用户银行卡信息
	 * @param parameters
	 */
	void addUserBank(Map<String, Object> parameters);

	/**
	 * 得到用户保存的所有银行信息
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getBanks(String userId);

	/**
	 * 删除这条银行卡信息
	 * @param bankId
	 */
	void deleteBank(String bankId);

}
