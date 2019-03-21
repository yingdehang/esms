package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.ActivityAmountControl;
import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.operationsManage.DeclarationService;
import com.example.elephantshopping.service.operationsManage.ExperienceService;
import com.example.elephantshopping.service.operationsManage.OrderService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.service.userManage.MessageService;
import com.example.elephantshopping.utils.JPushUtils;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.SendSMSUtils;
import com.example.elephantshopping.utils.UploadFileUtils;
/**
 * 体验专区
 * @author XB
 *
 */
@Controller
@RequestMapping("experience")
public class ExperienceController {
	@Autowired
	private ExperienceService experienceService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private DeclarationService declarationService;
	@Autowired
	private MessageService messageServie;
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private PermissionsController permissionsController;

	
	/**
	 * 转到活动管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toExperienceActivity")
	public ModelAndView toExperienceActivity(ModelAndView modelAndView){
		modelAndView.setViewName("/system/operationsManage/experienceManage/experienceActivity");
		return modelAndView;
	}
	
	/**
	 * 获取所有活动列表
	 * @param request
	 * @return
	 */
	@RequestMapping("getActivitis")
	@ResponseBody
	public Map<String,Object> getActivitis(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> activitis = experienceService.getActivitis(parameters);//未分页的数据List
		List<Map<String,Object>> activitisPage = experienceService.getActivitisPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",activitisPage);
		map.put("count",activitis.size());
		return map;
	}
	
	/**
	 * 开启/关闭活动
	 * @param request
	 * @return
	 */
	@RequestMapping("turn")
	@ResponseBody
	public int turn(HttpServletRequest request) {
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		experienceService.turn(parameters);
		return 0;
	}
	
