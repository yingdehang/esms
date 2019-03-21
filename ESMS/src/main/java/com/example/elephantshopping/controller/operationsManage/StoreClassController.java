package com.example.elephantshopping.controller.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.StoreClassService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 店铺管理Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("storeClass")
public class StoreClassController {
	@Autowired
	private StoreClassService storeClassService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 转到店铺分类列表页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toStoreClassList")
	public ModelAndView toStoreClassList(ModelAndView modelAndView, HttpServletRequest request) {
		modelAndView.addObject("addXxClass", permissionsController.queryPermissions("addXxClass", request));
		modelAndView.addObject("deleteXxClass", permissionsController.queryPermissions("deleteXxClass", request));
		modelAndView.setViewName("/system/operationsManage/storeClassManage/storeClassList");
		return modelAndView;
	}

	/**
	 * 获取/查询店铺分类数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getStoreClassList")
	@ResponseBody
	public Map<String, Object> getStoreClassList(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> storeClassList = storeClassService.getStoreClassList(parameters);// 未分页的数据List
		List<Map<String, Object>> storeClassListPage = storeClassService.getStoreClassListPage(parameters);// 分页的数据List
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", storeClassListPage);
		map.put("count", storeClassList.size());
		return map;
	}

	/**
	 * 修改店铺分类名称
	 * 
	 * @param storeClassId
	 * @param storeClassName
	 * @return
	 */
	@RequestMapping("edit")
	@ResponseBody
	public int edit(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		storeClassService.edit(parameters);
		return 0;
	}

	/**
	 * 添加店铺分类
	 * 
	 * @param className
	 * @return
	 */
	@RequestMapping("addClass")
	@ResponseBody
	public int addClass(String className) {
		storeClassService.addClass(className);
		return 0;
	}

	/**
	 * 删除店铺分类
	 * 
	 * @param className
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public String delete(String storeClassId) {
		List<Map<String, Object>> list = storeClassService.getStoreByClass("%" + storeClassId + "%");// 查询此店铺分类是否关联店铺
		if (list.size() > 0) {
			return "此分类与多个店铺关联，无法删除";
		} else {
			storeClassService.delete(storeClassId);
			return "ok";
		}
	}
}
