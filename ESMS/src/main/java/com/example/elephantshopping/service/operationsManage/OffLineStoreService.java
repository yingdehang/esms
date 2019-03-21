package com.example.elephantshopping.service.operationsManage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.merchants.MerchantsOrderDao;
import com.example.elephantshopping.dao.operationsManage.OffLineStoreDao;
import com.example.elephantshopping.dao.operationsManage.OnLineStoreDao;
import com.example.elephantshopping.service.userManage.MessageService;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.SendSMSUtils;

/**
 * 线下店铺Service
 * @author XB
 *
 */
@Service
public class OffLineStoreService 
{
	@Autowired
	private OffLineStoreDao offLineStoreDao;
	@Autowired
	private OnLineStoreDao onLineStoreDao;
	@Autowired
	private MerchantsOrderDao merchantsOrderDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ExperienceService experienceService;
	@Autowired
	private MessageService messageServie;
	/**
	 * 未认证的线下店铺数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getNoVerifyStore(Map<String, Object> parameters) 
	{
		return offLineStoreDao.getNoVerifyStore(parameters);
	}

	/**
	 * 未认证的店铺线下数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getNoVerifyStorePage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> offLineStorePage = offLineStoreDao.getNoVerifyStorePage(parameters);
		offLineStorePage = DateFormatUtils.timeforDateType(offLineStorePage, "TO_APPLY_FOR_TIME");
		return offLineStorePage;
	}

	/**
	 * 获取店铺所有分类
	 * @return
	 */
	public List<Map<String, Object>> getStoreClass()
	{
		return offLineStoreDao.getStoreClass();
	}

	/**
	 * 获取店铺信息
	 * @param certificationId
	 * @return
	 */
	public Map<String, Object> getStoreInfo(String certificationId)
	{
		return offLineStoreDao.getStoreInfo(certificationId);
	}

	/**
	 * 进行认证
	 * @param result
	 */
	public void verify(Map<String, Object> parameters) 
	{
		offLineStoreDao.verify(parameters);
	}

	/**
	 * 获取未分页的认证记录
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVerifyRecord(Map<String, Object> parameters) 
	{
		return offLineStoreDao.getVerifyRecord(parameters);
	}

	/**
	 * 获取已分页的认证记录
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getVerifyRecordPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> verifyRecord = offLineStoreDao.getVerifyRecordPage(parameters);
		DateFormatUtils.timeforDateType(verifyRecord, "TO_APPLY_FOR_TIME");
		DateFormatUtils.timeforDateType(verifyRecord, "CTIME");
		return verifyRecord;
	}

	/**
	 * 通过认证的线下店铺数据未分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOffLineStoreList(Map<String, Object> parameters) 
	{
		return offLineStoreDao.getOffLineStoreList(parameters);
	}

	/**
	 * 通过认证的线下店铺数据已分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOffLineStoreListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> offLineStoreListPage = offLineStoreDao.getOffLineStoreListPage(parameters);
		DateFormatUtils.timeforDateType(offLineStoreListPage, "OPEN_SHOP_TIME");
		return offLineStoreListPage;
	}

	/**
	 * 获取店铺状态（正常/冻结）
	 * @return
	 */
	public List<Map<String, Object>> getStoreState()
	{
		return offLineStoreDao.getStoreState();
	}

	/**
	 * 转换店铺状态（正常/冻结）
	 * @param parameters
	 */
	public void changeState(Map<String, Object> parameters) 
	{
		offLineStoreDao.changeState(parameters);
	}

	/**
	 * 改变店铺名字
	 * @param parameters
	 */
	public void changeName(Map<String, Object> parameters)
	{
		offLineStoreDao.changeName(parameters);
	}

	/**
	 * 改变店铺详细地址
	 * @param parameters
	 */
	public void changeAddr(Map<String, Object> parameters)
	{
		offLineStoreDao.changeAddr(parameters);
	}

	/**
	 * 改变店铺分类
	 * @param parameters
	 */
	public void changeClass(Map<String, Object> parameters)
	{
		offLineStoreDao.changeClass(parameters);
	}

	/**
	 * 改变店铺客服电话
	 * @param parameters
	 */
	public void changeServicePhone(Map<String, Object> parameters)
	{
		offLineStoreDao.changeServicePhone(parameters);
	}
	
	/**
	 * 定时器  每月初改变店铺等级
	 * @param id
	 */
	@Scheduled(cron = "0 0 0 1 * ?")
	public void changeLevel()
	{
		List<Map<String,Object>> storeCertificationList = offLineStoreDao.getStoreCertificationList();//取出所有的通过认证的店铺的认证信息
		for(Map<String,Object> map : storeCertificationList)
		{
			String id = (String) map.get("STORE_CERTIFICATION_ID");
			String newLevel = (String) map.get("WAITING_STATE");
			if(newLevel!=null&&(!newLevel.equals("")))
			{
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("id", id);
				parameters.put("newLevel", newLevel);
				offLineStoreDao.changeLevel(parameters);//将店铺等级替换为新的等级并删除WAITING_STATE字段的值
			}
		}
	}

