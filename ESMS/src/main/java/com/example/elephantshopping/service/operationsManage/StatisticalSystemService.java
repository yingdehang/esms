package com.example.elephantshopping.service.operationsManage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.operationsManage.StatisticalSystemDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class StatisticalSystemService {
	@Autowired
	private StatisticalSystemDao statisticalSystemDao;

	/**
	 * 查询数据
	 * 
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> queryStatisticalData(String areaId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 今日零点时间
		String toDayZeroTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00";
		if (null != areaId && (!areaId.equals(""))) {
			map.put("areaId", areaId);
		}
		map.put("endTime", toDayZeroTime);
		// 总用户数
		int usersCount = statisticalSystemDao.queryUserCount(map);
		// 总实名注册户数
		int userPassCount = statisticalSystemDao.queryUserPassCount(map);
		// 当前总积分
		Double totalScore = statisticalSystemDao.queryTotalScore(map);
		// 普通用户数及七天新增，（普通用户，实名注册用户）
		Map<String, Object> querymap = DateFormatUtils.betweenDateTimeMap(7 * 24 * 60 * 60 * 1000);
		if (null != areaId && (!areaId.equals(""))) {
			querymap.put("areaId", areaId);
		}
		int newUserCount = statisticalSystemDao.queryUserCount(querymap);
		int newUserPasscount = statisticalSystemDao.queryUserPassCount(querymap);
		// 线上店铺及七天新增
		int xsStoreCount = statisticalSystemDao.getXsStoreCount(map);
		int newXsStoreCount = statisticalSystemDao.getXsStoreCount(querymap);
		// 线下店铺及七天新增
		int xxStoreCount = statisticalSystemDao.getXxStoreCount(map);
		int newXxStoreCount = statisticalSystemDao.getXxStoreCount(querymap);

		// 昨日：佣金
		Map<String, Object> yesterday = DateFormatUtils.betweenDateTimeMap(1 * 24 * 60 * 60 * 1000);
		if (null != areaId && (!areaId.equals(""))) {
			yesterday.put("areaId", areaId);
		}
		// 线上店铺产生的佣金
		double xsCommission = statisticalSystemDao.queryYesterdayXsCommission(yesterday);
		// 线下店铺扫码支付产生的佣金
		double xxSmCommission = statisticalSystemDao.queryYesterdayXxSmCommission(yesterday);
		// 线下店铺报单产生的佣金
		double xxBdCommission = statisticalSystemDao.queryYesterdayXxBdCommission(yesterday);
		// 昨日佣金
		double commission = xsCommission + xxSmCommission + xxBdCommission;
		// 奖励金支出
		double incentivePayments = statisticalSystemDao.queryYesterdayIncentivePayments(yesterday);
		// 积分赠送
		double integralPresent = statisticalSystemDao.queryYesterdayIntegralPresent(yesterday);
		// 积分变化
		double integralChange = statisticalSystemDao.queryYesterdayIntegralChange(yesterday);

		// 充值金额
		double rechargeAmount = statisticalSystemDao.queryYesterdayRechargeAmount(yesterday);

		// 打包返回map
		map.put("usersCount", usersCount);
		map.put("userPassCount", userPassCount);
		map.put("totalScore", String.format("%.2f", totalScore));
		map.put("newUserCount", newUserCount);
		map.put("newUserPasscount", newUserPasscount);
		map.put("xsStoreCount", xsStoreCount);
		map.put("newXsStoreCount", newXsStoreCount);
		map.put("xxStoreCount", xxStoreCount);
		map.put("newXxStoreCount", newXxStoreCount);
		map.put("commission", String.format("%.2f", commission));
		map.put("incentivePayments", String.format("%.2f", incentivePayments));
		map.put("integralPresent", String.format("%.2f", integralPresent));
		map.put("integralChange", String.format("%.2f", integralChange));
		map.put("rechargeAmount", String.format("%.2f", rechargeAmount));
		return map;
	}

	/**
	 * 查询佣金收益
	 * 
	 * @param dataType
	 * @param areaId
	 * @return
	 */
	public Map<String, Object> getEarningsTrendData(String dataType, String areaId) {
		Map<String, Object> datamap = new HashMap<String, Object>();
		// 线上店铺佣金数组
		Double[] Onlinetoreevenue = null;
		// 实体店铺佣金数组
		Double[] entityShopevenue = null;
		// 总佣金
		Double[] onlineAndEntity = null;
		switch (dataType) {
		// 近七天
		case "qitian":
			Onlinetoreevenue = new Double[7];
			entityShopevenue = new Double[7];
			onlineAndEntity = new Double[7];
			datamap.put("oneX", new String[] { "七天前", "六天前", "五天前", "四天前", "三天前", "二天前", "一天前" });
			for (int i = 0; i < Onlinetoreevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(7 - i, "day");
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringday.put("areaId", areaId);
				}
				double commission = statisticalSystemDao.queryYesterdayXsCommission(duringday);
				Onlinetoreevenue[i] = commission;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(7 - i, "day");
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringday.put("areaId", areaId);
				}
				double commission1 = statisticalSystemDao.queryYesterdayXxBdCommission(duringday);
				double commission2 = statisticalSystemDao.queryYesterdayXxSmCommission(duringday);
				entityShopevenue[i] = commission1 + commission2;
			}
			datamap.put("entityShopevenue", entityShopevenue);
			for (int i = 0; i < onlineAndEntity.length; i++) {
				onlineAndEntity[i] = Onlinetoreevenue[i] + entityShopevenue[i];
			}
			datamap.put("onlineAndEntity", onlineAndEntity);
			break;
		// 近一月
		case "onemonth":
			datamap.put("oneX", new String[] { "四周前", "三周前", "二周前", "一周前" });
			Onlinetoreevenue = new Double[4];
			entityShopevenue = new Double[4];
			onlineAndEntity = new Double[4];
			// 线上店铺佣金数组
			for (int i = 0; i < Onlinetoreevenue.length; i++) {
				Map<String, Object> duringmonth = DateFormatUtils.betweenDaoTimeMap(4 - i, "week");
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringmonth.put("areaId", areaId);
				}
				Double evenue = statisticalSystemDao.queryYesterdayXsCommission(duringmonth);
				Onlinetoreevenue[i] = evenue;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			// 实体店铺佣金数组
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(7 - i, "week");
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringday.put("areaId", areaId);
				}
				double commission1 = statisticalSystemDao.queryYesterdayXxBdCommission(duringday);
				double commission2 = statisticalSystemDao.queryYesterdayXxSmCommission(duringday);
				entityShopevenue[i] = commission1 + commission2;
			}
			datamap.put("entityShopevenue", entityShopevenue);
			for (int i = 0; i < onlineAndEntity.length; i++) {
				onlineAndEntity[i] = Onlinetoreevenue[i] + entityShopevenue[i];
			}
			datamap.put("onlineAndEntity", onlineAndEntity);
			break;
		// 近一季度
		case "tweyear":
			datamap.put("oneX", new String[] { "12月前", "11月前", "10月前", "9月前", "8月前", "7月前", "6月前", "5月前", "4月前", "3月前",
					"2月前", "1月前" });
			Onlinetoreevenue = new Double[12];
			entityShopevenue = new Double[12];
			onlineAndEntity = new Double[12];
			// 线上店铺佣金数组
			for (int i = 0; i < Onlinetoreevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenMonth(12 - i);
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringday.put("areaId", areaId);
				}
				Double evenue = statisticalSystemDao.queryYesterdayXsCommission(duringday);
				Onlinetoreevenue[i] = evenue;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			// 实体店铺佣金数组
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenMonth(12 - i);
				if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
					duringday.put("areaId", areaId);
				}
				double commission1 = statisticalSystemDao.queryYesterdayXxBdCommission(duringday);
				double commission2 = statisticalSystemDao.queryYesterdayXxSmCommission(duringday);
				entityShopevenue[i] = commission1 + commission2;
			}
			datamap.put("entityShopevenue", entityShopevenue);
			for (int i = 0; i < onlineAndEntity.length; i++) {
				onlineAndEntity[i] = Onlinetoreevenue[i] + entityShopevenue[i];
			}
			datamap.put("onlineAndEntity", onlineAndEntity);
			break;
		default:
			break;
		}
		return datamap;
	}

	/**
	 * 查询店铺等级分布
	 * 
	 * @param areaId
	 * @return
	 */
	public Integer[] getShopGradeDistributionData(String areaId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
			map.put("areaId", areaId);
		}
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
		// 店铺等级,金银铜
		String[] storeDJ = { "STORE_DJ_GOLD", "STORE_DJ_SILVER", "STORE_DJ_COPPER" };
		// 店铺类型，线上线下
		String[] StoreType = { "STORE_XS_XS", "STORE_XS_XX" };
		int len = storeDJ.length * StoreType.length;
		Integer[] data = new Integer[len];
		int n = 0;
		for (int i = 0; i < StoreType.length; i++) {
			for (int j = 0; j < storeDJ.length; j++) {
				map.put("TYPES", StoreType[i]);
				map.put("LEVELS", storeDJ[j]);
				data[n++] = statisticalSystemDao.getShopGradeDistributionData(map);
			}
		}
		return data;
	}

	/**
	 * 会员充值金额占比
	 * 
	 * @param areaId
	 * @return
	 */
	public double[] getmembershipRechargeAmountData(String areaId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
			map.put("areaId", areaId);
		}
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
		// 会员字典数组,初级、中级、高级
		String[] MEMBERSHIP_GRADE = { "USER_DJ_PRIMARY", "USER_DJ_INTERMEDIATE", "USER_DJ_SENIOR" };
		double[] data = new double[] { 0.00, 0.00, 0.00 };
		for (int i = 0; i < data.length; i++) {
			map.put("MEMBERSHIP_GRADE", MEMBERSHIP_GRADE[i]);
			Double chongzi = statisticalSystemDao.getmembershipRechargeAmountData(map);
			if (null != chongzi) {
				data[i] = chongzi;
			}
		}
		return data;
	}

	/**
	 * 会员等级分布
	 * 
	 * @param areaId
	 * @return
	 */
	public int[] membershipistributionData(String areaId) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
			map.put("areaId", areaId);
		}
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
		// 会员字典数组
		String[] MEMBERSHIP_GRADE = { "USER_DJ_PRIMARY", "USER_DJ_INTERMEDIATE", "USER_DJ_SENIOR" };
		int[] data = new int[3];
		for (int i = 0; i < data.length; i++) {
			map.put("MEMBERSHIP_GRADE", MEMBERSHIP_GRADE[i]);
			data[i] = statisticalSystemDao.membershipistributionData(map);
		}
		return data;
	}

	/**
	 * 查询每月流水
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getRunningwaterlist(String areaId) {
		// 查询从上线到现在有多少个月了
		String[] months = DateFormatUtils.GetqueryMonthlyPeriod();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-")[2];
		int start = 0;
		if (day.equals("01")) {
			start++;
		}
		for (int i = 0; i < months.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			Map<String, Object> query = DateFormatUtils.betweenMonth(start++);
			if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
				query.put("areaId", areaId);
			}
			m.put("yearMonth", months[i]);
			m.put("clearingTime", query.get("endTime"));

			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			double offlineWater = statisticalSystemDao.queryToreEvenue(query);
			m.put("offlineWater", offlineWater);

			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			double onlineWater = statisticalSystemDao.queryToreEvenue(query);
			m.put("onlineWater", onlineWater);

			// 店铺总货款
			double countWater = offlineWater + onlineWater;
			m.put("countWater", String.format("%.2f", countWater));

			/* 佣金收益 */
			// 线上店铺产生的佣金
			double xsCommission = statisticalSystemDao.queryYesterdayXsCommission(query);
			// 线下店铺扫码支付产生的佣金
			double xxSmCommission = statisticalSystemDao.queryYesterdayXxSmCommission(query);
			// 线下店铺报单产生的佣金
			double xxBdCommission = statisticalSystemDao.queryYesterdayXxBdCommission(query);
			m.put("xsCommissionIncome", String.format("%.2f", xsCommission));
			m.put("xxCommissionIncome", String.format("%.2f", xxSmCommission + xxBdCommission));
			m.put("commissionIncome", String.format("%.2f", xsCommission + xxSmCommission + xxBdCommission));

			// 总流水=总货款+总佣金
			m.put("countWaterAndCommissionIncome",
					String.format("%.2f", countWater + xsCommission + xxSmCommission + xxBdCommission));
			// 新用户数
			int newUsers = statisticalSystemDao.queryUserCount(query);
			m.put("newUsers", newUsers);
			// 新实名注册户数
			int newPassUsers = statisticalSystemDao.queryUserPassCount(query);
			m.put("newPassUsers", newPassUsers);
			// 月充值总额
			double monthlyRecharge = statisticalSystemDao.queryYesterdayRechargeAmount(query);
			m.put("monthlyRecharge", monthlyRecharge);
			// 积分赠送
			double integralPresent = statisticalSystemDao.queryYesterdayIntegralPresent(query);
			m.put("integralPresent", String.format("%.2f", integralPresent));
			// 签到积分消耗
			double signIntegralConsumption = statisticalSystemDao.queryYesterdayIncentivePayments(query);
			m.put("signIntegralConsumption", String.format("%.2f", signIntegralConsumption));
			// 其它积分消耗
			m.put("otherIntegralConsumption", 0.00);
			// 签到奖励金
			m.put("signBonus", String.format("%.2f", signIntegralConsumption));
			m.put("index", start - 1);
			// 积分变化
			double integralChange = statisticalSystemDao.queryYesterdayIntegralChange(query);
			m.put("integralChange", String.format("%.2f", integralChange));
			if (null == areaId || areaId.equals("") || areaId.equals("0")) {
				// 平台总积分
				query.put("dtype", "1");
				double totalPlatformIntegral = statisticalSystemDao.queryDataTable(query);
				if (totalPlatformIntegral == 0.00) {
					m.put("totalPlatformIntegral", "未统计");
				} else {
					m.put("totalPlatformIntegral", String.format("%.2f", totalPlatformIntegral));
				}

				// 平台总余额
				query.put("dtype", "2");
				double totalBalance = statisticalSystemDao.queryDataTable(query);
				if (totalBalance == 0.00) {
					m.put("totalBalance", "未统计");
				} else {
					m.put("totalBalance", String.format("%.2f", totalBalance));
				}
				// 平台总零花钱
				query.put("dtype", "3");
				double totalMoney = statisticalSystemDao.queryDataTable(query);
				if (totalMoney == 0.00) {
					m.put("totalMoney", "未统计");
				} else {
					m.put("totalMoney", String.format("%.2f", totalMoney));
				}
				// 平台总消费券
				query.put("dtype", "4");
				double totalConsumptionCoupon = statisticalSystemDao.queryDataTable(query);
				if (totalConsumptionCoupon == 0.00) {
					m.put("totalConsumptionCoupon", "未统计");
				} else {
					m.put("totalConsumptionCoupon", String.format("%.2f", totalConsumptionCoupon));
				}

				// 积分赠送/佣金
				double commissionIncome = xsCommission + xxSmCommission + xxBdCommission;
				double integralOverCommission = integralPresent / commissionIncome;
				m.put("integralOverCommission", String.format("%.2f", integralOverCommission));
			}
			// 物流服务费
			double logisticsServiceCharge = queryLogisticsServiceCharge(query);
			m.put("logisticsServiceCharge", String.format("%.2f", logisticsServiceCharge));
			list.add(m);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "");
		map.put("count", list.size());
		map.put("data", list);
		map.put("code", 0);
		return map;
	}

	/**
	 * 获取导出list
	 * 
	 * @param indexs
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getRunningwaterDaochu(String indexs, String areaId) {
		// 查询地区id
		String[] index = indexs.split(";");
		// 查询从上线到现在有多少个月了
		String[] months = DateFormatUtils.GetqueryMonthlyPeriod();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < index.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			int n = Integer.parseInt(index[i]);
			Map<String, Object> query = DateFormatUtils.betweenMonth(n);
			if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
				query.put("areaId", areaId);
			}
			m.put("yearMonth", months[n]);
			m.put("clearingTime", query.get("endTime"));

			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			double offlineWater = statisticalSystemDao.queryToreEvenue(query);
			m.put("offlineWater", String.format("%.2f", offlineWater));

			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			double onlineWater = statisticalSystemDao.queryToreEvenue(query);
			m.put("onlineWater", String.format("%.2f", onlineWater));

			// 店铺总流水
			double countWater = offlineWater + onlineWater;
			m.put("countWater", String.format("%.2f", countWater));

			/* 佣金收益 */
			// 线上店铺产生的佣金
			double xsCommission = statisticalSystemDao.queryYesterdayXsCommission(query);
			// 线下店铺扫码支付产生的佣金
			double xxSmCommission = statisticalSystemDao.queryYesterdayXxSmCommission(query);
			// 线下店铺报单产生的佣金
			double xxBdCommission = statisticalSystemDao.queryYesterdayXxBdCommission(query);
			m.put("xsCommissionIncome", String.format("%.2f", xsCommission));
			m.put("xxCommissionIncome", String.format("%.2f", xxSmCommission + xxBdCommission));
			m.put("commissionIncome", String.format("%.2f", xsCommission + xxSmCommission + xxBdCommission));
			// 总流水=总货款+总佣金
			m.put("countWaterAndCommissionIncome",
					String.format("%.2f", countWater + xsCommission + xxSmCommission + xxBdCommission));
			// 新用户数
			int newUsers = statisticalSystemDao.queryUserCount(query);
			m.put("newUsers", newUsers);
			// 新实名注册户数
			int newPassUsers = statisticalSystemDao.queryUserPassCount(query);
			m.put("newPassUsers", newPassUsers);
			// 月充值总额
			double monthlyRecharge = statisticalSystemDao.queryYesterdayRechargeAmount(query);
			m.put("monthlyRecharge", String.format("%.2f", monthlyRecharge));
			// 积分赠送
			double integralPresent = statisticalSystemDao.queryYesterdayIntegralPresent(query);
			m.put("integralPresent", String.format("%.2f", integralPresent));
			// 签到积分消耗
			double signIntegralConsumption = statisticalSystemDao.queryYesterdayIncentivePayments(query);
			m.put("signIntegralConsumption", String.format("%.2f", signIntegralConsumption));
			// 其它积分消耗
			m.put("otherIntegralConsumption", 0.00);
			// 签到奖励金
			m.put("signBonus", String.format("%.2f", signIntegralConsumption));

			// 积分变化
			double integralChange = statisticalSystemDao.queryYesterdayIntegralChange(query);
			m.put("integralChange", String.format("%.2f", integralChange));
			if (null == areaId || areaId.equals("") || areaId.equals("0")) {
				// 平台总积分
				query.put("dtype", "1");
				double totalPlatformIntegral = statisticalSystemDao.queryDataTable(query);
				if (totalPlatformIntegral == 0.00) {
					m.put("totalPlatformIntegral", "未统计");
				} else {
					m.put("totalPlatformIntegral", String.format("%.2f", totalPlatformIntegral));
				}
				// 平台总余额
				query.put("dtype", "2");
				double totalBalance = statisticalSystemDao.queryDataTable(query);
				if (totalBalance == 0.00) {
					m.put("totalBalance", "未统计");
				} else {
					m.put("totalBalance", String.format("%.2f", totalBalance));
				}
				// 平台总零花钱
				query.put("dtype", "3");
				double totalMoney = statisticalSystemDao.queryDataTable(query);
				if (totalMoney == 0.00) {
					m.put("totalMoney", "未统计");
				} else {
					m.put("totalMoney", String.format("%.2f", totalMoney));
				}
				// 平台总消费券
				query.put("dtype", "4");
				double totalConsumptionCoupon = statisticalSystemDao.queryDataTable(query);
				if (totalConsumptionCoupon == 0.00) {
					m.put("totalConsumptionCoupon", "未统计");
				} else {
					m.put("totalConsumptionCoupon", String.format("%.2f", totalConsumptionCoupon));
				}
				// 积分赠送/佣金
				double commissionIncome = xsCommission + xxSmCommission + xxBdCommission;
				double integralOverCommission = integralPresent / commissionIncome;
				m.put("integralOverCommission", String.format("%.2f", integralOverCommission));
			}
			// 物流服务费
			double logisticsServiceCharge = queryLogisticsServiceCharge(query);
			m.put("logisticsServiceCharge", String.format("%.2f", logisticsServiceCharge));
			list.add(m);
		}
		return list;
	}

	/**
	 * 查询月流水明细
	 * 
	 * @param month
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	public Map<String, Object> getdayWaterlist(String month, String areaId) {
		List<Map<String, Object>> querylist = DateFormatUtils.getMonthDayMap(month);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> query : querylist) {
			if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
				query.put("areaId", areaId);
			}
			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			double offlineWater = statisticalSystemDao.queryToreEvenue(query);
			query.put("offlineWater", String.format("%.2f", offlineWater));

			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			double onlineWater = statisticalSystemDao.queryToreEvenue(query);
			query.put("onlineWater", String.format("%.2f", onlineWater));

			// 店铺总货款
			double countWater = offlineWater + onlineWater;
			query.put("countWater", String.format("%.2f", countWater));

			/* 佣金收益 */
			// 线上店铺产生的佣金
			double xsCommission = statisticalSystemDao.queryYesterdayXsCommission(query);
			// 线下店铺扫码支付产生的佣金
			double xxSmCommission = statisticalSystemDao.queryYesterdayXxSmCommission(query);
			// 线下店铺报单产生的佣金
			double xxBdCommission = statisticalSystemDao.queryYesterdayXxBdCommission(query);
			query.put("xsCommissionIncome", String.format("%.2f", xsCommission));
			query.put("xxCommissionIncome", String.format("%.2f", xxSmCommission + xxBdCommission));
			query.put("commissionIncome", String.format("%.2f", xsCommission + xxSmCommission + xxBdCommission));
			// 总流水=总货款+总佣金
			query.put("countWaterAndCommissionIncome",
					String.format("%.2f", countWater + xsCommission + xxSmCommission + xxBdCommission));
			// 新用户数
			int newUsers = statisticalSystemDao.queryUserCount(query);
			query.put("newUsers", newUsers);
			// 新实名注册户数
			int newPassUsers = statisticalSystemDao.queryUserPassCount(query);
			query.put("newPassUsers", newPassUsers);
			// 月充值总额
			double monthlyRecharge = statisticalSystemDao.queryYesterdayRechargeAmount(query);
			query.put("monthlyRecharge", String.format("%.2f", monthlyRecharge));
			// 积分赠送
			double integralPresent = statisticalSystemDao.queryYesterdayIntegralPresent(query);
			query.put("integralPresent", String.format("%.2f", integralPresent));
			// 签到积分消耗
			double signIntegralConsumption = statisticalSystemDao.queryYesterdayIncentivePayments(query);
			query.put("signIntegralConsumption", String.format("%.2f", signIntegralConsumption));
			// 其它积分消耗
			query.put("otherIntegralConsumption", 0.00);
			// 签到奖励金
			query.put("signBonus", String.format("%.2f", signIntegralConsumption));
			// 积分变化
			double integralChange = statisticalSystemDao.queryYesterdayIntegralChange(query);
			query.put("integralChange", String.format("%.2f", integralChange));
			if (null == areaId || areaId.equals("") || areaId.equals("0")) {
				// 平台总积分
				query.put("dtype", "1");
				double totalPlatformIntegral = statisticalSystemDao.queryDataTable(query);
				if (totalPlatformIntegral == 0.00) {
					query.put("totalPlatformIntegral", "未统计");
				} else {
					query.put("totalPlatformIntegral", String.format("%.2f", totalPlatformIntegral));
				}
				// 平台总余额
				query.put("dtype", "2");
				double totalBalance = statisticalSystemDao.queryDataTable(query);
				if (totalBalance == 0.00) {
					query.put("totalBalance", "未统计");
				} else {
					query.put("totalBalance", String.format("%.2f", totalBalance));
				}
				// 平台总零花钱
				query.put("dtype", "3");
				double totalMoney = statisticalSystemDao.queryDataTable(query);
				if (totalMoney == 0.00) {
					query.put("totalMoney", "未统计");
				} else {
					query.put("totalMoney", String.format("%.2f", totalMoney));
				}
				// 平台总消费券
				query.put("dtype", "4");
				double totalConsumptionCoupon = statisticalSystemDao.queryDataTable(query);
				if (totalConsumptionCoupon == 0.00) {
					query.put("totalConsumptionCoupon", "未统计");
				} else {
					query.put("totalConsumptionCoupon", String.format("%.2f", totalConsumptionCoupon));
				}
				// 积分/佣金
				double commissionIncome = xsCommission + xxSmCommission + xxBdCommission;
				double integralOverCommission = integralPresent / commissionIncome;
				query.put("integralOverCommission", String.format("%.2f", integralOverCommission));
			}
			// 物流服务费
			double logisticsServiceCharge = queryLogisticsServiceCharge(query);
			query.put("logisticsServiceCharge", String.format("%.2f", logisticsServiceCharge));
			list.add(query);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", list.size());
		map.put("data", list);
		map.put("code", 0);
		return map;
	}

	/**
	 * 查询月流水明细导出list
	 * 
	 * @param days
	 * @param areaId
	 * @return
	 */
	public List<Map<String, Object>> getDetailswaterDaochu(String days, String areaId) {
		List<Map<String, Object>> querylist = new ArrayList<Map<String, Object>>();
		String[] day = days.split(";");
		for (String d : day) {
			Map<String, Object> mamp = new HashMap<String, Object>();
			mamp.put("date", d);
			d = d.replaceAll("\\.", "-");
			mamp.put("startTime", d + " 00:00:00");
			Date date = null;
			try {
				date = new Date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(d + " 00:00:00").getTime()
						+ 24 * 60 * 60 * 1000);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			mamp.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
			querylist.add(mamp);
		}

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> query : querylist) {
			if (null != areaId && (!areaId.equals("")) && (!areaId.equals("0"))) {
				query.put("areaId", areaId);
			}
			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			double offlineWater = statisticalSystemDao.queryToreEvenue(query);
			query.put("offlineWater", String.format("%.2f", offlineWater));

			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			double onlineWater = statisticalSystemDao.queryToreEvenue(query);
			query.put("onlineWater", String.format("%.2f", onlineWater));

			// 店铺总流水
			double countWater = offlineWater + onlineWater;
			query.put("countWater", String.format("%.2f", countWater));

			/* 佣金收益 */
			// 线上店铺产生的佣金
			double xsCommission = statisticalSystemDao.queryYesterdayXsCommission(query);
			// 线下店铺扫码支付产生的佣金
			double xxSmCommission = statisticalSystemDao.queryYesterdayXxSmCommission(query);
			// 线下店铺报单产生的佣金
			double xxBdCommission = statisticalSystemDao.queryYesterdayXxBdCommission(query);
			query.put("xsCommissionIncome", String.format("%.2f", xsCommission));
			query.put("xxCommissionIncome", String.format("%.2f", xxSmCommission + xxBdCommission));
			query.put("commissionIncome", String.format("%.2f", xsCommission + xxSmCommission + xxBdCommission));
			// 总流水=总货款+总佣金
			query.put("countWaterAndCommissionIncome",
					String.format("%.2f", countWater + xsCommission + xxSmCommission + xxBdCommission));
			// 新用户数
			int newUsers = statisticalSystemDao.queryUserCount(query);
			query.put("newUsers", newUsers);
			// 新实名注册户数
			int newPassUsers = statisticalSystemDao.queryUserPassCount(query);
			query.put("newPassUsers", newPassUsers);
			// 月充值总额
			double monthlyRecharge = statisticalSystemDao.queryYesterdayRechargeAmount(query);
			query.put("monthlyRecharge", String.format("%.2f", monthlyRecharge));
			// 积分赠送
			double integralPresent = statisticalSystemDao.queryYesterdayIntegralPresent(query);
			query.put("integralPresent", String.format("%.2f", integralPresent));
			// 签到积分消耗
			double signIntegralConsumption = statisticalSystemDao.queryYesterdayIncentivePayments(query);
			query.put("signIntegralConsumption", String.format("%.2f", signIntegralConsumption));
			// 其它积分消耗
			query.put("otherIntegralConsumption", 0.00);
			// 签到奖励金
			query.put("signBonus", String.format("%.2f", signIntegralConsumption));
			// 积分变化
			double integralChange = statisticalSystemDao.queryYesterdayIntegralChange(query);
			query.put("integralChange", String.format("%.2f", integralChange));
			if (null == areaId || areaId.equals("") || areaId.equals("0")) {
				// 平台总积分
				query.put("dtype", "1");
				double totalPlatformIntegral = statisticalSystemDao.queryDataTable(query);
				if (totalPlatformIntegral == 0.00) {
					query.put("totalPlatformIntegral", "未统计");
				} else {
					query.put("totalPlatformIntegral", String.format("%.2f", totalPlatformIntegral));
				}
				// 平台总余额
				query.put("dtype", "2");
				double totalBalance = statisticalSystemDao.queryDataTable(query);
				if (totalBalance == 0.00) {
					query.put("totalBalance", "未统计");
				} else {
					query.put("totalBalance", String.format("%.2f", totalBalance));
				}
				// 平台总零花钱
				query.put("dtype", "3");
				double totalMoney = statisticalSystemDao.queryDataTable(query);
				if (totalMoney == 0.00) {
					query.put("totalMoney", "未统计");
				} else {
					query.put("totalMoney", String.format("%.2f", totalMoney));
				}
				// 平台总消费券
				query.put("dtype", "4");
				double totalConsumptionCoupon = statisticalSystemDao.queryDataTable(query);
				if (totalConsumptionCoupon == 0.00) {
					query.put("totalConsumptionCoupon", "未统计");
				} else {
					query.put("totalConsumptionCoupon", String.format("%.2f", totalConsumptionCoupon));
				}
				// 积分赠送/佣金
				double commissionIncome = xsCommission + xxSmCommission + xxBdCommission;
				double integralOverCommission = integralPresent / commissionIncome;
				query.put("integralOverCommission", String.format("%.2f", integralOverCommission));
			}
			// 物流服务费
			double logisticsServiceCharge = queryLogisticsServiceCharge(query);
			query.put("logisticsServiceCharge", String.format("%.2f", logisticsServiceCharge));
			list.add(query);
		}
		return list;
	}

	/**
	 * 每天0点，记录平台用户的（1：总积分，2：总余额；3：总零花钱；4：总消费券）
	 */
	@Scheduled(cron = "0 0 0 * * ?")
	public void platformUserTotalPoints() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		// 查询账户总积分，余额，消费券，零花钱
		Map<String, Object> total = statisticalSystemDao.queryMoneyData();
		// 查询当前时刻的平台用户总积分
		double totalPoints = statisticalSystemDao.queryTotalScore(map);
		// 记录当前时刻平台总积分
		map.put("uuid", UUIDUtils.randomID());
		map.put("dvalue", String.format("%.2f", totalPoints));
		map.put("dtype", 1);
		statisticalSystemDao.insertDatatable(map);
		// 记录当前时刻平台用户总余额
		map.put("uuid", UUIDUtils.randomID());
		map.put("dvalue", total.get("moneys"));
		map.put("dtype", 2);
		statisticalSystemDao.insertDatatable(map);
		// 查询当前时刻平台用户总零花钱
		map.put("uuid", UUIDUtils.randomID());
		map.put("dvalue", total.get("pocketmoney"));
		map.put("dtype", 3);
		statisticalSystemDao.insertDatatable(map);
		// 查询当前时刻总消费券
		map.put("uuid", UUIDUtils.randomID());
		map.put("dvalue", total.get("sonsumptionVolume"));
		map.put("dtype", 4);
		statisticalSystemDao.insertDatatable(map);
	}

	/**
	 * 查询物流服务费
	 */
	public double queryLogisticsServiceCharge(Map<String, Object> map) {
		int orderNumber = statisticalSystemDao.queryOrderMumber(map);
		return 0.3 * orderNumber;
	}
}
