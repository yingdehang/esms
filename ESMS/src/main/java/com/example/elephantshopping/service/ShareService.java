package com.example.elephantshopping.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.ShareDao;

@Service
public class ShareService {
	@Autowired
	private ShareDao shareDao;

	/**
	 * 查询商品评论
	 * 
	 * @param goodsId
	 * @return
	 */
	public List<Map<String, Object>> queryCommentsOnGoods(String goodsId) {
		// TODO Auto-generated method stub
		return shareDao.queryCommentsOnGoods(goodsId);
	}

	/**
	 * 查询店铺信息
	 * 
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> queryStoreInfo(String storeId) {
		Map<String, Object> storeinfo = shareDao.queryStoreInfo(storeId);
		String storeName = storeinfo.get("storeName").toString();
		if (storeName.length() > 8) {
			String rename = storeName.substring(0, 7);
			storeinfo.put("storename", rename + "...");
		} else {
			storeinfo.put("storename", storeName);
		}
		return storeinfo;
	}
}
