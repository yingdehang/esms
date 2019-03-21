package com.example.elephantshopping.controller.activityManage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.activityManage.ActivityService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UUIDUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

@Controller
@RequestMapping("activity")
public class ActivityController {
	@Autowired
	private ActivityService activityService;
	@Autowired
	private PermissionsController PermissionsController;

	/**
	 * 跳转活动管理页面
	 */
	@RequestMapping("toActivityHtml")
	public ModelAndView toActivityHtml(ModelAndView mav, HttpServletRequest request) {
		mav.addObject("openActivity", PermissionsController.queryPermissions("openActivity", request));
		mav.addObject("closeActivity", PermissionsController.queryPermissions("closeActivity", request));
		mav.addObject("addActivity", PermissionsController.queryPermissions("addActivity", request));
		mav.addObject("deleteActivity", PermissionsController.queryPermissions("deleteActivity", request));
		mav.addObject("setActivity", PermissionsController.queryPermissions("setActivity", request));
		mav.setViewName("/system/activityManage/activitylist");
		return mav;
	}

	/**
	 * 跳转活动设置页面
	 */
	@RequestMapping("toActivitySetHtml")
	public ModelAndView toActivitySetHtml(ModelAndView mav, String ESID) {
		mav.addObject("activity", activityService.queryActivityInfo(ESID));
		mav.setViewName("/system/activityManage/activitySet");
		return mav;
	}

