package com.example.elephantshopping.dao.goodsManage;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品分类dao
 * 
 * @author Asus
 *
 */
@Mapper
public interface ClassificationDao {
	/**
	 * 获取商品分类list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getClassIficationList(Map map);

	/**
	 * 获取商品分类count
	 * 
	 * @param map
	 * @return
	 */
	int getClassIficationListCount(Map map);

	/**
	 * 查询该商品类是否有子类
	 * 
	 * @param object
	 * @return
	 */
	int isHaveSon(String CLASSIFICATION_ID);

	/**
	 * 查询父id的pid
	 * 
	 * @param pID
	 * @return
	 */
	String queryclassificationParentId(String pID);

	/**
	 * 添加
	 * 
	 * @param map
	 */
	void addClassification(Map map);

	/**
	 * 删除
	 * 
	 * @param cLASSIFICATION_ID
	 */
	void deleteclassificationById(String cLASSIFICATION_ID);

	/**
	 * 修改
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateClassification(Map requestToMap);

	/**
	 * 查询是否还有商品在该类别下
	 * 
	 * @param cLASSIFICATION_ID
	 * @return
	 */
	int queryisHaveGoods(String cLASSIFICATION_ID);

}
