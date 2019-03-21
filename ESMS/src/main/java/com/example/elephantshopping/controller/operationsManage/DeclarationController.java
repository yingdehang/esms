package com.example.elephantshopping.controller.operationsManage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.DeclarationService;
import com.example.elephantshopping.service.operationsManage.OrderService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 报单审核Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("declaration")
public class DeclarationController 
{
	@Autowired
	private DeclarationService declarationService;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PermissionsController permissionsController;
	
	
	/**
	 * 转到报单列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toDeclaration")
	public ModelAndView toDeclaration(ModelAndView modelAndView,String userId,HttpServletRequest request)
	{
		List<Map<String,Object>> province  = storeVerifyService.getSubordinateArea(1);//获取所有的省
		modelAndView.addObject("province", province);
		modelAndView.addObject("userId", userId);
		
		//添加权限
		modelAndView.addObject("declarationAudit",permissionsController.queryPermissions("declarationAudit", request));
		
		modelAndView.setViewName("/system/operationsManage/financeManage/declaration");
		return modelAndView;
	}
	
	/**
	 * 获取报单申请数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getDeclarationList")
	@ResponseBody
	public Map<String,Object> getDeclarationList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String name = (String) parameters.get("name");
		if(name!=null&&(!name.equals("")))
		{
			parameters.put("name", "%"+name+"%");
		}
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		if(parameters.get("page").equals("")||parameters.get("page").equals("null"))
		{
			parameters.put("page", 1);
		}
		if(parameters.get("limit").equals("")||parameters.get("limit").equals("null"))
		{
			parameters.put("limit", 10);
		}
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> declaration = declarationService.getDeclaration(parameters);//报单申请数据
		List<Map<String,Object>> declarationPage = declarationService.getDeclarationPage(parameters);//报单申请数据分页
		for(Map<String,Object> list:declarationPage)
		{
			String province = (String) list.get("PROVINCE");
			String city = (String) list.get("CITY");
			String area = (String) list.get("AREA");
			if(area==null)
			{
				list.put("area","");
			}
			else
			{
				list.put("area", province+city+area);
			}
			BigDecimal money = (BigDecimal) list.get("MONEY");
			list.put("MONEY", money.toString());
		}
		map.put("code",0);
		map.put("msg","");
		map.put("data",declarationPage);
		map.put("count",declaration.size());
		return map;
	}
	
	/**
	 * 转到报单审核页面
	 * @param modelAndView
	 * @param id
	 * @return
	 */
	@RequestMapping("toDeclarationVeirfy")
	public ModelAndView toDeclarationVeiry(ModelAndView modelAndView,String id,String userId)
	{
		Map<String,Object> details = declarationService.getDeclarationInfo(id);//获取报单审核详情
		List<String> pics = new ArrayList<String>();
		if(details.get("CREDENTIALS")!=null)
		{
			String p = (String) details.get("CREDENTIALS");
			String[] pic = p.split(",");
			for(String s:pic)
			{
				pics.add(s);
			}
		}
		details.put("pics", pics);
		String zzName = (String) details.get("zzName");
		String zzPhone = (String) details.get("zzPhone");
		if(zzName==null)
		{
			details.put("zz", "");
		}
		else
		{
			details.put("zz", zzName+"("+zzPhone+")");
		}
		if(details.get("zzId")==null)
		{
			details.put("zzId","");
			details.put("memberGrade","");
		}
		if(details.get("NICKNAME")==null)
		{
			details.put("NICKNAME","");
		}
		if(details.get("PROVINCE")==null)
		{
			details.put("PROVINCE","");
		}
		if(details.get("CITY")==null)
		{
			details.put("CITY","");
		}
		if(details.get("AREA")==null)
		{
			details.put("AREA","");
		}
		modelAndView.addObject("details", details);
		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/operationsManage/financeManage/declarationVerify");
		return modelAndView;
	}
	
