package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 线上店铺管理Dao
 * @author XB
 *
 */
@Mapper
public interface OnLineStoreDao
{
	/**
	 * 线上店铺数据未分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreList(Map<String, Object> parameters);

	/**
	 * 线上店铺数据已分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreListPage(Map<String, Object> parameters);

	/**
	 * 改变店铺状态（冻结/解冻）
	 * @param parameters
	 */
	void changeState(Map<String, Object> parameters);

	/**
	 * 线上店铺申请未分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreApply(Map<String, Object> parameters);

	/**
	 * 线上店铺申请已分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreApplyPage(Map<String, Object> parameters);

	/**
	 * 查询店铺认证信息
	 * @return
	 */
	Map<String, Object> getVerifyInfo(String storeId);

	/**
	 * 查询店铺类型
	 * @return
	 */
	List<Map<String, Object>> getStoreType();

	/**
	 * 查询店铺等级
	 * @return
	 */
	List<Map<String, Object>> getStoreLevel();

	/**
	 * 查询店铺分类
	 * @param pId
	 * @return
	 */
	List<Map<String, Object>> getStoreClass(String pId);

	/**
	 * 查询店铺已选分类
	 * @param storeId
	 * @return
	 */
	String getOldClass(String storeId);

	/**
	 * 根据分类Id查询分类名
	 * @param classId
	 * @return
	 */
	String getClassName(String classId);

	/**
	 * 拒绝申请
	 * @param parameters
	 */
	void refuse(Map<String, Object> parameters);

	/**
	 * 改变店铺类型
	 * @param parameters
	 */
	void changeType(Map<String, Object> parameters);

	/**
	 * 删除店铺原来的分类
	 * @param storeId
	 */
	void deleteStoreClass(String storeId);

	/**
	 * 改变店铺分类
	 * @param parameters
	 */
	void changeClasses(Map<String, Object> parameters);

	/**
	 * onLineStoreDao
	 * @param id
	 */
	void pass(Map<String,Object> parameters);

	/**
	 * 改变店铺等级
	 * @param parameters
	 */
	void changeLevel(Map<String, Object> parameters);

	/**
	 * 改变店铺名字
	 * @param parameters
	 */
	void changeStoreName(Map<String, Object> parameters);

	/**
	 * 改变店铺服务电话
	 * @param parameters
	 */
	void changeServicePhone(Map<String, Object> parameters);

	/**
	 * 改变店铺图标
	 * @param parameters
	 */
	void changeStoreIcon(Map<String, Object> parameters);

	/**
	 * 得到店铺的图标
	 * @param storeId
	 * @return
	 */
	String getStoreIcon(String storeId);

	/**
	 * 添加开店时间
	 * @param storeId
	 */
	void addOpenTime(String storeId);

	/**
	 * 线上店铺审核记录未分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreVerifyRecord(Map<String, Object> parameters);

	/**
	 * 线上店铺审核记录已分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getOnLineStoreVerifyRecordPage(Map<String, Object> parameters);

	/**
	 * 根据手机号获取用户
	 * @param userPhone
	 * @return
	 */
	List<Map<String, Object>> getUserByPhone(String userPhone);

	/**
	 * 根据用户id获取用户实名认证信息
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserVerify(String userId);

	/**
	 * 根据用户手机号获取用户id
	 * @param userPhone
	 * @return
	 */
	String getUserIdByPhone(String userPhone);

	/**
	 * 添加店铺
	 * @param map
	 */
	void addStore(Map<String, Object> map);

	/**
	 * 添加店铺认证
	 * @param map
	 */
	void addStoreVerify(Map<String, Object> map);

	/**
	 * 添加店铺图标
	 * @param map
	 */
	void addStoreIcon(Map<String, Object> map);

	/**
	 * 添加店铺营业执照
	 * @param map
	 */
	void addLicense(Map<String, Object> map);

	/**
	 * 添加身份证正面照
	 * @param map
	 */
	void addIdCardUp(Map<String, Object> map);

	/**
	 * 添加身份证反面照
	 * @param map
	 */
	void addIdCardDown(Map<String, Object> map);

	/**
	 * 根据id获取用户的线上店铺
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserOnLineStore(String userId);

	/**
	 * 根据店铺名获取店铺
	 * @param storeName
	 * @return
	 */
	List<Map<String, Object>> getStoreByName(Map<String,Object> parameters);

	/**
	 * 删除店铺
	 * @param sId
	 */
	void deleteStore(String sId);

	/**
	 * 删除店铺认证
	 * @param scId
	 */
	void deleteStoreCertification(String scId);

	/**
	 * 判断店铺名是否重复
	 * @param storeName
	 * @return
	 */
	List<Map<String, Object>> getStoreByName2(String storeName);

	/**
	 * 改变店铺等级下月生效
	 * @param parameters
	 */
	void changeWaitingState(Map<String, Object> parameters);

	/**
	 * 得到某个店铺的等级
	 * @param id
	 * @return
	 */
	String getStoreLevelById(String id);

	/**
	 * 清空waitingstate的值
	 * @param id
	 */
	void clearWaitingState(String id);

	/**
	 * 改变此用户为系统用户
	 * @param userId
	 */
	void changeUserLevelSystem(String userId);

	/**
	 * 添加此用户角色为店家
	 * @param parameters
	 */
	void addUserMerchant(Map<String, Object> parameters);

	/**
	 * 判读用户角色表中此用户是否已是商家
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserMechant(String userId);

	/**
	 * 删除此用户的店家角色
	 * @param userId
	 */
	void deleteUserMerchant(String userId);

	/**
	 * 改变店铺是否自营
	 * @param parameters
	 */
	void changeIS(Map<String, Object> parameters);

	/**
	 * 查询店铺某月/日的APP支付收益
	 * @param parameters
	 * @return
	 */
	Map<String, Object> getStoreWater(Map<String, Object> parameters);

	/**
	 * 得到店铺开店时间
	 * @param storeId
	 * @return
	 */
	String getOpenTime(String storeId);

	/**
	 * 得到店铺的信息
	 * @param storeId
	 * @return
	 */
	Map<String, Object> getOnLineStoreInfoById(String storeId);

	/**
	 * 通过分类Id得到分类的信息
	 * @param classId
	 * @return
	 */
	Map<String, Object> getClassInfoById(String classId);

	/**
	 * 取出一个店铺中的某个分类的商品
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getGoodsByClassAndStore(Map<String, Object> parameters);

	/**
	 * 修改店铺的分类
	 * @param parameters
	 */
	void updateStoreClass(Map<String, Object> parameters);

	/**
	 * 查询此用户待审核的线上店铺申请
	 * @param userId
	 * @return
	 */
	List<Map<String, Object>> getUserOnLineStoreDSH(String userId);

	/**
	 * 修改店铺的排序
	 * @param parameters
	 */
	void changeSort(Map<String, Object> parameters);

}
