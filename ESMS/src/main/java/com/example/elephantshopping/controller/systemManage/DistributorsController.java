package com.example.elephantshopping.controller.systemManage;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.systemManage.DistributorsService;

/**
 * 分享者管理
 * 
 * @author XB
 *
 */
@Controller
@RequestMapping("distributors")
public class DistributorsController {
	@Autowired
	private DistributorsService distributorsService;

	/**
	 * 跳转分享管理菜单
	 */
	@RequestMapping("toDistributorsHtml")
	public ModelAndView toDistributorsHtml(ModelAndView mav) {
		mav.setViewName("/system/systemManage/distributors/distributorsList");
		return mav;
	}

	/**
	 * 获取分享管理菜单
	 */
	@RequestMapping("getDistributorslist")
	@ResponseBody
	public Map<String, Object> getDistributorslist(String phone) {
		return distributorsService.getDistributorslist(phone);
	}

	/**
	 * 验证用户手机号是否存在
	 */
	@RequestMapping("phoneishave")
	@ResponseBody
	public int phoneishave(String phone) {
		return distributorsService.phoneishave(phone);
	}

	/**
	 * 修改用户邀请人
	 */
	@RequestMapping("updatedistributors")
	@ResponseBody
	public int updatedistributors(String phone, String INVITATION) {
		int i = phoneishave(INVITATION);
		if (i == 0) {
			return i;
		}
		return distributorsService.updatedistributors(phone, INVITATION);
	}

	/**
	 * 修改用户邀请人
	 */
	@RequestMapping("cleariNVITATION")
	@ResponseBody
	public int cleariNVITATION(String phone, String INVITATION) {
		return distributorsService.updatedistributors(phone, INVITATION);
	}

}
