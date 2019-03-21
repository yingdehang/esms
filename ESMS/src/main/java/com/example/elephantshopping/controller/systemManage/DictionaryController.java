package com.example.elephantshopping.controller.systemManage;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.elephantshopping.service.systemManage.DictionaryService;
import com.example.elephantshopping.service.systemManage.PermissionsService;
import com.example.elephantshopping.utils.RequestUtils;

/**
 * 字典管理
 * 
 * @author YDH
 *
 */
@Controller
@RequestMapping("dictionary")
public class DictionaryController {
	@Autowired
	private PermissionsService permissionsService;
	@Autowired
	private DictionaryService dictionaryService;

	/**
	 * 根据角色id和菜单id查询该用户在本菜单中的权限，跳转字典管理页面
	 * 
	 * @param mav
	 * @param request
	 * @return
	 */
	@RequestMapping("todictionary")
	public ModelAndView toDictionaryHtml(ModelAndView mav, HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		Map m = permissionsService.queryPermission(map);
		mav.addObject("update", m.get("update"));
		mav.addObject("add", m.get("add"));
		mav.addObject("delete", m.get("delete"));
		mav.addObject("query", m.get("query"));
		mav.setViewName("/system/systemManage/dictionaryManage/dictionaryList");
		return mav;
	}

	/**
	 * 获取字典list
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("getDictionaryList")
	@ResponseBody
	public Map getDictionaryList(HttpServletRequest request) {
		Map map = RequestUtils.requestToMap(request);
		return dictionaryService.getDictionaryList(map);
	}

	/**
	 * 删除字典
	 * 
	 * @param DICTIONARY_ID
	 * @return
	 */
	@RequestMapping("deleteDictionaryById")
	@ResponseBody
	public int deleteDictionaryById(String DICTIONARY_ID) {
		dictionaryService.deleteDictionaryById(DICTIONARY_ID);
		return 1;
	}

	/**
	 * 返回父菜单按钮查询父菜单的PID
	 * 
	 * @param PID
	 * @return
	 */
	@RequestMapping("queryDictionaryParentId")
	@ResponseBody
	public String queryDictionaryParentId(String PID) {
		return dictionaryService.queryDictionaryParentId(PID);
	}

	/**
	 * 添加字典
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("addDictionary")
	@ResponseBody
	public int addDictionary(HttpServletRequest request) {
		dictionaryService.addDictionary(RequestUtils.requestToMap(request));
		return 1;
	}

	/**
	 * 修改字典
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("updateDictionary")
	@ResponseBody
	public int updateDictionary(HttpServletRequest request) {
		return dictionaryService.updateDictionary(RequestUtils.requestToMap(request));
	}
}
