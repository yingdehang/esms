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
 * 报单管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class DeclarationAop {
	@Autowired
	private LogService logservice;

	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.DeclarationController.*(..))")
	public void declarationLog() {
	}

	@After("declarationLog()")
	public void deAfter(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String url = request.getRequestURL().toString();
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		String note = "";
		switch (methodName) {
		case "pass":
			note = "用户[" + userPhone + "]通过了id为[" + map.get("id") + "],报单金额:[" + map.get("money") + "],买家Id:["
					+ map.get("customerId") + "],商家Id:[" + map.get("merchantId") + "],站长Id:[" + map.get("zzId")
					+ "],站长会员等级:[" + map.get("memberGrade") + "],店铺认证Id:[" + map.get("scId") + "]";
			logservice.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		case "refuse":
			note = "用户[" + userPhone + "]拒绝了id为[" + map.get("id") + "]的报单申请，理由[" + map.get("content") + "]";
			logservice.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
