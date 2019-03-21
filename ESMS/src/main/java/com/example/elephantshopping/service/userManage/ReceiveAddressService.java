package com.example.elephantshopping.service.userManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.userManage.ReceiveAddressDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 收货地址管理Service
 * @author XB
 *
 */
@Service
public class ReceiveAddressService 
{
	@Autowired
	private ReceiveAddressDao receiveAddressDao;

	/**
	 * 获取/查询收货地址list
	 * @return
	 */
	public List<Map<String, Object>> getReceiveAddressList(Map<String,Object> parameters)
	{
		return receiveAddressDao.getReceiveAddressList(parameters);
	}

	/**
	 * 获取/查询收货地址list并分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getReceiveAddressListPage(Map<String, Object> parameters) 
	{
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> receiveAddressListPage = receiveAddressDao.getReceiveAddressListPage(parameters);
		return DateFormatUtils.transforDateType(receiveAddressListPage);
	}
}
