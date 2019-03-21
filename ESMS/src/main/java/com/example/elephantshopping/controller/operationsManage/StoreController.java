package com.example.elephantshopping.controller.operationsManage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.StoreService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 店铺管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("store")
public class StoreController
{
	@Autowired
	private StoreService storeService;
	
	/**
	 * 转到店铺列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toStoreList")
	public ModelAndView toStoreList(ModelAndView modelAndView)
	{
		List<Map<String,Object>> storeStates = storeService.getStoreStatesList();
		modelAndView.addObject("storeStates",storeStates);
		modelAndView.setViewName("/system/operationsManage/storeManage/storeList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询店铺数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getStoreList")
	@ResponseBody
	public Map<String,Object> getStoreList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> storeList = storeService.getStoreList(parameters);//未分页的数据List
		List<Map<String,Object>> storeListPage = storeService.getStoreListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",storeListPage);
		map.put("count",storeList.size());
		return map;
	}
	
	/**
	 * 获取店铺分类信息以及店铺状态信息并转到添加店铺页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(ModelAndView modelAndView)
	{
		List<Map<String,Object>> storeStates = storeService.getStoreStatesList();
		modelAndView.addObject("storeStates",storeStates);
		List<Map<String,Object>> storeClass = storeService.getStoreClassList();
		modelAndView.addObject("storeClass",storeClass);
		modelAndView.setViewName("/system/operationsManage/storeManage/storeAdd");
		return modelAndView;
	}
	
	/**
	 * 添加店铺
	 * @param modelAndView
	 * @param file
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("add")
	public ModelAndView add(ModelAndView modelAndView,@RequestParam("storeIcon") MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException
	{
		/*
		 * 保存店铺基本信息
		 */
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String storeId = UUID.randomUUID().toString().replace("-", "");
		parameters.put("storeId", storeId);
		storeService.addStore(parameters);
		
		/*
		 * 保存店铺图标
		 */
		if(!file.isEmpty())
		{
			String url = UploadFileUtils.uploadFile(file, "store");
			
			/*
			 * 保存图片信息
			 */
			String iconId = UUID.randomUUID().toString().replace("-", "");
			storeService.addPhoto(iconId,"店铺",url);
			
			//将图片Id保存到店铺表里面
			storeService.addIconId(storeId,iconId);
		}
		
		List<Map<String,Object>> storeStates = storeService.getStoreStatesList();
		modelAndView.addObject("storeStates",storeStates);
		modelAndView.setViewName("/system/operationsManage/storeManage/storeList");
		return modelAndView;
	}
	
	/**
	 * 提交信息时判读店铺名是否重复
	 * @param storeName
	 * @param storeIcon
	 * @return
	 */
	@RequestMapping("addVerify")
	@ResponseBody
	public String addVerify(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);
		if(storeService.storeNameIsExists(parameters))//判断店铺名是否存在
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}
	
	/**
	 * 获取店铺分类信息.店铺状态信息.店铺信息并转到编辑店铺页面
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toEdit")
	public ModelAndView toEdit(ModelAndView modelAndView,String storeId)
	{
		Map<String,Object> storeInfo = storeService.getStoreInfo(storeId);//获取某个店铺的信息
		if(storeInfo.get("PHOTO_URL")==null)
		{
			storeInfo.put("PHOTO_URL", "");
		}
		modelAndView.addObject("storeInfo",storeInfo);
		
		List<Map<String,Object>> storeStates = storeService.getStoreStatesList();
		modelAndView.addObject("storeStates",storeStates);
		List<Map<String,Object>> storeClass = storeService.getStoreClassList();
		modelAndView.addObject("storeClass",storeClass);
		modelAndView.setViewName("/system/operationsManage/storeManage/storeEdit");
		return modelAndView;
	}
	
	
	/**
	 * 修改店铺数据
	 * @param modelAndView
	 * @param file
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("edit")
	public ModelAndView edit(ModelAndView modelAndView,@RequestParam("storeIcon") MultipartFile file,HttpServletRequest request) throws IllegalStateException, IOException
	{
		/*
		 * 修改店铺基本信息
		 */
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String storeId = (String) parameters.get("storeId");
		storeService.editStore(parameters);
		
		/*
		 * 修改店铺图标
		 */
		if(!file.isEmpty())
		{
			Map<String,Object> storeInfo = storeService.getStoreInfo(storeId);//获取店铺的图标信息
			if(storeInfo.get("PHOTO_ID")!=null)
			{
				String iId = (String) storeInfo.get("PHOTO_ID");
				String iUrl = (String) storeInfo.get("PHOTO_URL");
				/*
				 * 删除原来的图片
				 */
				UploadFileUtils.deleteFile(iUrl);
				storeService.deletIcon(iId);//删除原来表中的图片信息
			}
			
			/*
			 * 添加新的图片
			 */
			String path = UploadFileUtils.uploadFile(file,"store");
			
			/*
			 * 保存图片信息
			 */
			String iconId = UUID.randomUUID().toString().replace("-", "");
			storeService.addPhoto(iconId,"店铺",path);
			
			//将图片Id保存到店铺表里面
			storeService.updateIconId(storeId,iconId);
		}
		
		List<Map<String,Object>> storeStates = storeService.getStoreStatesList();
		modelAndView.addObject("storeStates",storeStates);
		modelAndView.setViewName("/system/operationsManage/storeManage/storeList");
		return modelAndView;
	}
}
