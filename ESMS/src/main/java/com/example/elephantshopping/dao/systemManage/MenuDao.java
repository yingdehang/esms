package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单管理Dao层
 * 
 * @author ASUS
 *
 */
@Mapper
public interface MenuDao {
	/**
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getMenuList(Map map);

	/**
	 * 获取总数
	 * 
	 * @param map
	 * @return
	 */
	int getMenuListCount(Map map);

	/**
	 * 添加菜单
	 * 
	 * @param map
	 */
	void addMenu(Map map);

	/**
	 * 删除菜单
	 * 
	 * @param menuId
	 */
	void deleteMenuById(String menuId);

	/**
	 * 修改菜单
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateMenuInfo(Map requestToMap);

	/**
	 * 获取全部菜单list
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAllMenuList();

	/**
	 * 向权限表添加数据
	 * 
	 * @param map
	 */
	void addToPermissions(Map map);

	/**
	 * 删除权限表中该菜单的信息
	 * 
	 * @param menuId
	 */
	void deletePermissons(String menuId);

	/**
	 * 查询该父菜单是否有子菜单
	 * 
	 * @param object
	 * @return
	 */
	int isHaveSon(Object object);

	/**
	 * 修改菜单排序
	 * @param requestToMap
	 * @return
	 */
	int updateSort(Map requestToMap);

}
