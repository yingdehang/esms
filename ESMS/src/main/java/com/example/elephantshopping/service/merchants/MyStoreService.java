package com.example.elephantshopping.service.merchants;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.merchants.MyStoreDao;

/**
 * 我的店铺Service
 * @author XB
 *
 */
@Service
public class MyStoreService 
{
	@Autowired
	private MyStoreDao myStoreDao;

	/**
	 * 根据用户Id获得店铺信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getUserStore(String userId) 
	{
		return myStoreDao.getUserStore(userId);
	}

	/**
	 * 查询待处理的订单
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getPendingOrder(String userId)
	{
		return myStoreDao.getPendingOrder(userId);
	}

	/**
	 * 查询所有的店铺状态
	 * @return
	 */
	public List<Map<String, Object>> getStoreState()
	{
		return myStoreDao.getStoreState();
	}

	/**
	 * 查询店铺的所有消息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getMessage(String userId) 
	{
		return myStoreDao.getMessage(userId);
	}

	/**
	 * 查询店铺认证信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getStoreCertification(String storeId)
	{
		return myStoreDao.getStoreCertification(storeId);
	}

	/**
	 * 改变店铺营业状态
	 * @param parameters
	 */
	public void changeState(Map<String, Object> parameters)
	{
		myStoreDao.changeState(parameters);
	}

	/**
	 * 改变店铺客服电话
	 * @param parameters
	 */
	public void changeServicePhone(Map<String, Object> parameters)
	{
		myStoreDao.changeServicePhone(parameters);
	}
}