	/**
	 * 改变活动排序
	 * @param id
	 * @param sort
	 * @return
	 */
	@RequestMapping("changeSort")
	@ResponseBody
	public int changeSort(String id, String sort) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("id", id);
		parameters.put("sort", sort);
		experienceService.changeSort(parameters);
		return 0;
	}
	
	@RequestMapping("addActivityImg")
	@ResponseBody
	public synchronized Map<String,Object> addActivityImg(MultipartFile file)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		String url;
		try {
			url = UploadFileUtils.uploadFile(file,"goods");
			map.put("url",url);
			map.put("code",0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 添加活动
	 * @param request
	 * @return
	 */
	@RequestMapping("addActivity")
	@ResponseBody
	public int addActivity(HttpServletRequest request) {
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		parameters.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
		experienceService.addActivity(parameters);
		return 0;
	}
	
	/**
	 * 修改活动
	 * @param request
	 * @return
	 */
	@RequestMapping("modifyActivity")
	@ResponseBody
	public int modifyActivity(HttpServletRequest request) {
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		//判断是否修改图片
		if(parameters.get("url")!=null){//是
			String id = (String) parameters.get("id");
			Map<String,Object> activity = experienceService.getActivity(id);//通过Id得到某个活动信息
			String url = (String) activity.get("PHOTO_URL");
			UploadFileUtils.deleteFile(url);//删除原来的图片
		}
		experienceService.modifyActivity(parameters);//修改活动
		return 0;
	}
	
	/**
	 * 通过Id得到某个活动信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getActivity")
	@ResponseBody
	public Map<String,Object> getActivity(String id) {
		return experienceService.getActivity(id);
	}
	
	/**
	 * 转到活动商品管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toExperienceGoods")
	public ModelAndView toExperienceGoods(ModelAndView modelAndView,String id){
		Map<String,Object> activity = experienceService.getActivity(id);
		modelAndView.addObject("activity", activity);
		modelAndView.setViewName("/system/operationsManage/experienceManage/experienceGoods");
		return modelAndView;
	}
	
	/**
	 * 查询活动的商品
	 * @param request
	 * @return
	 */
	@RequestMapping("getActivityGoods")
	@ResponseBody
	public Map<String,Object> getActivityGoods(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> goods = experienceService.getActivityGoods(parameters);//未分页的数据List
		List<Map<String,Object>> goodsPage = experienceService.getActivityGoodsPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",goodsPage);
		map.put("count",goods.size());
		return map;
	}
	
	/**
	 * 添加活动商品
	 * @param request
	 * @return
	 */
	@RequestMapping("addActivityGoods")
	@ResponseBody
	public int addActivityGoods(HttpServletRequest request) {
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		parameters.put("goodsId", UUID.randomUUID().toString().replaceAll("-", ""));
		experienceService.addActivityGoods(parameters);
		return 0;
	}
	
	/**
	 * 通过Id得到某个商品信息
	 * @param id
	 * @return
	 */
	@RequestMapping("getActivityGoodsById")
	@ResponseBody
	public Map<String,Object> getActivityGoodsById(String id) {
		return experienceService.getActivityGoodsById(id);
	}
	
	/**
	 * 修改活动商品
	 * @param request
	 * @return
	 */
	@RequestMapping("modifyActivityGoods")
	@ResponseBody
	public int modifyActivityGoods(HttpServletRequest request) {
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		//判断是否修改图片
		if(parameters.get("url")!=null){//是
			String id = (String) parameters.get("id");
			Map<String,Object> goods = experienceService.getActivityGoodsById(id);//通过Id得到某个商品信息
			String url = (String) goods.get("GOODS_PHOTO");
			UploadFileUtils.deleteFile(url);//删除原来的图片
		}
		experienceService.modifyActivityGoods(parameters);//修改商品
		return 0;
	}
	
	/**
	 * 删除某个商品
	 * @param id
	 * @return
	 */
	@RequestMapping("deleteGoods")
	@ResponseBody
	public int deleteGoods(String id) {
		Map<String,Object> goods = experienceService.getActivityGoodsById(id);//通过Id得到某个商品信息
		String url = (String) goods.get("GOODS_PHOTO");
		UploadFileUtils.deleteFile(url);//删除服务器上的图片
		experienceService.deleteGoods(id);//删除数据库的商品数据
		return 0;
	}
	
	/**
	 * 转到礼包激活审核页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toExperienceVerify")
	public ModelAndView toExperienceVerify(ModelAndView modelAndView,HttpServletRequest request){
		modelAndView.addObject("auditRecords", permissionsController.queryPermissions("auditRecords", request));
		modelAndView.addObject("packageManagement",
				permissionsController.queryPermissions("packageManagement", request));
		modelAndView.addObject("giftBagAudit", permissionsController.queryPermissions("giftBagAudit", request));
		modelAndView.setViewName("/system/operationsManage/experienceManage/experienceVerify");
		return modelAndView;
	}
	
	
	/**
	 * 查询待审核的激活申请
	 * @param request
	 * @return
	 */
	@RequestMapping("getVerifyList")
	@ResponseBody
	public Map<String,Object> getVerifyList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> verify = experienceService.getVerify(parameters);//未分页的数据List
		List<Map<String,Object>> verifyPage = experienceService.getVerifyPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",verifyPage);
		map.put("count",verify.size());
		return map;
	}
	
	/**
	 * 礼包激活审核
	 * @param auId 审核id
	 * @param accId 体验账号id
	 * @param userId 用户id
	 * @param verifyState 审核结果
	 * @param money 礼包金额
	 * @param content 失败理由
	 * @return
	 */
	@RequestMapping("experienceVerify")
	@ResponseBody
	@Transactional
	public synchronized int experienceVerify(String auId,String accId,String userId,String verifyState,double money,String content,String activityName,double integral,int type) {
		//查询此Id审核记录,判断是否重复审核
		Map<String,Object> audit = experienceService.getAuditById(auId);
		if(audit.get("STATE").equals("1")){
			Map<String,Object> parameters = new HashMap<String,Object>();
			parameters.put("verifyState",verifyState);
			parameters.put("content", content);
			parameters.put("auId", auId);
//		//得到体验账号的礼包审核记录，判断是否是第一个礼包
//		List<Map<String,Object>> list = experienceService.getAudits(accId);
			
//		//得到体验账号的礼包审核通过记录，判断是否是第一个激活的礼包
//		List<Map<String,Object>> accesslist = experienceService.getActivateAudits(accId);
			
			//判断审核结果
			if(verifyState.equals("1")){//成功
				//改变审核状态
				experienceService.experienceVerify(parameters);
				//判断礼包类型
				if(type==1){//体验专区
					//查询体验账户的零花钱和消费券
					double pocketMoney = experienceService.getAccountPocketMoney(accId);//体验零花钱
					double consumption = experienceService.getAccountConsumptionVolume(accId);//体验消费券
					double Integral = experienceService.getAccountIntegral(accId);//体验象币
					//用户增加零花钱和消费券并添加流水
					if(pocketMoney!=0){
						orderService.changeUserPocketMoney("1", userId, pocketMoney, "体验专区返回");//零花钱
					}
					if(consumption!=0){
						orderService.changeUserConsumption("1", userId, consumption, "体验专区返回");//消费券
					}
					//购买专区增加积分零花钱和消费券
					experienceService.addBuyMoney(accId,Integral,pocketMoney,consumption);
					//清除体验专区积分，零花钱，消费券
					experienceService.clearAccountMoney(accId);
					//添加象币明细
					experienceService.addElephantDetail(1, Integral, userId, 1);
				}else{//购买专区
					//购买专区增加此礼包赠送的象币
					experienceService.addBuyMoney(accId,integral,0,0);
					//添加象币明细
					experienceService.addElephantDetail(1, integral, userId, 1);
				}
				//向用户发送短信，您已经成功购买了XXXX礼包，系统将暂停自动签到，请务必每日手动签到领取奖励金。
				String phone = experienceService.getUserPhone(userId);//得到用户电话
				SendSMSUtils.activateRemind(phone, activityName);//发送短信
				//向短信记录表存短信记录
				experienceService.addMsgRecord(phone,"您已经成功购买了"+activityName+"礼包，系统将暂停自动签到，请务必每日手动签到领取奖励金。【象本商城】");
				
				//判断用户的邀请人是否存在
				Map<String,Object> invitation = declarationService.getInviationId(userId);
				if(invitation!=null){//如果存在
					//邀请人获得礼包金额15%的消费券
					double invitationMoney = new BigDecimal(money*0.15).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					String invitationId = (String) invitation.get("USERS_ID");//邀请人Id
					orderService.changeUserConsumption("1", invitationId, invitationMoney, "激活奖励");
					String pushId = "";
					if(invitation.get("PUSH_ID")!=null){
						pushId = (String) invitation.get("PUSH_ID");//邀请人的推送ID
					}
					if(!pushId.equals("")){
						//给邀请人发送推送，说明所得消费券
						messageServie.pushById(invitationId,pushId,"温馨提示","您的分享已成功激活，您获取"+String.valueOf(invitationMoney)+"消费券。");
					}
					
					//判断用户是否已经给上级加过速
					//获取用户已经加速的礼包数量
					int count = experienceService.isSpeed(accId);
					if(count==0){//如果没加过速
						//查询用户购买的礼包的总额
						Double sumPrice = experienceService.getSumPrice(accId);
						if(sumPrice==null){
							sumPrice=0.00;
						}
						sumPrice = sumPrice+money;
						if(sumPrice>=5000){
							//判断邀请人是否有体验账号
							Map<String,Object> account = experienceService.getAccountByUserId(invitationId);
							double invitationSpeed = 0;//邀请人的加速等级
							if(account==null){//没有
								//为邀请人创建体验账号
								experienceService.createAccount(invitationId);
								invitationSpeed = 0.0005;//邀请人的加速等级
								if(!pushId.equals("")){
									//给邀请人发送推送，说明加速比例
									messageServie.pushById(invitationId,pushId,"温馨提示","您的分享已成功激活，签到提速0.0005。");
								}
							}else{//有
								//判断此账号的加速是否小于0.001
								BigDecimal speed = (BigDecimal) account.get("SPEED");
								double s = speed.doubleValue();
								invitationSpeed = s;//邀请人的加速等级
								if(s<0.001){
									//加速
									experienceService.addSpeed(account.get("EEAID").toString());
									invitationSpeed = invitationSpeed+0.0005;//邀请人的加速等级
									if(!pushId.equals("")){
										//给邀请人发送推送，说明加速比例
										if(invitationSpeed<0.001){
											messageServie.pushById(invitationId,pushId,"温馨提示","您的分享已成功激活，签到提速"+new BigDecimal(invitationSpeed).setScale(4, BigDecimal.ROUND_HALF_UP).toString()+"。");
										}else{
											messageServie.pushById(invitationId,pushId,"温馨提示","您的分享已成功激活，签到提速已达到最大值。");
										}
									}
									//修改此礼包IS_SPEED为1
									experienceService.changeActivityIsSpeed(auId);
								}
							}
						}
					}
				}
				
				//判断用户所在区域站长是否存在，是则将礼包金额2%返给他的零花钱
				Map<String,Object> zz = declarationService.getUserZZ(userId);
				if(zz!=null){
					String zzId = (String) zz.get("USERS_ID");
					double zzMoney = new BigDecimal(money*0.02).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					orderService.changeUserPocketMoney("1", zzId, zzMoney, "礼包奖励");
				}
				//判断用户的上级中是否有副站长，是则将礼包金额3%返给副站长的零花钱
				Map<String,Object> fzz = declarationService.getInvitationsFzz(userId);
				if(fzz!=null){//如果存在副站长
					String fzzId = (String) fzz.get("USERS_ID");
					double fzzMoney = new BigDecimal(money*0.03).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
					orderService.changeUserPocketMoney("1", fzzId, fzzMoney, "礼包激活");
				}
			}else{//失败
				//改变审核状态
				experienceService.experienceVerify(parameters);
				//用户增加余额并添加余额流水和收益流水
				orderService.changeUserMoneys("1",userId,money,"礼包审核失败");
			}
		}
		
		return 0;
	}
	
	/**
	 * 转到礼包审核记录页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toExperienceVerifyRecord")
	public ModelAndView toExperienceVerifyRecord(ModelAndView modelAndView){
		List<Map<String, Object>> province = storeVerifyService.getSubordinateArea(1);// 获取所有的省
		modelAndView.addObject("province", province);
		//获取所有的礼包
		List<Map<String,Object>> activity = experienceService.getActivitis(new HashMap<String,Object>());
		modelAndView.addObject("activity", activity);
		modelAndView.setViewName("/system/operationsManage/experienceManage/experienceVerifyRecord");
		return modelAndView;
	}
	
	/**
	 * 查询审核记录
	 * @param request
	 * @return
	 */
	@RequestMapping("getVerifyRecord")
	@ResponseBody
	public Map<String,Object> getVerifyRecord(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> record = experienceService.getRecord(parameters);//未分页的数据List
		List<Map<String,Object>> recordPage = experienceService.getRecordPage(parameters);//分页的数据List
		for (Map<String, Object> m : recordPage) 
		{
			String province = (String) m.get("PROVINCE");
			String city = (String) m.get("CITY");
			String area = (String) m.get("AREA");
			if (province == null) {
				province = "";
			}
			if (city == null) {
				city = "";
			}
			if (area == null) {
				area = "";
			}
			m.put("area", province + city + area);
		}
		map.put("code",0);
		map.put("msg","");
		map.put("data",recordPage);
		map.put("count",record.size());
		return map;
	}
	
	/**
	 * 导出审核记录
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("exportVerifyRecord")
	public void exportVerifyRecord(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Map<String, Object> parameters = RequestUtils.requestToMap(request);
		List<Map<String, Object>> record = experienceService.getRecord(parameters);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("礼包激活审核表");
		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row0 = sheet.createRow(0);
		row0.createCell(0).setCellValue("序号");
		row0.createCell(1).setCellValue("买家手机号");
		row0.createCell(2).setCellValue("是否实名认证");
		row0.createCell(3).setCellValue("地区");
		row0.createCell(4).setCellValue("礼包名");
		row0.createCell(5).setCellValue("礼包金额");
		row0.createCell(6).setCellValue("礼包类型");
		row0.createCell(7).setCellValue("收货人");
		row0.createCell(8).setCellValue("收货电话");
		row0.createCell(9).setCellValue("收货地址");
		row0.createCell(10).setCellValue("领取时间");
		row0.createCell(11).setCellValue("下单时间");
		row0.createCell(12).setCellValue("支付时间");
		row0.createCell(13).setCellValue("审核通过时间");
		row0.createCell(14).setCellValue("审核状态");
		for (int i = 0; i < record.size(); i++) {
			HSSFRow row1 = sheet.createRow(i + 1);
			Map<String, Object> map = record.get(i);
			row1.createCell(0).setCellValue(i + 1);
			row1.createCell(1).setCellValue(map.get("userPhone").toString());
			if (map.get("certificationState") == null) {
				row1.createCell(2).setCellValue("否");
			} else if (map.get("certificationState").equals("USER_AUTHENTICATION_PASS")) {
				row1.createCell(2).setCellValue("是");
			} else {
				row1.createCell(2).setCellValue("否");
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
			row1.createCell(3).setCellValue(province + city + area);
			row1.createCell(4).setCellValue(map.get("ACTIVITY_NAME").toString());
			row1.createCell(5).setCellValue(map.get("MONEY").toString());
			if(map.get("REMARK")!=null){
				if(map.get("REMARK").equals("1")){
					row1.createCell(6).setCellValue("体验专区");
				}else{
					row1.createCell(6).setCellValue("购买专区");
				}
			}else{
				row1.createCell(6).setCellValue("");
			}
			if(map.get("LINK_MAN")!=null){
				row1.createCell(7).setCellValue(map.get("LINK_MAN").toString());
			}else{
				row1.createCell(7).setCellValue("");
			}
			if(map.get("PHONE")!=null){
				row1.createCell(8).setCellValue(map.get("PHONE").toString());
			}else{
				row1.createCell(8).setCellValue("");
			}
			if(map.get("ADDR")!=null){
				row1.createCell(9).setCellValue(map.get("ADDR").toString());
			}else{
				row1.createCell(9).setCellValue("");
			}
			row1.createCell(10).setCellValue(map.get("ADDTIME").toString());
			row1.createCell(11).setCellValue(map.get("ORDER_TIME").toString());
			row1.createCell(12).setCellValue(map.get("PAY_TIME").toString());
			row1.createCell(13).setCellValue(map.get("PASS_TIME").toString());
			if (map.get("STATE").equals("0")) {
				row1.createCell(14).setCellValue("未激活");
			} else if(map.get("STATE").equals("1")) {
				row1.createCell(14).setCellValue("申请激活");
			} else if(map.get("STATE").equals("2")) {
				row1.createCell(14).setCellValue("审核通过");
			} else if(map.get("STATE").equals("3")) {
				row1.createCell(14).setCellValue("审核失败");
			} else if(map.get("STATE").equals("4")) {
				row1.createCell(14).setCellValue("已过期");
			} else if(map.get("STATE").equals("5")) {
				row1.createCell(14).setCellValue("已领取");
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
	 * 得到礼包的上线设置数据
	 * @param accId
	 * @return
	 */
	@RequestMapping("getSetTime")
	@ResponseBody
	public Map<String,Object> getSetTime(String id){
		Map<String,Object> map = experienceService.getSetTime(id);
		if(map!=null){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(map.get("TIME"));
			map.put("TIME", time);
		}
		return map;
	}
	
	/**
	 * 转到礼包数量控制页面
	 * @param modelAndView
	 * @param acId 礼包Id
	 * @return
	 */
	@RequestMapping("toCountManage")
	public ModelAndView toCountManage(ModelAndView modelAndView,String acId){
		modelAndView.addObject("acId", acId);
		modelAndView.setViewName("/system/operationsManage/experienceManage/countManage");
		return modelAndView;
	}
	
	/**
	 * 查询礼包设置的定时器
	 * @param request
	 * @return
	 */
	@RequestMapping("getSetTimes")
	@ResponseBody
	public Map<String,Object> getSetTimes(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> setTime = experienceService.getSetTimes(parameters);//未分页的数据List
		List<Map<String,Object>> setTimePage = experienceService.getSetTimesPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",setTimePage);
		map.put("count",setTime.size());
		return map;
	}
	
	/**
	 * 新增礼包数量定时器
	 * @param acId
	 * @param time
	 * @param fixed
	 * @param changed
	 * @return
	 */
	@RequestMapping("addSetTime")
	@ResponseBody
	public int addSetTime(String acId,String time,String fixed,String changed)
	{
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		experienceService.addSetTime(id,acId,time,fixed,changed);
		new Thread(new ActivityAmountControl(id,experienceService)).start();
		return 0;
	}
	
	/**
	 * 修改礼包数量定时器
	 * @param id
	 * @param time
	 * @param fixed
	 * @param changed
	 * @return
	 */
	@RequestMapping("updateSetTime")
	@ResponseBody
	public int updateSetTime(String id,String time,String fixed,String changed)
	{
		Map<String,Object> map = experienceService.getSetTime(id);
		if(map!=null){
			experienceService.updateSetTime(id,time,fixed,changed);
		}
		return 0;
	}
	
	/**
	 * 停止定时器
	 * @param id
	 * @return
	 */
	@RequestMapping("stopSetTime")
	@ResponseBody
	public int stopSetTime(String id)
	{
		experienceService.stopSetTime(id);
		return 0;
	}
}
