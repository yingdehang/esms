package com.example.elephantshopping.controller.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.CommentsService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 评论管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("comments")
public class CommentsContoller 
{
	@Autowired
	private CommentsService commentsService;
	
	/**
	 * 转到评论列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toCommentsList")
	public ModelAndView toShoppinCarList(ModelAndView modelAndView)
	{
		List<Map<String, Object>> store = commentsService.getOnLineStore();// 查询所有线上店铺
		modelAndView.addObject("store", store);
		modelAndView.setViewName("/system/operationsManage/commentsManage/commentsList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询评论数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getCommentsList")
	@ResponseBody
	public Map<String,Object> getShoppingCarList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> shoppingCarList = commentsService.getCommentsList(parameters);//未分页的数据List
		List<Map<String,Object>> shoppingCarListPage = commentsService.getCommentsListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",shoppingCarListPage);
		map.put("count",shoppingCarList.size());
		return map;
	}
	
	/**
	 * 根据ID删除某条评论
	 * @param commentsId
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public int delete(String commentsId)
	{
		commentsService.delete(commentsId);
		return 0;
	}
	
	/**
	 * 查询店铺商品
	 * @param storeId
	 * @return
	 */
	@RequestMapping("getStoreGoods")
	@ResponseBody
	public List<Map<String,Object>> getStoreGoods(String storeId)
	{
		return commentsService.getStoreGoods(storeId);
	}
	
	/**
	 * 查询商品属性
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("getGoodsAttribute")
	@ResponseBody
	public List<Map<String,Object>> getGoodsAttribute(String goodsId)
	{
		return commentsService.getGoodsAttribute(goodsId);
	}
	
	/**
	 * 随机抽取用户(积分小于1，未评价过此商品)
	 * @param goodsId
	 * @return
	 */
	@RequestMapping("getRandomUser")
	@ResponseBody
	public Map<String,Object> getRandomUser(String goodsId)
	{
		List<Map<String,Object>> users = commentsService.getUsers();//取出所有积分小于1的用户
		Map<String,Object> user = null;
		for(int i=0;i<users.size();i++){
			String userId = users.get(i).get("USERS_ID").toString();
			List<Map<String,Object>> comment = commentsService.getUserComments(userId,goodsId);//查询用户对某个商品的评价
			if(comment.size()==0){
				user = users.get(i);
				break;
			}
		}
		return user;
	}
	
	/**
	 * 添加评论
	 * @param request
	 * @return
	 */
	@RequestMapping("addComment")
	@ResponseBody
	public synchronized int addComment(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		commentsService.addComment(parameters);
		return 0;
	}
}
