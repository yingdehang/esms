package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 权限接口
 * 
 * @author ASUS
 *
 */
@Mapper
public interface PermissionsDao {
	/**
	 * 查询用户在该菜单中是否有该权限
	 * 
	 * @param map
	 * @return
	 */
	int queryPermission(Map map);

	/**
	 * 查询该菜单所有权限
	 * 
	 * @param menuId
	 * @return
	 */
	List<Map<String, Object>> getMenuPermission(String menuId);

	/**
	 * 查询权限list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getPermissionsList(Map map);

	/**
	 * 查询权限count
	 * 
	 * @param map
	 * @return
	 */
	int getPermissionsListCount(Map map);

	/**
	 * 查询是否有子权限
	 * 
	 * @param object
	 * @return
	 */
	int isHaveSon(Object object);

	/**
	 * 添加权限
	 * 
	 * @param requestToMap
	 */
	void addpermissions(Map requestToMap);

	/**
	 * 删除权限
	 * 
	 * @param pERMISSIONS_ID
	 */
	void deletepermissionsById(String pERMISSIONS_ID);

	/**
	 * 修改权限
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updatepermissions(Map requestToMap);
}
