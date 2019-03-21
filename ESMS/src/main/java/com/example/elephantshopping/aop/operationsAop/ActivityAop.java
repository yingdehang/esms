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
import com.example.elephantshopping.service.activityManage.ActivityService;
import com.example.elephantshopping.service.log.LogService;
import com.example.elephantshopping.utils.CookiesUtils;
import com.example.elephantshopping.utils.LogUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 活动管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class ActivityAop {
	@Autowired
	private LogService logService;

	@Autowired
	private ActivityService activityService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.activityManage.ActivityController.*(..))")
	public void activityLog() {
	}

	@Before("activityLog()")
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
		// 添加活动
		case "addactivity":
			note = "用户[" + userPhone + "]添加活动名称为:[" + map.get("EANAME") + "],活动时间:[" + map.get("activeTime")
					+ "],活动链接:[" + map.get("EAURL") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改活动状态
		case "updateActivityState":
			String EASTATE = map.get("EASTATE").toString();
			Map<String, Object> activity = activityService.queryActivityInfo(map.get("ESID").toString());
			if (EASTATE.equals("0")) {
				note = "用户[" + userPhone + "]关闭活动[" + activity.get("EANAME") + "]";
			} else if (EASTATE.equals("1")) {
				note = "用户[" + userPhone + "]开启活动[" + activity.get("EANAME") + "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 删除活动
		case "deleteActivity":
			Map<String, Object> activity1 = activityService.queryActivityInfo(map.get("ESID").toString());
			note = "用户[" + userPhone + "]删除活动[" + activity1.get("EANAME") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}

}
