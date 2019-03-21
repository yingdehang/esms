package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺认证管理Dao
 * @author XB
 *
 */
@Mapper
public interface StoreVerifyDao
{
	/**
	 * 获取店铺等级
	 * @return
	 */
	List<Map<String, Object>> getStoreGradeList();

	/**
	 * 获取店铺类型
	 * @return
	 */
	List<Map<String, Object>> getStoreTypeList();
	
	/**
	 * 获取类型(线上/线下)
	 * @return
	 */
	List<Map<String, Object>> getTypeList();
	
	/**
	 * 未分页的店铺认证数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreVerifyList(Map<String, Object> parameters);

	/**
	 * 已分页的店铺认证数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getStoreVerifyListPage(Map<String, Object> parameters);

	/**
	 * 进行店铺认证
	 * @param parameters
	 */
	void toVerify(Map<String, Object> parameters);

	/**
	 * 获取店铺认证状态
	 * @return
	 */
	List<Map<String, Object>> getVerifyStates();

	/**
	 * 获取某个店铺的信息
	 * @param storeVerifyId
	 * @return
	 */
	Map<String, Object> getStoreVerifyInfo(String storeVerifyId);

	/**
	 * 修改店铺认证基本信息
	 * @param parameters
	 */
	void editStoreVerify(Map<String, Object> parameters);

	/**
	 * 获取店铺认证的图片信息
	 * @param storeVerifyId
	 * @return
	 */
	Map<String, Object> getStoreVerifyPhotoInfo(String storeVerifyId);

	/**
	 * 将身份证Id保存到店铺认证表里面
	 * @param map
	 */
	void updateIdCard(Map<String, Object> map);

	/**
	 * 将门头照片Id保存到店铺认证表里面
	 * @param map
	 */
	void updateHeadPhoto(Map<String, Object> map);

	/**
	 * 将营业执照Id保存到店铺认证表里面
	 * @param map
	 */
	void updateLicense(Map<String, Object> map);

	/**
	 * 根据ID获取店铺认证信息
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> getStoreVerifyById(String storeId);

	/**
	 * 添加店铺认证基本信息
	 * @param parameters
	 */
	void addStoreVerify(Map<String, Object> parameters);

	/**
	 * 判断此ID对应的店铺是否存在
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> storeIsExists(String storeId);

	/**
	 * 根据pId获得所有的下级地区
	 * @return
	 */
	List<Map<String, Object>> getSubordinateArea(int pId);

}
