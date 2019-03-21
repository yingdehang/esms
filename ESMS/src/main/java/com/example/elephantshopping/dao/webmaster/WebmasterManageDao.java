package com.example.elephantshopping.dao.webmaster;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WebmasterManageDao {
	/**
	 * 查询地区
	 * 
	 * @param pid
	 * @return
	 */
	List<Map<String, Object>> queryAreaList(String pid);

	/**
	 * 查询站长list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getWebmasterList(Map<String, Object> map);

	/**
	 * 查询站长list的count
	 * 
	 * @param map
	 * @return
	 */
	int getWebmasterListCount(Map<String, Object> map);

	/**
	 * 撤销站长
	 * 
	 * @param requestToMap
	 */
	int updateISZZ(Map requestToMap);

	/**
	 * 修改站长账户余额
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateWebmasterYue(Map requestToMap);

	/**
	 * 插入站长流水
	 * 
	 * @param requestToMap
	 */
	void insetWebmasterWater(Map requestToMap);

	/**
	 * 查询站长结算记录
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> masterSettlementRecordList(Map map);

	/**
	 * 站长结算记录总条数
	 * 
	 * @param map
	 * @return
	 */
	int getmasterSettlementRecordListCount(Map map);

	/**
	 * 查询该区域是否存在站长
	 * 
	 * @param areaId
	 * @return
	 */
	int VerifyThatThereIs(String areaId);

	/**
	 * 查询手机号是否已注册
	 * 
	 * @param userPhone
	 * @return
	 */
	int isHavePhone(String userPhone);

	/**
	 * 是否实名认证
	 * 
	 * @param userPhone
	 * @return
	 */
	int readyshimingrenzheng(String userPhone);

	/**
	 * 查询是否为站长
	 * 
	 * @param userPhone
	 * @return
	 */
	int isZhanzhanga(String userPhone);

	/**
	 * 添加站长
	 * 
	 * @param userphone
	 * @param area
	 * @return
	 */
	int addWebmaster(Map<String, Object> map);

	/**
	 * 查询角色id
	 * 
	 * @param userphone
	 * @return
	 */
	String getUserIdByPhone(String userphone);

	/**
	 * 添加站長角色
	 * 
	 * @param map
	 */
	void addWebmasterRole(Map<String, Object> map);

	/**
	 * 删除该用户站长角色
	 * 
	 * @param requestToMap
	 */
	void deleteZZRole(Map requestToMap);

}
