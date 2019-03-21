package com.example.elephantshopping.service.operationsManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.ShoppingCarDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 购物车管理Service
 * @author XB
 *
 */
@Service
public class ShoppingCarService
{
	@Autowired
	private ShoppingCarDao shoppingCarDao;

	/**
	 * 未分页的购物车数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getShoppingCarList(Map<String, Object> parameters) 
	{
		return shoppingCarDao.getShoppingCarList(parameters);
	}

	/**
	 * 已分页的购物车数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getShoppingCarListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> shoppingCarListPage = shoppingCarDao.getShoppingCarListPage(parameters);
		shoppingCarListPage = DateFormatUtils.transforDateType(shoppingCarListPage);
		return shoppingCarListPage;
	}
}
