package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.StoreVerifyDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 店铺认证管理Service
 * @author XB
 *
 */
@Service
public class StoreVerifyService 
{
	@Autowired
	private StoreVerifyDao storeVerifyDao;

	/**
	 * 获取店铺等级
	 * @return
	 */
	public List<Map<String, Object>> getStoreGradeList() 
	{
		return storeVerifyDao.getStoreGradeList();
	}
	
	/**
	 * 获取店铺类型
	 * @return
	 */
	public List<Map<String, Object>> getStoreTypeList() 
	{
		return storeVerifyDao.getStoreTypeList();
	}
	
	/**
	 * 获取类型(线上/线下)
	 * @return
	 */
	public List<Map<String, Object>> getTypeList() 
	{
		return storeVerifyDao.getTypeList();
	}
	
	/**
	 * 未分页的店铺认证数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreVerifyList(Map<String, Object> parameters) 
	{
		return storeVerifyDao.getStoreVerifyList(parameters);
	}

	/**
	 * 已分页的店铺认证数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getStoreVerifyListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> storeListPage = storeVerifyDao.getStoreVerifyListPage(parameters);
		storeListPage = DateFormatUtils.transforDateType(storeListPage);
		return storeListPage;
	}

	/**
	 * 进行店铺认证
	 * @param parameters
	 */
	public void toVerify(Map<String, Object> parameters) 
	{
		storeVerifyDao.toVerify(parameters);
	}

	/**
	 * 获取店铺认证状态
	 * @return
	 */
	public List<Map<String, Object>> getVerifyStates()
	{
		return storeVerifyDao.getVerifyStates();
	}

	/**
	 * 获取某个店铺的信息
	 * @param storeVerifyId
	 * @return
	 */
	public Map<String, Object> getStoreVerifyInfo(String storeVerifyId)
	{
		return storeVerifyDao.getStoreVerifyInfo(storeVerifyId);
	}

	/**
	 * 修改店铺认证基本信息
	 * @param parameters
	 */
	public void editStoreVerify(Map<String, Object> parameters)
	{
		storeVerifyDao.editStoreVerify(parameters);
	}

	/**
	 * 获取店铺认证的图片信息
	 * @param storeVerifyId
	 * @return
	 */
	public Map<String, Object> getStoreVerifyPhotoInfo(String storeVerifyId)
	{
		return storeVerifyDao.getStoreVerifyPhotoInfo(storeVerifyId);
	}

	/**
	 * 将身份证Id保存到店铺认证表里面
	 * @param storeVerifyId
	 * @param photoId
	 */
	public void updateIdCard(String storeVerifyId, String photoId)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeVerifyId", storeVerifyId);
		map.put("photoId", photoId);
		storeVerifyDao.updateIdCard(map);
	}

	/**
	 * 将门头照片Id保存到店铺认证表里面
	 * @param storeVerifyId
	 * @param photoId
	 */
	public void updateHeadPhoto(String storeVerifyId, String photoId)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeVerifyId", storeVerifyId);
		map.put("photoId", photoId);
		storeVerifyDao.updateHeadPhoto(map);
	}

	/**
	 * 将营业执照Id保存到店铺认证表里面
	 * @param storeVerifyId
	 * @param photoId
	 */
	public void updateLicense(String storeVerifyId, String photoId) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeVerifyId", storeVerifyId);
		map.put("photoId", photoId);
		storeVerifyDao.updateLicense(map);
	}

	/**
	 * 判断此店铺ID对应的认证信息是否存在
	 * @param storeId
	 * @return
	 */
	public boolean storeVerifyIsExists(String storeId)
	{
		List<Map<String,Object>> list = storeVerifyDao.getStoreVerifyById(storeId);
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
	 * 添加店铺认证基本信息
	 * @param parameters
	 */
	public void addStoreVerify(Map<String, Object> parameters)
	{
		storeVerifyDao.addStoreVerify(parameters);
	}

	/**
	 * 判断此ID对应的店铺是否存在
	 * @param storeId
	 * @return
	 */
	public boolean storeIsExists(String storeId) 
	{
		List<Map<String,Object>> list = storeVerifyDao.storeIsExists(storeId);
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
	 * 根据pId获得所有的下级地区
	 * @return
	 */
	public List<Map<String, Object>> getSubordinateArea(int pId) 
	{
		return storeVerifyDao.getSubordinateArea(pId);
	}

}