	/**
	 * 定时器  每天零时体验专区礼包自动过期 
	 * @param id
	 */
	@Scheduled(cron = "59 59 23 * * ?")
//	@Scheduled(cron = "0 * * * * ?")
	@Transactional
	public void activityOverdue()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String thisDay = sdf.format(new Date());//当天
		//查出体验专区所有已领取未购买的礼包
		List<Map<String,Object>> activitis = experienceService.getNoActivateActivity();
		for(Map<String,Object> m:activitis)
		{
			String addTime = sdf.format(m.get("ADDTIME"));//领取时间
			int validTime = (int) m.get("VALID_TIME");//有效期限
			List<String> days = DateFormatUtils.getDays(addTime, thisDay);
			if(days.size()-1==validTime)//过期
			{
				String auId = (String) m.get("EEAUID");
				//将礼包状态改为已过期
				experienceService.setActivityOverdue(auId);
				String accId = (String) m.get("EEAID");//体验账号Id
				//清除体验账号体验积分，体验零花钱，体验消费券
				experienceService.clearAccountMoney(accId);
				//清除体验专区签到记录
				experienceService.clearAccountSign(accId,1);
				//清除象币明细
				String userId = (String) m.get("USERS_ID");
				experienceService.clearElephantDetail(userId,2);
			}
		}
	}
	
	/**
	 * 定时器  体验专区礼包到期两天前提醒
	 * @param id
	 */
	@Transactional
	@Scheduled(cron = "0 0 10 * * ?")
	public void overdueRemind()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String thisDay = sdf.format(new Date());//当天
		//查出体验专区所有已领取未购买的礼包
		List<Map<String,Object>> activitis = experienceService.getNoActivateActivity();
		for(Map<String,Object> m:activitis)
		{
			String addTime = sdf.format(m.get("ADDTIME"));//领取时间
			int validTime = (int) m.get("VALID_TIME");//有效期限
			List<String> days = DateFormatUtils.getDays(addTime, thisDay);
			if(validTime-days.size()==0)//即将过期
			{
				String auId = (String) m.get("EEAUID");
				String phone = (String) m.get("userPhone");
				//向用户发送短信
				SendSMSUtils.orverdue(phone);
				//向短信记录表存短信记录
				experienceService.addMsgRecord(phone,"您领取的体验礼包将在明晚24点过期，快来看看吧，及时激活才能继续领取奖励金哦！【象本商城】");
			}
		}
	}
	
	/**
	 * 定时器 每天22点给未激活礼包的体验账号自动签到
	 */
	@Transactional
	@Scheduled(cron = "0 0 22 * * ?")
