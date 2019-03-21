package com.example.elephantshopping.controller.userManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.userManage.ReceiveAddressService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 收货地址管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("receiveAddress")
public class ReceiveAddressController 
{
	@Autowired
	private ReceiveAddressService receiveAddressService;
	
	/**
	 * 转到收货地址列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toReceiveAddressList")
	public ModelAndView toAppUserList(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/userManage/receiveAddressManage/receiveAddressList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询收货地址list
	 * @param phoneNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("getReceiveAddressList")
	@ResponseBody
	public Map<String,Object> getCollectionList(String phoneNumber,HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		List<Map<String,Object>> receiveAddressList = receiveAddressService.getReceiveAddressList(parameters);//未分页的数据List
		List<Map<String,Object>> receiveAddressListPage = receiveAddressService.getReceiveAddressListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",receiveAddressListPage);
		map.put("count",receiveAddressList.size());
		return map;
	}
}
