package com.example.elephantshopping.controller.operationsManage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.operationsManage.StoreService;
import com.example.elephantshopping.service.operationsManage.StoreVerifyService;
import com.example.elephantshopping.utils.RequestUtils;
import com.example.elephantshopping.utils.UploadFileUtils;

/**
 * 店铺认证管理Controller
 * @author XB
 *
 */
@Controller
@RequestMapping("storeVerify")
public class StoreVerifyController 
{
	@Autowired
	private StoreVerifyService storeVerifyService;
	@Autowired
	private StoreService storeService;
	
	/**
	 * 转到店铺认证列表页面
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping("toStoreVerifyList")
	public ModelAndView toStoreVerifyList(ModelAndView modelAndView)
	{
		List<Map<String,Object>> verifyStates = storeVerifyService.getVerifyStates();//获取店铺认证状态
		modelAndView.addObject("verifyStates",verifyStates);
		List<Map<String,Object>> storeGrade = storeVerifyService.getStoreGradeList();//获取店铺等级
		modelAndView.addObject("storeGrade",storeGrade);
		List<Map<String,Object>> storeType = storeVerifyService.getStoreTypeList();//获取店铺类型
		modelAndView.addObject("storeType",storeType);
		List<Map<String,Object>> type = storeVerifyService.getTypeList();//获取类型
		modelAndView.addObject("type",type);
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/storeVerifyList");
		return modelAndView;
	}
	
	/**
	 * 获取/查询店铺数据
	 * @param request
	 * @return
	 */
	@RequestMapping("getStoreVerifyList")
	@ResponseBody
	public Map<String,Object> getStoreVerifyList(HttpServletRequest request)
	{
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		Map<String,Object> map = new HashMap<String,Object>();//传给页面的Map
		int page = Integer.parseInt(parameters.get("page").toString());
		int limit = Integer.parseInt(parameters.get("limit").toString());
		parameters.put("which", (page-1)*limit);
		parameters.put("limit", limit);
		List<Map<String,Object>> storeVerifyList = storeVerifyService.getStoreVerifyList(parameters);//未分页的数据List
		List<Map<String,Object>> storeVerifyListPage = storeVerifyService.getStoreVerifyListPage(parameters);//分页的数据List
		map.put("code",0);
		map.put("msg","");
		map.put("data",storeVerifyListPage);
		map.put("count",storeVerifyList.size());
		return map;
	}
	
	/**
	 * 进行店铺认证
	 * @param request
	 * @return
	 */
	@RequestMapping("toVerify")
	@ResponseBody
	public int toVerify(HttpServletRequest request)
	{
		Map<String,Object> parameters = new RequestUtils().requestToMap(request);//将页面传来的参数封装成一个Map
		storeVerifyService.toVerify(parameters);
		return 0;
	}
	
	/**
	 * 获取店铺类型信息.店铺信息并转到编辑店铺认证信息页面
	 * @param modelAndView
	 * @param storeId
	 * @return
	 */
	@RequestMapping("toEdit")
	public ModelAndView toEdit(ModelAndView modelAndView,String storeVerifyId)
	{
		Map<String,Object> storeVerifyInfo = storeVerifyService.getStoreVerifyInfo(storeVerifyId);//获取某个店铺的信息
		if(storeVerifyInfo.get("BUSINESS_LICENSE")==null)
		{
			storeVerifyInfo.put("BUSINESS_LICENSE", "");
		}
		if(storeVerifyInfo.get("ID_CARD")==null)
		{
			storeVerifyInfo.put("ID_CARD", "");
		}
		if(storeVerifyInfo.get("HEAD_PHOTO")==null)
		{
			storeVerifyInfo.put("HEAD_PHOTO", "");
		}
		if(storeVerifyInfo.get("PHONE")==null)
		{
			storeVerifyInfo.put("PHONE", "");
		}
		modelAndView.addObject("storeVerifyInfo",storeVerifyInfo);
		
		List<Map<String,Object>> province  = storeVerifyService.getSubordinateArea(1);//获取所有的省
		modelAndView.addObject("province", province);
		List<Map<String,Object>> storeGrade = storeVerifyService.getStoreGradeList();//获取店铺等级
		modelAndView.addObject("storeGrade",storeGrade);
		List<Map<String,Object>> storeType = storeVerifyService.getStoreTypeList();//获取店铺类型
		modelAndView.addObject("storeType",storeType);
		List<Map<String,Object>> type = storeVerifyService.getTypeList();//获取类型
		modelAndView.addObject("type",type);
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/storeVerifyEdit");
		return modelAndView;
	}
	
