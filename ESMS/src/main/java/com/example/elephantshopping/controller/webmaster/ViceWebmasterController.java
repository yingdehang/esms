package com.example.elephantshopping.controller.webmaster;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.webmaster.ViceWebmsterService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 副站长管理
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("viceWebmaster")
public class ViceWebmasterController {
	@Autowired
	private ViceWebmsterService viceWebmasterService;
	@Autowired
	private PermissionsController permissioinsController;

	/**
	 * 跳转副站长管理页面
	 */
	@RequestMapping("toViceWebmsterHtml")
	public ModelAndView toViceWebmsterHtml(ModelAndView mav, HttpServletRequest request) {
		mav.addObject("repeater", permissioinsController.queryPermissions("repeater", request));
		mav.setViewName("/system/webmasterManage/viceWebmaster");
		return mav;
	}

	/**
	 * 查询站长list
	 */
	@RequestMapping("queryvicemasterlist")
	@ResponseBody
	public Map<String, Object> queryvicemasterlist(HttpServletRequest request) {
		return viceWebmasterService.queryvicemasterlist(RequestUtils.requestToMap(request));
	}

	/**
	 * 添加副站长
	 */
	@RequestMapping("addvicemaster")
	@ResponseBody
	public int addvicemaster(String masterPhone, String upPhone) {
		return viceWebmasterService.addvicemaster(masterPhone, upPhone);
	}

	/**
	 * 撤销副站长
	 */
	@RequestMapping("cancelViceMaster")
	@ResponseBody
	public int cancelViceMaster(String userId) {
		return viceWebmasterService.cancelViceMaster(userId);
	}
}
