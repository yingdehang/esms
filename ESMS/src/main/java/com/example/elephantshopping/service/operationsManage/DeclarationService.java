package com.example.elephantshopping.service.operationsManage;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.DeclarationDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 保单审核Service
 * @author XB
 *
 */
@Service
public class DeclarationService 
{
	@Autowired
	private DeclarationDao declarationDao;

	/**
	 * 报单申请数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getDeclaration(Map<String, Object> parameters) 
	{
		return declarationDao.getDeclaration(parameters);
	}

	/**
	 * 报单申请数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getDeclarationPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> list = declarationDao.getDeclarationPage(parameters);
		DateFormatUtils.timeforDateType(list, "APPLY_TIME");
		return list;
	}

	/**
	 * 获取报单审核详情
	 * @param id
	 * @return
	 */
	public Map<String, Object> getDeclarationInfo(String id) 
	{
		return declarationDao.getDeclarationInfo(id);
	}

	/**
	 * 审核通过
	 * @param parameters
	 */
	public void pass(Map<String, Object> parameters)
	{
		declarationDao.pass(parameters);
	}

	/**
	 * 审核失败
	 * @param parameters
	 */
	public void refuse(Map<String, Object> parameters) 
	{
		declarationDao.refuse(parameters);
	}

	/**
	 * 报单明细数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getDeclarationRecord(Map<String, Object> parameters) 
	{
		return declarationDao.getDeclarationRecord(parameters);
	}

	/**
	 * 报单明细数据分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getDeclarationRecordPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> list = declarationDao.getDeclarationRecordPage(parameters);
		DateFormatUtils.timeforDateType(list, "PASS_TIME");
		return list;
	}
	
	/**
	 * 根据Id查出某个系数的值
	 * @param ERId
	 * @return
	 */
	public double getCoefficientValue(String ERId) 
	{
		return declarationDao.getCoefficientValue(ERId);
	}

	/**
	 * 查出某个店铺的店铺等级系数
	 * @param storeId
	 * @return
	 */
	public double getStoreGradeCoefficient(String storeCertificationId) 
	{
		String storeLevel = declarationDao.getStoreLevel(storeCertificationId);//查出某个店铺的等级
		//根据店铺等级不同查出其店铺等级系数
		if(storeLevel.equals("STORE_DJ_GOLD"))//金牌
		{
			return declarationDao.getCoefficientValue("d8260b02fb9346bc8130057fcd551efc");
		}
		else if(storeLevel.equals("STORE_DJ_SILVER"))//银牌
		{
			return declarationDao.getCoefficientValue("31371587a0b242f1867d43c0fb5eb39e");
		}
		else//铜牌
		{
			return declarationDao.getCoefficientValue("214f994af9a94b69bbd3f54a862647f7");
		}
	}

	/**
	 * 获得用户IS_ZZ字段判断是不是站长
	 * @param customerId
	 * @return
	 */
	public String isZZ(String userId) 
	{
		return declarationDao.isZZ(userId);
	}

	/**
	 * 清除用户邀请人
	 * @param customerId
	 */
	public void clearInvitation(String customerId)
	{
		declarationDao.clearInvitation(customerId);
	}

	/**
	 * 得到买家的邀请人信息
	 * @param customerId
	 * @return
	 */
	public Map<String, Object> getInviationId(String customerId) 
	{
		return declarationDao.getInviationId(customerId);
	}

	/**
	 * 得到用户所在区域站长
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getUserZZ(String userId) {
		return declarationDao.getUserZZ(userId);
	}
	
	/**
	 * 得到用户邀请人中的副站长的信息
	 * @param userId
	 * @return
	 */
	public Map<String,Object> getInvitationsFzz(String userId){
		Map<String,Object> fzz = null;
		//判断用户的邀请人是否存在
		Map<String,Object> invitation = getInviationId(userId);
		if(invitation!=null){//存在
			String invitationId = (String) invitation.get("USERS_ID");
			//判断用户邀请人是否是副站长
			if(invitation.get("IS_FZZ")!=null){//IS_FZZ字段不为空
				int isFzz = (int) invitation.get("IS_FZZ");
				if(isFzz==1){//如果是副站长
					return invitation;
				}else{//不是副站长
					fzz = getInvitationsFzz(invitationId);
				}
			}else{//IS_FZZ字段为空
				fzz = getInvitationsFzz(invitationId);
			}
		}else{//不存在
			return null;
		}
		return fzz;
	}

	/**
	 * 查询报单状态
	 * @param id
	 * @return
	 */
	public String getDeclarationState(String id) {
		return declarationDao.getDeclarationState(id);
	}
}
