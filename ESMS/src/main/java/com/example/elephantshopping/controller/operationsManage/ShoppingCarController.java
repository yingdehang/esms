package com.example.elephantshopping.controller.operationsManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.ShoppingCarService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 购物车管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("shoppingCar")
public class ShoppingCarController
{
	@Autowired
	private ShoppingCarService shoppingCarService;
	
	/**
	 *  转到购物车列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toShoppingCarList")
	public ModelAndView toShoppinCarList(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/operationsManage/shoppingCarManage/shoppingCarList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询购物车数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getShoppingCarList")
	@ResponseBody
	public Map<String,Object> getShoppingCarList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> shoppingCarList = shoppingCarService.getShoppingCarList(parameters);//未分页的数据List
		List<Map<String,Object>> shoppingCarListPage = shoppingCarService.getShoppingCarListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",shoppingCarListPage);
		map.put("count",shoppingCarList.size());
		return map;
	}
}
