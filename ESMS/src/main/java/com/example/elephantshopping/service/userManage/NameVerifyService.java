package com.example.elephantshopping.service.userManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.userManage.NameVerifyDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 实名认证管理Service
 * @author XB
 *
 */
@Service
public class NameVerifyService
{
	@Autowired
	private NameVerifyDao nameVerifyDao;

	/**
	 * 查询实名认证
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> searchNameVerify(Map<String, Object> parameters)
	{
		return nameVerifyDao.searchNameVerify(parameters);
	}

	/**
	 * 查询实名认证并分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> searchNameVerifyPage(Map<String, Object> parameters)
	{
		List<Map<String, Object>> nameVerifyListPage = nameVerifyDao.searchNameVerifyPage(parameters);
		DateFormatUtils.timeforDateType(nameVerifyListPage, "TO_APPLY_FOR_TIME");
		DateFormatUtils.timeforDateType(nameVerifyListPage, "REGISTERED_TIME");
		return nameVerifyListPage;
	}

	/**
	 * 获取实名认证数据
	 * @return
	 */
	public List<Map<String, Object>> getNameVerifyList()
	{
		return nameVerifyDao.getNameVerifyList();
	}

	/**
	 * 获取实名认证数据并分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getNameVerifyListPage(Map<String, Object> parameters)
	{
		List<Map<String, Object>> nameVerifyListPage = nameVerifyDao.getNameVerifyListPage(parameters);
		return DateFormatUtils.transforDateType(nameVerifyListPage);
	}

	/**
	 * 获取实名认证状态
	 * @return
	 */
	public List<Map<String,Object>> getVerifyStates() 
	{
		return nameVerifyDao.getVerifyStates();
	}

	/**
	 * 获取所有实名认证状态
	 * @return
	 */
	public List<Map<String,Object>> getAllVerifyStates() 
	{
		return nameVerifyDao.getAllVerifyStates();
	}

	/**
	 * 判断此用户的身份证是否已被注册
	 * @param userId
	 * @return
	 */
	public int getUserByIdCard(String userId) 
	{
		return nameVerifyDao.getUserByIdCard(userId);
	}

	/**
	 * 通过认证
	 * @param parameters
	 */
	public void pass(Map<String, Object> parameters) 
	{
		nameVerifyDao.pass(parameters);
	}

	/**
	 * 认证失败
	 * @param parameters
	 */
	public void refuse(Map<String, Object> parameters) 
	{
		nameVerifyDao.refuse(parameters);
	}

	/**
	 * 查询用户的实名认证状态
	 * @param userId
	 * @return
	 */
	public String getVerifyStateById(String userId) {
		return nameVerifyDao.getVerifyStateById(userId);
	}
}
