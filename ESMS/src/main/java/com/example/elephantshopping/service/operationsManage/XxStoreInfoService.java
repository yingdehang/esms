package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.operationsManage.XxStoreInfoDao;

@Service
public class XxStoreInfoService {
	@Autowired
	private XxStoreInfoDao xxStoreInfoDao;

	/**
	 * 线下店铺内容审核list
	 * 
	 * @param requestToMap
	 */
	public Map<String, Object> getXxStoreInfolist(Map<String, Object> requestToMap) {
		int page = Integer.parseInt(requestToMap.get("page").toString());
		int limit = Integer.parseInt(requestToMap.get("limit").toString());
		page = (page - 1) * limit;
		requestToMap.put("page", page);
		requestToMap.put("limit", limit);
		int count = xxStoreInfoDao.getXxStoreInfolistcount(requestToMap);
		List<Map<String, Object>> list = xxStoreInfoDao.getXxStoreInfolist(requestToMap);
		if (list.size() > 0) {
			for (Map<String, Object> m : list) {
				m.put("PROVINCE", xxStoreInfoDao.queryCityName(m.get("PROVINCE").toString()));
				m.put("CITY", xxStoreInfoDao.queryCityName(m.get("CITY").toString()));
				m.put("AREA", xxStoreInfoDao.queryCityName(m.get("AREA").toString()));
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("code", 0);
		map.put("count", count);
		map.put("msg", "");
		return map;

	}

	/**
	 * 查询店铺内容审核详情
	 * 
	 * @param eSXID
	 * @return
	 */
	public Map<String, Object> getXxStoreInfoById(String eSXID) {
		Map<String, Object> storeInfo = xxStoreInfoDao.getXxStoreInfoById(eSXID);
		String PROVINCE = xxStoreInfoDao.queryCityName(storeInfo.get("PROVINCE").toString());
		String CITY = xxStoreInfoDao.queryCityName(storeInfo.get("CITY").toString());
		String AREA = xxStoreInfoDao.queryCityName(storeInfo.get("AREA").toString());
		storeInfo.put("address", PROVINCE + CITY + AREA);
		String realName = storeInfo.get("UC_NAMES").toString();
		String phone = storeInfo.get("PHONE").toString();
		String nameAndPhone = realName + "(" + phone + ")";
		storeInfo.put("nameAndPhone", nameAndPhone);
		String[] photos = null;
		if (null != storeInfo.get("STORE_PHOTO") && (!storeInfo.get("STORE_PHOTO").equals(""))) {
			photos=storeInfo.get("STORE_PHOTO").toString().split(",");
		}
		storeInfo.put("photos", photos);
		return storeInfo;
	}

	/**
	 * 修改店铺内容审核状态
	 * 
	 * @param ESXID
	 * @param infoState
	 * @param content
	 * @return
	 */
	public int updateStoreInfoState(String ESXID, String infoState, String content) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ESXID", ESXID);
		map.put("infoState", infoState);
		map.put("content", content);
		return xxStoreInfoDao.updateStoreInfoState(map);
	}

}
