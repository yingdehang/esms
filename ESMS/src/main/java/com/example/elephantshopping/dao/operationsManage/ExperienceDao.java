package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExperienceDao {

	/**
	 * 未分页的活动列表
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getActivitis(Map<String, Object> parameters);

	/**
	 * 分页的活动列表
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getActivitisPage(Map<String, Object> parameters);

	/**
	 * 开启/关闭活动
	 * @param parameters
	 */
	void turn(Map<String, Object> parameters);

	/**
	 * 改变活动排序
	 * @param parameters
	 */
	void changeSort(Map<String, Object> parameters);

	/**
	 * 添加活动
	 * @param parameters
	 */
	void addActivity(Map<String, Object> parameters);

	/**
	 * 通过Id得到某个活动信息
	 * @param id
	 * @return
	 */
	Map<String, Object> getActivity(String id);

	/**
	 * 修改活动
	 * @param parameters
	 */
	void modifyActivity(Map<String, Object> parameters);

	/**
	 * 查询活动的商品
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getActivityGoods(Map<String, Object> parameters);

	/**
	 * 查询活动的商品分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getActivityGoodsPage(Map<String, Object> parameters);

	/**
	 * 添加活动商品
	 * @param parameters
	 */
	void addActivityGoods(Map<String, Object> parameters);

	/**
	 * 通过Id得到某个商品信息
	 * @param id
	 * @return
	 */
	Map<String, Object> getActivityGoodsById(String id);

	/**
	 * 修改商品
	 * @param parameters
	 */
	void modifyActivityGoods(Map<String, Object> parameters);

	/**
	 * 删除数据库的商品数据
	 * @param id
	 */
	void deleteGoods(String id);

	/**
	 * 查询待审核的激活申请
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVerify(Map<String, Object> parameters);

	/**
	 * 查询待审核的激活申请分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getVerifyPage(Map<String, Object> parameters);

	/**
	 * 改变审核状态
	 * @param parameters
	 */
	void experienceVerify(Map<String, Object> parameters);

	/**
	 * 查询体验账户的信息
	 * @param accId
	 * @return
	 */
	Map<String, Object> getAccountById(String accId);

	/**
	 * 查询体验账户零花钱
	 * @param accId
	 * @return
	 */
	double getAccountPocketMoney(String accId);
	
	/**
	 * 查询体验账户消费券
	 * @param accId
	 * @return
	 */
	double getAccountConsumptionVolume(String accId);

	/**
	 * 通过用户Id查询体验账号
	 * @param userId
	 * @return
	 */
	Map<String, Object> getAccountByUserId(String userId);

	/**
	 * 为邀请人创建体验账号
	 * @param invitationId
	 */
	void createAccount(Map<String,Object> parameters);

	/**
	 * 加速
	 * @param acId
	 */
	void addSpeed(String acId);

	/**
	 * 体验专区审核表数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getRecord(Map<String, Object> parameters);

	/**
	 * 体验专区审核表数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getRecordPage(Map<String, Object> parameters);

	/**
	 * 查出体验专区所有已领取未购买的礼包
	 * @return
	 */
	List<Map<String, Object>> getNoActivateActivity();


	/**
	 * 将礼包状态改为已过期
	 * @param auId
	 */
	void setActivityOverdue(String auId);

	/**
	 * 获得体验账号某一天的签到数据
	 * @param parameters
	 * @return
	 */
	Map<String, Object> getSign(Map<String, Object> parameters);

	/**
	 * 添加签到数据
	 * @param parameters
	 */
	void addSign(Map<String, Object> parameters);

	/**
	 * 改变体验账户的积分零花钱消费券
	 * @param parameters
	 */
	void accountSign(Map<String, Object> parameters);

	/**
	 * 得到用户电话
	 * @param userId
	 * @return
	 */
	String getUserPhone(String userId);

	/**
	 * 清除体验账号积分，零花钱，消费券
	 * @param accId
	 */
	void clearAccountMoney(String accId);

	/**
	 * 清除体验账号的签到记录
	 * @param parameters
	 */
	void clearAccountSign(Map<String, Object> parameters);

	/**
	 * 得到体验账号的礼包审核记录
	 * @param accId
	 * @return
	 */
	List<Map<String, Object>> getAudits(String accId);

	/**
	 * 礼包积分加到体验账户
	 * @param parameters
	 */
	void addExperienceIntegral(Map<String, Object> parameters);

	/**
	 * 得到体验账号的礼包审核通过记录
	 * @param accId
	 * @return
	 */
	List<Map<String, Object>> getActivateAudits(String accId);

	/**
	 * 改变礼包数量
	 * @param parameters
	 */
	void changeActivityNumber(Map<String, Object> parameters);

	/**
	 * 增加象币明细
	 * @param parameters
	 */
	void addElephantDetail(Map<String, Object> parameters);

	/**
	 * 体验象币
	 * @param accId
	 * @return
	 */
	double getAccountIntegral(String accId);

	/**
	 * 购买专区增加积分零花钱和消费券
	 * @param parameters
	 */
	void addBuyMoney(Map<String, Object> parameters);


	/**
	 * 清除象币明细
	 * @param parameters
	 */
	void clearElephantDetail(Map<String, Object> parameters);

	/**
	 * 新增礼包数量定时器
	 * @param parameters
	 */
	void addSetTime(Map<String, Object> parameters);

	/**
	 * 修改礼包数量定时器
	 * @param parameters
	 */
	void updateSetTime(Map<String, Object> parameters);

	/**
	 * 删除上线设置
	 * @param id
	 */
	void deleteSetTime(String id);

	/**
	 * 获取用户已经加速的礼包数量
	 * @param accId
	 * @return
	 */
	int isSpeed(String accId);

	/**
	 * 查询用户购买的礼包的总额
	 * @param accId
	 * @return
	 */
	Double getSumPrice(String accId);

	/**
	 * 修改此礼包IS_SPEED为1
	 * @param auId
	 */
	void changeActivityIsSpeed(String auId);

	/**
	 * 未分页的定时器数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getSetTimes(Map<String, Object> parameters);

	/**
	 * 分页的定时器数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getSetTimesPage(Map<String, Object> parameters);

	/**
	 * 向短信记录表存短信记录
	 * @param parameters
	 */
	void addMsgRecord(Map<String, Object> parameters);

	/**
	 * 得到礼包的上线设置数据
	 * @param id
	 * @return
	 */
	Map<String, Object> getSetTime(String id);

	/**
	 * 停止定时器
	 * @param id
	 */
	void stopSetTime(String id);

	/**
	 * 对礼包数量进行增减
	 * @param parameters
	 */
	void addActivityNumber(Map<String, Object> parameters);

	/**
	 * 得到礼包的数量
	 * @param acId
	 * @return
	 */
	int getActivityNumber(String acId);

	/**
	 * 得到所有的定时任务
	 * @return
	 */
	List<Map<String, Object>> getAllSetTimes();

	/**
	 * 查出体验专区所有已领取未购买和待审核的礼包
	 * @return
	 */
	List<Map<String, Object>> getNoActivateActivity2();

	/**
	 * 查询此Id审核记录,判断是否重复审核
	 * @param auId
	 * @return
	 */
	Map<String, Object> getAuditById(String auId);

}
