package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.security.Permissions;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.OffLineStoreService;
import com.example.elephantshopping.service.operationsManage.OnLineStoreService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.service.userManage.AppUserService;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 线上店铺管理Controller
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("onLineStore")
public class OnLineStoreController {
	@Autowired
	private OnLineStoreService onLineStoreService;
	@Autowired
	private OffLineStoreService offLineStoreService;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 转到线上店铺管理页面
	 * 
	 * @return
	 */
	@RequestMapping("toOnLineStoreList")
	public ModelAndView toOnLineStoreList(ModelAndView modelAndView, HttpServletRequest request) {
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		// 添加权限
		modelAndView.addObject("seeXsStoreMonthWater",
				permissionsController.queryPermissions("seeXsStoreMonthWater", request));
		modelAndView.addObject("changeXsStoreState",
				permissionsController.queryPermissions("changeXsStoreState", request));
		modelAndView.addObject("exportXsStoreWater",
				permissionsController.queryPermissions("exportXsStoreWater", request));
		modelAndView.addObject("addXsStore", permissionsController.queryPermissions("addXsStore", request));
		modelAndView.addObject("xsGoodsManage", permissionsController.queryPermissions("xsGoodsManage", request));
		modelAndView.addObject("xsStoreInfoUpdate",
				permissionsController.queryPermissions("xsStoreInfoUpdate", request));
		modelAndView.addObject("xsOrderManage", permissionsController.queryPermissions("xsOrderManage", request));
		// 统计系统跳转带的参数：storeDJ
		 Map<String, Object> map = RequestUtils.requestToMap(request);
		 modelAndView.addObject("storeDJ", map.get("storeDJ"));

		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreList");
		return modelAndView;
	}

