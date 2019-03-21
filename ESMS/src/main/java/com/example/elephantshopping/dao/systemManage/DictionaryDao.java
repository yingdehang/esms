package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DictionaryDao {
	/**
	 * 获取字典菜单
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getDictionaryList(Map map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 * @return
	 */
	int getDictionaryListCount(Map map);

	/**
	 * 删除字典
	 * 
	 * @param dICTIONARY_ID
	 */
	void deleteDictionaryById(String dICTIONARY_ID);

	/**
	 * 查询父类的pid
	 * 
	 * @param pID
	 * @return
	 */
	String queryDictionaryParentId(String pID);

	/**
	 * 添加字典
	 * 
	 * @param requestToMap
	 */
	void addDictionary(Map requestToMap);

	/**
	 * 修改字典
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateDictionary(Map requestToMap);

	/**
	 * 查询字典是否有子类
	 * 
	 * @param object
	 * @return
	 */
	int isHaveSon(Object object);

}
