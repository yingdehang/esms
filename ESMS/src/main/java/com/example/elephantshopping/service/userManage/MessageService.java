package com.example.elephantshopping.service.userManage;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.userManage.MessageDao;
import com.example.elephantshopping.utils.JPushUtils;

/**
 * 消息管理Service
 * @author XB
 *
 */
@Service
public class MessageService 
{
	@Autowired
	private MessageDao messageDao;
	
	/**
	 * 获取未分页的消息数据
	 * @return
	 */
	public List<Map<String, Object>> getMessageList()
	{
		return messageDao.getMessageList();
	}

	/**
	 * 获取分页的消息数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getMessageListPage(Map<String, Object> parameters)
	{
		List<Map<String, Object>> messageListPage = messageDao.getMessageListPage(parameters);
		for(int i=0;i<messageListPage.size();i++)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(messageListPage.get(i).get("MESSAGE_TIME"));
			messageListPage.get(i).put("MESSAGE_TIME", time);//转换一下时间格式，方便页面显示
		}
		return messageListPage;
	}

	/**
	 * 将推送的内容存入数据库
	 * @param parameters
	 */
	public void sendSystemMessage(Map<String, Object> parameters) 
	{
		messageDao.sendSystemMessage(parameters);
	}

	/**
	 * 给个人推送消息存入数据库
	 * @param parameters
	 */
	public void sendAppMessage(Map<String, Object> parameters) {
		messageDao.sendAppMessage(parameters);
	}
	
	/**
	 * 向某个用户推送消息
	 * @param userId
	 * @param pushId
	 * @param title
	 * @param content
	 * @return
	 */
	public int pushById(String userId,String pushId,String title,String content)
	{
		JPushUtils.buildPushObject_all_registrationId_alertWithTitle(pushId,title,content);
		//将推送的内容存入数据库
		String mId = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("mId",mId);
		parameters.put("userId",userId);
		parameters.put("title",title);
		parameters.put("content",content);
		messageDao.sendAppMessage(parameters);
		return 0;
	}
}
