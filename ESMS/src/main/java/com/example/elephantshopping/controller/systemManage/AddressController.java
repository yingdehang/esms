package com.example.elephantshopping.controller.systemManage;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.systemManage.AddressService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 地址管理
 * 
 * @author ydh
 *
 */
@Controller
@RequestMapping("address")
public class AddressController {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private AddressService addressService;

	/**
	 * 根据角色id和菜单id查询该用户在本菜单中的权限，跳转地址管理页面
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toAddress")
	public ModelAndView toAddressHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.setViewName("/system/systemManage/addressManage/addressList");
		return mav;
	}

	/**
	 * 获取地址list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getAddressList")
	@ResponseBody
	public Map getAddressList(HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		return addressService.getAddressList(map);
	}

	/**
	 * 删除地址
	 * 
	 * @param Address_ID
	 * @return
	 */
	@RequestMapping("deleteAddressById")
	@ResponseBody
	public int deleteAddressById(String CITY_CODE) {
		addressService.deleteAddressById(CITY_CODE);
		return 1;
	}

	/**
	 * 返回父菜单按钮查询父菜单的PID
	 * 
	 * @param PID
	 * @return
	 */
	@RequestMapping("queryaddressParentId")
	@ResponseBody
	public int queryAddressParentId(int PID) {
		return addressService.queryAddressParentId(PID);
	}

	/**
	 * 添加地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addAddress")
	@ResponseBody
	public int addAddress(HttpServletRequest request) {
		addressService.addAddress(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 修改地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("updateAddress")
	@ResponseBody
	public int updateAddress(HttpServletRequest request) {
		return addressService.updateAddress(RequestUtils.requestToMap(request));
	}
}
