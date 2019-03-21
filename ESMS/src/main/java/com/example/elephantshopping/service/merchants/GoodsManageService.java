package com.example.elephantshopping.service.merchants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.elephantshopping.dao.merchants.GoodsManageDao;
import com.example.elephantshopping.utils.UUIDUtils;

@Service
public class GoodsManageService {
	@Autowired
	private GoodsManageDao goodsManageDao;

	/**
	 * 查询商品list
	 * 
	 * @param map
	 * @return
	 */
	public Map<String, Object> getGoodsList(Map<String, Object> map) {
		int page = Integer.parseInt(map.get("page").toString());
		int limit = Integer.parseInt(map.get("limit").toString());
		page = (page - 1) * limit;
		map.put("page", page);
		map.put("limit", limit);
		List<Map<String, Object>> list = goodsManageDao.getgoodsList(map);
		int count = goodsManageDao.getgoodsListCount(map);
		map.clear();
		map.put("msg", "");
		map.put("data", list);
		map.put("count", count);
		map.put("code", 0);
		return map;
	}

	/**
	 * 批量操作
	 * 
	 * @param goodsIdArray
	 * @param gOODS_STATE
	 * @return
	 */
	@Transactional
	public int batchOperation(String[] goodsIdArray, String gOODS_STATE) {
		int i = 0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("GOODS_STATE", gOODS_STATE);
		for (String goodId : goodsIdArray) {
			map.put("GOODSID", goodId);
			i = goodsManageDao.updateGoodsState(map);
			if (i == 0) {
				return i;
			}
		}
		return i;
	}

	/**
	 * 删除商品
	 * 
	 * @param gOODSID
	 */
	@Transactional
	public void DeleteGoodsById(String gOODSID) {
		// 删除商品表中的数据
		goodsManageDao.DeleteGoodsById(gOODSID);
		// 删除商品属性表中的数据
		goodsManageDao.deleteAttributeByGoodsId(gOODSID);
		// 删除数据库商品图片
		goodsManageDao.deletePhoto(gOODSID);
	}

	/**
	 * 修改商品库存
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateGoodsInventory(Map requestToMap) {
		return goodsManageDao.updateGoodsInventory(requestToMap);
	}

	/**
	 * 修改商品价格
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateGoodsPRICE(Map requestToMap) {
		return goodsManageDao.updateGoodsPRICE(requestToMap);
	}

	/**
	 * 修改商品状态
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateGoodsState(Map requestToMap) {
		return goodsManageDao.updateGoodsState(requestToMap);
	}

	/**
	 * 查询商品信息
	 * 
	 * @param goodsId
	 * @return
	 */
	public Map<String, Object> queryGoodsInfo(String goodsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询商品表商品信息
		Map<String, Object> goods = goodsManageDao.queryGoodsInfo(goodsId);
		map.put("goods", goods);
		// 查询商品图片
		List<Map<String, Object>> goodsImg = goodsManageDao.queryGoodsImg(goodsId);
		map.put("goodsImg", goodsImg);
		// 查询商品分类
		String PID = goods.get("PID").toString();
		String CLASS_NAME = goods.get("CLASS_NAME").toString();
		String classification = getCLASSIFICATION(PID, CLASS_NAME);
		map.put("classification", classification);
		// 查询商品属性
		List<Map<String, Object>> goodsAttribute = goodsManageDao.getGoodsAttribute(goodsId);
		map.put("goodsId", goodsId);
		for (Map<String, Object> m : goodsAttribute) {
			map.put("attributeName", m.get("ATTRIBUTE_NAME"));
			m.put("attributeValues", goodsManageDao.getAttributeValue(map));
		}
		map.put("goodsAttribute", goodsAttribute);
		return map;
	}

	/**
	 * 递归查询商品多级分类
	 */
	private String getCLASSIFICATION(String pid, String classificationName) {
		if (!pid.equals("0")) {
			Map<String, Object> classification = goodsManageDao.getGoodsClassification(pid);
			String pid1 = classification.get("PID").toString();
			String classificationName1 = classification.get("CLASS_NAME").toString();
			classificationName = classificationName1 + ">>" + classificationName;
			if (pid1.equals("0")) {
				return classificationName;
			}
			classificationName = getCLASSIFICATION(pid1, classificationName);
		}
		return classificationName;
	}

