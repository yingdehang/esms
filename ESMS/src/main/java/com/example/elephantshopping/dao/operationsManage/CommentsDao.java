package com.example.elephantshopping.dao.operationsManage;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 评论管理Dao
 * @author XB
 *
 */
@Mapper
public interface CommentsDao
{
	/**
	 * 未分页的评论数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getCommentsList(Map<String, Object> parameters);

	/**
	 * 分页的评论数据
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getCommentsListPage(Map<String, Object> parameters);

	/**
	 * 根据ID删除某条评论
	 * @param commentsId
	 */
	void delete(String commentsId);

	/**
	 * 查询所有线上店铺
	 * @return
	 */
	List<Map<String, Object>> getOnLineStore();

	/**
	 * 查询店铺商品
	 * @param storeId
	 * @return
	 */
	List<Map<String, Object>> getStoreGoods(String storeId);

	/**
	 * 查询商品属性
	 * @param goodsId
	 * @return
	 */
	List<Map<String, Object>> getGoodsAttribute(String goodsId);

	/**
	 * 取出所有积分小于1的用户
	 * @return
	 */
	List<Map<String, Object>> getUsers();

	/**
	 * 查询用户对某个商品的评价
	 * @param parameters
	 * @return
	 */
	List<Map<String, Object>> getUserComments(Map<String, Object> parameters);

	/**
	 * 添加评论
	 * @param parameters
	 */
	void addComment(Map<String, Object> parameters);
}
