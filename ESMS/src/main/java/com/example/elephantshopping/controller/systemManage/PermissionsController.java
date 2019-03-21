package com.example.elephantshopping.controller.systemManage;

import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.LoginService;
import com.example.elephantshopping.service.systemManage.MenuService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.CookiesUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 权限管理
 * 
 * @author ydh
 *
 */
@Controller
@RequestMapping("permissions")
public class PermissionsController {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private LoginService loginService;

	/**
	 * 页面加载时查询权限
	 * 
	 * @return
	 */
	@RequestMapping("queryPermissions")
	@ResponseBody
	public boolean queryPermissions(String permissioinCode, HttpServletRequest request) {
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		Set<String> permissino = loginService.getUserPermission(userPhone);
		return permissino.contains(permissioinCode);
	}

	/**
	 * 根据角色id和菜单id查询该用户在本菜单中的权限，跳转地址管理页面
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toPermissonsHtml")
	public ModelAndView toPermissonsHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.addObject("menuList", menuService.getAllMenuList());
		mav.setViewName("/system/systemManage/permissionsManage/permissionsList");
		return mav;
	}

	/**
	 * 获取权限list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getpermissionsList")
	@ResponseBody
	public Map<String, Object> getpermissionsList(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return permissionsService.getpermissionsList(map);
	}

	/**
	 * 删除权限
	 * 
	 * @param permissions_ID
	 * @return
	 */
	@RequestMapping("deletepermissionsById")
	@ResponseBody
	public int deletepermissionsById(String PERMISSIONS_ID) {
		permissionsService.deletepermissionsById(PERMISSIONS_ID);
		return 1;
	}

	/**
	 * 添加权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addpermissions")
	@ResponseBody
	public int addpermissions(HttpServletRequest request) {
		permissionsService.addpermissions(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 修改菜单权限
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("updatepermissions")
	@ResponseBody
	public int updatepermissions(HttpServletRequest request) {
		return permissionsService.updatepermissions(RequestUtils.requestToMap(request));
	}

}
