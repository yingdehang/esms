package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.StoreClassDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 店铺管理Service
 * @author XB
 *
 */
@Service
public class StoreClassService
{
	@Autowired
	private StoreClassDao storeClassDao;
	
	/**
	 * 未分页的店铺分类数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreClassList(Map<String, Object> parameters) 
	{
		return storeClassDao.getStoreClassList(parameters);
	}

	/**
	 * 已分页的店铺分类数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreClassListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> storeClassListPage = storeClassDao.getStoreClassListPage(parameters);
		storeClassListPage = DateFormatUtils.transforDateType(storeClassListPage);
		return storeClassListPage;
	}

	/**
	 * 修改店铺分类名称
	 * @param parameters
	 */
	public void edit(Map<String, Object> parameters)
	{
		storeClassDao.edit(parameters);
	}

	/**
	 * 添加店铺分类
	 * @param className
	 */
	public void addClass(String className) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("className", className);
		map.put("storeClassId", UUID.randomUUID().toString().replace("-", ""));
		storeClassDao.addClass(map);
	}

	/**
	 * 删除店铺分类
	 * @param storeClassId
	 */
	public void delete(String storeClassId)
	{
		storeClassDao.delete(storeClassId);
	}

	/**
	 * 查询此店铺分类是否关联店铺
	 * @param storeClassId
	 * @return
	 */
	public List<Map<String, Object>> getStoreByClass(String storeClassId) 
	{
		return storeClassDao.getStoreByClass(storeClassId);
	}

}
