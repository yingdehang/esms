package com.example.elephantshopping.controller.systemManage;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.systemManage.MenuService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.RequestUtils;
/**
 * 菜单管理
 * @author ydh
 *
 */
@Controller
@RequestMapping("menu")
public class MenuController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private PermissionsService permissionsService;

	/**
	 * 根据角色id和菜单id查询该用户在本菜单中的权限，跳转菜单管理页面
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toMenuHtml")
	public ModelAndView toMenuHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.setViewName("/system/systemManage/menuManage/menuList");
		return mav;
	}

	/**
	 * 获取菜单管理列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getMenuList")
	@ResponseBody
	public Map<String, Object> getMenuList(HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		return menuService.getMenuList(map);
	}

	/**
	 * 添加菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addMenu")
	@ResponseBody
	public int addMenu(HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		menuService.addMenu(map);
		return 1;
	};

	/**
	 * 删除菜单
	 * 
	 * @param menuId
	 * @return
	 */
	@RequestMapping("deleteMenuById")
	@ResponseBody
	public int deleteMenuById(String menuId) {
		menuService.deleteMenuByIde(menuId);
		return 1;
	}

	/**
	 * 修改菜单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("updateMenuInfo")
	@ResponseBody
	public int updateMenuInfo(HttpServletRequest request) {
		return menuService.updateMenuInfo(RequestUtils.requestToMap(request));
	};
	/**
	 * 修改菜单排序
	 */
	@RequestMapping("updateSort")
	@ResponseBody
	public int updateSort(HttpServletRequest request) {
		return menuService.updateSort(RequestUtils.requestToMap(request));
	};

}
