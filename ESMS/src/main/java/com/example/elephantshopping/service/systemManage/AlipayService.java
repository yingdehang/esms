package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.systemManage.AlipayDao;
import com.example.elephantshopping.utils.DateFormatUtils;

@Service
public class AlipayService {
	@Autowired
	private AlipayDao alipayDao;

	public Map<String, Object> getalipaypayList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		map.put("page", (page - 1) * limit);
		List<Map<String, Object>> list = alipayDao.getalipaypayList(map);
		List<Map<String, Object>> list1 = DateFormatUtils.timeforDateType(list, "gmt_create");
		List<Map<String, Object>> list2 = DateFormatUtils.timeforDateType(list1, "gmt_payment");
		List<Map<String, Object>> list3 = DateFormatUtils.timeforDateType(list2, "gmt_close");
		int count = alipayDao.getalipaypayListCount(map);
		map.clear();
		map.put("count", count);
		map.put("msg", "");
		map.put("data", list3);
		map.put("code", 0);
		return map;
	}
}
