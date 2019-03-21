package com.example.elephantshopping.aop.userManageAop;

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
 * APP用户管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class AppUserAop {
	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.userManage.AppUserController.*(..))")
	public void appuserLog() {
	}

	@Before("appuserLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		String url = request.getRequestURL().toString();
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		String note = "";
		String phone = "";
		String user2 = "";
		Map<String, Object> map = RequestUtils.requestToMap(request);
		switch (methodName) {
		// 导出用户list
		case "exportAppUserList":
			note = "用户[" + userPhone + "]导出注册时间在[" + map.get("start") + "--" + map.get("end") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 冻结操作方法
		case "forbidden":
			user2 = map.get("userId").toString();
			phone = logService.getNIKENAMEBYID(user2).get("PHONE").toString();
			note = "用户[" + userPhone + "]冻结了手机号为[" + phone + "]的用户帐号";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 启用操作方法
		case "startUsing":
			user2 = map.get("userId").toString();
			phone = logService.getNIKENAMEBYID(user2).get("PHONE").toString();
			note = "用户[" + userPhone + "]启用了手机号为[" + phone + "]的用户帐号";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出流水
		case "export":
			user2 = map.get("userId").toString();
			phone = logService.getNIKENAMEBYID(user2).get("PHONE").toString();
			note = "用户[" + userPhone + "]导出了手机号为[" + phone + "]的个人流水数据";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
