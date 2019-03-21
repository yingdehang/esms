package com.example.elephantshopping.controller.userManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.service.userManage.MessageService;
import com.example.elephantshopping.utils.JPushUtils;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 消息管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("message")
public class MessageController 
{
	@Autowired
	private MessageService messageServie;
	@Autowired
	private StoreVerifyService storeVerifyService;
	/**
	 * 转到消息管理页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toMessageList")
	public ModelAndView toMessageList(ModelAndView modelAndView)
	{
		List<Map<String,Object>> province  = storeVerifyService.getSubordinateArea(1);//获取所有的省
		modelAndView.addObject("province", province);
		modelAndView.setViewName("/system/userManage/messageManage/messageList");
		return modelAndView;
	}
	
	/**
	 * 获取消息数据
	 * @param page
	 * @param limit
	 * @return
	 */
	@RequestMapping("getMessageList")
	@ResponseBody
	public Map<String,Object> getMessageList(int page,int limit)
	{
		Map<String,Object> map = new HashMap<String,Object>();
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> messageList = messageServie.getMessageList();//未分页的数据List
		List<Map<String,Object>> messageListPage = messageServie.getMessageListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg",""); 
		map.put("data",messageListPage);
		map.put("count",messageList.size());
		return map;
	}
	
	/**
	 * 向所有用户推送消息
	 * @param title
	 * @param content
	 * @return
	 */
	@RequestMapping("push")
	@ResponseBody
	public int push(String title,String content)
	{
		if(content.length()>80)
		{
			JPushUtils.sendNotificationToAll(title,content.substring(0,80)+"...");
		}
		else
		{
			JPushUtils.sendNotificationToAll(title,content);
		}
		//将推送的内容存入数据库
		String mId = UUID.randomUUID().toString().replaceAll("-", "");
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("mId",mId);
		parameters.put("title",title);
		parameters.put("content",content);
		messageServie.sendSystemMessage(parameters);
		return 0;
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
		messageServie.sendAppMessage(parameters);
		return 0;
	}
}
