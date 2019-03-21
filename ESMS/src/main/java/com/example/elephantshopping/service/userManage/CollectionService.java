package com.example.elephantshopping.service.userManage;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.userManage.CollectionDao;

/**
 * 收藏管理Service
 * @author XB
 *
 */
@Service
public class CollectionService
{
	@Autowired
	private CollectionDao collectionDao;

	/**
	 * 获取未分页的收藏数据List
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getCollectionList() 
	{
		List<Map<String,Object>> collectionList = collectionDao.getCollectionList();
//		List<Map<String,Object>> finalList = getFinalList(collectionList);
//		return finalList;
		return collectionList;
	}
	
	/**
	 * 为传来的收藏信息集合加上收藏物品的名字并返回
	 * @param collectionList
	 * @return
	 */
	public List<Map<String, Object>> getFinalList(List<Map<String,Object>> collectionList) 
	{
		for(int i=0;i<collectionList.size();i++)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(collectionList.get(i).get("COLLECTION_TIME"));
			collectionList.get(i).put("COLLECTION_TIME", time);//转换一下时间格式，方便页面显示
			String name = "";
			String type=(String) collectionList.get(i).get("COLLECTION_TYPE");
			String id = (String) collectionList.get(i).get("ID");
			if(type.equals("商品"))
			{
				name = collectionDao.getGoodsName(id);//是商品则查询商品名字
			}
			else if(type.equals("店铺"))
			{
				name = collectionDao.getStoreName(id);//是店铺则查询店铺名字
			}
			collectionList.get(i).put("NAME", name);
		}
		return collectionList;
	}
	
	/**
	 * 获取收藏数据并分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getCollectionListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> collectionListPage = collectionDao.getCollectionListPage(parameters);
		List<Map<String,Object>> finalList = getFinalList(collectionListPage);
		return finalList;
	}

	/**
	 * 查询收藏
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> searchCollection(String phoneNumber)
	{
		List<Map<String,Object>> collectionList = collectionDao.searchCollection(phoneNumber);
//		List<Map<String,Object>> finalList = getFinalList(collectionList);
//		return finalList;
		return collectionList;
	}

	/**
	 * 查询收藏并分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> searchCollectionPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> collectionListPage = collectionDao.searchCollectionPage(parameters);
		List<Map<String,Object>> finalList = getFinalList(collectionListPage);
		return finalList;
	}
}
