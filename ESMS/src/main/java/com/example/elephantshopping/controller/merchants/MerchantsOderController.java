package com.example.elephantshopping.controller.merchants;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.merchants.MerchantsOrderService;
import com.example.elephantshopping.service.operationsManage.OrderService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 商家的订单管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("merchantsOrder")
public class MerchantsOderController
{
	@Autowired
	private MerchantsOrderService merchantsOrderService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PermissionsController permissionsController;
	
	/**
	 * 转到订单处理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderDispose")
	public ModelAndView toOrderDispose(ModelAndView modelAndView,String userId,HttpServletRequest request)
	{
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("which", "1");//页面加载时显示id=1的选项卡（等待发货的订单）
		modelAndView.setViewName("/system/merchants/orderManage/orderDispose");
		List<Map<String,Object>> logistics = merchantsOrderService.getLogistics();//获取物流公司
		modelAndView.addObject("logistics", logistics);
		//查询权限
		modelAndView.addObject("delivery",permissionsController.queryPermissions("delivery", request));
		modelAndView.addObject("bulkShipment",permissionsController.queryPermissions("bulkShipment", request));
		return modelAndView;
	}
	
	/**
	 * 转到订单管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderManage")
	public ModelAndView toOrderManage(ModelAndView modelAndView,String userId)
	{
		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/merchants/orderManage/orderManage");
		return modelAndView;
	}
	
	/**
	 * 获取/查询订单列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getOrders")
	@ResponseBody
	public Map<String,Object> getOrders(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		if(parameters.get("time")!=null&&(!parameters.get("time").equals("")))
		{
			String orderTimeStart = parameters.get("time").toString().substring(0, 19);
			String orderTimeEnd = parameters.get("time").toString().substring(22, 41);
			parameters.put("orderTimeStart", orderTimeStart);
			parameters.put("orderTimeEnd", orderTimeEnd);
		}
		List<Map<String,Object>> orderList = merchantsOrderService.getOrderList(parameters);//未分页的数据List
		List<Map<String,Object>> orderListPage = merchantsOrderService.getOrderListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",orderListPage);
		map.put("count",orderList.size());
		return map;
	}
	
	/**
	 * 转到确认发货页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toSendGoods")
	public ModelAndView toSendGoods(ModelAndView modelAndView,@RequestParam(value = "orderIds") String[] orderIds)
	{
		List<Map<String,Object>> orders = new ArrayList<Map<String,Object>>();
		for(int i=0;i<orderIds.length;i++)
		{
			orders.add(merchantsOrderService.getOrders(orderIds[i]).get(0));
		}
		modelAndView.addObject("orders", orders);//订单列表
		
		String addr = (String)orders.get(0).get("p")+orders.get(0).get("c")+orders.get(0).get("a")+orders.get(0).get("ADDR");//收货地址
		String contact = (String) orders.get(0).get("CONTACT");//收货人
		String lxPhone = (String) orders.get(0).get("LXPHONE");//联系电话
		boolean differentAddress = false;
		for(Map<String,Object> map:orders)
		{
			String a = (String) map.get("p")+map.get("c")+map.get("a")+map.get("ADDR");
			String b = (String) map.get("CONTACT");
			String c = (String) map.get("LXPHONE");
			if(!a.equals(addr)||!b.equals(contact)||!c.equals(lxPhone))
			{
				differentAddress = true;
			}
		}
		modelAndView.addObject("differentAddress", differentAddress);//是否重复地址
		modelAndView.addObject("addr", addr);
		modelAndView.addObject("contact", contact+"("+lxPhone+")");
		
		List<Map<String,Object>> logistics = merchantsOrderService.getLogistics();//获取物流公司
		modelAndView.addObject("logistics", logistics);
		modelAndView.setViewName("/system/merchants/orderManage/sendGoods");
		return modelAndView;
	}
	
	/**
	 * 转到确认发货页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toSendGoods2")
	public ModelAndView toSendGoods2(ModelAndView modelAndView,@RequestParam(value = "orderId") String orderId)
	{
		List<Map<String,Object>> orders = new ArrayList<Map<String,Object>>();
		orders = merchantsOrderService.getOrders(orderId);
		modelAndView.addObject("orders", orders);//订单列表
		
		String addr = (String)orders.get(0).get("p")+orders.get(0).get("c")+orders.get(0).get("a")+orders.get(0).get("ADDR");//收货地址
		String contact = (String) orders.get(0).get("CONTACT");//收货人
		String lxPhone = (String) orders.get(0).get("LXPHONE");//联系电话
		boolean differentAddress = false;
		for(Map<String,Object> map:orders)
		{
			String a = (String) map.get("p")+map.get("c")+map.get("a")+map.get("ADDR");
			String b = (String) map.get("CONTACT");
			String c = (String) map.get("LXPHONE");
			
			if(!a.equals(addr)||!b.equals(contact)||!c.equals(lxPhone))
			{
				differentAddress = true;
			}
		}
		modelAndView.addObject("differentAddress", differentAddress);//是否重复地址
		modelAndView.addObject("addr", addr);
		modelAndView.addObject("contact", contact+"("+lxPhone+")");
		
		List<Map<String,Object>> logistics = merchantsOrderService.getLogistics();//获取物流公司
		modelAndView.addObject("logistics", logistics);
		modelAndView.setViewName("/system/merchants/orderManage/sendGoods");
		return modelAndView;
	}
	
	/**
	 * 发货
	 * @return
	 */
	@RequestMapping("sendGoods")
	@ResponseBody
	@Transactional
	public synchronized String sendGoods(@RequestParam(value = "orderId[]") String[] orderId,String logistics,String awb)
	{
		String orderState = merchantsOrderService.getOrderState(orderId[0]);//得到订单状态
		if(orderState.equals("ORDER_STATE_PAY")){//判断订单是否已发货，防止重复发货
			if(orderState.equals("ORDER_STATE_QX"))//如果订单已取消，不能发货
			{
				return "此订单已自动取消,不能发货";
			}
			for(int i=0;i<orderId.length;i++)
			{
				if(logistics==null){
					logistics="";
				}
				if(awb==null){
					awb="";
				}
				merchantsOrderService.sendGoods(orderId[i],logistics,awb);//改变订单状态为发货
				//改变商品库存，购买数量
				Map<String,Object> orderInfo = merchantsOrderService.getOrderById(orderId[i]);//订单信息
				int number = (int) orderInfo.get("NUMBER");//获取订单的商品数量
				String goodsId = (String) orderInfo.get("GOODSID");//获取订单的商品Id
				merchantsOrderService.changeGoodsINVENTORY(goodsId,number);//减少商品库存
				merchantsOrderService.addGoodsPURCHASE_QUANTITY(goodsId,number);//增加商品购买数量
				//改变店铺今日订单数量，月销量
				String storeId = merchantsOrderService.getStoreIdByGoodsId(goodsId);//通过商品Id得到店铺Id
				merchantsOrderService.addStoreMONTH_SALES(storeId, number);//增加店铺月销量
				merchantsOrderService.addStoreSUM_BUY_NUMBER(storeId);//店铺今日订单数量加1
			}
		}
		return "ok";
	}
	
