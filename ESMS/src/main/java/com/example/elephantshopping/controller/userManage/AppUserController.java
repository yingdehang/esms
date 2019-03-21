package com.example.elephantshopping.controller.userManage;

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

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.OnLineStoreService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.service.userManage.AppUserService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * APP用户管理Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("appUser")
public class AppUserController {
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private PermissionsController permissionsController;
	@Autowired
	private OnLineStoreService onLineStoreService;

	/**
	 * 转到APP用户列表页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toAppUserList")
	public ModelAndView toAppUserList(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		// 查询权限
		modelAndView.addObject("exportAppUserWater",
				permissionsController.queryPermissions("exportAppUserWater", request));
		modelAndView.addObject("changeVerifytate", permissionsController.queryPermissions("changeVerifytate", request));
		// 统计系统参数MEMBERSHIP_GRADE
		Map<String, Object> map = RequestUtils.requestToMap(request);
		modelAndView.addObject("MEMBERSHIP_GRADE", map.get("MEMBERSHIP_GRADE"));
		modelAndView.setViewName("/system/userManage/appUserManage/appUserList");
		return modelAndView;
	}

	/**
	 * 转到APP用户列表页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toAppUserList2")
	public ModelAndView toAppUserList2(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		// 查询权限
		modelAndView.addObject("exportAppUserWater",
				permissionsController.queryPermissions("exportAppUserWater", request));
		modelAndView.addObject("changeVerifytate", permissionsController.queryPermissions("changeVerifytate", request));
		modelAndView.setViewName("/system/systemManage/appUserManage/appUserList");
		return modelAndView;
	}

	/**
	 * 表格获取/查询APP用户数据
	 * 
	 * @param phoneNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("getAppUserList")
	@ResponseBody
	public Map<String, Object> getAppUserList(String phoneNumber, HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
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
		List<Map<String, Object>> AppUserlist = appUserService.getAppUserList(parameters);// 未分页的数据List
		List<Map<String, Object>> AppUserlistPage = appUserService.getAppUserListPage(parameters);// 分页的数据List
		for (Map<String, Object> userInfo : AppUserlistPage) {
			String province = (String) userInfo.get("PROVINCE");
			String city = (String) userInfo.get("CITY");
			String area = (String) userInfo.get("AREA");
			if (province == null) {
				province = "";
			}
			if (city == null) {
				city = "";
			}
			if (area == null) {
				area = "";
			}
			userInfo.put("area", province + city + area);
			// 会员等级为空
			if (userInfo.get("membershipGrade") == null) {
				userInfo.put("membershipGrade", "");
			}
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", AppUserlistPage);
		map.put("count", AppUserlist.size());
		return map;
	}

	/**
	 * 禁用账号
	 * 
	 * @param userId
	 */
	@RequestMapping("forbidden")
	@ResponseBody
	public int forbidden(String userId) {
		appUserService.forbidden(userId);
		return 0;
	}

	/**
	 * 启用账号
	 * 
	 * @param userId
	 */
	@RequestMapping("startUsing")
	@ResponseBody
	public int startUsing(String userId) {
		appUserService.startUsing(userId);
		return 0;
	}

	/**
	 * 展示APP用户信息细节
	 * 
	 * @param modelAndView
	 * @param userId
	 * @return
	 */
	@RequestMapping("details")
	public ModelAndView details(ModelAndView modelAndView, String userId) {
		Map<String, Object> details = appUserService.getDetails(userId);
		if (details.get("PROVINCE") == null) {
			details.put("PROVINCE", "");
		}
		if (details.get("CITY") == null) {
			details.put("CITY", "");
		}
		if (details.get("AREA") == null) {
			details.put("AREA", "");
		}
		if (details.get("STATE") == null) {
			details.put("STATE", "");
		}
		if (details.get("UC_NAMES") == null) {
			details.put("UC_NAMES", "");
		}
		if (details.get("ID_CARD") == null) {
			details.put("ID_CARD", "");
		}
		if (details.get("IS_PHOTO") == null) {
			details.put("IS_PHOTO", "");
		}
		if (details.get("NICKNAME") == null) {
			details.put("NICKNAME", "");
		}
		if (details.get("SEX") == null) {
			details.put("SEX", "");
		}
		modelAndView.addObject("details", details);// 获取用户详细信息
		List<Map<String, Object>> userState = appUserService.getUserState();// 获取用户状态
		modelAndView.addObject("userState", userState);
		List<Map<String, Object>> verifyState = appUserService.getVerifyState();// 获取实名认证状态
		modelAndView.addObject("verifyState", verifyState);
		modelAndView.setViewName("/system/userManage/appUserManage/appUserDetails");
		return modelAndView;
	}

	/**
	 * 改变账户状态
	 * 
	 * @return
	 */
	@RequestMapping("changeUserState")
	@ResponseBody
	public int changeUserState(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		appUserService.changeUserState(parameters);
		return 0;
	}

	/**
	 * 改变账户认证状态
	 * 
	 * @return
	 */
	@RequestMapping("changeVerifytate")
	@ResponseBody
	public int changeVerifytate(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		appUserService.changeVerifytate(parameters);
		return 0;
	}