	/**
	 * 审核通过
	 * @param request
	 * @return
	 */
	@RequestMapping("pass")
	@ResponseBody
	@Transactional
	public synchronized String pass(String id,String userId,String customerId,String merchantId,String zzId,String memberGrade,String scId,double money)
	{
//		System.out.println("id,"+id);
//		System.out.println("userId,"+userId);
//		System.out.println("customerId,"+customerId);
//		System.out.println("merchantId,"+merchantId);
//		System.out.println("zzId,"+zzId);
//		System.out.println("memberGrade,"+memberGrade);
//		System.out.println("money,"+money);
//		System.out.println("scId,"+scId);
		
		String state = declarationService.getDeclarationState(id);//查询报单状态
		if(state.equals("0")){//申请中才可报单，防止重复通过
			
			//1.查出需要的各种系数
			//店铺等级系数
			double storeGradeCoefficient = declarationService.getStoreGradeCoefficient(scId);
			//佣金系数
			double commissionCoefficient = declarationService.getCoefficientValue("df0c3ce9456c4c35aa78201f8bf3852c");
			//积分奖励系数
			double integralAwardCoefficient = declarationService.getCoefficientValue("a7b44f0ab0774769bbf1596e5c078b67");
			//余额消费系数
			double balanceConsumeCoefficient = declarationService.getCoefficientValue("a0866f9684a2465e8a7a1ef97c782e27");
			//分享者零花钱奖励系数
			double sharerPocketMoneyAwardCoefficient = declarationService.getCoefficientValue("847af004c24c47599850969c85cdc827");
			//分享者积分奖励系数
			double sharerIntegralAwardCoefficient = declarationService.getCoefficientValue("29b0574c54c74e61bc708e9b16de0a85");
			//站长收益系数
			double webMasterEarningCoefficient = declarationService.getCoefficientValue("244cc4085ed24c459370d681123ddc44");
			//站长高级会员等级系数
			double webMasterMemberGradeCoefficient_hight = declarationService.getCoefficientValue("63179ab62f384e7ba1d21eb3ed72aa3d");
			//站长中级会员等级系数
			double webMasterMemberGradeCoefficient_middle = declarationService.getCoefficientValue("c671eb049cf84310a3a808559df2d2b9");
			//站长初级会员等级系数
			double webMasterMemberGradeCoefficient_low = declarationService.getCoefficientValue("c74c622ed89f4fdea54a357004cc32b6");
//		System.out.println("店铺等级系数:"+storeGradeCoefficient);
//		System.out.println("佣金系数:"+commissionCoefficient);
//		System.out.println("积分奖励系数:"+integralAwardCoefficient);
//		System.out.println("余额消费系数:"+balanceConsumeCoefficient);
//		System.out.println("分享者零花钱奖励系数:"+sharerPocketMoneyAwardCoefficient);
//		System.out.println("分享者积分奖励系数:"+sharerIntegralAwardCoefficient);
//		System.out.println("站长收益系数:"+webMasterEarningCoefficient);
//		System.out.println("站长高级会员等级系数:"+webMasterMemberGradeCoefficient_hight);
//		System.out.println("站长中级会员等级系数:"+webMasterMemberGradeCoefficient_middle);
//		System.out.println("站长初级会员等级系数:"+webMasterMemberGradeCoefficient_low);
			//2.判断商家及买家的账号是否冻结
			String merchantState = orderService.getUserState(merchantId);
			String customerState = orderService.getUserState(customerId);
			if(merchantState.equals("USER_STATE_DISABLE"))
			{
				return "商家账号已冻结";
			}
			else if(customerState.equals("USER_STATE_DISABLE"))
			{
				return "买家账号已冻结";
			}
			else
			{
				//3.计算佣金
				double commission = storeGradeCoefficient*commissionCoefficient*money;
//			System.out.println("佣金："+commission);
				//4.取出商家的零花钱和余额
				double balance = orderService.getUserMoneys(merchantId);//商家余额
//			System.out.println("商家余额："+balance);
				double pocketMoney = orderService.getUserPocketMoney(merchantId);//商家零花钱
//			System.out.println("商家零花钱："+pocketMoney);
				//5.判断商家的余额和零花钱是否足够扣除佣金,足够则扣除佣金
				if(balance+pocketMoney<commission)
				{
					return "商家余额及零花钱不足以支付佣金";
				}
				else
				{
					//优先判断余额是否有值
					if(balance==0)
					{
						//无余额，直接扣除零花钱
						orderService.changeUserPocketMoney("2", merchantId, commission,"佣金");
//					System.out.println("扣零花钱："+commission);
					}
					else
					{
						//判断余额是否足够扣除
						if(balance>=commission)
						{
							//余额足够则直接扣除余额
							orderService.changeUserMoneys("2", merchantId, commission,"佣金");
//						System.out.println("扣余额："+commission);
						}
						else
						{
							//余额不够则先扣所有余额，然后扣除零花钱
							orderService.changeUserMoneys("2", merchantId,balance,"佣金");
							orderService.changeUserPocketMoney("2", merchantId,commission-balance,"佣金");
//						System.out.println("扣余额："+balance);
//						System.out.println("扣零花钱："+(commission-balance));
						}
					}
				}
				//6.计算买家应得积分，并增加积分同时添加流水
				double integral = money*integralAwardCoefficient*storeGradeCoefficient*balanceConsumeCoefficient;
//			System.out.println("买家积分："+integral);
				orderService.changeUserIntegral("1",customerId,integral,"报单");
				
				
//			//7.判断买家是否是站长，是就清空邀请人直接进行下一步，不是则给邀请人返回零花钱并添加流水
//			String isZZ1 = declarationService.isZZ(customerId);//获得用户IS_ZZ字段判断是不是站长
//			if(isZZ1.equals("1"))
//			{
//				//是站长则清除其邀请人
//				//declarationService.clearInvitation(customerId);
//			}
//			else
//			{
				//不是站长则验证用户邀请人的状态，正常状态下为其邀请人添加零花钱
				Map<String,Object> customerInvitation = declarationService.getInviationId(customerId);//得到买家的邀请人信息
				//判断买家是否有邀请人
				if(customerInvitation!=null)
				{
					String customerInvitationId = (String) customerInvitation.get("USERS_ID");
					String customerInvitationState = (String) customerInvitation.get("STATE");
					//判断买家邀请人状态是否正常
					if(customerInvitationState.equals("USER_STATE_NORMAL"))
					{
						//如果邀请人状态正常，那么给他添加零花钱
						double invitationPocketMoney = money*storeGradeCoefficient*sharerPocketMoneyAwardCoefficient;
//						System.out.println("买家邀请人零花钱："+invitationPocketMoney);
						orderService.changeUserPocketMoney("1",customerInvitationId,invitationPocketMoney,"分享奖励");
						//添加分享奖励流水
						orderService.addShareAwardWater(customerId, customerInvitationId,"零花钱："+invitationPocketMoney, "分享奖励");
						//给买家邀请人添加分享奖励金
						orderService.changeShareReward("1", invitationPocketMoney, customerInvitationId);
					}
				}
//			}
				//8.给商家返回积分奖励
				double merchantIntegral = storeGradeCoefficient*commissionCoefficient*money;
//			System.out.println("商家积分奖励："+merchantIntegral);
				orderService.changeUserIntegral("1", merchantId,merchantIntegral,"报单");
//			//9.判断商家是否是站长，是就清空邀请人直接进行下一步，不是则给邀请人返回积分并添加流水
//			String isZZ2 = declarationService.isZZ(merchantId);//获得用户IS_ZZ字段判断是不是站长
//			if(isZZ2.equals("1"))
//			{
//				//是站长则清除其邀请人
//				//declarationService.clearInvitation(merchantId);
//			}
//			else
//			{
				//不是站长则验证用户邀请人的状态，正常状态下为其邀请人添加零花钱
				Map<String,Object> merchantInvitation = declarationService.getInviationId(merchantId);//得到商家的邀请人信息
				//判断商家是否有邀请人
				if(merchantInvitation!=null)
				{
					String merchantInvitationId = (String) merchantInvitation.get("USERS_ID");
					String merchantInvitationState = (String) merchantInvitation.get("STATE");
					//判断商家邀请人状态是否正常
					if(merchantInvitationState.equals("USER_STATE_NORMAL"))
					{
						//如果邀请人状态正常，那么给他添加积分
						double invitationIntegral = money*storeGradeCoefficient*sharerIntegralAwardCoefficient;
//						System.out.println("商家邀请人积分："+invitationIntegral);
						orderService.changeUserIntegral("1", merchantInvitationId, invitationIntegral,"分享奖励");
						//添加分享奖励流水
						orderService.addShareAwardWater(merchantId, merchantInvitationId,"积分："+invitationIntegral, "分享奖励");
						//给商家邀请人添加分享奖励积分
						orderService.changeShareReward("2", invitationIntegral, merchantInvitationId);
					}
				}
//			}
				//10.判断商家所在区域的站长是否存在以及状态是否正常，正常状态下给他添加站长金额
				//判断站长是否存在
				if(!zzId.equals(""))
				{
					//判断站长状态
					String zzState = orderService.getUserState(zzId);
					if(zzState.equals("USER_STATE_NORMAL"))
					{
						//正常状态下给他添加站长金额
						double zzMoney = 0.00;
						//根据站长的会员等级计算站长金额
						if(memberGrade.equals("USER_DJ_SENIOR"))//高级会员
						{
							zzMoney = money*webMasterEarningCoefficient*webMasterMemberGradeCoefficient_hight;
						}
						else if(memberGrade.equals("USER_DJ_INTERMEDIATE"))//中级会员
						{
							zzMoney = money*webMasterEarningCoefficient*webMasterMemberGradeCoefficient_middle;
						}
						else//初级会员
						{
							zzMoney = money*webMasterEarningCoefficient*webMasterMemberGradeCoefficient_low;
						}
//					System.out.println("站长金额："+zzMoney);
						//给站长添加站长金额
						orderService.changeWebMasterMoney("1", zzId, zzMoney, "商家提成");
					}
				}
			}
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("userId",userId);
			parameters.put("id",id);
			declarationService.pass(parameters);
		}
		return "ok";
	}
	
