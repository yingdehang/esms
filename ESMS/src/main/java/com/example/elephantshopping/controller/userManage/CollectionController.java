package com.example.elephantshopping.controller.userManage;

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

import com.example.elephantshopping.service.userManage.CollectionService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 收藏管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("collection")
public class CollectionController
{
	@Autowired
	private CollectionService collectionService;
	
	/**
	 *  转到收藏列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toCollectionList")
	public ModelAndView toCollectionList(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/userManage/collectionManage/collectionList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询收藏数据
	 * @param phoneNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("getCollectionList")
	@ResponseBody
	public Map<String,Object> getCollectionList(String phoneNumber,HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> collectionList = new ArrayList<Map<String,Object>>();//未分页的数据List
		List<Map<String,Object>> collectionListPage = new ArrayList<Map<String,Object>>();//分页的数据List
		if(phoneNumber!=null)
		{
			//查询收藏
			collectionList = collectionService.searchCollection(phoneNumber);
			//查询收藏并分页
			collectionListPage = collectionService.searchCollectionPage(parameters);
		}
		else
		{
			//获取收藏数据
			collectionList = collectionService.getCollectionList();
			//获取收藏数据并分页
			collectionListPage = collectionService.getCollectionListPage(parameters);
		}
		map.put("code",0);
		map.put("msg","");
		map.put("data",collectionListPage);
		map.put("count",collectionList.size());
		return map;
	}
}
