package com.example.elephantshopping.controller.goodsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.goodsManage.AttributeService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 商品属性Controller
 * @author XB
 *
 */

@Controller
@RequestMapping("attribute")
public class AttributeController
{
	@Autowired
	private AttributeService attributeService;
	
	/**
	 * 转到属性列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toAttributeList")
	public ModelAndView toAttributeList(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/goodsManage/attributeManage/attributeList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询属性数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getAttributeList")
	@ResponseBody
	public Map<String,Object> getAttributeList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> attributeListPage = attributeService.getAttributeListPage(parameters);//分页的数据List
		List<Map<String,Object>> attributeList = attributeService.getAttributeList(parameters);//未分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",attributeListPage);
		map.put("count",attributeList.size());
		return map;
	}
	
	/**
	 * 删除属性
	 * @param attributeId
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public int delete(String attributeId)
	{
		attributeService.delete(attributeId);
		return 0;
	}
	
	/**
	 * 修改
	 * @param request
	 * @return
	 */
	@RequestMapping("edit")
	@ResponseBody
	public int edit(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		String field = (String) parameters.get("field");
		if(field.equals("ATTRIBUTE_NAME"))
		{
			attributeService.editAttributeName(parameters);
		}
		if(field.equals("ATTRIBUTE_VALUE"))
		{
			attributeService.editAttributeValue(parameters);
		}
		return 0;
	}
}
