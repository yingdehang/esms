package com.example.elephantshopping.aop.operationsAop;

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
 * 大额报单管理
 * 
 * @author XB
 *
 */
@Aspect
@Component
public class BigOrderAuditAop {
	@Autowired
	private LogService logService;

	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.BigOrderAuditController.*(..))")
	public void bigorderlog() {
	}

	@After("bigorderlog()")
	public void doAfter(JoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		String note = "大额报单审核";
		switch (methodName) {
		case "updateBigOrderState":
			String bigState = map.get("bigState").toString();
			if (bigState.equals("2")) {
				note = "用户[" + userPhone + "]同意订单号为[" + map.get("orderNumber") + "]的报单申请";
			} else if (bigState.equals("3")) {
				note = "用户[" + userPhone + "]拒绝订单号为[" + map.get("orderNumber") + "]的报单申请，拒绝原因[" + map.get("content")
						+ "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, userPhone, note, request));
			break;
		default:
			break;
		}
	}
}
