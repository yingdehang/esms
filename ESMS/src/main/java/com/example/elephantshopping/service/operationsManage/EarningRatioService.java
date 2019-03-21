package com.example.elephantshopping.service.operationsManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.EarningRatioDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 收益比例管理Service
 * @author XB
 *
 */
@Service
public class EarningRatioService
{
	@Autowired
	private EarningRatioDao earningRatioDao;

	/**
	 * 未分页的收益比例数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getEarningRatio(Map<String, Object> parameters)
	{
		return earningRatioDao.getEarningRatio(parameters);
	}

	/**
	 * 已分页的收益比例数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getEarningRatioPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> list = earningRatioDao.getEarningRatioPage(parameters);
		DateFormatUtils.timeforDateType(list,"ADD_TIME");
		return list;
	}

	/**
	 * 删除
	 * @param id
	 */
	public void delete(String id)
	{
		earningRatioDao.delete(id);
	}

	/**
	 * 修改
	 * @param parameters
	 */
	public void edit(Map<String, Object> parameters)
	{
		earningRatioDao.edit(parameters);
	}

	/**
	 * 添加
	 * @param parameters
	 */
	public void add(Map<String, Object> parameters) 
	{
		earningRatioDao.add(parameters);
	}
}
