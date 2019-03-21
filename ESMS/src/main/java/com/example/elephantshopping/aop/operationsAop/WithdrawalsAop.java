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
 * 提现审核
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class WithdrawalsAop {
	@Autowired
	private LogService logService;

	/**
	 * 切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.WithdrawalsController.*(..))")
	public void withdrawalsLog() {
	}

	@After("withdrawalsLog()")
	public void deBefore(JoinPoint joinPoint) throws Throwable {
		ServletRequestAttributes attribut = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attribut.getRequest();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String url = request.getRequestURL().toString();
		String methodName = joinPoint.getSignature().getName();
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		String userPhone = CookiesUtils.readCookies(request).get("userPhone").getValue();
		String note = "提现审核";
		switch (methodName) {
		// 审核
		case "withdrawalsVerify":
			String verifyState = map.get("verifyState").toString();
			if (verifyState.equals("EXTRACT_STATE_NO")) {
				note = "用户[" + userPhone + "]拒绝了用户[id:" + map.get("userId") + "]的提现申请，原因：[" + map.get("content")
						+ "],其他参数[withdrawalsId:" + map.get("withdrawalsId") + ",money:" + map.get("money")
						+ ",MONEY_TYPE:" + map.get("moneyType") + ",withdrawalType:" + map.get("withdrawalType")
						+ ",bankName:" + map.get("bankName") + ",bank:" + map.get("bank") + "]";
			} else if (verifyState.equals("EXTRACT_STATE_PASS")) {
				note = "用户[" + userPhone + "]通过了用户[id:" + map.get("userId") + "]的提现申请，其他参数[withdrawalsId:"
						+ map.get("withdrawalsId") + ",money:" + map.get("money") + ",MONEY_TYPE:"
						+ map.get("moneyType") + ",withdrawalType:" + map.get("withdrawalType") + ",bankName:"
						+ map.get("bankName") + ",bank:" + map.get("bank") + "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出
		case "export":
			note = "用户[" + userPhone + "]导出了[" + map.get("start") + " -- " + map.get("end") + "]的提现申请数据";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}

}
