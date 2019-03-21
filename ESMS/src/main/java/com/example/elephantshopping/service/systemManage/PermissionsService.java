package com.example.elephantshopping.service.systemManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.systemManage.PermissionsDao;
import com.example.elephantshopping.utils.UUIDUtils;

/**
 * 权限service层
 * 
 * @author YDH
 *
 */
@Service
public class PermissionsService {
	@Autowired
	private PermissionsDao permissionsDao;

	/**
	 * 查询权限，返回权限列表
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> queryPermission(Map<String, Object> map) {
		// 查询该菜单所有权限
		List<Map<String, Object>> list = permissionsDao.getMenuPermission(map.get("menuId").toString());
		Map<String, Object> permissions = new HashMap<String, Object>();
		for (Map<String, Object> m : list) {
			map.put("permissionsId", m.get("PERMISSIONS_ID").toString());
			int isHave = permissionsDao.queryPermission(map);
			m.put("isHave", isHave);
			String code = m.get("PERMISSIONS_CODE").toString();
			switch (code) {
			// 查询
			case "QUERY":
				permissions.put("query", m);
				break;
			// 修改
			case "UPDATE":
				permissions.put("update", m);
				break;
			// 添加
			case "ADD":
				permissions.put("add", m);
				break;
			// 删除
			case "DELETE":
				permissions.put("delete", m);
				break;
			// 分配权限
			case "ASSIGN_PERMISSIONS":
				permissions.put("ASSIGN_PERMISSIONS", m);
				break;
			// 分配角色
			case "ASSIGN_ROLES":
				permissions.put("ASSIGN_ROLES", m);
				break;
			default:
				break;
			}
		}
		return permissions;
	}

	/**
	 * 获取权限list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getpermissionsList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = permissionsDao.getPermissionsList(map);
		int count = permissionsDao.getPermissionsListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 删除权限
	 * 
	 * @param cITY_CODE
	 */
	public void deletepermissionsById(String PERMISSIONS_ID) {
		permissionsDao.deletepermissionsById(PERMISSIONS_ID);
	}

	/**
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updatepermissions(Map<String, Object> requestToMap) {
		return permissionsDao.updatepermissions(requestToMap);
	}

	/**
	 * 添加权限
	 * 
	 * @param requestToMap
	 */
	public void addpermissions(Map<String, Object> requestToMap) {
		requestToMap.put("uuid", UUIDUtils.randomID());
		permissionsDao.addpermissions(requestToMap);
	}
}
