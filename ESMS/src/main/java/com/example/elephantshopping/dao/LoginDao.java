package com.example.elephantshopping.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 登陆接口
 * 
 * @author ydh
 *
 */
@Mapper
public interface LoginDao {
	/**
	 * 登陆
	 * 
	 * @param map
	 * @return
	 */
	int userlogin(Map map);

	/**
	 * 获取登录菜单
	 * 
	 * @param nICKNAME
	 * @return
	 */
	List<Map<String, Object>> getMenu(String nICKNAME);

	/**
	 * 获取用户名
	 * 
	 * @param pHONE
	 * @return
	 */
	Map<String, Object> getUserInfo(String pHONE);

	/**
	 * 获取用户头像
	 * 
	 * @param string
	 * @return
	 */
	String getUserHead(String string);

	/**
	 * 查询用户权限
	 * 
	 * @param phone
	 * @return
	 */
	List<Map<String, Object>> getUserPermission(String phone);

}
