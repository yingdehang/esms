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
import com.example.elephantshopping.dao.merchants.GoodsManageDao;
import com.example.elephantshopping.service.log.LogService;
import com.example.elephantshopping.utils.CookiesUtils;
import com.example.elephantshopping.utils.LogUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 店铺商品管理
 * 
 * @author XB
 *
 */
@Aspect
@Component
public class GoodsManageAop {
	@Autowired
	private LogService logService;

	@Autowired
	private GoodsManageDao goodsManageDao;

	/**
	 * 登录切点
	 */
	@Pointcut("execution(public * com.example.elephantshopping.controller.merchants.GoodsManageController.*(..))")
	public void storegoodsLog() {
	}

	@Before("storegoodsLog()")
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
		// 进入店铺商品管理
		case "toGoodsManageListHtml":
			note = "用户[" + userPhone + "]进入店铺[storeId:" + map.get("storeId") + "]的商品管理";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改商品销量
		case "updateGoodsXiaoLiang":
			note = "用户[" + userPhone + "]将商品[goodsId:" + map.get("GOODSID") + "]的销量[" + map.get("agoXiaoliang")
					+ "]修改为[" + map.get("newXiaoliang") + "],其他参数[storeId:" + map.get("storeId") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改库存
		case "updateGoodsInventory":
			note = "用户[" + userPhone + "]将商品[goodsId:" + map.get("GOODSID") + "]的库存修改为[" + map.get("INVENTORY") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改排序
		case "updateGoodsSort":
			note = "用户[" + userPhone + "]将商品[goodsId:" + map.get("GOODSID") + "]的排序修改为[" + map.get("STORE_SORT") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改价格
		case "updateGoodsPRICE":
			note = "用户[" + userPhone + "]将商品[goodsId:" + map.get("GOODSID") + "]的价格[" + map.get("ORIGINAL_PRICE")
					+ "]修改为[" + map.get("PRESENT_PRICE") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 修改商品信息
		case "updateGoods":
			note = "用户[" + userPhone + "]修改了商品[goodsId:" + map.get("goodsId") + "]的信息";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 添加商品
		case "addgoods":
			note = "用户[" + userPhone + "]给店铺[storeId" + map.get("storeId") + "]添加商品[" + map.get("GOODS_NAME") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 上架或下架
		case "updateGoodsState":
			String state = map.get("GOODS_STATE").toString();
			if (state.equals("GOODS_STATE_UP")) {
				note = "用户[" + userPhone + "]上架商品Id[" + map.get("GOODSID") + "]";
			} else if (state.equals("GOODS_STATE_DOWN")) {
				note = "用户[" + userPhone + "]下架商品Id[" + map.get("GOODSID") + "]";
			}
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		// 删除
		case "DeleteGoodsById":
			String goodsId = map.get("GOODSID").toString();
			Map<String, Object> goods = goodsManageDao.queryGoodsInfo(goodsId);
			note = "用户[" + userPhone + "]删除商品[" + goods.get("GOODS_NAME") + "]";
			logService.insertLog(LogUtils.insertlog(userId, url, note, request));
			break;
		default:
			break;
		}
	}
}
