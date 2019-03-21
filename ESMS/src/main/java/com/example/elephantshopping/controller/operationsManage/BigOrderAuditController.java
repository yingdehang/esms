package com.example.elephantshopping.controller.operationsManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.BigOrderAuditService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 大额订单审核
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("bigOrderAudit")
public class BigOrderAuditController {
	@Autowired
	private BigOrderAuditService bigOrderAuditService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 跳转大额订单审核页面
	 */
	@RequestMapping("toBigOrderAuditListHtml")
	public ModelAndView toBigOrderAuditListHtml(ModelAndView mav, HttpServletRequest request) {
		mav.setViewName("/system/operationsManage/bigOrdersAudit/bigOrderAuditList");
		mav.addObject("bigOrderVerify", permissionsController.queryPermissions("bigOrderVerify", request));
		return mav;
	}

	/**
	 * 查询大额订单审核list
	 */
	@RequestMapping("queryBigOrderAuditList")
	@ResponseBody
	public Map<String, Object> queryBigOrderAuditList(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		map.put("bigState", "1");
		return bigOrderAuditService.queryBigOrderAuditList(map);
	}

	/**
	 * 查询订单状态
	 */
	@RequestMapping("queryBigState")
	@ResponseBody
	public String queryBigState(String orderId) {
		return bigOrderAuditService.queryBigState(orderId);
	}

	/**
	 * 大额订单审核页面
	 */
	@RequestMapping("toBigOrderInfo")
	@ResponseBody
	public ModelAndView toBigOrderInfo(ModelAndView mav, String orderNumber) {
		Map<String, Object> orderInfo = bigOrderAuditService.queryBigOrderInfo(orderNumber);
		if (null != orderInfo) {
			mav.addObject("orderInfo", orderInfo);
			mav.setViewName("/system/operationsManage/bigOrdersAudit/bigOrderInfo");
		} else {
			mav.setViewName("/system/operationsManage/bigOrdersAudit/bigOrderAuditList");
		}
		return mav;
	}

	/**
	 * 修改大额订单状态
	 */
	@RequestMapping("updateBigOrderState")
	@ResponseBody
	public int updateBigOrderState(HttpServletRequest request) {
		return bigOrderAuditService.updateBigOrderState(RequestUtils.requestToMap(request));
	}

}
