package com.example.elephantshopping.shiro;

import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {
	private static final Logger log = LoggerFactory.getLogger(ShiroFilterFactoryBean.class);

	/**
	 * sessionDao
	 */
	@Bean(name = "sessionDao")
	public MemorySessionDAO sessionDao() {
		return new MemorySessionDAO();
	}

	/**
	 * 配置一个session管理器
	 */
	@Bean(value = "sessionManager")
	public DefaultWebSessionManager SessionManager(@Qualifier("sessionDao") MemorySessionDAO sessionDao) {
		DefaultWebSessionManager webSessionManager = new DefaultWebSessionManager();
		webSessionManager.setSessionDAO(sessionDao);
		return webSessionManager;
	}

	/**
	 * 缓存管理器
	 *
	 * @return
	 */
	@Bean(value = "ehCacheManager")
	public MemoryConstrainedCacheManager ehCacheManager() {
		log.info("ehCacheManager()");
		MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
		return cacheManager;
	}

	/**
	 * 记住我管理器 cookie管理对象;
	 *
	 * @return
	 */
	@Bean(name = "cookieRememberMeManager")
	public CookieRememberMeManager rememberMeManager() {
		System.out.println("rememberMeManager()");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		return cookieRememberMeManager;
	}

	/**
	 * cookie对象;
	 *
	 * @return
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		log.info("rememberMeCookie()");
		// 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		// <!-- 记住我cookie生效时间30天（259200） ,单位秒;-->
		simpleCookie.setMaxAge(259200);
		return simpleCookie;
	}

	@Bean(name = "securityManager")
	public SecurityManager securityManager(@Qualifier("authRealm") Realm authRealm,
			@Qualifier("cookieRememberMeManager") CookieRememberMeManager cookieRememberMeManager,
			@Qualifier("sessionManager") DefaultWebSessionManager sessionManager) {
		log.info("securityManager()");
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(authRealm);
		// 设置rememberMe管理器
		securityManager.setRememberMeManager(cookieRememberMeManager);
		securityManager.setSessionManager(sessionManager);
		return securityManager;
	}

	/**
	 * realm
	 *
	 * @return
	 */
	@Bean(name = "authRealm")
	public Realm myAuthRealm(@Qualifier("ehCacheManager") MemoryConstrainedCacheManager ehCacheManager) {
		log.info("myShiroRealm()");
		Realm myAuthorizingRealm = new Realm();
		myAuthorizingRealm.setCacheManager(ehCacheManager);
		return myAuthorizingRealm;
	}

	/**
	 * 开启shiro aop注解支持. 使用代理方式;所以需要开启代码支持;Controller才能使用@RequiresPermissions
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		log.info("authorizationAttributeSourceAdvisor()");
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

	/**
	 * shiro拦截器
	 * 
	 * @param securityManager
	 * @return
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilter(@Qualifier("securityManager") SecurityManager securityManager) {
		log.info("shirFilter()");
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// 必须设置 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// 拦截器.
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("/login/login", "anon");
		map.put("/login/userlogin", "anon");
		map.put("/login/outlogin", "anon");
		map.put("/login/isAlreadyOnline", "anon");
		// map.put("/xingyundazhuanpan/**", "anon");
		map.put("/img/**", "anon");
		map.put("/js/**", "anon");
		map.put("/layui/**", "anon");
		map.put("/test/**", "anon");
		map.put("/versionUpdate/**", "anon");
		map.put("/activityclick/**", "anon");
		map.put("/share/**", "anon");
		map.put("/**", "authc");
		// 如果不设置默认会自动寻找Web工程根目录下的"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login/tologin");
		// 登录成功后要跳转的链接;
		// shiroFilterFactoryBean.setSuccessUrl("/login/getMenu");
		// 未授权界面;
		// shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(map);
		return shiroFilterFactoryBean;
	}
}