	/**
	 * 查询活动管理list
	 */
	@RequestMapping("getActiveList")
	@ResponseBody
	public Map<String, Object> getActiveList(HttpServletRequest request) {
		return activityService.getActiveList(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改活动状态
	 */
	@RequestMapping("updateActivityState")
	@ResponseBody
	public int updateActivityState(HttpServletRequest request) {
		return activityService.updateActivityState(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改活动排序
	 */
	@RequestMapping("updateActivitySort")
	@ResponseBody
	public int updateActivitySort(HttpServletRequest request) {
		return activityService.updateActivitySort(RequestUtils.requestToMap(request));
	}

	/**
	 * 上传活动banner
	 */
	@RequestMapping("uploadBanner")
	@ResponseBody
	public synchronized Map<String, Object> uploadBanner(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = UploadFileUtils.uploadFile(file, "store");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("src", path);
			data.put("title", "banner");
			map.put("msg", "");
			map.put("code", 0);
			map.put("data", data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 添加活动
	 * 
	 */
	@RequestMapping("addactivity")
	@ResponseBody
	public int addactivity(HttpServletRequest request) {
		return activityService.addactivity(RequestUtils.requestToMap(request));
	}

	/**
	 * 删除banner
	 * 
	 */
	@RequestMapping("deletebanner")
	@ResponseBody
	public boolean deletebanner(String deleteBanner) {
		return UploadFileUtils.deleteFile(deleteBanner);
	}

	/**
	 * 删除活动
	 */
	@RequestMapping("deleteActivity")
	@ResponseBody
	public int deleteActivity(String ESID) {
		return activityService.deleteActivity(ESID);
	}

	// =============================新活动===============================
	/**
	 * 跳转活动管理页面
	 */
	@RequestMapping("toActivityManageHtml")
	public ModelAndView toActivityManageHtml(ModelAndView mav, HttpServletRequest request) {
		mav.addObject("closeNewActivity", PermissionsController.queryPermissions("closeNewActivity", request));
		mav.addObject("deleteNewActivity", PermissionsController.queryPermissions("deleteNewActivity", request));
		mav.addObject("openNewActivity", PermissionsController.queryPermissions("openNewActivity", request));
		mav.addObject("addNewActivity", PermissionsController.queryPermissions("addNewActivity", request));
		mav.addObject("updateNewActivity", PermissionsController.queryPermissions("updateNewActivity", request));
		mav.setViewName("/system/activityManage/activityManageList");
		return mav;
	}

	/**
	 * 查询首页活动list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getNewActiveList")
	@ResponseBody
	public Map<String, Object> getNewActiveList(HttpServletRequest request) {
		return activityService.getNewActiveList(RequestUtils.requestToMap(request));
	}

	/**
	 * 添加首页活动
	 */
	@RequestMapping("addNewActivity")
	@ResponseBody
	public int addNewActivity(HttpServletRequest request) {
		return activityService.addNewActivity(RequestUtils.requestToMap(request));
	}

	/**
	 * 删除首页活动
	 */
	@RequestMapping("deleteNewActivity")
	@ResponseBody
	public int deleteNewActivity(String NID) {
		return activityService.deleteNewActivity(NID);
	}

	/**
	 * 开启关闭首页活动
	 */
	@RequestMapping("updateNewActivityState")
	@ResponseBody
	public int updateNewActivityState(HttpServletRequest request) {
		return activityService.updateNewActivityState(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改排序
	 */
	@RequestMapping("updateNewActivitySort")
	@ResponseBody
	public int updateNewActivitySort(HttpServletRequest request) {
		return activityService.updateNewActivitySort(RequestUtils.requestToMap(request));
	}

	/**
	 * 跳转活动类型1页面
	 */
	@RequestMapping("typeOne")
	public ModelAndView typeOne(ModelAndView mav, String NID) {
		// 查询活动信息
		mav.addObject("activityInfo", activityService.queryNewActivityInfo(NID));
		mav.addObject("storelist", activityService.querystorelist());
		mav.setViewName("/system/activityManage/activityTypeOne");
		return mav;
	}

	/**
	 * 查询活动类型一商品list
	 */
	@RequestMapping("queryactivitygoods")
	@ResponseBody
	public Map<String, Object> queryactivitygoods(String nid, String goodsAdd, String goodsDel) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> list = activityService.querygoodslist(nid);
		if (null != goodsAdd && (!goodsAdd.equals(""))) {
			String[] goodsAdd1 = goodsAdd.split("\\|\\|;");
			for (String goods : goodsAdd1) {
				String[] good = goods.split("\\|,");
				Map<String, Object> g = new HashMap<String, Object>();
				g.put("storeId", good[0]);
				g.put("storeName", good[1]);
				g.put("goodsId", good[2]);
				g.put("goodsName", good[3]);
				g.put("dsort", good[4]);
				g.put("tid", UUIDUtils.randomID());
				list.add(g);
			}
		}
		if (null != goodsDel && (!goodsDel.equals(""))) {
			String[] goodsDel1 = goodsDel.split("\\|\\|;");
			List<String> goods = new ArrayList<>();
			for (String goo : goodsDel1) {
				goods.add(goo);
			}
			int one = list.size();
			int two = goods.size();
			for (int i = 0; one > 0 && i < one; i++) {
				Map<String, Object> m = list.get(i);
				String goodsId = m.get("goodsId").toString();
				int sort = Integer.parseInt(m.get("dsort").toString());
				for (int j = 0; two > 0 && j < two; j++) {
					String n = goods.get(j);
					String[] t = n.split("\\|,");
					String goodsI = t[0];
					int sor = Integer.parseInt(t[1]);
					if (goodsId.equals(goodsI) && sort == sor) {
						list.remove(m);
						i--;
						one--;
						goods.remove(n);
						j--;
						two--;
						break;
					}
				}
			}
		}
		map.put("msg", "");
		map.put("data", list);
		map.put("code", 0);
		map.put("count", list.size());
		return map;
	}

	/**
	 * 通过店铺id查询商品list
	 */
	@RequestMapping("querygoodslistByStoreId")
	@ResponseBody
	public List<Map<String, Object>> querygoodslistByStoreId(String storeId) {
		return activityService.querygoodslistByStoreId(storeId);
	}

	/**
	 * 活动类型1提交
	 */
	@RequestMapping("submitTypeOne")
	public ModelAndView submitTypeOne(ModelAndView mav, @RequestParam("goods") String[] goods,
			@RequestParam("sort") String[] sort, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		return toActivityManageHtml(mav, request);
	}

}
