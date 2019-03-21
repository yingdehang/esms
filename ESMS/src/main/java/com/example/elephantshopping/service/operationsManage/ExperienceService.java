package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.ExperienceDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class ExperienceService {
	@Autowired
	private ExperienceDao experienceDao;
	
	/**
	 * 未分页的活动列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getActivitis(Map<String, Object> parameters) {
		return experienceDao.getActivitis(parameters);
	}

	/**
	 * 分页的活动列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getActivitisPage(Map<String, Object> parameters) {
		List<Map<String,Object>> list = experienceDao.getActivitisPage(parameters);
		DateFormatUtils.timeforDateType(list, "ADDTIME");
		return list;
	}

	/**
	 * 开启/关闭活动
	 * @param parameters
	 */
	public void turn(Map<String, Object> parameters) {
		experienceDao.turn(parameters);
	}

	/**
	 * 改变活动排序
	 * @param parameters
	 */
	public void changeSort(Map<String, Object> parameters) {
		experienceDao.changeSort(parameters);
	}

	/**
	 * 添加活动
	 * @param parameters
	 */
	public void addActivity(Map<String, Object> parameters) {
		experienceDao.addActivity(parameters);
	}

	/**
	 * 通过Id得到某个活动信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getActivity(String id) {
		return experienceDao.getActivity(id);
	}
	
	/**
	 * 修改活动
	 * @param parameters
	 */
	public void modifyActivity(Map<String, Object> parameters) {
		experienceDao.modifyActivity(parameters);
	}

	/**
	 * 查询活动的商品
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getActivityGoods(Map<String, Object> parameters) {
		return experienceDao.getActivityGoods(parameters);
	}

	/**
	 * 查询活动的商品分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getActivityGoodsPage(Map<String, Object> parameters) {
		return experienceDao.getActivityGoodsPage(parameters);
	}

	/**
	 * 添加活动商品
	 * @param parameters
	 */
	public void addActivityGoods(Map<String, Object> parameters) {
		experienceDao.addActivityGoods(parameters);
	}

	/**
	 * 通过Id得到某个商品信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getActivityGoodsById(String id) {
		return experienceDao.getActivityGoodsById(id);
	}

	/**
	 * 修改商品
	 * @param parameters
	 */
	public void modifyActivityGoods(Map<String, Object> parameters) {
		experienceDao.modifyActivityGoods(parameters);
	}

	/**
	 * 删除数据库的商品数据
	 * @param id
	 */
	public void deleteGoods(String id) {
		experienceDao.deleteGoods(id);
	}

	/**
	 * 查询待审核的激活申请
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVerify(Map<String, Object> parameters) {
		return experienceDao.getVerify(parameters);
	}

	/**
	 * 查询待审核的激活申请分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVerifyPage(Map<String, Object> parameters) {
		List<Map<String,Object>> list = experienceDao.getVerifyPage(parameters);
		list = DateFormatUtils.timeforDateType(list, "PAY_TIME");
		return list;
	}

	/**
	 * 改变审核状态
	 * @param parameters
	 */
	public void experienceVerify(Map<String, Object> parameters) {
		experienceDao.experienceVerify(parameters);
	}

	/**
	 * 查询体验账户的信息
	 * @param accId
	 * @return
	 */
	public Map<String, Object> getAccountById(String accId) {
		return experienceDao.getAccountById(accId);
	}

	/**
	 * 查询体验账户零花钱
	 * @param accId
	 * @return
	 */
	public double getAccountPocketMoney(String accId) {
		return experienceDao.getAccountPocketMoney(accId);
	}
	
	/**
	 * 查询体验账户消费券
	 * @param accId
	 * @return
	 */
	public double getAccountConsumptionVolume(String accId) {
		return experienceDao.getAccountConsumptionVolume(accId);
	}

	/**
	 * 通过用户Id查询体验账号
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getAccountByUserId(String userId) {
		return experienceDao.getAccountByUserId(userId);
	}

	/**
	 * 为邀请人创建体验账号
	 * @param invitationId
	 */
	public void createAccount(String invitationId) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("invitationId",invitationId);
		parameters.put("acId", UUID.randomUUID().toString().replaceAll("-", ""));
		experienceDao.createAccount(parameters);
	}

	/**
	 * 加速
	 * @param acId
	 */
	public void addSpeed(String acId) {
		experienceDao.addSpeed(acId);
	}

	/**
	 * 体验专区审核表数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getRecord(Map<String, Object> parameters) {
		List<Map<String,Object>> list = experienceDao.getRecord(parameters);
		DateFormatUtils.timeforDateType(list, "ADDTIME");
		DateFormatUtils.timeforDateType(list, "ORDER_TIME");
		DateFormatUtils.timeforDateType(list, "PAY_TIME");
		DateFormatUtils.timeforDateType(list, "PASS_TIME");
		return list;
	}

	/**
	 * 体验专区审核表数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getRecordPage(Map<String, Object> parameters) {
		List<Map<String,Object>> list = experienceDao.getRecordPage(parameters);
		DateFormatUtils.timeforDateType(list, "ADDTIME");
		DateFormatUtils.timeforDateType(list, "ORDER_TIME");
		DateFormatUtils.timeforDateType(list, "PAY_TIME");
		DateFormatUtils.timeforDateType(list, "PASS_TIME");
		return list;
	}
	
	/**
	 * 查出体验专区所有已领取未购买的礼包
	 * @return
	 */
	public List<Map<String, Object>> getNoActivateActivity() {
		return experienceDao.getNoActivateActivity();
	}

	/**
	 * 将礼包状态改为已过期
	 * @param auId
	 */
	public void setActivityOverdue(String auId) {
		experienceDao.setActivityOverdue(auId);
	}

	/**
	 * 获得体验账号某一天的签到数据
	 * @param accId
	 * @param thisDay
	 * @return
	 */
	public Map<String, Object> getSign(String accId, String day) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accId", accId);
		parameters.put("day", day);
		return experienceDao.getSign(parameters);
	}

	/**
	 * 添加签到数据
	 * @param accId
	 * @param integral 扣除积分
	 * @param pocketMoney 增加零花钱
	 * @param consumption 增加消费券s
	 */
	public void addSign(String accId, double integral, double pocketMoney, double consumption) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("signId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("accId", accId);
		parameters.put("integral", integral);
		parameters.put("pocketMoney", pocketMoney);
		parameters.put("consumption", consumption);
		experienceDao.addSign(parameters);
	}

	/**
	 * 改变体验账户的积分零花钱消费券
	 * @param accId
	 * @param integral
	 * @param pocketMoney
	 * @param consumption
	 */
	public void accountSign(String accId, double integral, double pocketMoney, double consumption) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accId", accId);
		parameters.put("integral", integral);
		parameters.put("pocketMoney", pocketMoney);
		parameters.put("consumption", consumption);
		experienceDao.accountSign(parameters);
	}

	/**
	 * 得到用户电话
	 * @param userId
	 * @return
	 */
	public String getUserPhone(String userId) {
		return experienceDao.getUserPhone(userId);
	}

	/**
	 * 清除体验账号积分，零花钱，消费券
	 * @param accId
	 */
	public void clearAccountMoney(String accId) {
		experienceDao.clearAccountMoney(accId);
	}

	/**
	 * 清除体验账号的签到记录
	 * @param accId
	 * @param type 1:体验，2：购买
	 */
	public void clearAccountSign(String accId, int type) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accId", accId);
		parameters.put("type", type);
		experienceDao.clearAccountSign(parameters);
	}

	/**
	 * 得到体验账号的礼包审核记录
	 * @param accId
	 * @return
	 */
	public List<Map<String, Object>> getAudits(String accId) {
		return experienceDao.getAudits(accId);
	}

	/**
	 * 礼包积分加到体验账户
	 * @param integral
	 * @param accId 
	 */
	public void addExperienceIntegral(double integral, String accId) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accId", accId);
		parameters.put("integral", integral);
		experienceDao.addExperienceIntegral(parameters);
	}

	/**
	 * 得到体验账号的礼包审核通过记录
	 * @param accId
	 * @return
	 */
	public List<Map<String, Object>> getActivateAudits(String accId) {
		return experienceDao.getActivateAudits(accId);
	}

	/**
	 * 改变礼包数量
	 * @param id
	 * @param number
	 */
	public void changeActivityNumber(String id, int number) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", id);
		parameters.put("number", number);
		experienceDao.changeActivityNumber(parameters);
	}

	/**
	 * 增加象币明细
	 * @param type 类型（1：象币 2：体验象币）
	 * @param value 值
	 * @param userId 用户Id
	 * @param operation 操作(1：加   0：减)
	 */
	public void addElephantDetail(int type, double value, String userId, int operation) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("type", type);
		parameters.put("value", value);
		parameters.put("userId", userId);
		parameters.put("operation", operation);
		experienceDao.addElephantDetail(parameters);
	}

	/**
	 * 体验象币
	 * @param accId
	 * @return
	 */
	public double getAccountIntegral(String accId) {
		return experienceDao.getAccountIntegral(accId);
	}

	/**
	 * 购买专区增加积分零花钱和消费券
	 * @param accId
	 * @param integral
	 * @param pocketMoney
	 * @param consumption
	 */
	public void addBuyMoney(String accId, double integral, double pocketMoney, double consumption) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("accId",accId);
		parameters.put("integral",integral);
		parameters.put("pocketMoney",pocketMoney);
		parameters.put("consumption",consumption);
		experienceDao.addBuyMoney(parameters);
	}


	/**
	 * 清除象币明细
	 * @param userId
	 * @param type 1：象币 2：体验象币
	 */
	public void clearElephantDetail(String userId, int type) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId", userId);
		parameters.put("type", type);
		experienceDao.clearElephantDetail(parameters);
	}

	/**
	 * 新增礼包数量定时器
	 * @param id
	 * @param startTime
	 * @param endTime
	 * @param changed
	 */
	public void addSetTime(String id,String acId,String time,String fixed,String changed) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("sId",id);
		parameters.put("acId", acId);
		parameters.put("time", time);
		parameters.put("fixed", fixed);
		parameters.put("changed", changed);
		experienceDao.addSetTime(parameters);
	}

	/**
	 * 修改礼包数量定时器
	 * @param id
	 * @param time
	 * @param fixed
	 * @param changed
	 */
	public void updateSetTime(String id,String time,String fixed,String changed) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", id);
		parameters.put("time", time);
		parameters.put("fixed", fixed);
		parameters.put("changed", changed);
		experienceDao.updateSetTime(parameters);
	}

	/**
	 * 删除上线设置
	 * @param id
	 */
	public void deleteSetTime(String id) {
		experienceDao.deleteSetTime(id);
	}

	/**
	 * 获取用户已经加速的礼包数量
	 * @param accId
	 * @return
	 */
	public int isSpeed(String accId) {
		return experienceDao.isSpeed(accId);
	}

	/**
	 * 查询用户购买的礼包的总额
	 * @param accId
	 * @return
	 */
	public Double getSumPrice(String accId) {
		return experienceDao.getSumPrice(accId);
	}

	/**
	 * 修改此礼包IS_SPEED为1
	 * @param auId
	 */
	public void changeActivityIsSpeed(String auId) {
		experienceDao.changeActivityIsSpeed(auId);
	}

	/**
	 * 未分页的定时器数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getSetTimes(Map<String, Object> parameters) {
		return experienceDao.getSetTimes(parameters);
	}

	/**
	 * 分页的定时器数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getSetTimesPage(Map<String, Object> parameters) {
		List<Map<String,Object>> list = experienceDao.getSetTimesPage(parameters);
		list = DateFormatUtils.timeforDateType(list, "TIME");
		return list;
	}

	/**
	 * 向短信记录表存短信记录
	 * @param phone
	 * @param string
	 */
	public void addMsgRecord(String phone, String content) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("phone", phone);
		parameters.put("content", content);
		experienceDao.addMsgRecord(parameters);
	}

    /**
     * 得到礼包的上线设置数据
     * @param id
     * @return
     */
	public Map<String, Object> getSetTime(String id) {
		return experienceDao.getSetTime(id);
	}

	/**
	 * 停止定时器
	 * @param id
	 */
	public void stopSetTime(String id) {
		experienceDao.stopSetTime(id);
	}

	/**
	 * 对礼包数量进行增减
	 * @param acId
	 * @param changed
	 */
	public void addActivityNumber(String acId, int changed) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("acId", acId);
		parameters.put("changed", changed);
		experienceDao.addActivityNumber(parameters);
	}

	/**
	 * 得到礼包的数量
	 * @param acId
	 * @return
	 */
	public int getActivityNumber(String acId) {
		return experienceDao.getActivityNumber(acId);
	}

	/**
	 * 得到所有的定时任务
	 * @return
	 */
	public List<Map<String, Object>> getAllSetTimes() {
		return experienceDao.getAllSetTimes();
	}

	/**
	 * 查出体验专区所有已领取未购买和待审核的礼包
	 * @return
	 */
	public List<Map<String, Object>> getNoActivateActivity2() {
		return experienceDao.getNoActivateActivity2();
	}

	/**
	 * 查询此Id审核记录,判断是否重复审核
	 * @param auId
	 * @return
	 */
	public Map<String, Object> getAuditById(String auId) {
		return experienceDao.getAuditById(auId);
	}

}
