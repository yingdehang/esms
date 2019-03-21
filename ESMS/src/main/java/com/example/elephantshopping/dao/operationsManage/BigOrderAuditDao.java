package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BigOrderAuditDao {
	/**
	 * 查询区域名称
	 * 
	 * @param string
	 * @return
	 */
	String queryCityName(String string);

	/**
	 * 查询大额订单审核list的count
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryBigOrderAuditListCount(Map<String, Object> map);

	/**
	 * 查询大额订单审核list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryBigOrderAuditList(Map<String, Object> map);

	/**
	 * 查询订单状态
	 * 
	 * @param orderId
	 * @return
	 */
	String queryBigState(String orderId);

	/**
	 * 查询大额订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	List<Map<String, Object>> queryBigOrderInfo(String orderNumber);

	/**
	 * 修改大额订单状态
	 * 
	 * @param map
	 * @return
	 */
	int updateBigOrderState(Map<String, Object> map);

}
