package com.example.elephantshopping.service.operationsManage;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.operationsManage.OrderDao;
import com.example.elephantshopping.entity.Logistics;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.LogisticsUtils;

/**
 * 订单管理Service
 * @author XB
 *
 */
@Service
public class OrderService
{
	@Autowired
	private OrderDao orderDao;
	
	/**
	 * 获取/查询未分页订单列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderList = orderDao.getOrderList(parameters);
		DateFormatUtils.timeforDateType(orderList,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderList,"CANCEL_TIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
//		for(Map<String,Object> map : orderList)
//		{
//			String orderNumber = (String) map.get("ORDER_NUMBER");
//			List<Map<String,Object>> goods = orderDao.getOrderGoods(orderNumber);
//			map.put("goods", goods);
//			double sumPrice = 0;
//			for(Map<String,Object> m : goods)
//			{
//				int number =  (int) m.get("NUMBER");
//				BigDecimal price = (BigDecimal) m.get("PRICE");
//				m.put("goodsPrice", price.doubleValue()*number);
//				sumPrice += price.doubleValue()*number;
//			}
//			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			//排除用户昵称为空的情况
//			if(map.get("NICKNAME")==null)
//			{
//				map.put("NICKNAME","");
//			}
//			//排除未支付的情况
//			if(map.get("PAY_TYPE")==null)
//			{
//				map.put("PAY_TYPE","");
//			}
//			//排除店铺区域为空的情况
//			if(map.get("province")==null)
//			{
//				map.put("province","");
//			}
//			if(map.get("city")==null)
//			{
//				map.put("city","");
//			}
//			if(map.get("a")==null)
//			{
//				map.put("area","");
//			}
//			//排除收货地址为空的情况
//			if(map.get("userProvince")==null)
//			{
//				map.put("userProvince","");
//			}
//			if(map.get("userCity")==null)
//			{
//				map.put("userCity","");
//			}
//			if(map.get("userArea")==null)
//			{
//				map.put("userArea","");
//			}
//			//排除物流公司为空
//			if(map.get("company")==null)
//			{
//				map.put("company","");
//			}
//			//排除运单号为空
//			if(map.get("THE_AWB")==null)
//			{
//				map.put("THE_AWB","");
//			}
//		}
		return orderList;
	}
	
	/**
	 * 获取/查询未分页订单列表,用于订单管理的导出
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOrderList2(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderList = orderDao.getOrderList(parameters);
		DateFormatUtils.timeforDateType(orderList,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderList,"CANCEL_TIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		for(Map<String,Object> map : orderList)
		{
			String orderNumber = (String) map.get("ORDER_NUMBER");
			parameters.put("orderNumber", orderNumber);
			List<Map<String,Object>> goods = orderDao.getOrderGoods2(parameters);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				m.put("goodsPrice", price.doubleValue()*number);
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			//排除用户昵称为空的情况
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME","");
			}
			//排除未支付的情况
			if(map.get("PAY_TYPE")==null)
			{
				map.put("PAY_TYPE","");
			}
			//排除店铺区域为空的情况
			if(map.get("province")==null)
			{
				map.put("province","");
			}
			if(map.get("city")==null)
			{
				map.put("city","");
			}
			if(map.get("a")==null)
			{
				map.put("area","");
			}
			//排除收货地址为空的情况
			if(map.get("userProvince")==null)
			{
				map.put("userProvince","");
			}
			if(map.get("userCity")==null)
			{
				map.put("userCity","");
			}
			if(map.get("userArea")==null)
			{
				map.put("userArea","");
			}
			//排除物流公司为空
			if(map.get("company")==null)
			{
				map.put("company","");
			}
			//排除运单号为空
			if(map.get("THE_AWB")==null)
			{
				map.put("THE_AWB","");
			}
		}
		return orderList;
	}

	/**
	 * 获取/查询已分页订单列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOrderListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderListPage = orderDao.getOrderListPage(parameters);
		DateFormatUtils.timeforDateType(orderListPage,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderListPage,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"CANCEL_TIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		for(Map<String,Object> map : orderListPage)
		{
			String orderNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = orderDao.getOrderGoods(orderNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				m.put("goodsPrice", price.doubleValue()*number);
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			//排除用户昵称为空的情况
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME","");
			}
			//排除未支付的情况
			if(map.get("PAY_TYPE")==null)
			{
				map.put("PAY_TYPE","");
			}
			//排除店铺区域为空的情况
			if(map.get("province")==null)
			{
				map.put("province","");
			}
			if(map.get("city")==null)
			{
				map.put("city","");
			}
			if(map.get("a")==null)
			{
				map.put("area","");
			}
			//排除收货地址为空的情况
			if(map.get("userProvince")==null)
			{
				map.put("userProvince","");
			}
			if(map.get("userCity")==null)
			{
				map.put("userCity","");
			}
			if(map.get("userArea")==null)
			{
				map.put("userArea","");
			}
			//排除物流公司为空
			if(map.get("company")==null)
			{
				map.put("company","");
			}
			//排除运单号为空
			if(map.get("THE_AWB")==null)
			{
				map.put("THE_AWB","");
			}
		}
		return orderListPage;
	}

	/**
	 * 获取物流公司
	 * @return
	 */
	public List<Map<String, Object>> getLogistics()
	{
		return orderDao.getLogistics();
	}

