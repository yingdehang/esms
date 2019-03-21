package com.example.elephantshopping.controller.merchants;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.controller.systemManage.PermissionsController;
import com.example.elephantshopping.service.merchants.GoodsManageService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UUIDUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

@Controller
@RequestMapping("goodsManage")
public class GoodsManageController {
	@Autowired
	private GoodsManageService goodsManageService;
	@Autowired
	private PermissionsController permissionsController;

	/**
	 * 跳转商品管理列表
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("toGoodsManageHtml")
	public ModelAndView toGoodsManageListHtml(ModelAndView mav, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String goodsState = "GOODS_STATE_UP";
		if (null != map.get("goodsState") && (!map.get("goodsState").equals(""))) {
			goodsState = map.get("goodsState").toString();
		}
		mav.addObject("exportXsGoods", permissionsController.queryPermissions("exportXsGoods", request));
		mav.addObject("addXsGoods", permissionsController.queryPermissions("addXsGoods", request));
		mav.addObject("updateXsGoods", permissionsController.queryPermissions("updateXsGoods", request));
		mav.addObject("deleteXsGoods", permissionsController.queryPermissions("deleteXsGoods", request));
		mav.addObject("xsgoodsUpAndDown", permissionsController.queryPermissions("xsgoodsUpAndDown", request));
		mav.addObject("goodsState", goodsState);
		mav.addObject("storeId", map.get("storeId"));
		mav.addObject("storeName", goodsManageService.queryStoreNameById(map.get("storeId").toString()));
		mav.setViewName("/system/merchants/goodsManage/goodsManageList");
		return mav;
	}

	/**
	 * 查询商品管理list
	 */
	@RequestMapping("getGoodsList")
	@ResponseBody
	public Map<String, Object> getGoodsList(HttpServletRequest request) {
		return goodsManageService.getGoodsList(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改商品销量
	 */
	@RequestMapping("updateGoodsXiaoLiang")
	@ResponseBody
	public int updateGoodsXiaoLiang(HttpServletRequest request) {
		return goodsManageService.updateGoodsXiaoLiang(RequestUtils.requestToMap(request));
	}

	/**
	 * 批量操作(上架或下架)
	 */
	@RequestMapping("batchOperation")
	@ResponseBody
	public int batchOperation(
			@RequestParam(value = "goodsIds[]", required = false, defaultValue = "") String[] goodsIds,
			String GOODS_STATE) {
		return goodsManageService.batchOperation(goodsIds, GOODS_STATE);
	}

	/**
	 * 批量删除
	 */
	@RequestMapping("batchDelete")
	@ResponseBody
	public int batchDelete(@RequestParam(value = "goodsIds[]", required = false, defaultValue = "") String[] goodsIds) {
		for (String goodsId : goodsIds) {
			DeleteGoodsById(goodsId);
		}
		return 1;
	}

	/**
	 * 批量导出
	 */
	@RequestMapping("batchExport")
	@ResponseBody
	public String batchExport(
			@RequestParam(value = "goodsIds[]", required = false, defaultValue = "") String[] goodsIds,
			String storeId) {
		String goodsids = "";
		if (null != goodsIds && goodsIds.length > 0) {
			for (String id : goodsIds) {
				goodsids += id + ";";
			}
		} else if (null != storeId && (!storeId.equals(""))) {
			List<String> goodsIdlist = goodsManageService.getgoodsIdList(storeId);
			if (null != goodsIdlist && goodsIdlist.size() > 0) {
				for (String id : goodsIdlist) {
					goodsids += id + ";";
				}
			}
		}
		return goodsids;
	}

	/**
	 * 批量导出2
	 * 
	 * @throws IOException
	 */
	@RequestMapping("batchExport2")
	public void batchExport2(String goodsIds, HttpServletResponse response) throws IOException {
		List<Map<String, Object>> goodslist = goodsManageService.getGoodsList(goodsIds);
		// 创建HSSFWorkbook对象(excel的文档对象)
		HSSFWorkbook wb = new HSSFWorkbook();
		// 建立新的sheet对象（excel的表单）
		HSSFSheet sheet = wb.createSheet("商品信息一览表");

		// 生成一个样式
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中

		// 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
		HSSFRow row1 = sheet.createRow(0);
		// 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
		HSSFCell cell = row1.createCell(0);
		// 设置单元格内容
		cell.setCellValue("商品信息一览表");
		// 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 5));
		// 在sheet里创建第二行
		HSSFRow row2 = sheet.createRow(1);
		// 创建单元格并设置单元格内容
		row2.createCell(0).setCellValue("商品序号");
		row2.createCell(1).setCellValue("商品名称");
		row2.createCell(2).setCellValue("库存");
		row2.createCell(3).setCellValue("单价");
		row2.createCell(4).setCellValue("状态");
		row2.createCell(5).setCellValue("排序");
		for (int i = 0; i < goodslist.size(); i++) {
			// 在sheet里创建第三行
			HSSFRow row3 = sheet.createRow(i + 2);
			Map<String, Object> map = goodslist.get(i);
			row3.createCell(0).setCellValue(i + 1);
			row3.createCell(1).setCellValue(map.get("GOODS_NAME").toString());
			row3.createCell(2).setCellValue(map.get("INVENTORY").toString());
			row3.createCell(3).setCellValue(map.get("PRESENT_PRICE").toString());
			String state = map.get("GOODS_STATE").toString();
			switch (state) {
			case "GOODS_STATE_UP":
				row3.createCell(4).setCellValue("上架状态");
				break;
			case "GOODS_STATE_DOWN":
				row3.createCell(4).setCellValue("下架状态");
				break;
			case "GOODS_STATE_DSH":
				row3.createCell(4).setCellValue("待审核");
				break;
			case "GOODS_STATE_FAIL":
				row3.createCell(4).setCellValue("审核失败");
				break;
			case "GOODS_STATE_QZ_DOWN":
				row3.createCell(4).setCellValue("强制下架");
				break;
			default:
				break;
			}
			row3.createCell(5).setCellValue(map.get("STORE_SORT").toString());
		}
		// 输出Excel文件
		OutputStream output = response.getOutputStream();
		response.reset();
		response.setHeader("Content-disposition", "attachment; filename=details.xls");
		response.setContentType("application/msexcel");
		wb.write(output);
		output.close();
	}

	/**
	 * 删除商品
	 * 
	 * @param GOODSID
	 * @return
	 */
	@RequestMapping("DeleteGoods")
	@ResponseBody
	public int DeleteGoodsById(String GOODSID) {
		goodsManageService.DeleteGoodsById(GOODSID);
		return 1;
	}

	/**
	 * 修改商品库存
	 */
	@ResponseBody
	@RequestMapping("updateGoodsInventory")
	public int updateGoodsInventory(HttpServletRequest request) {
		return goodsManageService.updateGoodsInventory(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改商品价格
	 * 
	 * @return
	 */
	@RequestMapping("updateGoodsPRICE")
	@ResponseBody
	public int updateGoodsPRICE(HttpServletRequest request) {
		return goodsManageService.updateGoodsPRICE(RequestUtils.requestToMap(request));
	}

	/**
	 * 修改商品状态
	 */
	@RequestMapping("updateGoodsState")
	@ResponseBody
	public int updateGoodsState(HttpServletRequest request) {
		return goodsManageService.updateGoodsState(RequestUtils.requestToMap(request));
	}

	/**
	 * 查看商品详情
	 */
	@RequestMapping("queryGoodsInfo")
	public ModelAndView queryGoodsInfo(ModelAndView mav, String storeId, String goodsId) {
		mav.addObject("storeId", storeId);
		mav.addObject("goodsInfo", goodsManageService.queryGoodsInfo(goodsId));
		mav.setViewName("/system/merchants/goodsManage/goodsInfo");
		return mav;
	}

	/**
	 * 修改商品排序
	 */
	@RequestMapping("updateGoodsSort")
	@ResponseBody
	public int updateGoodsSort(HttpServletRequest request) {
		return goodsManageService.updateGoodsSort(RequestUtils.requestToMap(request));
	}

	/**
	 * 跳转编辑页面
	 */
	@RequestMapping("toUpdategoodsHtml")
	public ModelAndView toUpdategoodsHtml(ModelAndView mav, String storeId, String goodsId) {
		mav.addObject("storeId", storeId);
		mav.addObject("classifications", goodsManageService.getOneClassifications(storeId));
		Map<String, Object> goodsInfo = goodsManageService.queryGoodsInfo(goodsId);
		String[] classifications = goodsInfo.get("classification").toString().split(">>");
		mav.addObject("class1", classifications[0]);
		mav.addObject("class2", classifications[1]);
		mav.addObject("class3", classifications[2]);
		mav.addObject("goodsInfo", goodsInfo);
		mav.setViewName("/system/merchants/goodsManage/updategoods");
		return mav;
	}

	/**
	 * 跳转添加商品页面
	 */
	@RequestMapping("toAddgoodsHtml")
	public ModelAndView toAddgoodsHtml(ModelAndView mav, String storeId) {
		mav.addObject("storeId", storeId);
		// 查询商品分类
		mav.addObject("classifications", goodsManageService.getOneClassifications(storeId));
		mav.setViewName("/system/merchants/goodsManage/addgoods");
		return mav;
	}

	/**
	 * 商品分类联动
	 */
	@RequestMapping("getClassList")
	@ResponseBody
	public List<Map<String, Object>> getClassList(String pid, String storeId) {
		return goodsManageService.getClassifications(pid, storeId);
	}

	/**
	 * 商品图片上传
	 */
	@RequestMapping("uploadGoodsPhoto")
	@ResponseBody
	public synchronized Map<String, Object> uploadGoodsPhoto(MultipartFile file) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = UploadFileUtils.uploadFile(file, "goods");
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("src", path);
			data.put("title", "图片");
			map.put("msg", "");
			map.put("code", 0);
			map.put("data", data);
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取消添加商品操作，删除上传到云端的商品图片
	 */
	@RequestMapping("CancelAddGoods")
	@ResponseBody
	public ModelAndView CancelAddGoods(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		String goodsId = map.get("goodsId").toString();
		String goodsphotos = map.get("goodsphotos").toString();
		// 删除上传到云端的图片
		if (null != goodsphotos && (!goodsphotos.equals(""))) {
			String[] paths = goodsphotos.split(";");
			for (String path : paths) {
				UploadFileUtils.deleteFile(path);
			}
		}
		// 删除添加到属性表中的属性
		if (null != goodsId && (!goodsId.equals(""))) {
			goodsManageService.deleteAttributeByGoodsId(goodsId);
		}
		return toGoodsManageListHtml(mav, request);
	}

	/**
	 * 新添加商品获取商品属性list，首次为空
	 */
	@RequestMapping("goodsAttributeList")
	@ResponseBody
	public Map<String, Object> goodsAttributeList(String goodsId, String addAttributes, String delattributes) {
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

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("msg", "");
		map.put("code", 0);
		if (null != goodsId && goodsId != "") {
			List<Map<String, Object>> list = goodsManageService.goodsAttributeList(goodsId);
			// 预用户添加的属性
			if (null != addattrs) {
				for (int i = 0; i < addattrs.size(); i++) {
					Map<String, Object> addattr = new HashMap<String, Object>();
					addattr.put("ATTRIBUTE_ID", UUIDUtils.randomID());
					addattr.put("ATTRIBUTE_NAME", addattrs.get(i)[0]);
					addattr.put("ATTRIBUTE_VALUE", addattrs.get(i)[1]);
					list.add(addattr);
				}
			}
			// 预删除用户删除掉的属性
			if (null != delattrs) {
				int j = list.size();
				int m = delattrs.size();
				for (int i = 0; i < j; i++) {
					String attrId = list.get(i).get("ATTRIBUTE_ID").toString();
					String attrName = list.get(i).get("ATTRIBUTE_NAME").toString();
					String attrValue = list.get(i).get("ATTRIBUTE_VALUE").toString();
					for (int n = 0; n < m; n++) {
						if (delattrs.get(n)[0].equals(attrId)
								|| (delattrs.get(n)[1].equals(attrName) && delattrs.get(n)[2].equals(attrValue))) {
							list.remove(i);
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
			map.put("data", list);
			map.put("count", list.size());
		} else {
			map.put("data", null);
			map.put("count", 0);
		}
		return map;
	}

	/**
	 * 店家添加商品属性
	 */
	@RequestMapping("addAttribute")
	@ResponseBody
	public String addAttribute(String goodsId, String attributeName, String attributeValue) {
		return goodsManageService.addAttribute(goodsId, attributeName, attributeValue);
	}

	/**
	 * 删除商品属性
	 */
	@RequestMapping("deleteAttribute")
	@ResponseBody
	public int deleteAttribute(String attributeId) {
		goodsManageService.deleteAttribute(attributeId);
		return 1;
	}

	/**
	 * 添加商品,添加成功跳转商品待审核list页面
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("addgoods")
	public ModelAndView addgoods(ModelAndView mav, String DETAILS, HttpServletRequest request) {
		Map<String, Object> map = RequestUtils.requestToMap(request);
		String head = "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,user-scalable=no\"/>"
				+ "<style>img{max-width:100%;width:auto;height:auto;}</style></head><body>" + DETAILS
				+ "</body></html>";
		map.put("DETAILS", head);
		goodsManageService.addgoods(map);
		return toGoodsManageListHtml(mav, request);
	}

	/**
	 * 获取商品图片list
	 */
	@RequestMapping("goodsPhotoList")
	@ResponseBody
	public Map<String, Object> goodsPhotoList(String goodsId) {
		return goodsManageService.goodsPhotoList(goodsId);
	}

	/**
	 * 商品编辑页面删除商品图片
	 */
	@RequestMapping("deletephoto")
	@ResponseBody
	public int deletephoto(String PHOTO_ID) {
		goodsManageService.deletephoto(PHOTO_ID);
		return 1;
	}

	/**
	 * 修改商品信息
	 */
	@RequestMapping("updateGoodssss")
	public ModelAndView updateGoods(ModelAndView mav, String DETAILS, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		String head = "<html><head><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,user-scalable=no\"/>"
				+ "<style>img{max-width:100%;width:auto;height:auto;}</style></head><body>" + DETAILS
				+ "</body></html>";
		map.put("DETAILS", head);
		goodsManageService.updateGoods(map);
		return toGoodsManageListHtml(mav, request);
	}

	/**
	 * 取消修改商品，返回商品管理主页
	 */
	@RequestMapping("cancelUpdateGoods")
	public ModelAndView cancelUpdateGoods(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		// 删除上传到云端的图片
		if (null != map.get("goodsphotos") && (!map.get("goodsphotos").equals(""))) {
			String[] paths = map.get("goodsphotos").toString().split(";");
			for (String path : paths) {
				UploadFileUtils.deleteFile(path);
			}
		}
		return toGoodsManageListHtml(mav, request);
	}
}
