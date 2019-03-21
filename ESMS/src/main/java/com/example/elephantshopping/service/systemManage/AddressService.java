package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.systemManage.AddressDao;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class AddressService {
	@Autowired
	private AddressDao addressDao;

	/**
	 * 获取字典list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getAddressList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = addressDao.getAddressList(map);
		// 查询是否有子类
		for (Map<String, Object> m : list) {
			m.put("isHaveSon", addressDao.isHaveSon(m.get("CITY_CODE")));
		}
		int count = addressDao.getAddressListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 删除字典
	 * 
	 * @param address_ID
	 */
	@Transactional
	public void deleteAddressById(String address_ID) {
		addressDao.deleteAddressById(address_ID);
	}

	/**
	 * 查询父菜单的pid
	 * 
	 * @param pID
	 * @return
	 */
	public int queryAddressParentId(int pID) {
		return addressDao.queryAddressParentId(pID);
	}

	/**
	 * 添加字典
	 * 
	 * @param requestToMap
	 */
	@Transactional
	public void addAddress(Map<String, Object> requestToMap) {
		requestToMap.put("uuid", UUIDUtils.randomID());
		addressDao.addAddress(requestToMap);
	}

	/**
	 * 修改字典
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateAddress(Map<String, Object> requestToMap) {
		return addressDao.updateAddress(requestToMap);
	}
}
