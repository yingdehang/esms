package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.merchants.MerchantsOrderService;
import com.example.elephantshopping.service.operationsManage.OrderService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 订单管理Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("order")
public class OrderController {
	@Autowired
	private MerchantsOrderService merchantsOrderService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 转到订单管理页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderManage")
	public ModelAndView toOrderManage(ModelAndView modelAndView, String storeId) {
		Map<String, Object> storeInfo = orderService.getStoreInfoById(storeId);
		modelAndView.addObject("storeInfo", storeInfo);// 根据店铺id获取店铺信息
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		List<Map<String, Object>> orderState = orderService.getOrderState();// 查询订单的所有状态除待支付状态
		modelAndView.addObject("orderState", orderState);
		List<Map<String, Object>> goods = orderService.getStoreGoods(storeId);// 得到本店所有的商品列表
		modelAndView.addObject("goods", goods);
		modelAndView.setViewName("/system/operationsManage/orderManage/orderManage");
		return modelAndView;
	}

	/**
	 * 获取/查询订单列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getOrders")
	@ResponseBody
	public Map<String, Object> getOrders(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		if (parameters.get("time") != null && (!parameters.get("time").equals(""))) {
			String payTimeStart = parameters.get("time").toString().substring(0, 19);
			String payTimeEnd = parameters.get("time").toString().substring(22, 41);
			parameters.put("payTimeStart", payTimeStart);
			parameters.put("payTimeEnd", payTimeEnd);
		}
		List<Map<String, Object>> orderList = orderService.getOrderList(parameters);// 未分页的数据List
		List<Map<String, Object>> orderListPage = orderService.getOrderListPage(parameters);// 分页的数据List

		map.put("code", 0);
		map.put("msg", "");
		map.put("data", orderListPage);
		map.put("count", orderList.size());
		return map;
	}

	/**
	 * 从订单管理转到订单详情页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderDetails")
	public ModelAndView toOrderDetails(ModelAndView modelAndView, String orderNumber, String storeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("orderNumber", orderNumber);
		List<Map<String, Object>> orderList = orderService.getOrderByOrderNumber(orderNumber);// 得到同一个订单编号对应的所有订单
//		String nickName = (String) orderList.get(0).get("NICKNAME");// 排除nickname为空的情况
		if (orderList.get(0).get("NICKNAME") == null) {
			orderList.get(0).put("NICKNAME", "");
		}
		String payTime = (String) orderList.get(0).get("PAY_TYPE");// 排除未支付的情况
		if (payTime == null) {
			orderList.get(0).put("PAY_TYPE", "");
		}
		modelAndView.addObject("order", orderList.get(0));
		Map<String, Object> storeUser = orderService.getStoreUser(storeId);// 获取卖家信息
		modelAndView.addObject("storeUser", storeUser);
		List<Map<String, Object>> location = new ArrayList<Map<String, Object>>();
		Map<String, Object> a = new HashMap<String, Object>();
		a.put("name", "线上店铺管理>");
		a.put("value", "/onLineStore/toOnLineStoreList");
		location.add(a);
		Map<String, Object> b = new HashMap<String, Object>();
		b.put("name", "订单管理>");
		b.put("value", "javascript:history.back(-1)");
		location.add(b);
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("name", "订单详情");
		c.put("value", "javascript:location.reload()");
		location.add(c);
		modelAndView.addObject("location", location);
		modelAndView.setViewName("/system/merchants/orderManage/orderDetails");
		return modelAndView;
	}

	/**
	 * 从订单查询转到订单详情页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderDetails2")
	public ModelAndView toOrderDetails2(ModelAndView modelAndView, String orderNumber, String storeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("orderNumber", orderNumber);
		List<Map<String, Object>> orderList = orderService.getOrderByOrderNumber(orderNumber);
//		String nickName = (String) orderList.get(0).get("NICKNAME");// 排除nickname为空的情况
		if (orderList.get(0).get("NICKNAME") == null) {
			orderList.get(0).put("NICKNAME", "");
		}
		String payTime = (String) orderList.get(0).get("PAY_TYPE");// 排除未支付的情况
		if (payTime == null) {
			orderList.get(0).put("PAY_TYPE", "");
		}
		modelAndView.addObject("order", orderList.get(0));// 得到同一个订单编号对应的所有订单
		Map<String, Object> storeUser = orderService.getStoreUser(storeId);// 获取卖家信息
		modelAndView.addObject("storeUser", storeUser);
		List<Map<String, Object>> location = new ArrayList<Map<String, Object>>();
		Map<String, Object> a = new HashMap<String, Object>();
		a.put("name", "订单查询>");
		a.put("value", "/order/toOrderSearch");
		location.add(a);
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("name", "订单详情");
		c.put("value", "javascript:location.reload()");
		location.add(c);
		modelAndView.addObject("location", location);
		modelAndView.setViewName("/system/merchants/orderManage/orderDetails");
		return modelAndView;
	}

	/**
	 * 转到物流信息页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderLogistics")
	public ModelAndView toOrderLogistics(ModelAndView modelAndView, String orderId, String storeId) {
		Map<String, Object> order = orderService.getOrderById(orderId);
		modelAndView.addObject("order", order);
		Map<String, Object> storeUser = orderService.getStoreUser(storeId);// 获取卖家信息
		modelAndView.addObject("storeUser", storeUser);
		modelAndView.setViewName("/system/merchants/orderManage/orderLogistics");
		return modelAndView;
	}

	/**
	 * 转到订单查询页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderSearch")
	public ModelAndView toOrderSearch(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> orderState = orderService.getOrderState();// 查询订单的所有状态
		modelAndView.addObject("orderState", orderState);
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		List<Map<String, Object>> goods = orderService.getGoods();// 得到所有的商品列表
		modelAndView.addObject("goods", goods);
		// 添加权限
		modelAndView.addObject("arbitration", permissionsController.queryPermissions("arbitration", request));
		modelAndView.setViewName("/system/operationsManage/orderManage/orderSearch");
		return modelAndView;
	}

	/**
	 * 查询所有订单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getAllOrders")
	@ResponseBody
	public Map<String, Object> getAllOrders(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		if(parameters.get("page").equals("")||parameters.get("page").equals("null"))
		{
			parameters.put("page", 1);
		}
		if(parameters.get("limit").equals("")||parameters.get("limit").equals("null"))
		{
			parameters.put("limit", 10);
		}
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		if (parameters.get("time") != null && (!parameters.get("time").equals(""))) {
			String payTimeStart = parameters.get("time").toString().substring(0, 19);
			String payTimeEnd = parameters.get("time").toString().substring(22, 41);
			parameters.put("payTimeStart", payTimeStart);
			parameters.put("payTimeEnd", payTimeEnd);
		}
		if (parameters.get("fhTime") != null && (!parameters.get("fhTime").equals(""))) {
			String fhTimeStart = parameters.get("fhTime").toString().substring(0, 19);
			String fhTimeEnd = parameters.get("fhTime").toString().substring(22, 41);
			parameters.put("fhTimeStart", fhTimeStart);
			parameters.put("fhTimeEnd", fhTimeEnd);
		}
		if (parameters.get("shTime") != null && (!parameters.get("shTime").equals(""))) {
			String shTimeStart = parameters.get("shTime").toString().substring(0, 19);
			String shTimeEnd = parameters.get("shTime").toString().substring(22, 41);
			parameters.put("shTimeStart", shTimeStart);
			parameters.put("shTimeEnd", shTimeEnd);
		}
		List<Map<String, Object>> orderList = orderService.getAllOrderList(parameters);// 未分页的所有订单
		List<Map<String, Object>> orderListPage = orderService.getAllOrderListPage(parameters);// 分页的所有订单
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", orderListPage);
		map.put("count", orderList.size());
		return map;
	}

	/**
	 * 转到订单仲裁页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toArbitration")
	public ModelAndView toArbitration(ModelAndView modelAndView, String orderNumber, String storeId) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("orderNumber", orderNumber);
		List<Map<String, Object>> orderList = orderService.getOrderByOrderNumber(orderNumber);
		modelAndView.addObject("order", orderList.get(0));// 得到同一个订单编号对应的所有订单
		Map<String, Object> storeUser = orderService.getStoreUser(storeId);// 获取卖家信息
		modelAndView.addObject("storeUser", storeUser);
		modelAndView.addObject("arbitration", "true");// 是否仲裁
		List<Map<String, Object>> location = new ArrayList<Map<String, Object>>();
		Map<String, Object> a = new HashMap<String, Object>();
		a.put("name", "订单查询>");
		a.put("value", "/order/toOrderSearch");
		location.add(a);
		Map<String, Object> c = new HashMap<String, Object>();
		c.put("name", "仲裁");
		c.put("value", "javascript:location.reload()");
		location.add(c);
		modelAndView.addObject("location", location);
		modelAndView.setViewName("/system/merchants/orderManage/orderDetails");
		return modelAndView;
	}

	/**
	 * 仲裁
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("arbitration")
	@ResponseBody
	@Transactional
	public synchronized String arbitration(String merchantId, String userId, String orderNumber, double sumPrice, String duty,
			String reason, String payType) {
		String orderState = merchantsOrderService.getOrderStateByNumber(orderNumber);
		if(!orderState.equals("ORDER_STATE_YCX")){//判断订单是否已仲裁，防止重复仲裁
			// 1.判断商家及用户账号是否冻结
			String merchantState = orderService.getUserState(merchantId);
			String userState = orderService.getUserState(userId);
			if (merchantState.equals("USER_STATE_DISABLE")) {
				return "商家账号已冻结";
			} else if (userState.equals("USER_STATE_DISABLE")) {
				return "用户账号已冻结";
			} else {
				// 2.判断责任，并算出需要扣除的金额大小
				double money = 0.00;// 需要扣除的金额
				if (duty.equals("商家")) {
					money = sumPrice;
				} else {
					money = sumPrice * 0.8;
				}
				// 3.取出商家的零花钱和余额
				double balance = orderService.getUserMoneys(merchantId);// 用户余额
				double pocketMoney = orderService.getUserPocketMoney(merchantId);// 用户零花钱
				// 4.判断商家的余额和零花钱是否足够扣除
				if (balance + pocketMoney < money) {
					return "商家余额及零花钱不足以扣除";
				} else {
					// 5.优先判断零花钱是否有值
					if (pocketMoney == 0) {
						// 无零花钱，直接扣除余额
						orderService.changeUserMoneys("2", merchantId, money, "仲裁");
					} else {
						// 6.判断零花钱是否足够扣除
						if (pocketMoney >= money) {
							// 零花钱足够则直接扣除零花钱
							orderService.changeUserPocketMoney("2", merchantId, money, "仲裁");
						} else {
							// 零花钱不够则先扣所有零花钱，然后扣除余额
							orderService.changeUserPocketMoney("2", merchantId, pocketMoney, "仲裁");
							orderService.changeUserMoneys("2", merchantId, money - pocketMoney, "仲裁");
						}
					}
				}
				// 7.根据用户的支付方式，将钱返还给用户
				if (payType.equals("1"))// 余额
				{
					orderService.changeUserMoneys("1", userId, money, "仲裁");
				} else if (payType.equals("0"))// 零花钱
				{
					orderService.changeUserPocketMoney("1", userId, money, "仲裁");
				} else// 消费券
				{
					orderService.changeUserConsumption("1", userId, money, "仲裁");
				}
			}
			// 8.改变订单状态
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("duty", duty);
			parameters.put("reason", reason);
			parameters.put("orderNumber", orderNumber);
			orderService.arbitration(parameters);
		}
		return "ok";
	}

	/**
	 * 订单查询导出
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exportOrderSearch")
	public void exportOrderSearch(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		if (parameters.get("time") != null && (!parameters.get("time").equals(""))) {
			String payTimeStart = parameters.get("time").toString().substring(0, 19);
			String payTimeEnd = parameters.get("time").toString().substring(22, 41);
			parameters.put("payTimeStart", payTimeStart);
			parameters.put("payTimeEnd", payTimeEnd);
		}
		if (parameters.get("shTime") != null && (!parameters.get("shTime").equals(""))) {
			String shTimeStart = parameters.get("shTime").toString().substring(0, 19);
			String shTimeEnd = parameters.get("shTime").toString().substring(22, 41);
			parameters.put("shTimeStart", shTimeStart);
			parameters.put("shTimeEnd", shTimeEnd);
		}
		if (parameters.get("fhTime") != null && (!parameters.get("fhTime").equals(""))) {
			String fhTimeStart = parameters.get("fhTime").toString().substring(0, 19);
			String fhTimeEnd = parameters.get("fhTime").toString().substring(22, 41);
			parameters.put("fhTimeStart", fhTimeStart);
			parameters.put("fhTimeEnd", fhTimeEnd);
		}
		List<Map<String, Object>> orderList = orderService.getAllOrderList2(parameters);// 根据条件查询的订单

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("订单");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("序号");
		row0.createCell(1).setCellValue("订单编号");
		row0.createCell(2).setCellValue("支付时间");
		row0.createCell(3).setCellValue("商品名字");
		row0.createCell(4).setCellValue("商品属性");
		row0.createCell(5).setCellValue("商品价格");
		row0.createCell(6).setCellValue("数量");
		row0.createCell(7).setCellValue("买家昵称");
		row0.createCell(8).setCellValue("买家手机号");
		row0.createCell(9).setCellValue("商家区域");
		row0.createCell(10).setCellValue("店铺名");
		row0.createCell(11).setCellValue("订单状态");
		row0.createCell(12).setCellValue("交易类型");
		row0.createCell(13).setCellValue("实收款");
		row0.createCell(14).setCellValue("收货人姓名");
		row0.createCell(15).setCellValue("收货人电话");
		row0.createCell(16).setCellValue("收货人地址");
		row0.createCell(17).setCellValue("物流公司");
		row0.createCell(18).setCellValue("物流单号");
		row0.createCell(19).setCellValue("发货时间");
		row0.createCell(20).setCellValue("收货时间");
		int count = 1;
		for (int i = 0; i < orderList.size(); i++) {
			Map<String, Object> order = orderList.get(i);
			List<Map<String, Object>> goods = (List<Map<String, Object>>) order.get("goods");
			for (int j = 0; j < goods.size(); j++) {
				HSSFRow row1 = sheet.createRow(count);
				row1.createCell(0).setCellValue(count);
				row1.createCell(1).setCellValue(orderList.get(i).get("ORDER_NUMBER").toString());
				row1.createCell(2).setCellValue(orderList.get(i).get("PAY_TIME").toString());
				row1.createCell(3).setCellValue(goods.get(j).get("GOODS_NAME").toString());
				row1.createCell(4).setCellValue(goods.get(j).get("ATTRIBUTE").toString());
				row1.createCell(5).setCellValue(goods.get(j).get("PRICE").toString());
				row1.createCell(6).setCellValue(goods.get(j).get("NUMBER").toString());
				row1.createCell(7).setCellValue(orderList.get(i).get("NICKNAME").toString());
				if(orderList.get(i).get("PHONE")==null)
				{
					row1.createCell(8).setCellValue("");
				}
				else
				{
					row1.createCell(8).setCellValue(orderList.get(i).get("PHONE").toString());
				}
				row1.createCell(9).setCellValue(orderList.get(i).get("province").toString()
						+ orderList.get(i).get("city").toString() + orderList.get(i).get("a").toString());
				row1.createCell(10).setCellValue(orderList.get(i).get("STORE_NAME").toString());
				row1.createCell(11).setCellValue(orderList.get(i).get("DESCRIBES").toString());
				if (orderList.get(i).get("PAY_TYPE").equals("0")) {
					row1.createCell(12).setCellValue("零花钱");
				} else if (orderList.get(i).get("PAY_TYPE").equals("1")) {
					row1.createCell(12).setCellValue("余额");
				} else if (orderList.get(i).get("PAY_TYPE").equals("2")) {
					row1.createCell(12).setCellValue("消费券");
				} else {
					row1.createCell(12).setCellValue("未付款");
				}
				row1.createCell(13).setCellValue(goods.get(j).get("goodsPrice").toString());
				row1.createCell(14).setCellValue(orderList.get(i).get("CONTACT").toString());
				if (orderList.get(i).get("LXPHONE") == null) {
					row1.createCell(15).setCellValue(orderList.get(i).get("PHONE").toString());
				} else {
					row1.createCell(15).setCellValue(orderList.get(i).get("LXPHONE").toString());
				}
				row1.createCell(16).setCellValue(orderList.get(i).get("userProvince").toString()
						+ orderList.get(i).get("userCity").toString() + orderList.get(i).get("userArea").toString()
						+ orderList.get(i).get("ADDR").toString());
				row1.createCell(17).setCellValue(orderList.get(i).get("company").toString());
				row1.createCell(18).setCellValue(orderList.get(i).get("THE_AWB").toString());
				row1.createCell(19).setCellValue(orderList.get(i).get("DELIVERY_TIME").toString());
				row1.createCell(20).setCellValue(orderList.get(i).get("SHTIME").toString());
				count++;
			}
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}
	
	/**
	 * 订单管理导出
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exportOrderManage")
	public void exportOrderManage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		if (parameters.get("time") != null && (!parameters.get("time").equals(""))) {
			String orderTimeStart = parameters.get("time").toString().substring(0, 19);
			String orderTimeEnd = parameters.get("time").toString().substring(22, 41);
			parameters.put("orderTimeStart", orderTimeStart);
			parameters.put("orderTimeEnd", orderTimeEnd);
		}
		List<Map<String, Object>> orderList = orderService.getOrderList2(parameters);// 根据条件查询的订单

		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("订单");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("序号");
		row0.createCell(1).setCellValue("订单编号");
		row0.createCell(2).setCellValue("下单时间");
		row0.createCell(3).setCellValue("商品名字");
		row0.createCell(4).setCellValue("商品属性");
		row0.createCell(5).setCellValue("商品价格");
		row0.createCell(6).setCellValue("数量");
		row0.createCell(7).setCellValue("买家昵称");
		row0.createCell(8).setCellValue("买家手机号");
		row0.createCell(9).setCellValue("商家区域");
		row0.createCell(10).setCellValue("店铺名");
		row0.createCell(11).setCellValue("订单状态");
		row0.createCell(12).setCellValue("交易类型");
		row0.createCell(13).setCellValue("实收款");
		row0.createCell(14).setCellValue("收货人姓名");
		row0.createCell(15).setCellValue("收货人电话");
		row0.createCell(16).setCellValue("收货人地址");
		row0.createCell(17).setCellValue("物流公司");
		row0.createCell(18).setCellValue("物流单号");
		int count = 1;
		for (int i = 0; i < orderList.size(); i++) {
			Map<String, Object> order = orderList.get(i);
			List<Map<String, Object>> goods = (List<Map<String, Object>>) order.get("goods");
			for (int j = 0; j < goods.size(); j++) {
				HSSFRow row1 = sheet.createRow(count);
				row1.createCell(0).setCellValue(count);
				row1.createCell(1).setCellValue(orderList.get(i).get("ORDER_NUMBER").toString());
				row1.createCell(2).setCellValue(orderList.get(i).get("ORDER_TIME").toString());
				row1.createCell(3).setCellValue(goods.get(j).get("GOODS_NAME").toString());
				row1.createCell(4).setCellValue(goods.get(j).get("ATTRIBUTE").toString());
				row1.createCell(5).setCellValue(goods.get(j).get("PRICE").toString());
				row1.createCell(6).setCellValue(goods.get(j).get("NUMBER").toString());
				row1.createCell(7).setCellValue(orderList.get(i).get("NICKNAME").toString());
				row1.createCell(8).setCellValue(orderList.get(i).get("PHONE").toString());
				row1.createCell(9).setCellValue(orderList.get(i).get("province").toString()
						+ orderList.get(i).get("city").toString() + orderList.get(i).get("a").toString());
				row1.createCell(10).setCellValue(orderList.get(i).get("STORE_NAME").toString());
				row1.createCell(11).setCellValue(orderList.get(i).get("DESCRIBES").toString());
				if (orderList.get(i).get("PAY_TYPE").equals("0")) {
					row1.createCell(12).setCellValue("零花钱");
				} else if (orderList.get(i).get("PAY_TYPE").equals("1")) {
					row1.createCell(12).setCellValue("余额");
				} else if (orderList.get(i).get("PAY_TYPE").equals("2")) {
					row1.createCell(12).setCellValue("消费券");
				} else {
					row1.createCell(12).setCellValue("未付款");
				}
				row1.createCell(13).setCellValue(goods.get(j).get("goodsPrice").toString());
				row1.createCell(14).setCellValue(orderList.get(i).get("CONTACT").toString());
				if (orderList.get(i).get("LXPHONE") == null) {
					row1.createCell(15).setCellValue(orderList.get(i).get("PHONE").toString());
				} else {
					row1.createCell(15).setCellValue(orderList.get(i).get("LXPHONE").toString());
				}
				row1.createCell(16).setCellValue(orderList.get(i).get("userProvince").toString()
						+ orderList.get(i).get("userCity").toString() + orderList.get(i).get("userArea").toString()
						+ orderList.get(i).get("ADDR").toString());
				row1.createCell(17).setCellValue(orderList.get(i).get("company").toString());
				row1.createCell(18).setCellValue(orderList.get(i).get("THE_AWB").toString());
				count++;
			}
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}
}
