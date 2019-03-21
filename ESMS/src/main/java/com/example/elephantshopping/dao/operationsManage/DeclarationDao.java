package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 保单审核Dao
 * @author XB
 *
 */
@Mapper
public interface DeclarationDao 
{
	/**
	 * 报单申请数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getDeclaration(Map<String, Object> parameters);

	/**
	 * 报单申请数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getDeclarationPage(Map<String, Object> parameters);

	/**
	 * 获取报单审核详情
	 * @param id
	 * @return
	 */
	Map<String, Object> getDeclarationInfo(String id);

	/**
	 * 审核通过
	 * @param parameters
	 */
	void pass(Map<String, Object> parameters);

	/**
	 * 审核失败
	 * @param parameters
	 */
	void refuse(Map<String, Object> parameters);

	/**
	 * 报单明细数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getDeclarationRecord(Map<String, Object> parameters);

	/**
	 * 报单明细数据分页
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getDeclarationRecordPage(Map<String, Object> parameters);

	/**
	 * 根据Id查出某个系数的值
	 * @param eRId
	 * @return
	 */
	double getCoefficientValue(String ERId);

	/**
	 * 查出某个店铺的等级
	 * @param storeCertificationId
	 * @return
	 */
	String getStoreLevel(String storeCertificationId);

	/**
	 * 获得用户IS_ZZ字段判断是不是站长
	 * @param customerId
	 * @return
	 */
	String isZZ(String userId);

	/**
	 * 清除用户邀请人
	 * @param customerId
	 */
	void clearInvitation(String customerId);

	/**
	 * 得到买家的邀请人信息
	 * @param customerId
	 * @return
	 */
	Map<String, Object> getInviationId(String customerId);

	/**
	 * 得到用户所在区域站长
	 * @param userId
	 * @return
	 */
	Map<String, Object> getUserZZ(String userId);

	/**
	 * 查询报单状态
	 * @param id
	 * @return
	 */
	String getDeclarationState(String id);
}
