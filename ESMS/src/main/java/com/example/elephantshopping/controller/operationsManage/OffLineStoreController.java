package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.OffLineStoreService;
import com.example.elephantshopping.service.operationsManage.OnLineStoreService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 线下店铺Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("offLineStore")
public class OffLineStoreController {
	@Autowired
	private OffLineStoreService offLineStoreService;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private OnLineStoreService onLineStoreService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 转到线下店铺审核页面
	 * 
	 * @return
	 */
	@RequestMapping("toOffLineStoreVerify")
	public ModelAndView toOffLineStoreVerify(ModelAndView modelAndView, String userId, HttpServletRequest request) {
		modelAndView.addObject("userId", userId);
		List<Map<String, Object>> storeClass = offLineStoreService.getStoreClass();
		modelAndView.addObject("storeClass", storeClass);// 获取店铺所有分类
		// 添加权限
		modelAndView.addObject("xxStoreVerify", permissionsController.queryPermissions("xxStoreVerify", request));

		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/offLineStoreVerify");
		return modelAndView;
	}

	/**
	 * 获取未认证的线下店铺
	 * 
	 * @return
	 */
	@RequestMapping("getOffLineStore")
	@ResponseBody
	public Map<String, Object> getOffLineStore(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		String storeName = (String) parameters.get("storeName");
		if (storeName != null && (!storeName.equals(""))) {
			parameters.put("storeName", "%" + storeName + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> offLineStore = offLineStoreService.getNoVerifyStore(parameters);// 未认证的线下店铺数据
		List<Map<String, Object>> offLineStorePage = offLineStoreService.getNoVerifyStorePage(parameters);// 未认证的线下店铺数据分页
		for (Map<String, Object> storeInfo : offLineStorePage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", offLineStorePage);
		map.put("count", offLineStore.size());
		return map;
	}

	/**
	 * 转到认证页面
	 * 
	 * @param modelAndView
	 * @param certificationId
	 * @return
	 */
	@RequestMapping("toVerify")
	public ModelAndView toVerify(ModelAndView modelAndView, String certificationId, String userId) {
		Map<String, Object> storeInfo = offLineStoreService.getStoreInfo(certificationId);
		String province = (String) storeInfo.get("PROVINCE");
		String city = (String) storeInfo.get("CITY");
		String area = (String) storeInfo.get("AREA");
		storeInfo.put("area", province + city + area);
		if (storeInfo.get("BUSINESS_LICENSE") == null) {
			storeInfo.put("BUSINESS_LICENSE", "");
		}
		if (storeInfo.get("ID_CARD_UP") == null) {
			storeInfo.put("ID_CARD_UP", "");
		}
		if (storeInfo.get("HEAD_PHOTO") == null) {
			storeInfo.put("HEAD_PHOTO", "");
		}
		if (storeInfo.get("ID_CARD_DON") == null) {
			storeInfo.put("ID_CARD_DON", "");
		}
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("storeInfo", storeInfo);// 获取店铺信息
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/verify");
		return modelAndView;
	}

	/**
	 * 进行认证并转到主页面
	 * 
	 * @return
	 */
	@RequestMapping("verify")
	@Transactional
	public synchronized ModelAndView verify(ModelAndView modelAndView, String result, String certificationId, String userId,
			String content, String storeId, HttpServletRequest request) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("result", result);
		parameters.put("certificationId", certificationId);
		parameters.put("userId", userId);
		if (content != null) {
			parameters.put("content", content);
		}
		offLineStoreService.verify(parameters);
		if (result.equals("1"))// 如果审核通过，改变店铺的状态为正常,添加开店时间
		{
			offLineStoreService.changeStoreStateNormal(storeId);
		}
		// 添加权限
		modelAndView.addObject("xxStoreVerify", permissionsController.queryPermissions("xxStoreVerify", request));

		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/offLineStoreVerify");
		return modelAndView;
	}

	/**
	 * 转到认证记录页面
	 * 
	 * @return
	 */
	@RequestMapping("toRecord")
	public ModelAndView toRecord(ModelAndView modelAndView) {
		List<Map<String, Object>> storeClass = offLineStoreService.getStoreClass();
		modelAndView.addObject("storeClass", storeClass);// 获取店铺所有分类
		List<Map<String, Object>> verifyState = offLineStoreService.getVerifyState();
		modelAndView.addObject("verifyState", verifyState);// 获取店铺认证状态
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/record");
		return modelAndView;
	}

	/**
	 * 获取认证通过和未通过的线下店铺
	 * 
	 * @return
	 */
	@RequestMapping("getVerifyRecord")
	@ResponseBody
	public Map<String, Object> getVerifyRecord(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		String storeName = (String) parameters.get("storeName");
		if (storeName != null && (!storeName.equals(""))) {
			parameters.put("storeName", "%" + storeName + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> verifyRecord = offLineStoreService.getVerifyRecord(parameters);// 获取未分页的认证记录
		List<Map<String, Object>> verifyRecordPage = offLineStoreService.getVerifyRecordPage(parameters);// 获取已分页的认证记录
		for (Map<String, Object> storeInfo : verifyRecordPage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", verifyRecordPage);
		map.put("count", verifyRecord.size());
		return map;
	}

	/**
	 * 转到线下店铺管理页面
	 * 
	 * @return
	 */
	@RequestMapping("toOffLineStoreManage")
	public ModelAndView toOffLineStoreManage(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> storeClass = offLineStoreService.getStoreClass();
		modelAndView.addObject("storeClass", storeClass);// 获取店铺所有分类
		List<Map<String, Object>> storeState = offLineStoreService.getStoreState();
		modelAndView.addObject("storeState", storeState);// 获取店铺状态（正常/冻结）
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		// 添加权限
		modelAndView.addObject("updateXxStore", permissionsController.queryPermissions("updateXxStore", request));
		modelAndView.addObject("changeXxStoreState",
				permissionsController.queryPermissions("changeXxStoreState", request));
		modelAndView.addObject("exportXxStoreWater",
				permissionsController.queryPermissions("exportXxStoreWater", request));
		// 统计系统添加店铺等级查询参数
		// 统计系统跳转带的参数：storeDJ
		Map<String, Object> map = RequestUtils.requestToMap(request);
		modelAndView.addObject("storeDJ", map.get("storeDJ"));
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/offLineStoreManage");
		return modelAndView;
	}

	/**
	 * 获取通过认证的线下店铺
	 * 
	 * @return
	 */
	@RequestMapping("getOffLineStoreList")
	@ResponseBody
	public Map<String, Object> getOffLineStoreList(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		String storeName = (String) parameters.get("storeName");
		if (storeName != null && (!storeName.equals(""))) {
			parameters.put("storeName", "%" + storeName + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		if (parameters.get("page").equals("") || parameters.get("page").equals("null")) {
			parameters.put("page", 1);
		}
		if (parameters.get("limit").equals("") || parameters.get("limit").equals("null")) {
			parameters.put("limit", 10);
		}
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> offLineStoreList = offLineStoreService.getOffLineStoreList(parameters);// 通过认证的线下店铺数据未分页
		List<Map<String, Object>> offLineStoreListPage = offLineStoreService.getOffLineStoreListPage(parameters);// 通过认证的线下店铺数据已分页
		for (Map<String, Object> storeInfo : offLineStoreListPage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", offLineStoreListPage);
		map.put("count", offLineStoreList.size());
		return map;
	}

	/**
	 * 转换店铺状态（正常/冻结）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeState")
	@ResponseBody
	public int changeState(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.changeState(parameters);
		return 0;
	}

	/**
	 * 转到线下店铺详情页面
	 * 
	 * @return
	 */
	@RequestMapping("toDetails")
	public ModelAndView toDetails(ModelAndView modelAndView, String id) {
		Map<String, Object> details = offLineStoreService.getStoreInfo(id);
		String province = (String) details.get("PROVINCE");
		String city = (String) details.get("CITY");
		String area = (String) details.get("AREA");
		details.put("area", province + city + area);
		if (details.get("BUSINESS_LICENSE") == null) {
			details.put("BUSINESS_LICENSE", "");
		}
		if (details.get("ID_CARD_UP") == null) {
			details.put("ID_CARD_UP", "");
		}
		if (details.get("HEAD_PHOTO") == null) {
			details.put("HEAD_PHOTO", "");
		}
		if (details.get("ID_CARD_DON") == null) {
			details.put("ID_CARD_DON", "");
		}
		if (details.get("WAITING_STATE") == null) {
			details.put("WAITING_STATE", "");
		}
		modelAndView.addObject("details", details);
		List<Map<String, Object>> storeClass = offLineStoreService.getStoreClass();
		modelAndView.addObject("storeClass", storeClass);// 获取店铺所有分类
		List<Map<String, Object>> storeLevel = offLineStoreService.getStoreLevel();
		modelAndView.addObject("storeLevel", storeLevel);// 获取店铺所有等级
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/details");
		return modelAndView;
	}

	/**
	 * 改变店铺名字
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeName")
	@ResponseBody
	public int changeName(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.changeName(parameters);
		return 0;
	}

	/**
	 * 改变店铺详细地址
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeAddr")
	@ResponseBody
	public int changeAddr(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.changeAddr(parameters);
		return 0;
	}

	/**
	 * 改变店铺分类
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeClass")
	@ResponseBody
	public int changeClass(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.changeClass(parameters);
		return 0;
	}

	/**
	 * 改变店铺客服电话
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeServicePhone")
	@ResponseBody
	public int changeServicePhone(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.changeServicePhone(parameters);
		return 0;
	}

	/**
	 * 为店铺添加一个待改变的等级
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addNewLevel")
	@ResponseBody
	public int addNewLevel(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		offLineStoreService.addNewLevel(parameters);
		return 0;
	}

	/**
	 * 根据查询条件查出有多少个店铺并返回
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("toExport")
	@ResponseBody
	public int toExport(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		String storeName = (String) parameters.get("storeName");
		if (storeName != null && (!storeName.equals(""))) {
			parameters.put("storeName", "%" + storeName + "%");
		}
		List<Map<String, Object>> offLineStoreList = offLineStoreService.getOffLineStoreList(parameters);
		return offLineStoreList.size();
	}

	/**
	 * 导出
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("export")
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		String storeName = (String) parameters.get("storeName");
		if (storeName != null && (!storeName.equals(""))) {
			parameters.put("storeName", "%" + storeName + "%");
		}
		// 根据条件查询出店铺的信息
		List<Map<String, Object>> offLineStoreList = offLineStoreService.getOffLineStoreList(parameters);
		for (Map<String, Object> map : offLineStoreList) {
			String storeId = (String) map.get("STORE_ID");
			String userId = (String) map.get("USERS_ID");
			List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
			// 按月查询
			if (parameters.get("timeType").equals("month")) {
				String month = (String) parameters.get("month");
				Map<String, Object> monthWater = offLineStoreService.getStoreWater(storeId, month, userId);// 查询线下店铺某月的收益
				waters.add(monthWater);
			}
			// 按日查询
			else {
				String startTime = (String) parameters.get("startTime");
				String endTime = (String) parameters.get("endTime");
				List<String> days = DateFormatUtils.getDays(startTime, endTime);
				for (String day : days) {
					Map<String, Object> dayWater = offLineStoreService.getStoreWater(storeId, day, userId);// 查询线下店铺某天的收益
					waters.add(dayWater);
				}
			}
			map.put("waters", waters);
		}
		HSSFWorkbook wb = offLineStoreExport(offLineStoreList);

		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}

	/**
	 * 根据传来的集合创建表格，并返回HSSFWorkbook对象
	 * 
	 * @param onLineStoreList
	 * @return
	 */
	public HSSFWorkbook offLineStoreExport(List<Map<String, Object>> offLineStoreList) {
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("线下店铺流水");
		// 表头
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue("店铺名");
		HSSFCell cell1 = row0.createCell(1);
		cell1.setCellValue("店铺分类");
		HSSFCell cell2 = row0.createCell(2);
		cell2.setCellValue("店铺等级");
		HSSFCell cell3 = row0.createCell(3);
		cell3.setCellValue("店主信息");
		HSSFCell cell4 = row0.createCell(4);
		cell4.setCellValue("所在区域");

		HSSFCell cell5 = row0.createCell(5);
		cell5.setCellValue("站长信息");
		HSSFCell cell6 = row0.createCell(6);
		cell6.setCellValue("时间");
		HSSFCell cell7 = row0.createCell(7);
		cell7.setCellValue("收款流水");
		HSSFCell cell8 = row0.createCell(8);
		cell8.setCellValue("收款佣金");
		HSSFCell cell9 = row0.createCell(9);
		cell9.setCellValue("报单流水");
		HSSFCell cell10 = row0.createCell(10);
		cell10.setCellValue("报单佣金");
		HSSFCell cell11 = row0.createCell(11);
		cell11.setCellValue("合计佣金");
		int count = 1;// 第几行
		for (int i = 0; i < offLineStoreList.size(); i++) {
			List<Map<String, Object>> waters = (List<Map<String, Object>>) offLineStoreList.get(i).get("waters");
			for (int j = 0; j < waters.size(); j++) {
				HSSFRow row1 = sheet.createRow(count);
				row1.createCell(0).setCellValue(offLineStoreList.get(i).get("STORE_NAME").toString());
				row1.createCell(1).setCellValue(offLineStoreList.get(i).get("STORE_CLASS").toString());
				row1.createCell(2).setCellValue(offLineStoreList.get(i).get("LEVELS").toString());
				if (offLineStoreList.get(i).get("UC_NAMES") == null
						|| offLineStoreList.get(i).get("userPhone") == null) {
					row1.createCell(3).setCellValue("");
				} else {
					row1.createCell(3).setCellValue(offLineStoreList.get(i).get("UC_NAMES").toString() + "("
							+ offLineStoreList.get(i).get("PHONE").toString() + ")");
				}
				if (offLineStoreList.get(i).get("PROVINCE") == null || offLineStoreList.get(i).get("CITY") == null
						|| offLineStoreList.get(i).get("AREA") == null) {
					row1.createCell(4).setCellValue("");
				} else {
					row1.createCell(4)
							.setCellValue(offLineStoreList.get(i).get("PROVINCE").toString()
									+ offLineStoreList.get(i).get("CITY").toString()
									+ offLineStoreList.get(i).get("AREA").toString());
				}
				if (offLineStoreList.get(i).get("zzName") == null || offLineStoreList.get(i).get("zzPhone") == null) {
					row1.createCell(5).setCellValue("");
				} else {
					row1.createCell(5).setCellValue(
							offLineStoreList.get(i).get("zzName") + "(" + offLineStoreList.get(i).get("zzPhone") + ")");
				}
				row1.createCell(6).setCellValue(waters.get(j).get("time").toString());
				row1.createCell(7).setCellValue(waters.get(j).get("gMoney").toString());
				row1.createCell(8).setCellValue(waters.get(j).get("gCommission").toString());
				row1.createCell(9).setCellValue(waters.get(j).get("dMoney").toString());
				row1.createCell(10).setCellValue(waters.get(j).get("dCommission").toString());
				row1.createCell(11).setCellValue(waters.get(j).get("allCommission").toString());
				count++;// 行数加一
			}
		}
		return wb;
	}

	/**
	 * 转到线下店铺月流水页面
	 * 
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toMonthWater")
	public ModelAndView toMonthWater(ModelAndView modelAndView, String storeId) {
		modelAndView.addObject("storeId", storeId);
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/offLineStoreMonthWater");
		return modelAndView;
	}

	/**
	 * 获得一个店铺的月流水数据
	 * 
	 * @param storeId
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("getOffLineStoreMonthWater")
	@ResponseBody
	public Map<String, Object> getOffLineStoreMonthWater(String storeId, int page, int limit) {
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listPage = new ArrayList<Map<String, Object>>();
		int which = (page - 1) * limit;
		String openTime = onLineStoreService.getOpenTime(storeId);// 得到店铺开店时间
		String startMonth = openTime.substring(0, 7);// 开店时的月份
		String nowMonth = new SimpleDateFormat("yyyy-MM").format(new Date());// 当前月份
		List<String> month = DateFormatUtils.getMonth(startMonth, nowMonth);// 从开店到现在的所有月份
		// 未分页数据
		for (String m : month) {
			Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
			String userId = (String) storeInfo.get("USERS_ID");// 店主Id
			storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("userPhone") + ")");
			storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
			Map<String, Object> water = offLineStoreService.getStoreWater(storeId, m, userId);// 查询每个月的订单流水
			storeInfo.put("gMoney", water.get("gMoney"));
			storeInfo.put("gCommission", water.get("gCommission"));
			storeInfo.put("dMoney", water.get("dMoney"));
			storeInfo.put("dCommission", water.get("dCommission"));
			storeInfo.put("allCommission", water.get("allCommission"));
			storeInfo.put("time", m);
			list.add(storeInfo);
		}
		// 分页数据
		for (int i = which; i < which + limit; i++) {
			if (month.size() > i) {
				Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
				String userId = (String) storeInfo.get("USERS_ID");// 店主Id
				storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("userPhone") + ")");
				storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
				String province = (String) storeInfo.get("PROVINCE");
				String city = (String) storeInfo.get("CITY");
				String area = (String) storeInfo.get("AREA");
				storeInfo.put("area", province + city + area);
				Map<String, Object> water = offLineStoreService.getStoreWater(storeId, month.get(i), userId);// 查询每个月的订单流水
				storeInfo.put("gMoney", water.get("gMoney"));
				storeInfo.put("gCommission", water.get("gCommission"));
				storeInfo.put("dMoney", water.get("dMoney"));
				storeInfo.put("dCommission", water.get("dCommission"));
				storeInfo.put("allCommission", water.get("allCommission"));
				storeInfo.put("time", month.get(i));
				listPage.add(storeInfo);
			}
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", listPage);
		map.put("count", list.size());
		return map;
	}

	/**
	 * 转到线下店铺日流水页面
	 * 
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toDayWater")
	public ModelAndView toDayWater(ModelAndView modelAndView, String storeId, String month) {
		modelAndView.addObject("storeId", storeId);
		modelAndView.addObject("month", month);
		modelAndView.setViewName("/system/operationsManage/offLineStoreManage/offLineStoreDayWater");
		return modelAndView;
	}

	/**
	 * 获得一个月的日流水数据
	 * 
	 * @param storeId
	 * @param month
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("getOffLineStoreDayWater")
	@ResponseBody
	public Map<String, Object> getOffLineStoreDayWater(String storeId, String month, int page, int limit) {
		String startDay = "";// 此月第一天
		String endDay = "";// 此月最后一天
		String openTime = onLineStoreService.getOpenTime(storeId);// 得到店铺开店时间
		String startMonth = openTime.substring(0, 7);// 开店时的月份
		String nowMonth = new SimpleDateFormat("yyyy-MM").format(new Date());// 现在的月份
		String nowDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());// 现在的日期
		if (month.equals(startMonth))// 如果开店的月份就是month代表的月份
		{
			startDay = openTime.substring(0, 10);// 第一天就是开店的那天
		} else {
			startDay = month + "-01";// 第一天就是此月的第一天
		}
		if (month.equals(nowMonth))// 如果month代表的月份就是现在的月份
		{
			endDay = nowDay;// 最后一天就是今天
		} else {
			endDay = DateFormatUtils.getLastDayOfMonth(month);// 最后一天就是此月最后一天
		}

		List<String> days = DateFormatUtils.getDays(startDay, endDay);// 根据开始日期和结束日期得到需要查询的日期集合

		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> listPage = new ArrayList<Map<String, Object>>();
		int which = (page - 1) * limit;
		// 未分页数据
		for (String d : days) {
			Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
			String userId = (String) storeInfo.get("USERS_ID");// 店主Id
			storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("userPhone") + ")");
			storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
			Map<String, Object> water = offLineStoreService.getStoreWater(storeId, d, userId);// 查询每天的订单流水
			storeInfo.put("gMoney", water.get("gMoney"));
			storeInfo.put("gCommission", water.get("gCommission"));
			storeInfo.put("dMoney", water.get("dMoney"));
			storeInfo.put("dCommission", water.get("dCommission"));
			storeInfo.put("allCommission", water.get("allCommission"));
			storeInfo.put("time", d);
			list.add(storeInfo);
		}
		// 分页数据
		for (int i = which; i < which + limit; i++) {
			if (days.size() > i) {
				Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
				String userId = (String) storeInfo.get("USERS_ID");// 店主Id
				storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("userPhone") + ")");
				storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
				String province = (String) storeInfo.get("PROVINCE");
				String city = (String) storeInfo.get("CITY");
				String area = (String) storeInfo.get("AREA");
				storeInfo.put("area", province + city + area);
				Map<String, Object> water = offLineStoreService.getStoreWater(storeId, days.get(i), userId);// 查询每天的订单流水
				storeInfo.put("gMoney", water.get("gMoney"));
				storeInfo.put("gCommission", water.get("gCommission"));
				storeInfo.put("dMoney", water.get("dMoney"));
				storeInfo.put("dCommission", water.get("dCommission"));
				storeInfo.put("allCommission", water.get("allCommission"));
				storeInfo.put("time", days.get(i));
				listPage.add(storeInfo);
			}
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", listPage);
		map.put("count", list.size());
		return map;
	}

	/**
	 * 导出某几个月的流水
	 * 
	 * @param response
	 * @param monthArray
	 * @param storeId
	 * @throws IOException
	 */
	@RequestMapping("exportMonthWater")
	public void exportMonthWater(HttpServletResponse response, String[] monthArray, String storeId, String userId)
			throws IOException {
		List<Map<String, Object>> offLineStoreList = new ArrayList<Map<String, Object>>();
		for (String m : monthArray) {
			Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
			List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
			Map<String, Object> monthWater = offLineStoreService.getStoreWater(storeId, m, userId);// 查询店铺某月的APP支付收益
			waters.add(monthWater);
			storeInfo.put("waters", waters);
			offLineStoreList.add(storeInfo);
		}

		HSSFWorkbook wb = offLineStoreExport(offLineStoreList);

		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}

	/**
	 * 导出某几天的流水
	 * 
	 * @param response
	 * @param monthArray
	 * @param storeId
	 * @throws IOException
	 */
	@RequestMapping("exportDayWater")
	public void exportDayWater(HttpServletResponse response, String[] dayArray, String storeId, String userId)
			throws IOException {
		List<Map<String, Object>> offLineStoreList = new ArrayList<Map<String, Object>>();
		Map<String, Object> storeInfo = offLineStoreService.getOffLineStoreInfoById(storeId);// 得到店铺的信息
		List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
		for (String day : dayArray) {
			Map<String, Object> dayWater = offLineStoreService.getStoreWater(storeId, day, userId);// 查询店铺某天的APP支付收益
			waters.add(dayWater);
		}
		storeInfo.put("waters", waters);
		offLineStoreList.add(storeInfo);

		HSSFWorkbook wb = offLineStoreExport(offLineStoreList);

		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}
}
