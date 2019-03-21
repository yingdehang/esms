package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车管理Dao
 * @author XB
 *
 */
@Mapper
public interface ShoppingCarDao
{
	/**
	 * 未分页的购物车数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getShoppingCarList(Map<String, Object> parameters);

	/**
	 * 分页的购物车数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getShoppingCarListPage(Map<String, Object> parameters);

}
