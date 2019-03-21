package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.operationsManage.WithdrawalsDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 提现审核Service
 * @author XB
 *
 */
@Service
public class WithdrawalsService 
{
	@Autowired
	private WithdrawalsDao withdrawalsDao;

	/**
	 * 提现申请数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getWithdrawals(Map<String, Object> parameters) 
	{
		return withdrawalsDao.getWithdrawals(parameters);
	}

	/**
	 * 提现申请数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getWithdrawalsPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> list = withdrawalsDao.getWithdrawalsPage(parameters);
		DateFormatUtils.timeforDateType(list, "TO_APPLY_FOR_TIME");
		return list;
	}

	/**
	 * 提现申请明细数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getWithdrawalsRecord(Map<String, Object> parameters) 
	{
		return withdrawalsDao.getWithdrawalsRecord(parameters);
	}

	/**
	 * 提现申请明细数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getWithdrawalsRecordPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> list = withdrawalsDao.getWithdrawalsRecordPage(parameters);
		DateFormatUtils.timeforDateType(list, "TO_APPLY_FOR_TIME");
		return list;
	}

	/**
	 * 审核状态
	 * @return
	 */
	public List<Map<String, Object>> getWithdrawalsStates() 
	{
		return withdrawalsDao.getWithdrawalsStates();
	}

	/**
	 * 提现审核通过
	 * @param withdrawalsId
	 * @param verifyState
	 * @param content
	 */
	public void verifyPass(String withdrawalsId) 
	{
		withdrawalsDao.verifyPass(withdrawalsId);
	}
	
	/**
	 * 提现审核失败
	 * @param userId
	 * @param moneyType
	 * @param money
	 * @param withdrawalsId
	 * @param content
	 */
	public void verifyNo(double money, String withdrawalsId, String content) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("money", money);
		parameters.put("withdrawalsId", withdrawalsId);
		parameters.put("content", content);
		withdrawalsDao.verifyNo(parameters);
	}

	/**
	 * 用户表添加金额
	 * @param userId
	 * @param moneyType
	 * @param money
	 */
	public void addUserMoney(String userId, String moneyType, double money) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId", userId);
		parameters.put("moneyType", moneyType);
		parameters.put("money", money);
		withdrawalsDao.addUserMoney(parameters);
	}

	/**
	 * 添加余额流水
	 * @param userId
	 * @param money
	 * @param string
	 * @param string2
	 */
	public void addMoneyWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("moneyWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		withdrawalsDao.addMoneyWater(parameters);
	}

	/**
	 * 添加零花钱流水
	 * @param userId
	 * @param money
	 * @param string
	 * @param string2
	 */
	public void addPocketMoneyWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("pocketMoneyWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		withdrawalsDao.addPocketMoneyWater(parameters);
	}

	/**
	 * 添加收益流水
	 */
	public void addRunningWater(String userId,String waterType,double money,String name,String moneyType) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("runningWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("waterType", waterType);
		parameters.put("name", name);
		parameters.put("moneyType", moneyType);
		withdrawalsDao.addRunningWater(parameters);
	}

	/**
	 * 得到用户的旗舰店
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserQJStore(String userId) 
	{
		return withdrawalsDao.getUserQJStore(userId);
	}

	/**
	 * 判断用的银行卡信息是否存在
	 * @param userId
	 * @param bank
	 * @return
	 */
	public int getUserBank(String userId, String bank) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId",userId);
		parameters.put("bank",bank);
		return withdrawalsDao.getUserBank(parameters);
	}

	/**
	 * 添加一条用户银行卡信息
	 * @param userId
	 * @param bankName
	 * @param bank
	 */
	public void addUserBank(String userId, String bankName, String bank) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", UUID.randomUUID().toString().replaceAll("-",""));
		parameters.put("userId",userId);
		parameters.put("bank",bank);
		parameters.put("bankName",bankName);
		withdrawalsDao.addUserBank(parameters);
	}

	/**
	 * 得到用户保存的所有银行信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getBanks(String userId) 
	{
		return withdrawalsDao.getBanks(userId);
	}

	/**
	 * 删除这条银行卡信息
	 * @param bankId
	 */
	public void deleteBank(String bankId) 
	{
		withdrawalsDao.deleteBank(bankId);
	}
}
