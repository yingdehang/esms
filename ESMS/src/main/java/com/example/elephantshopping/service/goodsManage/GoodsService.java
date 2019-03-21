package com.example.elephantshopping.service.goodsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.elephantshopping.dao.goodsManage.GoodsDao;

/**
 * 
 * 商品管理
 */
@Service
public class GoodsService {
	@Autowired
	private GoodsDao goodsDao;

	/**
	 * 查询商品list
	 * 
	 * @param parameters
	 * @return
	 */
	public Map<String, Object> getGoodsList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String,Object>> list=goodsDao.getGoodsList(map);
		int count=goodsDao.getGoodsListCount(map);
		Map<String, Object> mp=new HashMap<String,Object>();
		mp.put("msg", "");
		mp.put("data", list);
		mp.put("code", 0);
		mp.put("count", count);
		return mp;
	}

	/**
	 * 修改商品状态
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updategoodsState(Map requestToMap) {
		return goodsDao.updategoodsState(requestToMap);
	}

}
