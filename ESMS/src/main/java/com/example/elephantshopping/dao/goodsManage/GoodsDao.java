package com.example.elephantshopping.dao.goodsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsDao {

	/**
	 * 分页数据
	 * 
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getGoodsList(Map<String, Object> parameters);

	/**
	 * 获取商品count
	 * @param map
	 * @return
	 */
	int getGoodsListCount(Map<String, Object> map);

	/**
	 * 修改商品狀態
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updategoodsState(Map requestToMap);

}
