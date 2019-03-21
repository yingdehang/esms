package com.example.elephantshopping.controller.systemManage;

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
import com.example.elephantshopping.service.systemManage.ImageService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

@Controller
@RequestMapping("image")
public class ImageController {
	@Autowired
	private ImageService imageService;
	@Autowired
	private PermissionsService permissionsService;

	/**
	 * 跳转图片管理页面
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toImageManageHtml")
	public ModelAndView toImageManageHtml(ModelAndView mav, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		Map<String, Object> m = permissionsService.queryPermission(map);
		mav.addObject("typelist", imageService.getPhotoType());
		mav.addObject("query", m.get("query"));
		mav.setViewName("/system/systemManage/imageManage/imageList");
		return mav;
	}

	/**
	 * 跳转首页banner管理
	 */
	@RequestMapping("toAPPhomepageManageHtml")
	public ModelAndView toAPPhomepageManageHtml(ModelAndView mav) {
		mav.setViewName("/system/APPHomepageImage/APPHomepageImage");
		return mav;
	}

	/**
	 * 添加图片
	 */
	@RequestMapping("addImage")
	@ResponseBody
	public int addImage(HttpServletRequest request) {
		return imageService.addImage(RequestUtils.requestToMap(request));
	}

	/**
	 * 获取图片list
	 * 
	 * @return
	 */
	@RequestMapping("getImageList")
	@ResponseBody
	public Map<String, Object> getImageList(HttpServletRequest request) {
		return imageService.getImageList(RequestUtils.requestToMap(request));
	}

	/**
	 * 删除图片
	 */
	@RequestMapping("deleteImageById")
	@ResponseBody
	public int deleteImageById(String PHOTO_ID, String PHOTO_URL) {
		try {
			UploadFileUtils.deleteFile(PHOTO_URL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageService.deleteImageById(PHOTO_ID);
	}

	/**
	 * 上传图片
	 */
	@RequestMapping("uploadUserhead")
	@ResponseBody
	public synchronized Map<String, Object> uploadBanner(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = UploadFileUtils.uploadFile(file, "user");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("src", path);
			data.put("title", "head");
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
	 * 修改点击地址
	 */
	@ResponseBody
	@RequestMapping("updateClickUrl")
	public int updateClickUrl(HttpServletRequest request) {
		return imageService.updateClickUrl(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改图片
	 */
	@ResponseBody
	@RequestMapping("updateImage")
	public int updateImage(HttpServletRequest request) {
		return imageService.updateImage(RequestUtils.requestToMap(request));
	}
	
	/**
	 * 修改图片名称
	 */
	@ResponseBody
	@RequestMapping("updatePHOTONAME")
	public int updatePHOTONAME(HttpServletRequest request) {
		return imageService.updatePHOTONAME(RequestUtils.requestToMap(request));
	}
	
}
