package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.systemManage.ImageDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class ImageService {
	@Autowired
	private ImageDao imageDao;

	/**
	 * 获取list
	 * 
	 * @param requestToMap
	 * @return
	 */
	public Map<String, Object> getImageList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = imageDao.getImageList(map);
		int count = imageDao.getImageListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(list, "ADD_TIME"));
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 获取图片类型
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getPhotoType() {
		return imageDao.getptlist();
	}

	/**
	 * 添加图片
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int addImage(Map<String, Object> map) {
		map.put("uuid", UUIDUtils.randomID());
		imageDao.addImage(map);
		return 1;
	}

	/**
	 * 删除图片
	 */
	@Transactional
	public int deleteImageById(String PHOTO_ID) {
		imageDao.deleteImageById(PHOTO_ID);
		return 1;
	}

	/**
	 * 修改点击链接地址
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateClickUrl(Map<String, Object> requestToMap) {
		return imageDao.updateClickUrl(requestToMap);
	}

	/**
	 * 修改图片
	 * 
	 * @param requestToMap
	 * @return
	 */
	public int updateImage(Map<String, Object> requestToMap) {
		return imageDao.updateImage(requestToMap);
	}

	/**
	 * 修改图片名称
	 * @param requestToMap
	 * @return
	 */
	public int updatePHOTONAME(Map<String, Object> requestToMap) {
		return imageDao.updatePHOTONAME(requestToMap);
	}
}
