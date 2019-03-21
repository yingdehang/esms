package com.example.elephantshopping.service.operationsManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.OnLineStoreDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 线上店铺管理Service
 * @author XB
 *
 */
@Service
public class OnLineStoreService
{
	@Autowired
	private OnLineStoreDao onLineStoreDao;

	/**
	 * 线上店铺数据未分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreList(Map<String, Object> parameters) 
	{
		return onLineStoreDao.getOnLineStoreList(parameters);
	}

	/**
	 * 线上店铺数据已分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> storeList = onLineStoreDao.getOnLineStoreListPage(parameters);
		DateFormatUtils.timeforDateType(storeList, "OPEN_SHOP_TIME");
		return storeList;
	}

	/**
	 * 改变店铺状态（冻结/解冻）
	 * @param parameters
	 */
	public void changeState(Map<String, Object> parameters)
	{
		onLineStoreDao.changeState(parameters);
	}

	/**
	 * 线上店铺申请未分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreApply(Map<String, Object> parameters)
	{
		return onLineStoreDao.getOnLineStoreApply(parameters);
	}

	/**
	 * 线上店铺申请已分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreApplyPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> storeList = onLineStoreDao.getOnLineStoreApplyPage(parameters);
		DateFormatUtils.timeforDateType(storeList, "TO_APPLY_FOR_TIME");
		return storeList;
	}

	/**
	 * 查询店铺认证信息
	 * @return
	 */
	public Map<String, Object> getVerifyInfo(String storeId) 
	{
		return onLineStoreDao.getVerifyInfo(storeId);
	}

	/**
	 * 查询店铺类型
	 * @return
	 */
	public List<Map<String, Object>> getStoreType()
	{
		return onLineStoreDao.getStoreType();
	}

	/**
	 * 查询店铺等级
	 * @return
	 */
	public List<Map<String, Object>> getStoreLevel() 
	{
		return onLineStoreDao.getStoreLevel();
	}

	/**
	 * 查询店铺分类
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> getStoreClass(String pId) 
	{
		return onLineStoreDao.getStoreClass(pId);
	}

	/**
	 * 查询店铺已选分类
	 * @param storeId
	 * @return
	 */
	public String getOldClassName(String storeId) 
	{
		String oldClass = "";
		String storeClass = onLineStoreDao.getOldClass(storeId);//查询店铺已选分类
		if(storeClass!=null)
		{
			String[] classes = storeClass.split(",");
			for(int i=0;i<classes.length;i++)
			{
				String className = onLineStoreDao.getClassName(classes[i]);//根据分类Id查询分类名
				oldClass+=className;
				if(i!=classes.length-1)
				{
					oldClass+=",";
				}
			}
		}
		return oldClass;
	}