	/**
	 * 取消订单
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("cancelOrder")
	public synchronized ModelAndView cancelOrder(ModelAndView modelAndView,String orderNumber,String userId,double money,String payType)
	{
		String orderState = merchantsOrderService.getOrderStateByNumber(orderNumber);//得到订单状态
		if(orderState.equals("ORDER_STATE_SQQX")){//判断订单是否申请取消，防止重复取消
			//将订单金额返还给买家
			if(payType.equals("0"))//零花钱
			{
				orderService.changeUserPocketMoney("1", userId, money, "订单取消");
			}
			else if(payType.equals("1"))//余额
			{
				orderService.changeUserMoneys("1", userId, money, "订单取消");
			}
			else//消费券
			{
				orderService.changeUserConsumption("1", userId, money, "订单取消");
			}
		}
		merchantsOrderService.cancelOrder(orderNumber);
		modelAndView.setViewName("/system/merchants/orderManage/orderDispose");
		modelAndView.addObject("which", "2");//页面加载时显示id=2的选项卡（申请取消的订单）
		modelAndView.addObject("userId",userId);
		return modelAndView;
	} 
	
	/**
	 * 转到订单详情页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderDetails")
	public ModelAndView toOrderDetails(ModelAndView modelAndView,String orderNumber)
	{
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("orderNumber", orderNumber);
		List<Map<String,Object>> orderList = merchantsOrderService.getOrderByOrderNumber(orderNumber);
		modelAndView.addObject("order", orderList.get(0));
		modelAndView.setViewName("/system/merchants/orderManage/orderDetails");
		return modelAndView;
	}
	
	/**
	 * 转到物流信息页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOrderLogistics")
	public ModelAndView toOrderLogistics(ModelAndView modelAndView,String orderId)
	{
		Map<String,Object> order = merchantsOrderService.getOrderById(orderId);
		modelAndView.addObject("order",order);
		modelAndView.setViewName("/system/merchants/orderManage/orderLogistics");
		return modelAndView;
	}
	
	/**
	 * 转到交易凭证上传页面
	 * @param modelAndView
	 * @param orderNumber
	 * @return
	 */
	@RequestMapping("toTradingProof")		
	public ModelAndView toTradingProof(ModelAndView modelAndView,String orderNumber)
	{
		List<Map<String,Object>> orderList = merchantsOrderService.getOrderByOrderNumber(orderNumber);
		modelAndView.addObject("order", orderList.get(0));
		modelAndView.setViewName("/system/merchants/orderManage/tradingProof");
		return modelAndView;
	}
	
