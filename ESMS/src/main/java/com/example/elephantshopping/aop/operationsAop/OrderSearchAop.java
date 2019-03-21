package com.example.elephantshopping.aop.operationsAop;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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
 * 订单查询
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class OrderSearchAop {

	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.OrderController.*(..))")
	public void OrderLog() {
	}

	@Before("OrderLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
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
		// 跳转仲裁页面
		case "toArbitration":
			note = "用户[" + userPhone + "]跳转订单[orderNumber:" + map.get("orderNumber") + ",storeId:" + map.get("storeId")
					+ "]的仲裁页面";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 仲裁
		case "arbitration":
			note = "用户[" + userPhone + "]仲裁订单号:[" + map.get("orderNumber") + "],被仲裁方：[" + map.get("duty") + "],原因["
					+ map.get("reason") + "],其他参数：[merchantId:" + map.get("merchantId") + ",userId:" + map.get("userId")
					+ ",sumPrice:" + map.get("sumPrice") + ",payType:" + map.get("payType") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
