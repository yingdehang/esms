package com.example.elephantshopping.service.systemManage;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.elephantshopping.dao.systemManage.DictionaryDao;
import com.example.elephantshopping.utils.DateFormatUtils;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class DictionaryService {
	@Autowired
	private DictionaryDao dictionaryDao;

	/**
	 * 获取字典list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String,Object> getDictionaryList(Map<String,Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = dictionaryDao.getDictionaryList(map);
		// 查询是否有子类
		for (Map<String, Object> m : list) {
			m.put("isHaveSon", dictionaryDao.isHaveSon(m.get("DICTIONARY_ID")));
		}
		int count = dictionaryDao.getDictionaryListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", DateFormatUtils.timeforDateType(list, "CREATE_TIME"));
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 删除字典
	 * 
	 * @param dICTIONARY_ID
	 */
	@Transactional
	public void deleteDictionaryById(String dICTIONARY_ID) {
		dictionaryDao.deleteDictionaryById(dICTIONARY_ID);
	}

	/**
	 * 查询父菜单的pid
	 * 
	 * @param pID
	 * @return
	 */
	public String queryDictionaryParentId(String pID) {
		return dictionaryDao.queryDictionaryParentId(pID);
	}

	/**
	 * 添加字典
	 * 
	 * @param requestToMap
	 */
	@Transactional
	public void addDictionary(Map<String,Object> requestToMap) {
		requestToMap.put("uuid", UUIDUtils.randomID());
		dictionaryDao.addDictionary(requestToMap);
	}

	/**
	 * 修改字典
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateDictionary(Map<String,Object> requestToMap) {
		return dictionaryDao.updateDictionary(requestToMap);
	}

}
