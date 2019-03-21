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
 * 线上店铺管理
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class XsStoreManageAop {

	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.OnLineStoreController.*(..))")
	public void onlinestoreLog() {
	}

	@Before("onlinestoreLog()")
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
		// 查看月流水
		case "toMonthWater":
			String storeId = map.get("storeId").toString();
			note = "用户[" + userPhone + "]查看线上店铺[id:" + storeId + "]的月流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出店铺的月流水
		case "exportMonthWater":
			note = "用户[" + userPhone + "]导出线上店铺[id:" + map.get("storeId") + ",monthArray:" + map.get("monthArray")
					+ "]的月流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 查看店铺
		case "toDayWater":
			note = "用户[" + userPhone + "]查看线上店铺[id:" + map.get("storeId") + ",month:" + map.get("month") + "]的日流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出日流水
		case "exportDayWater":
			note = "用户[" + userPhone + "]导出线上店铺[id:" + map.get("storeId") + ",dayArray:" + map.get("dayArray")
					+ "]的日流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 线上店铺冻结或解冻
		case "changeState":
			String id = map.get("id").toString();
			String state = map.get("result").toString();
			if (state.equals("0")) {
				note = "用户[" + userPhone + "]冻结了店铺id为[" + id + "]的线上店铺";
			} else if (state.equals("1")) {
				note = "用户[" + userPhone + "]解除冻结店铺id为[" + id + "]的线上店铺";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出流水
		case "export":
			note = "用户[" + userPhone + "]导出店铺流水[storeName:" + map.get("storeName") + ",phone:" + map.get("phone")
					+ ",storeClass:" + map.get("storeClass") + ",storeState:" + map.get("storeState") + ",start:"
					+ map.get("start") + ",end:" + map.get("end") + ",province:" + map.get("province") + ",city:"
					+ map.get("city") + ",area:" + map.get("area") + ",timeType:" + map.get("timeType") + ",month:"
					+ map.get("month") + ",startTime:" + map.get("startTime") + ",endTime:" + map.get("endTime") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺图标
		case "changeStoreIcon":
			note = "用户[" + userPhone + "]审核时修改店铺[" + map.get("storeId") + "]的图标";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 审核拒绝
		case "refuse":
			note = "用户[" + userPhone + "]审核拒绝了线上店铺[店铺认证ID:" + map.get("id") + "]的申请，理由[" + map.get("content") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 审核通过
		case "pass":
			note = "用户[" + userPhone + "]审核通过了线上店铺[" + map.get("storeId") + "]的申请";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
