package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlipayDao {
	/**
	 * 获取微信支付list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getalipaypayList(Map map);

	/**
	 * 微信支付count
	 * 
	 * @param map
	 * @return
	 */
	int getalipaypayListCount(Map map);

}
