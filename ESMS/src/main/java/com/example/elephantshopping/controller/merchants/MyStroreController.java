package com.example.elephantshopping.controller.merchants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.merchants.MyStoreService;

/**
 * 我的店铺Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("myStore")
public class MyStroreController
{
	@Autowired
	private MyStoreService myStoreService;
	
	/**
	 * 转到我的店铺页面
	 * @param modelAndView
	 * @param userId
	 * @return
	 */
	@RequestMapping("toMyStore")
	public ModelAndView toMyStore(ModelAndView modelAndView,String userId)
	{
		Map<String,Object> store = myStoreService.getUserStore(userId);
		modelAndView.addObject("store", store);//查询店铺信息
		List<Map<String,Object>> orders = myStoreService.getPendingOrder(userId);
		modelAndView.addObject("count", orders.size());//查询待处理的订单数量
		List<Map<String,Object>> storeState = myStoreService.getStoreState();
		modelAndView.addObject("storeState", storeState);//查询所有的店铺状态
		List<Map<String,Object>> message = myStoreService.getMessage(userId);
		modelAndView.addObject("message", message);//查询店铺的所有消息
		String storeId = (String) store.get("STORE_ID");
		Map<String,Object> storeCertification = myStoreService.getStoreCertification(storeId);
		if(storeCertification.get("newLevel")==null)
		{
			storeCertification.put("newLevel", "");
		}
		modelAndView.addObject("storeCertification", storeCertification);//查询店铺认证信息
		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/merchants/myStore");
		return modelAndView;
	}
	
	/**
	 * 改变店铺营业状态
	 * @param state
	 * @return
	 */
	@RequestMapping("changeState")
	@ResponseBody
	public int changeState(String state,String storeId)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("state",state);
		parameters.put("storeId",storeId);
		myStoreService.changeState(parameters);
		return 0;
	}
	
	/**
	 * 改变店铺客服电话
	 * @param servicePhone
	 * @param storeId
	 * @return
	 */
	@RequestMapping("changeServicePhone")
	@ResponseBody
	public int changeServicePhone(String servicePhone,String storeId)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("servicePhone",servicePhone);
		parameters.put("storeId",storeId);
		myStoreService.changeServicePhone(parameters);
		return 0;
	}
}
