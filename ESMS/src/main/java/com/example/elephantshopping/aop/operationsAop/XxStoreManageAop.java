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
 * 线下店铺管理及审核
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class XxStoreManageAop {

	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.operationsManage.OffLineStoreController.*(..))")
	public void offlineStoreLog() {
	}

	@Before("offlineStoreLog()")
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
		// 导出流水按钮
		case "export":
			note = "用户[" + userPhone + "]导出线下店铺流水[storeName:" + map.get("storeName") + ",phone:" + map.get("phone")
					+ ",storeClass:" + map.get("storeClass") + ",storeState:" + map.get("storeState") + ",start:"
					+ map.get("start") + ",end:" + map.get("end") + ",province:" + map.get("province") + ",city:"
					+ map.get("city") + ",area:" + map.get("area") + ",timeType:" + map.get("timeType") + ",month:"
					+ map.get("month") + ",startTime:" + map.get("startTime") + ",endTime:" + map.get("endTime") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 店铺冻结和解除冻结
		case "changeState":
			String id = map.get("id").toString();
			String state = map.get("result").toString();
			if (state.equals("0")) {
				note = "用户[" + userPhone + "]冻结了店铺id为[" + id + "]的线下店铺";
			} else if (state.equals("1")) {
				note = "用户[" + userPhone + "]解除冻结店铺id为[" + id + "]的线下店铺";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 转到线下店铺详情页面
		case "detail":
			String storeId = map.get("id").toString();
			note = "用户[" + userPhone + "]查看/修改店铺id为[" + storeId + "]的店铺";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 查看月流水
		case "toMonthWater":
			note = "用户[" + userPhone + "]查看线下店铺id为[" + map.get("storeId") + "]的线下店铺月流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺等级
		case "addNewLevel":
			note = "用户[" + userPhone + "]修改线下店铺id为[" + map.get("id") + "]的店铺等级[" + map.get("storeLevel") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺分类
		case "changeClass":
			note = "用户[" + userPhone + "]修改线下店铺id为[" + map.get("storeId") + "]的店铺分类[" + map.get("storeClass") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺名称
		case "changeName":
			note = "用户[" + userPhone + "]修改线下店铺id为[" + map.get("storeId") + "]的店铺名称[" + map.get("storeName") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺地址
		case "changeAddr":
			note = "用户[" + userPhone + "]修改线下店铺id为[" + map.get("id") + "]的店铺地址[" + map.get("addr") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改店铺服务电话
		case "changeServicePhone":
			note = "用户[" + userPhone + "]修改线下店铺id为[" + map.get("storeId") + "]的服务电话为[" + map.get("servicePhone") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出月流水
		case "exportMonthWater":
			note = "用户[" + userPhone + "]导出线下店铺id为[" + map.get("storeId") + "]日期为：[" + map.get("monthArray") + "]的月流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 查看日流水
		case "toDayWater":
			note = "用户[" + userPhone + "]查看线下店铺id为[" + map.get("storeId") + "]时间为[" + map.get("month") + "]的日流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 导出日流水
		case "exportDayWater":
			note = "用户[" + userPhone + "]导出线下店铺id为[" + map.get("storeId") + "]日期为：[" + map.get("dayArray") + "]的日流水";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 线下店铺审核
		case "verify":
			String result = map.get("result").toString();
			if (result.equals("1")) {
				note = "用户[" + userPhone + "]审核通过店铺id为[" + map.get("storeId") + "]的线下店铺";
			} else if (result.equals("0")) {
				note = "用户[" + userPhone + "]审核拒绝店铺id为[" + map.get("storeId") + "]的线下店铺,拒绝理由：[" + map.get("content")
						+ "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
