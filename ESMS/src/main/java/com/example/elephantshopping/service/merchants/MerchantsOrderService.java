package com.example.elephantshopping.service.merchants;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.transform.SynchronizedASTTransformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.merchants.MerchantsOrderDao;
import com.example.elephantshopping.entity.Logistics;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.LogisticsUtils;

/**
 * 商家的订单管理Service
 * @author XB
 *
 */
@Service
public class MerchantsOrderService 
{
	@Autowired
	private MerchantsOrderDao merchantsOrderDao;

	/**
	 * 获取/查询未分页订单列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOrderList(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderList = merchantsOrderDao.getOrderList(parameters);
		DateFormatUtils.timeforDateType(orderList,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderList,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderList,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderList,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderList,"CANCEL_TIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderList里面
		 */
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<orderList.size();i++)
		{
			Map<String,Object> map = orderList.get(i);
			String orderNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(orderNumber);
//			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				sumPrice += price.doubleValue()*number;
			}
//			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			
			String bigState = (String) map.get("BIGSTATE");
			if(bigState==null)
			{
				bigState = "";
			}
			if(parameters.get("state").equals("a")&&sumPrice>=5000&&!bigState.equals("2"))
			{
				list.add(map);
			}else if(parameters.get("state").equals("d")&&sumPrice<5000)
			{
				list.add(map);
			}
		}
		orderList.removeAll(list);
		return orderList;
	}

	/**
	 * 获取/查询已分页订单列表
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getOrderListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> orderListPage = merchantsOrderDao.getOrderListPage(parameters);
		DateFormatUtils.timeforDateType(orderListPage,"ORDER_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"PAY_TIME");//转换时间格式
		DateFormatUtils.timeforDateType(orderListPage,"DELIVERY_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"COMMENTS_TIME");
		DateFormatUtils.timeforDateType(orderListPage,"CANCEL_TIME");
		/**
		 * 根据订单编号取出每个订单包含的商品List并加到orderListPage里面
		 */
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<orderListPage.size();i++)
		{
			Map<String,Object> map = orderListPage.get(i);
			String orderNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(orderNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME", "");
			}
			if(map.get("LXPHONE")==null)
			{
				map.put("LXPHONE", map.get("PHONE"));
			}
			String bigState = (String) map.get("BIGSTATE");
			String orderState = (String) map.get("ORDER_STATE");
			if(bigState==null)//如果大额订单审核为空，将其值改为0
			{
				bigState = "0";
			}
			if(orderState.equals("ORDER_STATE_PAY")&&(sumPrice<5000||(sumPrice>=5000&&bigState.equals("2"))))//已付款
			{
				map.put("state", "0");
			}
			else if(orderState.equals("ORDER_STATE_SQQX"))//申请取消
			{
				map.put("state", "1");
			}
			else if(orderState.equals("ORDER_STATE_NO_GOODS"))//待收货
			{
				map.put("state", "2");
			}
			else if(sumPrice>=5000&&bigState.equals("0")&&orderState.equals("ORDER_STATE_PAY"))//大额订单未审核
			{
				map.put("state", "3");
			}
			else if(sumPrice>=5000&&bigState.equals("3")&&orderState.equals("ORDER_STATE_PAY"))//大额订单审核失败
			{
				map.put("state", "4");
			}
			else if(sumPrice>=5000&&bigState.equals("1")&&orderState.equals("ORDER_STATE_PAY"))//大额订单审核中
			{
				map.put("state", "5");
			}
			
			
			if(parameters.get("state").equals("a")&&sumPrice>=5000&&!bigState.equals("2"))
			{
				list.add(map);
			}else if(parameters.get("state").equals("d")&&sumPrice<5000)
			{
				list.add(map);
			}
		}
		orderListPage.removeAll(list);
		List<Map<String,Object>> list2 = new ArrayList<Map<String,Object>>();
		int which = Integer.parseInt(parameters.get("which").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		int length = which+limit;
		if(which+limit>orderListPage.size())
		{
			length = orderListPage.size();
		}
		for(int i=which;i<length;i++)
		{
			list2.add(orderListPage.get(i));
		}
		return list2;
	}

	/**
	 * 获取物流公司
	 * @return
	 */
	public List<Map<String, Object>> getLogistics()
	{
		return merchantsOrderDao.getLogistics();
	}

	/**
	 * 修改订单状态为发货
	 * @param orderId
	 * @param logistics
	 * @param awb
	 */
	public void sendGoods(String orderId, String logistics, String awb)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("orderId", orderId);
		map.put("logistics", logistics);
		map.put("awb", awb);
		merchantsOrderDao.sendGoods(map);
	}

	/**
	 * 取消订单
	 * @param orderId
	 */
	public void cancelOrder(String orderNumber)
	{
		List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(orderNumber);
		for(Map<String,Object> map:goods)
		{
			String orderId = (String) map.get("ORDER_ID");
			merchantsOrderDao.cancelOrder(orderId);
		}
	}

	/**
	 * 通过Id获取订单信息
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> getOrderById(String orderId)
	{ 
		Map<String,Object> order = merchantsOrderDao.getOrderById(orderId);
		String orderNumber = (String) order.get("ORDER_NUMBER");
		List<Map<String,Object>> orderLogisticsList = merchantsOrderDao.getOrderLogisticsList(orderNumber);//查询此订单包含的物流信息
		
		/**
		 * 根据运单号取出每个订单包含的商品List并根据运单号和物流公司取出物流信息
		 */
		for(Map<String,Object> map : orderLogisticsList)
		{
			String awb = (String) map.get("THE_AWB");
			List<Map<String,Object>> goods = merchantsOrderDao.getOrderByAWB(awb);
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
		List<Map<String,Object>> orderList = merchantsOrderDao.getOrderByOrderNumber(orderNumber);
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
			List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(oNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice",new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME", "");
			}
		}
		return orderList;
	}

	/**
	 * 发货时查询订单
	 * @param orderId
	 * @return
	 */
	public List<Map<String, Object>> getOrders(String orderId)
	{
		List<Map<String,Object>> orderList = merchantsOrderDao.getOrders(orderId);
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
			if(map.get("NICKNAME")==null)
			{
				map.put("NICKNAME", "");
			}
			String orderNumber = (String) map.get("ORDER_NUMBER");
			List<Map<String,Object>> goods = merchantsOrderDao.getOrderGoods(orderNumber);
			map.put("goods", goods);
			double sumPrice = 0;
			for(Map<String,Object> m : goods)
			{
				int number =  (int) m.get("NUMBER");
				BigDecimal price = (BigDecimal) m.get("PRICE");
				sumPrice += price.doubleValue()*number;
			}
			map.put("sumPrice", new BigDecimal(sumPrice).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
			String lxPhone = (String) map.get("LXPHONE");//联系电话
			if(lxPhone==null)
			{
				map.put("LXPHONE",(String) map.get("PHONE"));
			}
		}
		return orderList;
	}

	/**
	 * 通过商品Id得到店铺Id
	 * @param goodsId
	 * @return
	 */
	public String getStoreIdByGoodsId(String goodsId) 
	{
		return merchantsOrderDao.getStoreIdByGoodsId(goodsId);
	}

	/**
	 * 减少商品库存
	 * @param goodsId
	 * @param number
	 */
	public void changeGoodsINVENTORY(String goodsId, int number) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("goodsId", goodsId);
		parameters.put("number", number);
		merchantsOrderDao.changeGoodsINVENTORY(parameters);
	}

	/**
	 * 增加商品购买数量
	 * @param goodsId
	 * @param number
	 */
	public void addGoodsPURCHASE_QUANTITY(String goodsId, int number) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("goodsId", goodsId);
		parameters.put("number", number);
		merchantsOrderDao.addGoodsPURCHASE_QUANTITY(parameters);
	}

	/**
	 * 增加店铺月销量
	 * @param storeId
	 * @param number
	 */
	public void addStoreMONTH_SALES(String storeId, int number) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("storeId", storeId);
		parameters.put("number", number);
		merchantsOrderDao.addStoreMONTH_SALES(parameters);
	}

	/**
	 * 店铺今日订单数量加1
	 * @param storeId
	 */
	public void addStoreSUM_BUY_NUMBER(String storeId) 
	{
		merchantsOrderDao.addStoreSUM_BUY_NUMBER(storeId);
	}

	/**
	 * 取出此订单的交易凭证字段
	 * @param orderNumber
	 * @return
	 */
	public String getTradingProof(String orderNumber) 
	{
		return merchantsOrderDao.getTradingProof(orderNumber);
	}

	/**
	 * 将新的交易凭证信息保存到数据库，并修改审核状态为通过
	 * @param orderNumber
	 * @param tradingProof
	 */
	public void saveProofUrl(String orderNumber, String tradingProof) 
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("orderNumber", orderNumber);
		parameters.put("tradingProof", tradingProof);
		merchantsOrderDao.saveProofUrl(parameters);
	}
	
	/**
	 * 得到订单状态
	 * @param orderId
	 * @return
	 */
	public String getOrderState(String orderId) 
	{
		return merchantsOrderDao.getOrderState(orderId);
	}

	/**
	 * 得到某个订单的物流信息
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> gerOrderLogistics(String orderId) 
	{
		return merchantsOrderDao.gerOrderLogistics(orderId);
	}

	/**
	 * 修改订单物流信息
	 * @param parameters
	 */
	public void changeOrderLogistics(Map<String, Object> parameters) 
	{
		merchantsOrderDao.changeOrderLogistics(parameters);
	}

	/**
	 * 得到订单状态
	 * @param orderNumber
	 * @return
	 */
	public String getOrderStateByNumber(String orderNumber) {
		return merchantsOrderDao.getOrderStateByNumber(orderNumber);
	}

	
}
