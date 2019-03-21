package com.example.elephantshopping.controller.systemManage;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.service.systemManage.SystemUserService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * SYSTEM用户管理Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("systemUser")
public class SystemUserController {
	@Autowired
	private SystemUserService systemUserService;
	@Autowired
	private PermissionsService permissionService;

	/**
	 * 转到SYSTEM用户列表页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("tosystemUserList")
	public ModelAndView tosystemUserList(ModelAndView mav, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		Map<String, Object> m = permissionService.queryPermission(map);
		mav.addObject("info", map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.addObject("ASSIGN_ROLES", m.get("ASSIGN_ROLES"));
		mav.setViewName("/system/systemManage/systemUserManage/systemUserList");
		return mav;
	}

	/**
	 * 获取系统用户list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getSystemUserList")
	@ResponseBody
	public Map<String, Object> getSystemUserList(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return systemUserService.getSystemUserList(map);
	}

	/**
	 * 修改
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("updateSystemUser")
	@ResponseBody
	public int updateSystemUser(HttpServletRequest request) {
		return systemUserService.updateSystemUser(RequestUtils.requestToMap(request));
	}

	// /**
	// * 删除帐号
	// *
	// * @param USERS_ID
	// * @return
	// */
	// @RequestMapping("deleteSystemUser")
	// @ResponseBody
	// public int deleteSystemUser(String USERS_ID) {
	// systemUserService.deleteSystemUser(USERS_ID);
	// return 1;
	// }

	/**
	 * 跳转添加系统用户界面
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping("toAddUserHtml")
	public ModelAndView toAddUserHtml(ModelAndView mav, HttpServletRequest request) {
		mav.addObject("info", RequestUtils.requestToMap(request));
		mav.addObject("roleList", systemUserService.getRoleList());
		mav.addObject("areaList", systemUserService.getAreaList(1));
		mav.setViewName("/system/systemManage/systemUserManage/addSystemUser");
		return mav;
	}

	/**
	 * 通过获取地址list
	 * 
	 * @param pid
	 * @return
	 */
	@RequestMapping("getAreaList")
	@ResponseBody
	public List<Map<String, Object>> getAreaList(int pid) {
		return systemUserService.getAreaList(pid);
	}

	/**
	 * 添加系统用户
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("addSystemUser")
	public ModelAndView addSystemUser(ModelAndView mav, String[] roles, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		systemUserService.addSystemUser(map, roles);
		return tosystemUserList(mav, request);
	}

	/**
	 * 验证是否存在用户账号
	 */
	@RequestMapping("ishavephone")
	@ResponseBody
	public int ishavephone(String PHONE) {
		return systemUserService.ishavephone(PHONE);
	}

	/**
	 * 解除和禁用
	 */
	@RequestMapping("updateState")
	@ResponseBody
	public int updateState(HttpServletRequest request) {
		return systemUserService.updateUserState(RequestUtils.requestToMap(request));
	}

	/**
	 * 跳转分配角色页面
	 */
	@RequestMapping("gotoAssignRole")
	public ModelAndView gotoAssignRole(ModelAndView mav, String userId) {
		mav.addObject("userId", userId);
		mav.setViewName("/system/systemManage/systemUserManage/assignRole");
		return mav;
	}

	/**
	 * 获取用户角色list
	 * 
	 * @param mav
	 * @param userId
	 * @return
	 */
	@RequestMapping("assignRolelist")
	@ResponseBody
	public Map<String, Object> assignRolelist(ModelAndView mav, String userId) {
		return systemUserService.getAssignRoleList(userId);
	}

	/**
	 * 分配用户角色
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addUserRole")
	@ResponseBody
	public int addUserRole(HttpServletRequest request) {
		systemUserService.addUserRole(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 取消用户角色
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("deleteUserRole")
	@ResponseBody
	public int deleteUserRole(HttpServletRequest request) {
		systemUserService.deleteUserRole(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 修改用户头像
	 */
	@RequestMapping("updateUserHead")
	@ResponseBody
	public int updateUserHead(HttpServletRequest request) {
		return systemUserService.updateUserHead(RequestUtils.requestToMap(request));
	}

}
