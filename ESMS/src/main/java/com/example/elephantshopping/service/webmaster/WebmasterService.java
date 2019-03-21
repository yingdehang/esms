package com.example.elephantshopping.service.webmaster;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.webmaster.WebmasterDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class WebmasterService {
	@Autowired
	private WebmasterDao webmasterDao;

	/**
	 * 获取站长信息
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getWebmasterInfo(Map<String, Object> map, String userId) {
		Map<String, Object> webmasterInfo = new HashMap<String, Object>();
		// 查询站长信息
		Map<String, Object> webmaster = webmasterDao.getWebmasterInfo(userId);
		webmasterInfo.put("webmaster", webmaster);
		String areaId = webmaster.get("AREAID").toString();
		// 查询总收益
		Double totalRevenue = webmasterDao.queryTotalRevenue(map);
		if (null == totalRevenue) {
			totalRevenue = 0.00;
		}
		webmasterInfo.put("totalRevenue", totalRevenue);
		// 查询代理地区
		String agencyArea = queryAgencyArea(areaId);
		webmasterInfo.put("agencyArea", agencyArea);
		// 通过areaId查询该地区用户总数
		int userCount = webmasterDao.getuserCount(areaId);
		webmasterInfo.put("userCount", userCount);
		// 查询线上店铺数
		int xsStoreCount = webmasterDao.getXsStoreCount(areaId);
		webmasterInfo.put("xsStoreCount", xsStoreCount);
		// 查询线下店铺
		int xxStoreCount = webmasterDao.getXxStoreCount(areaId);
		webmasterInfo.put("xxStoreCount", xxStoreCount);
		// 最近七天新增用户数
		Map<String, Object> querymap = DateFormatUtils.betweenDateTimeMap(7 * 24 * 60 * 60 * 1000);
		querymap.put("areaId", areaId);
		int newUserCount = webmasterDao.getnewUserCount(querymap);
		webmasterInfo.put("newUserCount", newUserCount);
		// 七天新增线上店铺
		int newXsStoreCount = webmasterDao.getnewXsStoreCount(querymap);
		webmasterInfo.put("newXsStoreCount", newXsStoreCount);
		// 七天新增线下店铺
		int newXxStoreCount = webmasterDao.getnewXxStoreCount(querymap);
		webmasterInfo.put("newXxStoreCount", newXxStoreCount);
		// 查询上月
		Map<String, Object> queryMonth = DateFormatUtils.betweenMonth(1);
		queryMonth.put("userId", userId);
		Double lastMonthEarnings = webmasterDao.queryLastmonthEarnings(queryMonth);
		if (null == lastMonthEarnings) {
			lastMonthEarnings = 0.00;
		}
		webmasterInfo.put("lastMonthEarnings", lastMonthEarnings);
		// 查询本月
		Map<String, Object> querythisMonth = DateFormatUtils.betweenMonth(0);
		querythisMonth.put("userId", userId);
		Double thisMonthEarnings = webmasterDao.queryLastmonthEarnings(querythisMonth);
		if (null == thisMonthEarnings) {
			thisMonthEarnings = 0.00;
		}
		webmasterInfo.put("thisMonthEarnings", thisMonthEarnings);
		return webmasterInfo;
	}

	/**
	 * 分级查询代理区域
	 */
	public String queryAgencyArea(String areaId) {
		Map<String, Object> map = webmasterDao.queryAreaName(areaId);
		String pid = map.get("PID").toString();
		String area = map.get("CITY_NAME").toString();
		do {
			Map<String, Object> map1 = webmasterDao.queryAreaName(pid);
			pid = map1.get("PID").toString();
			area = map1.get("CITY_NAME").toString() + " > " + area;
		} while (!pid.equals("1"));
		return area;
	}

	/**
	 * 获取数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getEarningsTrendData(Map<String, Object> map, String userId) {
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		Map<String, Object> datamap = new HashMap<String, Object>();
		String dataType = map.get("dataType").toString();
		// 线上店铺收益数组
		Double[] Onlinetoreevenue = null;
		// 实体店铺收益数组
		Double[] entityShopevenue = null;
		// 总收益
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
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XS");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				Onlinetoreevenue[i] = evenue;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(7 - i, "day");
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XX");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				entityShopevenue[i] = evenue;
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
			// 线上店铺收益数组
			for (int i = 0; i < Onlinetoreevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(4 - i, "week");
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XS");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				Onlinetoreevenue[i] = evenue;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			// 实体店铺收益数组
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenDaoTimeMap(7 - i, "week");
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XX");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				entityShopevenue[i] = evenue;
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
			// 线上店铺收益数组
			for (int i = 0; i < Onlinetoreevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenMonth(12 - i);
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XS");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				Onlinetoreevenue[i] = evenue;
			}
			datamap.put("Onlinetoreevenue", Onlinetoreevenue);
			// 实体店铺收益数组
			for (int i = 0; i < entityShopevenue.length; i++) {
				Map<String, Object> duringday = DateFormatUtils.betweenMonth(12 - i);
				duringday.put("areaId", areaId);
				duringday.put("types", "STORE_XS_XX");
				Double evenue = webmasterDao.queryToreEvenue(duringday);
				if (null == evenue) {
					evenue = 0.00;
				}
				entityShopevenue[i] = evenue;
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
	 * 店铺等级分布
	 * 
	 * @param userId
	 * @return
	 */
	public Integer[] getShopGradeDistributionData(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		map.put("areaId", areaId);
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
				data[n++] = webmasterDao.getShopGradeDistributionData(map);
			}
		}
		return data;
	}

	/**
	 * 会员充值占比
	 * 
	 * @param userId
	 * @return
	 */
	public Double[] getmembershipRechargeAmountData(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		map.put("areaId", areaId);
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
		// 会员字典数组,初级、中级、高级
		String[] MEMBERSHIP_GRADE = { "USER_DJ_PRIMARY", "USER_DJ_INTERMEDIATE", "USER_DJ_SENIOR" };
		Double[] data = new Double[] { 0.00, 0.00, 0.00 };
		for (int i = 0; i < data.length; i++) {
			map.put("MEMBERSHIP_GRADE", MEMBERSHIP_GRADE[i]);
			Double chongzi = webmasterDao.getmembershipRechargeAmountData(map);
			if (null != chongzi) {
				data[i] = chongzi;
			}
		}
		return data;
	}

	/**
	 * 会员等级分布
	 * 
	 * @param userId
	 * @return
	 */
	public Integer[] membershipistributionData(String userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		map.put("areaId", areaId);
		map.put("endTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + " 00:00:00");
		// 会员字典数组
		String[] MEMBERSHIP_GRADE = { "USER_DJ_PRIMARY", "USER_DJ_INTERMEDIATE", "USER_DJ_SENIOR" };
		Integer[] data = new Integer[3];
		for (int i = 0; i < data.length; i++) {
			map.put("MEMBERSHIP_GRADE", MEMBERSHIP_GRADE[i]);
			data[i] = webmasterDao.membershipistributionData(map);
		}
		return data;
	}

	/**
	 * 查询用户用户总数
	 * 
	 * @param map
	 * @return
	 */
	public int getUsersCount(Map<String, Object> map, String userId) {
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		map.put("areaId", areaId);
		String startTime = DateFormatUtils.onlineTime;
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (null != map.get("startTime") && (!map.get("startTime").equals(""))) {
			startTime = map.get("startTime").toString();
		} else {
			map.put("startTime", startTime);
		}
		if (null != map.get("endTime") && (!map.get("endTime").equals(""))) {
			endTime = map.get("endTime").toString();
		} else {
			map.put("endTime", endTime);
		}
		return webmasterDao.getnewUserCount(map);
	}

	/**
	 * 查询店铺总数
	 * 
	 * @param map
	 * @param userId
	 * @return
	 */
	public int getStoreCount(Map<String, Object> map, String userId) {
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		map.put("areaId", areaId);
		String startTime = DateFormatUtils.onlineTime;
		String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if (null != map.get("startTime") && (!map.get("startTime").equals(""))) {
			startTime = map.get("startTime").toString();
		} else {
			map.put("startTime", startTime);
		}
		if (null != map.get("endTime") && (!map.get("endTime").equals(""))) {
			endTime = map.get("endTime").toString();
		} else {
			map.put("endTime", endTime);
		}
		return webmasterDao.getStoreCount(map);
	}

	/**
	 * 查询站长账户收支明细
	 * 
	 * @param map
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getPaymentlist(Map<String, Object> map, String userId) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		map.put("userId", userId);
		String startTime = DateFormatUtils.onlineTime;
		String endTime = "now()";
		if (null != map.get("startTime") && (!map.get("startTime").equals(""))) {
			startTime = map.get("startTime").toString();
		} else {
			map.put("startTime", startTime);
		}
		if (null != map.get("endTime") && (!map.get("endTime").equals(""))) {
			endTime = map.get("endTime").toString();
		} else {
			map.put("endTime", endTime);
		}
		List<Map<String, Object>> paymentList = webmasterDao.queryPaymentList(map);
		int count = webmasterDao.queryPaymentListCount(map);
		map.clear();
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(paymentList, "ETIME"));
		map.put("count", count);
		return map;
	}

	/**
	 * 查询站长账户数据
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getWebmasterInfo(String userId) {
		return webmasterDao.getWebmasterInfo(userId);
	}

	/**
	 * 查询收支明细记录
	 * 
	 * @param eZPIDs
	 * @return
	 */
	public List<Map<String, Object>> getPaymentList(String eZPIDs) {
		String[] ezpids = eZPIDs.split(";");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String s : ezpids) {
			list.add(webmasterDao.getPaymentList(s));
		}
		return list;
	}

	/**
	 * 获取站长每月流水
	 * 
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getRunningwaterlist(String userId) {
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
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
			query.put("userId", userId);
			query.put("areaId", areaId);
			m.put("yearMonth", months[i]);
			m.put("clearingTime", query.get("endTime"));
			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			Double offlineWater = webmasterDao.queryToreEvenue(query);
			if (null == offlineWater) {
				offlineWater = 0.00;
			}
			m.put("offlineWater", offlineWater);
			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			Double onlineWater = webmasterDao.queryToreEvenue(query);
			if (null == onlineWater) {
				onlineWater = 0.00;
			}
			m.put("onlineWater", onlineWater);
			Double countWater = offlineWater + onlineWater;
			if (countWater > 0) {
				m.put("countWater", new DecimalFormat("###,###,###.00").format(countWater));
			} else {
				m.put("countWater", countWater);
			}
			Double monthlyIncome = webmasterDao.queryTotalRevenue(query);
			if (null == monthlyIncome) {
				monthlyIncome = 0.00;
			}
			m.put("monthlyIncome", monthlyIncome);
			m.put("index", start - 1);
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
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getRunningwaterDaochu(String indexs, String userId) {
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		String[] index = indexs.split(";");
		// 查询从上线到现在有多少个月了
		String[] months = DateFormatUtils.GetqueryMonthlyPeriod();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < index.length; i++) {
			Map<String, Object> m = new HashMap<String, Object>();
			int n = Integer.parseInt(index[i]);
			Map<String, Object> query = DateFormatUtils.betweenMonth(n);
			query.put("userId", userId);
			query.put("areaId", areaId);
			m.put("yearMonth", months[n]);
			m.put("clearingTime", query.get("endTime"));
			// 查询线下店铺流水
			query.put("types", "STORE_XS_XX");
			Double offlineWater = webmasterDao.queryToreEvenue(query);
			if (null == offlineWater) {
				offlineWater = 0.00;
			}
			m.put("offlineWater", offlineWater);
			// 查询线上店铺流水
			query.put("types", "STORE_XS_XS");
			Double onlineWater = webmasterDao.queryToreEvenue(query);
			if (null == onlineWater) {
				onlineWater = 0.00;
			}
			m.put("onlineWater", onlineWater);
			Double countWater = offlineWater + onlineWater;
			if (countWater > 0) {
				m.put("countWater", new DecimalFormat("###,###,###.00").format(countWater));
			} else {
				m.put("countWater", countWater);
			}
			Double monthlyIncome = webmasterDao.queryTotalRevenue(query);
			if (null == monthlyIncome) {
				monthlyIncome = 0.00;
			}
			m.put("monthlyIncome", monthlyIncome);
			list.add(m);
		}
		return list;
	}

	/**
	 * 获得月流水
	 * 
	 * @param month
	 * @param userId
	 * @return
	 * @throws ParseException
	 */
	public Map<String, Object> getdayWaterlist(String month, String userId) {
		List<Map<String, Object>> query = DateFormatUtils.getMonthDayMap(month);
		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Double shouyiheji = 0.00;
		for (Map<String, Object> map : query) {
			map.put("areaId", areaId);
			map.put("userId", userId);
			// 查询线下店铺流水
			map.put("types", "STORE_XS_XX");
			Double offlineWater = webmasterDao.queryToreEvenue(map);
			if (null == offlineWater) {
				offlineWater = 0.00;
			}
			map.put("offlineWater", offlineWater);
			// 查询线上店铺流水
			map.put("types", "STORE_XS_XS");
			Double onlineWater = webmasterDao.queryToreEvenue(map);
			if (null == onlineWater) {
				onlineWater = 0.00;
			}
			map.put("onlineWater", onlineWater);
			Double countWater = offlineWater + onlineWater;
			if (countWater > 0) {
				map.put("countWater", String.format("%.2f", countWater));
			} else {
				map.put("countWater", countWater);
			}
			Double monthlyIncome = webmasterDao.queryTotalRevenue(map);
			if (null == monthlyIncome) {
				monthlyIncome = 0.00;
			}
			shouyiheji += monthlyIncome;
			map.put("monthlyIncome", monthlyIncome);
			list.add(map);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", String.format("%.2f", shouyiheji));
		map.put("count", list.size());
		map.put("data", list);
		map.put("code", 0);
		return map;
	}

	/**
	 * 查询导出月每日流水
	 * 
	 * @param days
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getDetailswaterDaochu(String days, String userId) {
		List<Map<String, Object>> query = new ArrayList<Map<String, Object>>();
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
			query.add(mamp);
		}

		// 查询地区id
		String areaId = webmasterDao.getAreaId(userId);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : query) {
			map.put("areaId", areaId);
			map.put("userId", userId);
			// 查询线下店铺流水
			map.put("types", "STORE_XS_XX");
			Double offlineWater = webmasterDao.queryToreEvenue(map);
			if (null == offlineWater) {
				offlineWater = 0.00;
			}
			map.put("offlineWater", offlineWater);
			// 查询线上店铺流水
			map.put("types", "STORE_XS_XS");
			Double onlineWater = webmasterDao.queryToreEvenue(map);
			if (null == onlineWater) {
				onlineWater = 0.00;
			}
			map.put("onlineWater", onlineWater);
			Double countWater = offlineWater + onlineWater;
			if (countWater > 0) {
				map.put("countWater", String.format("%.2f", countWater));
			} else {
				map.put("countWater", countWater);
			}
			Double monthlyIncome = webmasterDao.queryTotalRevenue(map);
			if (null == monthlyIncome) {
				monthlyIncome = 0.00;
			}
			map.put("monthlyIncome", monthlyIncome);
			list.add(map);
		}
		return list;
	}
}