	/**
	 * 通过Id获取订单信息
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getOrderById(String orderId)
	{ 
		Map<String,Object> order = orderDao.getOrderById(orderId);
		String orderNumber = (String) order.get("ORDER_NUMBER");
		List<Map<String,Object>> orderLogisticsList = orderDao.getOrderLogisticsList(orderNumber);//查询此订单包含的物流信息
		
		/**
		 * 根据运单号取出每个订单包含的商品List并根据运单号和物流公司取出物流信息
		 */
		for(Map<String,Object> map : orderLogisticsList)
		{
			String awb = (String) map.get("THE_AWB");
			List<Map<String,Object>> goods = orderDao.getOrderByAWB(awb);
			map.put("goods", goods);
			String company = (String) map.get("COURIER_COMPANY");
			List<Logistics> logistics = LogisticsUtils.getLogisticsInfo(company, awb);
			map.put("logistics", logistics);
		}
		
		order.put("orderLogisticsList", orderLogisticsList);
		return order;
	}

	/**
	 * 通过订单编号获取订单列表
	 * @param orderNumber
	 * @return
	 */
	public List<Map<String, Object>> getOrderByOrderNumber(String orderNumber) 
	{
		List<Map<String,Object>> orderList = orderDao.getOrderByOrderNumber(orderNumber);
		DateFormatUtils.timeforDateType(orderList,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"SHTIME");
		DateFormatUtils.timeforDateType(orderList,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderList,"CANCEL_TIME");
		DateFormatUtils.timeforDateType(orderList,"CXTIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		for(Map<String,Object> map : orderList)
		{
			String oNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = orderDao.getOrderGoods(oNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			if(map.get("LXPHONE")==null)
			{
				map.put("LXPHONE", map.get("PHONE"));
			}
		}
		return orderList;
	}

	/**
	 * 根据店铺id获取店铺信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getStoreInfoById(String storeId)
	{
		return orderDao.getStoreInfoById(storeId);
	}

	/**
	 * 获取卖家信息
	 * @param storeId
	 * @return
	 */
	public Map<String, Object> getStoreUser(String storeId) 
	{
		return orderDao.getStoreUser(storeId);
	}

	/**
	 * 未分页的所有订单
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getAllOrderList(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> orderList = orderDao.getAllOrderList(parameters);
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"SHTIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
//		for(Map<String,Object> map : orderList)
//		{
//			String orderNumber = (String) map.get("ORDER_NUMBER");
//			List<Map<String,Object>> goods = orderDao.getOrderGoods(orderNumber);
//			map.put("goods", goods);
//			double sumPrice = 0;
//			for(Map<String,Object> m : goods)
//			{
//				int number =  (int) m.get("NUMBER");
//				BigDecimal price = (BigDecimal) m.get("PRICE");
//				m.put("goodsPrice", price.doubleValue()*number);
//				sumPrice += price.doubleValue()*number;
//			}
//			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
//			//排除用户昵称为空的情况
//			if(map.get("NICKNAME")==null)
//			{
//				map.put("NICKNAME","");
//			}
//			//排除未支付的情况
//			if(map.get("PAY_TYPE")==null)
//			{
//				map.put("PAY_TYPE","");
//			}
//			//排除店铺区域为空的情况
//			if(map.get("province")==null)
//			{
//				map.put("province","");
//			}
//			if(map.get("city")==null)
//			{
//				map.put("city","");
//			}
//			if(map.get("a")==null)
//			{
//				map.put("area","");
//			}
//			//排除收货地址为空的情况
//			if(map.get("userProvince")==null)
//			{
//				map.put("userProvince","");
//			}
//			if(map.get("userCity")==null)
//			{
//				map.put("userCity","");
//			}
//			if(map.get("userArea")==null)
//			{
//				map.put("userArea","");
//			}
//			//排除物流公司为空
//			if(map.get("company")==null)
//			{
//				map.put("company","");
//			}
//			//排除运单号为空
//			if(map.get("THE_AWB")==null)
//			{
//				map.put("THE_AWB","");
//			}
//		}
		return orderList;
	}
	
	/**
	 * 未分页的所有订单,用于订单导出
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getAllOrderList2(Map<String, Object> parameters) 
	{
		List<Map<String,Object>> orderList = orderDao.getAllOrderList(parameters);
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"SHTIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		for(Map<String,Object> map : orderList)
		{
			String orderNumber = (String) map.get("ORDER_NUMBER");
			parameters.put("orderNumber", orderNumber);
			List<Map<String,Object>> goods = orderDao.getOrderGoods2(parameters);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				m.put("goodsPrice", price.doubleValue()*number);
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			//排除用户昵称为空的情况
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME","");
			}
			//排除未支付的情况
			if(map.get("PAY_TYPE")==null)
			{
				map.put("PAY_TYPE","");
			}
			//排除店铺区域为空的情况
			if(map.get("province")==null)
			{
				map.put("province","");
			}
			if(map.get("city")==null)
			{
				map.put("city","");
			}
			if(map.get("a")==null)
			{
				map.put("area","");
			}
			//排除收货地址为空的情况
			if(map.get("userProvince")==null)
			{
				map.put("userProvince","");
			}
			if(map.get("userCity")==null)
			{
				map.put("userCity","");
			}
			if(map.get("userArea")==null)
			{
				map.put("userArea","");
			}
			//排除物流公司为空
			if(map.get("company")==null)
			{
				map.put("company","");
			}
			//排除运单号为空
			if(map.get("THE_AWB")==null)
			{
				map.put("THE_AWB","");
			}
		}
		return orderList;
	}

	/**
	 * 分页的所有订单
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getAllOrderListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderListPage = orderDao.getAllOrderListPage(parameters);
		DateFormatUtils.timeforDateType(orderListPage,"PAY_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"SHTIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		for(Map<String,Object> map : orderListPage)
		{
			String orderNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = orderDao.getOrderGoods(orderNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				m.put("goodsPrice", price.doubleValue()*number);
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			//排除用户昵称为空的情况
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME","");
			}
			//排除未支付的情况
			if(map.get("PAY_TYPE")==null)
			{
				map.put("PAY_TYPE","");
			}
			//排除店铺区域为空的情况
			if(map.get("province")==null)
			{
				map.put("province","");
			}
			if(map.get("city")==null)
			{
				map.put("city","");
			}
			if(map.get("a")==null)
			{
				map.put("area","");
			}
			//排除收货地址为空的情况
			if(map.get("userProvince")==null)
			{
				map.put("userProvince","");
			}
			if(map.get("userCity")==null)
			{
				map.put("userCity","");
			}
			if(map.get("userArea")==null)
			{
				map.put("userArea","");
			}
			//排除物流公司为空
			if(map.get("company")==null)
			{
				map.put("company","");
			}
			//排除运单号为空
			if(map.get("THE_AWB")==null)
			{
				map.put("THE_AWB","");
			}
		}
		return orderListPage;
	}

	/**
	 * 仲裁
	 * @param parameters
	 */
	public void arbitration(Map<String, Object> parameters)
	{
		orderDao.arbitration(parameters);
	}

	/**
	 * 通过id拿到用户账号的状态（正常/冻结）
	 * @param userId
	 * @return
	 */
	public String getUserState(String userId) 
	{
		return orderDao.getUserState(userId);
	}

	/**
	 * 用户余额
	 * @param merchantId
	 * @return
	 */
	public double getUserMoneys(String userId) 
	{
		return orderDao.getUserMoneys(userId);
	}

	/**
	 * 用户零花钱
	 * @param merchantId
	 * @return
	 */
	public double getUserPocketMoney(String userId) 
	{
		return orderDao.getUserPocketMoney(userId);
	}
	
	/**
	 * 改变用户的余额，包括添加余额流水及收益流水
	 * @param type（1：收入/2：支出）
	 * @param userId
	 * @param money
	 * @param name 操作名称
	 */
	@Transactional
	public void changeUserMoneys(String type,String userId,double money,String name)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("money", money);
		parameters.put("userId", userId);
		orderDao.changeUserMoneys(parameters);//改变用户余额
		//添加余额流水
		addMoneyWater(userId,money,type,name);
		//添加收益流水
		addRunningWater(userId,type,money,name,"1");
	}
	
	/**
	 * 改变用户的零花钱，包括添加零花钱流水及收益流水
	 * @param type（1：收入/2：支出）
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void changeUserPocketMoney(String type,String userId,double money,String name)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("money", money);
		parameters.put("userId", userId);
		orderDao.changeUserPocketMoney(parameters);//改变用户零花钱
		//添加零花钱流水
		addPocketMoneyWater(userId,money,type,name);
		//添加收益流水
		addRunningWater(userId,type,money,name,"0");
	}
	
	/**
	 * 改变用户的消费券，包括添加消费券流水及收益流水
	 * @param type（1：收入/2：支出）
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void changeUserConsumption(String type,String userId,double money,String name)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("money", money);
		parameters.put("userId", userId);
		orderDao.changeUserConsumption(parameters);//改变用户消费券
		//添加消费券流水
		addConsumpationWater(userId,money,type,name);
		//添加收益流水
		addRunningWater(userId,type,money,name,"2");
	}
	
	/**
	 * 改变用户的积分，包括添加积分流水及收益流水
	 * @param type（1：收入/2：支出）
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void changeUserIntegral(String type,String userId,double money,String name)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("money", money);
		parameters.put("userId", userId);
		orderDao.changeUserIntegral(parameters);//改变用户积分
		//添加积分流水
		addIntegralWater(userId,money,type,name);
		//添加收益流水
		addRunningWater(userId,type,money,name,"3");
	}
	
	/**
	 * 改变站长金额，包括添加站长收支流水
	 * @param type（1：收入/2：支出）
	 * @param userId
	 * @param money
	 */
	@Transactional
	public void changeWebMasterMoney(String type,String userId,double money,String name)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("type", type);
		parameters.put("money", money);
		parameters.put("userId", userId);
		orderDao.changeWebMasterMoney(parameters);//改变站长金额
		//添加站长收支流水
		addWebMasterWater(userId,money,type,name);
	}
	
	/**
	 * 添加余额流水
	 * @param userId
	 * @param money
	 * @param type（1：收入/2：支出）
	 * @param name
	 */
	public void addMoneyWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("moneyWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		orderDao.addMoneyWater(parameters);
	}

	/**
	 * 添加零花钱流水
	 * @param userId
	 * @param money
	 * @param type（1：收入/2：支出）
	 * @param name
	 */
	public void addPocketMoneyWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("pocketMoneyWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		orderDao.addPocketMoneyWater(parameters);
	}

	/**
	 * 添加消费券流水
	 * @param userId
	 * @param money
	 * @param type（1：收入/2：支出）
	 * @param name
	 */
	public void addConsumpationWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("consumptionWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		orderDao.addConsumpationWater(parameters);
	}
	
	/**
	 * 添加积分流水
	 * @param userId
	 * @param money
	 * @param type（1：收入/2：支出）
	 * @param name
	 */
	public void addIntegralWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("consumptionWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		orderDao.addIntegralWater(parameters);
	}
	
	/**
	 * 添加站长收支流水
	 * @param userId
	 * @param money
	 * @param type（1：收入/2：支出）
	 * @param name
	 */
	public void addWebMasterWater(String userId, double money, String type, String name) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("webMasterWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("type", type);
		parameters.put("name", name);
		orderDao.addWebMasterWater(parameters);
	}
	
	/**
	 * 添加收益流水
	 * @param userId
	 * @param waterType（1：收入/2：支出）
	 * @param money
	 * @param name
	 * @param moneyType（0：零花钱    1：余额   2：消费券  3：积分）
	 */
	public void addRunningWater(String userId,String waterType,double money,String name,String moneyType) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("runningWaterId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("userId", userId);
		parameters.put("money", money);
		parameters.put("waterType", waterType);
		parameters.put("name", name);
		parameters.put("moneyType", moneyType);
		orderDao.addRunningWater(parameters);
	}
	
	/**
	 * 添加分享奖励流水
	 * @param donUserId（下级用户Id）
	 * @param userId
	 * @param moneys（积分：200）
	 * @param type
	 */
	public void addShareAwardWater(String donUserId,String userId,String moneys,String type)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("shareAwardId", UUID.randomUUID().toString().replaceAll("-", ""));
		parameters.put("donUserId", donUserId);
		parameters.put("userId", userId);
		parameters.put("moneys", moneys);
		parameters.put("type", type);
		orderDao.addShareAwardWater(parameters);
	}
	
	/**
	 * 改变用户的分享奖励金额/积分
	 * @param type（1.奖励金  2.积分）
	 * @param count（数量）
	 */
	public void changeShareReward(String type,double count,String userId)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId",userId);
		parameters.put("type", type);
		parameters.put("count", count);
		orderDao.changeShareReward(parameters);
	}

	/**
	 * 查询订单的所有状态
	 * @return
	 */
	public List<Map<String, Object>> getOrderState() 
	{
		return orderDao.getOrderState();
	}

	/**
	 * 得到所有的商品列表
	 * @return
	 */
	public List<Map<String, Object>> getGoods() 
	{
		return orderDao.getGoods();
	}

	/**
	 * 得到本店所有的商品列表
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getStoreGoods(String storeId) 
	{
		return orderDao.getStoreGoods(storeId);
	}
}
