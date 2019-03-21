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
 * 线下店铺内容审核
 * 
 * @author XB
 *
 */
@Aspect
@Component
public class XxStoreInfoAop {
	@Autowired
	private LogService logService;

	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.XxStoreInfoController.*(..))")
	public void xxStoreInfoLog() {
	};

	@After("xxStoreInfoLog()")
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
		// 审核
		case "updateStoreInfoState":
			String infoState = map.get("infoState").toString();
			if (infoState.equals("4")) {
				note = "用户[" + userPhone + "]审核通过了ESXID为[" + map.get("ESXID") + "]的店铺详情";
			} else if (infoState.equals("3")) {
				note = "用户[" + userPhone + "]审核拒绝了ESXID为[" + map.get("ESXID") + "]的店铺详情，原因[" + map.get("content") + "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
