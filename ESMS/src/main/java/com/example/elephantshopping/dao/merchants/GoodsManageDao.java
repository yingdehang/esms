package com.example.elephantshopping.dao.merchants;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsManageDao {
	/**
	 * 查询商品list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getgoodsList(Map map);

	/**
	 * 查询商品count
	 * 
	 * @param map
	 * @return
	 */
	int getgoodsListCount(Map map);

	/**
	 * 修改商品状态
	 * 
	 * @param map
	 * @return
	 */
	int updateGoodsState(Map<String, Object> map);

	/**
	 * 删除商品id
	 * 
	 * @param gOODSID
	 */
	void DeleteGoodsById(String gOODSID);

	/**
	 * 修改商品库存
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateGoodsInventory(Map requestToMap);

	/**
	 * 修改商品价格
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateGoodsPRICE(Map requestToMap);

	/**
	 * 查询商品表信息
	 * 
	 * @param goodsId
	 * @return
	 */
	Map<String, Object> queryGoodsInfo(String goodsId);

	/**
	 * 查询商品图片
	 * 
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> queryGoodsImg(String goodsId);

	/**
	 * 查询商品分类信息
	 * 
	 * @param classificationId
	 * @return
	 */
	Map<String, Object> getGoodsClassification(String pid);

	/**
	 * 获取商品属性列表
	 * 
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> getGoodsAttribute(String goodsId);

	/**
	 * 获取商品属性的值
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAttributeValue(Map<String, Object> map);

	/**
	 * 修改商品排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateGoodsSort(Map requestToMap);

	/**
	 * 查询商品分类菜单
	 * 
	 * @param pid
	 * @return
	 */
	List<Map<String, Object>> getClassifications(String pid);

	/**
	 * 查询商品所以属性
	 * 
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> getGoodsAllAttribute(String goodsId);

	/**
	 * 添加商品属性
	 * 
	 * @param map
	 */
	void addAttribute(Map<String, Object> map);

	/**
	 * 删除商品属性
	 * 
	 * @param attributeId
	 */
	void deleteAttribute(String attributeId);

	/**
	 * 通过商品id删除商品属性
	 * 
	 * @param gOODSID
	 */
	void deleteAttributeByGoodsId(String gOODSID);

	/**
	 * 删除商品图片
	 * 
	 * @param gOODSID
	 */
	void deletePhoto(String gOODSID);

	/**
	 * 添加商品
	 * 
	 * @param map
	 */
	void addgoods(Map map);

	/**
	 * 查询用户商店id
	 * 
	 * @param userId
	 * @return
	 */
	String getUserSTOREID(String userId);

	/**
	 * 添加商品图片
	 * 
	 * @param m
	 */
	void addgoodsPhoto(Map<String, Object> m);

	/**
	 * 删除商品图片
	 * 
	 * @param pHOTO_ID
	 */
	void deletephoto(String pHOTO_ID);

	/**
	 * 删除店家删除的旧图片
	 * 
	 * @param photo
	 */
	void deleteupdatePhoto(Map<String, Object> photo);

	/**
	 * 查询除该店铺所以id的list;
	 * 
	 * @param storeId
	 * @return
	 */
	List<String> getgoodsIdList(String storeId);

	/**
	 * 查询该店铺商品分类
	 * 
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getStoreClassifications(String storeId);

	/**
	 * 查询一级分类
	 * 
	 * @param classificat
	 * @return
	 */
	List<Map<String, Object>> getOneClassifications(Map<String, Object> classificat);

	/**
	 * 查询二级分类
	 * 
	 * @param map1
	 * @return
	 */
	List<Map<String, Object>> getTwoClassifications(Map<String, Object> map1);

	/**
	 * 修改商品销量
	 * 
	 * @param map
	 * @return
	 */
	int updategoodsXiaoliang(Map<String, Object> map);

	/**
	 * 修改店鋪月銷量
	 * 
	 * @param map
	 * @return
	 */
	int updateStoreXiaoliang(Map<String, Object> map);

	/**
	 * 查询店铺信息
	 * 
	 * @param storeId
	 * @return
	 */
	Map<String, Object> queryStoreById(String storeId);

	/**
	 * 修改商品信息
	 * 
	 * @param map
	 */
	void updategoods(Map<String, Object> map);
}