	/**
	 * 导出用户的个人流水数据
	 * 
	 * @param userId
	 * @throws IOException
	 */
	@RequestMapping("export")
	public void export(String userId, HttpServletResponse response) throws IOException {
		Map<String, Object> userInfo = appUserService.getUserInfo(userId);// 用户信息
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		List<Map<String, Object>> list = appUserService.getUserRunningAccounts(userId);// 获得用户个人流水数据
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("用户流水表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 16));
		sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 16));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 2));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 3, 4));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 5, 6));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 7, 8));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 9, 10));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 11, 12));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 13, 14));
		sheet.addMergedRegion(new CellRangeAddress(2, 2, 15, 16));
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row0.createCell(0);
		// 设置单元格内容
		cell.setCellValue("用户流水表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// 在sheet里创建第二行
		HSSFRow row1 = sheet.createRow(1);
		row1.createCell(0)
				.setCellValue("用户：" + userInfo.get("NICKNAME") + " " + userInfo.get("PHONE") + "   " + "导出时间：" + date);
		// 在sheet里创建第三行
		HSSFRow row2 = sheet.createRow(2);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("时间");
		row2.createCell(3).setCellValue("动作名称");
		row2.createCell(5).setCellValue("余额支出");
		row2.createCell(7).setCellValue("余额收入");
		row2.createCell(9).setCellValue("零钱支出");
		row2.createCell(11).setCellValue("零钱收入");
		row2.createCell(13).setCellValue("消费券支出");
		row2.createCell(15).setCellValue("消费券收入");
		row2.createCell(17).setCellValue("积分支出");
		row2.createCell(19).setCellValue("积分收入");
		for (int i = 0; i < list.size(); i++) {
			SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 0, 2));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 3, 4));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 5, 6));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 7, 8));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 9, 10));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 11, 12));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 13, 14));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 15, 16));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 17, 18));
			sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 3, 19, 20));
			HSSFRow row3 = sheet.createRow(i + 3);
			Map<String, Object> map = list.get(i);
			row3.createCell(0).setCellValue(s.format(map.get("time")));
			row3.createCell(3).setCellValue(map.get("action").toString());
			row3.createCell(5).setCellValue(map.get("balanceOut").toString());
			row3.createCell(7).setCellValue(map.get("balanceIn").toString());
			row3.createCell(9).setCellValue(map.get("pocketOut").toString());
			row3.createCell(11).setCellValue(map.get("pocketIn").toString());
			row3.createCell(13).setCellValue(map.get("consumptionOut").toString());
			row3.createCell(15).setCellValue(map.get("consumptionIn").toString());
			row3.createCell(17).setCellValue(map.get("integralOut").toString());
			row3.createCell(19).setCellValue(map.get("integralIn").toString());
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
	 * 导出app用户列表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exportAppUserList2")
	public void exportAppUserList2(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		List<Map<String, Object>> AppUserlist = appUserService.getAppUserList(parameters);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("app用户列表");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("序号");
		row0.createCell(1).setCellValue("手机号（账号）");
		row0.createCell(2).setCellValue("昵称");
		row0.createCell(3).setCellValue("注册时间");
		row0.createCell(4).setCellValue("账号状态");
		row0.createCell(5).setCellValue("会员等级");
		row0.createCell(6).setCellValue("余额");
		row0.createCell(7).setCellValue("零花钱");
		row0.createCell(8).setCellValue("消费券");
		row0.createCell(9).setCellValue("积分");
		row0.createCell(10).setCellValue("是否实名认证");
		row0.createCell(11).setCellValue("真实姓名");
		row0.createCell(12).setCellValue("地区");
		row0.createCell(13).setCellValue("分享者");
		row0.createCell(14).setCellValue("实名认证通过时间");
		for (int i = 0; i < AppUserlist.size(); i++) {
			HSSFRow row1 = sheet.createRow(i + 1);
			Map<String, Object> map = AppUserlist.get(i);
			row1.createCell(0).setCellValue(i + 1);
			row1.createCell(1).setCellValue(map.get("PHONE").toString());
			if (map.get("NICKNAME") == null) {
				row1.createCell(2).setCellValue("");
			} else {
				row1.createCell(2).setCellValue(map.get("NICKNAME").toString());
			}
			row1.createCell(3).setCellValue(map.get("REGISTERED_TIME").toString());
			if (map.get("userState").equals("USER_STATE_NORMAL")) {
				row1.createCell(4).setCellValue("正常");
			} else if (map.get("userState").equals("USER_STATE_DISABLE")) {
				row1.createCell(4).setCellValue("冻结");
			}
			if (map.get("MEMBERSHIP_GRADE") == null) {
				row1.createCell(5).setCellValue("");
			} else {
				row1.createCell(5).setCellValue(map.get("MEMBERSHIP_GRADE").toString());
			}
			row1.createCell(6).setCellValue(map.get("MONEYS").toString());
			row1.createCell(7).setCellValue(map.get("POCKETMONEY").toString());
			row1.createCell(8).setCellValue(map.get("CONSUMPTION_VOLUME").toString());
			row1.createCell(9).setCellValue(map.get("INTEGRAL").toString());
			if (map.get("STATE") == null) {
				row1.createCell(10).setCellValue("否");
			} else if (map.get("STATE").equals("USER_AUTHENTICATION_PASS")) {
				row1.createCell(10).setCellValue("是");
			} else {
				row1.createCell(10).setCellValue("否");
			}
			if (map.get("UC_NAMES") == null) {
				row1.createCell(11).setCellValue("");
			} else {
				row1.createCell(11).setCellValue(map.get("UC_NAMES").toString());
			}
			String province = (String) map.get("PROVINCE");
			String city = (String) map.get("CITY");
			String area = (String) map.get("AREA");
			if (province == null) {
				province = "";
			}
			if (city == null) {
				city = "";
			}
			if (area == null) {
				area = "";
			}
			row1.createCell(12).setCellValue(province + city + area);
			if (map.get("INVITATION") == null) {
				row1.createCell(13).setCellValue("");
			} else {
				row1.createCell(13).setCellValue(map.get("INVITATION").toString());
			}
			if(map.get("STATE").equals("USER_AUTHENTICATION_PASS")){
				row1.createCell(14).setCellValue(map.get("TO_APPLY_FOR_TIME").toString());
			}
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
	 * 导出app用户列表
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exportAppUserList")
	public void exportAppUserList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		List<Map<String, Object>> AppUserlist = appUserService.getAppUserList(parameters);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("app用户列表");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("序号");
		row0.createCell(1).setCellValue("手机号（账号）");
		row0.createCell(2).setCellValue("昵称");
		row0.createCell(3).setCellValue("注册时间");
		row0.createCell(4).setCellValue("账号状态");
		row0.createCell(5).setCellValue("会员等级");
		row0.createCell(6).setCellValue("是否实名认证");
		row0.createCell(7).setCellValue("真实姓名");
		row0.createCell(8).setCellValue("地区");
		row0.createCell(9).setCellValue("分享者");
		for (int i = 0; i < AppUserlist.size(); i++) {
			HSSFRow row1 = sheet.createRow(i + 1);
			Map<String, Object> map = AppUserlist.get(i);
			row1.createCell(0).setCellValue(i + 1);
			row1.createCell(1).setCellValue(map.get("PHONE").toString());
			if (map.get("NICKNAME") == null) {
				row1.createCell(2).setCellValue("");
			} else {
				row1.createCell(2).setCellValue(map.get("NICKNAME").toString());
			}
			row1.createCell(3).setCellValue(map.get("REGISTERED_TIME").toString());
			if (map.get("userState").equals("USER_STATE_NORMAL")) {
				row1.createCell(4).setCellValue("正常");
			} else if (map.get("userState").equals("USER_STATE_DISABLE")) {
				row1.createCell(4).setCellValue("冻结");
			}
			if (map.get("MEMBERSHIP_GRADE") == null) {
				row1.createCell(5).setCellValue("");
			} else {
				row1.createCell(5).setCellValue(map.get("MEMBERSHIP_GRADE").toString());
			}
			if (map.get("STATE") == null) {
				row1.createCell(6).setCellValue("否");
			} else if (map.get("STATE").equals("USER_AUTHENTICATION_PASS")) {
				row1.createCell(6).setCellValue("是");
			} else {
				row1.createCell(6).setCellValue("否");
			}
			if (map.get("UC_NAMES") == null) {
				row1.createCell(7).setCellValue("");
			} else {
				row1.createCell(7).setCellValue(map.get("UC_NAMES").toString());
			}
			String province = (String) map.get("PROVINCE");
			String city = (String) map.get("CITY");
			String area = (String) map.get("AREA");
			if (province == null) {
				province = "";
			}
			if (city == null) {
				city = "";
			}
			if (area == null) {
				area = "";
			}
			row1.createCell(8).setCellValue(province + city + area);
			if (map.get("INVITATION") == null) {
				row1.createCell(9).setCellValue("");
			} else {
				row1.createCell(9).setCellValue(map.get("INVITATION").toString());
			}
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
	 * 改变会员等级
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeGrade")
	@ResponseBody
	public int changeGrade(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		appUserService.changeGrade(parameters);
		return 0;
	}

	/**
	 * 判断用户是否存在
	 * 
	 * @param userPhone
	 * @return
	 */
	@RequestMapping("userExist")
	@ResponseBody
	public String userExist(String userPhone) {
		List<Map<String, Object>> user = onLineStoreService.getUserByPhone(userPhone);
		if (user.size() > 0) {
			return "ok";
		} else {
			return "no";
		}
	}

	/**
	 * 改变邀请人
	 * 
	 * @param userId
	 * @param invitation
	 * @return
	 */
	@RequestMapping("changeInvitation")
	@ResponseBody
	public int changeInvitation(String userId, String invitation) {
		appUserService.changeInvitation(userId, invitation);
		return 0;
	}
}
