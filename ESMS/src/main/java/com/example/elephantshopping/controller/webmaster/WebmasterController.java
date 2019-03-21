package com.example.elephantshopping.controller.webmaster;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.webmaster.WebmasterService;
import com.example.elephantshopping.utils.CookiesUtils;
import com.example.elephantshopping.utils.RequestUtils;

@Controller
@RequestMapping("webmaster")
public class WebmasterController {
	@Autowired
	private WebmasterService webmasterService;

	/**
	 * 跳转站长主页
	 */
	@RequestMapping("toWebmasterHtml")
	public ModelAndView toWebmasterHtml(ModelAndView mav, HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		mav.addObject("masterInfo", webmasterService.getWebmasterInfo(map, userId));
		mav.setViewName("/system/webmaster/webmasterHome");
		return mav;
	}

	/**
	 * 跳转用户数据
	 */
	@RequestMapping("toUserDataHtml")
	public ModelAndView toUserDataHtml(ModelAndView mav, HttpServletRequest request) {
		mav.setViewName("/system/webmaster/userData");
		return mav;
	}

	/**
	 * 跳转店铺数据
	 */
	@RequestMapping("toStoreDataHtml")
	public ModelAndView toStoreDataHtml(ModelAndView mav) {
		mav.setViewName("/system/webmaster/storeData");
		return mav;
	}

	/**
	 * 跳转账户数据
	 */
	@RequestMapping("toAccountDataHtml")
	public ModelAndView toAccountDataHtml(ModelAndView mav, HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		mav.addObject("webmaster", webmasterService.getWebmasterInfo(userId));
		mav.setViewName("/system/webmaster/accountData");
		return mav;
	}

	/**
	 * 跳转查看流水页面
	 * 
	 * @return
	 */
	@RequestMapping("toRunningWaterHtml")
	public ModelAndView toRunningWaterHtml(ModelAndView mav) {
		mav.setViewName("/system/webmaster/runningWater");
		return mav;
	}

	/**
	 * 跳转查看流水页面
	 * 
	 * @return
	 */
	@RequestMapping("toRunningWaterHtml1")
	public ModelAndView toRunningWaterHtml1(ModelAndView mav) {
		mav.setViewName("/system/webmaster/runningWater1");
		return mav;
	}

	/**
	 * 跳转查看明细页面
	 */
	@RequestMapping("toSeeDetailsHtml")
	public ModelAndView toSeeDetailsHtml(ModelAndView mav, String month) {
		mav.addObject("month", month);
		mav.setViewName("/system/webmaster/seeDetails");
		return mav;
	}

