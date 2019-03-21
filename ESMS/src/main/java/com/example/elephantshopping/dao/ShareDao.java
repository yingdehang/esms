package com.example.elephantshopping.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShareDao {
	/**
	 * 查询商品评论
	 * 
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> queryCommentsOnGoods(String goodsId);

	/**
	 * 查询店铺信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> queryStoreInfo(String storeId);

}
