package com.example.elephantshopping.service.goodsManage;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.goodsManage.ClassificationDao;
import com.example.elephantshopping.utils.UUIDUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 商品分类管理
 * 
 * @author Asus
 *
 */
@Service
public class ClassificationService {
	@Autowired
	private ClassificationDao classificationDao;

	/**
	 * 商品属性分类list
	 * 
	 * @param requestToMap
	 * @return
	 */
	public Map<String, Object> getClassIficationList(Map map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = classificationDao.getClassIficationList(map);
		for (Map<String, Object> m : list) {
			m.put("isHaveSon", classificationDao.isHaveSon(m.get("CLASSIFICATION_ID").toString()));
		}
		int count = classificationDao.getClassIficationListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 查询父类的pid
	 * 
	 * @param pID
	 * @return
	 */
	public String queryclassificationParentId(String pID) {
		return classificationDao.queryclassificationParentId(pID);
	}

	/**
	 * 添加
	 * 
	 * @param requestToMap
	 */
	@Transactional
	public void addClassification(Map map) {
		map.put("uuid", UUIDUtils.randomID());
		classificationDao.addClassification(map);
	}

	/**
	 * 删除
	 * 
	 * @param cLASSIFICATION_ID
	 */
	@Transactional
	public int deleteclassificationById(String cLASSIFICATION_ID) {
		int i = classificationDao.queryisHaveGoods(cLASSIFICATION_ID);
		if (i == 0) {
			classificationDao.deleteclassificationById(cLASSIFICATION_ID);
		}
		return i;
	}

	/**
	 * 修改
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateClassification(Map requestToMap) {
		if (null != requestToMap.get("deleteBanner") && (!requestToMap.get("deleteBanner").equals(""))) {
			String deleteBanner = requestToMap.get("deleteBanner").toString();
			UploadFileUtils.deleteFile(deleteBanner);
		}
		return classificationDao.updateClassification(requestToMap);
	}

}
