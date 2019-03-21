package com.example.elephantshopping.service.systemManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.systemManage.DistributorsDao;

@Service
public class DistributorsService {
	@Autowired
	private DistributorsDao distributorsDao;

	/**
	 * 查询分享列表
	 * 
	 * @param phone
	 * @return
	 */
	public Map<String, Object> getDistributorslist(String phone) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "");
		map.put("code", 0);
		if (null != phone && (!phone.equals(""))) {
			List<Map<String, Object>> list = distributorsDao.queryDistributorsPhone(phone);
			map.put("data", list);
			map.put("count", list.size());
		} else {
			map.put("data", null);
			map.put("count", 0);
		}
		return map;
	}

	/**
	 * 验证手机号是否存在
	 * 
	 * @param phone
	 * @return
	 */
	public int phoneishave(String phone) {
		return distributorsDao.phoneishave(phone);
	}

	/**
	 * 修改邀请人
	 * 
	 * @param phone
	 * @param iNVITATION
	 * @return
	 */
	public int updatedistributors(String phone, String iNVITATION) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("iNVITATION", iNVITATION);
		return distributorsDao.updatedistributors(map);
	}
}