	/**
	 * 上传交易凭证
	 * @param file
	 * @param storeId
	 * @return
	 */
	@RequestMapping("uploadTradingProof")
	@ResponseBody
	public synchronized Map<String,Object> uploadTradingProof(MultipartFile file)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String url;
		try {
			url = UploadFileUtils.uploadFile(file,"store");
			map.put("url",url);
			map.put("code",0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 保存大额订单交易凭证的地址
	 * @param modelAndView
	 * @param orderNumber
	 * @param tradingProof
	 * @return
	 */
	@RequestMapping("saveProofUrl")	
	@ResponseBody
	public int saveProofUrl(String orderNumber,String tradingProof)
	{
		//取出此订单的交易凭证字段
		String tp = merchantsOrderService.getTradingProof(orderNumber);
		if(tp!=null)//删除服务器中的交易凭证图片
		{
			String[] photos = tp.split(",");
			for(String s:photos)
			{
				UploadFileUtils.deleteFile(s);
			}
		}
		//将新的交易凭证信息保存到数据库，并修改审核状态为通过
		merchantsOrderService.saveProofUrl(orderNumber,tradingProof);
		return 0;
	}
	
	/**
	 * 得到某个订单的物流信息
	 * @param orderId
	 * @return
	 */
	@RequestMapping("gerOrderLogistics")	
	@ResponseBody
	public Map<String,Object> gerOrderLogistics(String orderId)
	{
		Map<String,Object> map = merchantsOrderService.gerOrderLogistics(orderId);
		if(map.get("COURIER_COMPANY")==null)
		{
			map.put("COURIER_COMPANY", "");
		}
		if(map.get("THE_AWB")==null)
		{
			map.put("THE_AWB", "");
		}
		return map;
	}
	
	/**
	 * 修改订单物流信息
	 * @param request
	 * @return
	 */
	@RequestMapping("changeOrderLogistics")	
	@ResponseBody
	public int changeOrderLogistics(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		merchantsOrderService.changeOrderLogistics(parameters);
		return 0;
	}
}
