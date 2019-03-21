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
 * 实名认证操作
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class NameVerifyAop {
	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.userManage.NameVerifyController.*(..))")
	public void verifyLog() {
	}

	@Before("verifyLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String url = request.getRequestURL().toString();
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		switch (methodName) {
		// 实名认证审核
		case "toVerify":
			String verifyState = map.get("verifyState").toString();
			String user2 = map.get("userId").toString();
			String phone = logService.getNIKENAMEBYID(user2).get("PHONE").toString();
			String note = "用户[" + userPhone + "]审核了手机号为[" + phone + "]的用户帐号实名认证";
			if (verifyState.equals("USER_AUTHENTICATION_NO")) {
				note = "用户[" + userPhone + "]拒绝了手机号为[" + phone + "]的用户帐号实名认证,拒绝原因[" + map.get("content") + "]";
			} else if (verifyState.equals("USER_AUTHENTICATION_PASS")) {
				note = "用户[" + userPhone + "]通过了手机号为[" + phone + "]的用户帐号实名认证";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
