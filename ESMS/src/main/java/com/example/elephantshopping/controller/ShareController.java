package com.example.elephantshopping.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.ShareService;
import com.example.elephantshopping.service.merchants.GoodsManageService;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("share")
public class ShareController {
	@Autowired
	private ShareService shareService;
	@Autowired
	private GoodsManageService goodsManageService;

	/**
	 * 到商品分析页面
	 * 
	 * @param mav
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("toGoodsShareHtml")
	public ModelAndView togoodsShareHtml(ModelAndView mav, String goodsId) {
		mav.addObject("goods", goodsManageService.queryGoodsInfo(goodsId));
		mav.addObject("comments", shareService.queryCommentsOnGoods(goodsId));
		mav.setViewName("/sharepage/goodsShare");
		return mav;
	}

	/**
	 * 到店铺分析页面
	 */
	@RequestMapping("tostoreShareHtml")
	public ModelAndView tostoreShareHtml(ModelAndView mav, String storeId) {
		mav.addObject("store", shareService.queryStoreInfo(storeId));
		mav.setViewName("/sharepage/storeShare");
		return mav;
	}

	/**
	 * 加载店铺商品
	 */
	@RequestMapping("queryStoreGoodsList")
	@ResponseBody
	public Map<String, Object> queryStoreGoodsList(HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		map.put("GOODS_STATE", "GOODS_STATE_UP");
		return goodsManageService.getGoodsList(map);
	}

	/**
	 * 跳转下载页面
	 */
	@RequestMapping("toDownloadHtml")
	public ModelAndView toDownloadHtml(ModelAndView mav) {
		mav.setViewName("/sharepage/download");
		return mav;
	}
}
