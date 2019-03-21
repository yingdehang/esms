package com.example.elephantshopping.service.systemManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elephantshopping.dao.systemManage.Roledao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class RoleService {
	@Autowired
	private Roledao roleDao;

	public Map<String, Object> getRoleList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> menuList = roleDao.getRoleList(map);
		int count = roleDao.getRoleListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(menuList, "CREATE_TIME"));
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 删除
	 * 
	 * @param rOLE_ID
	 */
	@Transactional
	public void deleteRoleById(String rOLE_ID) {
		roleDao.deleteRoleById(rOLE_ID);
		// 删除角色权限关了信息
		roleDao.deleteRolePermissionById(rOLE_ID);
	}

	/**
	 * 添加
	 * 
	 * @param requestToMap
	 */
	public void addrole(Map<String, Object> map) {
		map.put("uuid", UUIDUtils.randomID());
		roleDao.addrole(map);
	}

	/**
	 * 修改
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateRole(Map<String, Object> requestToMap) {
		return roleDao.updateRole(requestToMap);
	}

	/**
	 * 获取主菜单list
	 * 
	 * @param roleId
	 * @return
	 */
	public Map<String, Object> getRoleMenuList(String roleId, String pid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		if (null == pid) {
			pid = "0";
		}
		List<Map<String, Object>> list = roleDao.getmenupermissionList(pid);
		// 查询角色是否拥有该菜单的权限
		for (Map<String, Object> m : list) {
			map.put("permissionsId", m.get("PERMISSIONS_ID"));
			m.put("isHave", roleDao.isHavePermissions(map));
		}
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", 0);
		return map;
	}

	/**
	 * 获取角色在本菜单权限中的权限
	 * 
	 * @param roleId
	 * @param menuId
	 * @return
	 */
	public Map<String, Object> getrolemenupermissions(String roleId, String menuId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		List<Map<String, Object>> list = roleDao.getrolemenupermissions(menuId);
		// 查询角色是否拥有该菜单的权限
		for (Map<String, Object> m : list) {
			map.put("permissionsId", m.get("PERMISSIONS_ID"));
			m.put("isHave", roleDao.isHavePermissions(map));
		}
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", list.size());
		map.put("code", 0);
		return map;
	}

	/**
	 * 添加角色权限
	 * 
	 * @param roleId
	 * @param permissionsId
	 */
	public void addRolePermissions(String roleId, String permissionsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("permissionsId", permissionsId);
		map.put("uuid", UUIDUtils.randomID());
		roleDao.addRolePermissions(map);
	}

	/**
	 * 删除角色权限
	 * 
	 * @param requestToMap
	 */
	public void deleteRolePermissions(Map<String, Object> requestToMap) {
		roleDao.deleteRolePermissions(requestToMap);
	}
}