//	@Scheduled(cron = "0 * * * * ?")
	public void autoSing()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String thisDay = sdf.format(new Date());//当天
		//查出体验专区所有已领取未购买和待审核的礼包
		List<Map<String,Object>> activitis = experienceService.getNoActivateActivity2();
		for(Map<String,Object> m:activitis)
		{
			String accId = (String) m.get("EEAID");//体验账号的Id
			Map<String,Object> sign = experienceService.getSign(accId, thisDay);
			if(sign==null)//判断今天是否签到
			{
				String userId = (String) m.get("USERS_ID");//用户Id
				BigDecimal integral = (BigDecimal) m.get("INTEGRAL");
				BigDecimal speed = (BigDecimal) m.get("SPEED");
				if(integral.doubleValue()>0){
					double consumeIntegral = integral.doubleValue()*(0.003+speed.doubleValue());//消耗的积分
					BigDecimal b1 = new BigDecimal(consumeIntegral*0.5).setScale(2, BigDecimal.ROUND_HALF_UP);
					double money = b1.doubleValue();
					experienceService.addSign(accId,money*2,money,money);//添加体验专区签到数据
					experienceService.accountSign(accId,money*2,money,money);//改变体验账户的体验积分零花钱消费券
					//增加象币明细
					experienceService.addElephantDetail(2,money*2,userId,0);
					if(null != m.get("PUSH_ID"))
					{
						//给用户发推送消息
						String pushId = m.get("PUSH_ID").toString();
						Map<String,Object> account = experienceService.getAccountById(accId);//体验账号信息
						BigDecimal pocketMoney = (BigDecimal) account.get("POCKETMONEY");
						BigDecimal consumption = (BigDecimal) account.get("CONSUMPTION_VOLUME");
						messageServie.pushById(userId, pushId,"温馨提示","自动签到成功，今日签到得到"+String.valueOf(money)+"零花钱，"+String.valueOf(money)+"消费券。合计签到："+pocketMoney.toString()+"零花钱，"+consumption.toString()+"消费券。");
					}
				}
			}
		}
	}
	
	/**
	 * 定时器  每月最后一天12:00改变会员等级
	 * @param id
	 */
	@Scheduled(cron = "0 0 12 * * ?")
	public void changeMembershipGrade()
	{
		String thisMonth = new SimpleDateFormat("yyyy-MM").format(new Date());//当月
		String thisDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());//当天
		//得到当月最后一天
		String lastDay = DateFormatUtils.getLastDayOfMonth(thisMonth);
		String startTime = thisMonth+"-01 00:00:00";//查询的开始时间
		String endTime = lastDay+" 12:00:00";//查询的结束时间
		//1.判断当天是不是本月最后一天
		if(thisDay.equals(lastDay))
		{
			//2.取出不是当月注册的用户
			List<Map<String,Object>> users = offLineStoreDao.getUsersExceptThisMonth(thisMonth);
			for(Map<String,Object> map:users)
			{
				String userId = (String) map.get("USERS_ID");//用户Id
				String phone = (String) map.get("PHONE");//用户电话
				BigDecimal integral = (BigDecimal) map.get("INTEGRAL");//用户积分
				double i = integral.doubleValue();
				Map<String,Object> parameters = new HashMap<String,Object>();
				parameters.put("userId",userId);
				parameters.put("phone",phone);
				parameters.put("startTime",startTime);
				parameters.put("endTime",endTime);
				double om = 0;
				double opm = 0;
				double ldm = 0;
				double bdm = 0;
				Double orderMoney = offLineStoreDao.getOrderMoney(parameters);//查询用户本月订单总额
				if(orderMoney!=null)
				{
					om = orderMoney.doubleValue();
				}
//				Double offLinePayMoney = offLineStoreDao.getOffLinePayMoney(parameters);//查询用户本月线下店铺支付总额
//				if(offLinePayMoney!=null)
//				{
//					opm = offLinePayMoney.doubleValue();
//				}
//				Double littleDeclarationMoney = offLineStoreDao.getLittleDeclarationMoney(parameters);//查询用户本月小额报单总额
//				if(littleDeclarationMoney!=null)
//				{
//					ldm = littleDeclarationMoney.doubleValue();
//				}
//				Double bigDeclarationMoney = offLineStoreDao.getBigDeclarationMoney(parameters);//查询用户本月大额报单总额
//				if(bigDeclarationMoney!=null)
//				{
//					bdm = bigDeclarationMoney.doubleValue();
//				}
				//3.算出用户当月消费的余额占当前积分的比例
				double ratio = 0;
				if(i!=0)
				{
					ratio = (om+opm+ldm+bdm)/i;
				}
				else if(i==0&&om==0)
				{
					ratio = 0;
				}
				else
				{
					ratio = 0.01;
				}
				if(ratio<0.0033)//初级会员
				{
					parameters.put("grade", "USER_DJ_PRIMARY");
					offLineStoreDao.changeMembershipGrade(parameters);
				}
				else if(0.0033<=ratio&&ratio<0.01)//中级会员
				{
					parameters.put("grade", "USER_DJ_INTERMEDIATE");
					offLineStoreDao.changeMembershipGrade(parameters);
				}
				else//高级会员
				{
					parameters.put("grade", "USER_DJ_SENIOR");
					offLineStoreDao.changeMembershipGrade(parameters);
				}
			}
		}
	}
	