	/**
	 * 查询月流水
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("getdayWaterlist")
	@ResponseBody
	public Map<String, Object> getdayWaterlist(String month, String userID, HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		if (null != userID && (!userID.equals(""))) {
			userId = userID;
		}
		return webmasterService.getdayWaterlist(month, userId);
	}

	/**
	 * 站长每月流水明细数据导出
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getDetailswaterDaochu")
	public void getDetailswaterDaochu(String days, String userID, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> ma = RequestUtils.requestToMap(request);
		if (null != ma.get("userID") && (!"".equals(ma.get("userID")))) {
			userId = ma.get("userID").toString();
		}
		List<Map<String, Object>> list = webmasterService.getDetailswaterDaochu(days, userId);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("账户月流水明细一览表");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		// 设置单元格内容
		cell.setCellValue("账户月流水明细一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("年、月、日");
		row2.createCell(1).setCellValue("结算时间");
		row2.createCell(2).setCellValue("线下流水");
		row2.createCell(3).setCellValue("线上流水");
		row2.createCell(4).setCellValue("当日总流水");
		row2.createCell(5).setCellValue("当日收益");
		for (int i = 0; i < list.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			Map<String, Object> map = list.get(i);
			row3.createCell(0).setCellValue(map.get("date").toString());
			row3.createCell(1).setCellValue(map.get("endTime").toString());
			row3.createCell(2).setCellValue(map.get("offlineWater").toString());
			row3.createCell(3).setCellValue(map.get("onlineWater").toString());
			row3.createCell(4).setCellValue(map.get("countWater").toString());
			row3.createCell(5).setCellValue(map.get("monthlyIncome").toString());
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
	 * 获取站长每月流水数据
	 */
	@RequestMapping("getRunningwaterlist")
	@ResponseBody
	public Map<String, Object> getRunningwaterlist(HttpServletRequest request, String userID) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		if (null != userID && (!userID.equals(""))) {
			userId = userID;
		}
		return webmasterService.getRunningwaterlist(userId);
	}

	/**
	 * 站长每月流水数据导出
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getRunningwaterDaochu")
	public void getRunningwaterDaochu(String indexs, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> ma = RequestUtils.requestToMap(request);
		if (null != ma.get("userID") && (!"".equals(ma.get("userID")))) {
			userId = ma.get("userID").toString();
		}
		List<Map<String, Object>> list = webmasterService.getRunningwaterDaochu(indexs, userId);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("账户流水一览表");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		// 设置单元格内容
		cell.setCellValue("账户流水一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("年、月");
		row2.createCell(1).setCellValue("结算时间");
		row2.createCell(2).setCellValue("月线下流水");
		row2.createCell(3).setCellValue("月线上流水");
		row2.createCell(4).setCellValue("月线总流水");
		row2.createCell(5).setCellValue("当月收益");
		for (int i = 0; i < list.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			Map<String, Object> map = list.get(i);
			row3.createCell(0).setCellValue(map.get("yearMonth").toString());
			row3.createCell(1).setCellValue(map.get("clearingTime").toString());
			row3.createCell(2).setCellValue(map.get("offlineWater").toString());
			row3.createCell(3).setCellValue(map.get("onlineWater").toString());
			row3.createCell(4).setCellValue(map.get("countWater").toString());
			row3.createCell(5).setCellValue(map.get("monthlyIncome").toString());
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
	 * 获取收益趋势数据
	 */
	@RequestMapping("getEarningsTrendData")
	@ResponseBody
	public Map<String, Object> getEarningsTrendData(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return webmasterService.getEarningsTrendData(map, userId);
	}

	/**
	 * 店铺等级分布
	 */
	@RequestMapping("getShopGradeDistributionData")
	@ResponseBody
	public Integer[] getShopGradeDistributionData(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		return webmasterService.getShopGradeDistributionData(userId);
	}

	/**
	 * 会员充值占比
	 */
	@RequestMapping("getmembershipRechargeAmountData")
	@ResponseBody
	public Double[] getmembershipRechargeAmountData(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		return webmasterService.getmembershipRechargeAmountData(userId);
	}

	/**
	 * 会员等级分布
	 */
	@RequestMapping("membershipistributionData")
	@ResponseBody
	public Integer[] membershipistributionData(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		return webmasterService.membershipistributionData(userId);
	}

	/**
	 * 查询用户总数
	 */
	@RequestMapping("getUsersCount")
	@ResponseBody
	public int getUsersCount(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return webmasterService.getUsersCount(map, userId);
	}

	/**
	 * 查询店铺总数
	 */
	@RequestMapping("getStoreCount")
	@ResponseBody
	public int getStoreCount(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return webmasterService.getStoreCount(map, userId);
	}

	/**
	 * 查询站长账户数据
	 */
	@RequestMapping("getPaymentlist")
	@ResponseBody
	public Map<String, Object> getPaymentlist(HttpServletRequest request) {
		String userId = CookiesUtils.readCookies(request).get("userId").getValue();
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return webmasterService.getPaymentlist(map, userId);
	}

	/**
	 * 批量导出站长收支记录
	 * 
	 * @throws IOException
	 */
	@RequestMapping("batchExport")
	public void batchExport(String EZPIDs, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> goodslist = webmasterService.getPaymentList(EZPIDs);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("站长收支一览表");

		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		// 设置单元格内容
		cell.setCellValue("站长收支一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("序号");
		row2.createCell(1).setCellValue("时间");
		row2.createCell(2).setCellValue("金额");
		row2.createCell(3).setCellValue("事件名称");
		for (int i = 0; i < goodslist.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			Map<String, Object> map = goodslist.get(i);
			row3.createCell(0).setCellValue(i + 1);
			row3.createCell(1).setCellValue(map.get("ETIME").toString());
			row3.createCell(2).setCellValue(map.get("EMONY").toString());
			row3.createCell(3).setCellValue(map.get("CNAME").toString());
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
