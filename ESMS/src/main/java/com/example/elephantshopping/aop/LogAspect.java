package com.example.elephantshopping.aop;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.example.elephantshopping.service.LoginService;
import com.example.elephantshopping.service.log.LogService;
import com.example.elephantshopping.utils.LogUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 登录记录
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class LogAspect {
	@Autowired
	private LogService logService;
	@Autowired
	private LoginService loginService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.LoginController.*(..))")
	public void webLog() {
	}

	@Before("webLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
	}

	@AfterReturning(returning = "ret", pointcut = "webLog()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		// System.out.println("方法的返回值 : " + ret);
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		if (null != map.get("PHONE") && (!map.get("PHONE").equals(""))) {
			Map<String, Object> userInfo = loginService.getUserInfo(map.get("PHONE").toString());
			if (userInfo != null) {
				String userId = userInfo.get("USERS_ID").toString();
				String url = request.getRequestURL().toString();
				String note = "";
				String[] u = url.split("/");
				String p = u[u.length - 2] + "/" + u[u.length - 1];
				if (p.equals("login/userlogin")) {
					note = "登录后台管理系统";
				} else if (p.equals("login/outlogin")) {
					note = "退出后台管理系统";
				}
				// 记录下请求内容
				logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			}
		}
	}

	// 后置异常通知
	@AfterThrowing("webLog()")
	public void throwss(JoinPoint jp) {
		// 接收到请求，记录请求内容
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		Map map = RequestUtils.requestToMap(request);
		if (null != map.get("PHONE") && (!map.get("PHONE").equals(""))) {
			Map<String, Object> userInfo = loginService.getUserInfo(map.get("PHONE").toString());
			if (null != userInfo) {
				String userId = userInfo.get("USERS_ID").toString();
				String url = request.getRequestURL().toString();
				// 记录下请求内容
				logService.insertLog(LogUtils.insertlog(userId, url, "登录后台管理系统时发生异常", request));
			}
		}
	}

	// 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
	@After("webLog()")
	public void after(JoinPoint jp) {
		// System.out.println("方法最后执行.....");
	}

	// 环绕通知,环绕增强，相当于MethodInterceptor
	@Around("webLog()")
	public Object arround(ProceedingJoinPoint pjp) {
		// System.out.println("方法环绕start.....");
		try {
			Object o = pjp.proceed();
			// System.out.println("方法环绕proceed，结果是 :" + o);
			return o;
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}

}
