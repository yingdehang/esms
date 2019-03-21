package com.example.elephantshopping.controller.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.EarningRatioService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 收益比例管理
 * @author XB
 *
 */
@Controller
@RequestMapping("earningRatio")
public class EarningRatioController
{
	@Autowired
	private EarningRatioService earningRatioService;
	
	/**
	 * 转到收益比例管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toEarningRatio")
	public ModelAndView toEarningRatio(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/operationsManage/earningRatioManage/earningRatio");
		return modelAndView;
	}
	
	/**
	 * 获得收益比例列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getEarningRatio")
	@ResponseBody
	public Map<String,Object> getEarningRatio(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> earningRatio = earningRatioService.getEarningRatio(parameters);//未分页的收益比例数据
		List<Map<String,Object>> earningRatioPage = earningRatioService.getEarningRatioPage(parameters);//已分页的收益比例数据
		map.put("code",0);
		map.put("msg","");
		map.put("data",earningRatioPage);
		map.put("count",earningRatio.size());
		return map;
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public int delete(String id)
	{
		earningRatioService.delete(id);
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
		earningRatioService.edit(parameters);
		return 0;
	}
	/**
	 * 添加
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public int add(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		parameters.put("id",UUID.randomUUID().toString().replaceAll("-", ""));
		earningRatioService.add(parameters);
		return 0;
	}
}
