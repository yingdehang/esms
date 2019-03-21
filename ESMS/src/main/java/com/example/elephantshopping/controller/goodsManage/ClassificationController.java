package com.example.elephantshopping.controller.goodsManage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.goodsManage.ClassificationService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 商品分类管理
 * 
 * @author Asus
 *
 */
@Controller
@RequestMapping("classification")
public class ClassificationController {
	@Autowired
	private ClassificationService classificationService;
	@Autowired
	private PermissionsService permissionsService;

	@RequestMapping("toClassificationHtml")
	public ModelAndView toClassificationHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.setViewName("/system/goodsManage/classificationManage/classificationList");
		return mav;
	}

	/**
	 * 获取商品分类list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getClassIficationList")
	@ResponseBody
	public Map<String, Object> getClassIficationList(HttpServletRequest request) {
		return classificationService.getClassIficationList(RequestUtils.requestToMap(request));
	}

	/**
	 * 查询父分类的父pid
	 */
	@RequestMapping("queryclassificationParentId")
	@ResponseBody
	public String queryclassificationParentId(String PID) {
		return classificationService.queryclassificationParentId(PID);
	}

	/**
	 * 删除
	 * 
	 * @param CLASSIFICATION
	 * @return
	 */
	@ResponseBody
	@RequestMapping("deleteclassification")
	public int deleteclassificationById(String CLASSIFICATION_ID) {
		return classificationService.deleteclassificationById(CLASSIFICATION_ID);
	}

	/**
	 * 添加分类
	 * 
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("addClassification")
	public int addClassification(HttpServletRequest request) {
		classificationService.addClassification(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 上传商品分类banner
	 */
	@RequestMapping("uploadBanner")
	@ResponseBody
	public synchronized Map<String, Object> uploadBanner(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = UploadFileUtils.uploadFile(file, "goods");
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
	 * 修改
	 * 
	 * @return
	 */
	@RequestMapping("updateClassification")
	@ResponseBody
	public int updateClassification(HttpServletRequest request) {
		return classificationService.updateClassification(RequestUtils.requestToMap(request));
	}

}
