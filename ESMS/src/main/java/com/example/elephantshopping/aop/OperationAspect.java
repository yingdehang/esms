package com.example.elephantshopping.aop;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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
 * 用户操作记录
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class OperationAspect {
	@Autowired
	private LogService logService;

	/**
	 * 配置切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.systemManage.*.*(..))")
	public void operationLog() {
	}

	@Before("operationLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		String methodName = joinPoint.getSignature().getName();
		if (methodName.equals("queryPermissions")) {
		} 
		else {
			// 接收到请求，记录请求内容
			ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			HttpServletRequest request = attributes.getRequest();
			String url = request.getRequestURL().toString();
			String userId = CookiesUtils.readCookies(request).get("userId").getValue();
			String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
			Map<String, Object> map = RequestUtils.requestToMap(request);
			Set<String> set = map.keySet();
			Object[] args = new Object[set.size()];
			int i = 0;
			for (String key : set) {
				args[i] = key + ":" + map.get(key);
				i++;
			}
			String note = userPhone + "(操作内容:访问[" + joinPoint.getTarget().getClass().getName() + "]类的["
					+ joinPoint.getSignature().getName() + "]方法)系统管理,参数列表：[" + Arrays.toString(args) + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
		}
	}
}
