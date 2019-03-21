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
 * 商品审核
 * 
 * @author XB
 *
 */
@Component
@Aspect
public class goodsAop {
	@Autowired
	private LogService logService;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.goodsManage.GoodsController.*(..))")
	public void goodsLog() {
	}

	@Before("goodsLog()")
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
		// 商品审核方法
		case "updategoodsState":
			String GOODSID = map.get("GOODSID").toString();
			String goodsStatus = map.get("goodsStatus").toString();
			if (goodsStatus.equals("GOODS_STATE_UP")) {
				note = "账户[" + userPhone + "]审核通过商品ID为[" + GOODSID + "]的商品";
			} else if (goodsStatus.equals("GOODS_STATE_FAIL")) {
				note = "账户[" + userPhone + "]拒绝通过商品ID为[" + GOODSID + "]的商品,理由：[" + map.get("CONTENT") + "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
