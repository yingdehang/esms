package com.example.elephantshopping.controller.systemManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.elephantshopping.service.systemManage.AlipayService;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("alipay")
public class AlipayController {
	@Autowired
	private AlipayService alipayService;

	@RequestMapping("toAlipayPayList")
	public String toWechatPayListHtml() {
		return "/system/systemManage/paymentManage/AlipayPayList";
	}

	@RequestMapping("getalipaypayList")
	@ResponseBody
	public Map<String, Object> getalipaypayList(HttpServletRequest request) {
		return alipayService.getalipaypayList(RequestUtils.requestToMap(request));
	}
}
