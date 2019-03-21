package com.example.elephantshopping.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.LoginDao;

/**
 * 用戶登陆
 * 
 * @author ydh
 *
 */
@Service
public class LoginService {
	@Autowired
	private LoginDao loginDao;

	/**
	 * 用户登陆
	 * 
	 * @param map
	 * @return
	 */
	public int userlogin(Map map) {
		return loginDao.userlogin(map);
	}

	/**
	 * 根据用户名获取菜单
	 * 
	 * @param nICKNAME
	 * @return
	 */
	public List<Map<String, Object>> getMenu(String PHONE) {
		List<Map<String, Object>> list = loginDao.getMenu(PHONE);
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
	 * 获取用户信息显示
	 * 
	 * @param pHONE
	 * @return
	 */
	public Map<String, Object> getUserInfo(String pHONE) {
		Map<String, Object> map = loginDao.getUserInfo(pHONE);
		if (null != map) {
			if (null == map.get("HEAD") || map.get("HEAD").equals("")) {
				map.put("HEAD", "http://imagexb.test.upcdn.net/xbsc/user/20180327170728350.png");
			}
			if (null == map.get("NICKNAME") || map.get("NICKNAME").equals("")) {
				map.put("NICKNAME", map.get("PHONE"));
			}
		}
		return map;
	}

	/**
	 * 查询用户权限
	 * 
	 * @param phone
	 * @return
	 */
	public Set<String> getUserPermission(String phone) {
		List<Map<String, Object>> list = loginDao.getUserPermission(phone);
		Set<String> set = new HashSet<>();
		for (Map<String, Object> m : list) {
			set.add(m.get("PERMISSIONS_CODE").toString());
		}
		return set;
	}

}