	/**
	 * 修改商品排序
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateGoodsSort(Map requestToMap) {
		return goodsManageDao.updateGoodsSort(requestToMap);
	}

	/**
	 * 查询店铺商品一级分类list
	 * 
	 * @return
	 */
	public List<Map<String, Object>> getOneClassifications(String storeId) {
		// 查询店铺一级分类
		Map<String, Object> map = goodsManageDao.getStoreClassifications(storeId);
		String classifications = map.get("STORE_CLASSIFICATION_ID").toString();
		String[] classificat = classifications.split(",");
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("classifications", classificat);
		return goodsManageDao.getOneClassifications(map1);
	};

	/**
	 * 查询商品分类
	 * 
	 * @param storeId
	 * 
	 * @param string
	 * @return
	 */
	public List<Map<String, Object>> getClassifications(String pid, String storeId) {
		if (null != storeId && (!storeId.equals(""))) {
			Map<String, Object> map = goodsManageDao.getStoreClassifications(storeId);
			String classifications = map.get("STORE_CLASSIFICATION_ID").toString();
			String[] classificat = classifications.split(",");
			Map<String, Object> map1 = new HashMap<String, Object>();
			map1.put("classifications", classificat);
			map1.put("pid", pid);
			return goodsManageDao.getTwoClassifications(map1);
		}
		return goodsManageDao.getClassifications(pid);
	}

	/**
	 * 添加商品，操作商品属性方法
	 * 
	 * @param goodsId
	 * @return
	 */
	@Transactional
	public List<Map<String, Object>> goodsAttributeList(String goodsId) {
		// 查询商品属性
		List<Map<String, Object>> goodsAttribute = goodsManageDao.getGoodsAllAttribute(goodsId);
		return goodsAttribute;
	}

