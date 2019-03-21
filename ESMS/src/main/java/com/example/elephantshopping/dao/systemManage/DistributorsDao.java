package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DistributorsDao {
	/**
	 * 查询分析者
	 * 
	 * @param phone
	 * @return
	 */
	List<Map<String, Object>> queryDistributorsPhone(String phone);

	/**
	 * 验证手机号是否存在
	 * 
	 * @param phone
	 * @return
	 */
	int phoneishave(String phone);

	/**
	 * 修改邀請人
	 * 
	 * @return
	 */
	int updatedistributors(Map<String, Object> map);

}
