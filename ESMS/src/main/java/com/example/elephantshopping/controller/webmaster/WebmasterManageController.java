package com.example.elephantshopping.controller.webmaster;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.webmaster.WebmasterManageService;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("webmasterManage")
public class WebmasterManageController {
	@Autowired
	private WebmasterManageService webmasterManageService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 跳转站长管理页面
	 */
	@RequestMapping("toWebmasterManageHtml")
	public ModelAndView toWebmasterManageHtml(ModelAndView mav, HttpServletRequest request) {
		mav.addObject("provincelist", webmasterManageService.queryAreaList("1"));
		mav.addObject("cancelWebmaster", permissionsController.queryPermissions("cancelWebmaster", request));
		mav.addObject("seeWebmasterWater", permissionsController.queryPermissions("seeWebmasterWater", request));
		mav.addObject("addWebmaster", permissionsController.queryPermissions("addWebmaster", request));
		mav.addObject("webmasterSettlement", permissionsController.queryPermissions("webmasterSettlement", request));
		mav.setViewName("/system/webmasterManage/webmasterManage");
		return mav;
	}

	/**
	 * 查询站长数据
	 */
	@RequestMapping("getWebmasterData")
	@ResponseBody
	public Map<String, Object> getWebmasterData(HttpServletRequest request) {
		return webmasterManageService.getWebmasterData(RequestUtils.requestToMap(request));
	}

	/**
	 * 区域联动
	 */
	@RequestMapping("queryAreaList")
	@ResponseBody
	public List<Map<String, Object>> queryAreaList(String pid) {
		return webmasterManageService.queryAreaList(pid);
	}

	/**
	 * 撤销站长
	 */
	@RequestMapping("updateISZZ")
	@ResponseBody
	public int updateISZZ(HttpServletRequest request) {
		return webmasterManageService.updateISZZ(RequestUtils.requestToMap(request));
	}

	/**
	 * 站长结算
	 */
	@RequestMapping("zhanzhangjiesuan")
	@ResponseBody
	public int zhanzhangjiesuan(HttpServletRequest request) {
		return webmasterManageService.zhanzhangjiesuan(RequestUtils.requestToMap(request));
	}

	/**
	 * 跳转结算记录
	 */
	@RequestMapping("tomasterSettlementRecordHtml")
	public ModelAndView tomasterSettlementRecordHtml(ModelAndView mav) {
		mav.addObject("provincelist", webmasterManageService.queryAreaList("1"));
		mav.setViewName("/system/webmasterManage/masterSettlementRecord");
		return mav;
	}

	/**
	 * 查询结算记录list
	 */
	@RequestMapping("masterSettlementRecordList")
	@ResponseBody
	public Map<String, Object> masterSettlementRecordList(HttpServletRequest request) {
		return webmasterManageService.masterSettlementRecordList(RequestUtils.requestToMap(request));
	}

	/**
	 * 跳转查看流水页面
	 * 
	 * @return
	 */
	@RequestMapping("toRunningWaterHtml")
	public ModelAndView toRunningWaterHtml(ModelAndView mav, String userID) {
		mav.addObject("userID", userID);
		mav.setViewName("/system/webmasterManage/runningWater1");
		return mav;
	}

	/**
	 * 跳转查看明细页面
	 */
	@RequestMapping("toSeeDetailsHtml")
	public ModelAndView toSeeDetailsHtml(ModelAndView mav, String month, String userID) {
		mav.addObject("userID", userID);
		mav.addObject("month", month);
		mav.setViewName("/system/webmasterManage/seeDetails1");
		return mav;
	}

	/**
	 * 查询该区域是否存在站长
	 * 
	 * @param areaId
	 * @return
	 */
	@RequestMapping("VerifyThatThereIs")
	@ResponseBody
	public int VerifyThatThereIs(String areaId) {
		return webmasterManageService.VerifyThatThereIs(areaId);
	}

	/**
	 * 查询手机号码
	 */
	@RequestMapping("VerifyUserIsHave")
	@ResponseBody
	public int VerifyUserIsHave(String userPhone) {
		return webmasterManageService.verifyUserIsHave(userPhone);
	}

	/**
	 * 添加站长
	 */
	@RequestMapping("addWebmaster")
	@ResponseBody
	public int addWebmaster(String userphone, String area, String zzyqr) {
		return webmasterManageService.addWebmaster(userphone, area, zzyqr);
	}
}
