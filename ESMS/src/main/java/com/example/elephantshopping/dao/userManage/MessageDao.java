package com.example.elephantshopping.dao.userManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 消息管理Dao
 * @author XB
 *
 */
@Mapper
public interface MessageDao
{
	/**
	 * 获取未分页的消息数据
	 * @return
	 */
	public List<Map<String, Object>> getMessageList();

	/**
	 * 获取分页的消息数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getMessageListPage(Map<String, Object> parameters);

	/**
	 * 将推送的内容存入数据库
	 * @param parameters
	 */
	public void sendSystemMessage(Map<String, Object> parameters);

	/**
	 * 给个人推送消息存入数据库
	 * @param parameters
	 */
	public void sendAppMessage(Map<String, Object> parameters);

}
