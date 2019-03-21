package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import com.example.elephantshopping.service.operationsManage.WithdrawalsService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 提现审核Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("withdrawals")
public class WithdrawalsController {
	@Autowired
	private WithdrawalsService withdrawalsService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 转到提现审核页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toWithdrawals")
	public ModelAndView toWithdrawals(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> verifyStates = withdrawalsService.getWithdrawalsStates();
		modelAndView.addObject("verifyStates", verifyStates);// 审核状态
		// 添加权限
		modelAndView.addObject("withdrawalAudit", permissionsController.queryPermissions("withdrawalAudit", request));
		modelAndView.addObject("exportWithdrawal", permissionsController.queryPermissions("exportWithdrawal", request));

		modelAndView.setViewName("/system/operationsManage/financeManage/withdrawals");
		return modelAndView;
	}

	/**
	 * 得到提现申请数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getWithdrawalsList")
	@ResponseBody
	public Map<String, Object> getWithdrawalsList(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		String userName = (String) parameters.get("userName");
		if (userName != null && (!userName.equals(""))) {
			parameters.put("userName", "%" + userName + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> withdrawals = withdrawalsService.getWithdrawals(parameters);// 提现申请数据
		List<Map<String, Object>> withdrawalsPage = withdrawalsService.getWithdrawalsPage(parameters);// 提现申请数据分页
		for (Map<String, Object> m : withdrawalsPage) {
			BigDecimal money = (BigDecimal) m.get("MONEY");
			m.put("MONEY", money.toString());
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", withdrawalsPage);
		map.put("count", withdrawals.size());
		return map;
	}

	/**
	 * 转到提现明细页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toWithdrawalsRecord")
	public ModelAndView toWithdrawalsRecord(ModelAndView modelAndView) {
		modelAndView.setViewName("/system/operationsManage/financeManage/withdrawalsRecord");
		return modelAndView;
	}

	/**
	 * 获取提现明细数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getWithdrawalsRecord")
	@ResponseBody
	public Map<String, Object> getWithdrawalsRecord(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		String userName = (String) parameters.get("userName");
		if (userName != null && (!userName.equals(""))) {
			parameters.put("userName", "%" + userName + "%");
		}
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> withdrawals = withdrawalsService.getWithdrawalsRecord(parameters);// 提现申请明细数据
		List<Map<String, Object>> withdrawalsPage = withdrawalsService.getWithdrawalsRecordPage(parameters);// 提现申请明细数据分页
		for (Map<String, Object> m : withdrawalsPage) {
			BigDecimal money = (BigDecimal) m.get("MONEY");
			m.put("MONEY", money.toString());
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", withdrawalsPage);
		map.put("count", withdrawals.size());
		return map;
	}

	/**
	 * 提现审核
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("withdrawalsVerify")
	@ResponseBody
	@Transactional
	public synchronized int withdrawalsVerify(String withdrawalsId, String verifyState, String content, String userId, double money,
			String moneyType, String withdrawalType, String bankName, String bank,double original) {
		// System.out.println("withdrawalsId:"+withdrawalsId);
		// System.out.println("verifyState:"+verifyState);
		// System.out.println("content:"+content);
		// System.out.println("userId:"+userId);
		// System.out.println("money:"+money);
		// System.out.println("moneyType:"+moneyType);
		// 判断提现审核失败或者通过
		if (verifyState.equals("EXTRACT_STATE_PASS"))// 通过
		{
			// 修改提现记录状态
			withdrawalsService.verifyPass(withdrawalsId);
			if (withdrawalType.equals("2"))// 如果是银行支付，添加用户银行卡信息
			{
				List<Map<String, Object>> banks = withdrawalsService.getBanks(userId);// 得到用户保存的所有银行信息
				if (banks.size() >= 5) {
					String bankId = (String) banks.get(0).get("EBID");// 得到最早的银行卡信息的Id
					withdrawalsService.deleteBank(bankId);// 删除这条银行卡信息
				}
				// 判断用户的银行卡信息是否存在
				int count = withdrawalsService.getUserBank(userId, bank);
				if (count == 0)// 不存在则添加一条用户银行卡信息
				{
					withdrawalsService.addUserBank(userId, bankName, bank);
				}
			}
		} else// 失败
		{
//			double backMoney = 0;// 返给用户的金额
//			// 判断用户是否是旗舰店店主
//			List<Map<String, Object>> stores = withdrawalsService.getUserQJStore(userId);// 得到用户的旗舰店
//			if (stores.size() > 0) {
//				// 如果用户有旗舰店，全额返回
//				backMoney = money;
//			} else {
//				// 如果用户没有旗舰店，算出扣除手续费之前的金额，再返回
//				backMoney = money / 0.99;
//			}
//			backMoney = new BigDecimal(backMoney).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();// 保留两位小数
			// 1.用户表添加金额
			withdrawalsService.addUserMoney(userId, moneyType, original);
			// 2.提现记录表修改状态
			withdrawalsService.verifyNo(original, withdrawalsId, content);
			// 3.添加余额/零花钱收支表流水
			if (moneyType.equals("1"))// 余额
			{
				withdrawalsService.addMoneyWater(userId, original, "1", "提现失败");
			} else// 零花钱
			{
				withdrawalsService.addPocketMoneyWater(userId, original, "1", "提现失败");
			}
			// 4.添加收益流水
			withdrawalsService.addRunningWater(userId, "1", original, "提现失败", moneyType);
		}
		return 0;
	}

	@RequestMapping("export")
	public void export(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		List<Map<String, Object>> withdrawals = withdrawalsService.getWithdrawals(parameters);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("提现审核表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 7));
		// 生成一个样式
		HSSFCellStyle cellStyle1 = wb.createCellStyle();
		cellStyle1.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中
		// 生成一个样式
		HSSFCellStyle cellStyle2 = wb.createCellStyle();
		cellStyle2.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 水平布局：右对齐
		//标题
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell c0 = row0.createCell(0);
		c0.setCellValue("提现操作明细");
		c0.setCellStyle(cellStyle1);
		//时间
		HSSFRow row1 = sheet.createRow(1);
		HSSFCell c1 = row1.createCell(0);
		c1.setCellValue("操作时间："+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		c1.setCellStyle(cellStyle2);
		// 表头
		HSSFRow row2 = sheet.createRow(2);
		HSSFCell cell0 = row2.createCell(0);
		cell0.setCellValue("序号");
		cell0.setCellStyle(cellStyle1);
		HSSFCell cell1 = row2.createCell(1);
		cell1.setCellValue("提现类型");
		cell1.setCellStyle(cellStyle1);
		HSSFCell cell2 = row2.createCell(2);
		cell2.setCellValue("账户");
		cell2.setCellStyle(cellStyle1);
		HSSFCell cell3 = row2.createCell(3);
		cell3.setCellValue("用户名");
		cell3.setCellStyle(cellStyle1);
		HSSFCell cell4 = row2.createCell(4);
		cell4.setCellValue("手机号");
		cell4.setCellStyle(cellStyle1);
		HSSFCell cell5 = row2.createCell(5);
		cell5.setCellValue("金额");
		cell5.setCellStyle(cellStyle1);
		HSSFCell cell6 = row2.createCell(6);
		cell6.setCellValue("金额类型");
		cell6.setCellStyle(cellStyle1);
		HSSFCell cell7 = row2.createCell(7);
		cell7.setCellValue("申请时间");
		cell7.setCellStyle(cellStyle1);
		HSSFCell cell8 = row2.createCell(8);
		cell8.setCellValue("是否是旗舰店");
		cell8.setCellStyle(cellStyle1);
		for (int i = 0; i < withdrawals.size(); i++) {
			HSSFRow row3 = sheet.createRow(i + 3);
			row3.createCell(0).setCellValue(i + 1);
			if (withdrawals.get(i).get("WITHDRAWAL_TYPE").equals("1")) {
				row3.createCell(1).setCellValue("支付宝");
				row3.createCell(2).setCellValue(withdrawals.get(i).get("ALIPAY") + "");
			} else {
				row3.createCell(1).setCellValue(withdrawals.get(i).get("BANKNAME") + "");
				row3.createCell(2).setCellValue(withdrawals.get(i).get("BANK") + "");
			}
			row3.createCell(3).setCellValue(withdrawals.get(i).get("WITHDRAWAL_NAME") + "");
			row3.createCell(4).setCellValue(withdrawals.get(i).get("PHONE") + "");
			BigDecimal money = (BigDecimal) withdrawals.get(i).get("MONEY");
			row3.createCell(5).setCellValue(money.toString());
			if (withdrawals.get(i).get("MONEY_TYPE").equals("1")) {
				row3.createCell(6).setCellValue("余额");
			} else {
				row3.createCell(6).setCellValue("零花钱");
			}

			row3.createCell(7).setCellValue(withdrawals.get(i).get("TO_APPLY_FOR_TIME") + "");
			String userId = (String) withdrawals.get(i).get("USERS_ID");
			List<Map<String, Object>> list = withdrawalsService.getUserQJStore(userId);//查询提现用户是否有旗舰店
			if(list.size()>0)
			{
				row3.createCell(8).setCellValue("是");
			}
			else
			{
				row3.createCell(8).setCellValue("否");
			}
		}
		HSSFRow row4 = sheet.createRow(withdrawals.size()+3);
		row4.createCell(0).setCellValue("制表人：");
		row4.createCell(3).setCellValue("总经理签字：");
		row4.createCell(6).setCellValue("董事长签字：");
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}
}
