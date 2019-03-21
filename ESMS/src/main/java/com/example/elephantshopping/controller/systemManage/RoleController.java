package com.example.elephantshopping.controller.systemManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.service.systemManage.RoleService;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("role")
public class RoleController {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private RoleService roleService;

	/**
	 * 查询权限跳转角色管理list
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toRoleHtml")
	public ModelAndView toRoleHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.addObject("ASSIGN_PERMISSIONS", m.get("ASSIGN_PERMISSIONS"));
		mav.setViewName("/system/systemManage/roleManage/roleList");
		return mav;
	};

	/**
	 * 获取角色管理list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getRoleList")
	@ResponseBody
	public Map getRoleList(HttpServletRequest request) {
		return roleService.getRoleList(RequestUtils.requestToMap(request));
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping("deleteRoleById")
	@ResponseBody
	public int deleteRoleById(String ROLE_ID) {
		roleService.deleteRoleById(ROLE_ID);
		return 1;
	}

	/**
	 * 添加
	 */
	@RequestMapping("addrole")
	@ResponseBody
	public int addrole(HttpServletRequest request) {
		roleService.addrole(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 修改
	 */
	@RequestMapping("updateRole")
	@ResponseBody
	public int updateRole(HttpServletRequest request) {
		return roleService.updateRole(RequestUtils.requestToMap(request));
	}

	/**
	 * 获取角色菜单
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping("getrolemenulist")
	@ResponseBody
	public Map<String, Object> getrolemenulist(String roleId, String pid) {
		return roleService.getRoleMenuList(roleId, pid);
	}

	/**
	 * 
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	@RequestMapping("getrolemenupermissions")
	@ResponseBody
	public Map<String, Object> getrolemenupermissions(String roleId, String menuId) {
		return roleService.getrolemenupermissions(roleId, menuId);
	}

	/**
	 * 分配角色权限
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addRolePermissions")
	public int addRolePermissions(String roleId, String permissionsId) {
		roleService.addRolePermissions(roleId, permissionsId);
		return 1;
	}

	/**
	 * 删除角色权限
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteRolePermissions")
	public int deleteRolePermissions(HttpServletRequest request) {
		roleService.deleteRolePermissions(RequestUtils.requestToMap(request));
		return 1;
	}

}
