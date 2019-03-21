package com.example.elephantshopping.service.systemManage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elephantshopping.dao.systemManage.MenuDao;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class MenuService {
	@Autowired
	private MenuDao menuDao;

	/**
	 * 获取菜单列表
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getMenuList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> menuList = menuDao.getMenuList(map);
		for (Map<String, Object> m : menuList) {
			m.put("isHaveSon", menuDao.isHaveSon(m.get("MENU_ID")));
		}
		int count = menuDao.getMenuListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", menuList);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 添加菜单
	 * 
	 * @param map
	 */
	@Transactional
	public void addMenu(Map<String, Object> map) {
		map.put("uuid", UUIDUtils.randomID());
		menuDao.addMenu(map);
		map.put("MENU_NAME", map.get("MENU_NAME").toString() + "显示");
		// 向权限表添加
		menuDao.addToPermissions(map);
	}

	/**
	 * 删除菜单
	 * 
	 * @param menuId
	 */
	@Transactional
	public void deleteMenuByIde(String menuId) {
		menuDao.deleteMenuById(menuId);
		// 删除权限表中的信息
		menuDao.deletePermissons(menuId);
	}

	/**
	 * 修改菜单
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateMenuInfo(Map<String, Object> requestToMap) {
		return menuDao.updateMenuInfo(requestToMap);
	}

	/**
	 * 获取全部子菜单
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getAllMenuList() {
		List<Map<String, Object>> list = menuDao.getAllMenuList();
		List<Map<String, Object>> listone = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			if (map.get("PID").toString().equals("0")) {
				List<Map<String, Object>> listtwo = new ArrayList<Map<String, Object>>();
				for (Map<String, Object> m : list) {
					if (m.get("PID").equals(map.get("MENU_ID"))) {
						listtwo.add(m);
					}
				}
				map.put("sonMenuList", listtwo);
				listone.add(map);
			}
		}
		return listone;
	}

	/**
	 * 修改菜单排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateSort(Map<String, Object> requestToMap) {
		return menuDao.updateSort(requestToMap);
	}
}
