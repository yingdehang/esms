package com.example.elephantshopping.controller.systemManage;

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

import com.example.elephantshopping.service.systemManage.VersionService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 版本管理
 * @author XB
 *
 */
@RequestMapping("version")
@Controller
public class VersionController {
	@Autowired
	private VersionService versionService;
	
	/**
	 * 转到版本管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toVersionManage")
	public ModelAndView toVersionManage(ModelAndView modelAndView) {
		modelAndView.setViewName("/system/systemManage/versionManage/versionManage");
		return modelAndView;
	}
	
	/**
	 * 获取版本列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getVersionList")
	@ResponseBody
	public Map<String, Object> getVersionList(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> versionList = versionService.getVersionList(parameters);// 未分页的数据List
		List<Map<String, Object>> versionListPage = versionService.getVersionListPage(parameters);// 分页的数据List
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", versionListPage);
		map.put("count", versionList.size());
		return map;
	}
	
	/**
	 * 修改版本数据
	 * @param request
	 * @return
	 */
	@RequestMapping("modify")
	@ResponseBody
	public int modify(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		versionService.modify(parameters);
		return 0;
	}
	
	/**
	 * 添加版本
	 * @param request
	 * @return
	 */
	@RequestMapping("addVersion")
	@ResponseBody
	public int addClass(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		parameters.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		versionService.addVersion(parameters);
		return 0;
	}
}
