package com.example.elephantshopping.dao.systemManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ImageDao {
	/**
	 * 获取器图片list
	 * 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> getImageList(Map map);

	/**
	 * 获取图片总数
	 * 
	 * @param map
	 * @return
	 */
	int getImageListCount(Map map);

	/**
	 * 获取图片类型
	 * 
	 * @param dICTIONARY_ID
	 * @return
	 */
	List<Map<String, Object>> getptlist();

	/**
	 * 添加图片
	 * 
	 * @param map
	 */
	void addImage(Map<String, Object> map);

	/**
	 * 删除图片
	 */
	void deleteImageById(String PHOTO_ID);

	/**
	 * 修改点击链接地址
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateClickUrl(Map<String, Object> requestToMap);

	/**
	 * 修改图片
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updateImage(Map<String, Object> requestToMap);

	/**
	 * 修改图片名称
	 * 
	 * @param requestToMap
	 * @return
	 */
	int updatePHOTONAME(Map<String, Object> requestToMap);
}