	/**
	 * 修改店铺认证信息
	 * @param modelAndView
	 * @param file
	 * @param request
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@RequestMapping("edit")
	public ModelAndView edit(ModelAndView modelAndView,@RequestParam("idCard") MultipartFile idCard,@RequestParam("headPhoto") MultipartFile headPhoto,@RequestParam("license") MultipartFile license,HttpServletRequest request) throws IllegalStateException, IOException
	{
		/*
		 * 修改店铺认证基本信息
		 */
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String longitude = (String) parameters.get("longitude");
		String latitude = (String) parameters.get("latitude");
		parameters.put("longitude", longitude.substring(longitude.indexOf(":")+1,longitude.length()));
		parameters.put("latitude", latitude.substring(latitude.indexOf(":")+1,latitude.length()));
		String storeVerifyId = (String) parameters.get("storeVerifyId");
		storeVerifyService.editStoreVerify(parameters);
		
		Map<String,Object> storeVerifyPhotoInfo = storeVerifyService.getStoreVerifyPhotoInfo(storeVerifyId);//获取店铺认证的图片信息
		/*
		 * 修改身份证
		 */
		if(!idCard.isEmpty())
		{
			if(storeVerifyPhotoInfo!=null)
			{
				if(null!=storeVerifyPhotoInfo.get("iId")&&(!storeVerifyPhotoInfo.get("iId").equals("")))
				{
					String iId = (String) storeVerifyPhotoInfo.get("iId");
					String iUrl = (String) storeVerifyPhotoInfo.get("iUrl");
					/*
					 * 删除原来的图片
					 */
					UploadFileUtils.deleteFile(iUrl);
					storeService.deletIcon(iId);//删除原来表中的图片信息
				}
			}
			
			/*
			 * 添加新的图片
			 */
			String idCardPath = UploadFileUtils.uploadFile(idCard,"store");
			/*
			 * 保存图片信息
			 */
			String photoId = UUID.randomUUID().toString().replace("-", "");
			storeService.addPhoto(photoId,"店铺",idCardPath);
			
			//将身份证Id保存到店铺认证表里面
			storeVerifyService.updateIdCard(storeVerifyId,photoId);
		}
		/*
		 * 修改门头照片
		 */
		if(!headPhoto.isEmpty())
		{
			if(storeVerifyPhotoInfo!=null)
			{
				if(null!=storeVerifyPhotoInfo.get("hId")&&(!storeVerifyPhotoInfo.get("hId").equals("")))
				{
					String hId = (String) storeVerifyPhotoInfo.get("hId");
					String hUrl = (String) storeVerifyPhotoInfo.get("hUrl");
					/*
					 * 删除原来的图片
					 */
					UploadFileUtils.deleteFile(hUrl);
					storeService.deletIcon(hId);//删除原来表中的图片信息
				}
			}
			
			/*
			 * 添加新的图片
			 */
			String headPath = UploadFileUtils.uploadFile(headPhoto,"store");
			
			/*
			 * 保存图片信息
			 */
			String photoId = UUID.randomUUID().toString().replace("-", "");
			storeService.addPhoto(photoId,"店铺",headPath);
			
			//将门头照片Id保存到店铺认证表里面
			storeVerifyService.updateHeadPhoto(storeVerifyId,photoId);
		}
		/*
		 * 修改营业执照
		 */
		if(!license.isEmpty())
		{
			if(storeVerifyPhotoInfo!=null)
			{
				if(null!=storeVerifyPhotoInfo.get("lId")&&(!storeVerifyPhotoInfo.get("lId").equals("")))
				{
					String lId = (String) storeVerifyPhotoInfo.get("lId");
					String lUrl = (String) storeVerifyPhotoInfo.get("lUrl");
					/*
					 * 删除原来的图片
					 */
					UploadFileUtils.deleteFile(lUrl);
					storeService.deletIcon(lId);//删除原来表中的图片信息
				}
			}
			
			/*
			 * 添加新的图片
			 */
			String licensePath = UploadFileUtils.uploadFile(license, "store");
			
			/*
			 * 保存图片信息
			 */
			String photoId = UUID.randomUUID().toString().replace("-", "");
			storeService.addPhoto(photoId,"店铺",licensePath);
			
			//将营业执照Id保存到店铺认证表里面
			storeVerifyService.updateLicense(storeVerifyId,photoId);
		}
		List<Map<String,Object>> verifyStates = storeVerifyService.getVerifyStates();//获取店铺认证状态
		modelAndView.addObject("verifyStates",verifyStates);
		List<Map<String,Object>> storeGrade = storeVerifyService.getStoreGradeList();//获取店铺等级
		modelAndView.addObject("storeGrade",storeGrade);
		List<Map<String,Object>> storeType = storeVerifyService.getStoreTypeList();//获取店铺类型
		modelAndView.addObject("storeType",storeType);
		List<Map<String,Object>> type = storeVerifyService.getTypeList();//获取类型
		modelAndView.addObject("type",type);
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/storeVerifyList");
		return modelAndView;
	}
	
	/**
	 * 转到添加店铺认证页面
	 * @param modelAndView
	 * @param storeVerifyId
	 * @return
	 */
	@RequestMapping("toAdd")
	public ModelAndView toAdd(ModelAndView modelAndView,String storeVerifyId)
	{
		List<Map<String,Object>> province  = storeVerifyService.getSubordinateArea(1);//获取所有的省
		modelAndView.addObject("province", province);
		List<Map<String,Object>> storeGrade = storeVerifyService.getStoreGradeList();//获取店铺等级
		modelAndView.addObject("storeGrade",storeGrade);
		List<Map<String,Object>> storeType = storeVerifyService.getStoreTypeList();//获取店铺类型
		modelAndView.addObject("storeType",storeType);
		List<Map<String,Object>> type = storeVerifyService.getTypeList();//获取类型
		modelAndView.addObject("type",type);
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/storeVerifyAdd");
		return modelAndView;
	}
	
	/**
	 * 添加认证信息时判断此店铺ID是否存在并判断此店铺ID对应的认证信息是否存在
	 * @param storeId
	 * @return
	 */
	@RequestMapping("addVerify")
	@ResponseBody
	public String addVerify(String storeId)
	{
		if(storeVerifyService.storeIsExists(storeId))//判断此ID对应的店铺是否存在
		{
			if(storeVerifyService.storeVerifyIsExists(storeId))//判断此店铺ID对应的认证信息是否存在
			{
				return "此店铺ID对应的认证信息已存在";
			}
			else
			{
				return "permit";
			}
		}
		else
		{
			return "此店铺不存在";
		}
	}
	
	@RequestMapping("add")
	public ModelAndView add(ModelAndView modelAndView,@RequestParam("idCard") MultipartFile idCard,@RequestParam("headPhoto") MultipartFile headPhoto,@RequestParam("license") MultipartFile license,HttpServletRequest request) throws IOException
	{
		/*
		 * 添加店铺认证基本信息
		 */
		Map<String,Object> parameters = RequestUtils.requestToMap(request);//将页面传来的参数封装成一个Map
		String storeVerifyId = UUID.randomUUID().toString().replace("-", "");
		String longitude = (String) parameters.get("longitude");
		String latitude = (String) parameters.get("latitude");
		parameters.put("longitude", longitude.substring(longitude.indexOf(":")+1,longitude.length()));
		parameters.put("latitude", latitude.substring(latitude.indexOf(":")+1,latitude.length()));
		parameters.put("storeVerifyId", storeVerifyId);
		storeVerifyService.addStoreVerify(parameters);
		/*
		 * 添加身份证
		 */
		if(!idCard.isEmpty())
		{
			String iId = UUID.randomUUID().toString().replace("-", "");
			String iUrl = UploadFileUtils.uploadFile(idCard,"store");
			/*
			 * 保存图片信息
			 */
			storeService.addPhoto(iId,"店铺",iUrl);
			//将身份证Id保存到店铺认证表里面
			storeVerifyService.updateIdCard(storeVerifyId,iId);
		}
		/*
		 * 添加门头照片
		 */
		if(!headPhoto.isEmpty())
		{
			String hId = UUID.randomUUID().toString().replace("-", "");
			String hUrl = UploadFileUtils.uploadFile(headPhoto,"store");
			/*
			 * 保存图片信息
			 */
			storeService.addPhoto(hId,"店铺",hUrl);
			//将身份证Id保存到店铺认证表里面
			storeVerifyService.updateHeadPhoto(storeVerifyId,hId);
		}
		/*
		 * 添加营业执照
		 */
		if(!license.isEmpty())
		{
			String lId = UUID.randomUUID().toString().replace("-", "");
			String lUrl = UploadFileUtils.uploadFile(license,"store");
			/*
			 * 保存图片信息
			 */
			storeService.addPhoto(lId,"店铺",lUrl);
			//将身份证Id保存到店铺认证表里面
			storeVerifyService.updateLicense(storeVerifyId,lId);
		}
		
		List<Map<String,Object>> verifyStates = storeVerifyService.getVerifyStates();//获取店铺认证状态
		modelAndView.addObject("verifyStates",verifyStates);
		List<Map<String,Object>> storeGrade = storeVerifyService.getStoreGradeList();//获取店铺等级
		modelAndView.addObject("storeGrade",storeGrade);
		List<Map<String,Object>> storeType = storeVerifyService.getStoreTypeList();//获取店铺类型
		modelAndView.addObject("storeType",storeType);
		List<Map<String,Object>> type = storeVerifyService.getTypeList();//获取类型
		modelAndView.addObject("type",type);
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/storeVerifyList");
		return modelAndView;
	}
	
	/**
	 * 根据pId获得所有的下级地区
	 * @param pId
	 * @return
	 */
	@RequestMapping("getSubordinateArea")
	@ResponseBody
	public List<Map<String,Object>> getSubordinateArea(int pId)
	{
		return storeVerifyService.getSubordinateArea(pId);
	}
	
	@RequestMapping("location")
	public ModelAndView toLocation(ModelAndView modelAndView)
	{
		modelAndView.setViewName("/system/operationsManage/storeVerifyManage/location");
		return modelAndView;
	}
}
