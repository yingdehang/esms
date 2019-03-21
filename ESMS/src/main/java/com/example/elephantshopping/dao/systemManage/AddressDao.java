package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressDao {
	/**
	 * 获取地址菜单
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getAddressList(Map map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 * @return
	 */
	int getAddressListCount(Map map);

	/**
	 * 删除地址
	 * 
	 * @param Address_ID
	 */
	void deleteAddressById(String Address_ID);

	/**
	 * 查询父类的pid
	 * 
	 * @param pID
	 * @return
	 */
	int queryAddressParentId(int pID);

	/**
	 * 添加地址
	 * 
	 * @param requestToMap
	 */
	void addAddress(Map requestToMap);

	/**
	 * 修改地址
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateAddress(Map requestToMap);

	/**
	 * 查询地址是否有子类
	 * 
	 * @param object
	 * @return
	 */
	int isHaveSon(Object object);
}
