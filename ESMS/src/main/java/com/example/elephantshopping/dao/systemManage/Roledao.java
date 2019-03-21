package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Roledao {
	/**
	 * 获取角色菜单
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getRoleList(Map map);

	/**
	 * 查询角色总数
	 * 
	 * @param map
	 * @return
	 */
	int getRoleListCount(Map map);

	/**
	 * 删除角色
	 * 
	 * @param rOLE_ID
	 */
	void deleteRoleById(String rOLE_ID);

	/**
	 * 删除角色权限关了信息
	 * 
	 * @param rOLE_ID
	 */
	void deleteRolePermissionById(String rOLE_ID);

	/**
	 * 添加
	 * 
	 * @param map
	 */
	void addrole(Map map);

	/**
	 * 修改
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateRole(Map requestToMap);

	/**
	 * 获取权限菜单list
	 * 
	 * @return
	 */
	List<Map<String, Object>> getmenupermissionList(String pid);

	/**
	 * 验证用户是否用药该权限
	 * 
	 * @param map
	 * @return
	 */
	int isHavePermissions(Map<String, Object> map);

	/**
	 * 查询菜单权限
	 * 
	 * @param menuId
	 * @return
	 */
	List<Map<String, Object>> getrolemenupermissions(String menuId);

	/**
	 * 添加角色权限
	 * 
	 * @param map
	 */
	void addRolePermissions(Map<String, Object> map);

	/**
	 * 刪除权限
	 * @param requestToMap
	 */
	void deleteRolePermissions(Map requestToMap);

}
