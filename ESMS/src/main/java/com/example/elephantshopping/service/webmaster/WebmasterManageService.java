package com.example.elephantshopping.service.webmaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.webmaster.WebmasterDao;
import com.example.elephantshopping.dao.webmaster.WebmasterManageDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class WebmasterManageService {
	@Autowired
	private WebmasterManageDao webmasterManageDao;
	@Autowired
	private WebmasterDao webmasterDao;

	/**
	 * 查詢地区
	 * 
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> queryAreaList(String pid) {
		return webmasterManageDao.queryAreaList(pid);
	}

	/**
	 * 站长数据
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getWebmasterData(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<String> query = new ArrayList<String>();
		if (null != map.get("area") && (!map.get("area").equals(""))) {
			query.add(map.get("area").toString());
		} else if ((null == map.get("area") || map.get("area").equals(""))
				&& (null != map.get("city") && (!map.get("city").equals("")))) {
			List<Map<String, Object>> q = queryAreaList(map.get("city").toString());
			for (Map<String, Object> m : q) {
				query.add(m.get("CITY_CODE").toString());
			}
		} else if ((null == map.get("area") || map.get("area").equals(""))
				&& (null == map.get("city") || map.get("city").equals(""))
				&& (null != map.get("province") && (!map.get("province").equals("")))) {
			List<Map<String, Object>> q = queryAreaList(map.get("province").toString());
			for (Map<String, Object> m : q) {
				List<Map<String, Object>> q1 = queryAreaList(m.get("CITY_CODE").toString());
				for (Map<String, Object> m1 : q1) {
					query.add(m1.get("CITY_CODE").toString());
				}
			}
		}
		map.put("areas", query);
		List<Map<String, Object>> list = webmasterManageDao.getWebmasterList(map);
		for (Map<String, Object> pca : list) {
			pca.put("pcarea", getMasterArea(pca.get("AREAID").toString()));
		}
		int count = webmasterManageDao.getWebmasterListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("code", 0);
		map.put("data", DateFormatUtils.timeforDateType(list, "ZZTIME"));
		map.put("count", count);
		return map;
	}

	/**
	 * 查询站长区域
	 */
	public String getMasterArea(String areaId) {
		Map<String, Object> map = webmasterDao.queryAreaName(areaId);
		String pid = map.get("PID").toString();
		String area = map.get("CITY_NAME").toString();
		do {
			Map<String, Object> map1 = webmasterDao.queryAreaName(pid);
			pid = map1.get("PID").toString();
			area = map1.get("CITY_NAME").toString() + "、" + area;
		} while (!pid.equals("1"));
		return area;
	}

	/**
	 * 撤销站长
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateISZZ(Map<String, Object> requestToMap) {
		// 删除该用户站长角色
		webmasterManageDao.deleteZZRole(requestToMap);
		return webmasterManageDao.updateISZZ(requestToMap);
	}

	/**
	 * 站长结算
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int zhanzhangjiesuan(Map<String, Object> requestToMap) {
		// 修改站长余额
		int i = webmasterManageDao.updateWebmasterYue(requestToMap);
		// 添加账户流水
		requestToMap.put("uuid", UUIDUtils.randomID());
		webmasterManageDao.insetWebmasterWater(requestToMap);
		return i;
	}

	/**
	 * 查询结算记录
	 * 
	 * @param requestToMap
	 * @return
	 */
	public Map<String, Object> masterSettlementRecordList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<String> query = new ArrayList<String>();
		if (null != map.get("area") && (!map.get("area").equals(""))) {
			query.add(map.get("area").toString());
		} else if ((null == map.get("area") || map.get("area").equals(""))
				&& (null != map.get("city") && (!map.get("city").equals("")))) {
			List<Map<String, Object>> q = queryAreaList(map.get("city").toString());
			for (Map<String, Object> m : q) {
				query.add(m.get("CITY_CODE").toString());
			}
		} else if ((null == map.get("area") || map.get("area").equals(""))
				&& (null == map.get("city") || map.get("city").equals(""))
				&& (null != map.get("province") && (!map.get("province").equals("")))) {
			List<Map<String, Object>> q = queryAreaList(map.get("province").toString());
			for (Map<String, Object> m : q) {
				List<Map<String, Object>> q1 = queryAreaList(m.get("CITY_CODE").toString());
				for (Map<String, Object> m1 : q1) {
					query.add(m1.get("CITY_CODE").toString());
				}
			}
		}
		map.put("areas", query);
		if (null == map.get("startTime") || map.get("startTime").equals("")) {
			map.put("startTime", DateFormatUtils.onlineTime);
		}
		if (null == map.get("endTime") || map.get("endTime").equals("")) {
			map.put("endTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		List<Map<String, Object>> list = webmasterManageDao.masterSettlementRecordList(map);
		for (Map<String, Object> pca : list) {
			pca.put("pcarea", getMasterArea(pca.get("AREAID").toString()));
		}
		int count = webmasterManageDao.getmasterSettlementRecordListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("code", 0);
		map.put("data", DateFormatUtils.timeforDateType(list, "ETIME"));
		map.put("count", count);
		return map;
	}

	/**
	 * 验证区域是否存在站长
	 * 
	 * @param areaId
	 * @return
	 */
	public int VerifyThatThereIs(String areaId) {
		return webmasterManageDao.VerifyThatThereIs(areaId);
	}

	/**
	 * 查询手机号信息
	 * 
	 * @param userPhone
	 * @return
	 */
	public int verifyUserIsHave(String userPhone) {
		// 查询手机号是否注册
		int i = webmasterManageDao.isHavePhone(userPhone);
		if (i > 0) {
			// 是否实名认证通过
			int ii = webmasterManageDao.readyshimingrenzheng(userPhone);
			if (ii > 0) {
				// 是否为已经为站长
				int iii = webmasterManageDao.isZhanzhanga(userPhone);
				if (iii > 0) {
					return 1;
				}
				return 3;
			}
			return 2;
		}
		return 0;
	}

	/**
	 * 添加站长
	 * 
	 * @param userphone
	 * @param area
	 * @return
	 */
	@Transactional
	public int addWebmaster(String userphone, String area, String zzyqr) {
		// 查询角色id
		String userId = webmasterManageDao.getUserIdByPhone(userphone);
		// 添加站长角色
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("uuid", UUIDUtils.randomID());
		webmasterManageDao.addWebmasterRole(map);
		map.put("userphone", userphone);
		map.put("area", area);
		if (null != zzyqr && (!zzyqr.equals("")) && (!zzyqr.equals(userphone))) {
			map.put("zzyqr", zzyqr);
		}
		return webmasterManageDao.addWebmaster(map);
	}

}
