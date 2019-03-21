package com.example.elephantshopping.controller.userManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.DeclarationService;
import com.example.elephantshopping.service.operationsManage.OrderService;
import com.example.elephantshopping.service.userManage.NameVerifyService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 实名认证管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("nameVerify")
public class NameVerifyController 
{
	@Autowired
	private NameVerifyService nameVerifyService;
	@Autowired
	private PermissionsController permissionsController;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeclarationService declarationService;
	
	/**
	 * 转到实名认证列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toNameVerifyList")
	public ModelAndView toAppUserList(ModelAndView modelAndView,HttpServletRequest request)
	{
		List<Map<String,Object>> verifyStates = nameVerifyService.getVerifyStates();
		modelAndView.addObject("verifyStates", verifyStates);
		//添加权限
		modelAndView.addObject("nameVerify",permissionsController.queryPermissions("nameVerify", request));
		
		modelAndView.setViewName("/system/userManage/nameVerifyManage/nameVerifyList");
		return modelAndView;
	}
	
	/**
	 * 表格获取/查询实名认证数据
	 * @param phoneNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("getNameVerifyList")
	@ResponseBody
	public Map<String,Object> getAppUserList(String phoneNumber,HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> NameVerifylist = nameVerifyService.searchNameVerify(parameters);//未分页的数据List
		List<Map<String,Object>> NameVerifyListPage = nameVerifyService.searchNameVerifyPage(parameters);//分页的数据List
		for(Map<String,Object> m : NameVerifyListPage)
		{
			String pro = (String) m.get("PROVINCE");
			String city = (String) m.get("CITY");
			String area = (String) m.get("AREA");
			m.put("area",pro+city+area);
		}
		map.put("code",0);
		map.put("msg","");
		map.put("data",NameVerifyListPage);
		map.put("count",NameVerifylist.size());
		return map;
	}
	
	/**
	 * 进行实名认证
	 * @param request
	 * @return
	 */
	@RequestMapping("toVerify")
	@ResponseBody
	public String toVerify(HttpServletRequest request,String verifyState,String userId)
	{
		String state = nameVerifyService.getVerifyStateById(userId);//查询用户的实名认证状态
		if(!state.equals("USER_AUTHENTICATION_PASS")){//判断用户是否已通过实名认证，防止重复认证
			Map<String,Object> parameters = new RequestUtils().requestToMap(request);//将页面传来的参数封装成一个Map
			if(verifyState.equals("USER_AUTHENTICATION_PASS"))//认证通过
			{
				//判断此用户的身份证是否已被注册
				int count = nameVerifyService.getUserByIdCard(userId);
				if(count>0)
				{
					return "此用户的身份证已被注册";
				}
				else
				{
					nameVerifyService.pass(parameters);//通过认证
					//用户增加3.33零花钱和3.33消费券
					orderService.changeUserPocketMoney("1", userId, 3.33, "实名注册奖励");//零花钱
					orderService.changeUserConsumption("1", userId, 3.33, "实名注册奖励");//消费券
					//用户邀请人增加166积分
					//判断用户的邀请人是否存在
					Map<String,Object> invitation = declarationService.getInviationId(userId);
					if(invitation!=null){//如果存在
						String invitationId = (String) invitation.get("USERS_ID");//邀请人Id
						orderService.changeUserIntegral("1", invitationId, 166, "分享注册奖励");
					}
					return "ok";
				}
			}
			else//认证失败
			{
				nameVerifyService.refuse(parameters);//认证失败
				return "ok";
			}
		}
		else{
			return "ok";
		}
	}
}
