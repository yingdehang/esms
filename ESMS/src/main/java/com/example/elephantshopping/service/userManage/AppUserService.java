package com.example.elephantshopping.service.userManage;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.userManage.AppUserDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * APP用户管理Service
 * @author XB
 *
 */
@Service
public class AppUserService 
{
	@Autowired
	private AppUserDao appUserDao;

	/**
	 * 表格获取APP用户数据
	 * @return
	 */
	public List<Map<String, Object>> getAppUserList(Map<String,Object> parameters) 
	{
		List<Map<String, Object>> appUserList = appUserDao.getAppUserList(parameters);
		DateFormatUtils.timeforDateType(appUserList, "REGISTERED_TIME");
		DateFormatUtils.timeforDateType(appUserList, "TO_APPLY_FOR_TIME");
		return appUserList;
	}

	/**
	 * 禁用账号
	 * @param userId
	 */
	public void forbidden(String userId) 
	{
		appUserDao.forbidden(userId);
	}

	/**
	 * 启用账号
	 * @param userId
	 */
	public void startUsing(String userId)
	{
		appUserDao.startUsing(userId);
	}

	/**
	 * 表格获取APP用户数据并分页
	 * @return
	 */
	public List<Map<String, Object>> getAppUserListPage(Map<String,Object> parameters)
	{
		List<Map<String, Object>> appUserListPage = appUserDao.getAppUserListPage(parameters);
		DateFormatUtils.timeforDateType(appUserListPage, "REGISTERED_TIME");
		DateFormatUtils.timeforDateType(appUserListPage, "TO_APPLY_FOR_TIME");
		return appUserListPage;
	}


	/**
	 * 获取用户详细信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getDetails(String userId)
	{
		return appUserDao.getDetails(userId);
	}

	/**
	 * 获取用户状态
	 * @return
	 */
	public List<Map<String, Object>> getUserState() 
	{
		return appUserDao.getUserState();
	}

	/**
	 * 获取实名认证状态
	 * @return
	 */
	public List<Map<String, Object>> getVerifyState() 
	{
		return appUserDao.getVerifyState();
	}

	/**
	 * 改变账户状态
	 * @param parameters
	 */
	public void changeUserState(Map<String, Object> parameters) 
	{
		appUserDao.changeUserState(parameters);
	}

	/**
	 * 改变账户认证状态
	 * @param parameters
	 */
	public void changeVerifytate(Map<String, Object> parameters)
	{
		appUserDao.changeVerifytate(parameters);
	}

	/**
	 * 获得用户个人流水数据
	 * @param userId
	 * @return
	 */
	public List<Map<String, Object>> getUserRunningAccounts(String userId) 
	{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> balance = appUserDao.getUserMoneyWater(userId);//用户余额流水数据
		List<Map<String,Object>> pocket = appUserDao.getUserPocketMoney(userId);//用户零花钱流水数据
		List<Map<String,Object>> consumption = appUserDao.getUserConsumptionVolume(userId);//用户消费券流水数据
		List<Map<String,Object>> integral = appUserDao.getUserIntegral(userId);//用户积分流水数据
		//将余额流水按格式存入list
		for(Map<String,Object> b:balance)
		{
			 Map<String,Object> map = new HashMap<String,Object>();
			 map.put("time", b.get("ETIME"));
			 map.put("action", b.get("ENAME"));
			 map.put("pocketIn","0.00");
			 map.put("pocketOut","0.00");
			 map.put("consumptionIn","0.00");
			 map.put("consumptionOut","0.00");
			 map.put("integralIn","0.00");
			 map.put("integralOut","0.00");
			 if(b.get("ETYPE").equals("1"))
			 {
				 map.put("balanceIn",b.get("EMONEY"));
				 map.put("balanceOut","0.00");
			 }
			 else
			 {
				 map.put("balanceOut",b.get("EMONEY"));
				 map.put("balanceIn","0.00");
			 }
			 list.add(map);
		}
		//将零花钱流水按格式存入list
		for(Map<String,Object> p:pocket)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("time", p.get("ADDTIME"));
			map.put("action", p.get("CNAME"));
			map.put("balanceIn","0.00");
			map.put("balanceOut","0.00");
			map.put("consumptionIn","0.00");
			map.put("consumptionOut","0.00");
			map.put("integralIn","0.00");
			map.put("integralOut","0.00");
			if(p.get("ETYPE").equals("1"))
			{
				map.put("pocketIn",p.get("MONEY"));
				map.put("pocketOut","0.00");
			}
			else
			{
				map.put("pocketOut",p.get("MONEY"));
				map.put("pocketIn","0.00");
			}
			list.add(map);
		}
		//将消费券流水按格式存入list
		for(Map<String,Object> c:consumption)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("time", c.get("ADDTIME"));
			map.put("action", c.get("CNAME"));
			map.put("balanceIn","0.00");
			map.put("balanceOut","0.00");
			map.put("pocketIn","0.00");
			map.put("pocketOut","0.00");
			map.put("integralIn","0.00");
			map.put("integralOut","0.00");
			if(c.get("ECVTYPE").equals("1"))
			{
				map.put("consumptionIn",c.get("MOENY"));
				map.put("consumptionOut","0.00");
			}
			else
			{
				map.put("consumptionOut",c.get("MOENY"));
				map.put("consumptionIn","0.00");
			}
			list.add(map);
		}
		//将积分流水按格式存入list
		for(Map<String,Object> i:integral)
		{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("time", i.get("ADDTIME"));
			map.put("action", i.get("CNAME"));
			map.put("balanceIn","0.00");
			map.put("balanceOut","0.00");
			map.put("pocketIn","0.00");
			map.put("pocketOut","0.00");
			map.put("consumptionIn","0.00");
			map.put("consumptionOut","0.00");
			if(i.get("ETYPE").equals("1"))
			{
				map.put("integralIn",i.get("MONEY"));
				map.put("integralOut","0.00");
			}
			else
			{
				map.put("integralOut",i.get("MONEY"));
				map.put("integralIn","0.00");
			}
			list.add(map);
		}
		//按时间进行排序
		for(int i=0;i<list.size()-1;i++)
		{
			for(int j=i+1;j<list.size();j++)
			{
				Timestamp t1 = (Timestamp)list.get(i).get("time");
				Timestamp t2 = (Timestamp)list.get(j).get("time");
				if(t1.getTime()>t2.getTime())
				{
					Map<String,Object> map = list.get(j);
					list.set(j, list.get(i));
					list.set(i, map);
				}
			}
		}
		return list;
	}

	/**
	 * 用户信息
	 * @param userId
	 * @return
	 */
	public Map<String, Object> getUserInfo(String userId) 
	{
		return appUserDao.getUserInfo(userId);
	}

	/**
	 * 改变会员等级
	 * @param parameters
	 */
	public void changeGrade(Map<String, Object> parameters)
	{
		appUserDao.changeGrade(parameters);
	}

	/**
	 * 改变邀请人
	 * @param userId
	 * @param invitation
	 */
	public void changeInvitation(String userId, String invitation) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId", userId);
		parameters.put("invitation", invitation);
		appUserDao.changeInvitation(parameters);
	}
}