	/**
	 * 拒绝申请
	 * @param id
	 */
	public void refuse(String id,String content,String userId) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id",id);
		parameters.put("content", content);
		parameters.put("userId", userId);
		onLineStoreDao.refuse(parameters);
	}

	/**
	 * 改变店铺类型
	 * @param type
	 * @param id
	 */
	public void changeType(String type, String id)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("id", id);
		onLineStoreDao.changeType(parameters);
	}

	/**
	 * 删除店铺原来的分类
	 * @param storeId
	 */
	public void deleteStoreClass(String storeId)
	{
		onLineStoreDao.deleteStoreClass(storeId);
	}

	/**
	 * 改变店铺分类
	 * @param parameters
	 */
	public void changeClasses(Map<String, Object> parameters) 
	{
		onLineStoreDao.changeClasses(parameters);
	}

	/**
	 * 通过申请
	 * @param id
	 */
	public void pass(String id,String storeId,String userId)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("id", id);
		parameters.put("userId", userId);
		onLineStoreDao.pass(parameters);
		onLineStoreDao.addOpenTime(storeId);//添加开店时间
	}

	/**
	 * 改变店铺等级
	 * @param parameters
	 */
	public void changeLevel(Map<String, Object> parameters)
	{
		onLineStoreDao.changeLevel(parameters);
	}

	/**
	 * 改变店铺名字
	 * @param parameters
	 */
	public void changeStoreName(Map<String, Object> parameters)
	{
		onLineStoreDao.changeStoreName(parameters);
	}

	/**
	 * 改变店铺服务电话
	 * @param parameters
	 */
	public void changeServicePhone(Map<String, Object> parameters) 
	{
		onLineStoreDao.changeServicePhone(parameters);
	}

	/**
	 * 改变店铺图标
	 * @param url
	 * @param storeId
	 */
	public void changeStoreIcon(String url, String storeId) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("url", url);
		parameters.put("storeId",storeId);
		onLineStoreDao.changeStoreIcon(parameters);
	}

	/**
	 * 得到店铺的图标
	 * @param storeId
	 * @return
	 */
	public String getStoreIcon(String storeId) 
	{
		return onLineStoreDao.getStoreIcon(storeId);
	}

	/**
	 * 线上店铺审核记录未分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreVerifyRecord(Map<String, Object> parameters) 
	{
		return onLineStoreDao.getOnLineStoreVerifyRecord(parameters);
	}

	/**
	 * 线上店铺审核记录已分页
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStoreVerifyRecordPage(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> storeList = onLineStoreDao.getOnLineStoreVerifyRecordPage(parameters);
		DateFormatUtils.timeforDateType(storeList, "CTIME");
		return storeList;
	}

	/**
	 * 根据手机号获取用户
	 * @param userPhone
	 * @return
	 */
	public List<Map<String, Object>> getUserByPhone(String userPhone) 
	{
		return onLineStoreDao.getUserByPhone(userPhone);
	}

	/**
	 * 根据用户id获取用户实名认证信息
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserVerify(String userId) 
	{
		return onLineStoreDao.getUserVerify(userId);
	}

	/**
	 * 根据用户手机号获取用户id
	 * @param userPhone
	 * @return
	 */
	public String getUserIdByPhone(String userPhone) 
	{
		return onLineStoreDao.getUserIdByPhone(userPhone);
	}

	/**
	 * 添加店铺
	 * @param storeId
	 * @param userId
	 * @param classes
	 * @param storeName
	 * @param servicePhone
	 */
	public void addStore(String storeId, String userId, String classes, String storeName, String servicePhone) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeId", storeId);
		map.put("userId", userId);
		map.put("classes", classes);
		map.put("storeName", storeName);
		map.put("servicePhone", servicePhone);
		onLineStoreDao.addStore(map);
	}

	/**
	 * 添加店铺认证
	 * @param verifyId
	 * @param storeId
	 * @param storeType
	 * @param storeLevel
	 * @param is
	 */
	public void addStoreVerify(String verifyId, String storeId, String storeType, String storeLevel, String is) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("verifyId", verifyId);
		map.put("storeId", storeId);
		map.put("storeType", storeType);
		map.put("storeLevel", storeLevel);
		map.put("is", is);
		onLineStoreDao.addStoreVerify(map);
	}

	/**
	 * 添加店铺图标
	 * @param storeId
	 * @param storeIconUrl
	 */
	public void addStoreIcon(String storeId, String storeIconUrl) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeId", storeId);
		map.put("storeIconUrl", storeIconUrl);
		onLineStoreDao.addStoreIcon(map);
	}

	/**
	 * 添加店铺营业执照
	 * @param verifyId
	 * @param licenseUrl
	 */
	public void addLicense(String verifyId, String licenseUrl) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("verifyId", verifyId);
		map.put("licenseUrl", licenseUrl);
		onLineStoreDao.addLicense(map);
	}

	/**
	 * 添加身份证正面照
	 * @param verifyId
	 * @param idcardupUrl
	 */
	public void addIdCardUp(String verifyId, String idcardupUrl)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("verifyId", verifyId);
		map.put("idcardupUrl", idcardupUrl);
		onLineStoreDao.addIdCardUp(map);
	}

	/**
	 * 添加身份证反面照
	 * @param verifyId
	 * @param idcarddownUrl
	 */
	public void addIdCardDown(String verifyId, String idcarddownUrl) 
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("verifyId", verifyId);
		map.put("idcarddownUrl", idcarddownUrl);
		onLineStoreDao.addIdCardDown(map);
	}

	/**
	 * 根据id获取用户的线上店铺
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserOnLineStore(String userId) 
	{
		return onLineStoreDao.getUserOnLineStore(userId);
	}

	/**
	 * 根据店铺名获取店铺
	 * @param storeName
	 * @return
	 */
	public List<Map<String, Object>> getStoreByName(String storeName,String storeId) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("storeName", storeName);
		parameters.put("storeId", storeId);
		return onLineStoreDao.getStoreByName(parameters);
	}

	/**
	 * 删除店铺
	 * @param sId
	 */
	public void deleteStore(String sId) 
	{
		onLineStoreDao.deleteStore(sId);
	}

	/**
	 * 删除店铺认证
	 * @param scId
	 */
	public void deleteStoreCertification(String scId) 
	{
		onLineStoreDao.deleteStoreCertification(scId);
	}

	/**
	 * 判断店铺名是否重复
	 * @param storeName
	 * @return
	 */
	public List<Map<String, Object>> getStoreByName2(String storeName) 
	{
		return onLineStoreDao.getStoreByName2(storeName);
	}

	/**
	 * 改变店铺等级下月生效
	 * @param parameters
	 */
	public void changeWaitingState(Map<String, Object> parameters) 
	{
		onLineStoreDao.changeWaitingState(parameters);
	}

	/**
	 * 查询店铺已选分类
	 * @param storeId
	 * @return
	 */
	public String getOldClass(String storeId) 
	{
		return onLineStoreDao.getOldClass(storeId);
	}

	/**
	 * 得到某个店铺的等级
	 * @param id
	 * @return
	 */
	public String getStoreLevelById(String id) 
	{
		return onLineStoreDao.getStoreLevelById(id);
	}

	/**
	 * 清空waitingstate的值
	 * @param id
	 */
	public void clearWaitingState(String id) 
	{
		onLineStoreDao.clearWaitingState(id);
	}

	/**
	 * 改变此用户为系统用户
	 * @param userId
	 */
	public void changeUserLevelSystem(String userId) 
	{
		onLineStoreDao.changeUserLevelSystem(userId);
	}

	/**
	 * 添加此用户角色为店家
	 * @param userId
	 */
	public void addUserMerchant(String userId) 
	{
		
		List<Map<String,Object>> list = onLineStoreDao.getUserMechant(userId);
		if(list.size()==0)//判读用户角色表中此用户是否已是商家
		{
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("userId", userId);
			parameters.put("id", id);
			onLineStoreDao.addUserMerchant(parameters);
		}
	}

	/**
	 * 删除此用户的店家角色
	 * @param userId
	 */
	public void deleteUserMerchant(String userId) 
	{
		onLineStoreDao.deleteUserMerchant(userId);
	}

	/**
	 * 改变店铺是否自营
	 * @param parameters
	 */
	public void changeIS(Map<String, Object> parameters) 
	{
		onLineStoreDao.changeIS(parameters);
	}

	/**
	 * 查询店铺某月/日的APP支付收益
	 * @param storeId
	 * @param string
	 * @param month
	 * @return 
	 */
	public Map<String, Object> getStoreWater(String storeId, String typeName, String time) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("storeId", storeId);
		parameters.put("typeName", typeName);
		parameters.put("time", "%"+time+"%");
		Map<String,Object> waters = onLineStoreDao.getStoreWater(parameters);
		if(waters==null)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("MONEY", 0.00);
			map.put("COMMISSION", 0.00);
			waters=map;
		}
		waters.put("time", time);
		return waters;
	}

	/**
	 * 得到店铺开店时间
	 * @param storeId
	 * @return
	 */
	public String getOpenTime(String storeId) 
	{
		return onLineStoreDao.getOpenTime(storeId);
	}

	/**
	 * 得到店铺的信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getOnLineStoreInfoById(String storeId)
	{
		return onLineStoreDao.getOnLineStoreInfoById(storeId);
	}

	/**
	 * 通过分类Id得到分类的信息
	 * @param c
	 * @return
	 */
	public Map<String, Object> getClassInfoById(String classId) 
	{
		return onLineStoreDao.getClassInfoById(classId);
	}

	/**
	 * 取出一个店铺中的某个分类的商品
	 * @param classId
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getGoodsByClassAndStore(String classId, String storeId) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("classId", classId);
		parameters.put("storeId", storeId);
		return onLineStoreDao.getGoodsByClassAndStore(parameters);
	}

	/**
	 * 修改店铺的分类
	 * @param storeId
	 * @param classes
	 */
	public void updateStoreClass(String storeId, String classes) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("classes", classes);
		parameters.put("storeId", storeId);
		onLineStoreDao.updateStoreClass(parameters);
	}

	/**
	 * 查询此用户待审核的线上店铺申请
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserOnLineStoreDSH(String userId) 
	{
		return onLineStoreDao.getUserOnLineStoreDSH(userId);
	}

	/**
	 * 修改店铺的排序
	 * @param parameters
	 */
	public void changeSort(Map<String, Object> parameters) 
	{
		onLineStoreDao.changeSort(parameters);
	}
}
