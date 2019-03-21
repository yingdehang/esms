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
 * 站长管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class webmasterManageAop {

	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.webmaster.WebmasterManageController.*(..))")
	public void webmasterManageLog() {
	}

	@Before("webmasterManageLog()")
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
		// 撤销站长
		case "updateISZZ":
			String USER_ID = map.get("USERS_ID").toString();
			Map<String, Object> user = logService.getNIKENAMEBYID(USER_ID);
			note = "用户[" + userPhone + "]撤销用户手机号为[" + user.get("PHONE") + "]的站长职位";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 添加站长
		case "addWebmaster":
			note = "用户[" + userPhone + "]添加站长[" + map.get("userphone") + "],站长区域[" + map.get("area") + "],站长邀请人["
					+ map.get("zzyqr") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 站长结算
		case "zhanzhangjiesuan":
			String USERID = map.get("USERS_ID").toString();
			Map<String, Object> u = logService.getNIKENAMEBYID(USERID);
			note = "用户[" + userPhone + "]结算给站长[" + u.get("PHONE") + "]金额[" + map.get("money") + "],备注信息["
					+ map.get("cname") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 查看流水
		case "toRunningWaterHtml":
			String USERID1 = map.get("userID").toString();
			Map<String, Object> users = logService.getNIKENAMEBYID(USERID1);
			note = "用户[" + userPhone + "]查看站长[" + users.get("PHONE") + "]的月流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 查看月流水明细
		case "toSeeDetailsHtml":
			String USERID2 = map.get("userID").toString();
			Map<String, Object> user1 = logService.getNIKENAMEBYID(USERID2);
			note = "用户[" + userPhone + "]查看站长[" + user1.get("PHONE") + "]的月流水明细";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
