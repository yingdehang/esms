package com.example.elephantshopping.service.systemManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elephantshopping.dao.systemManage.SystemUserDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class SystemUserService {
	@Autowired
	private SystemUserDao systemUserDao;

	/**
	 * 获取系统用户list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getSystemUserList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = systemUserDao.getSystemUserList(map);
		int count = systemUserDao.getSystemUserListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(list, "REGISTERED_TIME"));
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 修改用户昵称
	 * 
	 * @param map
	 * @return
	 */
	public int updateSystemUser(Map<String, Object> map) {
		return systemUserDao.updateSystemUser(map);
	}

	// /**
	// * 删除用户
	// *
	// * @param uSERS_ID
	// */
	// @Transactional
	// public void deleteSystemUser(String uSERS_ID) {
	// // 删除用户表该角色
	// // systemUserDao.deleteSystemUser(uSERS_ID);
	// // 删除角色用户关联表中该用户信息
	// // systemUserDao.deleteRoleAndUser(uSERS_ID);
	// // 删除图片表中该用户的头像等信息
	// // systemUserDao.deleteImg(uSERS_ID);
	// }

	/**
	 * 获取所有角色list
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getRoleList() {
		return systemUserDao.getRoleList();
	}

	/**
	 * 获取所有地区list,第一次获取的父id为1的所有
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAreaList(int pid) {
		return systemUserDao.getAreaList(pid);
	}

	/**
	 * 添加系统用户
	 * 
	 * @param map
	 */
	@Transactional
	public void addSystemUser(Map<String, Object> map, String[] roles) {
		String PHONE = map.get("PHONE").toString();
		// 查询用户是否存在 0:不存在，1:存在
		String userId = systemUserDao.queryUserId(PHONE);
		// 修改帐号为系统帐号
		int ii = systemUserDao.updateUserIsSystem(userId);
		// 角色用户中间表
		if (null != roles && roles.length > 0) {
			for (String role_id : roles) {
				map.clear();
				map.put("userId", userId);
				map.put("uuid", UUIDUtils.randomID());
				map.put("roleId", role_id);
				systemUserDao.addSystemUserRole(map);
			}
		}
	}

	/**
	 * 禁用和解除禁用
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateUserState(Map<String, Object> requestToMap) {
		return systemUserDao.updateUserState(requestToMap);
	}

	/**
	 * 获取角色分配list
	 * 
	 * @return
	 */
	public Map<String, Object> getAssignRoleList(String userId) {
		List<Map<String, Object>> list = systemUserDao.getAssignRoleList();
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		for (Map<String, Object> role : list) {
			map.put("roleId", role.get("ROLE_ID"));
			int i = systemUserDao.isHave(map);
			role.put("isHave", i);
		}
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", 0);
		return map;
	}

	/**
	 * 添加用户角色
	 * 
	 * @param requestToMap
	 */
	public void addUserRole(Map<String, Object> requestToMap) {
		requestToMap.put("uuid", UUIDUtils.randomID());
		systemUserDao.addSystemUserRole(requestToMap);
	}

	/**
	 * 删除用户角色
	 * 
	 * @param requestToMap
	 */
	public void deleteUserRole(Map<String, Object> requestToMap) {
		systemUserDao.deleteUserRole(requestToMap);

	}

	/**
	 * 验证帐号是否已存在
	 * 
	 * @param pHONE
	 * @return
	 */
	public int ishavephone(String pHONE) {
		int i = systemUserDao.ishavephone(pHONE);
		int ii = systemUserDao.PhoneIsHave(pHONE);
		if (i > 0) {
			return 1;
		}
		if (ii == 0) {
			return ii;
		}
		return 2;
	}

	/**
	 * 修改头像
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateUserHead(Map<String, Object> requestToMap) {
		return systemUserDao.updateUserHead(requestToMap);
	}
}
