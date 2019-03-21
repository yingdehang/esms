package com.example.elephantshopping.service.activityManage;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elephantshopping.dao.activityManage.ActivityDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class ActivityService {
	@Autowired
	private ActivityDao activityDao;

	/**
	 * 查询活动管理列表
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getActiveList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", (page - 1) * limit);
		List<Map<String, Object>> list = activityDao.getActiveList(map);
		int count = activityDao.getActiveListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(DateFormatUtils.timeforDateType(list, "STARTTIME"), "ENDTIME"));
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 修改活动状态
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateActivityState(Map requestToMap) {
		return activityDao.updateActivityState(requestToMap);
	}

	/**
	 * 修改排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateActivitySort(Map requestToMap) {
		return activityDao.updateActivitySort(requestToMap);
	}

	/**
	 * 添加活动
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int addactivity(Map<String, Object> map) {
		String activeTime = map.get("activeTime").toString();
		String[] t = activeTime.split("-");
		String startTime = t[0] + "-" + t[1] + "-" + t[2];
		String endTime = t[3] + "-" + t[4] + "-" + t[5];
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		if (null != map.get("ESID") && (!map.get("ESID").equals(""))) {
			activityDao.deleteActivity(map.get("ESID").toString());
			map.put("uuid", map.get("ESID").toString());
		} else {
			map.put("uuid", UUIDUtils.randomID());
		}
		activityDao.addactivity(map);
		return 1;
	}

	/**
	 * 查詢活動信息
	 * 
	 * @param eSID
	 * @return
	 */
	public Map<String, Object> queryActivityInfo(String eSID) {
		Map<String, Object> map = activityDao.queryActivityInfo(eSID);
		String startTime = map.get("STARTTIME").toString();
		String endTime = map.get("ENDTIME").toString();
		map.put("activeTime", startTime + " - " + endTime);
		return map;
	}

	/**
	 * 删除活动
	 * 
	 * @param eSID
	 * @return
	 */
	public int deleteActivity(String eSID) {
		activityDao.deleteActivity(eSID);
		return 1;
	}

	public Map<String, Object> getNewActiveList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", (page - 1) * limit);
		List<Map<String, Object>> list = activityDao.getNewActiveList(map);
		int count = activityDao.getNewActiveListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 添加首页活动
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int addNewActivity(Map<String, Object> map) {
		try {
			map.put("uuid", UUIDUtils.randomID());
			activityDao.addNewActivity(map);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 
	 * @param nID
	 * @return
	 */
	public int deleteNewActivity(String NID) {
		try {
			activityDao.deleteNewActivity(NID);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	/**
	 * 开启和关闭活动
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateNewActivityState(Map<String, Object> requestToMap) {
		// TODO Auto-generated method stub
		return activityDao.updateNewActivityState(requestToMap);
	}

	/**
	 * 修改排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateNewActivitySort(Map<String, Object> requestToMap) {
		// TODO Auto-generated method stub
		return activityDao.updateNewActivitySort(requestToMap);
	}

	/**
	 * 查询首页活动信息
	 * 
	 * @param nID
	 * @return
	 */
	public Map<String, Object> queryNewActivityInfo(String nID) {
		Map<String, Object> map = activityDao.queryNewActivityInfo(nID);
		String ttype = map.get("TTYPE").toString();
		if (ttype.equals("1")) {
			String goodsId = map.get("PICTURE_ADDR").toString();
			String storeId = activityDao.queryStoreId(goodsId);
			map.put("storeId", storeId);
			// 查询商品list
			map.put("goodslist", activityDao.querygoodslistByStoreId(storeId));
		} else {
			map.put("storeId", 0);
			map.put("goodslist", null);
		}
		return map;
	}

	/**
	 * 查询首页活动类型1商品list
	 * 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> querygoodslist(String NID) {
		return activityDao.querygoodslist(NID);
	}

	/**
	 * 查询店铺list
	 * 
	 * @return
	 */
	public List<Map<String, Object>> querystorelist() {
		return activityDao.querystorelist();
	}

	/**
	 * 查询商品list
	 * 
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> querygoodslistByStoreId(String storeId) {
		// TODO Auto-generated method stub
		return activityDao.querygoodslistByStoreId(storeId);
	}
}
