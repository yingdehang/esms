package com.example.elephantshopping.service.operationsManage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.elephantshopping.dao.operationsManage.CommentsDao;
import com.example.elephantshopping.utils.DateFormatUtils;

/**
 * 评论管理Service
 * @author XB
 *
 */
@Service
public class CommentsService
{
	@Autowired
	private CommentsDao commentsDao;
	
	/**
	 * 未分页的评论数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getCommentsList(Map<String, Object> parameters) 
	{
		return commentsDao.getCommentsList(parameters);
	}

	/**
	 * 已分页的评论数据
	 * @param parameters
	 * @return
	 */
	public List<Map<String, Object>> getCommentsListPage(Map<String, Object> parameters)
	{
		List<Map<String,Object>> commentsListPage = commentsDao.getCommentsListPage(parameters);
		commentsListPage = DateFormatUtils.transforDateType(commentsListPage);
		return commentsListPage;
	}

	/**
	 * 根据ID删除某条评论
	 * @param commentsId
	 */
	public void delete(String commentsId)
	{
		commentsDao.delete(commentsId);
	}

	/**
	 *  查询所有线上店铺
	 * @return
	 */
	public List<Map<String, Object>> getOnLineStore() {
		return commentsDao.getOnLineStore();
	}

	/**
	 * 查询店铺商品
	 * @param storeId
	 * @return
	 */
	public List<Map<String, Object>> getStoreGoods(String storeId) {
		return commentsDao.getStoreGoods(storeId);
	}

	/**
	 * 查询商品属性
	 * @param goodsId
	 * @return
	 */
	public List<Map<String, Object>> getGoodsAttribute(String goodsId) {
		return commentsDao.getGoodsAttribute(goodsId);
	}

	/**
	 * 取出所有积分小于1的用户
	 * @return
	 */
	public List<Map<String, Object>> getUsers() {
		return commentsDao.getUsers();
	}

	/**
	 * 查询用户对某个商品的评价
	 * @param userId
	 * @param goodsId
	 * @return
	 */
	public List<Map<String, Object>> getUserComments(String userId, String goodsId) {
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put("userId", userId);
		parameters.put("goodsId", goodsId);
		return commentsDao.getUserComments(parameters);
	}

	/**
	 * 添加评论
	 * @param parameters
	 */
	public void addComment(Map<String, Object> parameters) {
		parameters.put("commentId", UUID.randomUUID().toString().replaceAll("-", ""));
		commentsDao.addComment(parameters);
	}
}
