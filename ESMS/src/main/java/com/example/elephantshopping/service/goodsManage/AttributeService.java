package com.example.elephantshopping.service.goodsManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.goodsManage.AttributeDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 商品属性Service
 * @author XB
 *
 */
@Service
public class AttributeService
{
	@Autowired
	private AttributeDao attributeDao;
	
	/**
	 * 未分页的属性数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getAttributeList(Map<String, Object> parameters) 
	{
		return attributeDao.getAttributeList(parameters);
	}

	/**
	 * 已分页的属性数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getAttributeListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> attributeListPage = attributeDao.getAttributeListPage(parameters);
		attributeListPage = DateFormatUtils.transforDateType(attributeListPage);
		return attributeListPage;
	}

	/**
	 * 删除属性
	 * @param attributeId
	 */
	@Transactional
	public void delete(String attributeId)
	{
		attributeDao.delete(attributeId);
	}

	/**
	 * 修改属性名
	 * @param parameters
	 */
	@Transactional
	public void editAttributeName(Map<String, Object> parameters)
	{
		attributeDao.editAttributeName(parameters);
	}

	/**
	 * 修改属性值
	 * @param parameters
	 */
	@Transactional
	public void editAttributeValue(Map<String, Object> parameters)
	{
		attributeDao.editAttributeValue(parameters);
	}
}
