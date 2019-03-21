package com.example.elephantshopping.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.example.elephantshopping.service.LoginService;
import com.example.elephantshopping.utils.CookiesUtils;

/**
 * 用户登陆
 * 
 * @author ydh
 *
 */
@Controller
@RequestMapping("login")
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private LoginService loginService;
	@Autowired
	private SessionDAO sessionDao;

	/**
	 * 跳转登陆页面
	 * 
	 * @return
	 */
	@RequestMapping("login")
	public ModelAndView tologin(ModelAndView mav) {
		mav.setViewName("/login");
		return mav;
	}

	/**
	 * 登出地址
	 */
	@RequestMapping("outlogin")
	public ModelAndView outlogin(ModelAndView mav, String msg) {
		SecurityUtils.getSubject().logout();
		mav.addObject("msg", msg);
		mav.setViewName("/login");
		return mav;
	}

	/**
	 * 账户未登录，或登录过时
	 */
	@RequestMapping("tologin")
	public String tologin() {
		return "/tologin";
	}

	/**
	 * 验证账户是否超时
	 */
	@RequestMapping("isTimeout")
	@ResponseBody
	public int isTimeout() {
		return 1;
	}

	/**
	 * 查询用户是否已经在线
	 */
	@RequestMapping("isAlreadyOnline")
	@ResponseBody
	public int isAlreadyOnline(String phone, String password, HttpServletRequest request) {
		Collection<Session> sessions = sessionDao.getActiveSessions();
		for (Session session : sessions) {
			String loginUsername = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));// 获得session中已经登录用户的名字
			if (phone.equals(loginUsername) && (!session.getId().equals(request.getSession().getId()))) { // 这里的username也就是当前登录的username
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 判断账号密码是否正确，正确则获取权限菜单跳转主页，错误返回登陆页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("userlogin")
	public ModelAndView userlogin(String PHONE, String PASSWORD, HttpServletRequest request,
			HttpServletResponse response) {
		if (null != PHONE && (!PHONE.equals(""))) {
			UsernamePasswordToken token = new UsernamePasswordToken(PHONE, PASSWORD, true, request.getLocalAddr());
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(token);
				Collection<Session> sessions = sessionDao.getActiveSessions();
				for (Session session : sessions) {
					String loginUsername = String
							.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));// 获得session中已经登录用户的名字
					if (PHONE.equals(loginUsername) && (!session.getId().equals(request.getSession().getId()))) { // 这里的username也就是当前登录的username
						sessionDao.delete(session);
					}
				}
				return getMenu(new ModelAndView(), PHONE, response);
			} catch (Exception e) {
				return outlogin(new ModelAndView(), "用户名或密码错误，登陆失败！");
			}
		} else {
			return outlogin(new ModelAndView(), "");
		}

	}

	/**
	 * 根据用户名获取菜单
	 * 
	 * @param mav
	 * @param NICKNAME
	 * @return
	 */
	@RequestMapping("getMenu")
	public ModelAndView getMenu(ModelAndView mav, String PHONE, HttpServletResponse response) {
		List<Map<String, Object>> list = loginService.getMenu(PHONE);
		if(list.size()>0) {
			Map<String, Object> userInfo = loginService.getUserInfo(PHONE);
			String userId = userInfo.get("USERS_ID").toString();
			CookiesUtils.addCookies(response, "userId", userId);
			CookiesUtils.addCookies(response, "userPhone", PHONE);
			// 获取用户头像和名称
			mav.addObject("userInfo", userInfo);
			mav.addObject("list", list);
			mav.setViewName("/index");
			return mav;
		}else {
			return outlogin(new ModelAndView(), "该用户无权限登陆系统");
		}
	}
}
