package com.example.elephantshopping.service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.ActivityClickDao;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class ActivityClickService {
	@Autowired
	private ActivityClickDao activityDao;

	/**
	 * 查询人气王大赛活动信息
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public Map<String, Object> queryRenqidasaiInfo(String startTime, String endTime) {
		// 查询参数者列表
		List<Map<String, Object>> list = activityDao.queryRenqidasaiList(endTime);
		for (Map<String, Object> user : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			String phone = user.get("phone").toString();
			map.put("phone", phone);
			// 查询邀请的普通用户数量
			int pNumber = activityDao.queryInvitedUsersNumber(map);
			// 查询邀请网店数量
			map.put("types", "STORE_XS_XS");
			int xsNumber = activityDao.queryInvitedStoreNumber(map);
			// 查询邀请实体店数量
			map.put("types", "STORE_XS_XX");
			int xxNumber = activityDao.queryInvitedStoreNumber(map);
			int shuliang = pNumber * 10 + xsNumber * 100 + xxNumber * 20;
			user.put("shuliang", shuliang);
		}

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("count", list.size());
		map.put("list", sort(list, "shuliang", 10));
		return map;
	}

	/**
	 * 集合排序
	 */
	public List<Map<String, Object>> sort(List<Map<String, Object>> list, String field, int rank) {
		List<Map<String, Object>> sortlist = new ArrayList<Map<String, Object>>();
		int c = 0;// 排名
		int a = 0; // 当前对象的像章数
		int b = 0;// 前一个对象的像章数
		for (int i = 0; i < rank; i++) {
			Map<String, Object> temple = list.get(0);
			for (int j = 0; j < list.size(); j++) {
				int n = Integer.parseInt(temple.get(field).toString());
				int m = Integer.parseInt(list.get(j).get(field).toString());
				if (n < m) {
					temple = list.get(j);
					a = m;
				} else {
					a = n;
				}
			}
			list.remove(temple);
			String phone = temple.get("phone").toString();
			temple.put("username",
					phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7));
			if (a == b) {
				temple.put("pm", c);
			} else {
				temple.put("pm", ++c);
			}
			sortlist.add(i, temple);
			b = Integer.parseInt(temple.get(field).toString());
		}
		return sortlist;
	}

	/**
	 * 查询个人排名
	 * 
	 * @param phone
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public Map<String, Object> queryPersonalRanking(String phone, String startTime, String endTime) {
		// 查询参数者列表
		List<Map<String, Object>> list = activityDao.queryRenqidasaiList(endTime);
		for (Map<String, Object> user : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			String phones = user.get("phone").toString();
			map.put("phone", phones);
			// 查询邀请的普通用户数量
			int pNumber = activityDao.queryInvitedUsersNumber(map);
			// 查询邀请网店数量
			map.put("types", "STORE_XS_XS");
			int xsNumber = activityDao.queryInvitedStoreNumber(map);
			// 查询邀请实体店数量
			map.put("types", "STORE_XS_XX");
			int xxNumber = activityDao.queryInvitedStoreNumber(map);
			int shuliang = pNumber * 10 + xsNumber * 100 + xxNumber * 20;
			user.put("shuliang", shuliang);
		}
		Map<String, Object> user = index(list, phone);
		if (null == user) {
			user = new HashMap<String, Object>();
			user.put("code", 0);
		} else {
			user.put("code", 1);
		}
		return user;
	}

	/**
	 * 通过手机号查询对象在集合中的排名并返回对象,顺带计算超过百分之多少的人
	 * 
	 * @param sortlist
	 * @param phone
	 * @return
	 */
	private Map<String, Object> index(List<Map<String, Object>> list, String phone) {
		List<Map<String, Object>> sortlist = sort(list, "shuliang", list.size());
		Map<String, Object> user = null;
		for (int i = 0; i < sortlist.size(); i++) {
			if (sortlist.get(i).get("phone").equals(phone)) {
				user = sortlist.get(i);
			}
		}
		if (null != user) {
			int pm = Integer.parseInt(user.get("pm").toString());
			int i = 0;
			for (Map<String, Object> map : sortlist) {
				int n = Integer.parseInt(map.get("pm").toString());
				if (n > pm) {
					i++;
				}
			}
			int count = sortlist.size();
			NumberFormat numberFormat = NumberFormat.getInstance();
			numberFormat.setMaximumFractionDigits(2);
			String bli = numberFormat.format((float) i / (float) count * 100);
			String bili = bli + "%";
			user.put("bili", bili);
		}
		return user;
	}

	/**
	 * 查询用户是否参与过游戏
	 * 
	 * @param userId
	 * @return
	 */
	public String queryIsGamed(String userId) {
		return activityDao.queryIsGamed(userId);
	}

	/**
	 * 通过礼品名查询被抽取礼品数量
	 * 
	 * @param string
	 * @return
	 */
	public int queryPrizeNumber(String prizeName) {
		return activityDao.queryPrizeNumber(prizeName);
	}

	/**
	 * 修改抽奖用户
	 * 
	 * @param map
	 */
	public void updateLottery(Map<String, Object> map) {
		activityDao.updateLottery(map);
	}

	/**
	 * 查询是否有抽奖资格
	 * 
	 * @param userId
	 * @return
	 */
	public int queryRaffle(String userId) {
		return activityDao.queryRaffle(userId);
	}

	/**
	 * 查询未抽奖人数
	 * 
	 * @return
	 */
	public int queryUserNumber() {
		return activityDao.queryUserNumber();
	}

	/**
	 * 查询中奖list
	 * 
	 * @return
	 */
	public List<Map<String, Object>> queryWinList() {
		List<Map<String, Object>> list = activityDao.queryWinList();
		for (Map<String, Object> map : list) {
			String phone = map.get("PHONE").toString();
			map.put("phone",
					phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7));
		}
		return list;
	}

	/**
	 * 查询可参与抽奖活动的用户（活动过期）
	 */
	// // @Scheduled(cron = "0 0 12 24 5 ?")
	// public void queryLuckyDraw() {
	// final String startTime = "2018-04-25 09:00:00";// 开始时间
	// final String endTime = "2018-05-24 12:00:00";// 结束时间
	// // 查询参数者列表
	// List<Map<String, Object>> list = activityDao.queryRenqidasaiList(endTime);
	// for (Map<String, Object> user : list) {
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put("startTime", startTime);
	// map.put("endTime", endTime);
	// String phone = user.get("phone").toString();
	// map.put("phone", phone);
	// // 查询邀请的普通用户数量
	// int pNumber = activityDao.queryInvitedUsersNumber(map);
	// // 查询邀请网店数量
	// map.put("types", "STORE_XS_XS");
	// int xsNumber = activityDao.queryInvitedStoreNumber(map);
	// // 查询邀请实体店数量
	// map.put("types", "STORE_XS_XX");
	// int xxNumber = activityDao.queryInvitedStoreNumber(map);
	// int shuliang = pNumber * 10 + xsNumber * 100 + xxNumber * 20;
	// user.put("shuliang", shuliang);
	// }
	// // 排名list
	// List<Map<String, Object>> sortlist = sort(list, "shuliang", list.size());
	// for (Map<String, Object> user : sortlist) {
	// int pm = Integer.parseInt(user.get("pm").toString());
	// int sl = Integer.parseInt(user.get("shuliang").toString());
	// if (pm > 3 && sl >= 20) {
	// user.put("uuid", UUIDUtils.randomID());
	// // 插入抽奖list
	// activityDao.insertLottery(user);
	// }
	// }
	// }

}
