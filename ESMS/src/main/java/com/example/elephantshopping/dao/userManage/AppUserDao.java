package com.example.elephantshopping.dao.userManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * APP用户管理Dao
 * @author XB
 *
 */
@Mapper
public interface AppUserDao
{
	/**
	 * 表格获取APP用户数据
	 * @return
	 */
	List<Map<String, Object>> getAppUserList(Map<String,Object> parameters);

	/**
	 * 禁用账号
	 * @param userId
	 */
	void forbidden(String userId);

	/**
	 * 启用账号
	 * @param userId
	 */
	void startUsing(String userId);


	/**
	 * 表格获取APP用户数据并分页
	 * @return
	 */
	List<Map<String, Object>> getAppUserListPage(Map<String,Object> parameters);

	/**
	 * 获取用户详细信息
	 * @param userId
	 * @return
	 */
	Map<String, Object> getDetails(String userId);

	/**
	 * 获取用户状态
	 * @return
	 */
	List<Map<String, Object>> getUserState();

	/**
	 * 获取实名认证状态
	 * @return
	 */
	List<Map<String, Object>> getVerifyState();

	/**
	 * 改变账户状态
	 * @param parameters
	 */
	void changeUserState(Map<String, Object> parameters);

	/**
	 * 改变账户认证状态
	 * @param parameters
	 */
	void changeVerifytate(Map<String, Object> parameters);

	/**
	 * 用户余额流水数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserMoneyWater(String userId);

	/**
	 * 用户零花钱流水数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserPocketMoney(String userId);

	/**
	 * 用户消费券流水数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserConsumptionVolume(String userId);

	/**
	 * 用户信息
	 * @param userId
	 * @return
	 */
	Map<String, Object> getUserInfo(String userId);

	/**
	 * 改变会员等级
	 * @param parameters
	 */
	void changeGrade(Map<String, Object> parameters);

	/**
	 * 改变邀请人
	 * @param parameters
	 */
	void changeInvitation(Map<String, Object> parameters);

	/**
	 * 用户积分流水数据
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserIntegral(String userId);

}
