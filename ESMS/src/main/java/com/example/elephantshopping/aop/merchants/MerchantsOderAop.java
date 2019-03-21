package com.example.elephantshopping.aop.merchants;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.elephantshopping.service.log.LogService;
import com.example.elephantshopping.utils.CookiesUtils;
import com.example.elephantshopping.utils.LogUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 商家订单处理
 * 
 */
@Aspect
@Component
public class MerchantsOderAop {
	@Autowired
	private LogService logService;

	@Pointcut("execution(public * com.example.elephantshopping.controller.merchants.MerchantsOderController.*(..))")
	public void storeorderlog() {
	}

	@After("storeorderlog()")
	public void doAfter(JoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		String note = "订单处理";
		switch (methodName) {
		case "sendGoods":
			String[] args = (String[]) joinPoint.getArgs()[0];
			note = "用户[" + userPhone + "]给订单号为" + Arrays.toString(args) + "的订单发货，快递CODE:[" + map.get("logistics")
					+ "],运单号：[" + map.get("awb") + "]";
			logService.insertLog(LogUtils.insertlog(userId, userPhone, note, request));
			break;
		default:
			break;
		}
	}
}
