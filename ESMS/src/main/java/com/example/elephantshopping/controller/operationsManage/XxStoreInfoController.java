package com.example.elephantshopping.controller.operationsManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.XxStoreInfoService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 线下店铺内容审核
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("xxstore")
public class XxStoreInfoController {
	@Autowired
	private XxStoreInfoService xxStoreInfoService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 跳转线下店铺详情list页面
	 */
	@RequestMapping("toXxStoreinfolist")
	public ModelAndView toXxStoreinfolist(ModelAndView mav, HttpServletRequest request) {
		mav.setViewName("/system/operationsManage/xxStoreInfo/xxStoreInfoList");
		mav.addObject("xxStoreInfoVerify", permissionsController.queryPermissions("xxStoreInfoVerify", request));
		return mav;
	}

	/**
	 * 跳转线下店铺详情审核记录list页面
	 */
	@RequestMapping("torecordlist")
	public ModelAndView torecordlist(ModelAndView mav) {
		mav.setViewName("/system/operationsManage/xxStoreInfo/recordList");
		return mav;
	}

	/**
	 * 查询线下店铺审核记录list
	 * 
	 * @return
	 */
	@RequestMapping("getrecordlistlist")
	@ResponseBody
	public Map<String, Object> getrecordlistlist(HttpServletRequest request) {
		return xxStoreInfoService.getXxStoreInfolist(RequestUtils.requestToMap(request));
	}

	/**
	 * 查询线下店铺list
	 * 
	 * @return
	 */
	@RequestMapping("getXxStoreInfolist")
	@ResponseBody
	public Map<String, Object> getXxStoreInfolist(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		map.put("type", "2");
		return xxStoreInfoService.getXxStoreInfolist(map);
	}

	/**
	 * 跳转线下店铺详情页面
	 */
	@RequestMapping("toXxStoreinfo")
	public ModelAndView toXxStoreinfo(ModelAndView mav, String ESXID) {
		mav.setViewName("/system/operationsManage/xxStoreInfo/xxStoreInfo");
		mav.addObject("storeInfo", xxStoreInfoService.getXxStoreInfoById(ESXID));
		return mav;
	}

	/**
	 * 修改店铺内容审核状态
	 */
	@RequestMapping("updateStoreInfoState")
	@ResponseBody
	public int updateStoreInfoState(String ESXID, String infoState, String content) {
		return xxStoreInfoService.updateStoreInfoState(ESXID, infoState, content);
	}

}
