package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatPayDao {
	/**
	 * 查询微信支付list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getWechatpaylist(Map<String, Object> map);

	/**
	 * 查询微信支付count
	 * @param map
	 * @return
	 */
	int getWechatpaylistCount(Map<String, Object> map);

}
