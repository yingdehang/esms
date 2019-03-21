package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemUserDao {
	/**
	 * 获取系统用户list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getSystemUserList(Map<String, Object> map);

	/**
	 * 获取系统户总数
	 * 
	 * @param map
	 * @return
	 */
	int getSystemUserListCount(Map<String, Object> map);

	/**
	 * 修改用户昵称
	 * 
	 * @param map
	 * @return
	 */
	int updateSystemUser(Map<String, Object> map);

	/**
	 * 删除用户表中的该用户
	 * 
	 * @param uSERS_ID
	 */
//	void deleteSystemUser(String uSERS_ID);

	/**
	 * 删除角色用户管理表中的该用户信息
	 * 
	 * @param uSERS_ID
	 */
	void deleteRoleAndUser(String uSERS_ID);

	/**
	 * 删除用户图片
	 * 
	 * @param uSERS_ID
	 */
	void deleteImg(String uSERS_ID);

	/**
	 * 获取所有角色list
	 * 
	 * @return
	 */
	List<Map<String, Object>> getRoleList();

	/**
	 * 获取地区list
	 * 
	 * @param pid
	 * @return
	 */
	List<Map<String, Object>> getAreaList(int pid);

	/**
	 * 添加系统用户
	 * 
	 * @param map
	 */
	void addSystemUser(Map<String, Object> map);

	/**
	 * 用户角色中间表添加关联
	 * 
	 * @param map
	 */
	void addSystemUserRole(Map<String, Object> map);

	/**
	 * 禁用和解除禁用
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateUserState(Map<String, Object> requestToMap);

	/**
	 * 获取角色list
	 * 
	 * @return
	 */
	List<Map<String, Object>> getAssignRoleList();

	/**
	 * 查询用户是否分配该角色
	 * 
	 * @param map
	 */
	int isHave(Map<String, Object> map);

	/**
	 * 删除用户角色
	 * 
	 * @param requestToMap
	 */
	void deleteUserRole(Map<String, Object> requestToMap);

	/**
	 * 查询城市名称
	 * 
	 * @param string
	 * @return
	 */
	String getCityName(String string);

	/**
	 * 验证是否存在
	 * 
	 * @param pHONE
	 * @return
	 */
	int ishavephone(String pHONE);

	/**
	 * 修改头像
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateUserHead(Map<String, Object> requestToMap);

	/**
	 * 添加系统用户验证是否存在
	 * 
	 * @param pHONE
	 * @return
	 */
	int PhoneIsHave(String pHONE);

	/**
	 * 查询用户的id
	 * 
	 * @param pHONE
	 * @return
	 */
	String queryUserId(String pHONE);

	/**
	 * 将用户修改为系统用户
	 * 
	 * @param userId
	 * @return
	 */
	int updateUserIsSystem(String userId);

}