	/**
	 * 添加商品属性
	 * 
	 * @param attributeName
	 * @param attributeValue
	 * @return
	 */
	@Transactional
	public String addAttribute(String goodsId, String attributeName, String attributeValue) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null == goodsId || goodsId.equals("")) {
			goodsId = UUIDUtils.randomID();
		} else {
			map.put("GOODSID", goodsId);
			map.put("GOODS_STATE", "GOODS_STATE_DSH");
			// 添加商品属性后，商品就如待审核状态
			goodsManageDao.updateGoodsState(map);
		}
		map.put("attributeName", attributeName);
		map.put("goodsId", goodsId);
		String[] addarray = attributeValue.replaceAll("，", ",").split(",");
		for (String addattr : addarray) {
			map.put("attributeValue", addattr);
			map.put("uuid", UUIDUtils.randomID());
			goodsManageDao.addAttribute(map);
		}
		return goodsId;
	}

	/**
	 * 删除商品属性
	 * 
	 * @param attributeId
	 */
	@Transactional
	public void deleteAttribute(String attributeId) {
		goodsManageDao.deleteAttribute(attributeId);
	}

	/**
	 * 通过商品id删除商品属性表中的数据
	 * 
	 * @param goodsId
	 */
	@Transactional
	public void deleteAttributeByGoodsId(String goodsId) {
		goodsManageDao.deleteAttributeByGoodsId(goodsId);
	}

	/**
	 * 添加商品
	 * 
	 * @param map
	 */
	@Transactional
	public void addgoods(Map<String, Object> map) {
		String[] goodsphotos = map.get("goodsphotos").toString().split(";");
		String testgoodsId = "";
		if (null != map.get("testgoodsId") && (!map.get("testgoodsId").equals(""))) {
			testgoodsId = map.get("testgoodsId").toString();
		} else {
			testgoodsId = UUIDUtils.randomID();
			map.put("testgoodsId", testgoodsId);
		}
		String goodsName = map.get("GOODS_NAME").toString();
		// 通过用户id查询用户商店id
		if (goodsphotos.length > 0) {
			map.put("GOODS_ICON", goodsphotos[0]);
		} else {
			map.put("GOODS_ICON", "");
		}
		// 添加商品表
		map.put("ADD_TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		goodsManageDao.addgoods(map);
		// 添加图片表
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("goodsId", testgoodsId);
		m.put("PHOTO_NAME", goodsName);
		if (goodsphotos.length > 0) {
			for (String goodsphoto : goodsphotos) {
				m.put("uuid", UUIDUtils.randomID());
				m.put("PHOTO_URL", goodsphoto);
				// 添加图片
				goodsManageDao.addgoodsPhoto(m);
			}
		}

	}

	/**
	 * 获取商品图片list
	 * 
	 * @param goodsId
	 * @return
	 */
	public Map<String, Object> goodsPhotoList(String goodsId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "");
		map.put("code", 0);
		map.put("data", goodsManageDao.queryGoodsImg(goodsId));
		map.put("count", 0);
		return map;
	}

	/**
	 * 删除商品图片
	 * 
	 * @param pHOTO_ID
	 */
	@Transactional
	public void deletephoto(String pHOTO_ID) {
		goodsManageDao.deletephoto(pHOTO_ID);
	}

	/**
	 * 修改商品
	 * 
	 * @param map
	 */
	@Transactional
	public void updateGoods(Map<String, Object> map) {
		// 取出商品id
		String goodsId = map.get("goodsId").toString();
		String GOODS_ICON = goodsManageDao.queryGoodsInfo(goodsId).get("GOODS_ICON").toString();
		// 通过id删除原有商品
		String addAttributes = "";
		String delattributes = "";
		if (null != map.get("addAttributes") && (!map.get("addAttributes").equals(""))) {
			addAttributes = map.get("addAttributes").toString();
		}
		if (null != map.get("deleteAttributes") && (!map.get("deleteAttributes").equals(""))) {
			delattributes = map.get("deleteAttributes").toString();
		}
		Map<String, List<String[]>> updateAttr = tidyAttributeList(addAttributes, delattributes);
		// 修改商品属性表
		List<String[]> addAttr = updateAttr.get("addattrs");
		List<String[]> delAttr = updateAttr.get("delattrs");
		if (null != delAttr && delAttr.size() > 0) {
			// 删除属性表中用户删除的商品属性
			for (String[] del : delAttr) {
				deleteAttribute(del[0]);
			}
		}
		if (null != addAttr && addAttr.size() > 0) {
			Map<String, Object> addAtbute = new HashMap<String, Object>();
			addAtbute.put("goodsId", goodsId);
			// 插入新添加的商品属性
			for (String[] add : addAttr) {
				addAtbute.put("uuid", UUIDUtils.randomID());
				addAtbute.put("attributeName", add[0]);
				addAtbute.put("attributeValue", add[1]);
				goodsManageDao.addAttribute(addAtbute);
			}

		}
		int i = goodsManageDao.queryGoodsImg(goodsId).size();
		// 插入和删除用户删除的图片和上传新添加的图片
		if (null != map.get("deletephotos") && (!map.get("deletephotos").equals(""))) {
			String[] photourls = map.get("deletephotos").toString().split(";");
			// 通过图片url删除商品图片
			Map<String, Object> photo = new HashMap<String, Object>();
			photo.put("goodsId", goodsId);
			int biaoji = 0;
			for (String ptsurl : photourls) {
				if (GOODS_ICON.equals(ptsurl)) {
					biaoji = 1;
				}
				photo.put("photoUrl", ptsurl);
				goodsManageDao.deleteupdatePhoto(photo);
			}
			List<Map<String, Object>> imglist = goodsManageDao.queryGoodsImg(goodsId);
			i = imglist.size();
			if (biaoji == 1) {
				if (i > 0) {
					GOODS_ICON = imglist.get(0).get("PHOTO_URL").toString();
				} else {
					GOODS_ICON = "http://imagexb.test.upcdn.net/xbsc/goods/20180530111131444.png";
				}
			}
		}
		if (null != map.get("goodsphotos") && (!map.get("goodsphotos").equals(""))) {
			String[] photourls = map.get("goodsphotos").toString().split(";");
			if (GOODS_ICON.equals("http://imagexb.test.upcdn.net/xbsc/goods/20180530111131444.png")) {
				GOODS_ICON = photourls[0];
			}
			// 添加商品图片
			Map<String, Object> photo = new HashMap<String, Object>();
			photo.put("goodsId", goodsId);
			photo.put("PHOTO_NAME", map.get("goodsName"));
			for (String ptsurl : photourls) {
				// 添加图片
				photo.put("uuid", UUIDUtils.randomID());
				photo.put("PHOTO_URL", ptsurl);
				goodsManageDao.addgoodsPhoto(photo);
			}
		} else {
			if (i == 0) {
				GOODS_ICON = "http://imagexb.test.upcdn.net/xbsc/goods/20180530111131444.png";
			}
		}
		map.put("GOODS_ICON", GOODS_ICON);
		// 修改商品表
		goodsManageDao.updategoods(map);
	}

	/**
	 * 商品数组添加和删除整理
	 */
	@Transactional
	public Map<String, List<String[]>> tidyAttributeList(String addAttributes, String delattributes) {
		// 用户添加的商品属性数组
		List<String[]> addattrs = null;
		// 用户删除的商品属性数组
		List<String[]> delattrs = null;
		if (null != addAttributes && addAttributes != "") {
			String[] addattrs1 = addAttributes.split(";");
			addattrs = new ArrayList<String[]>();
			for (int i = 0; i < addattrs1.length; i++) {
				String[] addattrs2 = addattrs1[i].replaceAll("，", ",").split(",");
				if (addattrs2.length > 2) {
					for (int j = 1; j < addattrs2.length; j++) {
						String[] addbox = new String[] { addattrs2[0], addattrs2[j] };
						addattrs.add(addbox);
					}
				} else {
					addattrs.add(addattrs2);
				}
			}
		}
		if (null != delattributes && delattributes != "") {
			String[] delattrs1 = delattributes.split(";");
			delattrs = new ArrayList<String[]>();
			for (int i = 0; i < delattrs1.length; i++) {
				String[] delattrs2 = delattrs1[i].replaceAll("，", ",").split(",");
				delattrs.add(delattrs2);
			}
		}
		if (null != addAttributes && addAttributes != "" && null != delattributes && delattributes != "") {
			int j = addattrs.size();
			int m = delattrs.size();
			for (int i = 0; i < j; i++) {
				String attrName = addattrs.get(i)[0];
				String attrValue = addattrs.get(i)[1];
				for (int n = 0; n < m; n++) {
					if (delattrs.get(n)[1].equals(attrName) && delattrs.get(n)[2].equals(attrValue)) {
						addattrs.remove(i);
						i--;
						j--;
						delattrs.remove(n);
						n--;
						m--;
						break;
					}
				}
			}
		}
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		map.put("addattrs", addattrs);
		map.put("delattrs", delattrs);
		return map;
	}

	/**
	 * 获取导出商品信息
	 * 
	 * @param goodsIds
	 * @return
	 */
	public List<Map<String, Object>> getGoodsList(String goodsIds) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String[] goodsids = goodsIds.split(";");
		for (String id : goodsids) {
			list.add(goodsManageDao.queryGoodsInfo(id));
		}
		return list;
	}

	/**
	 * 查询该用户店铺内的所有商品id
	 * 
	 * @param storeId
	 * @return
	 */
	public List<String> getgoodsIdList(String storeId) {
		return goodsManageDao.getgoodsIdList(storeId);
	}

	/**
	 * 修改商品销量
	 * 
	 * @param requestToMap
	 * @return
	 */
	@Transactional
	public int updateGoodsXiaoLiang(Map<String, Object> map) {
		String storeId = map.get("storeId").toString();
		// 查询店铺信息
		Map<String, Object> store = goodsManageDao.queryStoreById(storeId);
		// 修改后的销量
		int newXiaoliang = Integer.parseInt(map.get("newXiaoliang").toString());
		// 修改前的销量
		int agoXiaoliang = Integer.parseInt(map.get("agoXiaoliang").toString());
		// 修改前的店铺销量
		int MONTH_SALES = Integer.parseInt(store.get("MONTH_SALES").toString());
		// 修改后的店铺销量
		int monthSales = MONTH_SALES + (newXiaoliang - agoXiaoliang);
		map.put("storeSales", monthSales);
		// 修改商品销量
		int i = goodsManageDao.updategoodsXiaoliang(map);
		// 修改店铺月销量
		int ii = goodsManageDao.updateStoreXiaoliang(map);
		return i > 0 && ii > 0 ? 1 : 0;
	}

	/**
	 * 查询店铺名称
	 * 
	 * @param storeId
	 * @return
	 */
	public String queryStoreNameById(String storeId) {
		Map<String, Object> map = goodsManageDao.queryStoreById(storeId);
		return map.get("STORE_NAME").toString();
	}

}