//	/**
//	 * 每天晚上23点59分59秒将五天未发货的订单取消
//	 */
//	@Scheduled(cron = "59 59 23 * * ?")
//	public void cancleOrder()
//	{
//		List<Map<String,Object>> orders = offLineStoreDao.getPayOrder();//得到已付款的订单
//		for(Map<String,Object> map:orders)
//		{
//			try {
//				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//				String orderNumber = (String) map.get("ORDER_NUMBER");
//				Timestamp pt = (Timestamp) map.get("PAY_TIME");
//				String payTime = format.format(pt);
//				String nowDay = format.format(new Date());//当前日期
//				Date date = format.parse(payTime.substring(0,10));
//				Calendar c = Calendar.getInstance();
//				c.setTime(date);
//				c.add(Calendar.DAY_OF_MONTH, 4);
//				String payDay = format.format(c.getTime());
//				if(payDay.equals(nowDay))//五天未发货的订单，取消并退款
//				{
//					List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(orderNumber);
//					//判断此订单里是否有已发货的订单
//					boolean sended = false;
//					for(Map<String,Object> g : goods)
//					{
//						String orderState = (String) g.get("ORDER_STATE");
//						if(orderState.equals("ORDER_STATE_NO_GOODS"))
//						{
//							sended = true;
//						}
//					}
//					if(sended == false)//同一个订单编号里的所有订单都没发货才可以取消
//					{
//						offLineStoreDao.cancleOrder(orderNumber);//取消订单
//						String userId = (String) map.get("USERS_ID");//用户Id
//						String payType = (String) map.get("PAY_TYPE");//支付类型
//						double sumPrice = 0;//金额
//						for(Map<String,Object> m : goods)
//						{
//							int number =  (int) m.get("NUMBER");
//							BigDecimal price = (BigDecimal) m.get("PRICE");
//							sumPrice += price.doubleValue()*number;
//						}
//						//将订单金额返还给买家
//						if(payType.equals("0"))//零花钱
//						{
//							orderService.changeUserPocketMoney("1", userId, sumPrice, "订单取消");
//						}
//						else if(payType.equals("1"))//余额
//						{
//							orderService.changeUserMoneys("1", userId, sumPrice, "订单取消");
//						}
//						else//消费券
//						{
//							orderService.changeUserConsumption("1", userId, sumPrice, "订单取消");
//						}
//					}
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	/**
	 * 获取店铺所有等级
	 * @return
	 */
	public List<Map<String, Object>> getStoreLevel()
	{
		return offLineStoreDao.getStoreLevel();
	}

	/**
	 * 为店铺添加一个待改变的等级
	 * @param parameters
	 */
	public void addNewLevel(Map<String, Object> parameters)
	{
		offLineStoreDao.addNewLevel(parameters);
	}

	/**
	 * 获取店铺认证状态
	 * @return
	 */
	public List<Map<String, Object>> getVerifyState() 
	{
		return offLineStoreDao.getVerifyState();
	}

	/**
	 * 改变店铺的状态为正常
	 * @param storeId
	 */
	public void changeStoreStateNormal(String storeId) 
	{
		offLineStoreDao.changeStoreStateNormal(storeId);
	}

	/**
	 * 查询线下店铺某月/天的收益
	 * @param storeId
	 * @param time
	 * @return
	 */
	public Map<String, Object> getStoreWater(String storeId, String time,String userId) 
	{
		Map<String,Object> waters = new HashMap<String,Object>();
		double gMoney = 0;
		double gCommission = 0;
		double dMoney = 0;
		double dCommission = 0;
		double allCommission = 0;
		//收款流水
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("storeId", storeId);
		parameters.put("typeName", "扫一扫支付收益");
		parameters.put("time", "%"+time+"%");
		Map<String,Object> gWaters = onLineStoreDao.getStoreWater(parameters);
		if(gWaters!=null)
		{
			BigDecimal b1 = (BigDecimal) gWaters.get("MONEY");
			BigDecimal b2 = (BigDecimal) gWaters.get("COMMISSION");
			gMoney = b1.doubleValue();
			gCommission = b2.doubleValue();
		}
		//报单流水
		Map<String,Object> parameters2 = new HashMap<String,Object>();
		parameters2.put("storeId", storeId);
		parameters2.put("typeName", "报单");
		parameters2.put("time", "%"+time+"%");
		Map<String,Object> dWaters = onLineStoreDao.getStoreWater(parameters2);
		if(dWaters!=null)
		{
			BigDecimal b1 = ((BigDecimal) dWaters.get("MONEY")).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal b2 = ((BigDecimal) dWaters.get("COMMISSION")).setScale(2, BigDecimal.ROUND_HALF_UP);
			dMoney = b1.doubleValue();
			dCommission = b2.doubleValue();
		}
		//大额报单
		Map<String,Object> parameters3 = new HashMap<String,Object>();
		parameters3.put("userId", userId);
		parameters3.put("time", "%"+time+"%");
		Map<String,Object> dWaters2 = offLineStoreDao.getDeclarationWater(parameters3);
		if(dWaters2!=null)
		{
			BigDecimal b1 = ((BigDecimal) dWaters2.get("MONEY")).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal b2 = ((BigDecimal) dWaters2.get("COMMISSION")).setScale(2, BigDecimal.ROUND_HALF_UP);
			dMoney = dMoney+b1.doubleValue();
			dCommission = dCommission+b2.doubleValue();
		}
		allCommission = gCommission+dCommission;
		DecimalFormat df=new DecimalFormat("0.00");
		waters.put("gMoney",new Double(df.format(gMoney).toString()));
		waters.put("gCommission",new Double(df.format(gCommission).toString()));
		waters.put("dMoney",new Double(df.format(dMoney).toString()));
		waters.put("dCommission",new Double(df.format(dCommission).toString()));
		waters.put("allCommission",new Double(df.format(allCommission).toString()));
		waters.put("time", time);
		
		return waters;
	}

	/**
	 * 得到店铺的信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getOffLineStoreInfoById(String storeId)
	{
		return offLineStoreDao.getOffLineStoreInfoById(storeId);
	}
}
