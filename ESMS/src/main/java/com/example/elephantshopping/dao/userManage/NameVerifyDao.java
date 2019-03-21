package com.example.elephantshopping.dao.userManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 实名认证管理Dao
 * @author XB
 *
 */
@Mapper
public interface NameVerifyDao
{
	/**
	 * 查询实名认证
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> searchNameVerify(Map<String, Object> parameters);

	/**
	 * 查询实名认证并分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> searchNameVerifyPage(Map<String, Object> parameters);

	/**
	 * 获取实名认证数据
	 * @return
	 */
	List<Map<String, Object>> getNameVerifyList();

	/**
	 * 获取实名认证数据并分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getNameVerifyListPage(Map<String, Object> parameters);

	/**
	 * 获取实名认证状态
	 * @return
	 */
	List<Map<String, Object>> getVerifyStates();

	/**
	 * 获取所有实名认证状态
	 * @return
	 */
	List<Map<String, Object>> getAllVerifyStates();

	/**
	 * 判断此用户的身份证是否已被注册
	 * @param userId
	 * @return
	 */
	int getUserByIdCard(String userId);

	/**
	 * 通过认证
	 * @param parameters
	 */
	void pass(Map<String, Object> parameters);

	/**
	 * 认证失败
	 * @param parameters
	 */
	void refuse(Map<String, Object> parameters);

	/**
	 * 查询用户的实名认证状态
	 * @param userId
	 * @return
	 */
	String getVerifyStateById(String userId);

}
