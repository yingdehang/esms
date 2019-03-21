package com.example.elephantshopping.service.operationsManage;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.operationsManage.BigOrderAuditDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class BigOrderAuditService {
	@Autowired
	private BigOrderAuditDao bigOrderAuditDao;

	/**
	 * 
	 * @param requestToMap
	 * @return
	 */
	public Map<String, Object> queryBigOrderAuditList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		int count = bigOrderAuditDao.queryBigOrderAuditListCount(map).size();
		List<Map<String, Object>> list = bigOrderAuditDao.queryBigOrderAuditList(map);
		if (list.size() > 0) {
			for (Map<String, Object> m : list) {
				if (null != m.get("PROVINCE")) {
					m.put("PROVINCE", bigOrderAuditDao.queryCityName(m.get("PROVINCE").toString()));
				} else {
					m.put("PROVINCE", null);
				}
				if (null != m.get("CITY")) {
					m.put("CITY", bigOrderAuditDao.queryCityName(m.get("CITY").toString()));
				} else {
					m.put("CITY", null);
				}
				if (null != m.get("AREA")) {
					m.put("AREA", bigOrderAuditDao.queryCityName(m.get("AREA").toString()));
				} else {
					m.put("AREA", null);
				}
			}
		}
		map.clear();
		map.put("data", DateFormatUtils.timeforDateType(list, "ADDTIME"));
		map.put("code", 0);
		map.put("count", count);
		map.put("msg", "");
		return map;
	}

	/**
	 * 查询大额订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> queryBigOrderInfo(String orderNumber) {
		List<Map<String, Object>> list = bigOrderAuditDao.queryBigOrderInfo(orderNumber);
		double price = 0.00;
		for (Map<String, Object> m : list) {
			double p = Double.parseDouble(m.get("price").toString());
			int n = Integer.parseInt(m.get("number").toString());
			price += p * n;
		}
		Map<String, Object> map = list.get(0);
		map.put("price", new DecimalFormat("#########.00").format(price));
		String PROVINCE = "";
		String CITY = "";
		String AREA = "";
		if (null != map.get("PROVINCE")) {
			PROVINCE = bigOrderAuditDao.queryCityName(map.get("PROVINCE").toString());
		}
		if (null != map.get("CITY")) {
			CITY = bigOrderAuditDao.queryCityName(map.get("CITY").toString());
		}
		if (null != map.get("AREA")) {
			AREA = bigOrderAuditDao.queryCityName(map.get("AREA").toString());
		}
		map.put("address", PROVINCE + CITY + AREA);
		Object ucName = map.get("ucName");
		Object storePhone = map.get("storePhone");
		Object storeInfo = ucName + "(" + storePhone + ")";
		map.put("storeInfo", storeInfo);
		Object zzUcname = "null";
		Object zzPhone = "null";
		if (null != map.get("zzUcname") && (!map.get("zzUcname").equals(""))) {
			zzUcname = map.get("zzUcname");
		}
		if (null != map.get("zzPhone") && (!map.get("zzPhone").equals(""))) {
			zzPhone = map.get("zzPhone");
		}
		String zzInfo = zzUcname + "(" + zzPhone + ")";
		map.put("zzInfo", zzInfo);
		String[] photos = null;
		if (null != map.get("STATE_PHOTO") && (!map.get("STATE_PHOTO").equals(""))) {
			photos = map.get("STATE_PHOTO").toString().split(",");
		}
		map.put("photos", photos);
		if (null == map.get("storeName")) {
			map.put("storeName", "null");
		}
		return map;
	}

	/**
	 * 查询订单状态
	 * 
	 * @param orderId
	 * @return
	 */
	public String queryBigState(String orderId) {
		return bigOrderAuditDao.queryBigState(orderId);
	}

	/**
	 * 修改大额订单状态
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateBigOrderState(Map<String, Object> map) {
		return bigOrderAuditDao.updateBigOrderState(map);
	}
}
