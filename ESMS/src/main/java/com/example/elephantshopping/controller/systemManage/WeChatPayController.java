package com.example.elephantshopping.controller.systemManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.elephantshopping.service.systemManage.WechatPayService;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("wechat")
public class WeChatPayController {
	@Autowired
	private WechatPayService wechatPayService;

	@RequestMapping("toWechatPayListHtml")
	public String toWechatPayListHtml() {
		return "/system/systemManage/paymentManage/WeChatPayList";
	}

	@RequestMapping("getwechatpayList")
	@ResponseBody
	public Map<String, Object> getWechatpaylist(HttpServletRequest request) {
		return wechatPayService.getWechatpaylist(RequestUtils.requestToMap(request));
	}
}
