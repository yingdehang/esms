package com.example.elephantshopping.controller.goodsManage;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.goodsManage.GoodsService;
import com.example.elephantshopping.service.merchants.GoodsManageService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 商品审核
 *
 */
@Controller
@RequestMapping("goods")
public class GoodsController {

	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsManageService goodsManageService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 跳转公司商品审核页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("togoodsManage")
	public ModelAndView togoodsManage(ModelAndView modelAndView, HttpServletRequest request) {
		// 查询审核权限
		modelAndView.addObject("updategoodsState", permissionsController.queryPermissions("updategoodsState", request));
		modelAndView.setViewName("/system/goodsManage/goodsManage/goodsManage");
		return modelAndView;
	}

	/**
	 * 跳转商品审核记录页面
	 */
	@RequestMapping("tocommodityAuditRecordHtml")
	public ModelAndView tocommodityAuditRecordHtml(ModelAndView mav) {
		// 获取所有商品状态
		mav.setViewName("/system/goodsManage/goodsManage/commodityAuditRecord");
		return mav;
	}

	/**
	 * 获取/查询数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getGoodsList")
	@ResponseBody
	public Map<String, Object> getGoodsList(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return goodsService.getGoodsList(map);
	}

	/**
	 * 查看商品详情
	 */
	@RequestMapping("queryGoodsInfo")
	public ModelAndView queryGoodsInfo(ModelAndView mav, String goodsId) {
		mav.addObject("goodsInfo", goodsManageService.queryGoodsInfo(goodsId));
		mav.setViewName("/system/goodsManage/goodsManage/goodsDetials");
		return mav;
	}

	/**
	 * 修改商品状态
	 */
	@RequestMapping("updategoodsState")
	@ResponseBody
	public int updategoodsState(HttpServletRequest request) {
		return goodsService.updategoodsState(RequestUtils.requestToMap(request));
	}
}
