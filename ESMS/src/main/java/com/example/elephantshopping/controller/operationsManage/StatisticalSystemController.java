package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.example.elephantshopping.service.operationsManage.StatisticalSystemService;
import com.example.elephantshopping.service.webmaster.WebmasterManageService;

/**
 * 统计系统
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("statisticalSystem")
public class StatisticalSystemController {
	@Autowired
	private StatisticalSystemService statisticalSystemService;
	@Autowired
	private WebmasterManageService webmasterManageService;

	// ======================统计系统主页========================
	/**
	 * 跳转统计系统页面
	 */
	@RequestMapping("toStatisticalSystemHtml")
	public ModelAndView toStatisticalSystemHtml(ModelAndView mav) {
		// 查询省级区域list
		mav.addObject("provincelist", webmasterManageService.queryAreaList("1"));
		mav.setViewName("/system/operationsManage/statisticalSystem/statisticalSystem");
		return mav;
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
	 * 查询统计数据
	 */
	@RequestMapping("queryStatisticalData")
	@ResponseBody
	public Map<String, Object> queryStatisticalData(String areaId) {
		return statisticalSystemService.queryStatisticalData(areaId);
	}

	/**
	 * 查询佣金收益
	 */
	@RequestMapping("getEarningsTrendData")
	@ResponseBody
	public Map<String, Object> getEarningsTrendData(String dataType, String areaId) {
		return statisticalSystemService.getEarningsTrendData(dataType, areaId);
	}

	/**
	 * 店铺等级分布
	 */
	@RequestMapping("getShopGradeDistributionData")
	@ResponseBody
	public Integer[] getShopGradeDistributionData(String areaId) {
		return statisticalSystemService.getShopGradeDistributionData(areaId);
	}

	/**
	 * 会员充值金额占比
	 */
	@RequestMapping("getmembershipRechargeAmountData")
	@ResponseBody
	public double[] getmembershipRechargeAmountData(String areaId) {
		return statisticalSystemService.getmembershipRechargeAmountData(areaId);
	}

	/**
	 * 会员等级分布
	 */
	@RequestMapping("membershipistributionData")
	@ResponseBody
	public int[] membershipistributionData(String areaId) {
		return statisticalSystemService.membershipistributionData(areaId);
	}

	// ============================查看流水页面====================================
	/**
	 * 跳转查看流水页面
	 */
	@RequestMapping("toRunningWaterHtml")
	public ModelAndView toRunningWaterHtml(ModelAndView mav, String areaId, String provinceId, String cityId) {
		Map<String, Object> city = new HashMap<String, Object>();
		city.put("CITY_CODE", 0);
		city.put("CITY_NAME", "请选择城市");
		Map<String, Object> area = new HashMap<String, Object>();
		area.put("CITY_CODE", 0);
		area.put("CITY_NAME", "请选择区/县");
		List<Map<String, Object>> citylist = new ArrayList<>();
		citylist.add(0, city);
		List<Map<String, Object>> arealist = new ArrayList<>();
		arealist.add(0, area);
		if (!areaId.equals('0')) {
			citylist.addAll(webmasterManageService.queryAreaList(provinceId));
			arealist.addAll(webmasterManageService.queryAreaList(cityId));
		}
		mav.addObject("citylist", citylist);
		mav.addObject("arealist", arealist);
		// 查询省级区域list
		mav.addObject("provincelist", webmasterManageService.queryAreaList("1"));
		mav.addObject("areaId", areaId);
		mav.addObject("cityId", cityId);
		mav.addObject("provinceId", provinceId);
		mav.setViewName("/system/operationsManage/statisticalSystem/runningWater");
		return mav;
	}

	/**
	 * 获取流水list
	 */
	@RequestMapping("getRunningwaterlist")
	@ResponseBody
	public Map<String, Object> getRunningwaterlist(String areaId) {
		return statisticalSystemService.getRunningwaterlist(areaId);
	}

	/**
	 * 每月流水数据导出
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getRunningwaterDaochu")
	public void getRunningwaterDaochu(String indexs, String areaId, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = statisticalSystemService.getRunningwaterDaochu(indexs, areaId);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("统计流水一览表");
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 设置这些样式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style.setWrapText(true);
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		cell.setCellStyle(style);
		// 设置单元格内容
		cell.setCellValue("统计流水一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		int x = 17;
		if (null == areaId || areaId.equals("") || areaId.equals("0")) {
			x = 22;
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, x));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		int m = 0;
		row2.createCell(m++).setCellValue("年、月");
		row2.createCell(m++).setCellValue("结算时间");
		row2.createCell(m++).setCellValue("线下货款");
		row2.createCell(m++).setCellValue("线上货款");
		row2.createCell(m++).setCellValue("总货款");
		row2.createCell(m++).setCellValue("线上佣金(含物流费)");
		row2.createCell(m++).setCellValue("线下佣金");
		row2.createCell(m++).setCellValue("总佣金");
		row2.createCell(m++).setCellValue("总流水(总货款+总佣金)");
		row2.createCell(m++).setCellValue("新注册用户");
		row2.createCell(m++).setCellValue("新实名认证");
		row2.createCell(m++).setCellValue("月充值总额");
		row2.createCell(m++).setCellValue("积分赠送");
		row2.createCell(m++).setCellValue("签到积分消耗");
		row2.createCell(m++).setCellValue("其它积分消耗");
		row2.createCell(m++).setCellValue("签到奖励金");
		row2.createCell(m++).setCellValue("积分变化");
		row2.createCell(m++).setCellValue("物流服务费");
		if (x == 22) {
			row2.createCell(m++).setCellValue("平台总积分");
			row2.createCell(m++).setCellValue("平台总余额");
			row2.createCell(m++).setCellValue("平台总零花钱");
			row2.createCell(m++).setCellValue("平台总消费券");
			row2.createCell(m++).setCellValue("积分/佣金");
		}
		row2.setRowStyle(style);
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		for (int i = 0; i < list.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			row3.setRowStyle(style);
			Map<String, Object> map = list.get(i);
			int y = 0;
			row3.createCell(y++).setCellValue(map.get("yearMonth").toString());
			row3.createCell(y++).setCellValue(map.get("clearingTime").toString());
			row3.createCell(y++).setCellValue(map.get("offlineWater").toString());
			row3.createCell(y++).setCellValue(map.get("onlineWater").toString());
			row3.createCell(y++).setCellValue(map.get("countWater").toString());
			row3.createCell(y++).setCellValue(map.get("xsCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("xxCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("commissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("countWaterAndCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("newUsers").toString());
			row3.createCell(y++).setCellValue(map.get("newPassUsers").toString());
			row3.createCell(y++).setCellValue(map.get("monthlyRecharge").toString());
			row3.createCell(y++).setCellValue(map.get("integralPresent").toString());
			row3.createCell(y++).setCellValue(map.get("signIntegralConsumption").toString());
			row3.createCell(y++).setCellValue(map.get("otherIntegralConsumption").toString());
			row3.createCell(y++).setCellValue(map.get("signBonus").toString());
			row3.createCell(y++).setCellValue(map.get("integralChange").toString());
			row3.createCell(y++).setCellValue(map.get("logisticsServiceCharge").toString());
			if (x == 22) {
				row3.createCell(y++).setCellValue(map.get("totalPlatformIntegral").toString());
				row3.createCell(y++).setCellValue(map.get("totalBalance").toString());
				row3.createCell(y++).setCellValue(map.get("totalMoney").toString());
				row3.createCell(y++).setCellValue(map.get("totalConsumptionCoupon").toString());
				row3.createCell(y++).setCellValue(map.get("integralOverCommission").toString());
			}
			sheet.autoSizeColumn(i + 2);
			row3.setRowStyle(style);
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}

	// ==============================================查看明细=================================================
	/**
	 * 跳转查看明细页面
	 */
	@RequestMapping("toSeeDetailsHtml")
	public ModelAndView toSeeDetailsHtml(ModelAndView mav, String areaId, String provinceId, String cityId,
			String month) {
		Map<String, Object> city = new HashMap<String, Object>();
		city.put("CITY_CODE", 0);
		city.put("CITY_NAME", "请选择城市");
		Map<String, Object> area = new HashMap<String, Object>();
		area.put("CITY_CODE", 0);
		area.put("CITY_NAME", "请选择区/县");
		List<Map<String, Object>> citylist = new ArrayList<>();
		citylist.add(0, city);
		List<Map<String, Object>> arealist = new ArrayList<>();
		arealist.add(0, area);
		if (!areaId.equals('0')) {
			citylist.addAll(webmasterManageService.queryAreaList(provinceId));
			arealist.addAll(webmasterManageService.queryAreaList(cityId));
		}
		mav.addObject("citylist", citylist);
		mav.addObject("arealist", arealist);
		// 查询省级区域list
		mav.addObject("provincelist", webmasterManageService.queryAreaList("1"));
		mav.addObject("areaId", areaId);
		mav.addObject("cityId", cityId);
		mav.addObject("provinceId", provinceId);
		mav.addObject("month", month);
		mav.setViewName("/system/operationsManage/statisticalSystem/seeDetails");
		return mav;
	}

	/**
	 * 查询月流水明细
	 * 
	 * @throws ParseException
	 */
	@RequestMapping("getdayWaterlist")
	@ResponseBody
	public Map<String, Object> getdayWaterlist(String month, String areaId) {
		return statisticalSystemService.getdayWaterlist(month, areaId);
	}

	/**
	 * 站长每月流水明细数据导出
	 * 
	 * @throws IOException
	 */
	@RequestMapping("getDetailswaterDaochu")
	public void getDetailswaterDaochu(String days, String areaId, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = statisticalSystemService.getDetailswaterDaochu(days, areaId);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("统计流水一览表");
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 设置这些样式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		style.setWrapText(true);
		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		cell.setCellStyle(style);
		// 设置单元格内容
		cell.setCellValue("统计流水一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		int x = 17;
		if (null == areaId || areaId.equals("") || areaId.equals("0")) {
			x = 22;
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, x));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		int n = 0;
		row2.createCell(n++).setCellValue("年、月、日");
		row2.createCell(n++).setCellValue("结算时间");
		row2.createCell(n++).setCellValue("线下货款");
		row2.createCell(n++).setCellValue("线上货款");
		row2.createCell(n++).setCellValue("总货款");
		row2.createCell(n++).setCellValue("线上佣金(含物流费)");
		row2.createCell(n++).setCellValue("线下佣金");
		row2.createCell(n++).setCellValue("总佣金");
		row2.createCell(n++).setCellValue("总流水(总货款+总佣金)");
		row2.createCell(n++).setCellValue("新注册用户");
		row2.createCell(n++).setCellValue("新实名认证");
		row2.createCell(n++).setCellValue("充值总额");
		row2.createCell(n++).setCellValue("积分赠送");
		row2.createCell(n++).setCellValue("签到积分消耗");
		row2.createCell(n++).setCellValue("其它积分消耗");
		row2.createCell(n++).setCellValue("签到奖励金");
		row2.createCell(n++).setCellValue("积分变化");
		row2.createCell(n++).setCellValue("物流服务费");
		if (x == 22) {
			row2.createCell(n++).setCellValue("平台总积分");
			row2.createCell(n++).setCellValue("平台总余额");
			row2.createCell(n++).setCellValue("平台总零花钱");
			row2.createCell(n++).setCellValue("平台总消费券");
			row2.createCell(n++).setCellValue("积分/佣金");
		}
		row2.setRowStyle(style);
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		for (int i = 0; i < list.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			row3.setRowStyle(style);
			Map<String, Object> map = list.get(i);
			int y = 0;
			row3.createCell(y++).setCellValue(map.get("date").toString());
			row3.createCell(y++).setCellValue(map.get("endTime").toString());
			row3.createCell(y++).setCellValue(map.get("offlineWater").toString());
			row3.createCell(y++).setCellValue(map.get("onlineWater").toString());
			row3.createCell(y++).setCellValue(map.get("countWater").toString());
			row3.createCell(y++).setCellValue(map.get("xsCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("xxCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("commissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("countWaterAndCommissionIncome").toString());
			row3.createCell(y++).setCellValue(map.get("newUsers").toString());
			row3.createCell(y++).setCellValue(map.get("newPassUsers").toString());
			row3.createCell(y++).setCellValue(map.get("monthlyRecharge").toString());
			row3.createCell(y++).setCellValue(map.get("integralPresent").toString());
			row3.createCell(y++).setCellValue(map.get("signIntegralConsumption").toString());
			row3.createCell(y++).setCellValue(map.get("otherIntegralConsumption").toString());
			row3.createCell(y++).setCellValue(map.get("signBonus").toString());
			row3.createCell(y++).setCellValue(map.get("integralChange").toString());
			row3.createCell(y++).setCellValue(map.get("logisticsServiceCharge").toString());
			if (x == 22) {
				row3.createCell(y++).setCellValue(map.get("totalPlatformIntegral").toString());
				row3.createCell(y++).setCellValue(map.get("totalBalance").toString());
				row3.createCell(y++).setCellValue(map.get("totalMoney").toString());
				row3.createCell(y++).setCellValue(map.get("totalConsumptionCoupon").toString());
				row3.createCell(y++).setCellValue(map.get("integralOverCommission").toString());
			}
			sheet.autoSizeColumn(i + 2);
			row3.setRowStyle(style);
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