	/**
	 * 获取线上店铺
	 * 
	 * @return
	 */
	@RequestMapping("getOnLineStoreList")
	@ResponseBody
	public Map<String, Object> getOnLineStoreList(HttpServletRequest request) {
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
		List<Map<String, Object>> onLineStoreList = onLineStoreService.getOnLineStoreList(parameters);// 线上店铺数据未分页
		List<Map<String, Object>> onLineStoreListPage = onLineStoreService.getOnLineStoreListPage(parameters);// 线上店铺数据已分页
		for (Map<String, Object> storeInfo : onLineStoreListPage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", onLineStoreListPage);
		map.put("count", onLineStoreList.size());
		return map;
	}

	/**
	 * 改变店铺状态（冻结/解冻）
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeState")
	@ResponseBody
	public int changeState(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		onLineStoreService.changeState(parameters);
		return 0;
	}

	/**
	 * 转到线上店铺申请页面
	 * 
	 * @return
	 */
	@RequestMapping("toOnLineStoreApply")
	public ModelAndView toOnLineStoreApply(ModelAndView modelAndView, String userId, HttpServletRequest request) {
		modelAndView.addObject("userId", userId);
		modelAndView.addObject("xsStoreAudit", permissionsController.queryPermissions("xsStoreAudit", request));
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreApply");
		return modelAndView;
	}

	/**
	 * 获取线上店铺申请
	 * 
	 * @return
	 */
	@RequestMapping("getOnLineStoreApply")
	@ResponseBody
	public Map<String, Object> getOnLineStoreApply(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> onLineStoreApply = onLineStoreService.getOnLineStoreApply(parameters);// 线上店铺申请未分页
		List<Map<String, Object>> onLineStoreApplyPage = onLineStoreService.getOnLineStoreApplyPage(parameters);// 线上店铺申请已分页
		for (Map<String, Object> storeInfo : onLineStoreApplyPage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", onLineStoreApplyPage);
		map.put("count", onLineStoreApply.size());
		return map;
	}

	/**
	 * 转到线上店铺审核页面
	 * 
	 * @return
	 */
	@RequestMapping("toOnLineStoreVerify")
	public ModelAndView toOnLineStoreVerify(ModelAndView modelAndView, String storeId, String userId) {
		Map<String, Object> details = onLineStoreService.getVerifyInfo(storeId);
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
		if (details.get("STORE_ICON") == null) {
			details.put("STORE_ICON", "");
		}
		modelAndView.addObject("details", details);// 查询店铺认证信息
		List<Map<String, Object>> storeType = onLineStoreService.getStoreType();
		modelAndView.addObject("storeType", storeType);// 查询店铺类型
		List<Map<String, Object>> storeLevel = onLineStoreService.getStoreLevel();
		modelAndView.addObject("storeLevel", storeLevel);// 查询店铺等级
		List<Map<String, Object>> storeClass = onLineStoreService.getStoreClass("0");
		modelAndView.addObject("storeClass", storeClass);// 查询店铺一级分类
		String oldClassName = onLineStoreService.getOldClassName(storeId);
		modelAndView.addObject("oldClassName", oldClassName);// 查询店铺已选分类名
		String oldClass = onLineStoreService.getOldClass(storeId);
		if (oldClass == null) {
			oldClass = "";
		}
		modelAndView.addObject("oldClass", oldClass);// 查询店铺已选分类
		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreVerify");
		return modelAndView;
	}

	/**
	 * 查询店铺分类
	 * 
	 * @param pId
	 * @return
	 */
	@RequestMapping("getStoreClass")
	@ResponseBody
	public List<Map<String, Object>> getStoreClass(String pId) {
		return onLineStoreService.getStoreClass(pId);
	}

	/**
	 * 拒绝申请
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("refuse")
	@ResponseBody
	public int refuse(String id, String content, String userId) {
		onLineStoreService.refuse(id, content, userId);
		return 0;
	}

	/**
	 * 通过申请
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("pass")
	@ResponseBody
	@Transactional
	public synchronized int pass(String id, String storeId, String userId, String storeUserId) {
		onLineStoreService.pass(id, storeId, userId);
		// 改变此用户为系统用户
		onLineStoreService.changeUserLevelSystem(storeUserId);
		// 添加此用户角色为店家
		onLineStoreService.addUserMerchant(storeUserId);
		// 改变此店铺的状态为正常
		offLineStoreService.changeStoreStateNormal(storeId);
		return 0;
	}

	/**
	 * 改变店铺类型
	 * 
	 * @param type
	 * @return
	 */
	@RequestMapping("changeType")
	@ResponseBody
	public int changeType(String type, String id, String storeId) {
		onLineStoreService.changeType(type, id);// 改变店铺类型
		return 0;
	}

	/**
	 * 改变店铺的分类值并返回分类类型的字符串
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeClasses")
	@ResponseBody
	public String changeClasses(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		String storeId = (String) parameters.get("storeId");
		onLineStoreService.changeClasses(parameters);// 改变店铺分类
		String oldClass = onLineStoreService.getOldClassName(storeId);// 得到店铺分类的名字
		return oldClass;
	}

	/**
	 * 得到店铺原本分类的名字
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping("getOldClasses")
	@ResponseBody
	public String getOldClasses(String storeId) {
		return onLineStoreService.getOldClassName(storeId);// 得到店铺分类的名字;
	}

	/**
	 * 改变店铺等级
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeLevel")
	@ResponseBody
	public int changeLevel(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		onLineStoreService.changeLevel(parameters);
		return 0;
	}

	/**
	 * 改变店铺名字
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeStoreName")
	@ResponseBody
	public int changeStoreName(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		onLineStoreService.changeStoreName(parameters);
		return 0;
	}

	/**
	 * 改变店铺服务电话
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeServicePhone")
	@ResponseBody
	public int changeServicePhone(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		onLineStoreService.changeServicePhone(parameters);
		return 0;
	}

	/**
	 * 改变店铺是否自营
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeIS")
	@ResponseBody
	public int changeIS(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		onLineStoreService.changeIS(parameters);
		return 0;
	}

	/**
	 * 改变店铺图标
	 * 
	 * @param file
	 * @param storeId
	 * @return
	 */
	@RequestMapping("changeStoreIcon")
	@ResponseBody
	public int changeStoreIcon(MultipartFile file, String storeId) {
		try {
			String storeIcon = onLineStoreService.getStoreIcon(storeId);
			UploadFileUtils.deleteFile(storeIcon);// 得到店铺原来的图标并将它删除
			String url = UploadFileUtils.uploadFile(file, "store");
			onLineStoreService.changeStoreIcon(url, storeId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 转到线上店铺申请记录页面
	 * 
	 * @return
	 */
	@RequestMapping("toOnLineStoreVerifyRecord")
	public ModelAndView toOnLineStoreVerifyRecord(ModelAndView modelAndView, String userId) {
		modelAndView.addObject("userId", userId);
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreVerifyRecord");
		return modelAndView;
	}

	/**
	 * 获取线上店铺申请记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getOnLineStoreVerifyRecord")
	@ResponseBody
	public Map<String, Object> getOnLineStoreVerifyRecord(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);// 将页面传来的参数封装成一个Map
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page - 1) * limit);
		parameters.put("limit", limit);
		List<Map<String, Object>> onLineStoreVerifyRecord = onLineStoreService.getOnLineStoreVerifyRecord(parameters);// 线上店铺审核记录未分页
		List<Map<String, Object>> onLineStoreVerifyRecordPage = onLineStoreService
				.getOnLineStoreVerifyRecordPage(parameters);// 线上店铺审核记录已分页
		for (Map<String, Object> storeInfo : onLineStoreVerifyRecordPage) {
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", onLineStoreVerifyRecordPage);
		map.put("count", onLineStoreVerifyRecord.size());
		return map;
	}

	/**
	 * 转到添加店铺页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toAddOnLineStore")
	public ModelAndView toAddOnLineStore(ModelAndView modelAndView) {
		List<Map<String, Object>> storeType = onLineStoreService.getStoreType();
		modelAndView.addObject("storeType", storeType);// 查询店铺类型
		List<Map<String, Object>> storeLevel = onLineStoreService.getStoreLevel();
		modelAndView.addObject("storeLevel", storeLevel);// 查询店铺等级
		List<Map<String, Object>> storeClass = onLineStoreService.getStoreClass("0");
		modelAndView.addObject("storeClass", storeClass);// 查询店铺一级分类
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/addOnLineStore");
		return modelAndView;
	}

	/**
	 * 添加店铺
	 * 
	 * @param modelAndView
	 * @param storeIcon
	 * @param license
	 * @param idcardup
	 * @param idcarddown
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("addOnLineStore")
	public ModelAndView addOnLineStore(ModelAndView modelAndView, @RequestParam("storeIcon") MultipartFile storeIcon,
			@RequestParam("license") MultipartFile license, @RequestParam("idcardup") MultipartFile idcardup,
			@RequestParam("idcarddown") MultipartFile idcarddown, String storeName, String userPhone,
			String servicePhone, String is, String storeType, String classes, String storeLevel, String sort)
			throws IllegalStateException, IOException {
		String storeId = UUID.randomUUID().toString().replaceAll("-", "");// 店铺Id
		String userId = onLineStoreService.getUserIdByPhone(userPhone);// 根据用户手机号获取用户id
		// 改变此用户为系统用户
		onLineStoreService.changeUserLevelSystem(userId);
		// 添加此用户角色为店家
		onLineStoreService.addUserMerchant(userId);
		// 添加店铺前删除此用户待审核的线上店铺申请记录
		List<Map<String, Object>> store = onLineStoreService.getUserOnLineStoreDSH(userId);
		if (store.size() > 0) {
			for (Map<String, Object> s : store) {
				String sId = (String) s.get("STORE_ID");
				String scId = (String) s.get("STORE_CERTIFICATION_ID");
				onLineStoreService.deleteStore(sId);// 删除店铺
				onLineStoreService.deleteStoreCertification(scId);// 删除店铺认证
			}
		}
		// 添加店铺
		onLineStoreService.addStore(storeId, userId, classes, storeName, servicePhone);
		// 修改店铺排序
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("storeId", storeId);
		parameters.put("sort", sort);
		onLineStoreService.changeSort(parameters);
		// 添加店铺认证
		String verifyId = UUID.randomUUID().toString().replaceAll("-", "");// 店铺认证Id
		onLineStoreService.addStoreVerify(verifyId, storeId, storeType, storeLevel, is);
		// 添加店铺图标
		if (!storeIcon.isEmpty()) {
			String storeIconUrl = UploadFileUtils.uploadFile(storeIcon, "store");
			onLineStoreService.addStoreIcon(storeId, storeIconUrl);
		}
		// 添加店铺营业执照
		if (!license.isEmpty()) {
			String licenseUrl = UploadFileUtils.uploadFile(license, "store");
			onLineStoreService.addLicense(verifyId, licenseUrl);
		}
		// 添加身份证正面照
		if (!idcardup.isEmpty()) {
			String idcardupUrl = UploadFileUtils.uploadFile(idcardup, "store");
			onLineStoreService.addIdCardUp(verifyId, idcardupUrl);
		}
		// 添加身份证反面照
		if (!idcarddown.isEmpty()) {
			String idcarddownUrl = UploadFileUtils.uploadFile(idcarddown, "store");
			onLineStoreService.addIdCardDown(verifyId, idcarddownUrl);
		}
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreList");
		return modelAndView;
	}

	/**
	 * 根据用户手机号判断用户是否存在以及是否实名认证
	 * 
	 * @param userPhone
	 * @return
	 */
	@RequestMapping("userIsExist")
	@ResponseBody
	public String userIsExist(String userPhone) {
		// 判断用户是否存在
		List<Map<String, Object>> users = onLineStoreService.getUserByPhone(userPhone);
		if (users.size() == 0) {
			return "此用户不存在";
		} else {
			String userId = (String) users.get(0).get("USERS_ID");
			// 判断此用户是否进行实名认证
			List<Map<String, Object>> verify = onLineStoreService.getUserVerify(userId);
			// 判断此用户是否已有线上店铺
			List<Map<String, Object>> store = onLineStoreService.getUserOnLineStore(userId);
			if (verify.size() == 0) {
				return "此用户未进行实名认证";
			} else if (store.size() > 0) {
				return "此用户已有线上店铺";
			} else {
				return "ok";
			}
		}
	}

	/**
	 * 转到线上店铺详情页面
	 * 
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toOnLineStoreDetail")
	public ModelAndView toOnLineStoreDetail(ModelAndView modelAndView, String storeId) {
		Map<String, Object> details = onLineStoreService.getVerifyInfo(storeId);
		if (details.get("WAITING_STATE") == null) {
			details.put("WAITING_STATE", "");
		}
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
		if (details.get("STORE_ICON") == null) {
			details.put("STORE_ICON", "");
		}
		modelAndView.addObject("details", details);// 查询店铺认证信息
		List<Map<String, Object>> storeType = onLineStoreService.getStoreType();
		modelAndView.addObject("storeType", storeType);// 查询店铺类型
		List<Map<String, Object>> storeLevel = onLineStoreService.getStoreLevel();
		modelAndView.addObject("storeLevel", storeLevel);// 查询店铺等级
		List<Map<String, Object>> storeClass = onLineStoreService.getStoreClass("0");
		modelAndView.addObject("storeClass", storeClass);// 查询店铺一级分类
		String oldClassName = onLineStoreService.getOldClassName(storeId);
		modelAndView.addObject("oldClassName", oldClassName);// 查询店铺已选分类名
		String oldClass = onLineStoreService.getOldClass(storeId);
		if (oldClass == null) {
			oldClass = "";
		}
		modelAndView.addObject("oldClass", oldClass);// 查询店铺已选分类
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreDetail");
		return modelAndView;
	}

	/**
	 * 判断店铺名是否重复不包括自己
	 * 
	 * @param storeName
	 * @return
	 */
	@RequestMapping("storeNameIsExist")
	@ResponseBody
	public boolean storeNameIsExist(String storeName, String storeId) {
		List<Map<String, Object>> list = onLineStoreService.getStoreByName(storeName, storeId);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断店铺名是否重复
	 * 
	 * @param storeName
	 * @return
	 */
	@RequestMapping("storeNameIsExist2")
	@ResponseBody
	public boolean storeNameIsExist2(String storeName) {
		List<Map<String, Object>> list = onLineStoreService.getStoreByName2(storeName);// 判断店铺名是否重复
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 改变店铺等级下月生效
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("changeWaitingState")
	@ResponseBody
	public int changeWaitingState(HttpServletRequest request) {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		String id = (String) parameters.get("id");
		String level = (String) parameters.get("level");
		String storeLevel = onLineStoreService.getStoreLevelById(id);// 得到某个店铺的等级
		if (!storeLevel.equals(level)) {
			onLineStoreService.changeWaitingState(parameters);
		} else {
			onLineStoreService.clearWaitingState(id);// 如果修改的店铺等级就是目前的店铺等级，那么清空waitingstate的值
		}
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
		List<Map<String, Object>> onLineStoreList = onLineStoreService.getOnLineStoreList(parameters);
		return onLineStoreList.size();
	}

	/**
	 * 导出线上店铺的流水
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
		List<Map<String, Object>> onLineStoreList = onLineStoreService.getOnLineStoreList(parameters);
		for (Map<String, Object> map : onLineStoreList) {
			String storeId = (String) map.get("STORE_ID");
			List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
			// 按月查询
			if (parameters.get("timeType").equals("month")) {
				String month = (String) parameters.get("month");
				Map<String, Object> monthWater = onLineStoreService.getStoreWater(storeId, "APP支付收益", month);// 查询店铺某月的APP支付收益
				waters.add(monthWater);
			}
			// 按日查询
			else {
				String startTime = (String) parameters.get("startTime");
				String endTime = (String) parameters.get("endTime");
				List<String> days = DateFormatUtils.getDays(startTime, endTime);
				for (String day : days) {
					Map<String, Object> dayWater = onLineStoreService.getStoreWater(storeId, "APP支付收益", day);// 查询店铺某天的APP支付收益
					waters.add(dayWater);
				}
			}
			map.put("waters", waters);
		}
		HSSFWorkbook wb = onLineStoreExport(onLineStoreList);

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
	public HSSFWorkbook onLineStoreExport(List<Map<String, Object>> onLineStoreList) {
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("线上店铺流水");
		// 表头
		HSSFRow row0 = sheet.createRow(0);
		HSSFCell cell0 = row0.createCell(0);
		cell0.setCellValue("店铺名");
		HSSFCell cell1 = row0.createCell(1);
		cell1.setCellValue("店铺类型");
		HSSFCell cell2 = row0.createCell(2);
		cell2.setCellValue("店铺等级");
		HSSFCell cell3 = row0.createCell(3);
		cell3.setCellValue("店主信息");
		HSSFCell cell4 = row0.createCell(4);
		cell4.setCellValue("所在区域");
		HSSFCell cell5 = row0.createCell(5);
		cell5.setCellValue("是否自营");
		HSSFCell cell6 = row0.createCell(6);
		cell6.setCellValue("站长信息");
		HSSFCell cell7 = row0.createCell(7);
		cell7.setCellValue("时间");
		HSSFCell cell8 = row0.createCell(8);
		cell8.setCellValue("店铺流水");
		HSSFCell cell9 = row0.createCell(9);
		cell9.setCellValue("佣金");
		int count = 1;// 第几行
		for (int i = 0; i < onLineStoreList.size(); i++) {
			List<Map<String, Object>> waters = (List<Map<String, Object>>) onLineStoreList.get(i).get("waters");
			for (int j = 0; j < waters.size(); j++) {
				HSSFRow row1 = sheet.createRow(count);
				row1.createCell(0).setCellValue(onLineStoreList.get(i).get("STORE_NAME").toString());
				row1.createCell(1).setCellValue(onLineStoreList.get(i).get("STORE_TYPE").toString());
				row1.createCell(2).setCellValue(onLineStoreList.get(i).get("LEVELS").toString());
				if (onLineStoreList.get(i).get("UC_NAMES") == null || onLineStoreList.get(i).get("PHONE") == null) {
					row1.createCell(3).setCellValue("");
				} else {
					row1.createCell(3).setCellValue(onLineStoreList.get(i).get("UC_NAMES").toString() + "("
							+ onLineStoreList.get(i).get("PHONE").toString() + ")");
				}
				if (onLineStoreList.get(i).get("PROVINCE") == null || onLineStoreList.get(i).get("CITY") == null
						|| onLineStoreList.get(i).get("AREA") == null) {
					row1.createCell(4).setCellValue("");
				} else {
					row1.createCell(4)
							.setCellValue(onLineStoreList.get(i).get("PROVINCE").toString()
									+ onLineStoreList.get(i).get("CITY").toString()
									+ onLineStoreList.get(i).get("AREA").toString());
				}
				if (onLineStoreList.get(i).get("IS").toString().equals("1")) {
					row1.createCell(5).setCellValue("自营");
				} else {
					row1.createCell(5).setCellValue("非自营");
				}
				if (onLineStoreList.get(i).get("zzName") == null || onLineStoreList.get(i).get("zzPhone") == null) {
					row1.createCell(6).setCellValue("");
				} else {
					row1.createCell(6).setCellValue(
							onLineStoreList.get(i).get("zzName") + "(" + onLineStoreList.get(i).get("zzPhone") + ")");
				}
				row1.createCell(7).setCellValue(waters.get(j).get("time").toString());
				row1.createCell(8).setCellValue(waters.get(j).get("MONEY").toString());
				row1.createCell(9).setCellValue(waters.get(j).get("COMMISSION").toString());
				count++;// 行数加一
			}
		}
		return wb;
	}

	/**
	 * 转到店铺月流水页面
	 * 
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toMonthWater")
	public ModelAndView toMonthWater(ModelAndView modelAndView, String storeId) {
		modelAndView.addObject("storeId", storeId);
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreMonthWater");
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
	@RequestMapping("getOnLineStoreMonthWater")
	@ResponseBody
	public Map<String, Object> getOnLineStoreMonthWater(String storeId, int page, int limit) {
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
			Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
			storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("PHONE") + ")");
			storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
			Map<String, Object> water = onLineStoreService.getStoreWater(storeId, "APP支付收益", m);// 查询每个月的订单流水
			storeInfo.put("MONEY", water.get("MONEY"));
			storeInfo.put("COMMISSION", water.get("COMMISSION"));
			storeInfo.put("time", m);
			list.add(storeInfo);
		}
		// 分页数据
		for (int i = which; i < which + limit; i++) {
			if (month.size() > i) {
				Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
				storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("PHONE") + ")");
				storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
				String province = (String) storeInfo.get("PROVINCE");
				String city = (String) storeInfo.get("CITY");
				String area = (String) storeInfo.get("AREA");
				storeInfo.put("area", province + city + area);
				Map<String, Object> water = onLineStoreService.getStoreWater(storeId, "APP支付收益", month.get(i));// 查询每个月的订单流水
				storeInfo.put("MONEY", water.get("MONEY"));
				storeInfo.put("COMMISSION", water.get("COMMISSION"));
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
	 * 转到店铺日流水页面
	 * 
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toDayWater")
	public ModelAndView toDayWater(ModelAndView modelAndView, String storeId, String month) {
		modelAndView.addObject("storeId", storeId);
		modelAndView.addObject("month", month);
		modelAndView.setViewName("/system/operationsManage/onLineStoreManage/onLineStoreDayWater");
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
	@RequestMapping("getOnLineStoreDayWater")
	@ResponseBody
	public Map<String, Object> getOnLineStoreDayWater(String storeId, String month, int page, int limit) {
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
			Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
			storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("PHONE") + ")");
			storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
			String province = (String) storeInfo.get("PROVINCE");
			String city = (String) storeInfo.get("CITY");
			String area = (String) storeInfo.get("AREA");
			storeInfo.put("area", province + city + area);
			Map<String, Object> water = onLineStoreService.getStoreWater(storeId, "APP支付收益", d);// 查询每个月的订单流水
			storeInfo.put("MONEY", water.get("MONEY"));
			storeInfo.put("COMMISSION", water.get("COMMISSION"));
			storeInfo.put("time", d);
			list.add(storeInfo);
		}
		// 分页数据
		for (int i = which; i < which + limit; i++) {
			if (days.size() > i) {
				Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
				storeInfo.put("storeUser", storeInfo.get("UC_NAMES") + "(" + storeInfo.get("PHONE") + ")");
				storeInfo.put("zz", storeInfo.get("zzName") + "(" + storeInfo.get("zzPhone") + ")");
				String province = (String) storeInfo.get("PROVINCE");
				String city = (String) storeInfo.get("CITY");
				String area = (String) storeInfo.get("AREA");
				storeInfo.put("area", province + city + area);
				Map<String, Object> water = onLineStoreService.getStoreWater(storeId, "APP支付收益", days.get(i));// 查询每个月的订单流水
				storeInfo.put("MONEY", water.get("MONEY"));
				storeInfo.put("COMMISSION", water.get("COMMISSION"));
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
	public void exportMonthWater(HttpServletResponse response, String[] monthArray, String storeId) throws IOException {
		List<Map<String, Object>> onLineStoreList = new ArrayList<Map<String, Object>>();
		for (String m : monthArray) {
			Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
			List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
			Map<String, Object> monthWater = onLineStoreService.getStoreWater(storeId, "APP支付收益", m);// 查询店铺某月的APP支付收益
			waters.add(monthWater);
			storeInfo.put("waters", waters);
			onLineStoreList.add(storeInfo);
		}

		HSSFWorkbook wb = onLineStoreExport(onLineStoreList);

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
	public void exportDayWater(HttpServletResponse response, String[] dayArray, String storeId) throws IOException {
		List<Map<String, Object>> onLineStoreList = new ArrayList<Map<String, Object>>();
		Map<String, Object> storeInfo = onLineStoreService.getOnLineStoreInfoById(storeId);// 得到店铺的信息
		List<Map<String, Object>> waters = new ArrayList<Map<String, Object>>();
		for (String day : dayArray) {
			Map<String, Object> dayWater = onLineStoreService.getStoreWater(storeId, "APP支付收益", day);// 查询店铺某天的APP支付收益
			waters.add(dayWater);
		}
		storeInfo.put("waters", waters);
		onLineStoreList.add(storeInfo);

		HSSFWorkbook wb = onLineStoreExport(onLineStoreList);

		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=" + new Date().getTime() + ".xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}

	/**
	 * 得到线上店铺的分类
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping("getOnLineStoreClasses")
	@ResponseBody
	public Map<String, Object> getOnLineStoreClasses(String storeId) {
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		List<Map<String, Object>> storeClasses = new ArrayList<Map<String, Object>>();
		String oldClass = onLineStoreService.getOldClass(storeId);// 店铺已有的分类
		if (oldClass == null) {
			oldClass = "";
		}
		String[] classes = oldClass.split(",");
		for (String c : classes) {
			Map<String, Object> classInfo = onLineStoreService.getClassInfoById(c);// 通过分类Id得到分类的信息
			storeClasses.add(classInfo);
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", storeClasses);
		map.put("count", storeClasses.size());
		return map;
	}

	/**
	 * 删除店铺的某个分类
	 * 
	 * @param classId
	 * @param storeId
	 * @return
	 */
	@RequestMapping("deleteClass")
	@ResponseBody
	public String deleteClass(String classId, String storeId) {
		// 判断此店铺的此分类中是否存在商品
		List<Map<String, Object>> goods = onLineStoreService.getGoodsByClassAndStore(classId, storeId);
		if (goods.size() > 0) {
			return "此分类下存在商品，无法删除";
		} else {
			String oldClass = onLineStoreService.getOldClass(storeId);// 店铺已有的分类
			String[] classArray = oldClass.split(",");
			String classes = "";// classes就是删除一个分类后的店铺分类
			int j = 0;
			for (int i = 0; i < classArray.length; i++) {
				if (!classArray[i].equals(classId)) {
					if (j > 0) {
						classes += ",";
					}
					classes += classArray[i];
					j++;
				}
			}
			onLineStoreService.updateStoreClass(storeId, classes);// 修改店铺的分类
			return "ok";
		}
	}

	/**
	 * 添加分类
	 * 
	 * @param storeId
	 * @param classId
	 * @return
	 */
	@RequestMapping("addClass")
	@ResponseBody
	public String addClass(String storeId, String classId) {
		String classes = onLineStoreService.getOldClass(storeId);// 店铺已有的分类
		if (classes == null || classes.equals("")) {
			classes = "";
			classes += classId;
		} else {
			String[] classArray = classes.split(",");
			for (String c : classArray) {
				if (c.equals(classId)) {
					return "此分类已存在";
				}
			}
			classes += "," + classId;
		}
		onLineStoreService.updateStoreClass(storeId, classes);// 修改店铺的分类
		return "ok";
	}

	/**
	 * 添加店铺时得到线上店铺的分类
	 * 
	 * @param storeId
	 * @return
	 */
	@RequestMapping("getOnLineStoreClasses2")
	@ResponseBody
	public Map<String, Object> getOnLineStoreClasses2(String cl) {
		Map<String, Object> map = new HashMap<String, Object>();// 传给页面的Map
		List<Map<String, Object>> storeClasses = new ArrayList<Map<String, Object>>();
		if (!cl.equals("")) {
			String[] classes = cl.split(",");
			for (String c : classes) {
				Map<String, Object> classInfo = onLineStoreService.getClassInfoById(c);// 通过分类Id得到分类的信息
				storeClasses.add(classInfo);
			}
		}
		map.put("code", 0);
		map.put("msg", "");
		map.put("data", storeClasses);
		map.put("count", storeClasses.size());
		return map;
	}

	/**
	 * 修改店铺的排序
	 * 
	 * @param storeId
	 * @param sort
	 * @return
	 */
	@RequestMapping("changeSort")
	@ResponseBody
	public int changeSort(String storeId, String sort) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("storeId", storeId);
		parameters.put("sort", sort);
		onLineStoreService.changeSort(parameters);
		return 0;
	}
}
