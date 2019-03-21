package com.example.elephantshopping.dao.goodsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性Dao
 * @author XB
 *
 */
@Mapper
public interface AttributeDao
{
	/**
	 * 未分页的属性数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getAttributeList(Map<String, Object> parameters);

	/**
	 * 已分页的属性数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getAttributeListPage(Map<String, Object> parameters);

	/**
	 * 删除属性
	 * @param attributeId
	 */
	void delete(String attributeId);

	/**
	 * 修改属性名
	 * @param parameters
	 */
	void editAttributeName(Map<String, Object> parameters);

	/**
	 * 修改属性值
	 * @param parameters
	 */
	void editAttributeValue(Map<String, Object> parameters);

}