	/**
	 * 审核失败
	 * @param request
	 * @return
	 */
	@RequestMapping("refuse")
	@ResponseBody
	public int refuse(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		declarationService.refuse(parameters);
		return 0;
	}
	
	
	/**
	 * 转到保单记录页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toDeclarationRecord")
	public ModelAndView toDeclarationRecord(ModelAndView modelAndView)
	{
		List<Map<String,Object>> province  = storeVerifyService.getSubordinateArea(1);//获取所有的省
		modelAndView.addObject("province", province);
		modelAndView.setViewName("/system/operationsManage/financeManage/declarationRecord");
		return modelAndView;
	}
	
	/**
	 * 获取报单明细
	 * @param request
	 * @return
	 */
	@RequestMapping("getDeclarationRecordList")
	@ResponseBody
	public Map<String,Object> getDeclarationRecordList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String name = (String) parameters.get("name");
		if(name!=null&&(!name.equals("")))
		{
			parameters.put("name", "%"+name+"%");
		}
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> declaration = declarationService.getDeclarationRecord(parameters);//报单明细数据
		List<Map<String,Object>> declarationPage = declarationService.getDeclarationRecordPage(parameters);//报单明细数据分页
		for(Map<String,Object> list:declarationPage)
		{
			String province = (String) list.get("PROVINCE");
			String city = (String) list.get("CITY");
			String area = (String) list.get("AREA");
			if(area==null)
			{
				list.put("area","");
			}
			else
			{
				list.put("area", province+city+area);
			}
			BigDecimal money = (BigDecimal) list.get("MONEY");
			list.put("MONEY", money.toString());
		}
		map.put("code",0);
		map.put("msg","");
		map.put("data",declarationPage);
		map.put("count",declaration.size());
		return map;
	}
	
	/**
	 * 转到报单详情页面
	 * @param modelAndView
	 * @param id
	 * @return
	 */
	@RequestMapping("toDeclarationDetails")
	public ModelAndView toDeclarationDetails(ModelAndView modelAndView,String id)
	{
		Map<String,Object> details = declarationService.getDeclarationInfo(id);//获取报单审核详情
		String p = (String) details.get("CREDENTIALS");
		String[] pic = p.split(",");
		List<String> pics = new ArrayList<String>();
		for(String s:pic)
		{
			pics.add(s);
		}
		details.put("pics", pics);
		String zzName = (String) details.get("zzName");
		String zzPhone = (String) details.get("zzPhone");
		if(zzName==null)
		{
			details.put("zz", "");
		}
		else
		{
			details.put("zz", zzName+"("+zzPhone+")");
		}
		if(details.get("NICKNAME")==null)
		{
			details.put("NICKNAME","");
		}
		if(details.get("PROVINCE")==null)
		{
			details.put("PROVINCE","");
		}
		if(details.get("CITY")==null)
		{
			details.put("CITY","");
		}
		if(details.get("AREA")==null)
		{
			details.put("AREA","");
		}
		modelAndView.addObject("details", details);
		modelAndView.setViewName("/system/operationsManage/financeManage/declarationDetails");
		return modelAndView;
	}
}
