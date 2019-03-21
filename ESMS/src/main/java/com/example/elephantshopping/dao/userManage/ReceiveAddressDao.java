package com.example.elephantshopping.dao.userManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址管理Dao
 * @author XB
 *
 */
@Mapper
public interface ReceiveAddressDao 
{
	/**
	 * 获取/查询收货地址list
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getReceiveAddressList(Map<String, Object> parameters);

	/**
	 * 获取/查询收货地址list并分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getReceiveAddressListPage(Map<String, Object> parameters);

}
