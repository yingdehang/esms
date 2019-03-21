package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.StoreDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 店铺管理Service
 * @author XB
 *
 */
@Service
public class StoreService
{
	@Autowired
	private StoreDao storeDao;
	
	/**
	 * 未分页的店铺数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreList(Map<String, Object> parameters) 
	{
		return storeDao.getStoreList(parameters);
	}

	/**
	 * 已分页的店铺数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> storeListPage = storeDao.getStoreListPage(parameters);
		storeListPage = DateFormatUtils.transforDateType(storeListPage);
		return storeListPage;
	}

	/**
	 * 获取店铺状态列表
	 * @return
	 */
	public List<Map<String, Object>> getStoreStatesList() 
	{
		return storeDao.getStoreStatesList();
	}

	/**
	 * 获取店铺分类
	 * @return
	 */
	public List<Map<String, Object>> getStoreClassList()
	{
		return storeDao.getStoreClassList();
	}

	/**
	 * 判断店铺名是否存在
	 * @param parameters
	 * @return
	 */
	public boolean storeNameIsExists(Map<String, Object> parameters)
	{
		List<Map<String,Object>> list = storeDao.getStoreByName(parameters);
		if(list.size()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 保存店铺基本信息
	 * @param parameters
	 */
	public void addStore(Map<String, Object> parameters)
	{
		storeDao.addStore(parameters);
	}

	/**
	 * 保存图片信息
	 * @param iconId
	 * @param fileName
	 * @param string
	 */
	public void addPhoto(String iconId, String fileName, String path)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("iconId", iconId);
		map.put("fileName", fileName);
		map.put("path", path);
		storeDao.addPhoto(map);
	}

	/**
	 * 将图片Id保存到店铺表里面
	 * @param storeId
	 * @param iconId
	 */
	public void addIconId(String storeId, String iconId)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("iconId", iconId);
		map.put("storeId", storeId);
		storeDao.addIconId(map);
	}

	/**
	 * 获取某个店铺的信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getStoreInfo(String storeId)
	{
		return storeDao.getStoreInfo(storeId);
	}

	/**
	 * 修改店铺基本信息
	 * @param parameters
	 */
	public void editStore(Map<String, Object> parameters)
	{
		storeDao.editStore(parameters);
	}

	/**
	 * 删除原来表中的图片信息
	 * @param iId
	 */
	public void deletIcon(String iId)
	{
		storeDao.deletIcon(iId);
	}

	/**
	 * 修改店铺的图标Id
	 * @param storeId
	 * @param iconId
	 */
	public void updateIconId(String storeId, String iconId) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("iconId", iconId);
		map.put("storeId", storeId);
		storeDao.updateIconId(map);
	}


}
