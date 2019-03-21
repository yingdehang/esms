package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.systemManage.WechatPayDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class WechatPayService {
	@Autowired
	private WechatPayDao wechatPayDao;

	/**
	 * 微信支付list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getWechatpaylist(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", (page - 1) * limit);
		List<Map<String, Object>> list = wechatPayDao.getWechatpaylist(map);
		List<Map<String, Object>> list1 = DateFormatUtils.timeforDateType(list, "time_end");
		List<Map<String, Object>> list2 = DateFormatUtils.timeforDateType(list1, "orderTime");
		int count = wechatPayDao.getWechatpaylistCount(map);
		map.clear();
		map.put("count", count);
		map.put("msg", "");
		map.put("data", list2);
		map.put("code", 0);
		return map;
	}
}
