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
 * 线上店铺分类管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class ClassificationAop {

	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.goodsManage.ClassificationController.*(..))")
	public void classificationLog() {
	}

	@Before("classificationLog()")
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
		// 删除店铺分类方法url名
		case "deleteclassificationById":
			String CLASSIFICATION_ID = map.get("CLASSIFICATION_ID").toString();
			String CLASS_NAME = logService.queryXsStoreClassNameById(CLASSIFICATION_ID);
			note = "用户[" + userPhone + "]删除线上店铺分类[id:" + CLASSIFICATION_ID + ",CLASS_NAME:" + CLASS_NAME + "]的店铺分类";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 添加店铺分类
		case "addClassification":
			String className = map.get("CLASS_NAME").toString();
			note = "用户[" + userPhone + "]添加[" + className + "]的线上店铺分类";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
